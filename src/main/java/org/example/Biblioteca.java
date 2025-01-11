package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Biblioteca {

    Scanner scan = new Scanner(System.in);
    private String nombre;
    private String direccion;
    private ArrayList<Libro> libros;
    private int numeroLibros;

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        int r = scan.nextInt();

        switch (r) {
            case 1: //gestionar libros
                r = scan.nextInt();
                switch (r) {
                    case 1: //añadir
                        biblioteca.nuevoLibro(biblioteca.numeroLibros);
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

    public void nuevoLibro(int numeroLibros) {

        try {
            System.out.print("Ingrese el titulo del libro que desea añadir : ");
            String titulo = scan.nextLine();

            System.out.print("Ingrese el nombre del autor de este libro : ");
            String autor = scan.nextLine();

            System.out.print("Ingrese el año de publicación del libro : ");
            int anio = scan.nextInt();
            scan.nextLine();

            System.out.print("El libro " + titulo +
                    "con el id " + numeroLibros + 1
                    + " se ha añadido a la lista de libros");

            this.libros.add(new Libro(this.numeroLibros + 1, titulo, autor, anio));

        } catch (Exception e) {
            System.out.println("Ingrese valores válidos para añadir un libro");

        }


    }
}








