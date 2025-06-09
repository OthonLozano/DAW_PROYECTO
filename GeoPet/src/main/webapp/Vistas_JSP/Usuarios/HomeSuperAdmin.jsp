<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"SuperAdmin".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Panel SuperAdmin</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        .dashboard-container {
            min-height: 100vh;
            background-color: #f8f9fa;
            padding: 2rem 0;
        }

        .welcome-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            margin-bottom: 2rem;
            border-top: 5px solid #2e7d32;
        }

        .welcome-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            padding: 2rem;
            text-align: center;
        }

        .welcome-header h1 {
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .feature-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
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

        .btn-logout {
            background: #dc3545;
            border: none;
            border-radius: 0.375rem;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-logout:hover {
            background: #c82333;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
        }

        .feature-link {
            text-decoration: none;
            color: inherit;
        }

        .feature-link:hover {
            color: inherit;
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <div class="container">
        <!-- Tarjeta de Bienvenida -->
        <div class="welcome-card">
            <div class="welcome-header">
                <h1><i class="bi bi-shield-lock-fill me-2"></i>Panel de SuperAdmin</h1>
                <p class="mb-0">Bienvenido, <%= user.getNombre() %></p>
            </div>
        </div>

        <!-- Grid de Funcionalidades -->
        <div class="row g-4">
            <!-- Gestionar Administradores -->
            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/gestionar_administradores.jsp" class="feature-link">
                    <div class="feature-card">
                        <div class="text-center">
                            <i class="bi bi-people-fill feature-icon"></i>
                            <h4>Gestionar Administradores</h4>
                            <p class="text-muted">Administra los usuarios con rol de administrador</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Ver Usuarios -->
            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/ver_usuarios.jsp" class="feature-link">
                    <div class="feature-card">
                        <div class="text-center">
                            <i class="bi bi-person-lines-fill feature-icon"></i>
                            <h4>Ver Usuarios</h4>
                            <p class="text-muted">Visualiza y gestiona todos los usuarios del sistema</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Reportes Globales -->
            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/Vistas_JSP/Reportes/reportes_globales.jsp" class="feature-link">
                    <div class="feature-card">
                        <div class="text-center">
                            <i class="bi bi-graph-up feature-icon"></i>
                            <h4>Reportes Globales</h4>
                            <p class="text-muted">Accede a estadísticas y reportes del sistema</p>
                        </div>
                    </div>
                </a>
            </div>

            <!-- Añadir SuperAdmin -->
            <div class="col-md-6 col-lg-3">
                <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/registrar_superadmin.jsp" class="feature-link">
                    <div class="feature-card">
                        <div class="text-center">
                            <i class="bi bi-person-plus-fill feature-icon"></i>
                            <h4>Añadir SuperAdmin</h4>
                            <p class="text-muted">Registra nuevos usuarios con rol de superadmin</p>
                        </div>
                    </div>
                </a>
            </div>
        </div>

        <!-- Botón de Cerrar Sesión -->
        <div class="text-center mt-4">
            <form action="${pageContext.request.contextPath}/LogoutServlet" method="post" class="d-inline">
                <button type="submit" class="btn btn-logout">
                    <i class="bi bi-box-arrow-right me-2"></i>Cerrar Sesión
                </button>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
