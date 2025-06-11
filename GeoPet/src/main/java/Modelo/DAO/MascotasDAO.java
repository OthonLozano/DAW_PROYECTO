package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.MascotaConRelaciones;
import Modelo.JavaBeans.Mascotas;
import Modelo.JavaBeans.ImagenMascota;
import Modelo.JavaBeans.Especie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Mascotas.
 * Proporciona métodos para interactuar con la tabla 'mascotas' en la base de datos,
 * incluyendo operaciones complejas que involucran relaciones con especies e imágenes.
 *
 * Esta clase es fundamental en el sistema de gestión de mascotas perdidas, manejando:
 * - Registro completo de mascotas con todas sus características
 * - Consultas con relaciones complejas (especie, imágenes)
 * - Listados específicos por usuario y estados
 * - Validaciones de negocio para reportes de desaparición
 * - Operaciones de búsqueda y filtrado avanzado
 *
 * Las mascotas son entidades centrales que conectan con usuarios propietarios,
 * especies, imágenes y reportes de desaparición, proporcionando toda la información
 * necesaria para identificación y búsqueda efectiva.
 *
 * Características principales:
 * - Soporte para tipos enum de PostgreSQL (sexo_animal, estado_mascota)
 * - Consultas optimizadas con subconsultas para obtener imagen principal
 * - Filtrado por disponibilidad para reportes de desaparición
 * - Eliminación lógica para mantener integridad histórica
 * - Validaciones de microchip y características distintivas
 *
 * @author Sistema de Gestión DAW
 * @version 1.0
 */
public class MascotasDAO {

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
     * Registra una nueva mascota en la base de datos con todas sus características.
     *
     * Este método maneja la inserción completa de una mascota incluyendo:
     * - Información básica (nombre, edad, color, sexo)
     * - Características distintivas para identificación
     * - Información de microchip si está disponible
     * - Referencias a usuario propietario y especie
     * - Estados y fechas de registro
     *
     * Utiliza casting de tipos PostgreSQL para enum fields (::sexo_animal, ::estado_mascota)
     * para asegurar compatibilidad con tipos definidos en la base de datos.
     * Establece automáticamente el estatus como 'Alta' para nuevas mascotas.
     *
     * @param mascota Objeto Mascotas con todos los datos a insertar
     * @return boolean true si el registro fue exitoso, false en caso contrario
     */
    public boolean RegistrarMascota(Mascotas mascota) {
        String sql = "INSERT INTO mascotas (r_usuario, nombre, r_especie, edad, sexo, color, " +
                "caracteristicasdistintivas, microchip, numero_microchip, estado, fecha_registro, estatus) " +
                "VALUES (?, ?, ?, ?, ?::sexo_animal, ?, ?, ?, ?, ?::estado_mascota, ?, 'Alta')";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascota.getR_Usuario());
            ps.setString(2, mascota.getNombre());
            ps.setInt(3, mascota.getR_Especie());
            ps.setInt(4, mascota.getEdad());
            ps.setString(5, mascota.getSexo());
            ps.setString(6, mascota.getColor());
            ps.setString(7, mascota.getCaracteristicasDistintivas());
            ps.setBoolean(8, mascota.isMicrochip());
            ps.setInt(9, mascota.getNumero_Microchip());
            ps.setString(10, mascota.getEstado());
            ps.setDate(11, new java.sql.Date(mascota.getFecha_Registro().getTime()));
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al registrar mascota - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Obtiene todas las mascotas registradas en el sistema sin filtros.
     *
     * Retorna una lista completa de todas las mascotas incluyendo aquellas
     * con estatus 'Alta' y 'Baja'. Este método es útil para operaciones
     * administrativas que requieren acceso a todos los registros.
     *
     * @return List<Mascotas> Lista de todas las mascotas en el sistema
     */
    public List<Mascotas> ListarMascotas() {
        List<Mascotas> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setR_Usuario(rs.getInt("r_usuario"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setR_Especie(rs.getInt("r_especie"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setMicrochip(rs.getBoolean("microchip"));
                mascota.setNumero_Microchip(rs.getInt("numero_microchip"));
                mascota.setEstado(rs.getString("estado"));
                mascota.setFecha_Registro(rs.getDate("fecha_registro"));
                lista.add(mascota);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar mascotas - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Obtiene todas las mascotas activas de un usuario específico.
     *
     * Filtra las mascotas por usuario propietario y estatus 'Alta',
     * excluyendo aquellas que han sido eliminadas lógicamente.
     * Es fundamental para mostrar la lista personal de mascotas
     * de cada usuario en la interfaz.
     *
     * @param usuarioId ID del usuario propietario de las mascotas
     * @return List<Mascotas> Lista de mascotas activas del usuario
     */
    public List<Mascotas> ListarMascotasPorUsuario(int usuarioId) {
        List<Mascotas> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas WHERE estatus = 'Alta' AND r_usuario = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setR_Usuario(rs.getInt("r_usuario"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setR_Especie(rs.getInt("r_especie"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setMicrochip(rs.getBoolean("microchip"));
                mascota.setNumero_Microchip(rs.getInt("numero_microchip"));
                mascota.setEstado(rs.getString("estado"));
                mascota.setFecha_Registro(rs.getDate("fecha_registro"));
                lista.add(mascota);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar mascotas por usuario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Realiza una eliminación lógica de una mascota cambiando su estatus a 'Baja'.
     *
     * La eliminación lógica preserva todos los datos de la mascota en la base
     * de datos manteniendo la integridad referencial con reportes, avistamientos
     * e imágenes asociadas. Permite auditorías y recuperación de datos si es necesario.
     *
     * @param id ID único de la mascota a eliminar lógicamente
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean EliminarMascota(int id) {
        String sql = "UPDATE mascotas SET estatus = 'Baja' WHERE mascotaid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al eliminar mascota - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Actualiza completamente la información de una mascota existente.
     *
     * Permite modificar todos los campos de la mascota incluyendo características
     * físicas, información de microchip, estado actual y referencias relacionales.
     * Utiliza casting de tipos PostgreSQL para campos enum.
     *
     * @param mascota Objeto Mascotas con todos los datos actualizados
     * @return boolean true si la modificación fue exitosa, false en caso contrario
     */
    public boolean ModificarMascota(Mascotas mascota) {
        String sql = "UPDATE mascotas SET r_usuario = ?, nombre = ?, r_especie = ?, edad = ?, " +
                "sexo = ?::sexo_animal, color = ?, caracteristicasdistintivas = ?, microchip = ?, " +
                "numero_microchip = ?, estado = ?::estado_mascota, fecha_registro = ? WHERE mascotaid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascota.getR_Usuario());
            ps.setString(2, mascota.getNombre());
            ps.setInt(3, mascota.getR_Especie());
            ps.setInt(4, mascota.getEdad());
            ps.setString(5, mascota.getSexo());
            ps.setString(6, mascota.getColor());
            ps.setString(7, mascota.getCaracteristicasDistintivas());
            ps.setBoolean(8, mascota.isMicrochip());
            ps.setInt(9, mascota.getNumero_Microchip());
            ps.setString(10, mascota.getEstado());
            ps.setDate(11, new java.sql.Date(mascota.getFecha_Registro().getTime()));
            ps.setInt(12, mascota.getMascotaID());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al modificar mascota - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Busca y obtiene una mascota específica por su ID único.
     *
     * Retorna toda la información disponible de la mascota independientemente
     * de su estatus. Si no se encuentra la mascota, retorna un objeto Mascotas
     * vacío para evitar NullPointerException en el código cliente.
     *
     * @param id ID único de la mascota a buscar
     * @return Mascotas Objeto con los datos de la mascota, vacío si no se encuentra
     */
    public Mascotas BuscarMascota(int id) {
        Mascotas mascota = new Mascotas();
        String sql = "SELECT * FROM mascotas WHERE mascotaid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setR_Usuario(rs.getInt("r_usuario"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setR_Especie(rs.getInt("r_especie"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setMicrochip(rs.getBoolean("microchip"));
                mascota.setNumero_Microchip(rs.getInt("numero_microchip"));
                mascota.setEstado(rs.getString("estado"));
                mascota.setFecha_Registro(rs.getDate("fecha_registro"));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al buscar mascota por ID - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return mascota;
    }

    /**
     * Obtiene mascotas de un usuario con información completa de relaciones.
     *
     * Ejecuta una consulta compleja con JOINs que incluye:
     * - Información completa de la mascota
     * - Datos de la especie (nombre y descripción)
     * - Imagen principal (más reciente) si existe
     *
     * Utiliza una subconsulta optimizada con ROW_NUMBER() para obtener únicamente
     * la imagen más reciente de cada mascota, evitando duplicados y mejorando
     * el rendimiento.
     *
     * Solo retorna mascotas con estatus 'Alta' ordenadas por fecha de registro
     * descendente para mostrar las más recientes primero.
     *
     * @param usuarioId ID del usuario propietario de las mascotas
     * @return List<MascotaConRelaciones> Lista de mascotas con información completa
     */
    public List<MascotaConRelaciones> ListarMascotasConRelacionesPorUsuario(int usuarioId) {
        List<MascotaConRelaciones> lista = new ArrayList<>();
        String sql = """
        SELECT 
            m.mascotaid, m.r_usuario, m.nombre, m.r_especie, m.edad, m.sexo, m.color,
            m.caracteristicasdistintivas, m.microchip, m.numero_microchip, m.estado, m.fecha_registro,
            e.especieid, e.nombre as nombre_especie, e.descripcion as descripcion_especie,
            img.imagenid, img.url_imagen, img.fecha_carga, img.estatus as imagen_estatus
        FROM mascotas m
        INNER JOIN especie e ON m.r_especie = e.especieid
        LEFT JOIN (
            SELECT DISTINCT 
                imagenid, r_mascota, url_imagen, fecha_carga, estatus,
                ROW_NUMBER() OVER (PARTITION BY r_mascota ORDER BY fecha_carga DESC) as rn
            FROM imagenmascota 
            WHERE estatus = 'Alta'
        ) img ON m.mascotaid = img.r_mascota AND img.rn = 1
        WHERE m.r_usuario = ? AND m.estatus = 'Alta'
        ORDER BY m.fecha_registro DESC
        """;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Construcción del objeto Mascotas
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setR_Usuario(rs.getInt("r_usuario"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setR_Especie(rs.getInt("r_especie"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setMicrochip(rs.getBoolean("microchip"));
                mascota.setNumero_Microchip(rs.getInt("numero_microchip"));
                mascota.setEstado(rs.getString("estado"));
                mascota.setFecha_Registro(rs.getDate("fecha_registro"));

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
                    imagen.setEstatus(rs.getString("imagen_estatus"));
                }

                // Ensamblaje del objeto MascotaConRelaciones
                MascotaConRelaciones mascotaCompleta = new MascotaConRelaciones(mascota, especie, imagen);
                lista.add(mascotaCompleta);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en listarMascotasConRelacionesPorUsuario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Obtiene mascotas de un usuario que no tienen reportes de desaparición activos.
     *
     * Esta consulta especializada filtra mascotas que están disponibles para
     * crear nuevos reportes de desaparición. Excluye mascotas que ya tienen
     * reportes activos (estatus 'Alta' y estadoreporte != 'Activo') para evitar
     * reportes duplicados.
     *
     * Utiliza una subconsulta NOT IN para filtrar mascotas que aparecen en
     * reportes activos, asegurando que solo se muestren mascotas disponibles
     * para nuevos reportes.
     *
     * Los resultados se ordenan alfabéticamente por nombre para facilitar
     * la selección en interfaces de usuario.
     *
     * @param usuarioId ID del usuario propietario de las mascotas
     * @return List<Mascotas> Lista de mascotas disponibles para reportar como perdidas
     */
    public List<Mascotas> ListarMascotasSinReporteActivo(int usuarioId) {
        List<Mascotas> lista = new ArrayList<>();
        String sql = "SELECT m.* FROM mascotas m " +
                "WHERE m.r_usuario = ? AND m.estatus = 'Alta' " +
                "AND m.mascotaid NOT IN (" +
                "    SELECT r.r_mascota FROM reportedesaparicion r " +
                "    WHERE r.estatus = 'Alta' AND r.estadoreporte != 'Activo'" +
                ") " +
                "ORDER BY m.nombre";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mascotas mascota = new Mascotas();
                mascota.setMascotaID(rs.getInt("mascotaid"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setEdad(rs.getInt("edad"));
                mascota.setSexo(rs.getString("sexo"));
                mascota.setColor(rs.getString("color"));
                mascota.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                mascota.setR_Usuario(rs.getInt("r_usuario"));
                mascota.setR_Especie(rs.getInt("r_especie"));
                mascota.setEstado(rs.getString("estado"));
                lista.add(mascota);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Excepción en listarMascotasSinReporteActivo - " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos();
        }

        return lista;
    }
}