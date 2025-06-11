package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.Raza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Razas.
 * Proporciona métodos para interactuar con la tabla 'raza' en la base de datos,
 * incluyendo operaciones de consulta, inserción, actualización y eliminación lógica.
 *
 * Esta clase implementa el patrón DAO para separar la lógica de acceso a datos
 * de la lógica de negocio, proporcionando una interfaz limpia para las operaciones
 * relacionadas con la gestión de razas de mascotas en el sistema.
 *
 * Las razas proporcionan una clasificación más específica dentro de cada especie,
 * permitiendo una identificación más precisa de las mascotas reportadas. Son
 * fundamentales para mejorar la efectividad de búsquedas y coincidencias en
 * el sistema de avistamientos.
 */
public class RazaDAO {

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
     * Registra una nueva raza en la base de datos.
     *
     * El método establece automáticamente el estatus inicial como 'Alta',
     * indicando que la raza está activa y disponible para ser utilizada
     * en la clasificación de mascotas.
     *
     * Esta funcionalidad permite expandir el catálogo de razas disponibles
     * en el sistema, mejorando la precisión en la identificación de mascotas
     * y facilitando búsquedas más específicas.
     *
     * @param raza Objeto Raza con los datos a insertar (nombre y descripción)
     * @return boolean true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarRaza(Raza raza) {
        String sql = "INSERT INTO raza (nombre, descripcion, estatus) VALUES (?, ?, 'Alta')";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, raza.getNombre());
            ps.setString(2, raza.getDescripcion());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al registrar raza - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtiene todas las razas activas de la base de datos.
     *
     * Solo retorna razas con estatus 'Alta' (no eliminadas lógicamente).
     * Cada raza incluye su ID único, nombre y descripción completos.
     *
     * Este método es fundamental para poblar listas de selección en formularios
     * de registro de mascotas, filtros de búsqueda y operaciones de validación.
     * La información de razas es esencial para la correcta categorización
     * y identificación de mascotas en el sistema.
     *
     * @return List<Raza> Lista de todas las razas activas en el sistema
     */
    public List<Raza> listarRazas() {
        List<Raza> lista = new ArrayList<>();
        String sql = "SELECT * FROM raza WHERE estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            // Construcción de la lista de razas a partir de los resultados
            while (rs.next()) {
                Raza raza = new Raza();
                raza.setRazaID(rs.getInt("razaid"));
                raza.setNombre(rs.getString("nombre"));
                raza.setDescripcion(rs.getString("descripcion"));
                lista.add(raza);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar razas - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Actualiza la información de una raza existente.
     *
     * Permite modificar el nombre y descripción de una raza manteniendo
     * su ID y estatus originales. Esta funcionalidad es esencial para
     * correcciones de datos, actualizaciones de información descriptiva
     * y mejoras en la precisión de la clasificación.
     *
     * La operación preserva todas las relaciones existentes con mascotas
     * que puedan estar asociadas a esta raza, manteniendo la integridad
     * referencial del sistema.
     *
     * @param raza Objeto Raza con los datos actualizados
     * @return boolean true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarRaza(Raza raza) {
        String sql = "UPDATE raza SET nombre = ?, descripcion = ? WHERE razaID = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, raza.getNombre());
            ps.setString(2, raza.getDescripcion());
            ps.setInt(3, raza.getRazaID());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al modificar raza - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Realiza una eliminación lógica de una raza cambiando su estatus a 'Baja'.
     *
     * Esta implementación utiliza eliminación lógica en lugar de eliminación física
     * para mantener la integridad referencial con mascotas existentes que
     * puedan estar asociadas a esta raza. La eliminación lógica permite
     * preservar la consistencia histórica de los datos.
     *
     * Las razas eliminadas lógicamente no aparecerán en las listas de selección
     * para nuevas mascotas, pero las mascotas existentes mantendrán su
     * clasificación correcta para propósitos de consulta y auditoría.
     *
     * @param id ID único de la raza a eliminar lógicamente
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarRaza(int id) {
        String sql = "UPDATE raza SET estatus = 'Baja' WHERE razaID = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al eliminar raza - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Busca y obtiene una raza específica por su ID único.
     *
     * Retorna toda la información disponible de la raza incluyendo
     * ID, nombre y descripción, independientemente de su estatus.
     * Esta funcionalidad es útil para mostrar información completa
     * de razas asociadas a mascotas existentes.
     *
     * El método es esencial para operaciones de detalle, edición,
     * validación y para mostrar información histórica de mascotas
     * que puedan tener razas actualmente desactivadas.
     *
     * Si no se encuentra la raza, retorna un objeto Raza vacío
     * en lugar de null para evitar NullPointerException en el código cliente.
     *
     * @param id ID único de la raza a buscar
     * @return Raza Objeto con los datos de la raza, vacío si no se encuentra
     */
    public Raza buscarRaza(int id) {
        Raza raza = new Raza();
        String sql = "SELECT * FROM raza WHERE razaID = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            // Construcción del objeto Raza si se encuentra el registro
            if (rs.next()) {
                raza.setRazaID(rs.getInt("razaid"));
                raza.setNombre(rs.getString("nombre"));
                raza.setDescripcion(rs.getString("descripcion"));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al buscar raza - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return raza;
    }
}