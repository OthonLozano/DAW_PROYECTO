package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;
import org.mindrot.jbcrypt.BCrypt; // Librería jBCrypt

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/RegistrarClienteServlet")
public class RegistrarClienteServlet extends HttpServlet {
    private UsuariosDAO dao = new UsuariosDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar el formulario
        request.getRequestDispatcher("/Vistas_JSP/Usuarios/RegistrarCliente.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Recoger datos
            String nombre      = request.getParameter("nombre");
            String apellidoPat = request.getParameter("apellidoPat");
            String apellidoMat = request.getParameter("apellidoMat");
            String email       = request.getParameter("email");
            String passPlain   = request.getParameter("contrasenia");
            String telefono    = request.getParameter("telefono");
            String direccion   = request.getParameter("direccion");
            String ciudad      = request.getParameter("ciudad");

            // Hashear la contraseña
            String hashed = BCrypt.hashpw(passPlain, BCrypt.gensalt());

            // Crear objeto Usuario con rol Cliente y estatus Activo
            Usuarios u = new Usuarios();
            u.setNombre(nombre);
            u.setApellidoPat(apellidoPat);
            u.setApellidoMat(apellidoMat);
            u.setEmail(email);
            u.setContrasenia(hashed);
            u.setTelefono(telefono);
            u.setDireccion(direccion);
            u.setCiudad(ciudad);
            u.setUsuario("Cliente");    // rol
            u.setEstatus("Activo");     // puede ser tipo_estatus
            // Fecha_Registro se toma con DEFAULT CURRENT_DATE en la BD

            // Insertar en BD
            dao.registrarCliente(u);

            // Redirect al login con mensaje de éxito
            response.sendRedirect(request.getContextPath()
                    + "/index.jsp?msg=Registro+exitoso");
        } catch (Exception e) {
            // En caso de error, forward con mensaje
            request.setAttribute("error", "Error al registrar: " + e.getMessage());
            request.getRequestDispatcher("/Vistas_JSP/Usuarios/RegistrarCliente.jsp")
                    .forward(request, response);
        }
    }
}
