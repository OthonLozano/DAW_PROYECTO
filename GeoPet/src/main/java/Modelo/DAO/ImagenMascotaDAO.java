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

    // Registrar nueva imagen (siempre con estatus "Alta")
    public boolean registrarImagen(ImagenMascota i) {
        String sql = "INSERT INTO ImagenMascota (r_mascota, url_imagen, fecha_carga, estatus) VALUES (?, ?, ?, 'Alta')";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, i.getR_Mascota());
            ps.setString(2, i.getURL_Imagen());
            ps.setTimestamp(3, new java.sql.Timestamp(i.getFecha_Carga().getTime()));
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Registrar múltiples imágenes en una transacción
    public boolean registrarImagenes(List<ImagenMascota> imagenes) {
        String sql = "INSERT INTO ImagenMascota (r_mascota, url_imagen, fecha_carga, estatus) VALUES (?, ?, ?, 'Alta')";
        boolean exito = false;
        try {
            con = cn.getConnection();
            con.setAutoCommit(false);

            ps = con.prepareStatement(sql);
            for (ImagenMascota i : imagenes) {
                ps.setInt(1, i.getR_Mascota());
                ps.setString(2, i.getURL_Imagen());
                ps.setTimestamp(3, new java.sql.Timestamp(i.getFecha_Carga().getTime()));
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();
            exito = true;

        } catch (SQLException e) {
            System.out.println("Error al registrar imágenes: " + e.toString());
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.toString());
            }
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al restaurar auto-commit: " + e.toString());
            }
            cerrarRecursos();
        }
        return exito;
    }

    // Listar todas las imágenes activas (solo estatus = 'Alta')
    public List<ImagenMascota> listarImagenes() {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM ImagenMascota WHERE estatus = 'Alta' ORDER BY fecha_carga DESC";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ImagenMascota i = new ImagenMascota();
                i.setImagenID(rs.getInt("imagenid"));
                i.setR_Mascota(rs.getInt("r_mascota"));
                i.setURL_Imagen(rs.getString("url_imagen"));
                i.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                i.setEstatus(rs.getString("estatus"));
                lista.add(i);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar imágenes: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // Listar imágenes activas por mascota
    public List<ImagenMascota> listarImagenesPorMascota(int mascotaId) {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM ImagenMascota WHERE r_mascota = ? AND estatus = 'Alta' ORDER BY fecha_carga ASC";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ImagenMascota i = new ImagenMascota();
                i.setImagenID(rs.getInt("imagenid"));
                i.setR_Mascota(rs.getInt("r_mascota"));
                i.setURL_Imagen(rs.getString("url_imagen"));
                i.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                i.setEstatus(rs.getString("estatus"));
                lista.add(i);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar imágenes por mascota: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // Obtener la primera imagen activa de una mascota
    public String obtenerImagenPrincipal(int mascotaId) {
        String imagenUrl = null;
        String sql = "SELECT url_imagen FROM ImagenMascota WHERE r_mascota = ? AND estatus = 'Alta' ORDER BY fecha_carga ASC LIMIT 1";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();
            if (rs.next()) {
                imagenUrl = rs.getString("url_imagen");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener imagen principal: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return imagenUrl;
    }

    // Buscar imagen por ID (solo si está activa)
    public ImagenMascota buscarImagen(int id) {
        ImagenMascota i = new ImagenMascota();
        String sql = "SELECT * FROM ImagenMascota WHERE imagenid = ? AND estatus = 'Alta'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                i.setImagenID(rs.getInt("imagenid"));
                i.setR_Mascota(rs.getInt("r_mascota"));
                i.setURL_Imagen(rs.getString("url_imagen"));
                i.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                i.setEstatus(rs.getString("estatus"));
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
        String sql = "UPDATE ImagenMascota SET r_mascota = ?, url_imagen = ?, fecha_carga = ?, estatus = ? WHERE imagenid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, i.getR_Mascota());
            ps.setString(2, i.getURL_Imagen());
            ps.setTimestamp(3, new java.sql.Timestamp(i.getFecha_Carga().getTime()));
            ps.setString(4, i.getEstatus());
            ps.setInt(5, i.getImagenID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Actualizar solo la URL de una imagen
    public boolean actualizarUrlImagen(int imagenId, String nuevaUrl) {
        String sql = "UPDATE ImagenMascota SET url_imagen = ? WHERE imagenid = ? AND estatus = 'Alta'";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaUrl);
            ps.setInt(2, imagenId);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar URL de imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Eliminación LÓGICA - cambiar estatus a 'Baja'
    public boolean eliminarImagenLogico(int id) {
        String sql = "UPDATE ImagenMascota SET estatus = 'Baja' WHERE imagenid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar imagen (lógico): " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Eliminación FÍSICA - borrar completamente de la BD
    public boolean eliminarImagenFisico(int id) {
        String sql = "DELETE FROM ImagenMascota WHERE imagenid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar imagen (físico): " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Dar de baja todas las imágenes de una mascota (lógico)
    public boolean darBajaImagenesPorMascota(int mascotaId) {
        String sql = "UPDATE ImagenMascota SET estatus = 'Baja' WHERE r_mascota = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al dar de baja imágenes por mascota: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Reactivar imagen (cambiar de 'Baja' a 'Alta')
    public boolean reactivarImagen(int id) {
        String sql = "UPDATE ImagenMascota SET estatus = 'Alta' WHERE imagenid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al reactivar imagen: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Contar imágenes activas de una mascota
    public int contarImagenesActivasPorMascota(int mascotaId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as total FROM ImagenMascota WHERE r_mascota = ? AND estatus = 'Alta'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al contar imágenes activas: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return count;
    }

    // Verificar si una mascota tiene imágenes activas
    public boolean mascotaTieneImagenesActivas(int mascotaId) {
        return contarImagenesActivasPorMascota(mascotaId) > 0;
    }

    // Listar imágenes eliminadas (estatus = 'Baja') - para administración
    public List<ImagenMascota> listarImagenesEliminadas() {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM ImagenMascota WHERE estatus = 'Baja' ORDER BY fecha_carga DESC";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ImagenMascota i = new ImagenMascota();
                i.setImagenID(rs.getInt("imagenid"));
                i.setR_Mascota(rs.getInt("r_mascota"));
                i.setURL_Imagen(rs.getString("url_imagen"));
                i.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                i.setEstatus(rs.getString("estatus"));
                lista.add(i);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar imágenes eliminadas: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
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