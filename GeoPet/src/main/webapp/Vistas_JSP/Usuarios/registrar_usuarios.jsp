<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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

        .register-container {
            min-height: calc(100vh - 140px);
            display: flex;
            align-items: center;
            padding: 2rem 0;
        }

        .register-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            overflow: hidden;
            max-width: 900px;
            width: 100%;
            margin: 0 auto;
            border: none;
        }

        .register-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: var(--text-color);
            text-align: center;
            padding: 2rem;
            border-radius: 15px 15px 0 0;
        }

        .register-header h2 {
            font-weight: 600;
            margin: 0;
            font-size: 1.8rem;
        }

        .register-body {
            padding: 2rem;
        }

        .form-control {
            border-radius: 8px;
            padding: 0.75rem 1rem;
            border: 1px solid #e0e0e0;
            margin-bottom: 1.25rem;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: var(--secondary-color);
            box-shadow: 0 0 0 0.2rem rgba(170, 150, 218, 0.25);
        }

        .btn-register {
            background: linear-gradient(135deg, var(--secondary-color), var(--accent-color));
            border: none;
            border-radius: 8px;
            padding: 0.75rem;
            font-weight: 600;
            width: 100%;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(252, 186, 211, 0.3);
            background: linear-gradient(135deg, var(--accent-color), var(--secondary-color));
        }

        .form-floating label {
            color: var(--text-color);
        }

        .form-floating > .form-control:focus ~ label,
        .form-floating > .form-control:not(:placeholder-shown) ~ label {
            color: var(--secondary-color);
        }

        .alert {
            border-radius: 8px;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
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
                        <form action="${pageContext.request.contextPath}/RegistrarClienteServlet" method="post">
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

                            <button type="submit" class="btn btn-register mb-4">
                                <i class="bi bi-person-plus me-2"></i>Registrarse
                            </button>
                        </form>

                        <!-- Mensajes de error o éxito - Solo se muestran cuando hay un evento -->
                        <%
                            String success = request.getParameter("success");
                            String error = request.getParameter("error");

                            if (success != null && !success.isEmpty()) {
                        %>
                        <div class="alert alert-success d-flex align-items-center" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>
                            <div><%= success %></div>
                        </div>
                        <%
                        } else if (error != null && !error.isEmpty()) {
                        %>
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <div><%= error %></div>
                        </div>
                        <%
                            }
                        %>

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
