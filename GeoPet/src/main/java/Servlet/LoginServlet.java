package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet encargado de manejar la autenticación de usuarios en el sistema.
 * Procesa las credenciales de login y establece las sesiones correspondientes
 * según el tipo de usuario (Admin o Cliente).
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    // DAO para operaciones de usuarios
    private UsuariosDAO dao = new UsuariosDAO();

    /**
     * Procesa las peticiones POST para autenticar usuarios.
     * Valida las credenciales, verifica el estatus del usuario y
     * redirige según el rol asignado.
     *
     * @param request Petición HTTP con los parámetros de login
     * @param response Respuesta HTTP para redirección
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario de login
        String email = request.getParameter("email");
        String pass = request.getParameter("contrasenia");
        String ctx = request.getContextPath();

        try {
            // Intentar autenticar al usuario con las credenciales proporcionadas
            Usuarios user = dao.login(email, pass);

            if (user != null) {
                // Validación crítica: Verificar que el usuario esté activo
                String estatus = user.getEstatus();
                if ("Baja".equals(estatus)) {
                    // Usuario desactivado - denegar acceso
                    response.sendRedirect(ctx + "/index.jsp?error=Usuario+eliminado.+Contacte+al+administrador+para+mas+informacion.");
                    return;
                }

                // Verificar que el estatus sea válido para usuarios activos
                if (!"Alta".equals(estatus)) {
                    // Registrar advertencia para estatus no estándar pero continuar
                    System.out.println("ADVERTENCIA: Usuario con estatus no estándar: " + estatus);
                }

                // Crear nueva sesión para el usuario autenticado
                HttpSession session = request.getSession(true);

                // Guardar información del usuario en la sesión
                session.setAttribute("usuario", user);
                session.setAttribute("usuarioId", user.getUsuarioID());

                // Configurar tiempo de expiración de sesión (30 minutos)
                session.setMaxInactiveInterval(30 * 60);

                // Redirigir según el rol del usuario
                redirectByUserRole(user, response, ctx);

            } else {
                // Credenciales incorrectas - redirigir con mensaje de error
                response.sendRedirect(ctx + "/index.jsp?error=Credenciales+incorrectas");
            }

        } catch (Exception e) {
            // Manejo de errores generales durante el proceso de autenticación
            System.err.println("Error en LoginServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Error en el proceso de autenticación", e);
        }
    }

    /**
     * Redirige al usuario a la página correspondiente según su rol.
     *
     * @param user Usuario autenticado
     * @param response Respuesta HTTP para redirección
     * @param ctx Contexto de la aplicación
     * @throws IOException Si ocurre un error durante la redirección
     */
    private void redirectByUserRole(Usuarios user, HttpServletResponse response, String ctx)
            throws IOException {

        String userRole = user.getUsuario();

        switch (userRole) {
            case "Admin":
                // Redirigir a la página de administración
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeAdmin.jsp");
                break;

            case "Cliente":
                // Redirigir a la página del cliente
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeCliente.jsp");
                break;

            default:
                // Rol no reconocido - redirigir con error
                response.sendRedirect(ctx + "/index.jsp?error=Rol+no+válido");
                break;
        }
    }

    /**
     * Maneja las peticiones GET redirigiendo al formulario de login.
     * Esto previene el acceso directo al servlet mediante URL.
     *
     * @param request Petición HTTP GET
     * @param response Respuesta HTTP para redirección
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Redirigir peticiones GET al formulario de login principal
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}