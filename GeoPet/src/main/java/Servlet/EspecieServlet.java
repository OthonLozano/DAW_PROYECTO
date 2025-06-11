package Servlet;

import Modelo.DAO.EspecieDAO;
import Modelo.JavaBeans.Especie;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;
import Connection.Conexion;

@WebServlet(name = "EspecieServlet", urlPatterns = {"/EspecieServlet"})
public class EspecieServlet extends HttpServlet {

    EspecieDAO dao = new EspecieDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null) accion = "listar";

        switch (accion) {
            case "listar":
                List<Especie> lista = dao.ListarEspecies();
                request.setAttribute("especies", lista);
                request.getRequestDispatcher("/Vistas_JSP/Especie/listar_especies.jsp").forward(request, response);
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Especie especieEditar = dao.BuscarEspecie(idEditar);
                request.setAttribute("especie", especieEditar);
                request.getRequestDispatcher("/Vistas_JSP/Especie/editar_especies.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.EliminarEspecie(idEliminar);
                response.sendRedirect("EspecieServlet?accion=listar");
                break;

            default:
                response.sendRedirect("EspecieServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                Especie nueva = new Especie();
                nueva.setNombre(request.getParameter("nombre"));
                nueva.setDescripcion(request.getParameter("descripcion"));
                dao.RegistrarEspecie(nueva);
                response.sendRedirect("EspecieServlet?accion=listar");
                break;

            case "actualizar":
                Especie actualizada = new Especie();
                actualizada.setEspecieID(Integer.parseInt(request.getParameter("id")));
                actualizada.setNombre(request.getParameter("nombre"));
                actualizada.setDescripcion(request.getParameter("descripcion"));
                dao.ModificarEspecie(actualizada);
                response.sendRedirect("EspecieServlet?accion=listar");
                break;

            default:
                response.sendRedirect("EspecieServlet?accion=listar");
                break;
        }
    }
}
