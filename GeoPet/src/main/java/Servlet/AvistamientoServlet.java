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
 * Servlet controlador para gestionar operaciones CRUD de Avistamientos de mascotas.
 *
 * Este servlet actúa como el punto central de control para todas las operaciones
 * relacionadas con avistamientos de mascotas reportadas como perdidas. Maneja
 * solicitudes HTTP tanto GET como POST y coordina las interacciones entre la
 * capa de presentación (JSPs) y la capa de acceso a datos (DAO).
 *
 * Funcionalidades principales:
 * - Agregar nuevos avistamientos a reportes existentes
 * - Editar avistamientos existentes (solo por el autor)
 * - Eliminar avistamientos (eliminación lógica)
 * - Obtener datos específicos de avistamientos (para AJAX)
 * - Listar avistamientos por reporte
 *
 * Características de seguridad:
 * - Validación de sesiones de usuario
 * - Verificación de permisos de propiedad para edición/eliminación
 * - Validación exhaustiva de parámetros de entrada
 * - Manejo seguro de excepciones con redirecciones apropiadas
 *
 * El servlet utiliza el patrón MVC (Model-View-Controller) donde actúa como
 * controlador, delegando operaciones de datos al DAO y redirigiendo a vistas
 * apropiadas según el resultado de las operaciones.
 *
 * @author Sistema de Gestión DAW
 * @version 1.0
 */
@WebServlet(name = "AvistamientoServlet", urlPatterns = {"/AvistamientoServlet"})
public class AvistamientoServlet extends HttpServlet {

    /**
     * Instancia del DAO para realizar operaciones de base de datos de avistamientos.
     */
    private AvistamientoDAO avistamientoDAO;

    /**
     * Método de inicialización del servlet que se ejecuta una vez al cargar la aplicación.
     * Inicializa las dependencias necesarias para el funcionamiento del servlet.
     *
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        super.init();
        avistamientoDAO = new AvistamientoDAO();
    }

    /**
     * Método central de procesamiento que maneja todas las solicitudes HTTP.
     *
     * Analiza el parámetro 'accion' para determinar qué operación realizar
     * y delega la ejecución al método específico correspondiente. Implementa
     * un patrón de comando donde cada acción se mapea a un método específico.
     *
     * Acciones soportadas:
     * - agregar: Crear un nuevo avistamiento
     * - editar: Modificar un avistamiento existente
     * - eliminar: Eliminar lógicamente un avistamiento
     * - obtener: Recuperar datos de un avistamiento específico (para AJAX)
     * - listar: Mostrar avistamientos de un reporte (acción por defecto)
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
     * Procesa la creación de un nuevo avistamiento asociado a un reporte de desaparición.
     *
     * Este método maneja el flujo completo de creación de avistamientos:
     * 1. Validación de autenticación del usuario
     * 2. Extracción y validación de parámetros de entrada
     * 3. Conversión de tipos de datos (fechas, IDs)
     * 4. Creación del objeto Avistamiento
     * 5. Persistencia en base de datos a través del DAO
     * 6. Manejo de resultados y redirección apropiada
     *
     * Validaciones implementadas:
     * - Usuario autenticado en sesión
     * - ID de reporte válido y presente
     * - Campos obligatorios completos (fecha, ubicación, descripción)
     * - Formato de fecha correcto (yyyy-MM-dd)
     *
     * @param request HttpServletRequest conteniendo los datos del avistamiento
     * @param response HttpServletResponse para redirecciones y respuestas
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void agregarAvistamiento(HttpServletRequest request, HttpServletResponse response)
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
            String fechaAvistamientoStr = request.getParameter("fechaAvistamiento");
            String ubicacion = request.getParameter("ubicacion");
            String descripcion = request.getParameter("descripcion");
            String contacto = request.getParameter("contacto");

            // Validación de ID de reporte
            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                request.getSession().setAttribute("error", "ID de reporte no válido");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            // Validación de campos obligatorios
            if (fechaAvistamientoStr == null || fechaAvistamientoStr.trim().isEmpty() ||
                    ubicacion == null || ubicacion.trim().isEmpty() ||
                    descripcion == null || descripcion.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Todos los campos del avistamiento son obligatorios");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);

            // Conversión de fecha con formato específico
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaAvistamiento = sdf.parse(fechaAvistamientoStr);

            // Construcción del objeto Avistamiento con datos validados
            Avistamiento avistamiento = new Avistamiento();
            avistamiento.setR_Reporte(reporteId);
            avistamiento.setR_UsuarioReportante(usuarioId);
            avistamiento.setFecha_Avistamiento(fechaAvistamiento);
            avistamiento.setUbicacion(ubicacion.trim());
            avistamiento.setDescripcion(descripcion.trim());
            avistamiento.setContacto(contacto != null ? contacto.trim() : "");
            avistamiento.setR_Imagen(0); // Imagen opcional, por implementar

            // Persistencia del avistamiento en base de datos
            boolean exito = avistamientoDAO.agregarAvistamiento(avistamiento);

            // Manejo de resultado de la operación
            if (exito) {
                request.getSession().setAttribute("mensaje", "Avistamiento agregado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al agregar el avistamiento");
            }

            // Redirección al detalle del reporte con el nuevo avistamiento
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (ParseException e) {
            // Manejo específico de errores de formato de fecha
            System.err.println("ERROR: Fecha inválida - " + e.getMessage());
            request.getSession().setAttribute("error", "Formato de fecha inválido. Use YYYY-MM-DD");
            String reporteIdStr = request.getParameter("reporteId");
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
        } catch (NumberFormatException e) {
            // Manejo de errores de conversión de ID numérico
            System.err.println("ERROR: ID de reporte inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID de reporte inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            // Manejo general de excepciones inesperadas
            System.err.println("ERROR: Excepción en agregarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Procesa la edición de un avistamiento existente.
     *
     * Implementa un flujo de edición seguro que incluye:
     * 1. Validación de autenticación y autorización
     * 2. Verificación de permisos de propiedad del avistamiento
     * 3. Validación de datos de entrada
     * 4. Actualización de los datos en base de datos
     * 5. Manejo de resultados y redirección
     *
     * Características de seguridad:
     * - Solo el autor original puede editar el avistamiento
     * - Validación exhaustiva de parámetros
     * - Manejo seguro de excepciones
     *
     * @param request HttpServletRequest con los datos actualizados del avistamiento
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void editarAvistamiento(HttpServletRequest request, HttpServletResponse response)
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
            String avistamientoIdStr = request.getParameter("avistamientoId");
            String reporteIdStr = request.getParameter("reporteId");
            String fechaAvistamientoStr = request.getParameter("fechaAvistamiento");
            String ubicacion = request.getParameter("ubicacion");
            String descripcion = request.getParameter("descripcion");
            String contacto = request.getParameter("contacto");

            // Validación de completitud de datos
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

            // Verificación de permisos de propiedad
            if (!avistamientoDAO.esUsuarioPropietarioAvistamiento(avistamientoId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para editar este avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Conversión de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaAvistamiento = sdf.parse(fechaAvistamientoStr);

            // Construcción del objeto con datos actualizados
            Avistamiento avistamiento = new Avistamiento();
            avistamiento.setAvistamientoID(avistamientoId);
            avistamiento.setFecha_Avistamiento(fechaAvistamiento);
            avistamiento.setUbicacion(ubicacion.trim());
            avistamiento.setDescripcion(descripcion.trim());
            avistamiento.setContacto(contacto != null ? contacto.trim() : "");
            avistamiento.setR_Imagen(0); // Mantenimiento de imagen existente

            // Actualización en base de datos
            boolean exito = avistamientoDAO.editarAvistamiento(avistamiento);

            // Manejo de resultado
            if (exito) {
                request.getSession().setAttribute("mensaje", "Avistamiento editado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al editar el avistamiento");
            }

            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (ParseException e) {
            System.err.println("ERROR: Fecha inválida - " + e.getMessage());
            request.getSession().setAttribute("error", "Formato de fecha inválido. Use YYYY-MM-DD");
            String reporteIdStr = request.getParameter("reporteId");
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteIdStr);
        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción en editarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Procesa la eliminación lógica de un avistamiento.
     *
     * Implementa eliminación lógica (cambio de estatus) en lugar de eliminación
     * física para mantener integridad de datos y permitir auditorías.
     *
     * Características de seguridad:
     * - Verificación de autenticación del usuario
     * - Validación de permisos de propiedad
     * - Eliminación lógica preservando datos para auditoría
     *
     * @param request HttpServletRequest con IDs del avistamiento y reporte
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void eliminarAvistamiento(HttpServletRequest request, HttpServletResponse response)
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
            String avistamientoIdStr = request.getParameter("avistamientoId");
            String reporteIdStr = request.getParameter("reporteId");

            // Validación de parámetros requeridos
            if (avistamientoIdStr == null || reporteIdStr == null ||
                    avistamientoIdStr.trim().isEmpty() || reporteIdStr.trim().isEmpty()) {

                request.getSession().setAttribute("error", "Datos incompletos para eliminar avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                return;
            }

            int avistamientoId = Integer.parseInt(avistamientoIdStr);
            int reporteId = Integer.parseInt(reporteIdStr);

            // Verificación de permisos de propiedad
            if (!avistamientoDAO.esUsuarioPropietarioAvistamiento(avistamientoId, usuarioId)) {
                request.getSession().setAttribute("error", "No tienes permisos para eliminar este avistamiento");
                response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);
                return;
            }

            // Eliminación lógica del avistamiento
            boolean exito = avistamientoDAO.eliminarAvistamiento(avistamientoId);

            // Manejo de resultado
            if (exito) {
                request.getSession().setAttribute("mensaje", "Avistamiento eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el avistamiento");
            }

            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID inválido - " + e.getMessage());
            request.getSession().setAttribute("error", "ID inválido");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción en eliminarAvistamiento - " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error interno del servidor");
            response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        }
    }

    /**
     * Obtiene los datos de un avistamiento específico en formato JSON para solicitudes AJAX.
     *
     * Este método proporciona una API REST-like para obtener datos de avistamientos
     * sin recargar la página completa. Es útil para formularios de edición dinámicos
     * y interfaces de usuario reactivas.
     *
     * Formato de respuesta JSON:
     * - Éxito: {success: true, fechaAvistamiento: "...", ubicacion: "...", descripcion: "...", contacto: "..."}
     * - Error: {success: false, message: "descripción del error"}
     *
     * @param request HttpServletRequest conteniendo el ID del avistamiento
     * @param response HttpServletResponse para enviar datos JSON
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void obtenerAvistamiento(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String avistamientoIdStr = request.getParameter("avistamientoId");

            // Validación de parámetro requerido
            if (avistamientoIdStr == null || avistamientoIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int avistamientoId = Integer.parseInt(avistamientoIdStr);
            AvistamientoConRelaciones avistamiento = avistamientoDAO.obtenerAvistamientoPorId(avistamientoId);

            // Configuración de respuesta JSON
            response.setContentType("application/json");

            if (avistamiento != null) {
                // Construcción de respuesta JSON exitosa
                String json = String.format(
                        "{\"success\": true, \"fechaAvistamiento\": \"%s\", \"ubicacion\": \"%s\", " +
                                "\"descripcion\": \"%s\", \"contacto\": \"%s\"}",
                        new SimpleDateFormat("yyyy-MM-dd").format(avistamiento.getAvistamiento().getFecha_Avistamiento()),
                        escaparJson(avistamiento.getAvistamiento().getUbicacion()),
                        escaparJson(avistamiento.getAvistamiento().getDescripcion()),
                        escaparJson(avistamiento.getAvistamiento().getContacto())
                );

                response.getWriter().write(json);
            } else {
                // Respuesta de error: avistamiento no encontrado
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Avistamiento no encontrado\"}");
            }

        } catch (NumberFormatException e) {
            // Error de formato de ID
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID inválido\"}");
        } catch (Exception e) {
            // Error interno del servidor
            System.err.println("ERROR: Excepción en obtenerAvistamiento - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error interno\"}");
        }
    }

    /**
     * Lista todos los avistamientos asociados a un reporte específico.
     *
     * Este método maneja solicitudes para mostrar todos los avistamientos
     * relacionados con un reporte de desaparición. Puede ser utilizado tanto
     * para solicitudes AJAX como para navegación tradicional.
     *
     * @param request HttpServletRequest conteniendo el ID del reporte
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void listarAvistamientos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String reporteIdStr = request.getParameter("reporteId");

            // Validación de parámetro requerido
            if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int reporteId = Integer.parseInt(reporteIdStr);
            List<AvistamientoConRelaciones> avistamientos = avistamientoDAO.obtenerAvistamientosPorReporte(reporteId);

            // Configuración de atributos para la vista
            request.setAttribute("avistamientos", avistamientos);
            request.setAttribute("reporteId", reporteId);

            // Redirección al detalle del reporte con avistamientos cargados
            response.sendRedirect("ReporteDesaparicionServlet?accion=detalle&id=" + reporteId);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("ERROR: Excepción en listarAvistamientos - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Método utilitario para escapar caracteres especiales en strings JSON.
     *
     * Previene errores de sintaxis JSON y ataques de inyección al escapar
     * caracteres especiales como comillas, barras invertidas y caracteres de control.
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
}