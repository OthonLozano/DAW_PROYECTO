<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page session="true" %>
<%
    // Si ya hay un usuario en sesión, redirige a su dashboard
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user != null) {
        String rol = user.getUsuario();  // "SuperAdmin", "Admin" o "Cliente"
        String ctx = request.getContextPath();
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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Iniciar Sesión</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        .login-container {
            min-height: calc(100vh - 140px);
            display: flex;
            align-items: center;
            padding: 2rem 0;
        }

        .login-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            max-width: 900px;
            width: 100%;
            margin: 0 auto;
            border-top: 5px solid #2e7d32;
        }

        .login-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            text-align: center;
            padding: 2rem;
        }

        .login-header h2 {
            font-weight: 700;
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

        .feature-item {
            display: flex;
            align-items: center;
            margin-bottom: 1rem;
            color: white;
        }

        .feature-icon {
            margin-right: 1rem;
            font-size: 1.2rem;
        }
    </style>
</head>
<body class="bg-light">

<main class="login-container">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="login-card">
                    <div class="row g-0">
                        <!-- Columna izquierda - Información -->
                        <div class="col-lg-6">
                            <div class="login-header">
                                <h2 class="mb-3">
                                    <i class="bi bi-heart-fill me-2"></i>GeoPet
                                </h2>
                                <p class="mb-0">Reuniendo familias con sus mascotas perdidas</p>

                                <div class="mt-4">
                                    <div class="feature-item">
                                        <i class="bi bi-search feature-icon"></i>
                                        <span>Busca mascotas perdidas en tu área</span>
                                    </div>
                                    <div class="feature-item">
                                        <i class="bi bi-plus-circle feature-icon"></i>
                                        <span>Reporta tu mascota perdida</span>
                                    </div>
                                    <div class="feature-item">
                                        <i class="bi bi-people feature-icon"></i>
                                        <span>Conecta con la comunidad</span>
                                    </div>
                                    <div class="feature-item">
                                        <i class="bi bi-bell feature-icon"></i>
                                        <span>Recibe notificaciones importantes</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Columna derecha - Formulario -->
                        <div class="col-lg-6">
                            <div class="login-body">
                                <h3 class="text-center mb-4">
                                    <i class="bi bi-box-arrow-in-right me-2"></i>
                                    Iniciar Sesión
                                </h3>

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
                                        </label>
                                        <input type="email" class="form-control" id="email" name="email"
                                               placeholder="correo@ejemplo.com" required>
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

                                <div class="text-center mt-3">
                                    <small class="text-muted">
                                        <i class="bi bi-shield-check me-1"></i>
                                        Tus datos están seguros con nosotros
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>