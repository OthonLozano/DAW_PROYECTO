<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="Vistas_JSP/Common/header.jsp"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Iniciar Sesión</title>

    <style>
        .login-container {
            min-height: calc(100vh - 200px); /* Ajustar según altura del header/footer */
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
        }

        .login-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            max-width: 400px;
            width: 100%;
        }

        .login-header {
            background: linear-gradient(45deg, var(--geopet-primary, #667eea), var(--geopet-secondary, #764ba2));
            color: white;
            text-align: center;
            padding: 2rem 1rem;
        }

        .login-body {
            padding: 2rem;
        }

        .form-control {
            border-radius: 25px;
            border: 2px solid #e9ecef;
            padding: 12px 20px;
            margin-bottom: 1rem;
        }

        .form-control:focus {
            border-color: var(--geopet-primary, #667eea);
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }

        .btn-login {
            background: linear-gradient(45deg, var(--geopet-primary, #667eea), var(--geopet-secondary, #764ba2));
            border: none;
            border-radius: 25px;
            padding: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            width: 100%;
            color: white;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
            color: white;
        }

        .back-link {
            color: var(--geopet-primary, #667eea);
            text-decoration: none;
            font-weight: 500;
        }

        .back-link:hover {
            color: var(--geopet-secondary, #764ba2);
            text-decoration: underline;
        }
    </style>

    <script>
        function validateForm() {
            var user = document.getElementById("identificador").value;
            var pass = document.getElementById("contrasenia").value;

            if (user.trim() === "" || pass.trim() === "") {
                showAlert("Usuario y contraseña son requeridos.", "danger");
                return false;
            }

            if (pass.length < 4) {
                showAlert("La contraseña debe tener al menos 4 caracteres.", "warning");
                return false;
            }

            return true;
        }

        function showAlert(message, type) {
            const alertContainer = document.getElementById("alertContainer");
            const alert = `
                <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle me-2"></i>${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            `;
            alertContainer.innerHTML = alert;
        }
    </script>
</head>
<body class="bg-geopet-light">
<div class="login-container">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="login-card">
                    <!-- Header del login -->
                    <div class="login-header">
                        <h2 class="mb-2">
                            <i class="bi bi-geo-alt-fill me-2"></i>GeoPet
                        </h2>
                        <p class="mb-0">Iniciar Sesión</p>
                    </div>

                    <!-- Cuerpo del login -->
                    <div class="login-body">
                        <!-- Container para alertas -->
                        <div id="alertContainer" class="mb-3"></div>

                        <!-- Mostrar mensaje de error si existe -->
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger" role="alert">
                            <i class="bi bi-exclamation-triangle me-2"></i><%= error %>
                        </div>
                        <%
                            }
                        %>

                        <form method="post" action="${pageContext.request.contextPath}/ServletLogin" onsubmit="return validateForm();">
                            <div class="mb-3">
                                <label for="identificador" class="form-label">
                                    <i class="bi bi-person me-2"></i>Usuario o Email
                                </label>
                                <input
                                        type="text"
                                        class="form-control"
                                        id="identificador"
                                        name="identificador"
                                        placeholder="Ingresa tu usuario o email"
                                        required
                                        aria-label="Nombre de usuario o email"
                                        autocomplete="username">
                            </div>

                            <div class="mb-4">
                                <label for="contrasenia" class="form-label">
                                    <i class="bi bi-lock me-2"></i>Contraseña
                                </label>
                                <input
                                        type="password"
                                        class="form-control"
                                        id="contrasenia"
                                        name="contrasenia"
                                        placeholder="Ingresa tu contraseña"
                                        required
                                        aria-label="Contraseña"
                                        autocomplete="current-password">
                            </div>

                            <button type="submit" class="btn btn-login">
                                <i class="bi bi-box-arrow-in-right me-2"></i>Entrar
                            </button>
                        </form>

                        <hr class="my-4">

                        <!-- Enlaces adicionales -->
                        <div class="text-center">
                            <p class="mb-2">¿No tienes cuenta?</p>
                            <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/RegistrarCliente.jsp" class="btn btn-outline-primary rounded-pill">
                                <i class="bi bi-person-plus me-2"></i>Registrarse
                            </a>
                        </div>

                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/index.jsp" class="back-link">
                                <i class="bi bi-arrow-left me-2"></i>Volver al inicio
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="Vistas_JSP/Common/footer.jsp"%>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>