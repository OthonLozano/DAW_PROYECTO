<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Si ya hay un usuario en sesión, redirige a su dashboard
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user != null) {
        // NUEVA VALIDACIÓN: Verificar el estatus del usuario
        String estatus = user.getEstatus();
        String ctx = request.getContextPath();

        // Si el usuario está dado de baja, invalidar sesión y mostrar error
        if ("Baja".equals(estatus)) {
            session.invalidate();
            response.sendRedirect(ctx + "/login.jsp?error=Usuario+eliminado.+Contacte+al+administrador+para+más+información.");
            return;
        }

        // Si el usuario está activo, proceder normalmente
        String rol = user.getUsuario();  // "Admin" o "Cliente"
        switch (rol) {
            case "Admin":
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeAdmin.jsp");
                break;
            case "Cliente":
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeCliente.jsp");
                break;
            default:
                // sesión inválida por rol extraño: invalidamos y a login
                session.invalidate();
                response.sendRedirect(ctx + "/login.jsp?error=Rol+no+válido");
        }
        return;  // importante: nada más de este JSP se ejecuta
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Reuniendo Familias con sus Mascotas</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        .hero-section {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            padding: 4rem 0;
            margin-bottom: 3rem;
        }

        .feature-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            padding: 2rem;
            height: 100%;
            transition: all 0.3s ease;
            border-top: 3px solid #2e7d32;
        }

        .feature-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .feature-icon {
            font-size: 2.5rem;
            color: #2e7d32;
            margin-bottom: 1rem;
        }

        .login-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            border-top: 5px solid #2e7d32;
        }

        .login-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            padding: 2rem;
            text-align: center;
        }

        .login-body {
            padding: 2rem;
        }

        .form-control {
            border-radius: 0.375rem;
            padding: 0.75rem 1rem;
            border: 1px solid #e0e0e0;
            margin-bottom: 1.25rem;
        }

        .form-control:focus {
            border-color: #2e7d32;
            box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
        }

        .btn-login {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            border: none;
            border-radius: 0.375rem;
            padding: 0.75rem;
            font-weight: 600;
            width: 100%;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
            background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
        }

        .register-link {
            color: #2e7d32;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .register-link:hover {
            color: #1b5e20;
            text-decoration: none;
        }

        .testimonial-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            padding: 2rem;
            margin-bottom: 2rem;
        }

        .testimonial-avatar {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            margin-bottom: 1rem;
        }

        /* Estilos para diferentes tipos de alertas */
        .alert-usuario-eliminado {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
            border-left: 5px solid #dc3545;
        }

        .alert-usuario-eliminado .alert-icon {
            color: #dc3545;
            font-size: 1.2rem;
        }

        .alert-dismissible .btn-close {
            position: absolute;
            top: 0;
            right: 0;
            z-index: 2;
            padding: 1.25rem 1rem;
        }

        /* Animación para alertas */
        .alert {
            animation: slideInDown 0.5s ease-out;
        }

        @keyframes slideInDown {
            from {
                transform: translateY(-100%);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }
    </style>
</head>
<body class="bg-light">
<!-- Hero Section -->
<section class="hero-section">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-6">
                <h1 class="display-4 fw-bold mb-4">
                    <i class="bi bi-heart-fill me-2"></i>GeoPet
                </h1>
                <p class="lead mb-4">Reuniendo familias con sus mascotas perdidas. Tu plataforma confiable para encontrar y reportar mascotas extraviadas.</p>
                <a href="#login-section" class="btn btn-light btn-lg">
                    <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión
                </a>
            </div>

            <div class="col-lg-6 text-center">
                <img src="Resources/Imagen/Mascotas.png" alt="Mascotas" class="img-fluid" style="max-height: 400px;">
            </div>
        </div>
    </div>
</section>

<!-- Features Section -->
<section class="container mb-5">
    <h2 class="text-center mb-5">¿Por qué elegir GeoPet?</h2>
    <div class="row g-4">
        <div class="col-md-4">
            <div class="feature-card">
                <div class="text-center">
                    <i class="bi bi-search feature-icon"></i>
                    <h4>Búsqueda Eficiente</h4>
                    <p>Encuentra mascotas perdidas en tu área con nuestro sistema de búsqueda avanzado.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="feature-card">
                <div class="text-center">
                    <i class="bi bi-bell feature-icon"></i>
                    <h4>Notificaciones Instantáneas</h4>
                    <p>Recibe alertas inmediatas cuando se reporte una mascota cerca de tu ubicación.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="feature-card">
                <div class="text-center">
                    <i class="bi bi-shield-check feature-icon"></i>
                    <h4>Comunidad Confiable</h4>
                    <p>Únete a una red de amantes de mascotas comprometidos con ayudar.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Login Section -->
<section id="login-section" class="container mb-5">
    <div class="row justify-content-center">
        <div class="col-lg-6">
            <div class="login-card">
                <div class="login-header">
                    <h3 class="mb-0">
                        <i class="bi bi-box-arrow-in-right me-2"></i>
                        Iniciar Sesión
                    </h3>
                </div>
                <div class="login-body">
                    <%
                        String error = request.getParameter("error");
                        if (error != null && !error.isEmpty()) {
                            // Determinar el tipo de error para mostrar el estilo apropiado
                            boolean esUsuarioEliminado = error.toLowerCase().contains("eliminado") ||
                                    error.toLowerCase().contains("baja") ||
                                    error.toLowerCase().contains("desactivado");
                            String claseAlert = esUsuarioEliminado ? "alert-usuario-eliminado" : "alert-danger";
                            String iconoAlert = esUsuarioEliminado ? "bi-person-x-fill" : "bi-exclamation-triangle-fill";
                    %>
                    <div class="alert <%= claseAlert %> alert-dismissible fade show" role="alert">
                        <div class="d-flex align-items-center">
                            <i class="bi <%= iconoAlert %> alert-icon me-3"></i>
                            <div class="flex-grow-1">
                                <% if (esUsuarioEliminado) { %>
                                <strong>Acceso Denegado:</strong><br>
                                <%= error.replace("+", " ") %>
                                <hr class="my-2">
                                <small class="text-muted">
                                    <i class="bi bi-info-circle me-1"></i>
                                    Si crees que esto es un error, contacta al administrador del sistema.
                                </small>
                                <% } else { %>
                                <strong>Error de Autenticación:</strong><br>
                                <%= error.replace("+", " ") %>
                                <% } %>
                            </div>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="bi bi-envelope me-2"></i>Correo Electrónico
                            </label>
                            <input type="email" class="form-control"
                                   id="email" name="email" placeholder="correo@ejemplo.com" required>
                        </div>

                        <div class="mb-4">
                            <label for="contrasenia" class="form-label">
                                <i class="bi bi-lock me-2"></i>Contraseña
                            </label>
                            <input type="password" class="form-control" id="contrasenia" name="contrasenia"
                                   placeholder="Contraseña" required>
                        </div>

                        <button type="submit" class="btn btn-login mb-4">
                            <i class="bi bi-box-arrow-in-right me-2"></i>
                            Ingresar
                        </button>
                    </form>

                    <hr class="my-4">

                    <div class="text-center">
                        <p class="mb-3">¿No tienes una cuenta?</p>
                        <a href="${pageContext.request.contextPath}/RegistrarClienteServlet" class="btn btn-outline-success">
                            <i class="bi bi-person-plus me-2"></i>
                            Regístrate aquí
                        </a>
                    </div>

                    <!-- Información adicional para usuarios con problemas -->
                    <% if (error != null && (error.toLowerCase().contains("eliminado") || error.toLowerCase().contains("baja"))) { %>
                    <hr class="my-4">
                    <div class="text-center">
                        <div class="alert alert-info" role="alert">
                            <i class="bi bi-info-circle me-2"></i>
                            <strong>¿Necesitas ayuda?</strong><br>
                            <small>
                                Si tu cuenta fue desactivada por error, puedes:
                                <ul class="list-unstyled mt-2 mb-0">
                                    <li><i class="bi bi-envelope me-1"></i> Contactar soporte: soporte@geopet.com</li>
                                    <li><i class="bi bi-telephone me-1"></i> Llamar: +52 (xxx) xxx-xxxx</li>
                                </ul>
                            </small>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="bg-dark text-light py-4">
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h5><i class="bi bi-heart-fill me-2"></i>GeoPet</h5>
                <p class="mb-0">Reuniendo familias con sus mascotas perdidas</p>
            </div>
            <div class="col-md-6 text-md-end">
                <p class="mb-0">© 2025 GeoPet. Todos los derechos reservados.</p>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Auto-ocultar alertas después de 10 segundos (más tiempo para leer el mensaje de usuario eliminado)
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            setTimeout(() => {
                if (alert && !alert.classList.contains('fade')) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }
            }, 10000); // 10 segundos
        });
    });

    // Mostrar alerta más prominente si es error de usuario eliminado
    document.addEventListener('DOMContentLoaded', function() {
        const alertUsuarioEliminado = document.querySelector('.alert-usuario-eliminado');
        if (alertUsuarioEliminado) {
            // Hacer scroll hasta la alerta
            alertUsuarioEliminado.scrollIntoView({ behavior: 'smooth', block: 'center' });

            // Efecto de pulso para llamar la atención
            alertUsuarioEliminado.style.animation = 'pulse 2s infinite';

            // Quitar el efecto después de 6 segundos
            setTimeout(() => {
                alertUsuarioEliminado.style.animation = '';
            }, 6000);
        }
    });
</script>

<style>
    @keyframes pulse {
        0% { transform: scale(1); }
        50% { transform: scale(1.02); }
        100% { transform: scale(1); }
    }
</style>

</body>
</html>