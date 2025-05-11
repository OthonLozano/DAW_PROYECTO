package Servlet;

import Modelo.DAO.MascotasDAO;
import Modelo.JavaBeans.Mascotas;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import Connection.Conexion;

@WebServlet(name = "MascotaServlet", urlPatterns = {"/MascotaServlet"})
public class MascotaServlet extends HttpServlet {

    MascotasDAO mascotaDAO = new MascotasDAO();
    Mascotas mascota = new Mascotas();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                List<Mascotas> lista = mascotaDAO.ListarMascotas();
                request.setAttribute("mascotas", lista);
                request.getRequestDispatcher("Vistas_JSP/Mascotas/listar_mascotas.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                mascotaDAO.EliminarMascota(idEliminar);
                response.sendRedirect("MascotaServlet?accion=listar");
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Mascotas m = mascotaDAO.BuscarMascota(idEditar);
                request.setAttribute("mascota", m);
                request.getRequestDispatcher("Vistas_JSP/Mascotas/editar_mascotas.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("MascotaServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                mascota.setR_Usuario(Integer.parseInt(request.getParameter("r_usuario")));
                mascota.setNombre(request.getParameter("nombre"));
                mascota.setR_Especie(Integer.parseInt(request.getParameter("r_especie")));
                mascota.setEdad(Integer.parseInt(request.getParameter("edad")));
                mascota.setSexo(request.getParameter("sexo"));
                mascota.setColor(request.getParameter("color"));
                mascota.setCaracteristicasDistintivas(request.getParameter("caracteristicasdistintivas"));
                mascota.setMicrochip(Boolean.parseBoolean(request.getParameter("microchip")));
                mascota.setNumero_Microchip(Integer.parseInt(request.getParameter("numero_microchip")));
                mascota.setEstado(request.getParameter("estado"));
                mascota.setFecha_Registro(java.sql.Date.valueOf(request.getParameter("fecha_registro")));
                mascotaDAO.RegistrarMascota(mascota);
                request.setAttribute("mensaje", "¡Mascota registrada exitosamente!");
                request.getRequestDispatcher("Vistas_JSP/Mascotas/registrar_mascota.jsp").forward(request, response);
                break;

            case "actualizar":
                mascota.setMascotaID(Integer.parseInt(request.getParameter("mascotaid")));
                mascota.setR_Usuario(Integer.parseInt(request.getParameter("r_usuario")));
                mascota.setNombre(request.getParameter("nombre"));
                mascota.setR_Especie(Integer.parseInt(request.getParameter("r_especie")));
                mascota.setEdad(Integer.parseInt(request.getParameter("edad")));
                mascota.setSexo(request.getParameter("sexo"));
                mascota.setColor(request.getParameter("color"));
                mascota.setCaracteristicasDistintivas(request.getParameter("caracteristicasdistintivas"));
                mascota.setMicrochip(Boolean.parseBoolean(request.getParameter("microchip")));
                mascota.setNumero_Microchip(Integer.parseInt(request.getParameter("numero_microchip")));
                mascota.setEstado(request.getParameter("estado"));
                mascota.setFecha_Registro(java.sql.Date.valueOf(request.getParameter("fecha_registro")));
                mascotaDAO.ModificarMascota(mascota);
                response.sendRedirect("MascotaServlet?accion=listar");
                break;

            default:
                response.sendRedirect("MascotaServlet?accion=listar");
                break;
        }
    }
}

