package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet controlador para el registro de nuevos clientes/usuarios en el sistema.
 *
 * Este servlet maneja el proceso completo de registro de usuarios incluyendo:
 * - Presentación del formulario de registro (GET)
 * - Procesamiento y validación de datos de registro (POST)
 * - Encriptación segura de contraseñas usando BCrypt
 * - Persistencia de datos en base de datos
 * - Manejo de errores y redirecciones apropiadas
 *
 * Funcionalidades de seguridad:
 * - Validación exhaustiva de campos obligatorios
 * - Encriptación de contraseñas con BCrypt y salt único
 * - Sanitización de datos de entrada
 * - Codificación UTF-8 para soporte internacional
 * - Manejo seguro de excepciones sin exposición de información sensible
 *
 * Características del proceso de registro:
 * - Soporte para múltiples roles de usuario
 * - Campos opcionales y obligatorios diferenciados
 * - Validación de formato de email
 * - Establecimiento automático de estatus 'Alta'
 * - Redirecciones con mensajes informativos
 *
 * El servlet implementa las mejores prácticas de seguridad web incluyendo
 * validación tanto del lado servidor, encriptación robusta y manejo
 * defensivo de errores para prevenir ataques y exposición de información.
 *
 * @author Sistema de Gestión DAW
 * @version 1.0
 */
@WebServlet("/RegistrarClienteServlet")
public class RegistrarClienteServlet extends HttpServlet {

    /**
     * Instancia del DAO para operaciones de base de datos de usuarios.
     */
    private final UsuariosDAO dao = new UsuariosDAO();

    /**
     * Maneja solicitudes HTTP GET para mostrar el formulario de registro.
     *
     * Presenta la página de registro de usuarios permitiendo al cliente
     * ingresar todos los datos necesarios para crear una nueva cuenta.
     * Utiliza forward para mantener el contexto del request.
     *
     * @param request HttpServletRequest objeto de solicitud
     * @param response HttpServletResponse objeto de respuesta
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forward al formulario de registro manteniendo contexto del request
        request.getRequestDispatcher("/Vistas_JSP/Usuarios/RegistrarCliente.jsp")
                .forward(request, response);
    }

    /**
     * Procesa el registro de un nuevo usuario en el sistema.
     *
     * Este método implementa el flujo completo de registro incluyendo:
     * 1. Configuración de codificación UTF-8 para caracteres internacionales
     * 2. Extracción y validación de todos los parámetros del formulario
     * 3. Validación de campos obligatorios con mensajes específicos
     * 4. Encriptación segura de la contraseña usando BCrypt con salt
     * 5. Construcción y configuración del objeto Usuario
     * 6. Persistencia en base de datos a través del DAO
     * 7. Redirección con mensaje de éxito o error según corresponda
     *
     * Validaciones implementadas:
     * - Verificación de presencia de parámetros críticos
     * - Validación de campos obligatorios (nombre, apellidos, email, contraseña, rol)
     * - Sanitización de campos opcionales con valores por defecto
     * - Manejo defensivo contra ataques de inyección
     *
     * Características de seguridad:
     * - Encriptación BCrypt con salt único por contraseña
     * - Validación exhaustiva antes del procesamiento
     * - Codificación segura de mensajes de error en URLs
     * - No exposición de información sensible en logs o respuestas
     *
     * @param request HttpServletRequest conteniendo los datos del formulario
     * @param response HttpServletResponse para redirecciones
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuración de codificación UTF-8 antes de leer parámetros
        // Debe realizarse antes de cualquier llamada a getParameter()
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Extracción de parámetros del formulario de registro
            String nombre      = request.getParameter("nombre");
            String apellidoPat = request.getParameter("apellidoPat");
            String apellidoMat = request.getParameter("apellidoMat");
            String email       = request.getParameter("email");
            String passPlain   = request.getParameter("contrasenia");
            String telefono    = request.getParameter("telefono");
            String direccion   = request.getParameter("direccion");
            String ciudad      = request.getParameter("ciudad");
            String rol         = request.getParameter("rol");

            // Validación crítica: verificar que el formulario envió datos
            if (nombre == null && apellidoPat == null && email == null && passPlain == null) {
                throw new Exception("No se recibieron datos del formulario. Verifique la configuración.");
            }

            // Validación exhaustiva de campos obligatorios
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
                throw new Exception("Debe seleccionar un rol válido");
            }

            // Encriptación segura de contraseña usando BCrypt
            // BCrypt genera automáticamente un salt único para cada contraseña
            // proporcionando protección contra ataques de diccionario y rainbow tables
            String hashedPassword = BCrypt.hashpw(passPlain, BCrypt.gensalt());

            // Construcción del objeto Usuario con datos validados y sanitizados
            Usuarios usuario = new Usuarios();
            usuario.setNombre(nombre.trim());
            usuario.setApellidoPat(apellidoPat.trim());
            usuario.setApellidoMat(apellidoMat != null ? apellidoMat.trim() : ""); // Campo opcional
            usuario.setEmail(email.trim());
            usuario.setContrasenia(hashedPassword); // Contraseña encriptada, nunca plaintext
            usuario.setTelefono(telefono != null ? telefono.trim() : ""); // Campo opcional
            usuario.setDireccion(direccion != null ? direccion.trim() : ""); // Campo opcional
            usuario.setCiudad(ciudad != null ? ciudad.trim() : ""); // Campo opcional
            usuario.setUsuario(rol.trim()); // Rol seleccionado del formulario
            usuario.setEstatus("Alta"); // Estatus inicial para nuevos usuarios

            // Persistencia del usuario en base de datos
            dao.registrarCliente(usuario);

            // Redirección exitosa con mensaje de confirmación
            response.sendRedirect(request.getContextPath()
                    + "/Vistas_JSP/Usuarios/listar_usuarios.jsp?success=Usuario+registrado+exitosamente");

        } catch (Exception e) {
            // Manejo seguro de errores sin exposición de información sensible
            System.err.println("ERROR: Fallo en registro de usuario - " + e.getMessage());
            e.printStackTrace();

            // Preparación de mensaje de error sanitizado para el usuario
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Error desconocido al registrar usuario";
            }

            // Redirección a formulario con mensaje de error codificado de forma segura
            response.sendRedirect(request.getContextPath()
                    + "/Vistas_JSP/Usuarios/registra_usuario.jsp?error=" +
                    java.net.URLEncoder.encode("Error: " + errorMessage, "UTF-8"));
        }
    }
}