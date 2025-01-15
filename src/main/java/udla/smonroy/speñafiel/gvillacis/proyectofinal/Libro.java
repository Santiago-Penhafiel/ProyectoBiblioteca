package udla.smonroy.speñafiel.gvillacis.proyectofinal;


import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Libro {
    Scanner scan = new Scanner(System.in);
    private boolean prestado;
    private String titulo;
    private String autor;
    private Prestamo prestamo;
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
            String sql = "SELECT * FROM libros WHERE id = + ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next() && resultSet.getString("disponibilidad").equals("0")){
                System.out.println("El libro no se encuentra disponible");
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

            }

        }catch (SQLException e){
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


                    System.out.println("El libro ha sido devuelto correctamente.");
                }
            } else {
                System.out.println("No se encontró un libro con el ID proporcionado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al devolver el libro: " + e.getMessage());
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

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
}
