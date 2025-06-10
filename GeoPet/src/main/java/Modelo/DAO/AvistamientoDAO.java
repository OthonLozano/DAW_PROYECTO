package Modelo.DAO;

import Modelo.JavaBeans.Avistamiento;
import Modelo.JavaBeans.AvistamientoConRelaciones;
import Modelo.JavaBeans.Usuarios;
import Modelo.JavaBeans.ImagenMascota;
import Connection.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de Avistamientos
 */
public class AvistamientoDAO {

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
     * Obtiene todos los avistamientos de un reporte específico con información del usuario e imagen
     */
    public List<AvistamientoConRelaciones> obtenerAvistamientosPorReporte(int reporteId) {
        List<AvistamientoConRelaciones> avistamientos = new ArrayList<>();

        String sql = "SELECT " +
                "a.avistamientoid, a.r_reporte, a.r_usuarioreportante, a.fecha_avistamiento, " +
                "a.ubicacion, a.descripcion, a.contacto, a.fecha_registro, a.r_imagen, a.estatus, " +
                "u.usuarioid, u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, " +
                "u.telefono, u.email, " +
                "i.imagenid, i.url_imagen " +
                "FROM avistamiento a " +
                "INNER JOIN usuarios u ON a.r_usuarioreportante = u.usuarioid " +
                "LEFT JOIN imagenmascota i ON a.r_imagen = i.imagenid " +
                "WHERE a.r_reporte = ? AND a.estatus = 'Alta' " +
                "ORDER BY a.fecha_registro DESC";

        System.out.println("=== DEBUG AvistamientoDAO ===");
        System.out.println("DEBUG DAO: Obteniendo avistamientos para reporte ID = " + reporteId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Crear el avistamiento
                Avistamiento avistamiento = new Avistamiento();
                avistamiento.setAvistamientoID(rs.getInt("avistamientoid"));
                avistamiento.setR_Reporte(rs.getInt("r_reporte"));
                avistamiento.setR_UsuarioReportante(rs.getInt("r_usuarioreportante"));
                avistamiento.setFecha_Avistamiento(rs.getDate("fecha_avistamiento"));
                avistamiento.setUbicacion(rs.getString("ubicacion"));
                avistamiento.setDescripcion(rs.getString("descripcion"));
                avistamiento.setContacto(rs.getString("contacto"));
                avistamiento.setFecha_Registro(rs.getDate("fecha_registro"));
                avistamiento.setR_Imagen(rs.getInt("r_imagen"));
                avistamiento.setEstatus(rs.getString("estatus"));

                // Crear el usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Crear la imagen (puede ser null)
                ImagenMascota imagen = null;
                if (rs.getObject("imagenid") != null && rs.getInt("imagenid") > 0) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                }

                // Crear el objeto con relaciones
                AvistamientoConRelaciones avistamientoCompleto = new AvistamientoConRelaciones(avistamiento, usuario, imagen);
                avistamientos.add(avistamientoCompleto);

                System.out.println("DEBUG DAO: Avistamiento agregado - Usuario: " + usuario.getNombre());
            }

            System.out.println("DEBUG DAO: Total avistamientos obtenidos = " + avistamientos.size());

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en obtenerAvistamientosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return avistamientos;
    }

    /**
     * Agregar un nuevo avistamiento
     */
    public boolean agregarAvistamiento(Avistamiento avistamiento) {
        String sql = "INSERT INTO avistamiento (r_reporte, r_usuarioreportante, fecha_avistamiento, " +
                "ubicacion, descripcion, contacto, fecha_registro, r_imagen, estatus) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, 'Alta')";

        System.out.println("=== DEBUG AvistamientoDAO ===");
        System.out.println("DEBUG DAO: Agregando avistamiento - Usuario: " + avistamiento.getR_UsuarioReportante() +
                ", Reporte: " + avistamiento.getR_Reporte());

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamiento.getR_Reporte());
            ps.setInt(2, avistamiento.getR_UsuarioReportante());
            ps.setDate(3, new java.sql.Date(avistamiento.getFecha_Avistamiento().getTime()));
            ps.setString(4, avistamiento.getUbicacion());
            ps.setString(5, avistamiento.getDescripcion());
            ps.setString(6, avistamiento.getContacto());

            // Manejar imagen opcional
            if (avistamiento.getR_Imagen() > 0) {
                ps.setInt(7, avistamiento.getR_Imagen());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Avistamiento agregado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo agregar el avistamiento");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en agregarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtener un avistamiento específico por ID
     */
    public AvistamientoConRelaciones obtenerAvistamientoPorId(int avistamientoId) {
        String sql = "SELECT " +
                "a.avistamientoid, a.r_reporte, a.r_usuarioreportante, a.fecha_avistamiento, " +
                "a.ubicacion, a.descripcion, a.contacto, a.fecha_registro, a.r_imagen, a.estatus, " +
                "u.usuarioid, u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, " +
                "u.telefono, u.email, " +
                "i.imagenid, i.url_imagen " +
                "FROM avistamiento a " +
                "INNER JOIN usuarios u ON a.r_usuarioreportante = u.usuarioid " +
                "LEFT JOIN imagenmascota i ON a.r_imagen = i.imagenid " +
                "WHERE a.avistamientoid = ? AND a.estatus = 'Alta'";

        System.out.println("=== DEBUG AvistamientoDAO ===");
        System.out.println("DEBUG DAO: Buscando avistamiento con ID = " + avistamientoId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamientoId);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Crear el avistamiento
                Avistamiento avistamiento = new Avistamiento();
                avistamiento.setAvistamientoID(rs.getInt("avistamientoid"));
                avistamiento.setR_Reporte(rs.getInt("r_reporte"));
                avistamiento.setR_UsuarioReportante(rs.getInt("r_usuarioreportante"));
                avistamiento.setFecha_Avistamiento(rs.getDate("fecha_avistamiento"));
                avistamiento.setUbicacion(rs.getString("ubicacion"));
                avistamiento.setDescripcion(rs.getString("descripcion"));
                avistamiento.setContacto(rs.getString("contacto"));
                avistamiento.setFecha_Registro(rs.getDate("fecha_registro"));
                avistamiento.setR_Imagen(rs.getInt("r_imagen"));
                avistamiento.setEstatus(rs.getString("estatus"));

                // Crear el usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Crear la imagen (puede ser null)
                ImagenMascota imagen = null;
                if (rs.getObject("imagenid") != null && rs.getInt("imagenid") > 0) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                }

                System.out.println("DEBUG DAO: Avistamiento encontrado");
                return new AvistamientoConRelaciones(avistamiento, usuario, imagen);
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en obtenerAvistamientoPorId - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        System.out.println("DEBUG DAO: Avistamiento no encontrado");
        return null;
    }

    /**
     * Editar un avistamiento existente
     */
    public boolean editarAvistamiento(Avistamiento avistamiento) {
        String sql = "UPDATE avistamiento SET fecha_avistamiento = ?, ubicacion = ?, " +
                "descripcion = ?, contacto = ?, r_imagen = ? " +
                "WHERE avistamientoid = ? AND estatus = 'Alta'";

        System.out.println("=== DEBUG AvistamientoDAO ===");
        System.out.println("DEBUG DAO: Editando avistamiento ID = " + avistamiento.getAvistamientoID());

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(avistamiento.getFecha_Avistamiento().getTime()));
            ps.setString(2, avistamiento.getUbicacion());
            ps.setString(3, avistamiento.getDescripcion());
            ps.setString(4, avistamiento.getContacto());

            // Manejar imagen opcional
            if (avistamiento.getR_Imagen() > 0) {
                ps.setInt(5, avistamiento.getR_Imagen());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setInt(6, avistamiento.getAvistamientoID());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Avistamiento editado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo editar el avistamiento");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en editarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Eliminar un avistamiento (cambiar estatus a 'Baja')
     */
    public boolean eliminarAvistamiento(int avistamientoId) {
        String sql = "UPDATE avistamiento SET estatus = 'Baja' WHERE avistamientoid = ?";

        System.out.println("=== DEBUG AvistamientoDAO ===");
        System.out.println("DEBUG DAO: Eliminando avistamiento ID = " + avistamientoId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamientoId);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Avistamiento eliminado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo eliminar el avistamiento");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en eliminarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Verificar si el usuario es propietario del avistamiento
     */
    public boolean esUsuarioPropietarioAvistamiento(int avistamientoId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM avistamiento WHERE avistamientoid = ? AND r_usuarioreportante = ? AND estatus = 'Alta'";

        System.out.println("=== DEBUG AvistamientoDAO ===");
        System.out.println("DEBUG DAO: Verificando propiedad - Avistamiento: " + avistamientoId + ", Usuario: " + usuarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamientoId);
            ps.setInt(2, usuarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                boolean esPropietario = rs.getInt(1) > 0;
                System.out.println("DEBUG DAO: Es propietario = " + esPropietario);
                return esPropietario;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en esUsuarioPropietarioAvistamiento - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return false;
    }

    /**
     * Contar avistamientos de un reporte
     */
    public int contarAvistamientosPorReporte(int reporteId) {
        String sql = "SELECT COUNT(*) FROM avistamiento WHERE r_reporte = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en contarAvistamientosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return 0;
    }
}