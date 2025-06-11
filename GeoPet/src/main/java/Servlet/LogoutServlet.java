package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet encargado de manejar el cierre de sesión de usuarios en el sistema.
 * Invalida la sesión actual y redirige al usuario a la página de inicio
 * con un mensaje de confirmación.
 *
 * Soporta tanto peticiones GET como POST para mayor flexibilidad en la
 * implementación del logout desde diferentes tipos de elementos UI.
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    // Identificador de versión para serialización
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las peticiones GET para cierre de sesión.
     * Redirige la petición al método POST para mantener consistencia
     * en el procesamiento, permitiendo logout a través de enlaces directos.
     *
     * @param request Petición HTTP GET
     * @param response Respuesta HTTP para procesamiento
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Delegar al método POST para consistencia en el manejo
        doPost(request, response);
    }

    /**
     * Procesa el cierre de sesión del usuario.
     * Invalida la sesión activa si existe y redirige al usuario
     * a la página de inicio con un mensaje de confirmación.
     *
     * @param request Petición HTTP POST con la solicitud de logout
     * @param response Respuesta HTTP para redirección
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener la sesión actual sin crear una nueva si no existe
        HttpSession session = request.getSession(false);

        // Invalidar la sesión si existe una activa
        if (session != null) {
            session.invalidate();
        }

        // Obtener el contexto de la aplicación para construir la URL de redirección
        String ctx = request.getContextPath();

        // Redirigir al usuario a la página de inicio con mensaje de confirmación
        response.sendRedirect(ctx + "/index.jsp?msg=Cerraste+sesión+correctamente");
    }
}