package Modelo.DAO;
import Connection.Conexion;
import Modelo.JavaBeans.Raza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RazaDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean RegistrarRaza(Raza r) {
        String sql = "INSERT INTO raza (nombre, descripcion, estatus) VALUES (?, ?, 'Alta')";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al registrar raza: " + e.toString());
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

    public List<Raza> ListarRazas() {
        List<Raza> lista = new ArrayList<>();
        String sql = "SELECT * FROM raza WHERE estatus = 'Alta'";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Raza r = new Raza();
                r.setRazaID(rs.getInt("razaid"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar razas: " + e.toString());
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

    public boolean ModificarRaza(Raza r) {
        String sql = "UPDATE raza SET nombre = ?, descripcion = ? WHERE razaID = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setInt(3, r.getRazaID());
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al modificar raza: " + e.toString());
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

    public boolean EliminarRaza(int id) {
        String sql = "UPDATE raza SET estatus = 'Baja' WHERE razaID = ?";
        boolean exito = false;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            exito = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar raza: " + e.toString());
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

    public Raza BuscarRaza(int id) {
        Raza r = new Raza();
        String sql = "SELECT * FROM raza WHERE razaID = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                r.setRazaID(rs.getInt("razaid"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar raza: " + e.toString());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return r;
    }
}

