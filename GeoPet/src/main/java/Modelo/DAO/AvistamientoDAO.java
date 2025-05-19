package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.Avistamiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvistamientoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrarAvistamiento(Avistamiento a) {
        String sql = "INSERT INTO avistamientos (r_reporte, r_usuarioreportante, fecha_avistamiento, ubicacion, descripcion, contacto, fecha_registro, r_imagen, estatus) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Activo')";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, a.getR_Reporte());
            ps.setInt(2, a.getR_UsuarioReportante());
            ps.setDate(3, new java.sql.Date(a.getFecha_Avistamiento().getTime()));
            ps.setString(4, a.getUbicacion());
            ps.setString(5, a.getDescripcion());
            ps.setString(6, a.getContacto());
            ps.setDate(7, new java.sql.Date(a.getFecha_Registro().getTime()));
            ps.setInt(8, a.getR_Imagen());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar avistamiento: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    public List<Avistamiento> listarAvistamientos() {
        List<Avistamiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM avistamientos WHERE estatus = 'Activo'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Avistamiento a = new Avistamiento();
                a.setAvistamientoID(rs.getInt("avistamientoid"));
                a.setR_Reporte(rs.getInt("r_reporte"));
                a.setR_UsuarioReportante(rs.getInt("r_usuarioreportante"));
                a.setFecha_Avistamiento(rs.getDate("fecha_avistamiento"));
                a.setUbicacion(rs.getString("ubicacion"));
                a.setDescripcion(rs.getString("descripcion"));
                a.setContacto(rs.getString("contacto"));
                a.setFecha_Registro(rs.getDate("fecha_registro"));
                a.setR_Imagen(rs.getInt("r_imagen"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar avistamientos: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    public Avistamiento buscarAvistamiento(int id) {
        Avistamiento a = new Avistamiento();
        String sql = "SELECT * FROM avistamientos WHERE avistamientoid = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                a.setAvistamientoID(rs.getInt("avistamientoid"));
                a.setR_Reporte(rs.getInt("r_reporte"));
                a.setR_UsuarioReportante(rs.getInt("r_usuarioreportante"));
                a.setFecha_Avistamiento(rs.getDate("fecha_avistamiento"));
                a.setUbicacion(rs.getString("ubicacion"));
                a.setDescripcion(rs.getString("descripcion"));
                a.setContacto(rs.getString("contacto"));
                a.setFecha_Registro(rs.getDate("fecha_registro"));
                a.setR_Imagen(rs.getInt("r_imagen"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar avistamiento: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return a;
    }

    public boolean modificarAvistamiento(Avistamiento a) {
        String sql = "UPDATE avistamientos SET r_reporte = ?, r_usuarioreportante = ?, fecha_avistamiento = ?, ubicacion = ?, descripcion = ?, contacto = ?, fecha_registro = ?, r_imagen = ? WHERE avistamientoid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, a.getR_Reporte());
            ps.setInt(2, a.getR_UsuarioReportante());
            ps.setDate(3, new java.sql.Date(a.getFecha_Avistamiento().getTime()));
            ps.setString(4, a.getUbicacion());
            ps.setString(5, a.getDescripcion());
            ps.setString(6, a.getContacto());
            ps.setDate(7, new java.sql.Date(a.getFecha_Registro().getTime()));
            ps.setInt(8, a.getR_Imagen());
            ps.setInt(9, a.getAvistamientoID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar avistamiento: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    public boolean eliminarAvistamiento(int id) {
        String sql = "UPDATE avistamientos SET estatus = 'Inactivo' WHERE avistamientoid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar avistamiento: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

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
