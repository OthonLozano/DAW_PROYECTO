package Servlet;

import Modelo.DAO.AvistamientoDAO;
import Modelo.JavaBeans.Avistamiento;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "AvistamientoServlet", urlPatterns = {"/AvistamientoServlet"})
public class AvistamientoServlet extends HttpServlet {

    AvistamientoDAO avistamientoDAO = new AvistamientoDAO();
    Avistamiento avistamiento = new Avistamiento();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                List<Avistamiento> lista = avistamientoDAO.listarAvistamientos();
                request.setAttribute("avistamientos", lista);
                request.getRequestDispatcher("Vistas_JSP/Avistamientos/listar_avistamientos.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                avistamientoDAO.eliminarAvistamiento(idEliminar);
                response.sendRedirect("AvistamientoServlet?accion=listar");
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Avistamiento a = avistamientoDAO.buscarAvistamiento(idEditar);
                request.setAttribute("avistamiento", a);
                request.getRequestDispatcher("Vistas_JSP/Avistamientos/editar_avistamiento.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("AvistamientoServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            switch (accion) {
                case "registrar":
                    avistamiento.setR_Reporte(Integer.parseInt(request.getParameter("r_reporte")));
                    avistamiento.setR_UsuarioReportante(Integer.parseInt(request.getParameter("r_usuarioreportante")));
                    avistamiento.setFecha_Avistamiento(parseDate(request.getParameter("fecha_avistamiento")));
                    avistamiento.setUbicacion(request.getParameter("ubicacion"));
                    avistamiento.setDescripcion(request.getParameter("descripcion"));
                    avistamiento.setContacto(request.getParameter("contacto"));
                    avistamiento.setFecha_Registro(parseDate(request.getParameter("fecha_registro")));
                    avistamiento.setR_Imagen(Integer.parseInt(request.getParameter("r_imagen")));
                    avistamientoDAO.registrarAvistamiento(avistamiento);
                    request.setAttribute("mensaje", "¡Avistamiento registrado exitosamente!");
                    request.getRequestDispatcher("Vistas_JSP/Avistamientos/registrar_avistamiento.jsp").forward(request, response);
                    break;

                case "actualizar":
                    avistamiento.setAvistamientoID(Integer.parseInt(request.getParameter("avistamientoid")));
                    avistamiento.setR_Reporte(Integer.parseInt(request.getParameter("r_reporte")));
                    avistamiento.setR_UsuarioReportante(Integer.parseInt(request.getParameter("r_usuarioreportante")));
                    avistamiento.setFecha_Avistamiento(parseDate(request.getParameter("fecha_avistamiento")));
                    avistamiento.setUbicacion(request.getParameter("ubicacion"));
                    avistamiento.setDescripcion(request.getParameter("descripcion"));
                    avistamiento.setContacto(request.getParameter("contacto"));
                    avistamiento.setFecha_Registro(parseDate(request.getParameter("fecha_registro")));
                    avistamiento.setR_Imagen(Integer.parseInt(request.getParameter("r_imagen")));
                    avistamientoDAO.modificarAvistamiento(avistamiento);
                    response.sendRedirect("AvistamientoServlet?accion=listar");
                    break;

                default:
                    response.sendRedirect("AvistamientoServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private Date parseDate(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(fecha);
    }
}
