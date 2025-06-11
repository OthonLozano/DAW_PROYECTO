package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.ImagenMascota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO (Data Access Object) para gestionar operaciones CRUD de Imágenes de Mascotas.
 * Proporciona métodos para interactuar con la tabla 'ImagenMascota' en la base de datos,
 * incluyendo operaciones de consulta, inserción, actualización y eliminación física/lógica.
 *
 * Esta clase implementa el patrón DAO para separar la lógica de acceso a datos
 * de la lógica de negocio, proporcionando una interfaz limpia para las operaciones
 * relacionadas con la gestión de imágenes asociadas a mascotas.
 *
 * Las imágenes son elementos fundamentales para la identificación visual de mascotas
 * en reportes de pérdida y avistamientos, permitiendo a los usuarios reconocer
 * fácilmente a las mascotas extraviadas.
 *
 * Características principales:
 * - Soporte para eliminación lógica y física
 * - Gestión transaccional para operaciones batch
 * - Obtención de imagen principal por mascota
 * - Contadores y validaciones de existencia
 */
public class ImagenMascotaDAO {

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
     * Registra una nueva imagen de mascota en la base de datos.
     *
     * El método establece automáticamente:
     * - El estatus inicial como 'Alta'
     * - La fecha de carga proporcionada en el objeto ImagenMascota
     *
     * Esta función es fundamental para asociar evidencia visual a las mascotas
     * reportadas, facilitando su identificación posterior en avistamientos.
     *
     * @param imagen Objeto ImagenMascota con los datos a insertar
     * @return boolean true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrarImagen(ImagenMascota imagen) {
        String sql = "INSERT INTO ImagenMascota (r_mascota, url_imagen, fecha_carga, estatus) VALUES (?, ?, ?, 'Alta')";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, imagen.getR_Mascota());
            ps.setString(2, imagen.getURL_Imagen());
            ps.setTimestamp(3, new java.sql.Timestamp(imagen.getFecha_Carga().getTime()));
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al registrar imagen - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Registra múltiples imágenes en una sola transacción atómica.
     *
     * Utiliza operaciones batch para optimizar el rendimiento y garantizar
     * la consistencia de datos mediante transacciones. Si alguna imagen falla,
     * toda la operación se revierte automáticamente.
     *
     * Esta funcionalidad es esencial para casos donde se suben múltiples
     * imágenes simultáneamente de una mascota, asegurando que todas se
     * registren correctamente o ninguna en caso de error.
     *
     * @param imagenes Lista de objetos ImagenMascota a insertar
     * @return boolean true si todas las imágenes se registraron exitosamente, false en caso contrario
     */
    public boolean registrarImagenes(List<ImagenMascota> imagenes) {
        String sql = "INSERT INTO ImagenMascota (r_mascota, url_imagen, fecha_carga, estatus) VALUES (?, ?, ?, 'Alta')";

        try {
            con = cn.getConnection();
            con.setAutoCommit(false); // Iniciar transacción manual

            ps = con.prepareStatement(sql);

            // Preparar batch con todas las imágenes
            for (ImagenMascota imagen : imagenes) {
                ps.setInt(1, imagen.getR_Mascota());
                ps.setString(2, imagen.getURL_Imagen());
                ps.setTimestamp(3, new java.sql.Timestamp(imagen.getFecha_Carga().getTime()));
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al registrar imágenes en batch - " + e.getMessage());
            // Revertir transacción en caso de error
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.err.println("ERROR: Al hacer rollback - " + ex.getMessage());
            }
            return false;
        } finally {
            // Restaurar auto-commit y cerrar recursos
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("ERROR: Al restaurar auto-commit - " + e.getMessage());
            }
            cerrarRecursos();
        }
    }

    /**
     * Obtiene todas las imágenes activas ordenadas por fecha de carga descendente.
     *
     * Solo retorna imágenes con estatus 'Alta' (no eliminadas lógicamente).
     * El ordenamiento por fecha descendente muestra las imágenes más recientes primero.
     *
     * Este método es útil para listados generales, galerías administrativas
     * y operaciones de mantenimiento del sistema.
     *
     * @return List<ImagenMascota> Lista de todas las imágenes activas en el sistema
     */
    public List<ImagenMascota> listarImagenes() {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM ImagenMascota WHERE estatus = 'Alta' ORDER BY fecha_carga DESC";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ImagenMascota imagen = new ImagenMascota();
                imagen.setImagenID(rs.getInt("imagenid"));
                imagen.setR_Mascota(rs.getInt("r_mascota"));
                imagen.setURL_Imagen(rs.getString("url_imagen"));
                imagen.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                imagen.setEstatus(rs.getString("estatus"));
                lista.add(imagen);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar imágenes - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Obtiene todas las imágenes activas asociadas a una mascota específica.
     *
     * Las imágenes se ordenan por fecha de carga ascendente para mantener
     * el orden cronológico de subida, útil para mostrar la evolución temporal
     * de las imágenes de una mascota.
     *
     * Solo retorna imágenes con estatus 'Alta' para garantizar que
     * solo se muestren imágenes válidas y disponibles.
     *
     * @param mascotaId ID de la mascota de la cual se desean obtener las imágenes
     * @return List<ImagenMascota> Lista de imágenes asociadas a la mascota
     */
    public List<ImagenMascota> listarImagenesPorMascota(int mascotaId) {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM ImagenMascota WHERE r_mascota = ? AND estatus = 'Alta' ORDER BY fecha_carga ASC";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ImagenMascota imagen = new ImagenMascota();
                imagen.setImagenID(rs.getInt("imagenid"));
                imagen.setR_Mascota(rs.getInt("r_mascota"));
                imagen.setURL_Imagen(rs.getString("url_imagen"));
                imagen.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                imagen.setEstatus(rs.getString("estatus"));
                lista.add(imagen);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar imágenes por mascota - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return lista;
    }

    /**
     * Obtiene la URL de la primera imagen activa de una mascota como imagen principal.
     *
     * Retorna la imagen más antigua (primera cargada) basándose en la fecha de carga,
     * que típicamente representa la imagen principal o más representativa de la mascota.
     *
     * Esta funcionalidad es esencial para mostrar previsualizaciones, thumbnails
     * y representaciones principales de mascotas en listados y resúmenes.
     *
     * @param mascotaId ID de la mascota de la cual se desea obtener la imagen principal
     * @return String URL de la imagen principal, null si no tiene imágenes activas
     */
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
            System.err.println("ERROR: Al obtener imagen principal - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return imagenUrl;
    }

    /**
     * Busca y obtiene una imagen específica por su ID único.
     *
     * Solo retorna la imagen si tiene estatus 'Alta' (activa).
     * Si no se encuentra la imagen o está inactiva, retorna un objeto
     * ImagenMascota vacío para evitar NullPointerException.
     *
     * Este método es útil para operaciones de detalle, edición y validación
     * donde se necesita acceso completo a los datos de una imagen específica.
     *
     * @param id ID único de la imagen a buscar
     * @return ImagenMascota Objeto con los datos de la imagen, vacío si no se encuentra
     */
    public ImagenMascota buscarImagen(int id) {
        ImagenMascota imagen = new ImagenMascota();
        String sql = "SELECT * FROM ImagenMascota WHERE imagenid = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                imagen.setImagenID(rs.getInt("imagenid"));
                imagen.setR_Mascota(rs.getInt("r_mascota"));
                imagen.setURL_Imagen(rs.getString("url_imagen"));
                imagen.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                imagen.setEstatus(rs.getString("estatus"));
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al buscar imagen - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return imagen;
    }

    /**
     * Actualiza completamente la información de una imagen existente.
     *
     * Permite modificar todos los campos de la imagen incluyendo mascota asociada,
     * URL, fecha de carga y estatus. Esta funcionalidad completa es útil para
     * correcciones administrativas y reasignaciones de imágenes.
     *
     * @param imagen Objeto ImagenMascota con todos los datos actualizados
     * @return boolean true si la modificación fue exitosa, false en caso contrario
     */
    public boolean modificarImagen(ImagenMascota imagen) {
        String sql = "UPDATE ImagenMascota SET r_mascota = ?, url_imagen = ?, fecha_carga = ?, estatus = ? WHERE imagenid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, imagen.getR_Mascota());
            ps.setString(2, imagen.getURL_Imagen());
            ps.setTimestamp(3, new java.sql.Timestamp(imagen.getFecha_Carga().getTime()));
            ps.setString(4, imagen.getEstatus());
            ps.setInt(5, imagen.getImagenID());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al modificar imagen - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Actualiza únicamente la URL de una imagen específica.
     *
     * Esta operación parcial es útil para casos donde solo se necesita
     * cambiar la ubicación de la imagen sin afectar otros metadatos.
     * Solo actualiza imágenes con estatus 'Alta' para mantener consistencia.
     *
     * @param imagenId ID de la imagen a actualizar
     * @param nuevaUrl Nueva URL de la imagen
     * @return boolean true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarUrlImagen(int imagenId, String nuevaUrl) {
        String sql = "UPDATE ImagenMascota SET url_imagen = ? WHERE imagenid = ? AND estatus = 'Alta'";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevaUrl);
            ps.setInt(2, imagenId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al actualizar URL de imagen - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Realiza una eliminación lógica de una imagen cambiando su estatus a 'Baja'.
     *
     * La eliminación lógica preserva la imagen en la base de datos para
     * propósitos de auditoría y recuperación, mientras la oculta de las
     * consultas normales del sistema.
     *
     * Esta es la forma recomendada de eliminar imágenes para mantener
     * la integridad referencial y el historial completo.
     *
     * @param id ID único de la imagen a eliminar lógicamente
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarImagenLogico(int id) {
        String sql = "UPDATE ImagenMascota SET estatus = 'Baja' WHERE imagenid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al eliminar imagen lógicamente - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Realiza una eliminación física de una imagen, borrándola completamente de la base de datos.
     *
     * ADVERTENCIA: Esta operación es irreversible y puede afectar la integridad
     * referencial si existen dependencias en otras tablas.
     *
     * Se recomienda usar solo en casos especiales de limpieza administrativa
     * o cuando se esté seguro de que no existen referencias externas.
     *
     * @param id ID único de la imagen a eliminar físicamente
     * @return boolean true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminarImagenFisico(int id) {
        String sql = "DELETE FROM ImagenMascota WHERE imagenid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al eliminar imagen físicamente - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Desactiva todas las imágenes asociadas a una mascota específica mediante eliminación lógica.
     *
     * Esta operación en lote es útil cuando se elimina o desactiva una mascota
     * y se necesita ocultar todas sus imágenes asociadas de forma consistente.
     *
     * Mantiene la integridad referencial mientras oculta las imágenes
     * de las consultas normales del sistema.
     *
     * @param mascotaId ID de la mascota cuyas imágenes se desean desactivar
     * @return boolean true si la operación fue exitosa, false en caso contrario
     */
    public boolean darBajaImagenesPorMascota(int mascotaId) {
        String sql = "UPDATE ImagenMascota SET estatus = 'Baja' WHERE r_mascota = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, mascotaId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al dar de baja imágenes por mascota - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Reactiva una imagen previamente desactivada, cambiando su estatus de 'Baja' a 'Alta'.
     *
     * Esta funcionalidad permite recuperar imágenes eliminadas lógicamente,
     * útil para casos de errores administrativos o cambios de decisión.
     *
     * La reactivación hace que la imagen vuelva a aparecer en todas
     * las consultas normales del sistema.
     *
     * @param id ID único de la imagen a reactivar
     * @return boolean true si la reactivación fue exitosa, false en caso contrario
     */
    public boolean reactivarImagen(int id) {
        String sql = "UPDATE ImagenMascota SET estatus = 'Alta' WHERE imagenid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR: Al reactivar imagen - " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos();
        }
    }

    /**
     * Cuenta el número total de imágenes activas asociadas a una mascota específica.
     *
     * Solo considera imágenes con estatus 'Alta' para proporcionar un conteo
     * preciso de imágenes válidas y disponibles.
     *
     * Este método es fundamental para validaciones, límites de carga
     * y estadísticas del sistema.
     *
     * @param mascotaId ID de la mascota de la cual se desean contar las imágenes
     * @return int Número de imágenes activas asociadas a la mascota
     */
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
            System.err.println("ERROR: Al contar imágenes activas - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return count;
    }

    /**
     * Verifica si una mascota tiene al menos una imagen activa asociada.
     *
     * Este método de conveniencia utiliza el contador de imágenes activas
     * para determinar rápidamente la existencia de imágenes válidas.
     *
     * Es útil para validaciones de interfaz de usuario, reglas de negocio
     * y verificaciones antes de mostrar galerías o previsualizaciones.
     *
     * @param mascotaId ID de la mascota a verificar
     * @return boolean true si la mascota tiene imágenes activas, false en caso contrario
     */
    public boolean mascotaTieneImagenesActivas(int mascotaId) {
        return contarImagenesActivasPorMascota(mascotaId) > 0;
    }

    /**
     * Obtiene todas las imágenes eliminadas lógicamente (estatus 'Baja') para propósitos administrativos.
     *
     * Este método especializado permite a los administradores revisar imágenes
     * previamente eliminadas para auditorías, recuperación de datos o limpieza final.
     *
     * Las imágenes se ordenan por fecha de carga descendente para facilitar
     * la identificación de eliminaciones recientes.
     *
     * @return List<ImagenMascota> Lista de imágenes con estatus 'Baja'
     */
    public List<ImagenMascota> listarImagenesEliminadas() {
        List<ImagenMascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM ImagenMascota WHERE estatus = 'Baja' ORDER BY fecha_carga DESC";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ImagenMascota imagen = new ImagenMascota();
                imagen.setImagenID(rs.getInt("imagenid"));
                imagen.setR_Mascota(rs.getInt("r_mascota"));
                imagen.setURL_Imagen(rs.getString("url_imagen"));
                imagen.setFecha_Carga(rs.getTimestamp("fecha_carga"));
                imagen.setEstatus(rs.getString("estatus"));
                lista.add(imagen);
            }

        } catch (SQLException e) {
            System.err.println("ERROR: Al listar imágenes eliminadas - " + e.getMessage());
        } finally {
            cerrarRecursos();
        }

        return lista;
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