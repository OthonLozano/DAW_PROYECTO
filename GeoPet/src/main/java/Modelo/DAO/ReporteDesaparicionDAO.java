package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Reportes de Desaparición de mascotas.
 * Proporciona métodos para interactuar con la tabla 'reportedesaparicion' en la base de datos,
 * incluyendo operaciones complejas que involucran múltiples entidades relacionadas.
 *
 * Esta clase es el núcleo del sistema de gestión de mascotas perdidas, manejando:
 * - Registro de nuevos reportes de desaparición
 * - Consultas complejas con JOINs para obtener información completa
 * - Listados específicos por usuario y filtros diversos
 * - Verificación de estados y validaciones de negocio
 * - Operaciones de búsqueda y actualización de estados
 *
 * Los reportes de desaparición son entidades centrales que conectan mascotas,
 * usuarios, especies e imágenes, proporcionando toda la información necesaria
 * para facilitar la búsqueda y recuperación de mascotas extraviadas.
 *
 * Características principales:
 * - Soporte para consultas con relaciones múltiples (mascota, usuario, especie, imagen)
 * - Filtrado por usuario para vistas personalizadas
 * - Validación de estados de reportes activos
 * - Eliminación lógica para mantener integridad histórica
 * - Optimización de consultas para mejor rendimiento
 */
public class ReporteDesaparicionDAO {

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
     * Registra un nuevo reporte de desaparición en la base de datos.
     *
     * Este método maneja la inserción completa de un reporte con todos sus campos,
     * incluyendo referencias a mascota y usuario, fechas, ubicaciones, descripción
     * de la situación, recompensa ofrecida y estados del reporte.
     *
     * Utiliza casting de tipos PostgreSQL para enum fields (::reporte, ::tipo_estatus)
     * para asegurar compatibilidad con tipos definidos en la base de datos.
     *
     * @param reporte Objeto ReporteDesaparicion con todos los datos a insertar
     * @return boolean true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarReporte(ReporteDesaparicion reporte) {
        String sql = "INSERT INTO ReporteDesaparicion (r_mascota, r_usuario, fechadesaparicion, ubicacionultimavez, " +
                "descripcionsituacion, recompensa, estadoreporte, fecha_registro, estatus) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?::reporte, ?, ?::tipo_estatus)";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporte.getR_Mascota());
            ps.setInt(2, reporte.getR_Usuario());
            ps.setDate(3, new java.sql.Date(reporte.getFechaDesaparicion().getTime()));
            ps.setString(4, reporte.getUbicacionUltimaVez());
            ps.setString(5, reporte.getDescripcionSituacion());
            ps.setDouble(6, reporte.getRecompensa());
            ps.setString(7, reporte.getEstadoReporte());
            ps.setDate(8, new java.sql.Date(reporte.getFecha_Registro().getTime()));
            ps.setString(9, reporte.getEstatus());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al registrar reporte - " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtiene todos los reportes activos con información completa de todas las entidades relacionadas.
     *
     * Ejecuta una consulta compleja con múltiples JOINs para obtener:
     * - Información completa del reporte
     * - Datos del usuario propietario
     * - Información detallada de la mascota
     * - Datos de la especie
     * - URL de imagen principal (si existe)
     *
     * Solo retorna reportes con estatus 'Alta' y estadoreporte 'Activo',
     * garantizando que solo se muestren casos activos de búsqueda.
     * Los resultados se ordenan por fecha de registro descendente.
     *
     * @return List<ReporteConRelaciones> Lista de reportes completos con todas sus relaciones
     */
    public List<ReporteConRelaciones> listarReportesCompletos() {
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
                "WHERE r.estatus = 'Alta' AND r.estadoreporte = 'Activo' " +
                "ORDER BY r.fecha_registro DESC";

        return ejecutarConsultaReportes(sql, null);
    }

    /**
     * Obtiene todos los reportes activos excluyendo los del usuario especificado.
     *
     * Este método es útil para mostrar reportes de otras personas donde el usuario
     * actual puede contribuir con avistamientos o información. Excluye los reportes
     * propios para evitar redundancia en vistas de búsqueda colaborativa.
     *
     * Si no se proporciona usuarioIdActual (null), retorna todos los reportes activos.
     *
     * @param usuarioIdActual ID del usuario actual a excluir, null para incluir todos
     * @return List<ReporteConRelaciones> Lista de reportes externos con información completa
     */
    public List<ReporteConRelaciones> listarReportesExternos(Integer usuarioIdActual) {
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
                "WHERE r.estatus = 'Alta' AND r.estadoreporte = 'Activo'";

        // Agregar filtro de exclusión de usuario si se proporciona
        if (usuarioIdActual != null) {
            sql += " AND r.r_usuario != ?";
        }

        sql += " ORDER BY r.fecha_registro DESC";

        return ejecutarConsultaReportes(sql, usuarioIdActual);
    }

    /**
     * Método auxiliar para ejecutar consultas de reportes con manejo uniforme de resultados.
     *
     * Centraliza la lógica de ejecución de consultas complejas que involucran múltiples
     * entidades relacionadas, evitando duplicación de código y asegurando consistencia
     * en el manejo de resultados y construcción de objetos.
     *
     * Maneja la creación completa de objetos ReporteConRelaciones incluyendo:
     * - ReporteDesaparicion con todos sus campos
     * - Usuario propietario con información de contacto
     * - Mascota con características completas
     * - Especie con nombre y descripción
     * - Imagen principal (opcional)
     *
     * @param sql Consulta SQL a ejecutar
     * @param usuarioIdExcluir ID de usuario a excluir como parámetro, null si no aplica
     * @return List<ReporteConRelaciones> Lista de reportes construidos a partir de los resultados
     */
    private List<ReporteConRelaciones> ejecutarConsultaReportes(String sql, Integer usuarioIdExcluir) {
        List<ReporteConRelaciones> lista = new ArrayList<>();

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);

            // Establecer parámetro de exclusión de usuario si se proporciona
            if (usuarioIdExcluir != null) {
                ps.setInt(1, usuarioIdExcluir);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                // Construcción del objeto ReporteDesaparicion
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

                // Construcción del objeto Usuario propietario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(reporte.getR_Usuario());
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));

                // Construcción del objeto Mascota con características
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(reporte.getR_Mascota());
                mascota.setNombre(rs.getString("mascota_nombre"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));

                // Construcción del objeto Especie
                Especie especie = new Especie();
                especie.setNombre(rs.getString("especie_nombre"));

                // Construcción del objeto ImagenMascota (opcional)
                ImagenMascota imagen = null;
                String urlImagen = rs.getString("url_imagen");
                if (urlImagen != null && !urlImagen.trim().isEmpty()) {
                    imagen = new ImagenMascota();
                    imagen.setURL_Imagen(urlImagen);
                }

                // Ensamblaje del objeto ReporteConRelaciones completo
                ReporteConRelaciones reporteCompleto = new ReporteConRelaciones();
                reporteCompleto.setReporte(reporte);
                reporteCompleto.setUsuario(usuario);
                reporteCompleto.setMascota(mascota);
                reporteCompleto.setEspecie(especie);
                reporteCompleto.setImagen(imagen);

                lista.add(reporteCompleto);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en ejecutarConsultaReportes - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Obtiene todos los reportes de un usuario específico con información completa.
     *
     * Esta consulta utiliza una subconsulta optimizada con ROW_NUMBER() para obtener
     * únicamente la imagen más reciente de cada mascota, mejorando el rendimiento
     * y evitando duplicados en los resultados.
     *
     * Incluye información completa de todas las entidades relacionadas:
     * mascota, usuario, especie e imagen principal. Solo retorna reportes
     * con estatus 'Alta' ordenados por fecha de registro descendente.
     *
     * @param usuarioId ID del usuario del cual se desean obtener los reportes
     * @return List<ReporteConRelaciones> Lista de reportes del usuario con información completa
     */
    public List<ReporteConRelaciones> listarReportesPorUsuario(int usuarioId) {
        List<ReporteConRelaciones> lista = new ArrayList<>();
        String sql = """
            SELECT 
                r.reporteid, r.r_mascota, r.r_usuario, r.fechadesaparicion,
                r.ubicacionultimavez, r.descripcionsituacion, r.recompensa,
                r.estadoreporte, r.fecha_registro, r.estatus,
                m.mascotaid, m.nombre as nombre_mascota, m.edad, m.sexo, m.color,
                m.caracteristicasdistintivas, m.estado as estado_mascota,
                u.usuarioid, u.nombre as nombre_usuario, u.apellidopat, 
                u.apellidomat, u.telefono, u.email,
                e.especieid, e.nombre as nombre_especie, e.descripcion as descripcion_especie,
                img.imagenid, img.url_imagen, img.fecha_carga
            FROM ReporteDesaparicion r
            INNER JOIN Mascotas m ON r.r_mascota = m.mascotaid
            INNER JOIN Usuarios u ON r.r_usuario = u.usuarioid
            INNER JOIN Especie e ON m.r_especie = e.especieid
            LEFT JOIN (
                SELECT DISTINCT 
                    imagenid, r_mascota, url_imagen, fecha_carga,
                    ROW_NUMBER() OVER (PARTITION BY r_mascota ORDER BY fecha_carga DESC) as rn
                FROM ImagenMascota 
                WHERE estatus = 'Alta'
            ) img ON m.mascotaid = img.r_mascota AND img.rn = 1
            WHERE r.r_usuario = ? AND r.estatus = 'Alta'
            ORDER BY r.fecha_registro DESC
            """;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Construcción del objeto ReporteDesaparicion
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

                // Construcción del objeto Mascota
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setEstado(rs.getString("estado_mascota"));

                // Construcción del objeto Usuario
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("nombre_usuario"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));

                // Construcción del objeto Especie
                Especie especie = new Especie();
                especie.setEspecieID(rs.getInt("especieid"));
                especie.setNombre(rs.getString("nombre_especie"));
                especie.setDescripcion(rs.getString("descripcion_especie"));

                // Construcción del objeto ImagenMascota (opcional)
                ImagenMascota imagen = null;
                if (rs.getString("url_imagen") != null) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setR_Mascota(mascota.getMascotaID());
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                    imagen.setFecha_Carga(rs.getDate("fecha_carga"));
                    imagen.setEstatus("Alta");
                }

                // Ensamblaje del objeto ReporteConRelaciones
                ReporteConRelaciones reporteCompleto = new ReporteConRelaciones(reporte, mascota, usuario, especie, imagen);
                lista.add(reporteCompleto);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en listarReportesPorUsuario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Busca y obtiene un reporte específico por su ID único.
     *
     * Retorna únicamente los datos del reporte sin las entidades relacionadas.
     * Útil para operaciones que requieren solo información básica del reporte
     * o para validaciones antes de realizar operaciones más complejas.
     *
     * @param id ID único del reporte a buscar
     * @return ReporteDesaparicion Objeto con los datos del reporte, vacío si no se encuentra
     */
    public ReporteDesaparicion buscarReporte(int id) {
        ReporteDesaparicion reporte = new ReporteDesaparicion();
        String sql = "SELECT * FROM ReporteDesaparicion WHERE reporteid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
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
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al buscar reporte - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return reporte;
    }

    /**
     * Actualiza completamente la información de un reporte existente.
     *
     * Permite modificar todos los campos del reporte incluyendo referencias
     * a mascota y usuario, fechas, ubicaciones, descripción, recompensa y estados.
     * Utiliza casting de tipos PostgreSQL para campos enum.
     *
     * @param reporte Objeto ReporteDesaparicion con todos los datos actualizados
     * @return boolean true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarReporte(ReporteDesaparicion reporte) {
        String sql = "UPDATE ReporteDesaparicion SET r_mascota = ?, r_usuario = ?, fechadesaparicion = ?, " +
                "ubicacionultimavez = ?, descripcionsituacion = ?, recompensa = ?, estadoreporte = ?::reporte, " +
                "fecha_registro = ?, estatus = ?::tipo_estatus WHERE reporteid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporte.getR_Mascota());
            ps.setInt(2, reporte.getR_Usuario());
            ps.setDate(3, new java.sql.Date(reporte.getFechaDesaparicion().getTime()));
            ps.setString(4, reporte.getUbicacionUltimaVez());
            ps.setString(5, reporte.getDescripcionSituacion());
            ps.setDouble(6, reporte.getRecompensa());
            ps.setString(7, reporte.getEstadoReporte());
            ps.setDate(8, new java.sql.Date(reporte.getFecha_Registro().getTime()));
            ps.setString(9, reporte.getEstatus());
            ps.setInt(10, reporte.getReporteID());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al modificar reporte - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Realiza una eliminación lógica de un reporte cambiando su estatus a 'Baja'.
     *
     * La eliminación lógica preserva la información del reporte en la base de datos
     * para propósitos de auditoría y análisis, mientras lo oculta de las consultas
     * normales del sistema.
     *
     * @param id ID único del reporte a eliminar lógicamente
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarReporte(int id) {
        String sql = "UPDATE ReporteDesaparicion SET estatus = 'Baja' WHERE reporteid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al eliminar reporte - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Verifica si existe un reporte activo para una mascota específica.
     *
     * Busca reportes con estatus 'Alta' y estadoreporte diferente de 'En casa',
     * lo que indica que la mascota aún se considera perdida y tiene un reporte
     * activo de búsqueda.
     *
     * Esta validación es crucial para evitar reportes duplicados y para
     * determinar el estado actual de búsqueda de una mascota.
     *
     * @param mascotaId ID de la mascota a verificar
     * @return boolean true si existe un reporte activo, false en caso contrario
     */
    public boolean existeReporteActivoPorMascota(int mascotaId) {
        String sql = "SELECT COUNT(*) as total FROM reportedesaparicion " +
                "WHERE r_mascota = ? AND estatus = 'Alta' AND estadoreporte != 'En casa'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                return total > 0;
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en existeReporteActivoPorMascota - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return false;
    }

    /**
     * Obtiene el reporte activo más reciente de una mascota específica.
     *
     * Busca reportes con estatus 'Alta' y estadoreporte diferente de 'Activo'
     * (nota: parece haber un error lógico aquí, debería ser '!= En casa'),
     * ordenado por fecha de registro descendente para obtener el más reciente.
     *
     * @param mascotaId ID de la mascota de la cual obtener el reporte activo
     * @return ReporteDesaparicion Objeto con el reporte activo, null si no existe
     */
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
            System.err.println("ERROR: Excepción en obtenerReporteActivoPorMascota - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return null;
    }

    /**
     * Obtiene un reporte específico con todas sus relaciones completas.
     *
     * Ejecuta una consulta compleja que incluye:
     * - Información completa del reporte
     * - Datos completos del usuario propietario (incluyendo contacto y ubicación)
     * - Información detallada de la mascota (incluyendo microchip y estado)
     * - Datos de la especie con descripción
     * - Imagen principal de la mascota
     *
     * Esta es la consulta más completa disponible para obtener toda la información
     * necesaria para mostrar un reporte en detalle, incluyendo datos para contacto
     * y características completas para identificación.
     *
     * Solo retorna reportes con estatus 'Alta' para garantizar datos válidos.
     *
     * @param reporteId ID único del reporte a obtener
     * @return ReporteConRelaciones Objeto completo con todas las relaciones, null si no se encuentra
     */
    public ReporteConRelaciones obtenerReporteCompleto(int reporteId) {
        ReporteConRelaciones reporteCompleto = null;

        String sql = "SELECT " +
                "r.reporteid, r.r_usuario, r.r_mascota, r.fechadesaparicion, " +
                "r.ubicacionultimavez, r.descripcionsituacion, r.recompensa, " +
                "r.estadoreporte, r.fecha_registro, r.estatus, " +
                "u.usuarioid, u.nombre as usuario_nombre, u.apellidopat, u.apellidomat, " +
                "u.telefono, u.email, u.direccion, u.ciudad, " +
                "m.mascotaid, m.nombre as mascota_nombre, m.edad, m.sexo, m.color, " +
                "m.caracteristicasdistintivas, m.microchip, m.numero_microchip, m.estado as estado_mascota, m.r_especie, " +
                "e.especieid, e.nombre as especie_nombre, e.descripcion as especie_descripcion, " +
                "i.imagenid, i.url_imagen " +
                "FROM reportedesaparicion r " +
                "INNER JOIN usuarios u ON r.r_usuario = u.usuarioid " +
                "INNER JOIN mascotas m ON r.r_mascota = m.mascotaid " +
                "LEFT JOIN especie e ON m.r_especie = e.especieid " +
                "LEFT JOIN imagenmascota i ON m.mascotaid = i.r_mascota " +
                "WHERE r.reporteid = ? AND r.estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, reporteId);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Construcción del objeto ReporteDesaparicion
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

                // Construcción del objeto Mascota con información completa
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setR_Usuario(rs.getInt("r_usuario"));
                mascota.setR_Especie(rs.getInt("r_especie"));
                mascota.setNombre(rs.getString("mascota_nombre"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setColor(rs.getString("color"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setMicrochip(rs.getBoolean("microchip"));
                mascota.setNumero_Microchip(rs.getInt("numero_microchip"));
                mascota.setEstado(rs.getString("estado_mascota"));

                // Construcción del objeto Usuario propietario con información de contacto
                Usuarios usuario = new Usuarios();
                usuario.setUsuarioID(rs.getInt("usuarioid"));
                usuario.setNombre(rs.getString("usuario_nombre"));
                usuario.setApellidoPat(rs.getString("apellidopat"));
                usuario.setApellidoMat(rs.getString("apellidomat"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setCiudad(rs.getString("ciudad"));

                // Construcción del objeto Especie (opcional)
                Especie especie = null;
                if (rs.getObject("especieid") != null && rs.getInt("especieid") > 0) {
                    especie = new Especie();
                    especie.setEspecieID(rs.getInt("especieid"));
                    especie.setNombre(rs.getString("especie_nombre"));
                    especie.setDescripcion(rs.getString("especie_descripcion"));
                }

                // Construcción del objeto ImagenMascota (opcional)
                ImagenMascota imagen = null;
                if (rs.getObject("imagenid") != null && rs.getInt("imagenid") > 0) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenid"));
                    imagen.setR_Mascota(rs.getInt("mascotaid"));
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                }

                // Ensamblaje del objeto ReporteConRelaciones completo
                reporteCompleto = new ReporteConRelaciones(reporte, mascota, usuario, especie, imagen);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en obtenerReporteCompleto - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return reporteCompleto;
    }

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
}