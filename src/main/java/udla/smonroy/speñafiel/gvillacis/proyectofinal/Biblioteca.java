package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class Biblioteca {

    Scanner scan = new Scanner(System.in);
    private String nombre;
    private String direccion;
    private int numeroLibros;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Biblioteca biblioteca = new Biblioteca();
        System.out.println("Ingrese la opción para proceder");
        System.out.println("1. Agregar y eliminar libros");
        System.out.println("2. Administrar préstamos");
        System.out.println("3. ");
        System.out.println("4. Imprimir el inventario de libros");
        int r = scan.nextInt();

        switch (r) {
            case 1: //gestionar libros
                System.out.println("Ingrese la opción para proceder");
                System.out.println("1. Para añadir libros");
                System.out.println("2. Para eliminar un libro");
                r = scan.nextInt(); scan.nextLine();
                switch (r) {
                    case 1: //añadir libros a mysql
                        String sql = "INSERT INTO libros (id, titulo, autor, anio) VALUES (?, ?, ?, ?)";
                        Libro libro = biblioteca.nuevoLibro();
                        if (libro != null) {
                            try (Connection conexion = MySQL.getConexion();
                                 PreparedStatement stm = conexion.prepareStatement(sql)) {

                                stm.setInt(1, libro.getIdAleatorio(conexion));
                                stm.setString(2, libro.getTitulo());
                                stm.setString(3, libro.getAutor());
                                stm.setInt(4, libro.getAnio());
                                stm.executeUpdate();

                            } catch (SQLException e) {
                                System.out.println("Error al insertar el libro");
                                e.printStackTrace();
                            }
                        }

                        break;

                    case 2: //eliminar
                        System.out.print("Ingrese el titulo del libro que desea eliminar : ");
                        String elemento = scan.nextLine();
                        try(Connection conexion = MySQL.getConexion()){
                            MySQL.eliminarElemento(conexion, "libros", "titulo", elemento);
                        }catch (SQLException e){
                            System.out.println();
                        }
                        break;
                }
                break;

            case 2: //prestar o devolver
                System.out.println("1. Para hacer un préstamo");
                System.out.println("2. Para hacer una devolución");
                r = scan.nextInt(); scan.nextLine();
                switch (r) {
                    case 1: //prestar
                        System.out.print("Titulo del libro a ser prestado : ");
                        String elemento = scan.nextLine();
                        try(Connection conexion = MySQL.getConexion()){
                            ResultSet resultSet = MySQL.buscarPalabra(conexion, "titulo","libros", elemento);
                            System.out.println();

                            System.out.print("Ingrese el id del libro a prestar : ");
                            int id = scan.nextInt(); scan.nextLine();
                            Libro.prestar(conexion,id,0);

                        }catch (SQLException e){
                            e.printStackTrace();
                        }


                        break;

                    case 2: //devolver
                        break;
                }
                break;

            case 3: //gestionar personas
                break;

            case 4: //imprimir inventario
                try(Connection conexion = MySQL.getConexion()){
                    MySQL.imprimirTabla(conexion,"libros");
                }catch (SQLException e){
                    System.out.println();
                }
                break;

        }

    }

    public Libro nuevoLibro() {
        try {
            System.out.print("Ingrese el titulo del libro que desea añadir : ");
            String titulo = scan.nextLine();

            System.out.print("Ingrese el nombre del autor de este libro : ");
            String autor = scan.nextLine();

            System.out.print("Ingrese el año de publicación del libro : ");
            int anio = scan.nextInt();
            scan.nextLine();

            System.out.print("El libro " + titulo +
                    " se ha añadido a la lista de libros");
            return new Libro(titulo, autor, anio);

        } catch (Exception e) {
            System.out.println("Ingrese valores válidos para añadir un libro");
        }
        return null;
    }

    public Usuario nuevoUsuario() {
        try{
            System.out.print("Ingrese el nombre : ");
            String nombre = scan.nextLine();

            System.out.print("Ingrese la edad : ");
            int edad = scan.nextInt(); scan.nextLine();

            System.out.print("Ingrese el número de teléfono : ");
            int telefono = scan.nextInt(); scan.nextLine();

            System.out.println("Ingrese el numero de cédula ");
            int cedula = scan.nextInt(); scan.nextLine();

            System.out.println("Ingrese el correo : ");
            String correo = scan.nextLine();

            return new Usuario(nombre, edad, telefono, cedula, correo);

        }catch (Exception e){
            System.out.println("Ingrese valores válidos");
            return null;
        }
    }



}








