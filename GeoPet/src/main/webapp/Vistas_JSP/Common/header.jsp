<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GeoPet - Sistema de Gestión de Mascotas</title>

  <!-- Bootstrap CSS (CDN) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Bootstrap Icons (CDN) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
  <!-- GeoPet Custom CSS -->
  <link href="${pageContext.request.contextPath}/Resources/CSS/custom.css" rel="stylesheet">
</head>
<body>
<!-- Header Navigation -->
<nav class="navbar navbar-expand-lg geopet-header">
  <div class="container">
    <!-- Brand Logo -->
    <a class="navbar-brand" href="${pageContext.request.contextPath}/index.jsp">
      <i class="bi bi-geo-alt-fill me-2"></i>GeoPet
    </a>

    <!-- Mobile Toggle Button -->
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <!-- Navigation Menu -->
    <div class="collapse navbar-collapse" id="navbarNav">
      <!-- Main Navigation -->
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">
            <i class="bi bi-house-fill me-1"></i>Inicio
          </a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="mascotasDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-heart-fill me-1"></i>Mascotas
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/mascotas?action=listar">
              <i class="bi bi-list-ul me-1"></i>Ver Todas</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/mascotas?action=nuevo">
              <i class="bi bi-plus-circle me-1"></i>Agregar Nueva</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/mascotas?action=reportes">
              <i class="bi bi-bar-chart me-1"></i>Reportes</a></li>
          </ul>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="avistamientosDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-binoculars-fill me-1"></i>Avistamientos
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/avistamientos?action=listar">
              <i class="bi bi-list-ul me-1"></i>Ver Todos</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/avistamientos?action=nuevo">
              <i class="bi bi-plus-circle me-1"></i>Reportar Avistamiento</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/avistamientos?action=mapa">
              <i class="bi bi-geo-alt me-1"></i>Ver en Mapa</a></li>
          </ul>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/comentarios?action=listar">
            <i class="bi bi-chat-dots-fill me-1"></i>Comentarios
          </a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="usuariosDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-people-fill me-1"></i>Usuarios
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=listar">
              <i class="bi bi-list-ul me-1"></i>Ver Todos</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=nuevo">
              <i class="bi bi-person-plus me-1"></i>Nuevo Usuario</a></li>
          </ul>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/reportes">
            <i class="bi bi-clipboard-data me-1"></i>Reportes
          </a>
        </li>
      </ul>

      <!-- User Account Navigation -->
      <ul class="navbar-nav">
        <!-- Notification -->
        <li class="nav-item">
          <a class="nav-link position-relative" href="${pageContext.request.contextPath}/notificaciones">
            <i class="bi bi-bell me-1"></i>
            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                3
                                <span class="visually-hidden">notificaciones no leídas</span>
                            </span>
          </a>
        </li>
        <!-- User Account Dropdown -->
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-person-circle me-1"></i>Mi Cuenta
          </a>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/perfil">
              <i class="bi bi-person me-1"></i>Mi Perfil</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/configuracion">
              <i class="bi bi-gear me-1"></i>Configuración</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ayuda">
              <i class="bi bi-question-circle me-1"></i>Ayuda</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
              <i class="bi bi-box-arrow-right me-1"></i>Cerrar Sesión</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>