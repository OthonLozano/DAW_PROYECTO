package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDesaparicionDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Registrar nuevo reporte (ACTUALIZADO con todos los campos)
    public boolean registrarReporte(ReporteDesaparicion r) {
        String sql = "INSERT INTO ReporteDesaparicion (r_mascota, r_usuario, fechadesaparicion, ubicacionultimavez, descripcionsituacion, recompensa, estadoreporte, fecha_registro, estatus) VALUES (?, ?, ?, ?, ?, ?, ?::reporte, ?, ?::tipo_estatus)";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getR_Mascota());
            ps.setInt(2, r.getR_Usuario());
            ps.setDate(3, new java.sql.Date(r.getFechaDesaparicion().getTime()));
            ps.setString(4, r.getUbicacionUltimaVez());
            ps.setString(5, r.getDescripcionSituacion());
            ps.setDouble(6, r.getRecompensa());
            ps.setString(7, r.getEstadoReporte());
            ps.setDate(8, new java.sql.Date(r.getFecha_Registro().getTime()));
            ps.setString(9, r.getEstatus());
            ps.executeUpdate();
            exito = true;
            System.out.println("DEBUG: Reporte registrado exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al registrar reporte: " + e.toString());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Listar reportes activos con información completa
    public List<ReporteConRelaciones> listarReportesCompletos() {
        List<ReporteConRelaciones> lista = new ArrayList<>();
        String sql = """
            SELECT 
                r.reporteid,
                r.r_mascota,
                r.r_usuario,
                r.fechadesaparicion,
                r.ubicacionultimavez,
                r.descripcionsituacion,
                r.recompensa,
                r.estadoreporte,
                r.fecha_registro,
                r.estatus,
                m.mascotaid,
                m.nombre as nombre_mascota,
                m.edad,
                m.sexo,
                m.color,
                m.caracteristicasdistintivas,
                m.estado as estado_mascota,
                u.usuarioid,
                u.nombre as nombre_usuario,
                u.apellido,
                u.telefono,
                u.email,
                e.especieid,
                e.nombre as nombre_especie,
                e.descripcion as descripcion_especie,
                img.imagenmascotaid,
                img.url_imagen,
                img.fecha_carga
            FROM ReporteDesaparicion r
            INNER JOIN Mascotas m ON r.r_mascota = m.mascotaid
            INNER JOIN Usuarios u ON r.r_usuario = u.usuarioid
            INNER JOIN Especie e ON m.r_especie = e.especieid
            LEFT JOIN (
                SELECT DISTINCT 
                    imagenid,
                    r_mascota,
                    url_imagen,
                    fecha_carga,
                    ROW_NUMBER() OVER (PARTITION BY r_mascota ORDER BY fecha_carga DESC) as rn
                FROM ImagenMascota 
                WHERE estatus = 'Alta'
            ) img ON m.mascotaid = img.r_mascota AND img.rn = 1
            WHERE r.estatus = 'Alta'
            ORDER BY r.fecha_registro DESC
            """;

        System.out.println("DEBUG: Ejecutando consulta de reportes completos");

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Crear ReporteDesaparicion
                ReporteDesaparicion reporte = new ReporteDesaparicion();
                reporte.setReporteID(rs.getInt("reporteid"));
                reporte.setR_Mascota(rs.getInt("r_mascota"));
                reporte.setR_Usuario(rs.getInt("r_usuario"));
                reporte.setFechaDesaparicion(rs.getDate("fechadesaparicion"));
                reporte.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                reporte.setDescripcionSituacion(rs.getString("descripcionsituacion"));
                reporte.setRecompensa(rs.getDouble("recompensa"));
                reporte.setEstadoReporte(rs.getString("estadoreporte"));
                reporte.setFecha_Registro(rs.getDate("fecha_registro"));
                reporte.setEstatus(rs.getString("estatus"));

                // Crear Mascotas
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setEstado(rs.getString("estado_mascota"));

                // Crear Usuarios
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("nombre_usuario"));
                usuario.setApellidoMat(rs.getString("apellido"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Crear Especie
                Especie especie = new Especie();
                especie.setEspecieID(rs.getInt("especieid"));
                especie.setNombre(rs.getString("nombre_especie"));
                especie.setDescripcion(rs.getString("descripcion_especie"));

                // Crear ImagenMascota (puede ser null)
                ImagenMascota imagen = null;
                if (rs.getString("url_imagen") != null) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setR_Mascota(mascota.getMascotaID());
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                    imagen.setFecha_Carga(rs.getDate("fecha_carga"));
                    imagen.setEstatus("Alta");
                }

                // Crear ReporteConRelaciones
                ReporteConRelaciones reporteCompleto = new ReporteConRelaciones(reporte, mascota, usuario, especie, imagen);
                lista.add(reporteCompleto);

                System.out.println("DEBUG: Reporte completo agregado - " + mascota.getNombre() +
                        " de " + usuario.getNombre() + " " + usuario.getApellidoMat());
            }

            System.out.println("DEBUG: Total reportes completos obtenidos: " + lista.size());

        } catch (SQLException e) {
            System.out.println("ERROR: Exception en listarReportesCompletos - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    // Listar reportes por usuario
    public List<ReporteConRelaciones> listarReportesPorUsuario(int usuarioId) {
        List<ReporteConRelaciones> lista = new ArrayList<>();
        String sql = """
            SELECT 
                r.reporteid,
                r.r_mascota,
                r.r_usuario,
                r.fechadesaparicion,
                r.ubicacionultimavez,
                r.descripcionsituacion,
                r.recompensa,
                r.estadoreporte,
                r.fecha_registro,
                r.estatus,
                m.mascotaid,
                m.nombre as nombre_mascota,
                m.edad,
                m.sexo,
                m.color,
                m.caracteristicasdistintivas,
                m.estado as estado_mascota,
                u.usuarioid,
                u.nombre as nombre_usuario,
                u.apellido,
                u.telefono,
                u.email,
                e.especieid,
                e.nombre as nombre_especie,
                e.descripcion as descripcion_especie,
                img.imagenid,
                img.url_imagen,
                img.fecha_carga
            FROM ReporteDesaparicion r
            INNER JOIN Mascotas m ON r.r_mascota = m.mascotaid
            INNER JOIN Usuarios u ON r.r_usuario = u.usuarioid
            INNER JOIN Especie e ON m.r_especie = e.especieid
            LEFT JOIN (
                SELECT DISTINCT 
                    imagenid,
                    r_mascota,
                    url_imagen,
                    fecha_carga,
                    ROW_NUMBER() OVER (PARTITION BY r_mascota ORDER BY fecha_carga DESC) as rn
                FROM ImagenMascota 
                WHERE estatus = 'Alta'
            ) img ON m.mascotaid = img.r_mascota AND img.rn = 1
            WHERE r.r_usuario = ? AND r.estatus = 'Alta'
            ORDER BY r.fecha_registro DESC
            """;

        System.out.println("DEBUG: Ejecutando consulta de reportes por usuario ID: " + usuarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Similar al método anterior, pero filtrado por usuario
                // [Mismo código de creación de objetos]

                ReporteDesaparicion reporte = new ReporteDesaparicion();
                reporte.setReporteID(rs.getInt("reporteid"));
                reporte.setR_Mascota(rs.getInt("r_mascota"));
                reporte.setR_Usuario(rs.getInt("r_usuario"));
                reporte.setFechaDesaparicion(rs.getDate("fechadesaparicion"));
                reporte.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                reporte.setDescripcionSituacion(rs.getString("descripcionsituacion"));
                reporte.setRecompensa(rs.getDouble("recompensa"));
                reporte.setEstadoReporte(rs.getString("estadoreporte"));
                reporte.setFecha_Registro(rs.getDate("fecha_registro"));
                reporte.setEstatus(rs.getString("estatus"));

                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setEstado(rs.getString("estado_mascota"));

                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("nombre_usuario"));
                usuario.setApellidoMat(rs.getString("apellido"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                Especie especie = new Especie();
                especie.setEspecieID(rs.getInt("especieid"));
                especie.setNombre(rs.getString("nombre_especie"));
                especie.setDescripcion(rs.getString("descripcion_especie"));

                ImagenMascota imagen = null;
                if (rs.getString("url_imagen") != null) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setR_Mascota(mascota.getMascotaID());
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                    imagen.setFecha_Carga(rs.getDate("fecha_carga"));
                    imagen.setEstatus("Alta");
                }

                ReporteConRelaciones reporteCompleto = new ReporteConRelaciones(reporte, mascota, usuario, especie, imagen);
                lista.add(reporteCompleto);
            }

            System.out.println("DEBUG: Total reportes del usuario " + usuarioId + ": " + lista.size());

        } catch (SQLException e) {
            System.out.println("ERROR: Exception en listarReportesPorUsuario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    // Buscar un reporte por ID (ACTUALIZADO)
    public ReporteDesaparicion buscarReporte(int id) {
        ReporteDesaparicion r = new ReporteDesaparicion();
        String sql = "SELECT * FROM ReporteDesaparicion WHERE reporteid = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                r.setReporteID(rs.getInt("reporteid"));
                r.setR_Mascota(rs.getInt("r_mascota"));
                r.setR_Usuario(rs.getInt("r_usuario"));
                r.setFechaDesaparicion(rs.getDate("fechadesaparicion"));
                r.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                r.setDescripcionSituacion(rs.getString("descripcionsituacion"));
                r.setRecompensa(rs.getDouble("recompensa"));
                r.setEstadoReporte(rs.getString("estadoreporte"));
                r.setFecha_Registro(rs.getDate("fecha_registro"));
                r.setEstatus(rs.getString("estatus"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar reporte: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return r;
    }

    // Modificar un reporte existente (ACTUALIZADO)
    public boolean modificarReporte(ReporteDesaparicion r) {
        String sql = "UPDATE ReporteDesaparicion SET r_mascota = ?, r_usuario = ?, fechadesaparicion = ?, ubicacionultimavez = ?, descripcionsituacion = ?, recompensa = ?, estadoreporte = ?::reporte, fecha_registro = ?, estatus = ?::tipo_estatus WHERE reporteid = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getR_Mascota());
            ps.setInt(2, r.getR_Usuario());
            ps.setDate(3, new java.sql.Date(r.getFechaDesaparicion().getTime()));
            ps.setString(4, r.getUbicacionUltimaVez());
            ps.setString(5, r.getDescripcionSituacion());
            ps.setDouble(6, r.getRecompensa());
            ps.setString(7, r.getEstadoReporte());
            ps.setDate(8, new java.sql.Date(r.getFecha_Registro().getTime()));
            ps.setString(9, r.getEstatus());
            ps.setInt(10, r.getReporteID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar reporte: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return exito;
    }

    // Eliminar un reporte (cambiar estatus a Baja)
    public boolean eliminarReporte(int id) {
        String sql = "UPDATE ReporteDesaparicion SET estatus = 'Baja' WHERE reporteid = ?";
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