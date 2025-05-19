package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.ReporteDesaparicion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDesaparicionDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Registrar nuevo reporte
    public boolean registrarReporte(ReporteDesaparicion r) {
        String sql = "INSERT INTO reporte_desaparicion (r_mascota, r_usuario, fecha_desaparicion, ubicacionultimavez, recompensa, estadoreporte, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getR_Mascota());
            ps.setInt(2, r.getR_Usuario());
            ps.setDate(3, new java.sql.Date(r.getFechaDesaparicion().getTime()));
            ps.setString(4, r.getUbicacionUltimaVez());
            ps.setDouble(5, r.getRecompensa());
            ps.setString(6, r.getEstadoReporte());
            ps.setDate(7, new java.sql.Date(r.getFecha_Registro().getTime()));
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar reporte: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Listar todos los reportes
    public List<ReporteDesaparicion> listarReportes() {
        List<ReporteDesaparicion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reporte_desaparicion";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReporteDesaparicion r = new ReporteDesaparicion();
                r.setReporteID(rs.getInt("reporteid"));
                r.setR_Mascota(rs.getInt("r_mascota"));
                r.setR_Usuario(rs.getInt("r_usuario"));
                r.setFechaDesaparicion(rs.getDate("fecha_desaparicion"));
                r.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                r.setRecompensa(rs.getDouble("recompensa"));
                r.setEstadoReporte(rs.getString("estadoreporte"));
                r.setFecha_Registro(rs.getDate("fecha_registro"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar reportes: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // Buscar un reporte por ID
    public ReporteDesaparicion buscarReporte(int id) {
        ReporteDesaparicion r = new ReporteDesaparicion();
        String sql = "SELECT * FROM reporte_desaparicion WHERE reporteid = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                r.setReporteID(rs.getInt("reporteid"));
                r.setR_Mascota(rs.getInt("r_mascota"));
                r.setR_Usuario(rs.getInt("r_usuario"));
                r.setFechaDesaparicion(rs.getDate("fecha_desaparicion"));
                r.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                r.setRecompensa(rs.getDouble("recompensa"));
                r.setEstadoReporte(rs.getString("estadoreporte"));
                r.setFecha_Registro(rs.getDate("fecha_registro"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar reporte: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return r;
    }

    // Modificar un reporte existente
    public boolean modificarReporte(ReporteDesaparicion r) {
        String sql = "UPDATE reporte_desaparicion SET r_mascota = ?, r_usuario = ?, fecha_desaparicion = ?, ubicacionultimavez = ?, recompensa = ?, estadoreporte = ?, fecha_registro = ? WHERE reporteid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getR_Mascota());
            ps.setInt(2, r.getR_Usuario());
            ps.setDate(3, new java.sql.Date(r.getFechaDesaparicion().getTime()));
            ps.setString(4, r.getUbicacionUltimaVez());
            ps.setDouble(5, r.getRecompensa());
            ps.setString(6, r.getEstadoReporte());
            ps.setDate(7, new java.sql.Date(r.getFecha_Registro().getTime()));
            ps.setInt(8, r.getReporteID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar reporte: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Eliminar un reporte (físicamente)
    public boolean eliminarReporte(int id) {
        String sql = "DELETE FROM reporte_desaparicion WHERE reporteid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar reporte: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Cerrar recursos
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
