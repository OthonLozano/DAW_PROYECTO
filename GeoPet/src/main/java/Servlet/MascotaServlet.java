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
import java.sql.Connection;
import java.util.List;
import Connection.Conexion;

@WebServlet(name = "MascotaServlet", urlPatterns = {"/MascotaServlet"})
public class MascotaServlet extends HttpServlet {

    MascotasDAO mascotaDAO = new MascotasDAO();
    EspecieDAO especieDAO = new EspecieDAO();
    Mascotas mascota = new Mascotas();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG MascotaServlet: Accion recibida = " + accion + " ===");

        if (accion == null) {
            accion = "listar";
        }

        // Declarar variables comunes una sola vez
        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        switch (accion) {
            case "listar":
                System.out.println("DEBUG: Caso listar - mascotas con relaciones");

                // Verificar si viene algún mensaje de otra operación
                String mensaje = request.getParameter("mensaje");
                String error = request.getParameter("error");

                if (mensaje != null) {
                    System.out.println("DEBUG: Mensaje recibido = " + mensaje);
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
                    System.out.println("DEBUG: Error recibido = " + error);
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

                // Verificar que hay un usuario logueado
                if (usuarioId == null) {
                    System.out.println("ERROR: No hay usuario logueado en la sesión");
                    request.setAttribute("mensaje", "Error: Debe iniciar sesión para ver sus mascotas.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    return;
                }

                System.out.println("DEBUG: Listando mascotas con relaciones para usuario ID = " + usuarioId);

                // Obtener las mascotas con relaciones del usuario actual
                List<MascotaConRelaciones> listaConRelaciones = mascotaDAO.ListarMascotasConRelacionesPorUsuario(usuarioId);
                System.out.println("DEBUG: Total de mascotas con relaciones obtenidas = " + (listaConRelaciones != null ? listaConRelaciones.size() : "NULL"));

                if (listaConRelaciones != null && !listaConRelaciones.isEmpty()) {
                    System.out.println("DEBUG: Mascotas con relaciones del usuario:");
                    for (MascotaConRelaciones mascotaRel : listaConRelaciones) {
                        System.out.println("  - ID: " + mascotaRel.getMascota().getMascotaID() +
                                ", Nombre: " + mascotaRel.getMascota().getNombre() +
                                ", Especie: " + mascotaRel.getEspecie().getNombre() +
                                ", Imagen: " + (mascotaRel.tieneImagen() ? "Sí" : "No"));
                    }
                } else {
                    System.out.println("DEBUG: No hay mascotas para el usuario o la lista es null");
                }

                request.setAttribute("mascotasConRelaciones", listaConRelaciones);
                request.getRequestDispatcher("Vistas_JSP/Mascotas/listar_mascotas.jsp").forward(request, response);
                break;

            case "registrar":
                System.out.println("=== DEBUG: Entrando al caso 'registrar' ===");

                System.out.println("DEBUG: Usuario ID de sesión = " + usuarioId);

                // Obtener la lista de especies para el combobox
                System.out.println("=== DEBUG: Iniciando obtención de especies ===");
                List<Especie> especies = null;

                try {
                    System.out.println("DEBUG: Creando instancia de EspecieDAO...");
                    if (especieDAO == null) {
                        System.out.println("ERROR: especieDAO es null, creando nueva instancia");
                        especieDAO = new EspecieDAO();
                    }

                    System.out.println("DEBUG: Llamando a ListarEspecies()...");
                    especies = especieDAO.ListarEspecies();

                    System.out.println("DEBUG: Lista obtenida - " + (especies != null ? "NO NULL" : "ES NULL"));
                    System.out.println("DEBUG: Tamaño de lista - " + (especies != null ? especies.size() : "N/A"));

                    if (especies != null && !especies.isEmpty()) {
                        System.out.println("DEBUG: Especies encontradas:");
                        for (Especie esp : especies) {
                            System.out.println("  - " + esp.getNombre() + " (ID: " + esp.getEspecieID() + ")");
                        }
                    } else {
                        System.out.println("DEBUG: No se encontraron especies o la lista es null");
                    }

                } catch (Exception e) {
                    System.out.println("ERROR: Exception al obtener especies - " + e.getMessage());
                    e.printStackTrace();
                }

                // Pasar el ID del usuario y la lista de especies a la vista
                System.out.println("DEBUG: Estableciendo atributos para JSP...");
                request.setAttribute("usuarioId", usuarioId);
                request.setAttribute("especies", especies);

                System.out.println("DEBUG: Redirigiendo a JSP...");
                request.getRequestDispatcher("/Vistas_JSP/Mascotas/registrar_mascotas.jsp")
                        .forward(request, response);
                break;

            case "eliminar":
                System.out.println("=== DEBUG: Entrando al caso 'eliminar' ===");

                try {
                    String idParam = request.getParameter("id");
                    System.out.println("DEBUG: Parámetro ID recibido = " + idParam);

                    if (idParam == null || idParam.trim().isEmpty()) {
                        System.out.println("ERROR: ID de mascota no proporcionado");
                        response.sendRedirect("MascotaServlet?accion=listar&error=id_faltante");
                        return;
                    }

                    int idEliminar = Integer.parseInt(idParam);
                    System.out.println("DEBUG: ID a eliminar = " + idEliminar);

                    // Verificar que la mascota existe antes de intentar eliminarla
                    Mascotas mascotaAEliminar = mascotaDAO.BuscarMascota(idEliminar);
                    if (mascotaAEliminar == null) {
                        System.out.println("ERROR: No se encontró la mascota con ID " + idEliminar);
                        response.sendRedirect("MascotaServlet?accion=listar&error=mascota_no_encontrada");
                        return;
                    }

                    System.out.println("DEBUG: Mascota a eliminar encontrada: " + mascotaAEliminar.getNombre());

                    // Verificar si el usuario actual es el propietario de la mascota
                    if (usuarioId != null && !usuarioId.equals(mascotaAEliminar.getR_Usuario())) {
                        System.out.println("ERROR: Usuario " + usuarioId + " no es propietario de la mascota (propietario: " + mascotaAEliminar.getR_Usuario() + ")");
                        response.sendRedirect("MascotaServlet?accion=listar&error=no_autorizado");
                        return;
                    }

                    System.out.println("DEBUG: Llamando a EliminarMascota...");
                    boolean resultado = mascotaDAO.EliminarMascota(idEliminar);

                    if (resultado) {
                        System.out.println("DEBUG: Mascota eliminada exitosamente");
                        response.sendRedirect("MascotaServlet?accion=listar&mensaje=eliminado");
                    } else {
                        System.out.println("ERROR: Fallo al eliminar la mascota");
                        response.sendRedirect("MascotaServlet?accion=listar&error=eliminar_fallo");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: ID de mascota no válido - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("MascotaServlet?accion=listar&error=id_invalido");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception general en eliminar - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("MascotaServlet?accion=listar&error=general");
                }
                break;

            case "editar":
                System.out.println("=== DEBUG: Entrando al caso 'editar' ===");
                int idEditar = Integer.parseInt(request.getParameter("id"));
                System.out.println("DEBUG: ID a editar = " + idEditar);

                Mascotas m = mascotaDAO.BuscarMascota(idEditar);
                System.out.println("DEBUG: Mascota encontrada = " + (m != null ? m.getNombre() : "NULL"));

                // Obtener la lista de especies para el combobox en edición
                List<Especie> especiesEditar = null;
                try {
                    System.out.println("DEBUG: Obteniendo especies para edición...");
                    especiesEditar = especieDAO.ListarEspecies();
                    System.out.println("DEBUG: Especies para edición - " + (especiesEditar != null ? especiesEditar.size() + " encontradas" : "NULL"));
                } catch (Exception e) {
                    System.out.println("ERROR: Exception al obtener especies para edición - " + e.getMessage());
                    e.printStackTrace();
                }

                request.setAttribute("mascota", m);
                request.setAttribute("especies", especiesEditar);
                request.getRequestDispatcher("Vistas_JSP/Mascotas/editar_mascotas.jsp").forward(request, response);
                break;

            default:
                System.out.println("DEBUG: Caso default");
                response.sendRedirect("MascotaServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        System.out.println("=== DEBUG MascotaServlet POST: Accion = " + accion + " ===");

        switch (accion) {
            case "registrar":
                // Obtener el ID del usuario desde la sesión existente
                HttpSession session = request.getSession();
                Integer usuarioId = (Integer) session.getAttribute("usuarioId");

                // Usar el ID del usuario de la sesión
                mascota.setR_Usuario(usuarioId);
                mascota.setNombre(request.getParameter("nombre"));
                mascota.setR_Especie(Integer.parseInt(request.getParameter("r_especie")));
                mascota.setEdad(Integer.parseInt(request.getParameter("edad")));
                mascota.setSexo(request.getParameter("sexo"));
                mascota.setColor(request.getParameter("color"));
                mascota.setCaracteristicasDistintivas(request.getParameter("caracteristicasdistintivas"));
                mascota.setMicrochip(Boolean.parseBoolean(request.getParameter("microchip")));

                // Manejar el número de microchip (puede ser nulo)
                String numeroMicrochip = request.getParameter("numero_microchip");
                if (numeroMicrochip != null && !numeroMicrochip.trim().isEmpty()) {
                    mascota.setNumero_Microchip(Integer.parseInt(numeroMicrochip));
                } else {
                    mascota.setNumero_Microchip(0); // O el valor por defecto que uses
                }

                mascota.setEstado(request.getParameter("estado"));
                mascota.setFecha_Registro(java.sql.Date.valueOf(request.getParameter("fecha_registro")));

                boolean registroExitoso = mascotaDAO.RegistrarMascota(mascota);

                if (registroExitoso) {
                    // FLUJO DIRECTO: Buscar la mascota recién registrada para obtener su ID
                    List<Mascotas> mascotasUsuario = mascotaDAO.ListarMascotasPorUsuario(usuarioId);

                    if (!mascotasUsuario.isEmpty()) {
                        // OPCIÓN 1: Obtener la ÚLTIMA mascota (más reciente por ID)
                        Mascotas ultimaMascota = null;
                        int maxId = 0;

                        for (Mascotas m : mascotasUsuario) {
                            if (m.getMascotaID() > maxId) {
                                maxId = m.getMascotaID();
                                ultimaMascota = m;
                            }
                        }

                        System.out.println("DEBUG: Mascota recién registrada - ID: " + ultimaMascota.getMascotaID() + ", Nombre: " + ultimaMascota.getNombre());

                        int mascotaId = ultimaMascota.getMascotaID();
                        String nombreMascota = ultimaMascota.getNombre();

                        // Redirigir directamente a subir fotos
                        response.sendRedirect("Vistas_JSP/Mascotas/imagen_mascota_focused.jsp?mascotaId=" + mascotaId +
                                "&action=subir&nombre=" + nombreMascota);

                    } else {
                        // Fallback: si no se puede obtener el ID, mostrar mensaje de éxito
                        List<Especie> especies = especieDAO.ListarEspecies();
                        request.setAttribute("especies", especies);
                        request.setAttribute("mensaje", "¡Mascota registrada exitosamente!");
                        request.setAttribute("usuarioId", usuarioId);
                        request.getRequestDispatcher("Vistas_JSP/Mascotas/registrar_mascotas.jsp").forward(request, response);
                    }
                }
                break;

            case "actualizar":
                System.out.println("=== DEBUG: Entrando al caso 'actualizar' ===");

                try {
                    // Obtener todos los parámetros del formulario
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

                    System.out.println("DEBUG: Parámetros recibidos:");
                    System.out.println("  - MascotaID: " + mascotaId);
                    System.out.println("  - Nombre: " + nombre);
                    System.out.println("  - EspecieID: " + especieId);
                    System.out.println("  - Edad: " + edad);
                    System.out.println("  - Sexo: " + sexo);
                    System.out.println("  - Color: " + color);
                    System.out.println("  - Microchip: " + microchip);
                    System.out.println("  - Estado: " + estado);

                    // Obtener la mascota actual para preservar el usuario
                    Mascotas mascotaActual = mascotaDAO.BuscarMascota(mascotaId);
                    if (mascotaActual == null) {
                        System.out.println("ERROR: No se encontró la mascota con ID " + mascotaId);
                        response.sendRedirect("MascotaServlet?accion=listar");
                        return;
                    }

                    // Configurar la mascota con los nuevos datos
                    mascota.setMascotaID(mascotaId);
                    mascota.setR_Usuario(mascotaActual.getR_Usuario()); // Preservar el usuario original
                    mascota.setNombre(nombre);
                    mascota.setR_Especie(especieId);
                    mascota.setEdad(edad);
                    mascota.setSexo(sexo);
                    mascota.setColor(color);
                    mascota.setCaracteristicasDistintivas(caracteristicas);
                    mascota.setMicrochip(microchip);

                    // Manejar el número de microchip de forma segura
                    String numeroMicrochipStr = request.getParameter("numero_microchip");
                    if (numeroMicrochipStr != null && !numeroMicrochipStr.trim().isEmpty()) {
                        try {
                            mascota.setNumero_Microchip(Integer.parseInt(numeroMicrochipStr));
                        } catch (NumberFormatException e) {
                            System.out.println("DEBUG: Número de microchip inválido, estableciendo 0");
                            mascota.setNumero_Microchip(0);
                        }
                    } else {
                        mascota.setNumero_Microchip(0);
                    }

                    mascota.setEstado(estado);

                    // Manejar la fecha de registro de forma segura
                    if (fechaRegistro != null && !fechaRegistro.trim().isEmpty()) {
                        mascota.setFecha_Registro(java.sql.Date.valueOf(fechaRegistro));
                    } else {
                        // Preservar la fecha original si no se proporciona una nueva
                        mascota.setFecha_Registro(mascotaActual.getFecha_Registro());
                    }

                    System.out.println("DEBUG: Llamando a ModificarMascota...");
                    boolean resultado = mascotaDAO.ModificarMascota(mascota);

                    if (resultado) {
                        System.out.println("DEBUG: Mascota actualizada exitosamente");
                        // Redirigir con mensaje de éxito
                        response.sendRedirect("MascotaServlet?accion=listar&mensaje=actualizado");
                    } else {
                        System.out.println("ERROR: Fallo al actualizar la mascota");
                        // En caso de error, volver al formulario de edición con las especies
                        List<Especie> especiesEditar = especieDAO.ListarEspecies();
                        request.setAttribute("mascota", mascota);
                        request.setAttribute("especies", especiesEditar);
                        request.setAttribute("mensaje", "Error al actualizar la mascota. Inténtelo nuevamente.");
                        request.getRequestDispatcher("Vistas_JSP/Mascotas/editar_mascotas.jsp").forward(request, response);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Error de formato en los números - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("MascotaServlet?accion=listar&error=formato");
                } catch (Exception e) {
                    System.out.println("ERROR: Exception general en actualizar - " + e.getMessage());
                    e.printStackTrace();
                    response.sendRedirect("MascotaServlet?accion=listar&error=general");
                }
                break;

            default:
                response.sendRedirect("MascotaServlet?accion=listar");
                break;
        }
    }
}