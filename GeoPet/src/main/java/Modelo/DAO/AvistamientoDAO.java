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
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Avistamientos.
 * Proporciona métodos para interactuar con la tabla 'avistamiento' en la base de datos,
 * incluyendo operaciones de consulta, inserción, actualización y eliminación lógica.
 *
 * Esta clase implementa el patrón DAO para separar la lógica de acceso a datos
 * de la lógica de negocio, proporcionando una interfaz limpia para las operaciones
 * relacionadas con avistamientos de mascotas.
 */
public class AvistamientoDAO {

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
     * Obtiene todos los avistamientos asociados a un reporte específico con información completa.
     * Realiza un JOIN con las tablas usuarios e imagenmascota para obtener datos relacionados.
     *
     * La consulta incluye:
     * - Información completa del avistamiento
     * - Datos del usuario que reportó el avistamiento
     * - Información de la imagen asociada (si existe)
     *
     * Solo retorna avistamientos con estatus 'Alta' (no eliminados lógicamente).
     * Los resultados se ordenan por fecha de registro en orden descendente.
     *
     * @param reporteId ID del reporte del cual se desean obtener los avistamientos
     * @return List<AvistamientoConRelaciones> Lista de avistamientos con sus relaciones completas
     */
    public List<AvistamientoConRelaciones> obtenerAvistamientosPorReporte(int reporteId) {
        List<AvistamientoConRelaciones> avistamientos = new ArrayList<>();

        // Consulta SQL con JOIN para obtener información completa del avistamiento
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

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Construcción del objeto Avistamiento con datos de la consulta
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

                // Construcción del objeto Usuario asociado al avistamiento
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Construcción del objeto ImagenMascota (opcional - puede ser null)
                ImagenMascota imagen = null;
                if (rs.getObject("imagenid") != null && rs.getInt("imagenid") > 0) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                }

                // Creación del objeto compuesto con todas las relaciones
                AvistamientoConRelaciones avistamientoCompleto = new AvistamientoConRelaciones(avistamiento, usuario, imagen);
                avistamientos.add(avistamientoCompleto);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en obtenerAvistamientosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return avistamientos;
    }

    /**
     * Inserta un nuevo avistamiento en la base de datos.
     *
     * El método establece automáticamente:
     * - La fecha de registro como la fecha actual (CURRENT_DATE)
     * - El estatus inicial como 'Alta'
     *
     * Maneja la imagen de referencia como campo opcional, estableciendo NULL
     * si no se proporciona una imagen válida.
     *
     * @param avistamiento Objeto Avistamiento con los datos a insertar
     * @return boolean true si la inserción fue exitosa, false en caso contrario
     */
    public boolean agregarAvistamiento(Avistamiento avistamiento) {
        String sql = "INSERT INTO avistamiento (r_reporte, r_usuarioreportante, fecha_avistamiento, " +
                "ubicacion, descripcion, contacto, fecha_registro, r_imagen, estatus) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, 'Alta')";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamiento.getR_Reporte());
            ps.setInt(2, avistamiento.getR_UsuarioReportante());
            ps.setDate(3, new java.sql.Date(avistamiento.getFecha_Avistamiento().getTime()));
            ps.setString(4, avistamiento.getUbicacion());
            ps.setString(5, avistamiento.getDescripcion());
            ps.setString(6, avistamiento.getContacto());

            // Manejo de imagen opcional: establece NULL si no hay imagen válida
            if (avistamiento.getR_Imagen() > 0) {
                ps.setInt(7, avistamiento.getR_Imagen());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en agregarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtiene un avistamiento específico por su ID con información completa de relaciones.
     * Realiza un JOIN con las tablas usuarios e imagenmascota para obtener datos relacionados.
     *
     * Solo retorna el avistamiento si tiene estatus 'Alta' (no eliminado lógicamente).
     *
     * @param avistamientoId ID único del avistamiento a buscar
     * @return AvistamientoConRelaciones Objeto con el avistamiento y sus relaciones, null si no se encuentra
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

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamientoId);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Construcción del objeto Avistamiento
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

                // Construcción del objeto Usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Construcción del objeto ImagenMascota (opcional)
                ImagenMascota imagen = null;
                if (rs.getObject("imagenid") != null && rs.getInt("imagenid") > 0) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                }

                return new AvistamientoConRelaciones(avistamiento, usuario, imagen);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en obtenerAvistamientoPorId - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return null;
    }

    /**
     * Actualiza la información de un avistamiento existente.
     *
     * Solo permite editar los campos modificables:
     * - Fecha del avistamiento
     * - Ubicación
     * - Descripción
     * - Información de contacto
     * - Imagen de referencia
     *
     * La operación solo se realiza si el avistamiento tiene estatus 'Alta'.
     *
     * @param avistamiento Objeto Avistamiento con los datos actualizados
     * @return boolean true si la actualización fue exitosa, false en caso contrario
     */
    public boolean editarAvistamiento(Avistamiento avistamiento) {
        String sql = "UPDATE avistamiento SET fecha_avistamiento = ?, ubicacion = ?, " +
                "descripcion = ?, contacto = ?, r_imagen = ? " +
                "WHERE avistamientoid = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(avistamiento.getFecha_Avistamiento().getTime()));
            ps.setString(2, avistamiento.getUbicacion());
            ps.setString(3, avistamiento.getDescripcion());
            ps.setString(4, avistamiento.getContacto());

            // Manejo de imagen opcional
            if (avistamiento.getR_Imagen() > 0) {
                ps.setInt(5, avistamiento.getR_Imagen());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            ps.setInt(6, avistamiento.getAvistamientoID());

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en editarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Realiza una eliminación lógica de un avistamiento cambiando su estatus a 'Baja'.
     *
     * Esta implementación utiliza eliminación lógica en lugar de eliminación física
     * para mantener la integridad referencial y permitir auditorías futuras.
     *
     * @param avistamientoId ID único del avistamiento a eliminar
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarAvistamiento(int avistamientoId) {
        String sql = "UPDATE avistamiento SET estatus = 'Baja' WHERE avistamientoid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamientoId);

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en eliminarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Verifica si un usuario específico es el propietario (reportante) de un avistamiento.
     *
     * Esta validación es crucial para operaciones de edición y eliminación,
     * asegurando que solo el usuario que reportó el avistamiento pueda modificarlo.
     *
     * Solo considera avistamientos con estatus 'Alta'.
     *
     * @param avistamientoId ID del avistamiento a verificar
     * @param usuarioId ID del usuario cuya propiedad se desea verificar
     * @return boolean true si el usuario es propietario del avistamiento, false en caso contrario
     */
    public boolean esUsuarioPropietarioAvistamiento(int avistamientoId, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM avistamiento WHERE avistamientoid = ? AND r_usuarioreportante = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, avistamientoId);
            ps.setInt(2, usuarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en esUsuarioPropietarioAvistamiento - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return false;
    }

    /**
     * Cuenta el número total de avistamientos activos asociados a un reporte específico.
     *
     * Solo considera avistamientos con estatus 'Alta' para proporcionar
     * un conteo preciso de avistamientos válidos.
     *
     * Este método es útil para generar estadísticas y validaciones
     * antes de realizar operaciones sobre reportes.
     *
     * @param reporteId ID del reporte del cual se desea contar los avistamientos
     * @return int Número de avistamientos activos asociados al reporte
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
            System.err.println("ERROR: Excepción en contarAvistamientosPorReporte - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return 0;
    }
}