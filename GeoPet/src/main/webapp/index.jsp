<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Inicio</title>

    <!-- Bootstrap CSS (CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Bootstrap Icons (CDN) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <!-- GeoPet Custom CSS -->
    <link href="${pageContext.request.contextPath}/Resources/CSS/custom.css" rel="stylesheet">
</head>
<body class="bg-geopet-light">
<%@include file="Vistas_JSP/Common/header.jsp"%>
<main class="main-container">
    <div class="container py-5">
        <!-- Hero Section -->
        <section class="hero-section mb-5">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold text-geopet-primary">
                        <i class="bi bi-geo-alt-fill me-2"></i>Bienvenido a GeoPet
                    </h1>
                    <p class="lead my-4">Sistema integral de gestión para el cuidado y seguimiento de mascotas. Mantén registro de tus mascotas, reporta avistamientos y conecta con otros amantes de los animales.</p>

                    <!-- Botones de acción principales -->
                    <div class="d-flex flex-wrap gap-3 mb-4">
                        <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/login.jsp" class="btn btn-geopet-primary btn-lg">
                            <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión
                        </a>
                        <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/RegistrarCliente.jsp" class="btn btn-outline-geopet-primary btn-lg">
                            <i class="bi bi-person-plus me-2"></i>Registrarse
                        </a>
                    </div>

                    <!-- Botones de funcionalidades (para usuarios ya logueados) -->
                    <div class="d-flex flex-wrap gap-3">
                        <a href="#" class="btn btn-geopet-secondary" onclick="checkLogin('mascota')">
                            <i class="bi bi-plus-circle me-2"></i>Registrar Mascota
                        </a>
                        <a href="#" class="btn btn-geopet-secondary" onclick="checkLogin('avistamiento')">
                            <i class="bi bi-binoculars me-2"></i>Reportar Avistamiento
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 d-none d-lg-block">
                    <img src="${pageContext.request.contextPath}/Resources/images/pet-hero.png" alt="Mascotas felices" class="img-fluid rounded-4 shadow">
                </div>
            </div>
        </section>

        <!-- Call to Action Section (para usuarios no logueados) -->
        <section class="cta-section mb-5">
            <div class="card bg-geopet-primary text-white">
                <div class="card-body text-center py-5">
                    <h2 class="card-title mb-3">¿Listo para cuidar mejor a tu mascota?</h2>
                    <p class="card-text lead mb-4">Únete a nuestra comunidad y mantén a tu mascota siempre segura</p>
                    <div class="d-flex justify-content-center gap-3">
                        <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/login.jsp" class="btn btn-light btn-lg">
                            <i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión
                        </a>
                        <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/RegistrarCliente.jsp" class="btn btn-outline-light btn-lg">
                            <i class="bi bi-person-plus me-2"></i>Crear Cuenta
                        </a>
                    </div>
                </div>
            </div>
        </section>

        <!-- Stats Section -->
        <section class="mb-5">
            <div class="row g-4">
                <div class="col-md-3 col-6">
                    <div class="stats-card">
                        <div class="stats-icon">
                            <i class="bi bi-heart-pulse"></i>
                        </div>
                        <span class="stats-number">1,247</span>
                        <span class="stats-label">Mascotas Registradas</span>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stats-card">
                        <div class="stats-icon">
                            <i class="bi bi-eye"></i>
                        </div>
                        <span class="stats-number">3,892</span>
                        <span class="stats-label">Avistamientos Reportados</span>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stats-card">
                        <div class="stats-icon">
                            <i class="bi bi-people"></i>
                        </div>
                        <span class="stats-number">856</span>
                        <span class="stats-label">Usuarios Activos</span>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="stats-card">
                        <div class="stats-icon">
                            <i class="bi bi-check-circle"></i>
                        </div>
                        <span class="stats-number">312</span>
                        <span class="stats-label">Mascotas Encontradas</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- Features Section -->
        <section class="mb-5">
            <h2 class="section-title">Características Principales</h2>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon text-center">
                            <i class="bi bi-geo-fill"></i>
                        </div>
                        <h3>Geolocalización</h3>
                        <p>Sistema avanzado de seguimiento para ubicar a tus mascotas en tiempo real.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon text-center">
                            <i class="bi bi-bell-fill"></i>
                        </div>
                        <h3>Alertas</h3>
                        <p>Notificaciones instantáneas cuando tu mascota sale de su zona segura.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon text-center">
                            <i class="bi bi-shield-check"></i>
                        </div>
                        <h3>Protección</h3>
                        <p>Comunidad activa que ayuda a encontrar mascotas perdidas rápidamente.</p>
                    </div>
                </div>
            </div>
        </section>
    </div>
</main>

<%@include file="Vistas_JSP/Common/footer.jsp"%>

<!-- Bootstrap JS (CDN) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script para verificar login -->
<script>
    function checkLogin(action) {
        <%
            String usuario = (String) session.getAttribute("usuario");
            if (usuario != null) {
        %>
        if (action === 'mascota') {
            window.location.href = '${pageContext.request.contextPath}/Vistas_JSP/Mascotas/ListarMascotas.jsp';
        } else if (action === 'avistamiento') {
            window.location.href = '${pageContext.request.contextPath}/Vistas_JSP/ReporteDesaparicion/ListarReportes.jsp';
        }
        <%
            } else {
        %>
        alert('Debes iniciar sesión para acceder a esta funcionalidad');
        window.location.href = '${pageContext.request.contextPath}/Vistas_JSP/Usuarios/login.jsp';
        <%
            }
        %>
    }
</script>
</body>
</html>