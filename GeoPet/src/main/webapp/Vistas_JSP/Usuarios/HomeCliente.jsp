<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"Cliente".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Panel de Cliente</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/Resources/CSS/custom.css" rel="stylesheet">
</head>
<body class="bg-geopet-light">
<%@include file="../Common/header.jsp"%>

<main class="main-container">
    <div class="container py-5">
        <!-- Breadcrumb -->
        <nav class="breadcrumb-geopet mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp">Inicio</a></li>
                <li class="breadcrumb-item active">Panel de Cliente</li>
            </ol>
        </nav>

        <!-- Welcome Card -->
        <div class="geopet-card mb-5 fade-in">
            <div class="geopet-card-header">
                <h2><i class="fas fa-user-circle me-2"></i>¡Hola, <%= user.getNombre() %>!</h2>
            </div>
            <div class="geopet-card-body">
                <p class="lead">Bienvenido a tu área de cliente. Aquí puedes gestionar toda la información de tus mascotas.</p>

                <!-- Quick Actions -->
                <div class="row g-4 mt-4">
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/MascotaServlet?accion=listar" class="quick-action-btn">
                            <i class="fas fa-paw"></i>
                            <h5>Mis Mascotas</h5>
                            <p class="small text-muted">Administra tus mascotas registradas</p>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/MascotaServlet?accion=registrar" class="quick-action-btn">
                            <i class="fas fa-plus-circle"></i>
                            <h5>Nueva Mascota</h5>
                            <p class="small text-muted">Registra una nueva mascota</p>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=mis_reportes" class="quick-action-btn">
                            <i class="fas fa-search-location"></i>
                            <h5>Mis Reportes</h5>
                            <p class="small text-muted">Revisa tus reportes de desaparición</p>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=listar" class="quick-action-btn">
                            <i class="fas fa-search-location"></i>
                            <h5>Reportes de otros</h5>
                            <p class="small text-muted">Revisa los reportes de desaparición</p>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Logout Section -->
        <div class="text-center mt-5">
            <form action="${pageContext.request.contextPath}/LogoutServlet" method="post">
                <button type="submit" class="btn btn-geopet-outline">
                    <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                </button>
            </form>
        </div>
    </div>
</main>

<%@include file="../Common/footer.jsp"%>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom JS -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const animatedElements = document.querySelectorAll('.fade-in');

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate');
                }
            });
        }, { threshold: 0.1 });

        animatedElements.forEach(el => observer.observe(el));
    });
</script>
</body>
</html>