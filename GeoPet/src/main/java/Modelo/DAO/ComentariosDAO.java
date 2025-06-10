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
 * DAO para gestionar operaciones CRUD de Comentarios
 */
public class ComentariosDAO {

    // Variables para la conexión
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Método para cerrar recursos de base de datos
     */
    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("ERROR: Al cerrar recursos - " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los comentarios de un reporte específico con información del usuario
     */
    public List<ComentariosConRelaciones> obtenerComentariosPorReporte(int reporteId) {
        List<ComentariosConRelaciones> comentarios = new ArrayList<>();

        String sql = "SELECT " +
                "c.comentarioid, c.r_usuario, c.r_reporte, c.contenido, " +
                "c.fecha_comentario, c.estatus, " +
                "u.usuarioid, u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, " +
                "u.telefono, u.email " +
                "FROM comentarios c " +
                "INNER JOIN usuarios u ON c.r_usuario = u.usuarioid " +
                "WHERE c.r_reporte = ? AND c.estatus = 'Alta' " +
                "ORDER BY c.fecha_comentario DESC";

        System.out.println("=== DEBUG ComentariosDAO ===");
        System.out.println("DEBUG DAO: Obteniendo comentarios para reporte ID = " + reporteId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Crear el comentario
                Comentarios comentario = new Comentarios();
                comentario.setComentarioID(rs.getInt("comentarioid"));
                comentario.setR_Usuario(rs.getInt("r_usuario"));
                comentario.setR_Reporte(rs.getInt("r_reporte"));
                comentario.setContenido(rs.getString("contenido"));
                comentario.setFecha_Comentario(rs.getDate("fecha_comentario"));
                comentario.setEstatus(rs.getString("estatus"));

                // Crear el usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Crear el objeto con relaciones
                ComentariosConRelaciones comentarioCompleto = new ComentariosConRelaciones(comentario, usuario);
                comentarios.add(comentarioCompleto);

                System.out.println("DEBUG DAO: Comentario agregado - Usuario: " + usuario.getNombre());
            }

            System.out.println("DEBUG DAO: Total comentarios obtenidos = " + comentarios.size());

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en obtenerComentariosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return comentarios;
    }

    /**
     * Agregar un nuevo comentario
     */
    public boolean agregarComentario(Comentarios comentario) {
        String sql = "INSERT INTO comentarios (r_usuario, r_reporte, contenido, fecha_comentario, estatus) " +
                "VALUES (?, ?, ?, CURRENT_DATE, 'Alta')";

        System.out.println("=== DEBUG ComentariosDAO ===");
        System.out.println("DEBUG DAO: Agregando comentario - Usuario: " + comentario.getR_Usuario() +
                ", Reporte: " + comentario.getR_Reporte());

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentario.getR_Usuario());
            ps.setInt(2, comentario.getR_Reporte());
            ps.setString(3, comentario.getContenido());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Comentario agregado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo agregar el comentario");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en agregarComentario - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtener un comentario específico por ID
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

        System.out.println("=== DEBUG ComentariosDAO ===");
        System.out.println("DEBUG DAO: Buscando comentario con ID = " + comentarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Crear el comentario
                Comentarios comentario = new Comentarios();
                comentario.setComentarioID(rs.getInt("comentarioid"));
                comentario.setR_Usuario(rs.getInt("r_usuario"));
                comentario.setR_Reporte(rs.getInt("r_reporte"));
                comentario.setContenido(rs.getString("contenido"));
                comentario.setFecha_Comentario(rs.getDate("fecha_comentario"));
                comentario.setEstatus(rs.getString("estatus"));

                // Crear el usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                System.out.println("DEBUG DAO: Comentario encontrado");
                return new ComentariosConRelaciones(comentario, usuario);
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en obtenerComentarioPorId - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        System.out.println("DEBUG DAO: Comentario no encontrado");
        return null;
    }

    /**
     * Editar un comentario existente
     */
    public boolean editarComentario(Comentarios comentario) {
        String sql = "UPDATE comentarios SET contenido = ? WHERE comentarioid = ? AND estatus = 'Alta'";

        System.out.println("=== DEBUG ComentariosDAO ===");
        System.out.println("DEBUG DAO: Editando comentario ID = " + comentario.getComentarioID());

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, comentario.getContenido());
            ps.setInt(2, comentario.getComentarioID());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Comentario editado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo editar el comentario");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en editarComentario - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Eliminar un comentario (cambiar estatus a 'Baja')
     */
    public boolean eliminarComentario(int comentarioId) {
        String sql = "UPDATE comentarios SET estatus = 'Baja' WHERE comentarioid = ?";

        System.out.println("=== DEBUG ComentariosDAO ===");
        System.out.println("DEBUG DAO: Eliminando comentario ID = " + comentarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentarioId);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Comentario eliminado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo eliminar el comentario");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en eliminarComentario - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Verificar si el usuario es propietario del comentario
     */
    public boolean esUsuarioPropietarioComentario(int comentarioId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM comentarios WHERE comentarioid = ? AND r_usuario = ? AND estatus = 'Alta'";

        System.out.println("=== DEBUG ComentariosDAO ===");
        System.out.println("DEBUG DAO: Verificando propiedad - Comentario: " + comentarioId + ", Usuario: " + usuarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, comentarioId);
            ps.setInt(2, usuarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                boolean esPropietario = rs.getInt(1) > 0;
                System.out.println("DEBUG DAO: Es propietario = " + esPropietario);
                return esPropietario;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en esUsuarioPropietarioComentario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return false;
    }

    /**
     * Contar comentarios de un reporte
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
            System.out.println("ERROR DAO: Exception en contarComentariosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return 0;
    }
}