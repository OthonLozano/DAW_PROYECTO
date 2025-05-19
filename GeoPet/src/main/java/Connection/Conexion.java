package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Parámetros de conexión
    private final String url      = "jdbc:postgresql://localhost:5433/CreacionTablas";
    private final String usuario  = "postgres";
    private final String password = "mouse";

    public Conexion() { }

    public Connection getConnection() {
        Connection conexion = null;
        try {
            // Carga explícita del driver (opcional a partir de JDBC 4+ si está en el classpath)
            Class.forName("org.postgresql.Driver");
            // Aquí obtienes la conexión de verdad:
            conexion = DriverManager.getConnection(url, usuario, password);
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver de PostgreSQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error conectando a la BD: " + e.getMessage());
        }
        return conexion;
    }
}