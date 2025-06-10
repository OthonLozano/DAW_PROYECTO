<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Registro de Usuario</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">

    <style>
        :root {
            --primary-color: #a8d8ea;
            --secondary-color: #aa96da;
            --accent-color: #fcbad3;
            --background-color: #f8f9fa;
            --text-color: #2c3e50;
        }
        
        body {
            background-color: var(--background-color);
            color: var(--text-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            background: white;
            margin-bottom: 2rem;
        }

        .card-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 1.5rem;
            border: none;
        }

        .card-header h2 {
            margin: 0;
            font-size: 1.8rem;
            font-weight: 600;
        }

        .card-body {
            padding: 2rem;
        }

        .form-control {
            border-radius: 8px;
            padding: 0.75rem 1rem;
            border: 1px solid #e0e0e0;
            margin-bottom: 1rem;
        }

        .form-control:focus {
            border-color: var(--secondary-color);
            box-shadow: 0 0 0 0.2rem rgba(170, 150, 218, 0.25);
        }

        .form-floating label {
            color: var(--text-color);
        }

        .btn {
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--secondary-color), var(--accent-color));
            border: none;
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(252, 186, 211, 0.3);
        }

        .btn-secondary {
            background: var(--primary-color);
            border: none;
            color: var(--text-color);
        }

        .btn-secondary:hover {
            background: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(170, 150, 218, 0.3);
            color: white;
        }

        .alert {
            border-radius: 10px;
            margin-bottom: 1rem;
        }

        .form-select {
            border-radius: 8px;
            padding: 0.75rem 1rem;
            border: 1px solid #e0e0e0;
            margin-bottom: 1rem;
        }

        .form-select:focus {
            border-color: var(--secondary-color);
            box-shadow: 0 0 0 0.2rem rgba(170, 150, 218, 0.25);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2><i class="fas fa-user-plus me-2"></i>Registro de Usuario</h2>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/RegistrarClienteServlet" method="post">
                <div class="row">
                    <div class="col-md-4">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" required />
                            <label for="nombre"><i class="fas fa-user me-2"></i>Nombre</label>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="apellidoPat" name="apellidoPat" placeholder="Apellido Paterno" required />
                            <label for="apellidoPat"><i class="fas fa-user me-2"></i>Apellido Paterno</label>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="apellidoMat" name="apellidoMat" placeholder="Apellido Materno" required />
                            <label for="apellidoMat"><i class="fas fa-user me-2"></i>Apellido Materno</label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-floating mb-3">
                            <input type="email" class="form-control" id="email" name="email" placeholder="Correo Electrónico" required />
                            <label for="email"><i class="fas fa-envelope me-2"></i>Correo Electrónico</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating mb-3">
                            <input type="password" class="form-control" id="contrasenia" name="contrasenia" placeholder="Contraseña" required />
                            <label for="contrasenia"><i class="fas fa-lock me-2"></i>Contraseña</label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" required />
                            <label for="telefono"><i class="fas fa-phone me-2"></i>Teléfono</label>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" required />
                            <label for="direccion"><i class="fas fa-map-marker-alt me-2"></i>Dirección</label>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="ciudad" name="ciudad" placeholder="Ciudad" required />
                            <label for="ciudad"><i class="fas fa-city me-2"></i>Ciudad</label>
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
                            <label for="rol"><i class="fas fa-user-tag me-2"></i>Rol</label>
                        </div>
                    </div>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/UsuariosServlet?accion=listar" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver a la Lista
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Guardar Usuario
                    </button>
                </div>
            </form>

            <!-- Mensajes de error o éxito -->
            <div id="alertContainer">
                <c:if test="${not empty msg}">
                    <div class="alert alert-success d-flex align-items-center" role="alert">
                        <i class="fas fa-check-circle me-2"></i>
                        <div>${msg}</div>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger d-flex align-items-center" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        <div>${error}</div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>