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
        int r = scan.nextInt();

        switch (r) {
            case 1: //gestionar libros
                r = scan.nextInt();
                switch (r){
                    case 1: //añadir
                        break;

                    case 2: //editar
                        break;

                    case 3: //eliminar
                        break;
                }
                break;

            case 2: //prestar o devolver
                r = scan.nextInt();
                switch (r){
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



}








