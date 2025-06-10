package Servlet;

import Modelo.DAO.AvistamientoDAO;
import Modelo.JavaBeans.Avistamiento;
import Modelo.JavaBeans.AvistamientoConRelaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Servlet para gestionar operaciones CRUD de Avistamientos
 */
@WebServlet(name = "AvistamientoServlet", urlPatterns = {"/AvistamientoServlet"})
public class AvistamientoServlet extends HttpServlet {

    private AvistamientoDAO avistamientoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        avistamientoDAO = new AvistamientoDAO();
        System.out.println("=== AvistamientoServlet inicializado ===");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG AvistamientoServlet ===");
        System.out.println("DEBUG SERVLET: Acción recibida = " + accion);

        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        try {
            switch (accion.toLowerCase()) {
                case "agregar":
                    agregarAvistamiento(request, response);
                    break;
                case "editar":
                    editarAvistamiento(request, response);
                    break;
                case "eliminar":
                    eliminarAvistamiento(request, response);
                    break;
                case "obtener":
                    obtenerAvistamiento(request, response);
                    break;
                case "listar":
                    listarAvistamientos(request, response);
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
     * Agregar nuevo avistamiento
     */
    private void agregarAvistamiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - AGREGAR AVISTAMIENTO ===");

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
            String fechaAvistamientoStr = request.getParameter("fechaAvistamiento");
            String ubicacion = request.getParameter("ubicacion");
            String descripcion = request.getParameter("descripcion");
            String contacto = request.getParameter("contacto");

            System.out.println("DEBUG SERVLET: ReporteID=" + reporteIdStr + ", UsuarioID=" + usuarioId);
            System.out.println("DEBUG SERVLET: Fecha=" + fechaAvistamientoStr + ", Ubicacion=" + ubicacion);

            // Validaciones
            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                request.getSession().setAttribute("error", "ID de reporte no válido");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            if (fechaAvistamientoStr == null || fechaAvistamientoStr.trim().isEmpty() ||
                    ubicacion == null || ubicacion.trim().isEmpty() ||
                    descripcion == null || descripcion.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Todos los campos del avistamiento son obligatorios");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);

            // Convertir fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaAvistamiento = sdf.parse(fechaAvistamientoStr);

            // Crear avistamiento
            Avistamiento avistamiento = new Avistamiento();
            avistamiento.setR_Reporte(reporteId);
            avistamiento.setR_UsuarioReportante(usuarioId);
            avistamiento.setFecha_Avistamiento(fechaAvistamiento);
            avistamiento.setUbicacion(ubicacion.trim());
            avistamiento.setDescripcion(descripcion.trim());
            avistamiento.setContacto(contacto != null ? contacto.trim() : "");
            avistamiento.setR_Imagen(0); // Por ahora sin imagen

            // Guardar en BD
            boolean exito = avistamientoDAO.agregarAvistamiento(avistamiento);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Avistamiento agregado exitosamente");
                System.out.println("DEBUG SERVLET: Avistamiento agregado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al agregar el avistamiento");
                System.out.println("DEBUG SERVLET: Error al agregar avistamiento");
            }

            // Redirigir al detalle del reporte
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (ParseException e) {
            System.out.println("ERROR SERVLET: Fecha inválida - " + e.getMessage());
            request.getSession().setAttribute("error", "Formato de fecha inválido. Use YYYY-MM-DD");
            String reporteIdStr = request.getParameter("reporteId");
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
        } catch (NumberFormatException e) {
            System.out.println("ERROR SERVLET: ID de reporte inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID de reporte inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en agregarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Editar avistamiento existente
     */
    private void editarAvistamiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - EDITAR AVISTAMIENTO ===");

        try {
            // Obtener usuario de la sesión
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Obtener parámetros
            String avistamientoIdStr = request.getParameter("avistamientoId");
            String reporteIdStr = request.getParameter("reporteId");
            String fechaAvistamientoStr = request.getParameter("fechaAvistamiento");
            String ubicacion = request.getParameter("ubicacion");
            String descripcion = request.getParameter("descripcion");
            String contacto = request.getParameter("contacto");

            System.out.println("DEBUG SERVLET: AvistamientoID=" + avistamientoIdStr + ", ReporteID=" + reporteIdStr);

            // Validaciones
            if (avistamientoIdStr == null || reporteIdStr == null || fechaAvistamientoStr == null ||
                    ubicacion == null || descripcion == null ||
                    avistamientoIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty() ||
                    fechaAvistamientoStr.trim().isEmpty() || ubicacion.trim().isEmpty() ||
                    descripcion.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para editar avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int avistamientoId = Integer.parseInt(avistamientoIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificar permisos
            if (!avistamientoDAO.esUsuarioPropietarioAvistamiento(avistamientoId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para editar este avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Convertir fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaAvistamiento = sdf.parse(fechaAvistamientoStr);

            // Crear avistamiento con datos actualizados
            Avistamiento avistamiento = new Avistamiento();
            avistamiento.setAvistamientoID(avistamientoId);
            avistamiento.setFecha_Avistamiento(fechaAvistamiento);
            avistamiento.setUbicacion(ubicacion.trim());
            avistamiento.setDescripcion(descripcion.trim());
            avistamiento.setContacto(contacto != null ? contacto.trim() : "");
            avistamiento.setR_Imagen(0); // Por ahora sin imagen

            // Actualizar en BD
            boolean exito = avistamientoDAO.editarAvistamiento(avistamiento);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Avistamiento editado exitosamente");
                System.out.println("DEBUG SERVLET: Avistamiento editado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al editar el avistamiento");
                System.out.println("DEBUG SERVLET: Error al editar avistamiento");
            }

            // Redirigir al detalle del reporte
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (ParseException e) {
            System.out.println("ERROR SERVLET: Fecha inválida - " + e.getMessage());
            request.getSession().setAttribute("error", "Formato de fecha inválido. Use YYYY-MM-DD");
            String reporteIdStr = request.getParameter("reporteId");
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
        } catch (NumberFormatException e) {
            System.out.println("ERROR SERVLET: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en editarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Eliminar avistamiento
     */
    private void eliminarAvistamiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - ELIMINAR AVISTAMIENTO ===");

        try {
            // Obtener usuario de la sesión
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Obtener parámetros
            String avistamientoIdStr = request.getParameter("avistamientoId");
            String reporteIdStr = request.getParameter("reporteId");

            System.out.println("DEBUG SERVLET: AvistamientoID=" + avistamientoIdStr + ", ReporteID=" + reporteIdStr);

            // Validaciones
            if (avistamientoIdStr == null || reporteIdStr == null ||
                    avistamientoIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para eliminar avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            int avistamientoId = Integer.parseInt(avistamientoIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificar permisos
            if (!avistamientoDAO.esUsuarioPropietarioAvistamiento(avistamientoId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para eliminar este avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Eliminar avistamiento
            boolean exito = avistamientoDAO.eliminarAvistamiento(avistamientoId);

            if (exito) {
                request.getSession().setAttribute("mensaje", "Avistamiento eliminado exitosamente");
                System.out.println("DEBUG SERVLET: Avistamiento eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el avistamiento");
                System.out.println("DEBUG SERVLET: Error al eliminar avistamiento");
            }

            // Redirigir al detalle del reporte
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.out.println("ERROR SERVLET: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en eliminarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Obtener avistamiento específico (para AJAX)
     */
    private void obtenerAvistamiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - OBTENER AVISTAMIENTO ===");

        try {
            String avistamientoIdStr = request.getParameter("avistamientoId");

            if (avistamientoIdStr == null || avistamientoIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int avistamientoId = Integer.parseInt(avistamientoIdStr);
            AvistamientoConRelaciones avistamiento = avistamientoDAO.obtenerAvistamientoPorId(avistamientoId);

            if (avistamiento != null) {
                request.setAttribute("avistamiento", avistamiento);
                response.setContentType("application/json");

                // Construir JSON con los datos del avistamiento
                String json = String.format(
                        "{\"success\": true, \"fechaAvistamiento\": \"%s\", \"ubicacion\": \"%s\", " +
                                "\"descripcion\": \"%s\", \"contacto\": \"%s\"}",
                        new SimpleDateFormat("yyyy-MM-dd").format(avistamiento.getAvistamiento().getFecha_Avistamiento()),
                        avistamiento.getAvistamiento().getUbicacion(),
                        avistamiento.getAvistamiento().getDescripcion(),
                        avistamiento.getAvistamiento().getContacto()
                );

                response.getWriter().write(json);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Avistamiento no encontrado\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID inválido\"}");
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en obtenerAvistamiento - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error interno\"}");
        }
    }

    /**
     * Listar avistamientos de un reporte (para AJAX)
     */
    private void listarAvistamientos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG - LISTAR AVISTAMIENTOS ===");

        try {
            String reporteIdStr = request.getParameter("reporteId");

            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);
            List<AvistamientoConRelaciones> avistamientos = avistamientoDAO.obtenerAvistamientosPorReporte(reporteId);

            request.setAttribute("avistamientos", avistamientos);
            request.setAttribute("reporteId", reporteId);

            // Forward a una vista parcial para AJAX o redireccionar
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("ERROR SERVLET: Exception en listarAvistamientos - " + e.getMessage());
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
        return "Servlet para gestionar operaciones CRUD de Avistamientos";
    }
}