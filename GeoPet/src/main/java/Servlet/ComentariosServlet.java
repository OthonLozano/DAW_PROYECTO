package Servlet;

import Modelo.DAO.ComentariosDAO;
import Modelo.JavaBeans.Comentarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ComentariosServlet", urlPatterns = {"/ComentariosServlet"})
public class ComentariosServlet extends HttpServlet {

    ComentariosDAO comentariosDAO = new ComentariosDAO();
    Comentarios comentario = new Comentarios();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                List<Comentarios> lista = comentariosDAO.listarComentarios();
                request.setAttribute("comentarios", lista);
                request.getRequestDispatcher("Vistas_JSP/Comentarios/listar_comentarios.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                comentariosDAO.eliminarComentario(idEliminar);
                response.sendRedirect("ComentariosServlet?accion=listar");
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Comentarios c = comentariosDAO.buscarComentario(idEditar);
                request.setAttribute("comentario", c);
                request.getRequestDispatcher("Vistas_JSP/Comentarios/editar_comentario.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("ComentariosServlet?accion=listar");
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
                    comentario.setR_Usuario(Integer.parseInt(request.getParameter("r_usuario")));
                    comentario.setR_Reporte(Integer.parseInt(request.getParameter("r_reporte")));
                    comentario.setContenido(request.getParameter("contenido"));
                    comentario.setFecha_Comentario(parseDate(request.getParameter("fecha_comentario")));
                    comentariosDAO.registrarComentario(comentario);
                    request.setAttribute("mensaje", "¡Comentario registrado exitosamente!");
                    request.getRequestDispatcher("Vistas_JSP/Comentarios/registrar_comentario.jsp").forward(request, response);
                    break;

                case "actualizar":
                    comentario.setComentarioID(Integer.parseInt(request.getParameter("comentarioid")));
                    comentario.setR_Usuario(Integer.parseInt(request.getParameter("r_usuario")));
                    comentario.setR_Reporte(Integer.parseInt(request.getParameter("r_reporte")));
                    comentario.setContenido(request.getParameter("contenido"));
                    comentario.setFecha_Comentario(parseDate(request.getParameter("fecha_comentario")));
                    comentariosDAO.modificarComentario(comentario);
                    response.sendRedirect("ComentariosServlet?accion=listar");
                    break;

                default:
                    response.sendRedirect("ComentariosServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el comentario: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private Date parseDate(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(fecha);
    }
}
