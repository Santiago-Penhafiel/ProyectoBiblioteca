package udla.smonroy.speñafiel.gvillacis.proyectofinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "sasa";
    private static Connection conexion =  null;

    public static Connection getConexion(){ //Establece la conexión con la base
        if (conexion == null) {
            try{
                conexion = DriverManager.getConnection(URL,USER,PASSWORD);
            } catch (SQLException e){
                System.out.println("No se pudo establecer conexión");
            }
        }
        return conexion;
    }

    public static void cerrarConexion(){ //Cierra la conexión con la base
        if(conexion != null){
            try {
                conexion.close();
            }catch (SQLException e){
                System.out.println("No se pudo cerrar la conexión");
            }
        }
    }
}
