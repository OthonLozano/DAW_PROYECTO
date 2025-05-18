package Servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {
        "/Vistas_JSP/Usuarios/*",     // atrapa tus JSPs actuales
        "/EspecieServlet",            // y también cualquier servlet que quieras proteger
        "/MascotaServlet",
        "/ReporteDesaparicionServlet",
        // …otros servlets protegidos
})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Cabeceras anti-cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Validar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=Por+favor+inicie+sesión");
            return;
        }

        chain.doFilter(request, response);
    }
}
