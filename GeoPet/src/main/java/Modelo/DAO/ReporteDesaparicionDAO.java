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

        // SQL corregido con apellidopat y apellidomat - SIN FILTRO DE USUARIO
        String sql = "SELECT " +
                "r.reporteid, r.r_usuario, r.r_mascota, r.fechadesaparicion, " +
                "r.ubicacionultimavez, r.descripcionsituacion, r.recompensa, " +
                "r.estadoreporte, r.fecha_registro, r.estatus, " +
                "u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, u.telefono, " +
                "m.nombre as mascota_nombre, m.edad, m.sexo, m.color, m.caracteristicasdistintivas, " +
                "e.nombre as especie_nombre, " +
                "i.url_imagen " +
                "FROM reportedesaparicion r " +
                "INNER JOIN usuarios u ON r.r_usuario = u.usuarioid " +
                "INNER JOIN mascotas m ON r.r_mascota = m.mascotaid " +
                "INNER JOIN especie e ON m.r_especie = e.especieid " +
                "LEFT JOIN imagenmascota i ON m.mascotaid = i.r_mascota " +
                "WHERE r.estatus = 'Alta' AND r.estadoreporte = 'Activo' " +  // Solo reportes perdidos
                "ORDER BY r.fecha_registro DESC";

        return ejecutarConsultaReportes(sql, null);
    }

    // AGREGAR este nuevo método para reportes excluyendo al usuario actual:
    public List<ReporteConRelaciones> listarReportesExternos(Integer usuarioIdActual) {
        List<ReporteConRelaciones> lista = new ArrayList<>();

        String sql = "SELECT " +
                "r.reporteid, r.r_usuario, r.r_mascota, r.fechadesaparicion, " +
                "r.ubicacionultimavez, r.descripcionsituacion, r.recompensa, " +
                "r.estadoreporte, r.fecha_registro, r.estatus, " +
                "u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, u.telefono, " +
                "m.nombre as mascota_nombre, m.edad, m.sexo, m.color, m.caracteristicasdistintivas, " +
                "e.nombre as especie_nombre, " +
                "i.url_imagen " +
                "FROM reportedesaparicion r " +
                "INNER JOIN usuarios u ON r.r_usuario = u.usuarioid " +
                "INNER JOIN mascotas m ON r.r_mascota = m.mascotaid " +
                "INNER JOIN especie e ON m.r_especie = e.especieid " +
                "LEFT JOIN imagenmascota i ON m.mascotaid = i.r_mascota " +
                "WHERE r.estatus = 'Alta' AND r.estadoreporte = 'Activo' ";

        // Si hay un usuario logueado, excluir sus reportes
        if (usuarioIdActual != null) {
            sql += "AND r.r_usuario != ? ";
            System.out.println("DEBUG DAO: Excluyendo reportes del usuario ID = " + usuarioIdActual);
        }

        sql += "ORDER BY r.fecha_registro DESC";

        return ejecutarConsultaReportes(sql, usuarioIdActual);
    }

    // MÉTODO AUXILIAR para evitar duplicar código:
    private List<ReporteConRelaciones> ejecutarConsultaReportes(String sql, Integer usuarioIdExcluir) {
        List<ReporteConRelaciones> lista = new ArrayList<>();

        System.out.println("=== DEBUG ReporteDesaparicionDAO ===");
        System.out.println("DEBUG DAO: SQL = " + sql);

        try {
            con = cn.getConnection();
            System.out.println("DEBUG DAO: Conexión establecida");

            ps = con.prepareStatement(sql);

            // Si hay que excluir un usuario, establecer el parámetro
            if (usuarioIdExcluir != null) {
                ps.setInt(1, usuarioIdExcluir);
                System.out.println("DEBUG DAO: Parámetro usuarioIdExcluir = " + usuarioIdExcluir);
            }

            rs = ps.executeQuery();
            System.out.println("DEBUG DAO: Query ejecutado");

            int contador = 0;
            while (rs.next()) {
                contador++;
                System.out.println("DEBUG DAO: Procesando fila " + contador);

                // Crear objeto ReporteDesaparicion
                ReporteDesaparicion reporte = new ReporteDesaparicion();
                reporte.setReporteID(rs.getInt("reporteid"));
                reporte.setR_Usuario(rs.getInt("r_usuario"));
                reporte.setR_Mascota(rs.getInt("r_mascota"));
                reporte.setFechaDesaparicion(rs.getDate("fechadesaparicion"));
                reporte.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                reporte.setDescripcionSituacion(rs.getString("descripcionsituacion"));
                reporte.setRecompensa(rs.getDouble("recompensa"));
                reporte.setEstadoReporte(rs.getString("estadoreporte"));
                reporte.setFecha_Registro(rs.getDate("fecha_registro"));
                reporte.setEstatus(rs.getString("estatus"));

                System.out.println("DEBUG DAO: Reporte ID = " + reporte.getReporteID() +
                        ", Usuario = " + reporte.getR_Usuario() +
                        ", Mascota = " + reporte.getR_Mascota() +
                        ", Estado = " + reporte.getEstadoReporte());

                // Crear objeto Usuarios
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(reporte.getR_Usuario());
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));

                // Crear objeto Mascotas
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(reporte.getR_Mascota());
                mascota.setNombre(rs.getString("mascota_nombre"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));

                // Crear objeto Especie
                Especie especie = new Especie();
                especie.setNombre(rs.getString("especie_nombre"));

                // Crear objeto ImagenMascota (opcional)
                ImagenMascota imagen = null;
                String urlImagen = rs.getString("url_imagen");
                if (urlImagen != null && !urlImagen.trim().isEmpty()) {
                    imagen = new ImagenMascota();
                    imagen.setURL_Imagen(urlImagen);
                }

                // Crear el objeto ReporteConRelaciones
                ReporteConRelaciones reporteCompleto = new ReporteConRelaciones();
                reporteCompleto.setReporte(reporte);
                reporteCompleto.setUsuario(usuario);
                reporteCompleto.setMascota(mascota);
                reporteCompleto.setEspecie(especie);
                reporteCompleto.setImagen(imagen);

                lista.add(reporteCompleto);
                System.out.println("DEBUG DAO: Reporte completo agregado a la lista");
            }

            System.out.println("DEBUG DAO: Total reportes procesados = " + contador);
            System.out.println("DEBUG DAO: Tamaño final de la lista = " + lista.size());

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en ejecutarConsultaReportes - " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("ERROR DAO: Exception al cerrar conexiones - " + e.getMessage());
            }
        }

        System.out.println("DEBUG DAO: Retornando lista con " + lista.size() + " elementos");
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
                u.apellidopat, 
                u.apellidomat,
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
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
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

    public boolean existeReporteActivoPorMascota(int mascotaId) {
        String sql = "SELECT COUNT(*) as total FROM reportedesaparicion " +
                "WHERE r_mascota = ? AND estatus = 'Alta' AND estadoreporte != 'En casa'";

        System.out.println("DEBUG DAO: Verificando reporte activo para mascota ID = " + mascotaId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("DEBUG DAO: Reportes activos encontrados = " + total);
                return total > 0;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en existeReporteActivoPorMascota - " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("ERROR DAO: Exception al cerrar conexiones - " + e.getMessage());
            }
        }

        return false;
    }

    public ReporteDesaparicion obtenerReporteActivoPorMascota(int mascotaId) {
        String sql = "SELECT * FROM reportedesaparicion " +
                "WHERE r_mascota = ? AND estatus = 'Alta' AND estadoreporte != 'Activo' " +
                "ORDER BY fecha_registro DESC LIMIT 1";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();

            if (rs.next()) {
                ReporteDesaparicion reporte = new ReporteDesaparicion();
                reporte.setReporteID(rs.getInt("reporteid"));
                reporte.setR_Usuario(rs.getInt("r_usuario"));
                reporte.setR_Mascota(rs.getInt("r_mascota"));
                reporte.setFechaDesaparicion(rs.getDate("fechadesaparicion"));
                reporte.setUbicacionUltimaVez(rs.getString("ubicacionultimavez"));
                reporte.setDescripcionSituacion(rs.getString("descripcionsituacion"));
                reporte.setRecompensa(rs.getDouble("recompensa"));
                reporte.setEstadoReporte(rs.getString("estadoreporte"));
                reporte.setFecha_Registro(rs.getDate("fecha_registro"));
                reporte.setEstatus(rs.getString("estatus"));

                return reporte;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en obtenerReporteActivoPorMascota - " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("ERROR DAO: Exception al cerrar conexiones - " + e.getMessage());
            }
        }

        return null;
    }
}