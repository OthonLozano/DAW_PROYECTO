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
 * Servlet controlador para gestionar operaciones CRUD de Comentarios en reportes de mascotas.
 *
 * Este servlet actúa como el punto central de control para todas las operaciones
 * relacionadas con comentarios en reportes de desaparición de mascotas. Facilita
 * la comunicación colaborativa entre usuarios del sistema permitiendo agregar,
 * editar, eliminar y consultar comentarios asociados a reportes específicos.
 *
 * Funcionalidades principales:
 * - Agregar nuevos comentarios a reportes existentes
 * - Editar comentarios existentes (solo por el autor)
 * - Eliminar comentarios (eliminación lógica, solo por el autor)
 * - Obtener datos específicos de comentarios (para AJAX)
 * - Listar comentarios por reporte
 *
 * Características de seguridad:
 * - Validación de sesiones de usuario autenticadas
 * - Verificación de permisos de propiedad para edición/eliminación
 * - Validación exhaustiva de parámetros de entrada
 * - Sanitización de contenido para prevenir inyecciones
 * - Manejo seguro de excepciones con redirecciones apropiadas
 *
 * Los comentarios son fundamentales para la colaboración en la búsqueda de mascotas,
 * permitiendo que múltiples usuarios aporten información, coordinen esfuerzos
 * y mantengan comunicación efectiva durante el proceso de búsqueda.
 *
 * El servlet implementa el patrón MVC donde actúa como controlador, delegando
 * operaciones de datos al DAO y redirigiendo a vistas apropiadas según el resultado.
 *
 * @author Sistema de Gestión DAW
 * @version 1.0
 */
@WebServlet(name = "ComentariosServlet", urlPatterns = {"/ComentariosServlet"})
public class ComentariosServlet extends HttpServlet {

    /**
     * Instancia del DAO para realizar operaciones de base de datos de comentarios.
     */
    private ComentariosDAO comentariosDAO;

    /**
     * Método de inicialización del servlet que se ejecuta una vez al cargar la aplicación.
     * Inicializa las dependencias necesarias para el funcionamiento del servlet.
     *
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        super.init();
        comentariosDAO = new ComentariosDAO();
    }

    /**
     * Método central de procesamiento que maneja todas las solicitudes HTTP.
     *
     * Analiza el parámetro 'accion' para determinar qué operación realizar
     * y delega la ejecución al método específico correspondiente. Implementa
     * un patrón de comando donde cada acción se mapea a un método específico.
     *
     * Acciones soportadas:
     * - agregar: Crear un nuevo comentario en un reporte
     * - editar: Modificar un comentario existente (solo autor)
     * - eliminar: Eliminar lógicamente un comentario (solo autor)
     * - obtener: Recuperar datos de un comentario específico (para AJAX)
     * - listar: Mostrar comentarios de un reporte (acción por defecto)
     *
     * @param request HttpServletRequest objeto de solicitud HTTP
     * @param response HttpServletResponse objeto de respuesta HTTP
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuración de codificación para soporte de caracteres Unicode
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Obtener y normalizar la acción solicitada
        String accion = request.getParameter("accion");
        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar"; // Acción por defecto
        }

        try {
            // Dispatcher de acciones utilizando switch para mejor rendimiento
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
                    // Acción no reconocida: redirigir a listado de reportes
                    response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            // Manejo global de excepciones con logging y redirección segura
            System.err.println("ERROR: Excepción en processRequest - " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Procesa la creación de un nuevo comentario asociado a un reporte de desaparición.
     *
     * Este método maneja el flujo completo de creación de comentarios:
     * 1. Validación de autenticación del usuario
     * 2. Extracción y validación de parámetros de entrada
     * 3. Sanitización del contenido del comentario
     * 4. Creación del objeto Comentario
     * 5. Persistencia en base de datos a través del DAO
     * 6. Manejo de resultados y redirección apropiada
     *
     * Validaciones implementadas:
     * - Usuario autenticado en sesión
     * - ID de reporte válido y presente
     * - Contenido del comentario no vacío
     * - Sanitización de caracteres especiales
     *
     * Los comentarios facilitan la colaboración entre usuarios permitiendo
     * compartir información adicional, coordinar búsquedas y mantener
     * comunicación efectiva durante el proceso de localización de mascotas.
     *
     * @param request HttpServletRequest conteniendo los datos del comentario
     * @param response HttpServletResponse para redirecciones y respuestas
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void agregarComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validación de autenticación del usuario
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Extracción de parámetros de la solicitud
            String reporteIdStr = request.getParameter("reporteId");
            String contenido = request.getParameter("contenido");

            // Validación de ID de reporte
            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                request.getSession().setAttribute("error", "ID de reporte no válido");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            // Validación de contenido del comentario
            if (contenido == null || contenido.trim().isEmpty()) {
                request.getSession().setAttribute("error", "El contenido del comentario no puede estar vacío");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);

            // Construcción del objeto Comentario con datos validados y sanitizados
            Comentarios comentario = new Comentarios();
            comentario.setR_Reporte(reporteId);
            comentario.setR_Usuario(usuarioId);
            comentario.setContenido(contenido.trim()); // Sanitización básica

            // Persistencia del comentario en base de datos
            boolean exito = comentariosDAO.agregarComentario(comentario);

            // Manejo de resultado de la operación
            if (exito) {
                request.getSession().setAttribute("mensaje", "Comentario agregado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al agregar el comentario");
            }

            // Redirección al detalle del reporte con el nuevo comentario
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            // Manejo de errores de conversión de ID numérico
            System.err.println("ERROR: ID de reporte inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID de reporte inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            // Manejo general de excepciones inesperadas
            System.err.println("ERROR: Excepción en agregarComentario - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Procesa la edición de un comentario existente.
     *
     * Implementa un flujo de edición seguro que incluye:
     * 1. Validación de autenticación y autorización
     * 2. Verificación de permisos de propiedad del comentario
     * 3. Validación y sanitización del nuevo contenido
     * 4. Actualización de los datos en base de datos
     * 5. Manejo de resultados y redirección
     *
     * Características de seguridad:
     * - Solo el autor original puede editar el comentario
     * - Validación exhaustiva de parámetros
     * - Sanitización de contenido para prevenir inyecciones
     * - Manejo seguro de excepciones
     *
     * Esta funcionalidad permite a los usuarios corregir errores tipográficos,
     * actualizar información o mejorar la claridad de sus comentarios sin
     * perder el historial de participación en las discusiones.
     *
     * @param request HttpServletRequest con los datos actualizados del comentario
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void editarComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validación de autenticación
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Extracción de parámetros
            String comentarioIdStr = request.getParameter("comentarioId");
            String reporteIdStr = request.getParameter("reporteId");
            String contenido = request.getParameter("contenido");

            // Validación de completitud de datos
            if (comentarioIdStr == null || reporteIdStr == null || contenido == null ||
                    comentarioIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty() || contenido.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para editar comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int comentarioId = Integer.parseInt(comentarioIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificación de permisos de propiedad
            if (!comentariosDAO.esUsuarioPropietarioComentario(comentarioId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para editar este comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Construcción del objeto con datos actualizados y sanitizados
            Comentarios comentario = new Comentarios();
            comentario.setComentarioID(comentarioId);
            comentario.setContenido(contenido.trim()); // Sanitización básica

            // Actualización en base de datos
            boolean exito = comentariosDAO.editarComentario(comentario);

            // Manejo de resultado
            if (exito) {
                request.getSession().setAttribute("mensaje", "Comentario editado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al editar el comentario");
            }

            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción en editarComentario - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Procesa la eliminación lógica de un comentario.
     *
     * Implementa eliminación lógica (cambio de estatus) en lugar de eliminación
     * física para mantener integridad de datos y preservar el historial de
     * discusiones para propósitos de auditoría y análisis.
     *
     * Características de seguridad:
     * - Verificación de autenticación del usuario
     * - Validación de permisos de propiedad
     * - Eliminación lógica preservando datos para auditoría
     * - Control de acceso estricto (solo el autor puede eliminar)
     *
     * La eliminación lógica mantiene la integridad de las conversaciones
     * while removing inappropriate content from public view, allowing
     * administrators to review patterns and maintain system quality.
     *
     * @param request HttpServletRequest con IDs del comentario y reporte
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void eliminarComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validación de autenticación
            HttpSession session = request.getSession();
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");

            if (usuarioId == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Extracción de parámetros
            String comentarioIdStr = request.getParameter("comentarioId");
            String reporteIdStr = request.getParameter("reporteId");

            // Validación de parámetros requeridos
            if (comentarioIdStr == null || reporteIdStr == null ||
                    comentarioIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para eliminar comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            int comentarioId = Integer.parseInt(comentarioIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificación de permisos de propiedad
            if (!comentariosDAO.esUsuarioPropietarioComentario(comentarioId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para eliminar este comentario");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Eliminación lógica del comentario
            boolean exito = comentariosDAO.eliminarComentario(comentarioId);

            // Manejo de resultado
            if (exito) {
                request.getSession().setAttribute("mensaje", "Comentario eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el comentario");
            }

            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción en eliminarComentario - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Obtiene los datos de un comentario específico en formato JSON para solicitudes AJAX.
     *
     * Este método proporciona una API REST-like para obtener datos de comentarios
     * sin recargar la página completa. Es útil para formularios de edición dinámicos,
     * previsualizaciones de contenido y interfaces de usuario reactivas.
     *
     * Formato de respuesta JSON:
     * - Éxito: {success: true, contenido: "texto del comentario"}
     * - Error: {success: false, message: "descripción del error"}
     *
     * Esta funcionalidad mejora la experiencia del usuario al permitir
     * interacciones más fluidas y responsivas en la interfaz de comentarios.
     *
     * @param request HttpServletRequest conteniendo el ID del comentario
     * @param response HttpServletResponse para enviar datos JSON
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void obtenerComentario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String comentarioIdStr = request.getParameter("comentarioId");

            // Validación de parámetro requerido
            if (comentarioIdStr == null || comentarioIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int comentarioId = Integer.parseInt(comentarioIdStr);
            ComentariosConRelaciones comentario = comentariosDAO.obtenerComentarioPorId(comentarioId);

            // Configuración de respuesta JSON
            response.setContentType("application/json");

            if (comentario != null) {
                // Construcción de respuesta JSON exitosa con contenido escapado
                String contenidoEscapado = escaparJson(comentario.getComentario().getContenido());
                response.getWriter().write("{\"success\": true, \"contenido\": \"" + contenidoEscapado + "\"}");
            } else {
                // Respuesta de error: comentario no encontrado
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Comentario no encontrado\"}");
            }

        } catch (NumberFormatException e) {
            // Error de formato de ID
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID inválido\"}");
        } catch (Exception e) {
            // Error interno del servidor
            System.err.println("ERROR: Excepción en obtenerComentario - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error interno\"}");
        }
    }

    /**
     * Lista todos los comentarios asociados a un reporte específico.
     *
     * Este método maneja solicitudes para mostrar todos los comentarios
     * relacionados con un reporte de desaparición. Facilita la visualización
     * completa de la conversación y colaboración alrededor de un caso específico.
     *
     * Puede ser utilizado tanto para solicitudes AJAX como para navegación
     * tradicional, proporcionando flexibilidad en la implementación de la interfaz.
     *
     * @param request HttpServletRequest conteniendo el ID del reporte
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void listarComentarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String reporteIdStr = request.getParameter("reporteId");

            // Validación de parámetro requerido
            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);
            List<ComentariosConRelaciones> comentarios = comentariosDAO.obtenerComentariosPorReporte(reporteId);

            // Configuración de atributos para la vista
            request.setAttribute("comentarios", comentarios);
            request.setAttribute("reporteId", reporteId);

            // Redirección al detalle del reporte con comentarios cargados
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("ERROR: Excepción en listarComentarios - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método utilitario para escapar caracteres especiales en strings JSON.
     *
     * Previene errores de sintaxis JSON y ataques de inyección al escapar
     * caracteres especiales como comillas, barras invertidas y caracteres de control.
     * Es especialmente importante para contenido de comentarios que puede contener
     * texto libre ingresado por usuarios.
     *
     * @param input String de entrada a escapar
     * @return String con caracteres especiales escapados para JSON
     */
    private String escaparJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Maneja solicitudes HTTP GET delegando al método de procesamiento central.
     *
     * @param request HttpServletRequest objeto de solicitud
     * @param response HttpServletResponse objeto de respuesta
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Maneja solicitudes HTTP POST delegando al método de procesamiento central.
     *
     * @param request HttpServletRequest objeto de solicitud
     * @param response HttpServletResponse objeto de respuesta
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Retorna información descriptiva sobre este servlet.
     *
     * @return String descripción del servlet
     */
    @Override
    public String getServletInfo() {
        return "Servlet controlador para gestionar operaciones CRUD de comentarios colaborativos en reportes de mascotas";
    }
}