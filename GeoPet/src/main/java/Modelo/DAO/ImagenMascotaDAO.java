package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.ImagenMascota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImagenMascotaDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Registrar nueva imagen
    public boolean registrarImagen(ImagenMascota i) {
        String sql = "INSERT INTO imagen_mascota (r_mascota, url_imagen, fecha_carga) VALUES (?, ?, ?)";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, i.getR_Mascota());
            ps.setInt(2, i.getURL_Imagen());
            ps.setDate(3, new java.sql.Date(i.getFecha_Carga().getTime()));
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Listar todas las imágenes
    public List<ImagenMascota> listarImagenes() {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM imagen_mascota";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ImagenMascota i = new ImagenMascota();
                i.setImagenID(rs.getInt("imagenid"));
                i.setR_Mascota(rs.getInt("r_mascota"));
                i.setURL_Imagen(rs.getInt("url_imagen"));
                i.setFecha_Carga(rs.getDate("fecha_carga"));
                lista.add(i);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar imágenes: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // Buscar imagen por ID
    public ImagenMascota buscarImagen(int id) {
        ImagenMascota i = new ImagenMascota();
        String sql = "SELECT * FROM imagen_mascota WHERE imagenid = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                i.setImagenID(rs.getInt("imagenid"));
                i.setR_Mascota(rs.getInt("r_mascota"));
                i.setURL_Imagen(rs.getInt("url_imagen"));
                i.setFecha_Carga(rs.getDate("fecha_carga"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return i;
    }

    // Modificar imagen
    public boolean modificarImagen(ImagenMascota i) {
        String sql = "UPDATE imagen_mascota SET r_mascota = ?, url_imagen = ?, fecha_carga = ? WHERE imagenid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, i.getR_Mascota());
            ps.setInt(2, i.getURL_Imagen());
            ps.setDate(3, new java.sql.Date(i.getFecha_Carga().getTime()));
            ps.setInt(4, i.getImagenID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Eliminar imagen (físico)
    public boolean eliminarImagen(int id) {
        String sql = "DELETE FROM imagen_mascota WHERE imagenid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Método para cerrar conexiones
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