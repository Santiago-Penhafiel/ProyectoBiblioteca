package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Biblioteca {

    Scanner scan = new Scanner(System.in);
    private String nombre;
    private String direccion;
    private int numeroLibros;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Biblioteca biblioteca = new Biblioteca();
        int r = scan.nextInt();

        switch (r) {
            case 1: //gestionar libros
                r = scan.nextInt();
                switch (r) {
                    case 1: //añadir libros a mysql
                        String sql = "INSERT INTO libros (titulo, autor, anio) VALUES (?, ?, ?)";
                        Libro libro = biblioteca.nuevoLibro();
                        try(Connection conexion = MySQL.getConexion();
                                PreparedStatement stm = conexion.prepareStatement(sql)){

                            stm.setString(1, libro.getTitulo());
                            stm.setString(2, libro.getAutor());
                            stm.setInt(3, libro.getAnio());
                            stm.executeUpdate();

                        } catch (SQLException e){
                            System.out.println("Error al insertar el libro");
                            e.printStackTrace();
                        }
                        break;

                    case 2: //editar

                        break;

                    case 3: //eliminar
                        break;
                }
                break;

            case 2: //prestar o devolver
                r = scan.nextInt();
                switch (r) {
                    case 1: //prestar
                        break;

                    case 2: //devolver
                        break;
                }
                break;

            case 3: //gestionar personas
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

}








