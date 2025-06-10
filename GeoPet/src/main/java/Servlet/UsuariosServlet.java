package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/UsuariosServlet")
public class UsuariosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "listar";
        }

        System.out.println("=== DEBUG SERVLET ===");
        System.out.println("Acción solicitada: " + accion);

        switch (accion) {
            case "listar":
                listarUsuarios(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            case "restaurar":
                restaurarUsuario(request, response);
                break;
            case "editar":
                editarUsuario(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            default:
                listarUsuarios(request, response);
                break;
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Iniciando listado de usuarios...");

        try {
            UsuariosDAO dao = new UsuariosDAO();
            List<Usuarios> usuarios = dao.listar();

            System.out.println("Usuarios obtenidos: " + usuarios.size());

            // Debug: Mostrar los primeros usuarios
            for (int i = 0; i < Math.min(usuarios.size(), 3); i++) {
                Usuarios u = usuarios.get(i);
                System.out.println("Usuario " + (i+1) + ": " + u.getNombre() + " " + u.getApellidoPat() + " - Estatus: " + u.getEstatus());
            }

            request.setAttribute("usuarios", usuarios);
            request.setAttribute("tipoLista", "todos");

            String jspPath = "Vistas_JSP/Usuarios/listar_usuarios.jsp";
            System.out.println("Redirigiendo a: " + jspPath);

            request.getRequestDispatcher(jspPath).forward(request, response);

        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("error", "Error al cargar la lista de usuarios: " + e.getMessage());
            request.getRequestDispatcher("Vistas_JSP/Usuarios/listar_usuarios.jsp").forward(request, response);
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        System.out.println("Eliminando usuario ID: " + idParam);

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                UsuariosDAO dao = new UsuariosDAO();

                // Usar eliminación lógica (soft delete)
                boolean resultado = dao.eliminarUsuario(id);

                if (resultado) {
                    System.out.println("Usuario eliminado exitosamente (soft delete)");
                    response.sendRedirect(request.getContextPath() +
                            "/UsuariosServlet?accion=listar&success=Usuario eliminado correctamente");
                } else {
                    System.out.println("No se pudo eliminar el usuario");
                    response.sendRedirect(request.getContextPath() +
                            "/UsuariosServlet?accion=listar&error=No se pudo eliminar el usuario. Verifique que exista.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: ID inválido - " + e.getMessage());
                response.sendRedirect(request.getContextPath() +
                        "/UsuariosServlet?accion=listar&error=ID de usuario inválido");
            } catch (Exception e) {
                System.out.println("Error general al eliminar: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() +
                        "/UsuariosServlet?accion=listar&error=Error interno del servidor");
            }
        } else {
            System.out.println("ID no proporcionado para eliminación");
            response.sendRedirect(request.getContextPath() + "/UsuariosServlet?accion=listar");
        }
    }

    private void restaurarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        System.out.println("Restaurando usuario ID: " + idParam);

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                UsuariosDAO dao = new UsuariosDAO();
                boolean resultado = dao.restaurarUsuario(id);

                if (resultado) {
                    System.out.println("Usuario restaurado exitosamente");
                    response.sendRedirect(request.getContextPath() +
                            "/UsuariosServlet?accion=listar&success=Usuario restaurado correctamente");
                } else {
                    System.out.println("No se pudo restaurar el usuario");
                    response.sendRedirect(request.getContextPath() +
                            "/UsuariosServlet?accion=listar&error=No se pudo restaurar el usuario");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: ID inválido para restaurar - " + e.getMessage());
                response.sendRedirect(request.getContextPath() +
                        "/UsuariosServlet?accion=listar&error=ID inválido");
            } catch (Exception e) {
                System.out.println("Error general al restaurar: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() +
                        "/UsuariosServlet?accion=listar&error=Error interno del servidor");
            }
        } else {
            System.out.println("ID no proporcionado para restauración");
            response.sendRedirect(request.getContextPath() + "/UsuariosServlet?accion=listar");
        }
    }

    /**
     * Nuevo método para manejar la edición desde el listado de usuarios (para administradores)
     */
    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        System.out.println("Cargando usuario para edición, ID: " + idParam);

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                UsuariosDAO dao = new UsuariosDAO();
                Usuarios usuario = dao.obtenerPorId(id);

                if (usuario != null) {
                    System.out.println("Usuario encontrado: " + usuario.getNombre() + " " + usuario.getApellidoPat());
                    request.setAttribute("usuario", usuario);
                    request.getRequestDispatcher("Usuario.jsp").forward(request, response);
                } else {
                    System.out.println("Usuario no encontrado con ID: " + id);
                    response.sendRedirect(request.getContextPath() +
                            "/UsuariosServlet?accion=listar&error=Usuario no encontrado");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: ID inválido - " + e.getMessage());
                response.sendRedirect(request.getContextPath() +
                        "/UsuariosServlet?accion=listar&error=ID de usuario inválido");
            } catch (Exception e) {
                System.out.println("Error general al cargar usuario: " + e.getMessage());
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() +
                        "/UsuariosServlet?accion=listar&error=Error interno del servidor");
            }
        } else {
            System.out.println("ID no proporcionado para edición");
            response.sendRedirect(request.getContextPath() + "/UsuariosServlet?accion=listar");
        }
    }

    /**
     * Muestra el formulario para crear un nuevo usuario
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Mostrando formulario para nuevo usuario");
        request.getRequestDispatcher("Vistas_JSP/Usuarios/registrar_usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        boolean esNuevo = (idStr == null || idStr.trim().isEmpty());

        System.out.println("=== POST - GUARDAR USUARIO ===");
        System.out.println("Es nuevo usuario: " + esNuevo);

        // Obtener datos del formulario
        String nombre = request.getParameter("nombre");
        String apellidoPat = request.getParameter("apellidoPat");
        String apellidoMat = request.getParameter("apellidoMat");
        String email = request.getParameter("email");
        String contrasenia = request.getParameter("contrasenia");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String ciudad = request.getParameter("ciudad");

        System.out.println("Datos recibidos - Nombre: " + nombre + ", Email: " + email);

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() ||
                email == null || !email.contains("@")) {

            System.out.println("Validación fallida - campos obligatorios");
            String errorMsg = "Por favor, complete todos los campos obligatorios correctamente.";

            // Si es nuevo usuario, la contraseña es obligatoria
            if (esNuevo && (contrasenia == null || contrasenia.length() < 6)) {
                errorMsg = "Por favor, complete todos los campos obligatorios. La contraseña debe tener al menos 6 caracteres.";
            }

            request.setAttribute("error", errorMsg);
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellidoPat", apellidoPat);
            request.setAttribute("apellidoMat", apellidoMat);
            request.setAttribute("email", email);
            request.setAttribute("telefono", telefono);
            request.setAttribute("direccion", direccion);
            request.setAttribute("ciudad", ciudad);

            String jspDestino = esNuevo ? "Vistas_JSP/Usuarios/registrar_usuarios.jsp" : "Usuario.jsp";

            // Si es edición, necesitamos el objeto usuario completo
            if (!esNuevo) {
                try {
                    UsuariosDAO dao = new UsuariosDAO();
                    Usuarios usuario = dao.obtenerPorId(Integer.parseInt(idStr));
                    if (usuario != null) {
                        // Actualizar con los datos del formulario
                        usuario.setNombre(nombre);
                        usuario.setApellidoPat(apellidoPat);
                        usuario.setApellidoMat(apellidoMat);
                        usuario.setEmail(email);
                        usuario.setTelefono(telefono);
                        usuario.setDireccion(direccion);
                        usuario.setCiudad(ciudad);
                        request.setAttribute("usuario", usuario);
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar usuario para validación: " + e.getMessage());
                }
            }

            request.getRequestDispatcher(jspDestino).forward(request, response);
            return;
        }

        try {
            Date now = new Date(System.currentTimeMillis());
            UsuariosDAO dao = new UsuariosDAO();

            // Validar email duplicado
            if (esNuevo) {
                // Para nuevo usuario, verificar que el email no exista
                Usuarios usuarioExistente = dao.buscarPorEmail(email.trim());
                if (usuarioExistente != null) {
                    System.out.println("Email ya existe: " + email);
                    request.setAttribute("error", "El email ya está registrado en el sistema.");
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("apellidoPat", apellidoPat);
                    request.setAttribute("apellidoMat", apellidoMat);
                    request.setAttribute("telefono", telefono);
                    request.setAttribute("direccion", direccion);
                    request.setAttribute("ciudad", ciudad);
                    request.getRequestDispatcher("Vistas_JSP/Usuarios/registrar_usuarios.jsp").forward(request, response);
                    return;
                }
            } else {
                // Para actualización, verificar que el email no esté siendo usado por otro usuario
                if (dao.emailExisteParaOtroUsuario(email.trim(), Integer.parseInt(idStr))) {
                    System.out.println("Email ya existe para otro usuario: " + email);
                    request.setAttribute("error", "El email ya está siendo utilizado por otro usuario.");

                    // Cargar el usuario actual para el formulario
                    Usuarios usuario = dao.obtenerPorId(Integer.parseInt(idStr));
                    if (usuario != null) {
                        usuario.setNombre(nombre);
                        usuario.setApellidoPat(apellidoPat);
                        usuario.setApellidoMat(apellidoMat);
                        usuario.setEmail(email);
                        usuario.setTelefono(telefono);
                        usuario.setDireccion(direccion);
                        usuario.setCiudad(ciudad);
                        request.setAttribute("usuario", usuario);
                    }

                    request.getRequestDispatcher("Usuario.jsp").forward(request, response);
                    return;
                }
            }

            // Crear el objeto Usuario
            Usuarios usuario = new Usuarios();
            usuario.setNombre(nombre.trim());
            usuario.setApellidoPat(apellidoPat != null ? apellidoPat.trim() : "");
            usuario.setApellidoMat(apellidoMat != null ? apellidoMat.trim() : "");
            usuario.setEmail(email.trim());
            usuario.setTelefono(telefono != null ? telefono.trim() : "");
            usuario.setDireccion(direccion != null ? direccion.trim() : "");
            usuario.setCiudad(ciudad != null ? ciudad.trim() : "");
            usuario.setFecha_Registro(now);
            usuario.setUsuario("Cliente"); // Valor por defecto
            usuario.setEstatus("Alta"); // Valor por defecto

            // Manejo de contraseñas
            if (!esNuevo) {
                // Para actualización
                usuario.setUsuarioID(Integer.parseInt(idStr));

                if (contrasenia != null && !contrasenia.trim().isEmpty()) {
                    // Si se proporciona nueva contraseña, hashearla
                    if (contrasenia.length() < 6) {
                        System.out.println("Contraseña muy corta en actualización");
                        request.setAttribute("error", "La nueva contraseña debe tener al menos 6 caracteres.");

                        Usuarios usuarioActual = dao.obtenerPorId(Integer.parseInt(idStr));
                        if (usuarioActual != null) {
                            usuarioActual.setNombre(nombre);
                            usuarioActual.setApellidoPat(apellidoPat);
                            usuarioActual.setApellidoMat(apellidoMat);
                            usuarioActual.setEmail(email);
                            usuarioActual.setTelefono(telefono);
                            usuarioActual.setDireccion(direccion);
                            usuarioActual.setCiudad(ciudad);
                            request.setAttribute("usuario", usuarioActual);
                        }

                        request.getRequestDispatcher("Usuario.jsp").forward(request, response);
                        return;
                    }
                    usuario.setContrasenia(hashearContrasenia(contrasenia));
                } else {
                    // Mantener la contraseña actual
                    Usuarios usuarioActual = dao.obtenerPorId(Integer.parseInt(idStr));
                    if (usuarioActual != null) {
                        usuario.setContrasenia(usuarioActual.getContrasenia());
                        usuario.setUsuario(usuarioActual.getUsuario()); // Mantener tipo de usuario
                        usuario.setEstatus(usuarioActual.getEstatus()); // Mantener estatus
                    }
                }
            } else {
                // Para nuevo usuario, siempre hashear la contraseña
                if (contrasenia == null || contrasenia.length() < 6) {
                    System.out.println("Contraseña requerida para nuevo usuario");
                    request.setAttribute("error", "La contraseña es obligatoria y debe tener al menos 6 caracteres.");
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("apellidoPat", apellidoPat);
                    request.setAttribute("apellidoMat", apellidoMat);
                    request.setAttribute("email", email);
                    request.setAttribute("telefono", telefono);
                    request.setAttribute("direccion", direccion);
                    request.setAttribute("ciudad", ciudad);
                    request.getRequestDispatcher("Usuario.jsp").forward(request, response);
                    return;
                }
                usuario.setContrasenia(hashearContrasenia(contrasenia));
            }

            int resultado;

            if (!esNuevo) {
                // Actualizar usuario existente
                resultado = dao.actualizar(usuario);
                System.out.println("Actualizando usuario ID: " + idStr + ", Resultado: " + resultado);
            } else {
                // Crear nuevo usuario
                resultado = dao.createClient(usuario);
                System.out.println("Creando nuevo usuario, Resultado: " + resultado);
            }

            if (resultado > 0) {
                String mensaje = esNuevo ? "Usuario registrado correctamente" : "Usuario actualizado correctamente";
                System.out.println("Operación exitosa: " + mensaje);
                response.sendRedirect(request.getContextPath() + "/UsuariosServlet?accion=listar&success=" + mensaje);
            } else {
                String mensaje = "Error al " + (esNuevo ? "registrar" : "actualizar") + " el usuario. Intente nuevamente.";
                System.out.println("Operación fallida: " + mensaje);
                request.setAttribute("error", mensaje);

                String jspDestino = esNuevo ? "Vistas_JSP/Usuarios/registrar_usuarios.jsp" : "Usuario.jsp";

                // Para edición, cargar el usuario actual
                if (!esNuevo) {
                    request.setAttribute("usuario", usuario);
                }

                request.getRequestDispatcher(jspDestino).forward(request, response);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error de formato en ID: " + e.getMessage());
            request.setAttribute("error", "Error en el formato del ID de usuario.");
            response.sendRedirect(request.getContextPath() + "/UsuariosServlet?accion=listar");

        } catch (Exception e) {
            System.err.println("Error al procesar usuario: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("error", "Error interno del servidor: " + e.getMessage());

            String jspDestino = esNuevo ? "Vistas_JSP/Usuarios/registrar_usuarios.jsp" : "Usuario.jsp";

            // Para edición, intentar cargar el usuario
            if (!esNuevo && idStr != null) {
                try {
                    UsuariosDAO dao = new UsuariosDAO();
                    Usuarios usuario = dao.obtenerPorId(Integer.parseInt(idStr));
                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);
                    }
                } catch (Exception ex) {
                    System.err.println("Error adicional al cargar usuario: " + ex.getMessage());
                }
            }

            request.getRequestDispatcher(jspDestino).forward(request, response);
        }
    }

    /**
     * Método para hashear contraseñas usando BCrypt
     */
    private String hashearContrasenia(String contrasenia) {
        try {
            String hashed = BCrypt.hashpw(contrasenia, BCrypt.gensalt());
            System.out.println("Contraseña hasheada exitosamente");
            return hashed;
        } catch (Exception e) {
            System.err.println("Error al hashear contraseña: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, devolver la contraseña sin hashear (no recomendado para producción)
            return contrasenia;
        }
    }
}