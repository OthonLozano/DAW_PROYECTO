package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

//String ctx = request.getContextPath();
//response.sendRedirect(ctx + "/EspecieServlet?accion=listar");

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UsuariosDAO dao = new UsuariosDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass  = request.getParameter("contrasenia");
        String ctx   = request.getContextPath();

        try {
            Usuarios user = dao.login(email, pass);
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", user);

                // Redirige según rol
                switch (user.getUsuario()) {
                    case "SuperAdmin":
                        response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeSuperAdmin.jsp");
                        break;
                    case "Admin":
                        response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeAdmin.jsp");
                        break;
                    case "Cliente":
                        response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeCliente.jsp");
                        break;
                    default:
                        response.sendRedirect(ctx + "/index.jsp?error=Rol+no+válido");
                }
            } else {
                response.sendRedirect(ctx + "/index.jsp?error=Credenciales+incorrectas");
            }
        } catch (Exception e) {
            throw new ServletException("Error en el login", e);
        }
    }
}

