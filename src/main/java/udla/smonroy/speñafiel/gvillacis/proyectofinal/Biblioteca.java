package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import java.sql.*;
import java.util.Scanner;

public class Biblioteca {

    Scanner scan = new Scanner(System.in);
    private String nombre;
    private String direccion;
    private int numeroLibros;

    public static void main(String[] args) {
        Connection conexion = MySQL.getConexion();
        Scanner scan = new Scanner(System.in);

        int r = -1;

        while (r != 0){
            Biblioteca biblioteca = new Biblioteca();
            System.out.println("\nIngrese la opción para proceder");
            System.out.println("1. Agregar y eliminar libros");
            System.out.println("2. Administrar préstamos");
            System.out.println("3. Administrar personas");
            System.out.println("4. Imprimir el inventario de libros");
            System.out.println("0. Salir");
            r = scan.nextInt();

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
                                try {
                                    PreparedStatement stm = conexion.prepareStatement(sql);
                                    stm.setInt(1, libro.getIdAleatorio(conexion));
                                    stm.setString(2, libro.getTitulo());
                                    stm.setString(3, libro.getAutor());
                                    stm.setInt(4, libro.getAnio());
                                    stm.executeUpdate();


                                } catch (SQLException e) {
                                    System.out.println("Error al insertar el libro");
                                    System.out.println(e.getMessage());
                                }
                            }

                            break;

                        case 2: //eliminar
                            System.out.print("Ingrese el titulo del libro que desea eliminar : ");
                            String elemento = scan.nextLine();

                                try{
                                    MySQL.buscar(conexion, "titulo", "libros", elemento, true);

                                    System.out.print("Ingrese el id del libro que desea eliminar : ");
                                    int id = scan.nextInt(); scan.nextLine();

                                    MySQL.eliminarElemento(conexion, "libros", "id", id);
                                } catch (Exception e){
                                    System.out.println("Ingrese valores válidos");
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

                            try {

                                System.out.print("Titulo del libro a ser prestado : ");
                                String elemento = scan.nextLine();
                                MySQL.buscar(conexion, "titulo", "libros", elemento, true);
                                System.out.println();

                                System.out.print("Ingrese el id del libro a prestar : ");
                                int id = scan.nextInt();
                                scan.nextLine();

                                System.out.println("Ingrese el numero de cédula de la persona que requiere el préstamo : ");
                                String cedula = scan.nextLine();

                                if(Usuario.imprimir(conexion, cedula)){
                                    Libro.prestar(conexion, id, 0, cedula);
                                }

                            }catch (Exception e){
                                System.out.println(e.getMessage());
                                System.out.println("Ingrese valores válidos");
                            }


                            break;

                        case 2: //devolver
                            try {
                                System.out.print("Titulo del libro a devolver : ");
                                String elemento = scan.nextLine();
                                MySQL.buscar(conexion, "titulo", "libros", elemento, true);
                                System.out.println();

                                System.out.print("Ingrese el id del libro a devolver : ");
                                int id = scan.nextInt();
                                scan.nextLine();
                                Libro.devolver(MySQL.getConexion(), id);1


                            }catch (Exception e){
                                System.out.println("Ingrese valores válidos");
                            }
                            break;
                    }
                    break;

                case 3: //gestionar personas
                    System.out.println("1. Para gestionar usuarios");
                    System.out.println("2. Para gestionar empleados");
                    r = scan.nextInt(); scan.nextLine();
                    switch (r){
                        case 1://gestionar usuarios
                            System.out.println("1. Para agregar");
                            System.out.println("2. Para eliminar");
                            System.out.println("3. Para modificar usuario");
                            r = scan.nextInt(); scan.nextLine();
                            switch (r){
                                case 1://agregar usuario
                                    Usuario usuario = biblioteca.nuevoUsuario();
                                    String sql = "INSERT INTO usuarios (cedula, nombre, edad, telefono, correo) VALUES (?, ?, ?, ?, ?)";
                                    try {

                                         PreparedStatement stm = conexion.prepareStatement(sql);

                                        ResultSet resultSet = MySQL.buscar(conexion, "cedula", "usuarios", usuario.getCedula(), false);
                                        boolean existe = false;
                                        if (resultSet != null){
                                            while (resultSet.next()){
                                                if (resultSet.getString(1).equals(usuario.getCedula())){
                                                    existe = true;
                                                }
                                            }
                                        }

                                        //System.out.println("EXISTE : "+existe);

                                        if (!existe){
                                            stm.setString(1, usuario.getCedula());
                                            stm.setString(2, usuario.getNombre());
                                            stm.setInt(3, usuario.getEdad());
                                            stm.setString(4, usuario.getNumeroTelefonico());
                                            stm.setString(5, usuario.getCorreo());

                                            stm.executeUpdate();
                                        } else {
                                            System.out.println("El número de cédula ingresado ya existe");
                                        }


                                    } catch (SQLException e){
                                        e.printStackTrace();
                                    }

                                    break;
                                case 2://eliminar usuario
                                    Usuario.eliminarUsuario(conexion);
                                    break;

                                case 3://modificar usuario
                                    Usuario.modificarUsuario(conexion);
                                    break;

                            }
                            break;

                        case 2://gestionar empleados
                            break;
                    }
                    break;


                case 4: //imprimir inventario
                    MySQL.imprimirTabla(conexion, "libros", "cedula");
                    break;

            }
        }

        MySQL.cerrarConexion();
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
            String telefono = scan.nextLine();

            System.out.print("Ingrese el numero de cédula : ");
            String cedula = scan.nextLine();

            System.out.print("Ingrese el correo : ");
            String correo = scan.nextLine();

            return new Usuario(nombre, edad, telefono, cedula, correo);

        }catch (Exception e){
            System.out.println("Ingrese valores válidos");
            return null;
        }
    }





}








