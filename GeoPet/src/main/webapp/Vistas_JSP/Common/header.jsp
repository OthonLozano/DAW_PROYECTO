<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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
  <link href="${pageContext.request.contextPath}/CSS/custom.css" rel="stylesheet">
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
      <!-- User Account Navigation -->
      <ul class="navbar-nav">
        <!-- User Account Dropdown -->
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
             data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-person-circle me-1"></i>Mi Cuenta
          </a>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/Usuario.jsp">
              <i class="bi bi-person me-1"></i>Mi Perfil</a></li>
            <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/LogoutServlet">
              <i class="bi bi-box-arrow-right me-1"></i>Cerrar Sesión</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>