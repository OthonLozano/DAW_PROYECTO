package Servlet;

import Modelo.DAO.ReporteDesaparicionDAO;
import Modelo.DAO.MascotasDAO;
import Modelo.JavaBeans.ReporteDesaparicion;
import Modelo.JavaBeans.ReporteConRelaciones;
import Modelo.JavaBeans.Mascotas;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "ReporteDesaparicionServlet", urlPatterns = {"/ReporteDesaparicionServlet"})
public class ReporteDesaparicionServlet extends HttpServlet {

    ReporteDesaparicionDAO reporteDAO = new ReporteDesaparicionDAO();
    MascotasDAO mascotaDAO = new MascotasDAO();

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
            case "listar":
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

                // Obtener todos los reportes activos para mostrar públicamente
                List<ReporteConRelaciones> listaReportes = reporteDAO.listarReportesCompletos();
                System.out.println("DEBUG: Total reportes obtenidos = " + (listaReportes != null ? listaReportes.size() : "NULL"));

                request.setAttribute("reportesCompletos", listaReportes);
                request.getRequestDispatcher("Vistas_JSP/Reportes/listar_reportes.jsp").forward(request, response);
                break;

            case "mis_reportes":
                System.out.println("DEBUG: Caso mis_reportes");

                // Verificar que hay un usuario logueado
                if (usuarioId == null) {
                    System.out.println("ERROR: No hay usuario logueado");
                    request.setAttribute("mensaje", "Error: Debe iniciar sesión para ver sus reportes.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }

                // Obtener reportes del usuario actual
                List<ReporteConRelaciones> misReportes = reporteDAO.listarReportesPorUsuario(usuarioId);
                System.out.println("DEBUG: Reportes del usuario " + usuarioId + " = " + (misReportes != null ? misReportes.size() : "NULL"));

                request.setAttribute("misReportes", misReportes);
                request.getRequestDispatcher("Vistas_JSP/Reportes/mis_reportes.jsp").forward(request, response);
                break;

            case "registrar":
                System.out.println("DEBUG: Caso registrar reporte");

                // Verificar que hay un usuario logueado
                if (usuarioId == null) {
                    System.out.println("ERROR: No hay usuario logueado");
                    request.setAttribute("mensaje", "Error: Debe iniciar sesión para registrar un reporte.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }

                // Obtener las mascotas del usuario para el combobox
                List<Mascotas> mascotasUsuario = mascotaDAO.ListarMascotasPorUsuario(usuarioId);
                System.out.println("DEBUG: Mascotas del usuario para reporte = " + (mascotasUsuario != null ? mascotasUsuario.size() : "NULL"));

                request.setAttribute("usuarioId", usuarioId);
                request.setAttribute("mascotasUsuario", mascotasUsuario);
                request.getRequestDispatcher("Vistas_JSP/Reportes/registrar_reporte.jsp").forward(request, response);
                break;

            case "editar":
                System.out.println("DEBUG: Caso editar reporte");

                try {
                    int reporteId = Integer.parseInt(request.getParameter("id"));
                    System.out.println("DEBUG: ID de reporte a editar = " + reporteId);

                    ReporteDesaparicion reporte = reporteDAO.buscarReporte(reporteId);
                    if (reporte == null || reporte.getReporteID() == 0) {
                        System.out.println("ERROR: Reporte no encontrado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=reporte_no_encontrado");
                        return;
                    }

                    // Verificar que el usuario actual es el propietario del reporte
                    if (usuarioId != null && !usuarioId.equals(reporte.getR_Usuario())) {
                        System.out.println("ERROR: Usuario no autorizado para editar este reporte");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=no_autorizado");
                        return;
                    }

                    // Obtener las mascotas del usuario para el combobox
                    List<Mascotas> mascotasUsuario2 = mascotaDAO.ListarMascotasPorUsuario(usuarioId);

                    request.setAttribute("reporte", reporte);
                    request.setAttribute("mascotasUsuario", mascotasUsuario2);
                    request.getRequestDispatcher("Vistas_JSP/Reportes/editar_reporte.jsp").forward(request, response);

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: ID de reporte inválido");
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=id_invalido");
                }
                break;

            case "eliminar":
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

            default:
                System.out.println("DEBUG: Caso default");
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                break;
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
            case "registrar":
                System.out.println("DEBUG: Registrando nuevo reporte");

                try {
                    // Verificar que hay un usuario logueado
                    if (usuarioId == null) {
                        System.out.println("ERROR: No hay usuario logueado");
                        response.sendRedirect("index.jsp");
                        return;
                    }

                    ReporteDesaparicion nuevoReporte = new ReporteDesaparicion();
                    nuevoReporte.setR_Usuario(usuarioId);
                    nuevoReporte.setR_Mascota(Integer.parseInt(request.getParameter("mascota_id")));
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

                    nuevoReporte.setEstadoReporte(request.getParameter("estado_reporte"));
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
                        List<Mascotas> mascotasUsuario = mascotaDAO.ListarMascotasPorUsuario(usuarioId);
                        request.setAttribute("usuarioId", usuarioId);
                        request.setAttribute("mascotasUsuario", mascotasUsuario);
                        request.setAttribute("mensaje", "Error al registrar el reporte. Inténtelo nuevamente.");
                        request.getRequestDispatcher("Vistas_JSP/Reportes/registrar_reporte.jsp").forward(request, response);
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: Exception en registrar reporte - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=registrar&error=general");
                }
                break;

            case "actualizar":
                System.out.println("DEBUG: Actualizando reporte");

                try {
                    int reporteId = Integer.parseInt(request.getParameter("reporte_id"));
                    System.out.println("DEBUG: ID de reporte a actualizar = " + reporteId);

                    // Verificar que el reporte existe y pertenece al usuario
                    ReporteDesaparicion reporteExistente = reporteDAO.buscarReporte(reporteId);
                    if (reporteExistente == null || !usuarioId.equals(reporteExistente.getR_Usuario())) {
                        System.out.println("ERROR: Reporte no encontrado o no autorizado");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=no_autorizado");
                        return;
                    }

                    // Actualizar los datos
                    reporteExistente.setR_Mascota(Integer.parseInt(request.getParameter("mascota_id")));
                    reporteExistente.setFechaDesaparicion(java.sql.Date.valueOf(request.getParameter("fecha_desaparicion")));
                    reporteExistente.setUbicacionUltimaVez(request.getParameter("ubicacion_ultima_vez"));
                    reporteExistente.setDescripcionSituacion(request.getParameter("descripcion_situacion"));

                    String recompensaStr = request.getParameter("recompensa");
                    if (recompensaStr != null && !recompensaStr.trim().isEmpty()) {
                        reporteExistente.setRecompensa(Double.parseDouble(recompensaStr));
                    } else {
                        reporteExistente.setRecompensa(0.0);
                    }

                    reporteExistente.setEstadoReporte(request.getParameter("estado_reporte"));

                    boolean actualizacionExitosa = reporteDAO.modificarReporte(reporteExistente);

                    if (actualizacionExitosa) {
                        System.out.println("DEBUG: Reporte actualizado exitosamente");
                        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&mensaje=actualizado");
                    } else {
                        System.out.println("ERROR: Fallo al actualizar el reporte");
                        // Volver al formulario con error
                        List<Mascotas> mascotasUsuario = mascotaDAO.ListarMascotasPorUsuario(usuarioId);
                        request.setAttribute("reporte", reporteExistente);
                        request.setAttribute("mascotasUsuario", mascotasUsuario);
                        request.setAttribute("mensaje", "Error al actualizar el reporte. Inténtelo nuevamente.");
                        request.getRequestDispatcher("Vistas_JSP/Reportes/editar_reporte.jsp").forward(request, response);
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: Exception en actualizar reporte - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes&error=general");
                }
                break;

            default:
                response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
                break;
        }
    }
}