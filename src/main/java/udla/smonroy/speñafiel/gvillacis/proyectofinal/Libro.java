package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import com.mysql.cj.util.EscapeTokenizer;

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

    public static void prestar (Connection conexion, int id, int nuevo){
        Scanner scan = new Scanner(System.in);
        //System.out.println(sql);
        try(Statement stm = conexion.createStatement()){
            ResultSet resultSet = stm.executeQuery("SELECT * FROM libros WHERE id = " + id);
            if(resultSet.next() && resultSet.getString("disponibilidad").equals("0")){
                System.out.println("El libro no se encuentra disponible");
            }else {
                String sql = "UPDATE libros SET disponibilidad = " + nuevo + " WHERE id = " + id;
                stm.executeUpdate(sql);
                sql = "UPDATE libros SET fecha_prestamo = CURDATE() WHERE id = " + id;
                stm.executeUpdate(sql);

                System.out.print("Ingrese el número de días del préstamo : ");
                String dias = scan.nextLine();

                sql = "UPDATE libros SET fecha_final_prestamo = DATE_ADD(fecha_prestamo, INTERVAL "+ dias + " DAY) WHERE id = " + id;
                stm.executeUpdate(sql);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void devolver(Connection conexion, int id) {
        try (Statement stm = conexion.createStatement()) {
            // Verificar si el libro está prestado
            ResultSet resultSet = stm.executeQuery("SELECT * FROM libros WHERE id = " + id);
            if (resultSet.next()) {
                String disponibilidad = resultSet.getString("disponibilidad");
                if (disponibilidad.equals("1")) {
                    System.out.println("El libro ya está disponible en la biblioteca. No es necesario devolverlo.");
                } else {
                    // Actualizar la disponibilidad del libro
                    String sql = "UPDATE libros SET disponibilidad = 1 WHERE id = " + id;
                    stm.executeUpdate(sql);

                    // Limpiar las fechas de préstamo
                    sql = "UPDATE libros SET fecha_prestamo = NULL, fecha_final_prestamo = NULL WHERE id = " + id;
                    stm.executeUpdate(sql);

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
