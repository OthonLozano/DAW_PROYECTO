package Servlet;

import Modelo.DAO.ReporteDesaparicionDAO;
import Modelo.DAO.MascotasDAO;
import Modelo.DAO.ComentariosDAO;
import Modelo.DAO.AvistamientoDAO;
import Modelo.JavaBeans.ReporteDesaparicion;
import Modelo.JavaBeans.ReporteConRelaciones;
import Modelo.JavaBeans.Mascotas;
import Modelo.JavaBeans.ComentariosConRelaciones;
import Modelo.JavaBeans.AvistamientoConRelaciones;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "ReporteDesaparicionServlet", urlPatterns = {"/ReporteDesaparicionServlet"})
public class ReporteDesaparicionServlet extends HttpServlet {

    ReporteDesaparicionDAO reporteDAO = new ReporteDesaparicionDAO();
    MascotasDAO mascotaDAO = new MascotasDAO();
    private ComentariosDAO comentariosDAO = new ComentariosDAO();
    private AvistamientoDAO avistamientoDAO = new AvistamientoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG ReporteDesaparicionServlet: Accion recibida = " + accion + " ===");

        if (accion == null) {
            accion = "listar";
        }

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        switch (accion) {
            case "listar": {
                System.out.println("DEBUG: Caso listar reportes");

                // Verificar mensajes
                String mensaje = request.getParameter("mensaje");
                String error = request.getParameter("error");

                if (mensaje != null) {
                    System.out.println("DEBUG: Mensaje recibido = " + mensaje);
                    switch (mensaje) {
                        case "eliminado":
                            request.setAttribute("mensaje", "¡Reporte eliminado exitosamente!");
                            break;
                        case "actualizado":
                            request.setAttribute("mensaje", "¡Reporte actualizado exitosamente!");
                            break;
                        case "registrado":
                            request.setAttribute("mensaje", "¡Reporte registrado exitosamente!");
                            break;
                    }
                }

                if (error != null) {
                    System.out.println("DEBUG: Error recibido = " + error);
                    request.setAttribute("mensaje", "Error: " + error);
                }

                // Obtener reportes EXTERNOS (excluyendo los del usuario actual)
                List<ReporteConRelaciones> listaReportes;

                if (usuarioId != null) {
                    // Usuario logueado: mostrar solo reportes de OTROS usuarios
                    System.out.println("DEBUG: Usuario logueado - mostrando reportes externos (excluyendo usuario " + usuarioId + ")");
                    listaReportes = reporteDAO.listarReportesExternos(usuarioId);
                } else {
                    // Usuario NO logueado: mostrar todos los reportes
                    System.out.println("DEBUG: Usuario no logueado - mostrando todos los reportes");
                    listaReportes = reporteDAO.listarReportesCompletos();
                }

                System.out.println("DEBUG: Total reportes obtenidos = " + (listaReportes != null ? listaReportes.size() : "NULL"));

                request.setAttribute("reportesCompletos", listaReportes);
                request.setAttribute("usuarioLogueado", usuarioId != null); // Para el JSP
                request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/listar_reportedesaparicion.jsp").forward(request, response);
                break;
            }

            case "detalle": {
                System.out.println("=== DEBUG: Caso detalle ===");

                try {
                    // Obtener el ID del reporte desde el parámetro
                    String reporteIdStr = request.getParameter("id");
                    System.out.println("DEBUG: ID de reporte solicitado = " + reporteIdStr);

                    if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                        System.out.println("ERROR: ID de reporte no proporcionado");
                        request.setAttribute("mensaje", "ID de reporte no válido");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                        return;
                    }

                    int reporteId = Integer.parseInt(reporteIdStr);
                    System.out.println("DEBUG: Buscando reporte con ID = " + reporteId);

                    // Obtener el reporte completo con todas sus relaciones
                    ReporteConRelaciones reporteCompleto = reporteDAO.obtenerReporteCompleto(reporteId);

                    if (reporteCompleto == null || reporteCompleto.getReporte() == null) {
                        System.out.println("ERROR: Reporte no encontrado con ID = " + reporteId);
                        request.setAttribute("mensaje", "Reporte no encontrado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                        return;
                    }

                    System.out.println("DEBUG: Reporte encontrado - Mascota: " +
                            reporteCompleto.getMascota().getNombre() +
                            ", Propietario: " + reporteCompleto.getUsuario().getNombre());

                    // *** NUEVA FUNCIONALIDAD: Obtener comentarios y avistamientos ***
                    System.out.println("DEBUG: Obteniendo comentarios y avistamientos...");

                    // Obtener comentarios del reporte
                    List<ComentariosConRelaciones> comentarios = comentariosDAO.obtenerComentariosPorReporte(reporteId);
                    System.out.println("DEBUG: Comentarios obtenidos = " + (comentarios != null ? comentarios.size() : "NULL"));

                    // Obtener avistamientos del reporte
                    List<AvistamientoConRelaciones> avistamientos = avistamientoDAO.obtenerAvistamientosPorReporte(reporteId);
                    System.out.println("DEBUG: Avistamientos obtenidos = " + (avistamientos != null ? avistamientos.size() : "NULL"));

                    // *** MANEJAR MENSAJES DE SESIÓN ***
                    // Obtener mensajes de la sesión (éxito/error de comentarios y avistamientos)
                    String mensajeSession = (String) session.getAttribute("mensaje");
                    String errorSession = (String) session.getAttribute("error");

                    if (mensajeSession != null) {
                        request.setAttribute("mensaje", mensajeSession);
                        session.removeAttribute("mensaje");
                        System.out.println("DEBUG: Mensaje de sesión: " + mensajeSession);
                    }

                    if (errorSession != null) {
                        request.setAttribute("error", errorSession);
                        session.removeAttribute("error");
                        System.out.println("DEBUG: Error de sesión: " + errorSession);
                    }

                    // Pasar todos los datos a la vista
                    request.setAttribute("reporteCompleto", reporteCompleto);
                    request.setAttribute("comentarios", comentarios);
                    request.setAttribute("avistamientos", avistamientos);

                    // Información adicional para la vista
                    request.setAttribute("usuarioLogueado", usuarioId);
                    request.setAttribute("totalComentarios", comentarios != null ? comentarios.size() : 0);
                    request.setAttribute("totalAvistamientos", avistamientos != null ? avistamientos.size() : 0);

                    System.out.println("DEBUG: Enviando datos al JSP - Reporte, comentarios y avistamientos");

                    // Redirigir a la vista de detalle
                    request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/detalle_mascota_perdida.jsp").forward(request, response);

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: ID de reporte no válido - " + e.getMessage());
                    request.setAttribute("mensaje", "ID de reporte no válido: " + e.getMessage());
                    response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception en caso detalle - " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("mensaje", "Error al obtener los detalles: " + e.getMessage());
                    response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                }
                break;
            }

            case "detalle_mi_reporte": {
                System.out.println("=== DEBUG: Caso detalle_mi_reporte ===");

                // Verificar que hay un usuario logueado
                if (usuarioId == null) {
                    System.out.println("ERROR: No hay usuario logueado");
                    response.sendRedirect("login.jsp");
                    return;
                }

                try {
                    // Obtener el ID del reporte desde el parámetro
                    String reporteIdStr = request.getParameter("id");
                    System.out.println("DEBUG: ID de reporte solicitado = " + reporteIdStr);

                    if (reporteIdStr == null || reporteIdStr.trim().isEmpty()) {
                        System.out.println("ERROR: ID de reporte no proporcionado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=id_invalido");
                        return;
                    }

                    int reporteId = Integer.parseInt(reporteIdStr);
                    System.out.println("DEBUG: Buscando reporte con ID = " + reporteId);

                    // Obtener el reporte completo con todas sus relaciones
                    ReporteConRelaciones reporteCompleto = reporteDAO.obtenerReporteCompleto(reporteId);

                    if (reporteCompleto == null || reporteCompleto.getReporte() == null) {
                        System.out.println("ERROR: Reporte no encontrado con ID = " + reporteId);
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=reporte_no_encontrado");
                        return;
                    }

                    // VERIFICAR QUE EL USUARIO ES EL PROPIETARIO DEL REPORTE
                    if (!usuarioId.equals(reporteCompleto.getReporte().getR_Usuario())) {
                        System.out.println("ERROR: Usuario " + usuarioId + " no es propietario del reporte " + reporteId);
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=acceso_denegado");
                        return;
                    }

                    System.out.println("DEBUG: Reporte encontrado - Mascota: " +
                            reporteCompleto.getMascota().getNombre() +
                            ", Propietario: " + reporteCompleto.getUsuario().getNombre());

                    // Obtener comentarios y avistamientos
                    System.out.println("DEBUG: Obteniendo comentarios y avistamientos...");

                    List<ComentariosConRelaciones> comentarios = comentariosDAO.obtenerComentariosPorReporte(reporteId);
                    System.out.println("DEBUG: Comentarios obtenidos = " + (comentarios != null ? comentarios.size() : "NULL"));

                    List<AvistamientoConRelaciones> avistamientos = avistamientoDAO.obtenerAvistamientosPorReporte(reporteId);
                    System.out.println("DEBUG: Avistamientos obtenidos = " + (avistamientos != null ? avistamientos.size() : "NULL"));

                    // Manejar mensajes de sesión
                    String mensajeSession = (String) session.getAttribute("mensaje");
                    String errorSession = (String) session.getAttribute("error");

                    if (mensajeSession != null) {
                        request.setAttribute("mensaje", mensajeSession);
                        session.removeAttribute("mensaje");
                        System.out.println("DEBUG: Mensaje de sesión: " + mensajeSession);
                    }

                    if (errorSession != null) {
                        request.setAttribute("error", errorSession);
                        session.removeAttribute("error");
                        System.out.println("DEBUG: Error de sesión: " + errorSession);
                    }

                    // Pasar todos los datos a la vista
                    request.setAttribute("reporteCompleto", reporteCompleto);
                    request.setAttribute("comentarios", comentarios);
                    request.setAttribute("avistamientos", avistamientos);
                    request.setAttribute("usuarioLogueado", usuarioId);
                    request.setAttribute("totalComentarios", comentarios != null ? comentarios.size() : 0);
                    request.setAttribute("totalAvistamientos", avistamientos != null ? avistamientos.size() : 0);

                    System.out.println("DEBUG: Enviando datos al JSP de propietario");

                    // Redirigir a la vista de detalle para propietarios
                    request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/detalle_mi_mascota.jsp").forward(request, response);

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: ID de reporte no válido - " + e.getMessage());
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=id_invalido");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception en caso detalle_mi_reporte - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=error_general");
                }
                break;
            }

            case "registrar": {
                System.out.println("=== DEBUG: Caso registrar reporte ===");

                // Verificar que hay un usuario logueado
                if (usuarioId == null) {
                    System.out.println("ERROR: No hay usuario logueado - usuarioId es NULL");
                    request.setAttribute("mensaje", "Error: Debe iniciar sesión para registrar un reporte.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }

                System.out.println("DEBUG: Usuario logueado ID = " + usuarioId);

                // Obtener mascotas del usuario que NO tienen reportes activos
                List<Mascotas> mascotasDisponibles = mascotaDAO.ListarMascotasSinReporteActivo(usuarioId);
                System.out.println("DEBUG: Mascotas disponibles para reportar = " +
                        (mascotasDisponibles != null ? mascotasDisponibles.size() : "NULL"));

                if (mascotasDisponibles == null || mascotasDisponibles.isEmpty()) {
                    // Verificar si el usuario tiene mascotas pero todas tienen reportes activos
                    List<Mascotas> todasLasMascotas = mascotaDAO.ListarMascotasPorUsuario(usuarioId);

                    if (todasLasMascotas != null && !todasLasMascotas.isEmpty()) {
                        System.out.println("DEBUG: El usuario tiene mascotas pero todas están reportadas");
                        request.setAttribute("mensaje",
                                "Todas sus mascotas ya tienen reportes activos. " +
                                        "Para crear un nuevo reporte, primero debe marcar alguna mascota como 'En casa' " +
                                        "desde 'Mis Reportes'.");
                    } else {
                        System.out.println("DEBUG: El usuario no tiene mascotas registradas");
                        request.setAttribute("mensaje", "No tienes mascotas registradas.");
                    }
                }

                request.setAttribute("usuarioId", usuarioId);
                request.setAttribute("mascotasUsuario", mascotasDisponibles);
                request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/registrar_reportedesaparicion.jsp").forward(request, response);
                break;
            }

            case "editar": {
                System.out.println("=== DEBUG: Caso editar reporte ===");

                String reporteIdStr = request.getParameter("id");
                int reporteId = Integer.parseInt(reporteIdStr);
                System.out.println("DEBUG: Editando reporte ID = " + reporteId);

                // Buscar el reporte
                ReporteDesaparicion reporteAEditar = reporteDAO.buscarReporte(reporteId);

                System.out.println("DEBUG: Reporte encontrado - redirigiendo al formulario");

                // Pasar el reporte al JSP de edición
                request.setAttribute("reporte", reporteAEditar);
                request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/editar_reportedesaparicion.jsp").forward(request, response);
                break;
            }

            case "eliminar": {
                System.out.println("DEBUG: Caso eliminar reporte");

                try {
                    String idParam = request.getParameter("id");
                    System.out.println("DEBUG: Parámetro ID recibido = " + idParam);

                    if (idParam == null || idParam.trim().isEmpty()) {
                        System.out.println("ERROR: ID de reporte no proporcionado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=id_faltante");
                        return;
                    }

                    int idEliminar = Integer.parseInt(idParam);
                    System.out.println("DEBUG: ID a eliminar = " + idEliminar);

                    // Verificar que el reporte existe
                    ReporteDesaparicion reporteAEliminar = reporteDAO.buscarReporte(idEliminar);
                    if (reporteAEliminar == null || reporteAEliminar.getReporteID() == 0) {
                        System.out.println("ERROR: No se encontró el reporte con ID " + idEliminar);
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=reporte_no_encontrado");
                        return;
                    }

                    // Verificar si el usuario actual es el propietario del reporte
                    if (usuarioId != null && !usuarioId.equals(reporteAEliminar.getR_Usuario())) {
                        System.out.println("ERROR: Usuario " + usuarioId + " no es propietario del reporte");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=no_autorizado");
                        return;
                    }

                    System.out.println("DEBUG: Llamando a eliminarReporte...");
                    boolean resultado = reporteDAO.eliminarReporte(idEliminar);

                    if (resultado) {
                        System.out.println("DEBUG: Reporte eliminado exitosamente");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&mensaje=eliminado");
                    } else {
                        System.out.println("ERROR: Fallo al eliminar el reporte");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=eliminar_fallo");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: ID de reporte no válido - " + e.getMessage());
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=id_invalido");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception general en eliminar - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=general");
                }
                break;
            }

            case "marcar_encontrada": {
                System.out.println("DEBUG: Caso marcar_encontrada");

                try {
                    int reporteIdMarca = Integer.parseInt(request.getParameter("id"));
                    System.out.println("DEBUG: ID de reporte a marcar como encontrada = " + reporteIdMarca);

                    // Verificar que el reporte existe
                    ReporteDesaparicion reporteAActualizar = reporteDAO.buscarReporte(reporteIdMarca);
                    if (reporteAActualizar == null || reporteAActualizar.getReporteID() == 0) {
                        System.out.println("ERROR: Reporte no encontrado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=reporte_no_encontrado");
                        return;
                    }

                    // Verificar que el usuario actual es el propietario del reporte
                    if (usuarioId != null && !usuarioId.equals(reporteAActualizar.getR_Usuario())) {
                        System.out.println("ERROR: Usuario no autorizado para modificar este reporte");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=no_autorizado");
                        return;
                    }

                    // Verificar que el reporte no esté ya marcado como "En casa"
                    if ("En casa".equals(reporteAActualizar.getEstadoReporte())) {
                        System.out.println("DEBUG: El reporte ya está marcado como 'En casa'");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&mensaje=ya_en_casa");
                        return;
                    }

                    // Actualizar el estado del reporte a "En casa"
                    reporteAActualizar.setEstadoReporte("Cerrado");

                    boolean actualizacionExitosa = reporteDAO.modificarReporte(reporteAActualizar);

                    if (actualizacionExitosa) {
                        System.out.println("DEBUG: Reporte marcado como 'En casa' exitosamente");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&mensaje=marcada_en_casa");
                    } else {
                        System.out.println("ERROR: Fallo al marcar el reporte como 'En casa'");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=actualizar_fallo");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: ID de reporte inválido");
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=id_invalido");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception general en marcar_encontrada - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=general");
                }
                break;
            }

            case "mis_reportes": {
                System.out.println("DEBUG: Caso mis_reportes");

                // Verificar que hay un usuario logueado
                if (usuarioId == null) {
                    System.out.println("ERROR: No hay usuario logueado");
                    request.setAttribute("mensaje", "Error: Debe iniciar sesión para ver sus reportes.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }

                // Verificar mensajes adicionales
                String mensaje1 = request.getParameter("mensaje");
                if (mensaje1 != null) {
                    System.out.println("DEBUG: Mensaje recibido = " + mensaje1);
                    switch (mensaje1) {
                        case "eliminado":
                            request.setAttribute("mensaje", "¡Reporte eliminado exitosamente!");
                            break;
                        case "actualizado":
                            request.setAttribute("mensaje", "¡Reporte actualizado exitosamente!");
                            break;
                        case "registrado":
                            request.setAttribute("mensaje", "¡Reporte registrado exitosamente!");
                            break;
                        case "marcada_en_casa":
                            request.setAttribute("mensaje", "¡Mascota marcada como 'En casa' exitosamente! Ahora puede crear nuevos reportes para esta mascota si es necesario.");
                            break;
                        case "ya_en_casa":
                            request.setAttribute("mensaje", "Esta mascota ya está marcada como 'En casa'.");
                            break;
                    }
                }

                // Obtener reportes del usuario actual
                List<ReporteConRelaciones> misReportes = reporteDAO.listarReportesPorUsuario(usuarioId);
                System.out.println("DEBUG: Reportes del usuario " + usuarioId + " = " + (misReportes != null ? misReportes.size() : "NULL"));

                request.setAttribute("misReportes", misReportes);
                request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/mis_reportes.jsp").forward(request, response);
                break;
            }

            default: {
                System.out.println("DEBUG: Caso default");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG ReporteDesaparicionServlet POST: Accion = " + accion + " ===");

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        switch (accion) {
            case "registrar": {
                System.out.println("DEBUG: Registrando nuevo reporte");

                try {
                    // Verificar que hay un usuario logueado
                    if (usuarioId == null) {
                        System.out.println("ERROR: No hay usuario logueado");
                        response.sendRedirect("index.jsp");
                        return;
                    }

                    int mascotaId = Integer.parseInt(request.getParameter("mascota_id"));
                    System.out.println("DEBUG: Mascota ID a reportar = " + mascotaId);

                    // VALIDACIÓN: Verificar que la mascota no tenga ya un reporte activo
                    if (reporteDAO.existeReporteActivoPorMascota(mascotaId)) {
                        System.out.println("ERROR: La mascota ya tiene un reporte activo");

                        // Obtener información del reporte existente
                        ReporteDesaparicion reporteExistente = reporteDAO.obtenerReporteActivoPorMascota(mascotaId);

                        // Volver al formulario con error
                        List<Mascotas> mascotasDisponibles = mascotaDAO.ListarMascotasSinReporteActivo(usuarioId);
                        request.setAttribute("usuarioId", usuarioId);
                        request.setAttribute("mascotasUsuario", mascotasDisponibles);
                        request.setAttribute("mensaje",
                                "Error: Esta mascota ya tiene un reporte activo desde el " +
                                        new SimpleDateFormat("dd/MM/yyyy").format(reporteExistente.getFecha_Registro()) +
                                        ". Para crear un nuevo reporte, primero debe marcar la mascota como 'En casa' " +
                                        "desde 'Mis Reportes'.");
                        request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/registrar_reportedesaparicion.jsp").forward(request, response);
                        return;
                    }

                    // VALIDACIÓN: Verificar que la mascota pertenece al usuario logueado
                    Mascotas mascotaAReportar = mascotaDAO.BuscarMascota(mascotaId);
                    if (mascotaAReportar == null || mascotaAReportar.getR_Usuario() != usuarioId.intValue()) {
                        System.out.println("ERROR: La mascota no pertenece al usuario o no existe");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=registrar&error=mascota_no_autorizada");
                        return;
                    }

                    // Continuar con el registro normal del reporte
                    ReporteDesaparicion nuevoReporte = new ReporteDesaparicion();
                    nuevoReporte.setR_Usuario(usuarioId);
                    nuevoReporte.setR_Mascota(mascotaId);
                    nuevoReporte.setFechaDesaparicion(java.sql.Date.valueOf(request.getParameter("fecha_desaparicion")));
                    nuevoReporte.setUbicacionUltimaVez(request.getParameter("ubicacion_ultima_vez"));
                    nuevoReporte.setDescripcionSituacion(request.getParameter("descripcion_situacion"));

                    // Manejar recompensa
                    String recompensaStr = request.getParameter("recompensa");
                    if (recompensaStr != null && !recompensaStr.trim().isEmpty()) {
                        nuevoReporte.setRecompensa(Double.parseDouble(recompensaStr));
                    } else {
                        nuevoReporte.setRecompensa(0.0);
                    }

                    nuevoReporte.setEstadoReporte("Activo"); // Establecer estado inicial
                    nuevoReporte.setFecha_Registro(new java.sql.Date(System.currentTimeMillis()));
                    nuevoReporte.setEstatus("Alta");

                    System.out.println("DEBUG: Datos del reporte - Mascota: " + nuevoReporte.getR_Mascota() +
                            ", Usuario: " + nuevoReporte.getR_Usuario() +
                            ", Estado: " + nuevoReporte.getEstadoReporte());

                    boolean registroExitoso = reporteDAO.registrarReporte(nuevoReporte);

                    if (registroExitoso) {
                        System.out.println("DEBUG: Reporte registrado exitosamente");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&mensaje=registrado");
                    } else {
                        System.out.println("ERROR: Fallo al registrar el reporte");
                        // Volver al formulario con error
                        List<Mascotas> mascotasDisponibles = mascotaDAO.ListarMascotasSinReporteActivo(usuarioId);
                        request.setAttribute("usuarioId", usuarioId);
                        request.setAttribute("mascotasUsuario", mascotasDisponibles);
                        request.setAttribute("mensaje", "Error al registrar el reporte. Inténtelo nuevamente.");
                        request.getRequestDispatcher("Vistas_JSP/ReporteDesaparicion/registrar_reportedesaparicion.jsp").forward(request, response);
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: Exception en registrar reporte - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=registrar&error=general");
                }
                break;
            }

            case "actualizar": {
                System.out.println("=== DEBUG: Caso actualizar reporte ===");

                try {
                    // Obtener datos del formulario
                    int reporteIdAct = Integer.parseInt(request.getParameter("reporteid"));
                    int mascotaIdAct = Integer.parseInt(request.getParameter("r_mascota"));
                    String fechaDesaparicionStr = request.getParameter("fecha_desaparicion");
                    String ubicacionUltimaVez = request.getParameter("ubicacionultimavez");
                    String descripcionSituacion = request.getParameter("descripcionsituacion");
                    double recompensa = Double.parseDouble(request.getParameter("recompensa"));

                    System.out.println("DEBUG: Actualizando reporte ID = " + reporteIdAct);
                    System.out.println("DEBUG: Datos recibidos - Mascota: " + mascotaIdAct + ", Ubicación: " + ubicacionUltimaVez);

                    // Verificar que el reporte existe y pertenece al usuario
                    ReporteDesaparicion reporteExistente = reporteDAO.buscarReporte(reporteIdAct);
                    if (reporteExistente == null || reporteExistente.getR_Usuario() != usuarioId) {
                        System.out.println("ERROR: Reporte no encontrado o sin permisos");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=sin_permisos");
                        return;
                    }

                    // Crear objeto con los datos actualizados
                    ReporteDesaparicion reporteActualizado = new ReporteDesaparicion();
                    reporteActualizado.setReporteID(reporteIdAct);
                    reporteActualizado.setR_Mascota(mascotaIdAct);
                    reporteActualizado.setR_Usuario(usuarioId); // Mantener el usuario original

                    // Convertir fecha
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    reporteActualizado.setFechaDesaparicion(sdf.parse(fechaDesaparicionStr));

                    reporteActualizado.setUbicacionUltimaVez(ubicacionUltimaVez);
                    reporteActualizado.setDescripcionSituacion(descripcionSituacion);
                    reporteActualizado.setRecompensa(recompensa);
                    reporteActualizado.setEstadoReporte("Activo");
                    reporteActualizado.setFecha_Registro(reporteExistente.getFecha_Registro());
                    reporteActualizado.setEstatus("Alta");

                    // Actualizar en la base de datos
                    boolean exito = reporteDAO.modificarReporte(reporteActualizado);

                    if (exito) {
                        System.out.println("DEBUG: Reporte actualizado exitosamente");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&mensaje=actualizado");
                    } else {
                        System.out.println("ERROR: Fallo al actualizar reporte");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=editar&id=" + reporteIdAct + "&error=fallo_actualizacion");
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: Exception en actualizar reporte - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=error_inesperado");
                }
                break;
            }

            case "cambiar_estado": {
                System.out.println("=== DEBUG: Caso cambiar_estado ===");

                if (usuarioId == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                try {
                    String reporteIdStr = request.getParameter("reporteId");
                    String nuevoEstado = request.getParameter("nuevoEstado");

                    System.out.println("DEBUG: Cambiando estado - ReporteID=" + reporteIdStr + ", NuevoEstado=" + nuevoEstado);

                    if (reporteIdStr == null || nuevoEstado == null || reporteIdStr.trim().isEmpty() || nuevoEstado.trim().isEmpty()) {
                        session.setAttribute("error", "Datos incompletos para cambiar estado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes");
                        return;
                    }

                    int reporteId = Integer.parseInt(reporteIdStr);

                    // Verificar que el reporte existe y pertenece al usuario
                    ReporteDesaparicion reporteExistente = reporteDAO.buscarReporte(reporteId);
                    if (reporteExistente == null || !usuarioId.equals(reporteExistente.getR_Usuario())) {
                        session.setAttribute("error", "No tienes permisos para modificar este reporte");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes");
                        return;
                    }

                    // Actualizar el estado
                    reporteExistente.setEstadoReporte(nuevoEstado);
                    boolean actualizado = reporteDAO.modificarReporte(reporteExistente);

                    if (actualizado) {
                        String mensajeEstado = "";
                        switch (nuevoEstado) {
                            case "Encontrado":
                                mensajeEstado = "¡Excelente! Mascota marcada como encontrada";
                                break;
                            case "Activo":
                                mensajeEstado = "Búsqueda reactivada exitosamente";
                                break;
                            case "Pausado":
                                mensajeEstado = "Búsqueda pausada temporalmente";
                                break;
                            case "Cerrado":
                                mensajeEstado = "Reporte cerrado exitosamente";
                                break;
                        }
                        session.setAttribute("mensaje", mensajeEstado);
                        System.out.println("DEBUG: Estado actualizado exitosamente");
                    } else {
                        session.setAttribute("error", "Error al actualizar el estado del reporte");
                        System.out.println("ERROR: Fallo al actualizar estado");
                    }

                    response.sendRedirect("ReporteDesaparicionServlet?accion=detalle_mi_reporte&id=" + reporteId);

                } catch (NumberFormatException e) {
                    session.setAttribute("error", "ID de reporte inválido");
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception en cambiar_estado - " + e.getMessage());
                    e.printStackTrace();
                    session.setAttribute("error", "Error interno del servidor");
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes");
                }
                break;
            }

            default: {
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                break;
            }
        }
    }
}