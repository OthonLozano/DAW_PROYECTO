package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.Mascotas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MascotasDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean RegistrarMascota(Mascotas m) {
        String sql = "INSERT INTO mascotas (r_usuario, nombre, r_especie, edad, sexo, color, caracteristicasdistintivas, microchip, numero_microchip, estado, fecha_registro, estatus) " +
                "VALUES (?, ?, ?, ?, ?::sexo_animal, ?, ?, ?, ?, ?::estado_mascota, ?, 'Alta')";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, m.getR_Usuario());
            ps.setString(2, m.getNombre());
            ps.setInt(3, m.getR_Especie());
            ps.setInt(4, m.getEdad());
            ps.setString(5, m.getSexo());
            ps.setString(6, m.getColor());
            ps.setString(7, m.getCaracteristicasDistintivas());
            ps.setBoolean(8, m.isMicrochip());
            ps.setInt(9, m.getNumero_Microchip());
            ps.setString(10, m.getEstado());
            ps.setDate(11, new java.sql.Date(m.getFecha_Registro().getTime()));
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar mascota: " + e.toString());
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return exito;
    }

    public List<Mascotas> ListarMascotas() {
        List<Mascotas> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mascotas m = new Mascotas();
                m.setMascotaID(rs.getInt("mascotaid"));
                m.setR_Usuario(rs.getInt("r_usuario"));
                m.setNombre(rs.getString("nombre"));
                m.setR_Especie(rs.getInt("r_especie"));
                m.setEdad(rs.getInt("edad"));
                m.setSexo(rs.getString("sexo"));
                m.setColor(rs.getString("color"));
                m.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                m.setMicrochip(rs.getBoolean("microchip"));
                m.setNumero_Microchip(rs.getInt("numero_microchip"));
                m.setEstado(rs.getString("estado"));
                m.setFecha_Registro(rs.getDate("fecha_registro"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar mascotas: " + e.toString());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return lista;
    }

    public boolean EliminarMascota(int id) {
        String sql = "UPDATE mascotas SET Estatus = 'Baja' WHERE mascotaID = ?";
        boolean exito = false;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar mascota: " + e.toString());
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return exito;
    }

    public boolean ModificarMascota(Mascotas m) {
        String sql = "UPDATE mascotas SET r_usuario = ?, nombre = ?, r_especie = ?, edad = ?, sexo = ?::sexo_animal, color = ?, caracteristicasdistintivas = ?, microchip = ?, numero_microchip = ?, estado = ?::estado_mascota, fecha_registro = ? WHERE mascotaid = ?";
        boolean exito = false;

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, m.getR_Usuario());
            ps.setString(2, m.getNombre());
            ps.setInt(3, m.getR_Especie());
            ps.setInt(4, m.getEdad());
            ps.setString(5, m.getSexo());
            ps.setString(6, m.getColor());
            ps.setString(7, m.getCaracteristicasDistintivas());
            ps.setBoolean(8, m.isMicrochip());
            ps.setInt(9, m.getNumero_Microchip());
            ps.setString(10, m.getEstado());
            ps.setDate(11, new java.sql.Date(m.getFecha_Registro().getTime()));
            ps.setInt(12, m.getMascotaID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar mascota: " + e.toString());
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return exito;
    }

    public Mascotas BuscarMascota(int id) {
        Mascotas m = new Mascotas();
        String sql = "SELECT * FROM mascotas WHERE mascotaid = ?";

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                m.setMascotaID(rs.getInt("mascotaid"));
                m.setR_Usuario(rs.getInt("r_usuario"));
                m.setNombre(rs.getString("nombre"));
                m.setR_Especie(rs.getInt("r_especie"));
                m.setEdad(rs.getInt("edad"));
                m.setSexo(rs.getString("sexo"));
                m.setColor(rs.getString("color"));
                m.setCaracteristicasDistintivas(rs.getString("caracteristicasdistintivas"));
                m.setMicrochip(rs.getBoolean("microchip"));
                m.setNumero_Microchip(rs.getInt("numero_microchip"));
                m.setEstado(rs.getString("estado"));
                m.setFecha_Registro(rs.getDate("fecha_registro"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar mascota por ID: " + e.toString());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return m;
    }
}
