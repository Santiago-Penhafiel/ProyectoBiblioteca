package udla.smonroy.speñafiel.gvillacis.proyectofinal;


import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Libro {
    Scanner scan = new Scanner(System.in);
    private boolean prestado;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(String titulo, String autor, int anio) {
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
    }

    public int getIdAleatorio(Connection conexion){ //genera un id aleatorio único de 4 dígitos
        Random random = new Random();
        int randomId = random.nextInt(9000)+1000;
        while (!esIdUnico(randomId, conexion)){
            randomId = random.nextInt(9000)+1000;
        }

        return randomId;
    }

    private boolean esIdUnico(int randomId, Connection conexion){
        try(PreparedStatement stm = conexion.prepareStatement("SELECT COUNT(*) FROM libros WHERE id = ?")){
            stm.setInt(1,randomId);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return false;
            }

        } catch (SQLException e){
            System.out.println("No es posible acceder a la base de datos");
            return false;
        }
        return true;
    }

    public static void prestar (Connection conexion, int id, int nuevo, String cedula){
        Scanner scan = new Scanner(System.in);
        //System.out.println(sql);
        try{
            String sql = "SELECT * FROM libros WHERE id = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            sql = "SELECT * FROM usuarios WHERE cedula = ?";
            PreparedStatement busquedaUsuario = conexion.prepareStatement(sql);
            busquedaUsuario.setString(1, cedula);
            ResultSet resultadoUsuario = busquedaUsuario.executeQuery();
            //System.out.println("LLEGA");
            String idUsuario = null;
            if (resultSet.next()){
                if (resultadoUsuario.next()){
                    idUsuario = resultadoUsuario.getString("id");
                }

                if(resultSet.getString("disponibilidad").equals("0")) {
                    System.out.println("El libro no se encuentra disponible");

                } else if (resultSet.getString("cedula") !=  null) {
                    System.out.println("El libro se encuentra en préstamo");

                } else if (idUsuario != null){
                    System.out.println("El usuario ya tiene un libro");
                }else {
                    sql = "UPDATE libros SET disponibilidad = ?, fecha_prestamo = CURDATE(), cedula = ? WHERE id = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setInt(1, nuevo);
                    preparedStatement.setString(2, cedula);
                    preparedStatement.setInt(3, id);

                    preparedStatement.executeUpdate();

                    System.out.print("Ingrese el número de días del préstamo : ");
                    String dias = scan.nextLine();

                    sql = "UPDATE libros SET fecha_final_prestamo = DATE_ADD(fecha_prestamo, INTERVAL ? DAY) WHERE id = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setString(1, dias);
                    preparedStatement.setInt(2, id);

                    preparedStatement.executeUpdate();

                    sql = "UPDATE usuarios SET id = ? WHERE cedula = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, cedula);

                    preparedStatement.executeUpdate();

                }
            }

        }catch (SQLException e){
            System.out.println("AQUI");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void devolver(Connection conexion, int id) {
        try  {
            // Verificar si el libro está prestado
            String sql = "SELECT * FROM libros WHERE id = ?";

            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String disponibilidad = resultSet.getString("disponibilidad");
                String cedula = resultSet.getString("cedula");

                if (disponibilidad.equals("1")) {
                    System.out.println("El libro ya está disponible en la biblioteca. No es necesario devolverlo.");
                } else {
                    // Actualizar la disponibilidad del libro
                    sql = "UPDATE libros SET disponibilidad = 1 WHERE id = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setInt(1, id);

                    preparedStatement.executeUpdate();

                    // Limpiar las fechas de préstamo
                    sql = "UPDATE libros SET fecha_prestamo = NULL, fecha_final_prestamo = NULL WHERE id = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setInt(1, id);

                    preparedStatement.executeUpdate();

                    //Liberar al usuario de la deuda
                    sql = "UPDATE usuarios SET id = NULL WHERE cedula = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setString(1, cedula);

                    preparedStatement.executeUpdate();

                    //Limpiar la columna cedula
                    sql = "UPDATE libros SET cedula = NULL WHERE id = ?";
                    preparedStatement = conexion.prepareStatement(sql);
                    preparedStatement.setInt(1, id);

                    preparedStatement.executeUpdate();


                    System.out.println("El libro ha sido devuelto correctamente.");
                }
            } else {
                System.out.println("No se encontró un libro con el ID proporcionado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al devolver el libro: " + e.getMessage());
        }
    }

    public static void modificarLibro(Connection conexion) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Ingrese el titulo del libro que desea modificar : ");
        String titulo = scan.nextLine();

        MySQL.buscar(conexion, "titulo", "libros", titulo, true, null, true);

        System.out.print("Ingrese el id del libro que desea modificar: ");
        int id = scan.nextInt(); scan.nextLine();

        String queryBuscar = "SELECT * FROM libros WHERE id = ?";
        String queryActualizar = "UPDATE libros SET titulo = ?, autor = ?, anio = ? WHERE id = ?";

        try {
            PreparedStatement buscarStmt = conexion.prepareStatement(queryBuscar);
            buscarStmt.setInt(1, id);
            ResultSet rs = buscarStmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nLibro encontrado:");
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("Autor: " + rs.getInt("autor"));
                System.out.println("Año: " + rs.getString("anio"));

                System.out.println("\nIngrese los nuevos datos del libro:");
                System.out.print("Nuevo titulo (deje en blanco para no modificar): ");
                String nuevoTitulo = scan.nextLine();
                nuevoTitulo = nuevoTitulo.isEmpty() ? rs.getString("titulo") : nuevoTitulo;

                System.out.print("Nuevo autor (deje en blanco para no modificar): ");
                String nuevoAutor = scan.nextLine();
                nuevoAutor = nuevoAutor.isEmpty() ? rs.getString("autor") : nuevoAutor;

                System.out.print("Nuevo Año (deje en blanco para no modificar): ");
                String nuevoAnioStr = scan.nextLine();
                int nuevoAnio = nuevoAnioStr.isEmpty() ? rs.getInt("anio") : Integer.parseInt(nuevoAnioStr);

                try (PreparedStatement actualizarStmt = conexion.prepareStatement(queryActualizar)) {
                    actualizarStmt.setString(1, nuevoTitulo);
                    actualizarStmt.setString(2, nuevoAutor);
                    actualizarStmt.setInt(3, nuevoAnio);
                    actualizarStmt.setInt(4, id);

                    int filasActualizadas = actualizarStmt.executeUpdate();
                    if (filasActualizadas > 0) {
                        System.out.println("Libro actualizado correctamente.");
                    } else {
                        System.out.println("No se pudo actualizar el usuario.");
                    }
                }
            } else {
                System.out.println("No se encontró un libro con el id ingresado");
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar el libro: " + e.getMessage());
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }


    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}
