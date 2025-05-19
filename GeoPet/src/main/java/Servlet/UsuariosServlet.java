package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/ServletUsuario")
public class UsuariosServlet extends HttpServlet {

    // Mostrar usuarios
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Usuarios> usuarios = new UsuariosDAO().listar();  // Asegúrate de que 'Usuarios' esté bien referenciado
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("Usuario.jsp").forward(request, response);
    }

    // Insertar nuevo usuario
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        boolean esNuevo = (idStr == null || idStr.trim().isEmpty());

        // Obtener datos del formulario
        String nombre = request.getParameter("nombre");
        String apellidoPat = request.getParameter("apellidoPat");
        String apellidoMat = request.getParameter("apellidoMat");
        String email = request.getParameter("email");
        String contrasenia = request.getParameter("contrasenia");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String ciudad = request.getParameter("ciudad");

        // Validaciones
        if (nombre == null || nombre.isEmpty() || email == null || !email.contains("@") || contrasenia == null || contrasenia.length() < 8) {
            request.setAttribute("error", "Datos inválidos.");
            request.getRequestDispatcher("Usuario.jsp").forward(request, response);
            return;
        }

        Date now = new Date(System.currentTimeMillis());

        // Crear el objeto Usuario
        Usuarios usuario = new Usuarios();
        usuario.setNombre(nombre);
        usuario.setApellidoPat(apellidoPat);
        usuario.setApellidoMat(apellidoMat);
        usuario.setEmail(email);
        usuario.setContrasenia(contrasenia);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);
        usuario.setCiudad(ciudad);
        usuario.setFecha_Registro(now);
        usuario.setUsuario("");  // Puedes asignar un valor por defecto o generar uno si es necesario
        //usuario.setEstatus("");  // Puedes asignar un valor por defecto si es necesario

        // Guardar el usuario en la base de datos
        int resultado;
        UsuariosDAO dao = new UsuariosDAO();

        if (!esNuevo) {
            usuario.setUsuarioID(Integer.parseInt(idStr));
            resultado = dao.actualizar(usuario);
        } else {
            resultado = dao.createClient(usuario);
        }

        if (resultado > 0) {
            response.sendRedirect("ServletUsuario");
        } else {
            request.setAttribute("error", "Error al guardar usuario.");
            request.getRequestDispatcher("Usuario.jsp").forward(request, response);
        }
    }
}
