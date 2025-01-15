package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class Biblioteca {

    Scanner scan = new Scanner(System.in);

    private final String nombre;
    private final String sede;

    public Biblioteca(String nombre, String sede) {//constructor
        this.nombre = nombre;
        this.sede = sede;
    }

    public static void main(String[] args) {
        Connection conexion = MySQL.getConexion();
        Scanner scan = new Scanner(System.in);

        Biblioteca biblioteca = new Biblioteca("Gaboteca", "Valle de los Chillos" );

        System.out.println("\t\t\t\t\t" + biblioteca.getNombre().toUpperCase(Locale.ROOT));
        System.out.println("Sede : " + biblioteca.getSede());


        int r = -1;

        while (r != 0){

            System.out.println("\nIngrese la opción para proceder");
            System.out.println("1. Agregar y eliminar libros");
            System.out.println("2. Administrar préstamos");
            System.out.println("3. Administrar usuarios");
            System.out.println("4. Mostrar el inventario de libros");
            System.out.println("5. Mostrar usuarios");
            System.out.println("6. Buscador");
            System.out.println("0. Salir");
            System.out.print("Opción : ");
            r = scan.nextInt();

            switch (r) {
                case 1: //gestionar libros
                    System.out.println("Ingrese la opción para proceder");
                    System.out.println("1. Para añadir libros");
                    System.out.println("2. Para eliminar un libro");
                    System.out.println("3. Para editar un libro");
                    System.out.print("Opción : ");
                    int r1 = scan.nextInt(); scan.nextLine();
                    switch (r1) {
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
                                    MySQL.buscar(conexion, "titulo", "libros", elemento, true, null);

                                    System.out.print("Ingrese el id del libro que desea eliminar : ");
                                    int id = scan.nextInt(); scan.nextLine();

                                    MySQL.eliminarElemento(conexion, "libros", "id", id);
                                } catch (Exception e){
                                    System.out.println("Ingrese valores válidos");
                                }

                            break;

                        case 3://editar un libro
                            Libro.modificarLibro(conexion);
                            break;
                    }
                    break;

                case 2: //prestar o devolver
                    System.out.println("1. Para hacer un préstamo");
                    System.out.println("2. Para hacer una devolución");
                    System.out.print("Opción : ");
                    int r2 = scan.nextInt(); scan.nextLine();
                    switch (r2) {
                        case 1: //prestar

                            try {

                                System.out.print("Titulo del libro a ser prestado : ");
                                String elemento = scan.nextLine();
                                ResultSet resultSet = MySQL.buscar(conexion, "titulo", "libros", elemento, true, "cedula");
                                System.out.println();

                                if (resultSet != null){
                                    System.out.print("Ingrese el id del libro a prestar : ");
                                    int id = scan.nextInt();
                                    scan.nextLine();

                                    System.out.print("Ingrese el numero de cédula de la persona que requiere el préstamo : ");
                                    String cedula = scan.nextLine();

                                    int diasRetraso = MySQL.diasPasados(conexion, cedula);

                                    if (diasRetraso > 0){
                                        System.out.println("El usuario tiene un retraso de " + diasRetraso + "dias");
                                    }

                                    if(Usuario.imprimir(conexion, cedula)){
                                        Libro.prestar(conexion, id, 0, cedula);
                                    }
                                }

                            }catch (Exception e){
                                System.out.println(e.getMessage());
                                System.out.println("Ingrese valores válidos");
                                e.printStackTrace();
                            }


                            break;

                        case 2: //devolver
                            try {
                                System.out.print("Titulo del libro a devolver : ");
                                String elemento = scan.nextLine();
                                MySQL.buscar(conexion, "titulo", "libros", elemento, true, "cedula");
                                System.out.println();

                                System.out.print("Ingrese el id del libro a devolver : ");
                                int id = scan.nextInt();
                                scan.nextLine();
                                Libro.devolver(MySQL.getConexion(), id);


                            }catch (Exception e){
                                System.out.println("Ingrese valores válidos");
                            }
                            break;
                    }
                    break;

                case 3: //gestionar usuarios
                    System.out.println("1. Para agregar");
                    System.out.println("2. Para eliminar");
                    System.out.println("3. Para modificar usuario");
                    System.out.print("Opción : ");
                    int r3 = scan.nextInt(); scan.nextLine();
                    switch (r3){
                        case 1://agregar usuario
                            Usuario usuario = biblioteca.nuevoUsuario();
                            if (usuario != null){
                                String sql = "INSERT INTO usuarios (cedula, nombre, edad, telefono, correo) VALUES (?, ?, ?, ?, ?)";
                                try {

                                    PreparedStatement stm = conexion.prepareStatement(sql);

                                    ResultSet resultSet = MySQL.buscar(conexion, "cedula", "usuarios", usuario.getCedula(), false, null);
                                    boolean existe = false;
                                    if (resultSet != null){//comprueba la existencia de un usuario con la cedula
                                        while (resultSet.next()){
                                            if (resultSet.getString("cedula").equals(usuario.getCedula())){
                                                existe = true;
                                            }
                                        }
                                    }

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
                                    System.out.println(e.getMessage());
                                }
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

                case 4: //imprimir inventario
                    MySQL.imprimirTabla(conexion, "libros", "cedula");
                    break;

                case 5:  //lista de usuarios
                    MySQL.imprimirTabla(conexion, "usuarios", "id");

                    break;

            }
        }

        MySQL.cerrarConexion();
        }


    private Libro nuevoLibro() {
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

    private Usuario nuevoUsuario() {
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

    public String getNombre() {
        return nombre;
    }

    public String getSede() {
        return sede;
    }
}








