package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import javax.xml.transform.Source;
import java.sql.*;
import java.util.Scanner;

public class Usuario extends Persona{
    private int multa;


    public Usuario(String nombre, int edad, String numeroTelefonico, String cedula, String correo) {
        super(nombre, edad, numeroTelefonico, cedula, correo);
    }

    public static boolean imprimir(Connection conexion, String cedula){
        boolean existe = false;
        try {
            String sql = "SELECT * FROM usuarios WHERE cedula = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, cedula);
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet!=null){

                System.out.println("NO ES NULO");
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnas = resultSetMetaData.getColumnCount();
                for (int i = 1; i <= columnas ; i++) {
                    System.out.print(resultSetMetaData.getColumnName(i) + "\t");
                }
                System.out.println();

                while (resultSet.next()){

                    for (int i = 1; i <= columnas; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                    System.out.println();
                    existe = true;
                }
            }

            if (! existe){
                System.out.println("No existe usuario con ese número de cédula");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return existe;
    }

    public static void modificarUsuario(Connection conexion) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Ingrese la cédula del usuario que desea modificar: ");
        String cedula = scan.nextLine();

        String queryBuscar = "SELECT * FROM usuarios WHERE cedula = ?";
        String queryActualizar = "UPDATE usuarios SET nombre = ?, edad = ?, telefono = ?, correo = ? WHERE cedula = ?";

        try {
            PreparedStatement buscarStmt = conexion.prepareStatement(queryBuscar);
            buscarStmt.setString(1, cedula);
            ResultSet rs = buscarStmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nUsuario encontrado:");
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Edad: " + rs.getInt("edad"));
                System.out.println("Teléfono: " + rs.getString("telefono"));
                System.out.println("Correo: " + rs.getString("correo"));

                System.out.println("\nIngrese los nuevos datos del usuario:");
                System.out.print("Nuevo nombre (deje en blanco para no modificar): ");
                String nuevoNombre = scan.nextLine();
                nuevoNombre = nuevoNombre.isEmpty() ? rs.getString("nombre") : nuevoNombre;

                System.out.print("Nueva edad (deje en blanco para no modificar): ");
                String nuevaEdadStr = scan.nextLine();
                int nuevaEdad = nuevaEdadStr.isEmpty() ? rs.getInt("edad") : Integer.parseInt(nuevaEdadStr);

                System.out.print("Nuevo teléfono (deje en blanco para no modificar): ");
                String nuevoTelefono = scan.nextLine();
                nuevoTelefono = nuevoTelefono.isEmpty() ? rs.getString("telefono") : nuevoTelefono;

                System.out.print("Nuevo correo (deje en blanco para no modificar): ");
                String nuevoCorreo = scan.nextLine();
                nuevoCorreo = nuevoCorreo.isEmpty() ? rs.getString("correo") : nuevoCorreo;

                try (PreparedStatement actualizarStmt = conexion.prepareStatement(queryActualizar)) {
                    actualizarStmt.setString(1, nuevoNombre);
                    actualizarStmt.setInt(2, nuevaEdad);
                    actualizarStmt.setString(3, nuevoTelefono);
                    actualizarStmt.setString(4, nuevoCorreo);
                    actualizarStmt.setString(5, cedula);

                    int filasActualizadas = actualizarStmt.executeUpdate();
                    if (filasActualizadas > 0) {
                        System.out.println("Usuario actualizado correctamente.");
                    } else {
                        System.out.println("No se pudo actualizar el usuario.");
                    }
                }
            } else {
                System.out.println("No se encontró un usuario con la cédula ingresada.");
            }

        } catch (SQLException e) {
            System.out.println("Error al modificar el usuario: " + e.getMessage());
        }
    }

    public static void eliminarUsuario(Connection conexion){
        Scanner scan = new Scanner(System.in);
        System.out.print("Ingrese la cédula del usuario que desea eliminar: ");
        String cedula = scan.nextLine();

        String queryBuscar = "SELECT * FROM usuarios WHERE cedula = ?";
        String queryEliminar = "DELETE FROM usuarios WHERE cedula = ?";

        try {
            PreparedStatement buscarStmt = conexion.prepareStatement(queryBuscar);
            buscarStmt.setString(1, cedula);
            ResultSet rs = buscarStmt.executeQuery();
            System.out.println("CEDULA = " + cedula + " STM = " + buscarStmt.toString());

            if (rs.next()) {
                System.out.println("\nUsuario encontrado:");
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Edad: " + rs.getInt("edad"));
                System.out.println("Teléfono: " + rs.getString("telefono"));
                System.out.println("Correo: " + rs.getString("correo"));

                System.out.print("\n¿Está seguro de que desea eliminar este usuario? (s/n): ");
                String confirmacion = scan.nextLine();

                if (confirmacion.equalsIgnoreCase("s")) {
                    try (PreparedStatement eliminarStmt = conexion.prepareStatement(queryEliminar)) {
                        eliminarStmt.setString(1, cedula);

                        int filasEliminadas = eliminarStmt.executeUpdate();
                        if (filasEliminadas > 0) {
                            System.out.println("Usuario eliminado correctamente.");
                        } else {
                            System.out.println("No se pudo eliminar el usuario.");
                        }
                    }
                } else {
                    System.out.println("Operación cancelada.");
                }
            } else {
                System.out.println("No se encontró un usuario con la cédula ingresada.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public int getMulta() {
        return multa;
    }

    public void setMulta(int multa) {
        this.multa = multa;
    }
}
