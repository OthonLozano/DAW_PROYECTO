package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.Comentarios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentariosDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Registrar nuevo comentario
    public boolean registrarComentario(Comentarios c) {
        String sql = "INSERT INTO comentarios (r_usuario, r_reporte, contenido, fecha_comentario) VALUES (?, ?, ?, ?)";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getR_Usuario());
            ps.setInt(2, c.getR_Reporte());
            ps.setString(3, c.getContenido());
            ps.setDate(4, new java.sql.Date(c.getFecha_Comentario().getTime()));
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar comentario: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Listar todos los comentarios
    public List<Comentarios> listarComentarios() {
        List<Comentarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM comentarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Comentarios c = new Comentarios();
                c.setComentarioID(rs.getInt("comentarioid"));
                c.setR_Usuario(rs.getInt("r_usuario"));
                c.setR_Reporte(rs.getInt("r_reporte"));
                c.setContenido(rs.getString("contenido"));
                c.setFecha_Comentario(rs.getDate("fecha_comentario"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar comentarios: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // Buscar un comentario por su ID
    public Comentarios buscarComentario(int id) {
        Comentarios c = new Comentarios();
        String sql = "SELECT * FROM comentarios WHERE comentarioid = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                c.setComentarioID(rs.getInt("comentarioid"));
                c.setR_Usuario(rs.getInt("r_usuario"));
                c.setR_Reporte(rs.getInt("r_reporte"));
                c.setContenido(rs.getString("contenido"));
                c.setFecha_Comentario(rs.getDate("fecha_comentario"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar comentario: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return c;
    }

    // Modificar comentario existente
    public boolean modificarComentario(Comentarios c) {
        String sql = "UPDATE comentarios SET r_usuario = ?, r_reporte = ?, contenido = ?, fecha_comentario = ? WHERE comentarioid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getR_Usuario());
            ps.setInt(2, c.getR_Reporte());
            ps.setString(3, c.getContenido());
            ps.setDate(4, new java.sql.Date(c.getFecha_Comentario().getTime()));
            ps.setInt(5, c.getComentarioID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar comentario: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Eliminar comentario físicamente (puedes cambiar a lógica si tienes 'estatus')
    public boolean eliminarComentario(int id) {
        String sql = "DELETE FROM comentarios WHERE comentarioid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar comentario: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Cerrar conexiones
    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.toString());
        }
    }
}
