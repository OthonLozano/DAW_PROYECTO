package Servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Filtro de autenticación para proteger recursos restringidos del sistema.
 *
 * Este filtro implementa un mecanismo de control de acceso basado en sesiones
 * que intercepta todas las solicitudes a recursos protegidos y valida que
 * el usuario tenga una sesión activa válida antes de permitir el acceso.
 *
 * Funcionalidades principales:
 * - Validación de sesiones de usuario autenticadas
 * - Implementación de cabeceras anti-cache para seguridad
 * - Redirección automática a página de inicio de sesión para usuarios no autenticados
 * - Protección de JSPs y Servlets específicos del sistema
 *
 * El filtro se aplica a patrones específicos de URL que requieren autenticación,
 * incluyendo vistas de usuario y servlets de gestión de datos críticos.
 */
@WebFilter(urlPatterns = {
        "/Vistas_JSP/Usuarios/*",           // Protege todas las vistas JSP de usuarios autenticados
        "/EspecieServlet",                  // Servlet de gestión de especies
        "/MascotaServlet",                  // Servlet de gestión de mascotas
        "/ReporteDesaparicionServlet"       // Servlet de gestión de reportes de desaparición
        // Nota: Agregar aquí otros servlets que requieran autenticación
})
public class AuthFilter implements Filter {

    /**
     * Método principal del filtro que intercepta y procesa todas las solicitudes HTTP.
     *
     * Este método implementa la lógica de control de acceso realizando las siguientes operaciones:
     * 1. Configuración de cabeceras de seguridad anti-cache
     * 2. Validación de la existencia y validez de la sesión de usuario
     * 3. Verificación de la presencia del atributo de usuario en la sesión
     * 4. Redirección a página de login si la autenticación falla
     * 5. Continuación del procesamiento si la autenticación es exitosa
     *
     * Las cabeceras anti-cache previenen que navegadores almacenen en caché
     * páginas protegidas, evitando acceso no autorizado mediante botones de
     * retroceso después del cierre de sesión.
     *
     * @param req ServletRequest la solicitud HTTP entrante
     * @param res ServletResponse la respuesta HTTP saliente
     * @param chain FilterChain cadena de filtros para continuar el procesamiento
     * @throws IOException si ocurre un error de entrada/salida durante el procesamiento
     * @throws ServletException si ocurre un error específico del servlet
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // Conversión de tipos para acceso a funcionalidades específicas de HTTP
        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Configuración de cabeceras de seguridad anti-cache
        // Estas cabeceras previenen que el navegador almacene páginas protegidas en caché
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");                    // Compatibilidad con HTTP/1.0
        response.setDateHeader("Expires", 0);                        // Fecha de expiración en el pasado

        // Obtención de la sesión existente sin crear una nueva
        // El parámetro false evita la creación automática de sesión
        HttpSession session = request.getSession(false);

        // Validación de autenticación: verificar existencia de sesión y usuario
        if (session == null || session.getAttribute("usuario") == null) {
            // Usuario no autenticado: redireccionar a página de inicio de sesión
            // Se incluye un mensaje de error explicativo en la URL
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=Por+favor+inicie+sesión");
            return; // Terminar procesamiento del filtro
        }

        // Usuario autenticado: continuar con la cadena de filtros
        // Permite que la solicitud proceda hacia el recurso solicitado
        chain.doFilter(request, response);
    }
}