package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Para logout por GET (por si alguien pincha en un enlace)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    // Logout “oficial” por POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1) Invalidar la sesión si existe
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 2) Redirigir al login (o index) con mensaje opcional
        String ctx = request.getContextPath();
        response.sendRedirect(ctx + "/index.jsp?msg=Cerraste+sesión+correctamente");
    }
}
