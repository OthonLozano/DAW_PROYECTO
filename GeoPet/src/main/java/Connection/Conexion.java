package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Parámetros de conexión
    String url = "jdbc:postgresql://localhost:5433/ProyectoDAW";
    String usuario = "postgres";
    String password = "othon lozano 8";

    public Conexion() {
    }

    public Connection getConnection() {
        Connection conexion = null;

        try {
            // Registrar el driver
            Class.forName("org.postgresql.Driver");

            // Establecer la conexión
            conexion = DriverManager.getConnection(url, usuario, password);

            if (conexion != null) {
                System.out.println("Conexión exitosa a la base de datos");
            } else {
                System.out.println("Error al conectar a la base de datos");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de PostgreSQL");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error de SQL");
            e.printStackTrace();
        }

        return conexion;  // Devuelve la conexión abierta
    }
}