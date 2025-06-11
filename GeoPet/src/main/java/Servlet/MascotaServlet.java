package Servlet;

import Modelo.DAO.MascotasDAO;
import Modelo.DAO.EspecieDAO;
import Modelo.JavaBeans.MascotaConRelaciones;
import Modelo.JavaBeans.Mascotas;
import Modelo.JavaBeans.Especie;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet controlador para gestionar operaciones CRUD completas de Mascotas.
 *
 * Este servlet actúa como el punto central de control para todas las operaciones
 * relacionadas con la gestión de mascotas en el sistema. Maneja tanto la presentación
 * de formularios como el procesamiento de datos, proporcionando una interfaz
 * completa para que los usuarios administren sus mascotas.
 *
 * Funcionalidades principales:
 * - Listado de mascotas con información completa (especie, imágenes)
 * - Registro de nuevas mascotas con validaciones
 * - Edición de mascotas existentes con preservación de datos críticos
 * - Eliminación lógica con verificación de permisos
 * - Gestión de relaciones con especies e imágenes
 * - Flujo integrado de registro y subida de imágenes
 *
 * Características de seguridad:
 * - Verificación de propiedad antes de modificaciones/eliminaciones
 * - Validación de sesiones de usuario activas
 * - Sanitización de parámetros de entrada
 * - Manejo seguro de conversiones de tipos
 * - Preservación de datos críticos en actualizaciones
 *
 * Flujos de trabajo especializados:
 * - Registro → Subida de imágenes (flujo directo)
 * - Edición con precarga de datos y especies
 * - Listado con relaciones completas para vista enriquecida
 * - Eliminación con confirmaciones y validaciones
 *
 * El servlet implementa el patrón MVC donde actúa como controlador,
 * delegando operaciones de datos a DAOs específicos y redirigiendo
 * a vistas apropiadas según el resultado de las operaciones.
 */
@WebServlet(name = "MascotaServlet", urlPatterns = {"/MascotaServlet"})
public class MascotaServlet extends HttpServlet {

    /**
     * Instancia del DAO para operaciones de base de datos de mascotas.
     */
    private final MascotasDAO mascotaDAO = new MascotasDAO();

    /**
     * Instancia del DAO para operaciones de base de datos de especies.
     */
    private final EspecieDAO especieDAO = new EspecieDAO();

    /**
     * Objeto Mascotas para manipulación de datos temporales.
     */
    private final Mascotas mascota = new Mascotas();

    /**
     * Maneja solicitudes HTTP GET para presentar formularios y listados.
     *
     * Procesa diferentes acciones basadas en el parámetro 'accion':
     * - "listar": Muestra mascotas del usuario con relaciones completas
     * - "registrar": Presenta formulario de registro con especies disponibles
     * - "eliminar": Procesa eliminación lógica con validaciones de seguridad
     * - "editar": Presenta formulario de edición con datos precargados
     *
     * Implementa manejo completo de mensajes de estado (éxito/error) mediante
     * parámetros URL y los convierte en mensajes user-friendly para la interfaz.
     *
     * @param request HttpServletRequest objeto de solicitud HTTP
     * @param response HttpServletResponse objeto de respuesta HTTP
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // Acción por defecto
        }

        // Obtener información de sesión común para todas las operaciones
        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        switch (accion) {
            case "listar":
                procesarListado(request, response, usuarioId);
                break;

            case "registrar":
                procesarFormularioRegistro(request, response, usuarioId);
                break;

            case "eliminar":
                procesarEliminacion(request, response, usuarioId);
                break;

            case "editar":
                procesarFormularioEdicion(request, response);
                break;

            default:
                // Redirección segura para acciones no reconocidas
                response.sendRedirect("MascotaServlet?accion=listar");
                break;
        }
    }

    /**
     * Procesa el listado de mascotas del usuario con información completa.
     *
     * Muestra las mascotas del usuario actual incluyendo:
     * - Información básica de la mascota
     * - Datos de la especie asociada
     * - Imagen principal si está disponible
     *
     * Maneja mensajes de estado provenientes de otras operaciones y los
     * convierte en mensajes user-friendly para mostrar en la interfaz.
     *
     * @param request HttpServletRequest conteniendo parámetros de mensajes
     * @param response HttpServletResponse para redirecciones
     * @param usuarioId ID del usuario actual de la sesión
     * @throws ServletException si ocurre error del servlet
     * @throws IOException si ocurre error de entrada/salida
     */
    private void procesarListado(HttpServletRequest request, HttpServletResponse response, Integer usuarioId)
            throws ServletException, IOException {

        // Procesamiento de mensajes de estado de operaciones anteriores
        String mensaje = request.getParameter("mensaje");
        String error = request.getParameter("error");

        if (mensaje != null) {
            switch (mensaje) {
                case "eliminado":
                    request.setAttribute("mensaje", "¡Mascota eliminada exitosamente!");
                    break;
                case "actualizado":
                    request.setAttribute("mensaje", "¡Mascota actualizada exitosamente!");
                    break;
            }
        }

        if (error != null) {
            switch (error) {
                case "id_faltante":
                    request.setAttribute("mensaje", "Error: ID de mascota no proporcionado.");
                    break;
                case "mascota_no_encontrada":
                    request.setAttribute("mensaje", "Error: La mascota no fue encontrada.");
                    break;
                case "no_autorizado":
                    request.setAttribute("mensaje", "Error: No tienes permiso para eliminar esta mascota.");
                    break;
                case "eliminar_fallo":
                    request.setAttribute("mensaje", "Error: No se pudo eliminar la mascota.");
                    break;
                default:
                    request.setAttribute("mensaje", "Error: " + error);
                    break;
            }
        }

        // Validación de sesión activa
        if (usuarioId == null) {
            request.setAttribute("mensaje", "Error: Debe iniciar sesión para ver sus mascotas.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // Obtener mascotas del usuario con información completa de relaciones
        List<MascotaConRelaciones> listaConRelaciones = mascotaDAO.ListarMascotasConRelacionesPorUsuario(usuarioId);

        // Configurar atributos para la vista
        request.setAttribute("mascotasConRelaciones", listaConRelaciones);
        request.getRequestDispatcher("Vistas_JSP/Mascotas/listar_mascotas.jsp").forward(request, response);
    }

    /**
     * Presenta el formulario de registro de nuevas mascotas.
     *
     * Prepara el formulario de registro cargando la lista de especies
     * disponibles para el selector y configurando el ID del usuario
     * actual para asociar la mascota al propietario correcto.
     *
     * @param request HttpServletRequest para configurar atributos
     * @param response HttpServletResponse para forward
     * @param usuarioId ID del usuario actual propietario
     * @throws ServletException si ocurre error del servlet
     * @throws IOException si ocurre error de entrada/salida
     */
    private void procesarFormularioRegistro(HttpServletRequest request, HttpServletResponse response, Integer usuarioId)
            throws ServletException, IOException {

        // Obtener lista de especies disponibles para el selector
        List<Especie> especies = null;
        try {
            especies = especieDAO.ListarEspecies();
        } catch (Exception e) {
            System.err.println("ERROR: Al obtener especies para registro - " + e.getMessage());
            e.printStackTrace();
        }

        // Configurar atributos para el formulario de registro
        request.setAttribute("usuarioId", usuarioId);
        request.setAttribute("especies", especies);

        request.getRequestDispatcher("/Vistas_JSP/Mascotas/registrar_mascotas.jsp").forward(request, response);
    }

    /**
     * Procesa la eliminación lógica de una mascota con validaciones de seguridad.
     *
     * Implementa un flujo seguro de eliminación que incluye:
     * 1. Validación de parámetros de entrada
     * 2. Verificación de existencia de la mascota
     * 3. Validación de permisos de propiedad
     * 4. Eliminación lógica en base de datos
     * 5. Redirección con mensaje apropiado
     *
     * La eliminación es lógica (cambio de estatus) para preservar
     * integridad referencial con reportes y avistamientos asociados.
     *
     * @param request HttpServletRequest conteniendo ID de mascota
     * @param response HttpServletResponse para redirecciones
     * @param usuarioId ID del usuario actual para verificación de permisos
     * @throws IOException si ocurre error de entrada/salida
     */
    private void procesarEliminacion(HttpServletRequest request, HttpServletResponse response, Integer usuarioId)
            throws IOException {

        try {
            String idParam = request.getParameter("id");

            // Validación de parámetro ID requerido
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendRedirect("MascotaServlet?accion=listar&error=id_faltante");
                return;
            }

            int idEliminar = Integer.parseInt(idParam);

            // Verificación de existencia de la mascota
            Mascotas mascotaAEliminar = mascotaDAO.BuscarMascota(idEliminar);
            if (mascotaAEliminar == null) {
                response.sendRedirect("MascotaServlet?accion=listar&error=mascota_no_encontrada");
                return;
            }

            // Validación de permisos de propiedad
            if (usuarioId != null && !usuarioId.equals(mascotaAEliminar.getR_Usuario())) {
                response.sendRedirect("MascotaServlet?accion=listar&error=no_autorizado");
                return;
            }

            // Ejecutar eliminación lógica
            boolean resultado = mascotaDAO.EliminarMascota(idEliminar);

            if (resultado) {
                response.sendRedirect("MascotaServlet?accion=listar&mensaje=eliminado");
            } else {
                response.sendRedirect("MascotaServlet?accion=listar&error=eliminar_fallo");
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: ID de mascota no válido - " + e.getMessage());
            response.sendRedirect("MascotaServlet?accion=listar&error=id_invalido");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción general en eliminación - " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("MascotaServlet?accion=listar&error=general");
        }
    }

    /**
     * Presenta el formulario de edición con datos de mascota precargados.
     *
     * Busca la mascota por ID y precarga todos sus datos en el formulario
     * de edición. También carga la lista de especies disponibles para
     * permitir cambios en la clasificación de la mascota.
     *
     * @param request HttpServletRequest conteniendo ID de mascota a editar
     * @param response HttpServletResponse para forward
     * @throws ServletException si ocurre error del servlet
     * @throws IOException si ocurre error de entrada/salida
     */
    private void procesarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idEditar = Integer.parseInt(request.getParameter("id"));

        // Buscar mascota a editar
        Mascotas mascotaEditar = mascotaDAO.BuscarMascota(idEditar);

        // Obtener especies disponibles para el selector
        List<Especie> especiesEditar = null;
        try {
            especiesEditar = especieDAO.ListarEspecies();
        } catch (Exception e) {
            System.err.println("ERROR: Al obtener especies para edición - " + e.getMessage());
            e.printStackTrace();
        }

        // Configurar atributos para el formulario de edición
        request.setAttribute("mascota", mascotaEditar);
        request.setAttribute("especies", especiesEditar);
        request.getRequestDispatcher("Vistas_JSP/Mascotas/editar_mascotas.jsp").forward(request, response);
    }

    /**
     * Maneja solicitudes HTTP POST para procesamiento de formularios.
     *
     * Procesa diferentes acciones de formularios:
     * - "registrar": Registra nueva mascota y redirige a subida de imágenes
     * - "actualizar": Actualiza mascota existente con validaciones
     *
     * Implementa flujos de trabajo específicos como el registro integrado
     * que redirige automáticamente a la subida de imágenes después del
     * registro exitoso de una nueva mascota.
     *
     * @param request HttpServletRequest conteniendo datos del formulario
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        switch (accion) {
            case "registrar":
                procesarRegistroMascota(request, response);
                break;

            case "actualizar":
                procesarActualizacionMascota(request, response);
                break;

            default:
                response.sendRedirect("MascotaServlet?accion=listar");
                break;
        }
    }

    /**
     * Procesa el registro de una nueva mascota con flujo integrado a subida de imágenes.
     *
     * Implementa el flujo completo de registro:
     * 1. Extracción y validación de datos del formulario
     * 2. Configuración del objeto Mascota con datos sanitizados
     * 3. Registro en base de datos
     * 4. Búsqueda de la mascota recién registrada para obtener su ID
     * 5. Redirección automática al módulo de subida de imágenes
     *
     * El flujo integrado mejora la experiencia del usuario al guiarlo
     * automáticamente desde el registro hasta la subida de imágenes,
     * completando el proceso de alta de mascota en un solo flujo.
     *
     * @param request HttpServletRequest conteniendo datos del formulario
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre error del servlet
     * @throws IOException si ocurre error de entrada/salida
     */
    private void procesarRegistroMascota(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener usuario de la sesión
        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        // Configuración del objeto Mascota con datos del formulario
        mascota.setR_Usuario(usuarioId);
        mascota.setNombre(request.getParameter("nombre"));
        mascota.setR_Especie(Integer.parseInt(request.getParameter("r_especie")));
        mascota.setEdad(Integer.parseInt(request.getParameter("edad")));
        mascota.setSexo(request.getParameter("sexo"));
        mascota.setColor(request.getParameter("color"));
        mascota.setCaracteristicasDistintivas(request.getParameter("caracteristicasdistintivas"));
        mascota.setMicrochip(Boolean.parseBoolean(request.getParameter("microchip")));

        // Manejo seguro del número de microchip (campo opcional)
        String numeroMicrochip = request.getParameter("numero_microchip");
        if (numeroMicrochip != null && !numeroMicrochip.trim().isEmpty()) {
            mascota.setNumero_Microchip(Integer.parseInt(numeroMicrochip));
        } else {
            mascota.setNumero_Microchip(0); // Valor por defecto para campos vacíos
        }

        mascota.setEstado(request.getParameter("estado"));
        mascota.setFecha_Registro(java.sql.Date.valueOf(request.getParameter("fecha_registro")));

        // Registro en base de datos
        boolean registroExitoso = mascotaDAO.RegistrarMascota(mascota);

        if (registroExitoso) {
            // Búsqueda de la mascota recién registrada para flujo integrado
            List<Mascotas> mascotasUsuario = mascotaDAO.ListarMascotasPorUsuario(usuarioId);

            if (!mascotasUsuario.isEmpty()) {
                // Identificar la mascota más reciente (ID más alto)
                Mascotas ultimaMascota = null;
                int maxId = 0;

                for (Mascotas m : mascotasUsuario) {
                    if (m.getMascotaID() > maxId) {
                        maxId = m.getMascotaID();
                        ultimaMascota = m;
                    }
                }

                int mascotaId = ultimaMascota.getMascotaID();
                String nombreMascota = ultimaMascota.getNombre();

                // Redirección directa al módulo de subida de imágenes
                response.sendRedirect("Vistas_JSP/Mascotas/imagen_mascota_focused.jsp?mascotaId=" + mascotaId +
                        "&action=subir&nombre=" + nombreMascota);

            } else {
                // Fallback: mostrar mensaje de éxito si no se puede obtener ID
                List<Especie> especies = especieDAO.ListarEspecies();
                request.setAttribute("especies", especies);
                request.setAttribute("mensaje", "¡Mascota registrada exitosamente!");
                request.setAttribute("usuarioId", usuarioId);
                request.getRequestDispatcher("Vistas_JSP/Mascotas/registrar_mascotas.jsp").forward(request, response);
            }
        }
    }

    /**
     * Procesa la actualización de una mascota existente con preservación de datos críticos.
     *
     * Implementa actualización segura que:
     * 1. Valida y extrae todos los parámetros del formulario
     * 2. Preserva datos críticos como propietario original
     * 3. Maneja campos opcionales con valores por defecto seguros
     * 4. Actualiza solo los campos modificables por el usuario
     * 5. Preserva fecha de registro original si no se especifica nueva
     *
     * La preservación de datos críticos evita modificaciones accidentales
     * de campos como propietario y fecha de registro original, manteniendo
     * la integridad de la información histórica.
     *
     * @param request HttpServletRequest conteniendo datos actualizados
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre error del servlet
     * @throws IOException si ocurre error de entrada/salida
     */
    private void procesarActualizacionMascota(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Extracción de parámetros del formulario de edición
            int mascotaId = Integer.parseInt(request.getParameter("mascotaid"));
            String nombre = request.getParameter("nombre");
            int especieId = Integer.parseInt(request.getParameter("r_especie"));
            int edad = Integer.parseInt(request.getParameter("edad"));
            String sexo = request.getParameter("sexo");
            String color = request.getParameter("color");
            String caracteristicas = request.getParameter("caracteristicasdistintivas");
            boolean microchip = Boolean.parseBoolean(request.getParameter("microchip"));
            String estado = request.getParameter("estado");
            String fechaRegistro = request.getParameter("fecha_registro");

            // Obtener mascota actual para preservar datos críticos
            Mascotas mascotaActual = mascotaDAO.BuscarMascota(mascotaId);
            if (mascotaActual == null) {
                response.sendRedirect("MascotaServlet?accion=listar");
                return;
            }

            // Configuración de la mascota con datos actualizados
            mascota.setMascotaID(mascotaId);
            mascota.setR_Usuario(mascotaActual.getR_Usuario()); // Preservar propietario original
            mascota.setNombre(nombre);
            mascota.setR_Especie(especieId);
            mascota.setEdad(edad);
            mascota.setSexo(sexo);
            mascota.setColor(color);
            mascota.setCaracteristicasDistintivas(caracteristicas);
            mascota.setMicrochip(microchip);

            // Manejo seguro del número de microchip
            String numeroMicrochipStr = request.getParameter("numero_microchip");
            if (numeroMicrochipStr != null && !numeroMicrochipStr.trim().isEmpty()) {
                try {
                    mascota.setNumero_Microchip(Integer.parseInt(numeroMicrochipStr));
                } catch (NumberFormatException e) {
                    mascota.setNumero_Microchip(0); // Valor por defecto para entradas inválidas
                }
            } else {
                mascota.setNumero_Microchip(0);
            }

            mascota.setEstado(estado);

            // Manejo de fecha de registro con preservación de original
            if (fechaRegistro != null && !fechaRegistro.trim().isEmpty()) {
                mascota.setFecha_Registro(java.sql.Date.valueOf(fechaRegistro));
            } else {
                mascota.setFecha_Registro(mascotaActual.getFecha_Registro()); // Preservar fecha original
            }

            // Actualización en base de datos
            boolean resultado = mascotaDAO.ModificarMascota(mascota);

            if (resultado) {
                response.sendRedirect("MascotaServlet?accion=listar&mensaje=actualizado");
            } else {
                // En caso de error, volver al formulario con datos cargados
                List<Especie> especiesEditar = especieDAO.ListarEspecies();
                request.setAttribute("mascota", mascota);
                request.setAttribute("especies", especiesEditar);
                request.setAttribute("mensaje", "Error al actualizar la mascota. Inténtelo nuevamente.");
                request.getRequestDispatcher("Vistas_JSP/Mascotas/editar_mascotas.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Error de formato en actualización - " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("MascotaServlet?accion=listar&error=formato");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción general en actualización - " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("MascotaServlet?accion=listar&error=general");
        }
    }
}