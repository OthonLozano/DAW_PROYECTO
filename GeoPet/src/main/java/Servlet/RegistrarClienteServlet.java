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

        // Configurar encoding ANTES de leer parámetros
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            System.out.println("=== INICIO DEBUG SERVLET ===");
            System.out.println("Content-Type: " + request.getContentType());
            System.out.println("Method: " + request.getMethod());
            System.out.println("Request URI: " + request.getRequestURI());

            // Recoger datos
            String nombre      = request.getParameter("nombre");
            String apellidoPat = request.getParameter("apellidoPat");
            String apellidoMat = request.getParameter("apellidoMat");
            String email       = request.getParameter("email");
            String passPlain   = request.getParameter("contrasenia");
            String telefono    = request.getParameter("telefono");
            String direccion   = request.getParameter("direccion");
            String ciudad      = request.getParameter("ciudad");
            String rol         = request.getParameter("rol");

            // Debug: Imprimir valores recibidos
            System.out.println("--- PARÁMETROS RECIBIDOS ---");
            System.out.println("Nombre: [" + nombre + "]");
            System.out.println("ApellidoPat: [" + apellidoPat + "]");
            System.out.println("ApellidoMat: [" + apellidoMat + "]");
            System.out.println("Email: [" + email + "]");
            System.out.println("Contraseña: [" + (passPlain != null ? "***" : "null") + "]");
            System.out.println("Telefono: [" + telefono + "]");
            System.out.println("Direccion: [" + direccion + "]");
            System.out.println("Ciudad: [" + ciudad + "]");
            System.out.println("Rol: [" + rol + "]");

            // Verificar si todos los parámetros son null
            if (nombre == null && apellidoPat == null && email == null && passPlain == null) {
                throw new Exception("Todos los parámetros llegaron como null. Verifique el formulario.");
            }

            // Validar campos obligatorios
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("El campo Nombre es obligatorio");
            }
            if (apellidoPat == null || apellidoPat.trim().isEmpty()) {
                throw new Exception("El campo Apellido Paterno es obligatorio");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new Exception("El campo Email es obligatorio");
            }
            if (passPlain == null || passPlain.trim().isEmpty()) {
                throw new Exception("El campo Contraseña es obligatorio");
            }
            if (rol == null || rol.trim().isEmpty()) {
                throw new Exception("Debe seleccionar un rol");
            }

            System.out.println("--- VALIDACIONES PASADAS ---");

            // Hashear la contraseña
            String hashed = BCrypt.hashpw(passPlain, BCrypt.gensalt());
            System.out.println("Contraseña hasheada correctamente");

            // Crear objeto Usuario
            Usuarios u = new Usuarios();
            u.setNombre(nombre.trim());
            u.setApellidoPat(apellidoPat.trim());
            u.setApellidoMat(apellidoMat != null ? apellidoMat.trim() : "");
            u.setEmail(email.trim());
            u.setContrasenia(hashed);
            u.setTelefono(telefono != null ? telefono.trim() : "");
            u.setDireccion(direccion != null ? direccion.trim() : "");
            u.setCiudad(ciudad != null ? ciudad.trim() : "");
            u.setUsuario(rol.trim());
            u.setEstatus("Alta");

            System.out.println("--- OBJETO USUARIO CREADO ---");
            System.out.println("Usuario.nombre: " + u.getNombre());
            System.out.println("Usuario.email: " + u.getEmail());
            System.out.println("Usuario.rol: " + u.getUsuario());
            System.out.println("Usuario.estatus: " + u.getEstatus());

            // Insertar en BD
            System.out.println("--- INSERTANDO EN BASE DE DATOS ---");
            dao.registrarCliente(u);

            System.out.println("✅ Usuario registrado exitosamente en BD");
            System.out.println("=== FIN DEBUG SERVLET ===");

            // Redirigir con mensaje de éxito
            response.sendRedirect(request.getContextPath()
                    + "/Vistas_JSP/Usuarios/listar_usuarios.jsp?success=Usuario+registrado+exitosamente");

        } catch (Exception e) {
            System.err.println("❌ ERROR en doPost: " + e.getMessage());
            e.printStackTrace();

            // Redirigir con mensaje de error al formulario correcto
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Error desconocido al registrar usuario";
            }

            response.sendRedirect(request.getContextPath()
                    + "/Vistas_JSP/Usuarios/registra_usuario.jsp?error=" +
                    java.net.URLEncoder.encode("Error: " + errorMessage, "UTF-8"));
        }
    }
}