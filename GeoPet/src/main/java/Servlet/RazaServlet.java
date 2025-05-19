package Servlet;

import Modelo.DAO.RazaDAO;
import Modelo.JavaBeans.Raza;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RazaServlet", urlPatterns = {"/RazaServlet"})
public class RazaServlet extends HttpServlet {

    RazaDAO razaDAO = new RazaDAO();
    Raza raza = new Raza();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                List<Raza> lista = razaDAO.ListarRazas();
                request.setAttribute("razas", lista);
                request.getRequestDispatcher("Vistas_JSP/Razas/listar_razas.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                razaDAO.EliminarRaza(idEliminar);
                response.sendRedirect("RazaServlet?accion=listar");
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Raza r = razaDAO.BuscarRaza(idEditar);
                request.setAttribute("raza", r);
                request.getRequestDispatcher("Vistas_JSP/Razas/editar_raza.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("RazaServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                raza.setNombre(request.getParameter("nombre"));
                raza.setDescripcion(request.getParameter("descripcion"));
                razaDAO.RegistrarRaza(raza);
                request.setAttribute("mensaje", "¡Raza registrada exitosamente!");
                request.getRequestDispatcher("Vistas_JSP/Razas/registrar_raza.jsp").forward(request, response);
                break;

            case "actualizar":
                raza.setRazaID(Integer.parseInt(request.getParameter("razaid")));
                raza.setNombre(request.getParameter("nombre"));
                raza.setDescripcion(request.getParameter("descripcion"));
                razaDAO.ModificarRaza(raza);
                response.sendRedirect("RazaServlet?accion=listar");
                break;

            default:
                response.sendRedirect("RazaServlet?accion=listar");
                break;
        }
    }
}
