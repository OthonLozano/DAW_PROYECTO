package Servlet;

import Modelo.DAO.ReporteDesaparicionDAO;
import Modelo.JavaBeans.ReporteDesaparicion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ReporteDesaparicionServlet", urlPatterns = {"/ReporteDesaparicionServlet"})
public class ReporteDesaparicionServlet extends HttpServlet {

    ReporteDesaparicionDAO reporteDAO = new ReporteDesaparicionDAO();
    ReporteDesaparicion reporte = new ReporteDesaparicion();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                List<ReporteDesaparicion> lista = reporteDAO.listarReportes();
                request.setAttribute("reportes", lista);
                request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/listar_reportes.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                reporteDAO.eliminarReporte(idEliminar);
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                ReporteDesaparicion r = reporteDAO.buscarReporte(idEditar);
                request.setAttribute("reporte", r);
                request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/editar_reporte.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
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
                    reporte.setR_Mascota(Integer.parseInt(request.getParameter("r_mascota")));
                    reporte.setR_Usuario(Integer.parseInt(request.getParameter("r_usuario")));
                    reporte.setFechaDesaparicion(parseDate(request.getParameter("fecha_desaparicion")));
                    reporte.setUbicacionUltimaVez(request.getParameter("ubicacionultimavez"));
                    reporte.setRecompensa(Double.parseDouble(request.getParameter("recompensa")));
                    reporte.setEstadoReporte(request.getParameter("estadoreporte"));
                    reporte.setFecha_Registro(parseDate(request.getParameter("fecha_registro")));
                    reporteDAO.registrarReporte(reporte);
                    request.setAttribute("mensaje", "¡Reporte registrado exitosamente!");
                    request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/registrar_reporte.jsp").forward(request, response);
                    break;

                case "actualizar":
                    reporte.setReporteID(Integer.parseInt(request.getParameter("reporteid")));
                    reporte.setR_Mascota(Integer.parseInt(request.getParameter("r_mascota")));
                    reporte.setR_Usuario(Integer.parseInt(request.getParameter("r_usuario")));
                    reporte.setFechaDesaparicion(parseDate(request.getParameter("fecha_desaparicion")));
                    reporte.setUbicacionUltimaVez(request.getParameter("ubicacionultimavez"));
                    reporte.setRecompensa(Double.parseDouble(request.getParameter("recompensa")));
                    reporte.setEstadoReporte(request.getParameter("estadoreporte"));
                    reporte.setFecha_Registro(parseDate(request.getParameter("fecha_registro")));
                    reporteDAO.modificarReporte(reporte);
                    response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                    break;

                default:
                    response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el reporte: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private Date parseDate(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(fecha);
    }
}

