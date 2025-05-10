package Modelo.DAO;

import Connection.Conexion;
import Modelo.JavaBeans.Especie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecieDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public EspecieDAO() {
    }

    public boolean RegistrarEspecie(Especie es) {
        String sql = "INSERT INTO especie (nombre, descripcion) VALUES (?, ?)";
        boolean e = false;

        // Crear una nueva instancia de Conexion para cada operación
        Conexion conexionLocal = new Conexion();
        Connection localCon = null;
        PreparedStatement localPs = null;

        try {
            localCon = conexionLocal.getConnection();

            // Verificar que la conexión no esté cerrada antes de usarla
            if (localCon == null || localCon.isClosed()) {
                System.out.println("Error: La conexión está cerrada o es nula");
                return false;
            }

            localPs = localCon.prepareStatement(sql);
            localPs.setString(1, es.getNombre());
            localPs.setString(2, es.getDescripcion());
            localPs.executeUpdate();
            e = true;
        } catch (SQLException ex) {
            System.out.println("Error al registrar especie: " + ex.toString());
            e = false;
        } finally {
            try {
                if (localPs != null) localPs.close();
                if (localCon != null) localCon.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return e;
    }

    public List ListarEspecies() {
        List<Especie> ListaEs = new ArrayList();
        String sql = "SELECT * FROM especie";

        try {
            this.con = this.cn.getConnection();
            this.ps = this.con.prepareStatement(sql);
            this.rs = this.ps.executeQuery();

            while(this.rs.next()) {
                Especie es = new Especie();
                es.setEspecieID(this.rs.getInt("especieid"));
                es.setNombre(this.rs.getString("nombre"));
                es.setDescripcion(this.rs.getString("descripcion"));
                ListaEs.add(es);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar especies: " + e.toString());
        } finally {
            try {
                if (this.rs != null) this.rs.close();
                if (this.ps != null) this.ps.close();
                if (this.con != null) this.con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return ListaEs;
    }

    public boolean EliminarEspecie(int id) {
        String sql = "DELETE FROM especie WHERE especieid = ?";
        boolean ex = false;

        try {
            // Abrir nueva conexión
            this.con = this.cn.getConnection();
            this.ps = this.con.prepareStatement(sql);
            this.ps.setInt(1, id);
            this.ps.execute();
            ex = true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar especie: " + e.toString());
            ex = false;
        } finally {
            try {
                if (this.ps != null) this.ps.close();
                if (this.con != null) this.con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return ex;
    }

    public boolean ModificarEspecie(Especie es) {
        String sql = "UPDATE especie SET nombre = ?, descripcion = ? WHERE especieid = ?";
        boolean e = false;

        try {
            // Abrir nueva conexión
            this.con = this.cn.getConnection();
            this.ps = this.con.prepareStatement(sql);
            this.ps.setString(1, es.getNombre());
            this.ps.setString(2, es.getDescripcion());
            this.ps.setInt(3, es.getEspecieID());
            this.ps.execute();
            e = true;
        } catch (SQLException ex) {
            System.out.println("Error al modificar especie: " + ex.toString());
            e = false;
        } finally {
            try {
                if (this.ps != null) this.ps.close();
                if (this.con != null) this.con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return e;
    }

    public Especie BuscarEspecie(int especieId) {
        Especie es = new Especie();
        String sql = "SELECT * FROM especie WHERE especieid = ?";

        try {
            this.con = this.cn.getConnection();
            this.ps = this.con.prepareStatement(sql);
            this.ps.setInt(1, especieId);
            this.rs = this.ps.executeQuery();

            if (this.rs.next()) {
                es.setEspecieID(this.rs.getInt("especieid"));
                es.setNombre(this.rs.getString("nombre"));
                es.setDescripcion(this.rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar especie: " + e.toString());
        } finally {
            try {
                if (this.rs != null) this.rs.close();
                if (this.ps != null) this.ps.close();
                if (this.con != null) this.con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        return es;
    }

//    public Especie BuscarEspeciePorNombre(String nombre) {
//        Especie es = new Especie();
//        String sql = "SELECT * FROM especie WHERE nombre = ?";
//
//        try {
//            this.con = this.cn.getConnection();
//            this.ps = this.con.prepareStatement(sql);
//            this.ps.setString(1, nombre);
//            this.rs = this.ps.executeQuery();
//
//            if (this.rs.next()) {
//                es.setEspecieID(this.rs.getInt("especieid"));
//                es.setNombre(this.rs.getString("nombre"));
//                es.setDescripcion(this.rs.getString("descripcion"));
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al buscar especie por nombre: " + e.toString());
//        } finally {
//            try {
//                if (this.rs != null) this.rs.close();
//                if (this.ps != null) this.ps.close();
//                if (this.con != null) this.con.close();
//            } catch (SQLException e) {
//                System.out.println(e.toString());
//            }
//        }
//
//        return es;
//    }
//
//    public List<Especie> BuscarEspeciesPorNombreParcial(String nombre) {
//        List<Especie> ListaEs = new ArrayList<>();
//        String sql = "SELECT * FROM especie WHERE nombre LIKE ?";
//
//        try {
//            this.con = this.cn.getConnection();
//            this.ps = this.con.prepareStatement(sql);
//            this.ps.setString(1, "%" + nombre + "%");
//            this.rs = this.ps.executeQuery();
//
//            while(this.rs.next()) {
//                Especie es = new Especie();
//                es.setEspecieID
//                        (this.rs.getInt("especieid"));
//                es.setNombre(this.rs.getString("nombre"));
//                es.setDescripcion(this.rs.getString("descripcion"));
//                ListaEs.add(es);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al buscar especies por nombre parcial: " + e.toString());
//        } finally {
//            try {
//                if (this.rs != null) this.rs.close();
//                if (this.ps != null) this.ps.close();
//                if (this.con != null) this.con.close();
//            } catch (SQLException e) {
//                System.out.println(e.toString());
//            }
//        }
//
//        return ListaEs;
//    }
}