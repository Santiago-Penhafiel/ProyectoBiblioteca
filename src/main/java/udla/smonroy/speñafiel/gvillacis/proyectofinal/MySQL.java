package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class MySQL {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "sasa";
    private static Connection conexion =  null;

    public static Connection getConexion(){ //Establece la conexión con la base
        if (conexion == null) {
            try{
                conexion = DriverManager.getConnection(URL,USER,PASSWORD);
            } catch (SQLException e){
                System.out.println("No se pudo establecer conexión");
            }
        }
        return conexion;
    }

    public static void cerrarConexion(){ //Cierra la conexión con la base
        if(conexion != null){
            try {
                conexion.close();
            }catch (SQLException e){
                System.out.println("No se pudo cerrar la conexión");
            }
        }
    }

    public static void eliminarElemento(Connection conexion, String tabla, String columna, Object elemento){
        try  {

            String sql = "DELETE FROM " + tabla+ " WHERE " + columna + " = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);


            if (elemento instanceof String){
                preparedStatement.setString(1, (String)elemento );
            } else if (elemento instanceof Integer){
                preparedStatement.setInt(1, (Integer)elemento);
            }

            //System.out.println(sql);
            if(preparedStatement.executeUpdate() > 0){
                System.out.println("Se ha eliminado " + elemento);
            } else {
                System.out.println("No se ha encontrado " + elemento);
            }

        }catch (SQLException e){
            System.out.println("No se pudo eliminar " + elemento);
            System.out.println(e.getMessage());;
        }
    }

    public static void imprimirTabla(Connection conexion, String tabla, String excepto){
        try(Statement stm = conexion.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT * FROM " + tabla)){ //obtiene toda la tabla

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData(); // obtiene los metadatos de las columnas
            int columnas = resultSetMetaData.getColumnCount();

            int no = -1;

            if (resultSet.next()){

                for (int i = 1; i <= columnas; i++) { //imprime el encabezado de las columnas
                    String resultado = resultSetMetaData.getColumnName(i);
                    if (! resultado.equals(excepto)){
                        System.out.print(resultSetMetaData.getColumnName(i) + "\t");
                    } else {
                        no = i;
                    }
                }

                System.out.println();

                do {//imprime el contenido de las columnas
                    for (int i = 1; i <= columnas; i++) {
                        if (i != no){
                            if(tabla.equals("libros") && i == 5 && resultSet.getString(i).equals("1")){
                                System.out.print("Disponible\t");
                            } else if (tabla.equals("libros") && i == 5 && resultSet.getString(i).equals("0")) {
                                System.out.print("No disponible\t");
                            }else if(resultSet.getString(i) == null) {
                                System.out.print("-------\t");
                            } else {
                                System.out.print(resultSet.getString(i) + "\t");
                            }
                        }

                    }
                    System.out.println();
                } while (resultSet.next());
            }else {
                System.out.println("Lista vacía");
            }

            if (tabla.equals("usuarios")){
                Statement statement = conexion.createStatement();
                ResultSet resultado = statement.executeQuery("SELECT * FROM usuarios");

                ArrayList<String> cedulas = new ArrayList<>();

                while (resultado.next()){
                    cedulas.add(resultado.getString("cedula"));
                }
                boolean encabezado;
                for (int i = 0; i < cedulas.size(); i++) {
                    if (MySQL.diasPasados(conexion, cedulas.get(i)) > 0){
                        System.out.println("LOS SIGUIENTES CLIENTES TIENEN DÍAS ATRASADOS EN SU ENTREGA");
                        encabezado = (i == 0);
                        MySQL.buscar(conexion, "cedula", "usuarios", cedulas.get(i), true, null, encabezado);
                        System.out.println(MySQL.diasPasados(conexion, cedulas.get(i)) + " Días retrasado");
                    }
                }

            }


        }catch (SQLException e){
            System.out.println(e.getMessage());;

        }

    }

    public static ResultSet buscar (Connection conexion, String columna, String tabla, Object elemento, boolean imprimir, String excepto, boolean encabezado){
        String sql = "SELECT * FROM " + tabla + " WHERE " + columna + " = ?"; //se usa la tabla, columna y el elemento a buscar que se requiera
        try{
            PreparedStatement stm = conexion.prepareStatement(sql);
            //agrega el elemento para realizar la búsqueda
            if (elemento instanceof String){
                stm.setString(1, String.valueOf(elemento) );
            } else if(elemento instanceof Integer){
                stm.setInt(1, (Integer)elemento);
            }

            ResultSet resultSet =  stm.executeQuery();

            int no = 10000;
            if (imprimir && resultSet.next()){
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnas = resultSetMetaData.getColumnCount();

                if (encabezado){
                    for (int i = 1; i <= columnas; i++) {
                        if (!resultSetMetaData.getColumnName(i).equals(excepto)){
                            System.out.print(resultSetMetaData.getColumnName(i) + "\t");
                        }else {
                            no = i;
                        }

                    }
                }
                System.out.println();

                do {
                    for (int i = 1; i <= columnas; i++) {
                       if(resultSet.getString(i) == null && i%no != 0){
                           System.out.print("--------\t");
                       } else if(resultSet.getString(i) != null && i%no != 0){
                           System.out.print(resultSet.getString(i) + "\t");
                       }
                    }
                    System.out.println();
                } while (resultSet.next());
            } else {
                System.out.println("Elemento no encontrado");
                return null;
            }
            return resultSet;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static int diasPasados (Connection conexion, String cedula){
        try {
            String sql = "SELECT * FROM usuarios WHERE cedula = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, cedula);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                int id = resultSet.getInt("id");

                sql = "SELECT fecha_final_prestamo FROM libros WHERE id = ?";
                preparedStatement =conexion.prepareStatement(sql);
                preparedStatement.setInt(1, id);

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){

                    LocalDate fechaActual = LocalDate.now(); //fecha actual

                    LocalDate fecha_final_prestamo = resultSet.getObject("fecha_final_prestamo", LocalDate.class);//se obtiene la fecha última de devolución

                    return (int)ChronoUnit.DAYS.between(fecha_final_prestamo, fechaActual); //dias de diferencia entre las dos fechas

                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;

    }




}
