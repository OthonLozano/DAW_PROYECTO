package Servlets;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/ServletLogin")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identificador = request.getParameter("identificador"); // Puede ser nombre o email
        String contrasenia = request.getParameter("contrasenia");

        // Validación de los campos
        if (identificador == null || identificador.trim().isEmpty() ||
                contrasenia == null || contrasenia.trim().isEmpty()) {
            request.setAttribute("error", "Debe ingresar su email/nombre y contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Verificar si ya hay una sesión activa
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("usuario") != null) {
            response.sendRedirect("menu.jsp");
            return;
        }

        // Buscar usuario por nombre o email
        UsuariosDAO dao = new UsuariosDAO();
        List<Usuarios> usuarios = dao.buscarPorNombre(identificador);  // Usar 'Usuarios'
        Usuarios usuario = usuarios.isEmpty() ? null : usuarios.get(0); // Usar 'Usuarios'

        if (usuario == null) {
            usuario = dao.buscarPorEmail(identificador);
        }

        // Verificar credenciales (sin encriptación)
        if (usuario != null && contrasenia.equals(usuario.getContrasenia())) {
            // Crear nueva sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setMaxInactiveInterval(30 * 60); // La sesión expira después de 30 minutos

            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("error", "Credenciales inválidas.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}