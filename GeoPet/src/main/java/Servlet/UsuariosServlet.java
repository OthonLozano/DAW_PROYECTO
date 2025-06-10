package Servlet;

import Modelo.DAO.UsuariosDAO;
import Modelo.JavaBeans.Usuarios;

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
                email == null || !email.contains("@") ||
                contrasenia == null || contrasenia.length() < 6) {

            System.out.println("Validación fallida");
            request.setAttribute("error", "Por favor, complete todos los campos obligatorios correctamente.");
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellidoPat", apellidoPat);
            request.setAttribute("apellidoMat", apellidoMat);
            request.setAttribute("email", email);
            request.setAttribute("telefono", telefono);
            request.setAttribute("direccion", direccion);
            request.setAttribute("ciudad", ciudad);

            String jspDestino = esNuevo ? "Vistas_JSP/Usuarios/registrar_usuarios.jsp" : "Vistas_JSP/Usuarios/editar_usuario.jsp";
            request.getRequestDispatcher(jspDestino).forward(request, response);
            return;
        }

        try {
            Date now = new Date(System.currentTimeMillis());

            // Crear el objeto Usuario
            Usuarios usuario = new Usuarios();
            usuario.setNombre(nombre.trim());
            usuario.setApellidoPat(apellidoPat != null ? apellidoPat.trim() : "");
            usuario.setApellidoMat(apellidoMat != null ? apellidoMat.trim() : "");
            usuario.setEmail(email.trim());
            usuario.setContrasenia(contrasenia); // TODO: Hashear la contraseña con BCrypt
            usuario.setTelefono(telefono != null ? telefono.trim() : "");
            usuario.setDireccion(direccion != null ? direccion.trim() : "");
            usuario.setCiudad(ciudad != null ? ciudad.trim() : "");
            usuario.setFecha_Registro(now);
            usuario.setUsuario("Cliente"); // Valor por defecto
            usuario.setEstatus("Alta"); // Valor por defecto

            UsuariosDAO dao = new UsuariosDAO();
            int resultado;

            if (!esNuevo) {
                // Actualizar usuario existente
                usuario.setUsuarioID(Integer.parseInt(idStr));
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

                String jspDestino = esNuevo ? "Vistas_JSP/Usuarios/registrar_usuarios.jsp" : "Vistas_JSP/Usuarios/editar_usuario.jsp";
                request.getRequestDispatcher(jspDestino).forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error al procesar usuario: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("error", "Error interno del servidor: " + e.getMessage());

            String jspDestino = esNuevo ? "Vistas_JSP/Usuarios/registrar_usuarios.jsp" : "Vistas_JSP/Usuarios/editar_usuario.jsp";
            request.getRequestDispatcher(jspDestino).forward(request, response);
        }
    }
}