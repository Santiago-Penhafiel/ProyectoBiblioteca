package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import java.sql.*;

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

    public static void eliminarElemento(Connection conexion, String tabla, String columna, String elemento){
        try (Statement stm = conexion.createStatement()) {
            String sql = "DELETE FROM " + tabla + " WHERE " + columna + " = \"" + elemento + "\"";
            //System.out.println(sql);
            if(stm.executeUpdate(sql) > 0){
                System.out.println("Se ha eliminado " + elemento);
            } else {
                System.out.println("No se ha encontrado " + elemento);
            }

        }catch (SQLException e){
            System.out.println("No se pudo eliminar " + elemento);
            e.printStackTrace();
        }
    }

    public static void imprimirTabla(Connection conexion, String tabla){
        try(Statement stm = conexion.createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT * FROM " + tabla)){ //obtiene toda la tabla

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData(); // obtiene los metadatos de las columnas
            int columnas = resultSetMetaData.getColumnCount();
            //System.out.println("COLUMNAS " + columnas);

            for (int i = 1; i <= columnas; i++) { //imprime el encabezado de las columnas
                System.out.print(resultSetMetaData.getColumnName(i) + "\t");
            }

            System.out.println();

            while (resultSet.next()){//imprime el contenido de las columnas
                for (int i = 1; i <= columnas; i++) {
                    if(i == 5 && resultSet.getString(i).equals("1")){
                        System.out.print("Disponible\t");
                    } else if (i == 5 && resultSet.getString(i).equals("0")) {
                        System.out.print("No disponible\t");
                    }else if (i == 6 && resultSet.getString(i) == null){
                        System.out.print("No prestado");
                    } else {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                }
                System.out.println();
            }

        }catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static ResultSet buscar (Connection conexion, String columna, String tabla, Object elemento, boolean imprimir){
        String sql = "SELECT * FROM " + tabla + " WHERE " + columna + " = ?"; //se usa la tabla, columna y el elemento a buscar que se requiera
        try{
            PreparedStatement stm = conexion.prepareStatement(sql);
            //System.out.println(elemento.getClass());
            if (elemento instanceof String){
                stm.setString(1, String.valueOf(elemento) );
            } else if(elemento instanceof Integer){
                stm.setInt(1, (Integer)elemento);
            }

            ResultSet resultSet =  stm.executeQuery();
            if (imprimir){
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnas = resultSetMetaData.getColumnCount();

                for (int i = 1; i <= columnas; i++) {
                    System.out.print(resultSetMetaData.getColumnName(i) + "\t");
                }
                System.out.println();

                while (resultSet.next()){
                    for (int i = 1; i <= columnas; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                    System.out.println();
                }
            }
            return resultSet;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }




}
