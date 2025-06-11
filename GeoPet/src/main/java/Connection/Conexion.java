package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para el manejo de conexiones a la base de datos PostgreSQL.
 * Implementa el patrón Singleton implícito para la configuración de conexión.
 */
public class Conexion {
    private final String url = "jdbc:postgresql://localhost:5433/ProyectoDAW";
    private final String usuario = "postgres";
    private final String password = "othon lozano 8";

    /**
     * Constructor por defecto de la clase Conexion.
     * Inicializa una nueva instancia sin parámetros adicionales.
     */
    public Conexion() {
        // Constructor vacío - La configuración se maneja mediante constantes de clase
    }

    /**
     * Establece y retorna una conexión activa a la base de datos PostgreSQL.
     *
     * Este método encapsula la lógica de conexión incluyendo:
     * - Carga explícita del driver JDBC de PostgreSQL
     * - Establecimiento de la conexión usando DriverManager
     * - Manejo de excepciones específicas para drivers y SQL
     *
     * @return Connection objeto de conexión a la base de datos, null si ocurre algún error
     * @throws ClassNotFoundException si el driver de PostgreSQL no está disponible en el classpath
     * @throws SQLException si ocurre un error durante el establecimiento de la conexión
     */
    public Connection getConnection() {
        Connection conexion = null;

        try {
            // Carga explícita del driver PostgreSQL JDBC
            // Nota: Esta línea es opcional desde JDBC 4.0+ si el driver está en el classpath
            Class.forName("org.postgresql.Driver");

            // Establecimiento de la conexión utilizando los parámetros configurados
            conexion = DriverManager.getConnection(url, usuario, password);

        } catch (ClassNotFoundException e) {
            // Manejo de excepción cuando el driver de PostgreSQL no está disponible
            System.err.println("Error: No se encontró el driver de PostgreSQL en el classpath. "
                    + "Verifique que postgresql-jdbc esté incluido en las dependencias. "
                    + "Detalle: " + e.getMessage());

        } catch (SQLException e) {
            // Manejo de excepción para errores relacionados con la base de datos
            System.err.println("Error: Fallo al establecer conexión con la base de datos. "
                    + "Verifique la configuración de red, credenciales y estado del servidor. "
                    + "Detalle: " + e.getMessage());
        }

        return conexion;
    }
}