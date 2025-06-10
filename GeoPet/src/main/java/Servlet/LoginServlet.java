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

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Email: " + email);
        System.out.println("Contraseña recibida: " + (pass != null ? "***" : "null"));

        try {
            Usuarios user = dao.login(email, pass);
            if (user != null) {
                System.out.println("Usuario encontrado: " + user.getNombre());
                System.out.println("Rol: " + user.getUsuario());
                System.out.println("Estatus: " + user.getEstatus());

                // *** VALIDACIÓN CRÍTICA: Verificar estatus del usuario ***
                String estatus = user.getEstatus();
                if ("Baja".equals(estatus)) {
                    System.out.println("ACCESO DENEGADO: Usuario con estatus Baja");
                    response.sendRedirect(ctx + "/index.jsp?error=Usuario+eliminado.+Contacte+al+administrador+para+mas+informacion.");
                    return;
                }

                // Verificar que el estatus sea válido (opcional - solo para debug)
                if (!"Alta".equals(estatus)) {
                    System.out.println("ADVERTENCIA: Usuario con estatus no estándar: " + estatus);
                    // Continúa el proceso pero registra la advertencia
                }

                System.out.println("Usuario autorizado - Creando sesión");

                HttpSession session = request.getSession(true);

                // Guardar el objeto usuario completo
                session.setAttribute("usuario", user);

                // NUEVO: Guardar también el ID del usuario por separado
                session.setAttribute("usuarioId", user.getUsuarioID()); // ← Agregar esta línea

                // Configurar tiempo de sesión (30 minutos)
                session.setMaxInactiveInterval(30 * 60);

                System.out.println("DEBUG Login: Usuario logueado - ID: " + user.getUsuarioID() + ", Rol: " + user.getUsuario());

                // Redirige según rol
                switch (user.getUsuario()) {
                    case "Admin":
                        System.out.println("Redirigiendo a HomeAdmin");
                        response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeAdmin.jsp");
                        break;
                    case "Cliente":
                        System.out.println("Redirigiendo a HomeCliente");
                        response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeCliente.jsp");
                        break;
                    default:
                        System.out.println("Rol no reconocido: " + user.getUsuario());
                        response.sendRedirect(ctx + "/index.jsp?error=Rol+no+válido");
                }
            } else {
                System.out.println("Login fallido: Credenciales incorrectas para " + email);
                response.sendRedirect(ctx + "/index.jsp?error=Credenciales+incorrectas");
            }
        } catch (Exception e) {
            System.err.println("Error en LoginServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error en el login", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir requests GET al formulario de login
        System.out.println("GET request a LoginServlet - Redirigiendo a index.jsp");
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}