package Modelo.DAO;

import Modelo.JavaBeans.Usuarios;
import Connection.Conexion;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

public class UsuariosDAO {

    private static final String CREATE = "INSERT INTO Usuarios(Nombre, ApellidoPat, ApellidoMat, Email, Contrasenia, Telefono, Direccion, Ciudad, Fecha_Registro, Usuario, Estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String READ = "SELECT * FROM Usuarios";
    private static final String UPDATE = "UPDATE Usuarios SET Nombre=?, ApellidoPat=?, ApellidoMat=?, Email=?, Contrasenia=?, Telefono=?, Direccion=?, Ciudad=?, Fecha_Registro=?, Usuario=?, Estatus=? WHERE UsuarioID=?";
    private static final String DELETE = "DELETE FROM Usuarios WHERE UsuarioID=?";

    // Método para registrar cliente (nuevo método limpio)
    public void registrarCliente(Usuarios u) throws SQLException {
        String sql = """
            INSERT INTO Usuarios
              (Nombre, ApellidoPat, ApellidoMat,
               Email, Contrasenia,
               Telefono, Direccion, Ciudad,
               Fecha_Registro, Usuario, Estatus)
            VALUES
              (?, ?, ?, ?, ?,
               ?, ?, ?,
               CURRENT_DATE, 'Cliente', 'Alta')
            """;
        Conexion conexion = new Conexion();
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidoPat());
            ps.setString(3, u.getApellidoMat());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getContrasenia());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getDireccion());
            ps.setString(8, u.getCiudad());

            ps.executeUpdate();
        }
    }

    // Método para crear cliente (corregido)
    public int createClient(Usuarios u) {
        int registros = 0;
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(CREATE)
        ) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidoPat());
            ps.setString(3, u.getApellidoMat());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getContrasenia());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getDireccion());
            ps.setString(8, u.getCiudad());
            ps.setDate(9, new java.sql.Date(u.getFecha_Registro().getTime()));
            ps.setString(10, u.getUsuario() != null ? u.getUsuario() : "Cliente"); // Valor por defecto
            ps.setString(11, u.getEstatus() != null ? u.getEstatus() : "Alta"); // Valor por defecto

            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
        return registros;
    }

    // Método para listar todos los usuarios (con debug)
    public List<Usuarios> listar() {
        List<Usuarios> lista = new ArrayList<>();
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(READ);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                Usuarios u = mapUsuario(rs);
                lista.add(u);
            }
            // Debug: Mostrar cuántos usuarios se encontraron
            System.out.println("Usuarios encontrados: " + lista.size());
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // Método para actualizar usuario (corregido)
    public int actualizar(Usuarios u) {
        int registros = 0;
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidoPat());
            ps.setString(3, u.getApellidoMat());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getContrasenia());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getDireccion());
            ps.setString(8, u.getCiudad());
            ps.setDate(9, new java.sql.Date(u.getFecha_Registro().getTime()));
            ps.setString(10, u.getUsuario() != null ? u.getUsuario() : "Cliente");
            ps.setString(11, u.getEstatus() != null ? u.getEstatus() : "Alta");
            ps.setInt(12, u.getUsuarioID());

            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        return registros;
    }

    /*// Método para eliminar usuario
    public int eliminar(int id) {
        int registros = 0;
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE)
        ) {
            ps.setInt(1, id);
            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return registros;
    }*/

    // Método para buscar usuario por nombre
    public List<Usuarios> buscarPorNombre(String nombre) {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE Nombre LIKE ?";
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapUsuario(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return lista;
    }

    // Método para buscar usuario por email
    public Usuarios buscarPorEmail(String email) {
        String sql = "SELECT * FROM Usuarios WHERE Email = ?";
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    // Método para obtener usuario por ID
    public Usuarios obtenerPorId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE UsuarioID = ?";
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    // Método que mapea ResultSet a objeto Usuario (CORREGIDO)
    private Usuarios mapUsuario(ResultSet rs) throws SQLException {
        Usuarios u = new Usuarios();
        u.setUsuarioID(rs.getInt("UsuarioID"));
        u.setNombre(rs.getString("Nombre"));
        u.setApellidoPat(rs.getString("ApellidoPat"));
        u.setApellidoMat(rs.getString("ApellidoMat"));
        u.setEmail(rs.getString("Email"));
        u.setContrasenia(rs.getString("Contrasenia"));
        u.setTelefono(rs.getString("Telefono"));
        u.setDireccion(rs.getString("Direccion"));
        u.setCiudad(rs.getString("Ciudad"));
        u.setFecha_Registro(rs.getDate("Fecha_Registro"));
        u.setUsuario(rs.getString("Usuario"));
        u.setEstatus(rs.getString("Estatus")); // DESCOMENTADO
        return u;
    }

    // Método para login
    public Usuarios login(String email, String contrasenia) throws SQLException {
        String sql = "SELECT UsuarioID, Nombre, ApellidoPat, ApellidoMat,"
                + " Email, Contrasenia, Usuario, Estatus"
                + " FROM Usuarios WHERE Email = ?";
        Conexion conexion = new Conexion();
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("Contrasenia");
                    if (BCrypt.checkpw(contrasenia, hashed)) {
                        Usuarios u = new Usuarios();
                        u.setUsuarioID(rs.getInt("UsuarioID"));
                        u.setNombre(rs.getString("Nombre"));
                        u.setApellidoPat(rs.getString("ApellidoPat"));
                        u.setApellidoMat(rs.getString("ApellidoMat"));
                        u.setEmail(rs.getString("Email"));
                        u.setUsuario(rs.getString("Usuario"));
                        u.setEstatus(rs.getString("Estatus"));
                        return u;
                    }
                }
            }
        }
        return null;
    }

    // Método para eliminar usuario (eliminación lógica - soft delete)
    public boolean eliminarUsuario(int usuarioId) {
        String sql = "UPDATE Usuarios SET Estatus = 'Baja' WHERE UsuarioID = ?";

        System.out.println("=== DEBUG UsuariosDAO ===");
        System.out.println("DEBUG DAO: Eliminando usuario ID = " + usuarioId);

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, usuarioId);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Usuario eliminado exitosamente (estatus cambiado a 'Baja')");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo eliminar el usuario - ID no encontrado");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en eliminarUsuario - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método alternativo si quieres mantener el método original también
    public int eliminar(int id) {
        // Llamar al nuevo método de eliminación lógica
        return eliminarUsuario(id) ? 1 : 0;
    }

    // Método para listar solo usuarios activos (modificado)
    public List<Usuarios> listarActivos() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE Estatus = 'Alta'";

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                Usuarios u = mapUsuario(rs);
                lista.add(u);
            }
            System.out.println("Usuarios activos encontrados: " + lista.size());
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al listar usuarios activos: " + e.getMessage());
        }
        return lista;
    }

    // Método para restaurar un usuario eliminado
    public boolean restaurarUsuario(int usuarioId) {
        String sql = "UPDATE Usuarios SET Estatus = 'Alta' WHERE UsuarioID = ?";

        System.out.println("DEBUG DAO: Restaurando usuario ID = " + usuarioId);

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, usuarioId);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Usuario restaurado exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo restaurar el usuario");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en restaurarUsuario - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar usuarios eliminados (para administración)
    public List<Usuarios> listarEliminados() {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE Estatus = 'Baja'";

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                Usuarios u = mapUsuario(rs);
                lista.add(u);
            }
            System.out.println("Usuarios eliminados encontrados: " + lista.size());
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al listar usuarios eliminados: " + e.getMessage());
        }
        return lista;
    }

    // Agregar estos métodos al final de tu clase UsuariosDAO

    /**
     * Método específico para actualizar perfil de usuario (sin cambiar contraseña)
     * Útil cuando el usuario edita su perfil pero no quiere cambiar la contraseña
     */
    public int actualizarPerfil(Usuarios u) {
        int registros = 0;
        String sql = """
        UPDATE Usuarios SET 
            Nombre=?, ApellidoPat=?, ApellidoMat=?, 
            Email=?, Telefono=?, Direccion=?, Ciudad=?
        WHERE UsuarioID=?
        """;

        System.out.println("DEBUG DAO: Actualizando perfil de usuario ID: " + u.getUsuarioID());

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidoPat());
            ps.setString(3, u.getApellidoMat());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getDireccion());
            ps.setString(7, u.getCiudad());
            ps.setInt(8, u.getUsuarioID());

            registros = ps.executeUpdate();

            if (registros > 0) {
                System.out.println("DEBUG DAO: Perfil actualizado exitosamente");
            } else {
                System.out.println("DEBUG DAO: No se pudo actualizar el perfil");
            }

        } catch (SQLException e) {
            e.printStackTrace(System.out);
            System.err.println("Error al actualizar perfil: " + e.getMessage());
        }
        return registros;
    }

    /**
     * Método para actualizar solo la contraseña del usuario
     */
    public boolean actualizarContrasenia(int usuarioId, String nuevaContrasenia) {
        String sql = "UPDATE Usuarios SET Contrasenia = ? WHERE UsuarioID = ?";

        System.out.println("DEBUG DAO: Actualizando contraseña para usuario ID: " + usuarioId);

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Hash de la nueva contraseña
            String hashedPassword = BCrypt.hashpw(nuevaContrasenia, BCrypt.gensalt());

            ps.setString(1, hashedPassword);
            ps.setInt(2, usuarioId);

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("DEBUG DAO: Contraseña actualizada exitosamente");
                return true;
            } else {
                System.out.println("DEBUG DAO: No se pudo actualizar la contraseña");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para verificar si un email ya está en uso por otro usuario
     * Útil para validar que no se duplique el email al editar perfil
     */
    public boolean emailExisteParaOtroUsuario(String email, int usuarioId) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE Email = ? AND UsuarioID != ? AND Estatus = 'Alta'";

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setInt(2, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email duplicado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método para validar la contraseña actual del usuario
     * Útil para verificar la contraseña antes de permitir cambios
     */
    public boolean validarContraseniaActual(int usuarioId, String contraseniaActual) {
        String sql = "SELECT Contrasenia FROM Usuarios WHERE UsuarioID = ?";

        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("Contrasenia");
                    return BCrypt.checkpw(contraseniaActual, hashedPassword);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar contraseña actual: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}