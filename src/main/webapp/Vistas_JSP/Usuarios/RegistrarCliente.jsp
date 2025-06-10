<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Registro de Cliente</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        .register-container {
            min-height: calc(100vh - 140px);
            display: flex;
            align-items: center;
            padding: 2rem 0;
        }

        .register-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            max-width: 900px;
            width: 100%;
            margin: 0 auto;
            border-top: 5px solid #2e7d32;
        }

        .register-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            text-align: center;
            padding: 2rem;
        }

        .register-header h2 {
            font-weight: 700;
        }

        .register-body {
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

        .btn-register {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            border: none;
            border-radius: 0.375rem;
            padding: 0.75rem;
            font-weight: 600;
            width: 100%;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
            background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
        }

        .login-link {
            color: #2e7d32;
            font-weight: 500;
            transition: color 0.3s ease;
        }

        .login-link:hover {
            color: #1b5e20;
            text-decoration: none;
        }

        .alert {
            display: none;
            margin-bottom: 1rem;
            animation: fadeIn 0.5s ease-in;
        }

        .alert.show {
            display: flex;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        #alertContainer {
            min-height: 0;
            transition: min-height 0.3s ease;
        }

        #alertContainer:empty {
            min-height: 0;
            margin: 0;
            padding: 0;
        }
    </style>
</head>
<body class="bg-light">

<main class="register-container">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="register-card">
                    <div class="register-header">
                        <h2 class="mb-3">
                            <i class="bi bi-person-plus-fill me-2"></i>Registro de Cliente
                        </h2>
                        <p class="mb-0">Únete a nuestra comunidad de amantes de mascotas</p>
                    </div>
                    <div class="register-body">
                        <form action="${pageContext.request.contextPath}/RegistrarClienteServlet" method="post" onsubmit="return handleSubmit(event)">
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-floating mb-3">
                                        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" required />
                                        <label for="nombre"><i class="bi bi-person me-2"></i>Nombre</label>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-floating mb-3">
                                        <input type="text" class="form-control" id="apellidoPat" name="apellidoPat" placeholder="Apellido Paterno" required />
                                        <label for="apellidoPat"><i class="bi bi-person me-2"></i>Apellido Paterno</label>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-floating mb-3">
                                        <input type="text" class="form-control" id="apellidoMat" name="apellidoMat" placeholder="Apellido Materno" required />
                                        <label for="apellidoMat"><i class="bi bi-person me-2"></i>Apellido Materno</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input type="email" class="form-control" id="email" name="email" placeholder="Correo Electrónico" required />
                                        <label for="email"><i class="bi bi-envelope me-2"></i>Correo Electrónico</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input type="password" class="form-control" id="contrasenia" name="contrasenia" placeholder="Contraseña" required />
                                        <label for="contrasenia"><i class="bi bi-lock me-2"></i>Contraseña</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" required />
                                        <label for="telefono"><i class="bi bi-telephone me-2"></i>Teléfono</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" required />
                                        <label for="direccion"><i class="bi bi-geo-alt me-2"></i>Dirección</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-floating mb-3">
                                        <input type="text" class="form-control" id="ciudad" name="ciudad" placeholder="Ciudad" required />
                                        <label for="ciudad"><i class="bi bi-building me-2"></i>Ciudad</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-floating mb-3">
                                        <select class="form-select" id="rol" name="rol" required>
                                            <option value="">Seleccione un rol</option>
                                            <option value="Admin">Administrador</option>
                                            <option value="Cliente">Cliente</option>
                                        </select>
                                        <label for="rol"><i class="bi bi-person-badge me-2"></i>Rol</label>
                                    </div>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-register mb-4">
                                <i class="bi bi-person-plus me-2"></i>Registrarse
                            </button>
                        </form>

                        <!-- Mensajes de error o éxito -->
                        <div id="alertContainer">
                            <c:if test="${not empty msg}">
                                <div class="alert alert-success d-flex align-items-center" role="alert">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <div>${msg}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger d-flex align-items-center" role="alert">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    <div>${error}</div>
                                </div>
                            </c:if>
                        </div>

                        <div class="text-center">
                            <p class="mb-0">¿Ya tienes una cuenta?</p>
                            <a href="${pageContext.request.contextPath}/LoginServlet" class="login-link">
                                <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script para manejar alertas -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const alertContainer = document.getElementById('alertContainer');
        const alerts = alertContainer.querySelectorAll('.alert');
        
        // Mostrar las alertas que tengan contenido
        alerts.forEach(alert => {
            if (alert.textContent.trim()) {
                alert.classList.add('show');
            }
        });

        // Si no hay alertas visibles, ocultar el contenedor
        if (!alertContainer.querySelector('.alert.show')) {
            alertContainer.style.display = 'none';
        }
    });

    function handleSubmit(event) {
        event.preventDefault();
        const form = event.target;
        const formData = new FormData(form);
        
        fetch(form.action, {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                window.location.href = '${pageContext.request.contextPath}/Vistas_JSP/Usuarios/listar_usuario.jsp';
            } else {
                throw new Error('Error en el registro');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            const alertContainer = document.getElementById('alertContainer');
            alertContainer.innerHTML = `
                <div class="alert alert-danger d-flex align-items-center show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div>Error al registrar el usuario</div>
                </div>
            `;
        });
        
        return false;
    }
</script>
</body>
</html> 