<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"Admin".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración - GeoPet</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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
        
        .navbar {
            background-color: var(--primary-color) !important;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .navbar-brand {
            color: var(--text-color) !important;
            font-weight: bold;
        }
        
        .card {
            border: none;
            border-radius: 15px;
            transition: transform 0.3s ease;
            background: white;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .card:hover {
            transform: translateY(-5px);
        }
        
        .card-icon {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: var(--secondary-color);
        }
        
        .btn-primary {
            background-color: var(--secondary-color);
            border-color: var(--secondary-color);
        }
        
        .btn-primary:hover {
            background-color: var(--accent-color);
            border-color: var(--accent-color);
        }
        
        .welcome-section {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 2rem;
            border-radius: 15px;
            margin-bottom: 2rem;
        }
        
        .logout-btn {
            background-color: var(--accent-color);
            border: none;
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            transition: all 0.3s ease;
        }
        
        .logout-btn:hover {
            background-color: #f8a5c2;
            transform: scale(1.05);
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
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-paw me-2"></i>GeoPet Admin
            </a>
            <div class="d-flex align-items-center">
                <span class="me-3">
                    <i class="fas fa-user-circle me-2"></i>
                    <%= user.getNombre() %> <%= user.getApellidoPat() %>
                </span>
                <form action="${pageContext.request.contextPath}/LogoutServlet" method="post" class="d-inline">
                    <button type="submit" class="logout-btn">
                        <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                    </button>
                </form>
            </div>
        </div>
    </nav>

    <div class="container py-5">
        <div class="welcome-section">
            <h1 class="display-4">Bienvenido al Panel de Administración</h1>
            <p class="lead">Gestiona las especies, razas, usuarios y reportes del sistema</p>
        </div>

        <div class="row g-4">
            <!-- Gestión de Especies -->
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 p-4 text-center">
                    <div class="card-body">
                        <i class="fas fa-dog card-icon"></i>
                        <h3 class="card-title">Especies</h3>
                        <p class="card-text">Gestiona las diferentes especies de mascotas en el sistema</p>
                        <a href="${pageContext.request.contextPath}/EspecieServlet?accion=listar" class="btn btn-primary">
                            <i class="fas fa-cog me-2"></i>Gestionar Especies
                        </a>
                    </div>
                </div>
            </div>
            <!-- Gestión de Usuarios -->
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 p-4 text-center">
                    <div class="card-body">
                        <i class="fas fa-users card-icon"></i>
                        <h3 class="card-title">Usuarios</h3>
                        <p class="card-text">Visualiza y gestiona todos los usuarios del sistema</p>
                        <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/listar_usuarios.jsp" class="btn btn-primary">
                            <i class="fas fa-user-cog me-2"></i>Gestionar Usuarios
                        </a>
                    </div>
                </div>
            </div>

            <!-- Reportes -->
            <div class="col-md-6 col-lg-3">
                <div class="card h-100 p-4 text-center">
                    <div class="card-body">
                        <i class="fas fa-chart-line card-icon"></i>
                        <h3 class="card-title">Reportes</h3>
                        <p class="card-text">Accede a estadísticas y reportes del sistema</p>
                        <a href="${pageContext.request.contextPath}/Vistas_JSP/Reportes/reportes_globales.jsp" class="btn btn-primary">
                            <i class="fas fa-chart-bar me-2"></i>Ver Reportes
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
