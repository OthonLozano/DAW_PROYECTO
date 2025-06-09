<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    // Si ya hay un usuario en sesión, redirige a su dashboard
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user != null) {
        String rol = user.getUsuario();  // "SuperAdmin", "Admin" o "Cliente"
        String ctx = request.getContextPath();
        switch (rol) {
            case "SuperAdmin":
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeSuperAdmin.jsp");
                break;
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
                <img src="img/pets-hero.png" alt="Mascotas" class="img-fluid" style="max-height: 400px;">
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
                    <!-- Mostrar errores si existen -->
                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <div>${param.error}</div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="bi bi-envelope me-2"></i>Correo Electrónico
                                <c:if test="${not empty error}">
                                    <i class="bi bi-exclamation-triangle-fill text-danger ms-2"></i>
                                </c:if>
                            </label>
                            <input type="email" class="form-control ${not empty error ? 'is-invalid' : ''}"
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
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Testimonials Section -->
<section class="container mb-5">
    <h2 class="text-center mb-5">Lo que dicen nuestros usuarios</h2>
    <div class="row">
        <div class="col-md-4">
            <div class="testimonial-card">
                <img src="img/avatar1.jpg" alt="Usuario" class="testimonial-avatar">
                <h5>María González</h5>
                <p class="text-muted">"Gracias a GeoPet encontré a mi perro Luna después de 3 días perdida. ¡La mejor plataforma!"</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="testimonial-card">
                <img src="img/avatar2.jpg" alt="Usuario" class="testimonial-avatar">
                <h5>Carlos Rodríguez</h5>
                <p class="text-muted">"La comunidad es increíble. Todos se ayudan mutuamente para encontrar mascotas perdidas."</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="testimonial-card">
                <img src="img/avatar3.jpg" alt="Usuario" class="testimonial-avatar">
                <h5>Ana Martínez</h5>
                <p class="text-muted">"Las notificaciones instantáneas son muy útiles. Me ayudaron a encontrar a mi gato rápidamente."</p>
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
                <p class="mb-0">© 2024 GeoPet. Todos los derechos reservados.</p>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>