package org.example;

import java.util.Scanner;

public class Libro {
    Scanner scan = new Scanner(System.in);
    private boolean prestado;
    private int id;
    private String titulo;
    private String autor;
    private Prestamo prestamo;
    private int anio;

    public Libro(int id, String titulo, String autor, int anio) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
    }

    public Libro nuevoLibro(int numeroLibros) {

        try {
            System.out.print("Ingrese el titulo del libro que desea añadir : ");
            String titulo = scan.nextLine();

            System.out.print("Ingrese el nombre del autor de este libro : ");
            String autor = scan.nextLine();

            System.out.print("Ingrese el año de publicación del libro : ");
            int anio = scan.nextInt();
            scan.nextLine();

            System.out.print("El libro " + titulo +
                    "con el id " + numeroLibros+1
                    +" se ha añadido a la lista de libros");

            return new Libro(numeroLibros+1, titulo, autor, anio);
        } catch (Exception e){
            System.out.println("Ingrese valores válidos para añadir un libro");
            return null;
        }


    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
