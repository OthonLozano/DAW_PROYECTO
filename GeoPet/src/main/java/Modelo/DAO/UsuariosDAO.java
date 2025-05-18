package Modelo.DAO;

import Modelo.JavaBeans.Usuarios;
import Connection.Conexion;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

public class UsuariosDAO {

    private static final String CREATE = "INSERT INTO Usuarios(Nombre, ApellidoPat, ApellidoMat, Email, Contrasenia, Telefono, Direccion, Ciudad, Fecha_Registro, Usuario, Estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CREATECLIENT = "INSERT INTO Usuarios(Nombre, ApellidoPat, ApellidoMat, Email, Contrasenia, Telefono, Direccion, Ciudad, Fecha_Registro, Usuario, Estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Alta')";
    private static final String CREATEADMIN = "INSERT INTO Usuarios(Nombre, ApellidoPat, ApellidoMat, Email, Contrasenia, Telefono, Direccion, Ciudad, Fecha_Registro, Usuario, Estatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Alta')";
    private static final String READ = "SELECT * FROM Usuarios";
    private static final String UPDATE = "UPDATE Usuarios SET Nombre=?, ApellidoPat=?, ApellidoMat=?, Email=?, Contrasenia=?, Telefono=?, Direccion=?, Ciudad=?, Fecha_Registro=?, Usuario=?, Estatus=? WHERE UsuarioID=?";
    private static final String DELETE = "DELETE FROM Usuarios WHERE UsuarioID=?";

    // Método para crear un nuevo Cliente
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

    // Método para crear un nuevo Cliente
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
            ps.setString(10, u.getUsuario());
            //ps.setString(11, u.getEstatus());

            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return registros;
    }

    // Método para listar todos los usuarios
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
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return lista;
    }

    // Método para actualizar un usuario existente
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
            ps.setString(10, u.getUsuario());
            //ps.setString(11, u.getEstatus());
            ps.setInt(12, u.getUsuarioID());

            registros = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return registros;
    }

    // Método para eliminar un usuario por su ID
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
        }
        return registros;
    }

    // Método para buscar un usuario por nombre
    public List<Usuarios> buscarPorNombre(String nombre) {
        List<Usuarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE Nombre = ?";
        Conexion conexion = new Conexion();
        try (
                Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nombre);
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

    // Método para buscar un usuario por email
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

    // Método que mapea un ResultSet a un objeto de tipo Usuarios
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
        //u.setEstatus(rs.getString("Estatus"));
        return u;
    }

    //Método para obtener aquellos usuarios que son solo ADMIN
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
                        u.setUsuario(rs.getString("Usuario"));    // Admin o Cliente
                        u.setEstatus(rs.getString("Estatus"));
                        return u;
                    }
                }
            }
        }
        return null;
    }

}
