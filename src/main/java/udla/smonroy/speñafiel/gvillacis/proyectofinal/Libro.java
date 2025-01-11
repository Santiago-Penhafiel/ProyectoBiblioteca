package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void editarTitulo (){
        System.out.print("Ingrese ");
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
