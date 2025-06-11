package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.Especie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Especies.
 * Proporciona métodos para interactuar con la tabla 'especie' en la base de datos,
 * incluyendo operaciones de consulta, inserción, actualización y eliminación lógica.
 *
 * Esta clase implementa el patrón DAO para separar la lógica de acceso a datos
 * de la lógica de negocio, proporcionando una interfaz limpia para las operaciones
 * relacionadas con la gestión de especies de mascotas en el sistema.
 *
 * Las especies definen las categorías principales de mascotas (perro, gato, etc.)
 * y son fundamentales para la clasificación y organización de reportes.
 */
public class EspecieDAO {

    /**
     * Instancia de la clase Conexion para obtener conexiones a la base de datos.
     */
    private final Conexion cn = new Conexion();

    /**
     * Objeto Connection para manejar la conexión activa a la base de datos.
     */
    private Connection con;

    /**
     * Objeto PreparedStatement para ejecutar consultas SQL parametrizadas.
     */
    private PreparedStatement ps;

    /**
     * Objeto ResultSet para manejar los resultados de las consultas SELECT.
     */
    private ResultSet rs;

    /**
     * Constructor por defecto de la clase EspecieDAO.
     * Inicializa una nueva instancia sin parámetros adicionales.
     */
    public EspecieDAO() {
        // Constructor vacío - La configuración se maneja mediante la instancia de Conexion
    }

    /**
     * Método utilitario para cerrar todos los recursos de base de datos de forma segura.
     * Cierra en orden inverso: ResultSet, PreparedStatement y Connection.
     *
     * Este método debe ser llamado en el bloque finally de cada operación
     * para evitar memory leaks y conexiones abiertas.
     */
    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("ERROR: Al cerrar recursos de base de datos - " + e.getMessage());
        }
    }

    /**
     * Registra una nueva especie en la base de datos.
     *
     * El método establece automáticamente el estatus inicial como 'Alta'.
     * Utiliza una conexión local independiente para asegurar la integridad
     * de la operación y evitar conflictos con otras transacciones concurrentes.
     *
     * Valida que la conexión esté activa antes de proceder con la inserción
     * para garantizar la fiabilidad de la operación.
     *
     * @param es Objeto Especie con los datos a insertar (nombre y descripción)
     * @return boolean true si el registro fue exitoso, false en caso contrario
     */
    public boolean RegistrarEspecie(Especie es) {
        String sql = "INSERT INTO especie (nombre, descripcion, estatus) VALUES (?, ?, 'Alta')";

        // Utilizar conexión local para asegurar independencia de la operación
        Conexion conexionLocal = new Conexion();
        Connection localCon = null;
        PreparedStatement localPs = null;

        try {
            localCon = conexionLocal.getConnection();

            // Validación de conexión activa antes de proceder
            if (localCon == null || localCon.isClosed()) {
                System.err.println("ERROR: La conexión está cerrada o es nula");
                return false;
            }

            localPs = localCon.prepareStatement(sql);
            localPs.setString(1, es.getNombre());
            localPs.setString(2, es.getDescripcion());
            localPs.executeUpdate();

            return true;

        } catch (SQLException ex) {
            System.err.println("ERROR: Al registrar especie - " + ex.getMessage());
            return false;
        } finally {
            // Cierre seguro de recursos locales
            try {
                if (localPs != null) localPs.close();
                if (localCon != null) localCon.close();
            } catch (SQLException ex) {
                System.err.println("ERROR: Al cerrar recursos locales - " + ex.getMessage());
            }
        }
    }

    /**
     * Obtiene todas las especies activas de la base de datos.
     *
     * Solo retorna especies con estatus 'Alta' (no eliminadas lógicamente).
     * Cada especie incluye su ID único, nombre y descripción completos.
     *
     * Este método es fundamental para poblar listas de selección en la interfaz
     * de usuario y para operaciones de validación y referencia cruzada.
     *
     * Implementa validación de conexión robusta para asegurar la disponibilidad
     * de datos incluso en condiciones de red inestables.
     *
     * @return List<Especie> Lista de todas las especies activas en el sistema
     */
    public List<Especie> ListarEspecies() {
        List<Especie> listaEspecies = new ArrayList<>();
        String sql = "SELECT * FROM especie WHERE estatus = 'Alta'";

        try {
            con = cn.getConnection();

            // Validación de conexión antes de proceder con la consulta
            if (con == null || con.isClosed()) {
                System.err.println("ERROR: Conexión es null o está cerrada");
                return listaEspecies;
            }

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            // Construcción de la lista de especies a partir de los resultados
            while (rs.next()) {
                Especie especie = new Especie();
                especie.setEspecieID(rs.getInt("especieid"));
                especie.setNombre(rs.getString("nombre"));
                especie.setDescripcion(rs.getString("descripcion"));
                listaEspecies.add(especie);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar especies - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return listaEspecies;
    }

    /**
     * Realiza una eliminación lógica de una especie cambiando su estatus a 'Baja'.
     *
     * Esta implementación utiliza eliminación lógica en lugar de eliminación física
     * para mantener la integridad referencial con reportes y mascotas existentes
     * que puedan estar asociados a esta especie.
     *
     * La eliminación lógica permite preservar la consistencia histórica
     * y facilita auditorías y recuperación de datos si es necesario.
     *
     * @param id ID único de la especie a eliminar lógicamente
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean EliminarEspecie(int id) {
        String sql = "UPDATE especie SET estatus = 'Baja' WHERE especieid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Al eliminar especie - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Actualiza la información de una especie existente.
     *
     * Permite modificar el nombre y descripción de una especie manteniendo
     * su ID y estatus originales. Esta funcionalidad es esencial para
     * correcciones de datos y actualizaciones de información descriptiva.
     *
     * La operación preserva todas las relaciones existentes con otras
     * entidades del sistema como reportes y mascotas asociadas.
     *
     * @param es Objeto Especie con los datos actualizados
     * @return boolean true si la modificación fue exitosa, false en caso contrario
     */
    public boolean ModificarEspecie(Especie es) {
        String sql = "UPDATE especie SET nombre = ?, descripcion = ? WHERE especieid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, es.getNombre());
            ps.setString(2, es.getDescripcion());
            ps.setInt(3, es.getEspecieID());

            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.err.println("ERROR: Al modificar especie - " + ex.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Busca y obtiene una especie específica por su ID único.
     *
     * Retorna toda la información disponible de la especie incluyendo
     * ID, nombre y descripción, independientemente de su estatus.
     *
     * Este método es útil para operaciones de detalle, edición y validación
     * donde se necesita acceso completo a los datos de una especie específica.
     *
     * Si no se encuentra la especie, retorna un objeto Especie vacío
     * en lugar de null para evitar NullPointerException en el código cliente.
     *
     * @param especieId ID único de la especie a buscar
     * @return Especie Objeto con los datos de la especie, vacío si no se encuentra
     */
    public Especie BuscarEspecie(int especieId) {
        Especie especie = new Especie();
        String sql = "SELECT * FROM especie WHERE especieid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, especieId);
            rs = ps.executeQuery();

            // Construcción del objeto Especie si se encuentra el registro
            if (rs.next()) {
                especie.setEspecieID(rs.getInt("especieid"));
                especie.setNombre(rs.getString("nombre"));
                especie.setDescripcion(rs.getString("descripcion"));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al buscar especie - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return especie;
    }
}