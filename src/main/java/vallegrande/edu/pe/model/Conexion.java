
package vallegrande.edu.pe.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:33061/bd_incidencias";
    private static final String USER = "root";
    private static final String PASS = "nina2006";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}
