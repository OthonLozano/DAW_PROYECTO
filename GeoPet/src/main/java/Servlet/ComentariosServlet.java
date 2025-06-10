package Servlet;

import Modelo.DAO.ComentariosDAO;
import Modelo.JavaBeans.Comentarios;
import Modelo.JavaBeans.ComentariosConRelaciones;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestionar operaciones CRUD de Comentarios
 */
@WebServlet(name = "ComentariosServlet", urlPatterns = {"/ComentariosServlet"})
public class ComentariosServlet extends HttpServlet {

    private ComentariosDAO comentariosDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        comentariosDAO = new ComentariosDAO();
        System.out.println("=== ComentariosServlet inicializado ===");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG ComentariosServlet ===");
        System.out.println("DEBUG SERVLET: Acción recibida = " + accion);

        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        try {
            switch (accion.toLowerCase()) {
                case "agregar":
                    agregarComentario(request, response);
                    break;
                case "editar":
                    editarComentario(request, response);
                    break;
                case "eliminar":
                    eliminarComentario(request, response);
                    break;
                case "obtener":
                    obtenerComentario(request, response);
                    break;
                case "listar":
                    listarComentarios(request, response);
                    break;
                default:
                    System.out.println("DEBUG SERVLET: Acción no reconocida, redirigiendo a listar");
                    response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en processRequest - " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Agregar nuevo comentario
     */
    private void agregarComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - AGREGAR COMENTARIO ===");

        try {
            // Obtener usuario de la sesión
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                System.out.println("DEBUG SERVLET: Usuario no autenticado");
                response.sendRedirect("login.jsp");
                return;
            }

            // Obtener parámetros
            String reporteIdStr = request.getParameter("reporteId");
            String contenido = request.getParameter("contenido");

            System.out.println("DEBUG SERVLET: ReporteID=" + reporteIdStr + ", UsuarioID=" + usuarioId);
            System.out.println("DEBUG SERVLET: Contenido=" + contenido);

            // Validaciones
            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                request.getSession().setAttribute("error", "ID de reporte no válido");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            if (contenido == null || contenido.trim().isEmpty()) {
                request.getSession().setAttribute("error", "El contenido del comentario no puede estar vacío");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);

            // Crear comentario
            Comentarios comentario = new Comentarios();
            comentario.setR_Reporte(reporteId);
            comentario.setR_Usuario(usuarioId);
            comentario.setContenido(contenido.trim());

            // Guardar en BD
            boolean exito = comentariosDAO.agregarComentario(comentario);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Comentario agregado exitosamente");
                System.out.println("DEBUG SERVLET: Comentario agregado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al agregar el comentario");
                System.out.println("DEBUG SERVLET: Error al agregar comentario");
            }

            // Redirigir al detalle del reporte
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.out.println("ERROR SERVLET: ID de reporte inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID de reporte inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en agregarComentario - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Editar comentario existente
     */
    private void editarComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - EDITAR COMENTARIO ===");

        try {
            // Obtener usuario de la sesión
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Obtener parámetros
            String comentarioIdStr = request.getParameter("comentarioId");
            String reporteIdStr = request.getParameter("reporteId");
            String contenido = request.getParameter("contenido");

            System.out.println("DEBUG SERVLET: ComentarioID=" + comentarioIdStr + ", ReporteID=" + reporteIdStr);

            // Validaciones
            if (comentarioIdStr == null || reporteIdStr == null || contenido == null ||
                    comentarioIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty() || contenido.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para editar comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int comentarioId = Integer.parseInt(comentarioIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificar permisos
            if (!comentariosDAO.esUsuarioPropietarioComentario(comentarioId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para editar este comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Crear comentario con datos actualizados
            Comentarios comentario = new Comentarios();
            comentario.setComentarioID(comentarioId);
            comentario.setContenido(contenido.trim());

            // Actualizar en BD
            boolean exito = comentariosDAO.editarComentario(comentario);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Comentario editado exitosamente");
                System.out.println("DEBUG SERVLET: Comentario editado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al editar el comentario");
                System.out.println("DEBUG SERVLET: Error al editar comentario");
            }

            // Redirigir al detalle del reporte
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.out.println("ERROR SERVLET: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en editarComentario - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Eliminar comentario
     */
    private void eliminarComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - ELIMINAR COMENTARIO ===");

        try {
            // Obtener usuario de la sesión
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Obtener parámetros
            String comentarioIdStr = request.getParameter("comentarioId");
            String reporteIdStr = request.getParameter("reporteId");

            System.out.println("DEBUG SERVLET: ComentarioID=" + comentarioIdStr + ", ReporteID=" + reporteIdStr);

            // Validaciones
            if (comentarioIdStr == null || reporteIdStr == null ||
                    comentarioIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para eliminar comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            int comentarioId = Integer.parseInt(comentarioIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificar permisos
            if (!comentariosDAO.esUsuarioPropietarioComentario(comentarioId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para eliminar este comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Eliminar comentario
            boolean exito = comentariosDAO.eliminarComentario(comentarioId);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Comentario eliminado exitosamente");
                System.out.println("DEBUG SERVLET: Comentario eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el comentario");
                System.out.println("DEBUG SERVLET: Error al eliminar comentario");
            }

            // Redirigir al detalle del reporte
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.out.println("ERROR SERVLET: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en eliminarComentario - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Obtener comentario específico (para AJAX)
     */
    private void obtenerComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - OBTENER COMENTARIO ===");

        try {
            String comentarioIdStr = request.getParameter("comentarioId");

            if (comentarioIdStr == null || comentarioIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int comentarioId = Integer.parseInt(comentarioIdStr);
            ComentariosConRelaciones comentario = comentariosDAO.obtenerComentarioPorId(comentarioId);

            if (comentario != null) {
                request.setAttribute("comentario", comentario);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true, \"contenido\": \"" +
                        comentario.getComentario().getContenido() + "\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Comentario no encontrado\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID inválido\"}");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en obtenerComentario - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error interno\"}");
        }
    }

    /**
     * Listar comentarios de un reporte (para AJAX)
     */
    private void listarComentarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - LISTAR COMENTARIOS ===");

        try {
            String reporteIdStr = request.getParameter("reporteId");

            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);
            List<ComentariosConRelaciones> comentarios = comentariosDAO.obtenerComentariosPorReporte(reporteId);

            request.setAttribute("comentarios", comentarios);
            request.setAttribute("reporteId", reporteId);

            // Forward a una vista parcial para AJAX o redireccionar
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en listarComentarios - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet para gestionar operaciones CRUD de Comentarios";
    }
}