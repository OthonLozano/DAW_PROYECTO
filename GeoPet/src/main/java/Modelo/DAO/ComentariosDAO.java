package Modelo.DAO;

import Modelo.JavaBeans.Comentarios;
import Modelo.JavaBeans.ComentariosConRelaciones;
import Modelo.JavaBeans.Usuarios;
import Connection.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Comentarios.
 * Proporciona métodos para interactuar con la tabla 'comentarios' en la base de datos,
 * incluyendo operaciones de consulta, inserción, actualización y eliminación lógica.
 *
 * Esta clase implementa el patrón DAO para separar la lógica de acceso a datos
 * de la lógica de negocio, proporcionando una interfaz limpia para las operaciones
 * relacionadas con comentarios en reportes de mascotas.
 *
 * Los comentarios están asociados a reportes específicos y usuarios, permitiendo
 * la colaboración y comunicación entre usuarios del sistema.
 */
public class ComentariosDAO {

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
     * Obtiene todos los comentarios asociados a un reporte específico con información completa del usuario.
     * Realiza un JOIN con la tabla usuarios para obtener datos completos del autor del comentario.
     *
     * La consulta incluye:
     * - Información completa del comentario
     * - Datos del usuario que escribió el comentario
     *
     * Solo retorna comentarios con estatus 'Alta' (no eliminados lógicamente).
     * Los resultados se ordenan por fecha de comentario en orden descendente
     * para mostrar los comentarios más recientes primero.
     *
     * @param reporteId ID del reporte del cual se desean obtener los comentarios
     * @return List<ComentariosConRelaciones> Lista de comentarios con información del usuario autor
     */
    public List<ComentariosConRelaciones> obtenerComentariosPorReporte(int reporteId) {
        List<ComentariosConRelaciones> comentarios = new ArrayList<>();

        // Consulta SQL con JOIN para obtener información completa del comentario y usuario
        String sql = "SELECT " +
                "c.comentarioid, c.r_usuario, c.r_reporte, c.contenido, " +
                "c.fecha_comentario, c.estatus, " +
                "u.usuarioid, u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, " +
                "u.telefono, u.email " +
                "FROM comentarios c " +
                "INNER JOIN usuarios u ON c.r_usuario = u.usuarioid " +
                "WHERE c.r_reporte = ? AND c.estatus = 'Alta' " +
                "ORDER BY c.fecha_comentario DESC";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Construcción del objeto Comentarios con datos de la consulta
                Comentarios comentario = new Comentarios();
                comentario.setComentarioID(rs.getInt("comentarioid"));
                comentario.setR_Usuario(rs.getInt("r_usuario"));
                comentario.setR_Reporte(rs.getInt("r_reporte"));
                comentario.setContenido(rs.getString("contenido"));
                comentario.setFecha_Comentario(rs.getDate("fecha_comentario"));
                comentario.setEstatus(rs.getString("estatus"));

                // Construcción del objeto Usuario autor del comentario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Creación del objeto compuesto con las relaciones
                ComentariosConRelaciones comentarioCompleto = new ComentariosConRelaciones(comentario, usuario);
                comentarios.add(comentarioCompleto);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en obtenerComentariosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return comentarios;
    }

    /**
     * Inserta un nuevo comentario en la base de datos.
     *
     * El método establece automáticamente:
     * - La fecha del comentario como la fecha actual (CURRENT_DATE)
     * - El estatus inicial como 'Alta'
     *
     * Esta función permite a los usuarios participar en discusiones
     * sobre reportes específicos, facilitando la comunicación colaborativa.
     *
     * @param comentario Objeto Comentarios con los datos a insertar
     * @return boolean true si la inserción fue exitosa, false en caso contrario
     */
    public boolean agregarComentario(Comentarios comentario) {
        String sql = "INSERT INTO comentarios (r_usuario, r_reporte, contenido, fecha_comentario, estatus) " +
                "VALUES (?, ?, ?, CURRENT_DATE, 'Alta')";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentario.getR_Usuario());
            ps.setInt(2, comentario.getR_Reporte());
            ps.setString(3, comentario.getContenido());

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en agregarComentario - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtiene un comentario específico por su ID con información completa del usuario autor.
     * Realiza un JOIN con la tabla usuarios para obtener datos completos del autor.
     *
     * Solo retorna el comentario si tiene estatus 'Alta' (no eliminado lógicamente).
     *
     * Este método es útil para operaciones de edición, validación de permisos
     * y visualización detallada de comentarios específicos.
     *
     * @param comentarioId ID único del comentario a buscar
     * @return ComentariosConRelaciones Objeto con el comentario y datos del usuario, null si no se encuentra
     */
    public ComentariosConRelaciones obtenerComentarioPorId(int comentarioId) {
        String sql = "SELECT " +
                "c.comentarioid, c.r_usuario, c.r_reporte, c.contenido, " +
                "c.fecha_comentario, c.estatus, " +
                "u.usuarioid, u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, " +
                "u.telefono, u.email " +
                "FROM comentarios c " +
                "INNER JOIN usuarios u ON c.r_usuario = u.usuarioid " +
                "WHERE c.comentarioid = ? AND c.estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Construcción del objeto Comentarios
                Comentarios comentario = new Comentarios();
                comentario.setComentarioID(rs.getInt("comentarioid"));
                comentario.setR_Usuario(rs.getInt("r_usuario"));
                comentario.setR_Reporte(rs.getInt("r_reporte"));
                comentario.setContenido(rs.getString("contenido"));
                comentario.setFecha_Comentario(rs.getDate("fecha_comentario"));
                comentario.setEstatus(rs.getString("estatus"));

                // Construcción del objeto Usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                return new ComentariosConRelaciones(comentario, usuario);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en obtenerComentarioPorId - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return null;
    }

    /**
     * Actualiza el contenido de un comentario existente.
     *
     * Solo permite modificar el contenido del comentario, manteniendo
     * inmutables los demás campos como fecha, autor y reporte asociado.
     *
     * La operación solo se realiza si el comentario tiene estatus 'Alta',
     * evitando modificaciones en comentarios eliminados lógicamente.
     *
     * Este método es útil para correcciones o actualizaciones de contenido
     * por parte del autor original del comentario.
     *
     * @param comentario Objeto Comentarios con el contenido actualizado
     * @return boolean true si la actualización fue exitosa, false en caso contrario
     */
    public boolean editarComentario(Comentarios comentario) {
        String sql = "UPDATE comentarios SET contenido = ? WHERE comentarioid = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, comentario.getContenido());
            ps.setInt(2, comentario.getComentarioID());

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en editarComentario - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Realiza una eliminación lógica de un comentario cambiando su estatus a 'Baja'.
     *
     * Esta implementación utiliza eliminación lógica en lugar de eliminación física
     * para mantener la integridad referencial, preservar el historial de discusiones
     * y permitir auditorías futuras.
     *
     * Los comentarios eliminados lógicamente no aparecerán en las consultas
     * normales pero permanecen en la base de datos para propósitos de auditoría.
     *
     * @param comentarioId ID único del comentario a eliminar
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarComentario(int comentarioId) {
        String sql = "UPDATE comentarios SET estatus = 'Baja' WHERE comentarioid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentarioId);

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en eliminarComentario - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Verifica si un usuario específico es el propietario (autor) de un comentario.
     *
     * Esta validación es crucial para operaciones de edición y eliminación,
     * asegurando que solo el usuario que escribió el comentario pueda modificarlo.
     *
     * Solo considera comentarios con estatus 'Alta' para evitar validaciones
     * sobre comentarios ya eliminados lógicamente.
     *
     * @param comentarioId ID del comentario a verificar
     * @param usuarioId ID del usuario cuya propiedad se desea verificar
     * @return boolean true si el usuario es autor del comentario, false en caso contrario
     */
    public boolean esUsuarioPropietarioComentario(int comentarioId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM comentarios WHERE comentarioid = ? AND r_usuario = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentarioId);
            ps.setInt(2, usuarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en esUsuarioPropietarioComentario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return false;
    }

    /**
     * Cuenta el número total de comentarios activos asociados a un reporte específico.
     *
     * Solo considera comentarios con estatus 'Alta' para proporcionar
     * un conteo preciso de comentarios válidos y visibles.
     *
     * Este método es útil para generar estadísticas de participación,
     * mostrar contadores en la interfaz de usuario y validaciones
     * antes de realizar operaciones sobre reportes.
     *
     * @param reporteId ID del reporte del cual se desea contar los comentarios
     * @return int Número de comentarios activos asociados al reporte
     */
    public int contarComentariosPorReporte(int reporteId) {
        String sql = "SELECT COUNT(*) FROM comentarios WHERE r_reporte = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en contarComentariosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return 0;
    }
}