<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="Vistas_JSP/Common/header.jsp"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Inicio</title>
</head>
<body class="bg-geopet-light">
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
                    <div class="d-flex gap-3">
                        <a href="#" class="btn btn-geopet-primary">
                            <i class="bi bi-plus-circle me-2"></i>Registrar Mascota
                        </a>
                        <a href="#" class="btn btn-geopet-secondary">
                            <i class="bi bi-binoculars me-2"></i>Reportar Avistamiento
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 d-none d-lg-block">
                    <img src="${pageContext.request.contextPath}/images/pet-hero.png" alt="Mascotas felices" class="img-fluid rounded-4 shadow">
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

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>