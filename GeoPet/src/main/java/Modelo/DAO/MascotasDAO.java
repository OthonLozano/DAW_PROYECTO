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

    public List<Mascotas> ListarMascotasPorUsuario(int usuarioId) {
        List<Mascotas> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas WHERE estatus = 'Alta' AND r_usuario = ?";

        System.out.println("DEBUG DAO: Ejecutando consulta: " + sql);
        System.out.println("DEBUG DAO: Para usuario ID: " + usuarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
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

                System.out.println("DEBUG DAO: Mascota encontrada - ID: " + m.getMascotaID() + ", Nombre: " + m.getNombre() + ", Usuario: " + m.getR_Usuario());
            }

            System.out.println("DEBUG DAO: Total mascotas activas del usuario " + usuarioId + ": " + lista.size());

        } catch (SQLException e) {
            System.out.println("Error al listar mascotas por usuario: " + e.toString());
            e.printStackTrace();
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

    public List<MascotaConRelaciones> ListarMascotasConRelacionesPorUsuario(int usuarioId) {
        List<MascotaConRelaciones> lista = new ArrayList<>();
        String sql = """
        SELECT 
            m.mascotaid,
            m.r_usuario,
            m.nombre,
            m.r_especie,
            m.edad,
            m.sexo,
            m.color,
            m.caracteristicasdistintivas,
            m.microchip,
            m.numero_microchip,
            m.estado,
            m.fecha_registro,
            e.especieid,
            e.nombre as nombre_especie,
            e.descripcion as descripcion_especie,
            img.imagenid,
            img.url_imagen,
            img.fecha_carga,
            img.estatus as imagen_estatus
        FROM mascotas m
        INNER JOIN especie e ON m.r_especie = e.especieid
        LEFT JOIN (
            SELECT DISTINCT 
                imagenid,
                r_mascota,
                url_imagen,
                fecha_carga,
                estatus,
                ROW_NUMBER() OVER (PARTITION BY r_mascota ORDER BY fecha_carga DESC) as rn
            FROM imagenmascota 
            WHERE estatus = 'Alta'
        ) img ON m.mascotaid = img.r_mascota AND img.rn = 1
        WHERE m.r_usuario = ? AND m.estatus = 'Alta'
        ORDER BY m.fecha_registro DESC
        """;

        System.out.println("DEBUG DAO: Ejecutando consulta mascotas con relaciones para usuario ID: " + usuarioId);

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                // Crear el objeto Mascotas
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

                // Crear el objeto Especie
                Especie especie = new Especie();
                especie.setEspecieID(rs.getInt("especieid"));
                especie.setNombre(rs.getString("nombre_especie"));
                especie.setDescripcion(rs.getString("descripcion_especie"));

                // Crear el objeto ImagenMascota (puede ser null)
                ImagenMascota imagen = null;
                if (rs.getString("url_imagen") != null) {
                    imagen = new ImagenMascota();
                    imagen.setImagenID(rs.getInt("imagenID"));
                    imagen.setR_Mascota(mascota.getMascotaID());
                    imagen.setURL_Imagen(rs.getString("url_imagen"));
                    imagen.setFecha_Carga(rs.getDate("fecha_carga"));
                    imagen.setEstatus(rs.getString("imagen_estatus"));
                }

                // Crear el objeto contenedor
                MascotaConRelaciones mascotaCompleta = new MascotaConRelaciones(mascota, especie, imagen);
                lista.add(mascotaCompleta);

                System.out.println("DEBUG DAO: Mascota con relaciones agregada - " + mascota.getNombre() +
                        " (Especie: " + especie.getNombre() +
                        ", Imagen: " + (imagen != null ? "Sí" : "No") + ")");
            }

            System.out.println("DEBUG DAO: Total mascotas con relaciones obtenidas: " + lista.size());

        } catch (SQLException e) {
            System.out.println("ERROR DAO: Exception en ListarMascotasConRelacionesPorUsuario - " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("ERROR DAO: Al cerrar conexiones - " + e.getMessage());
            }
        }

        return lista;
    }
}
