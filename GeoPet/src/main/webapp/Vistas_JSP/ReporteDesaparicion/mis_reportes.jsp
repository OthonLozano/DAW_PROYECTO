<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.ReporteConRelaciones" %>
<%@ page import="Modelo.JavaBeans.ReporteDesaparicion" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>
<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<%@ page import="Modelo.JavaBeans.ImagenMascota" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mis Reportes - GeoPet</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
  <!-- Font Awesome -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f8f9fa;
      min-height: 100vh;
    }

    .main-header {
      background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
      color: white;
      padding: 3rem 0;
      margin-bottom: 2rem;
      position: relative;
      overflow: hidden;
    }

    .main-header::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="reports" width="40" height="40" patternUnits="userSpaceOnUse"><g fill="rgba(255,255,255,0.1)"><rect x="10" y="10" width="20" height="15" rx="2"/><circle cx="15" cy="17" r="1"/><circle cx="20" cy="17" r="1"/></g></pattern></defs><rect width="100" height="100" fill="url(%23reports)"/></svg>');
      pointer-events: none;
    }

    .header-content {
      position: relative;
      z-index: 1;
      text-align: center;
    }

    .header-title {
      font-size: 3rem;
      font-weight: bold;
      margin-bottom: 1rem;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
      animation: fadeInDown 0.8s ease-out;
    }

    .header-subtitle {
      font-size: 1.2rem;
      opacity: 0.9;
      margin-bottom: 0;
      animation: fadeInUp 0.8s ease-out 0.2s both;
    }

    @keyframes fadeInDown {
      from {
        opacity: 0;
        transform: translateY(-30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    @keyframes fadeInUp {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .stats-card {
      background: white;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      padding: 2rem;
      text-align: center;
      margin-bottom: 2rem;
      border-top: 4px solid #007bff;
      transition: all 0.3s ease;
    }

    .stats-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
    }

    .stats-title {
      color: #2e7d32;
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 1rem;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
    }

    .stats-content {
      color: #495057;
      font-size: 1.1rem;
    }

    .stats-number {
      color: #007bff;
      font-weight: bold;
      font-size: 1.25rem;
    }

    .acciones-principales {
      text-align: center;
      margin-bottom: 2rem;
    }

    .btn-main {
      background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
      border: none;
      color: white;
      font-weight: 600;
      padding: 0.875rem 2rem;
      border-radius: 12px;
      font-size: 1rem;
      transition: all 0.3s ease;
      text-decoration: none;
      display: inline-block;
      box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
      margin: 0.5rem;
    }

    .btn-main:hover {
      background: linear-gradient(135deg, #c82333 0%, #bd2130 100%);
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(220, 53, 69, 0.4);
      color: white;
    }

    .btn-secondary-main {
      background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
      border: none;
      color: white;
      font-weight: 600;
      padding: 0.875rem 2rem;
      border-radius: 12px;
      font-size: 1rem;
      transition: all 0.3s ease;
      text-decoration: none;
      display: inline-block;
      box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
      margin: 0.5rem;
    }

    .btn-secondary-main:hover {
      background: linear-gradient(135deg, #495057 0%, #343a40 100%);
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(108, 117, 125, 0.4);
      color: white;
    }

    .alert-geopet {
      border-radius: 12px;
      padding: 1rem;
      margin-bottom: 1.5rem;
      border: none;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    .alert-success-geopet {
      background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
      color: #155724;
      border-left: 4px solid #28a745;
    }

    .alert-danger-geopet {
      background: linear-gradient(135deg, #f8d7da 0%, #f5c2c7 100%);
      color: #721c24;
      border-left: 4px solid #dc3545;
    }

    .reportes-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
      gap: 2rem;
      margin-bottom: 2rem;
    }

    .reporte-card {
      background: white;
      border-radius: 20px;
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      transition: all 0.4s ease;
      position: relative;
      cursor: pointer;
      transform: translateY(0);
    }

    .reporte-card:hover {
      transform: translateY(-10px);
      box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
    }

    .reporte-card.perdido,
    .reporte-card.activo {
      border-left: 5px solid #dc3545;
    }

    .reporte-card.en-casa {
      border-left: 5px solid #28a745;
    }

    .reporte-card.pausado {
      border-left: 5px solid #ffc107;
    }

    .reporte-card.encontrado {
      border-left: 5px solid #17a2b8;
    }

    .reporte-card.cerrado {
      border-left: 5px solid #6c757d;
    }

    .click-hint {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      background: rgba(0, 123, 255, 0.95);
      color: white;
      padding: 0.75rem 1.5rem;
      border-radius: 25px;
      font-size: 0.875rem;
      font-weight: bold;
      opacity: 0;
      transition: all 0.3s ease;
      z-index: 10;
      pointer-events: none;
      backdrop-filter: blur(10px);
    }

    .reporte-card:hover .click-hint {
      opacity: 1;
      transform: translate(-50%, -50%) scale(1.05);
    }

    .imagen-container {
      position: relative;
      height: 280px;
      overflow: hidden;
    }

    .mascota-imagen {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.4s ease;
    }

    .reporte-card:hover .mascota-imagen {
      transform: scale(1.1);
    }

    .imagen-placeholder {
      width: 100%;
      height: 100%;
      background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-size: 4rem;
    }

    .estado-badge {
      position: absolute;
      top: 15px;
      right: 15px;
      padding: 0.5rem 1rem;
      border-radius: 25px;
      font-size: 0.75rem;
      font-weight: bold;
      text-transform: uppercase;
      z-index: 5;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
      backdrop-filter: blur(10px);
    }

    .badge-activo,
    .badge-perdido {
      background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
      color: white;
      animation: pulse 2s infinite;
    }

    .badge-en-casa {
      background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
      color: white;
    }

    .badge-cerrado {
      background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
      color: white;
    }

    .badge-pausado {
      background: linear-gradient(135deg, #ffc107 0%, #e0a800 100%);
      color: #212529;
    }

    .badge-encontrado {
      background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
      color: white;
    }

    @keyframes pulse {
      0% {
        box-shadow: 0 0 0 0 rgba(220, 53, 69, 0.7),
        0 2px 10px rgba(0, 0, 0, 0.2);
      }
      70% {
        box-shadow: 0 0 0 10px rgba(220, 53, 69, 0),
        0 2px 10px rgba(0, 0, 0, 0.2);
      }
      100% {
        box-shadow: 0 0 0 0 rgba(220, 53, 69, 0),
        0 2px 10px rgba(0, 0, 0, 0.2);
      }
    }

    .sexo-icon {
      position: absolute;
      top: 15px;
      left: 15px;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      font-size: 1.25rem;
      z-index: 5;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
      backdrop-filter: blur(10px);
    }

    .sexo-macho {
      background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
      color: white;
    }

    .sexo-hembra {
      background: linear-gradient(135deg, #e83e8c 0%, #d32776 100%);
      color: white;
    }

    .card-body {
      padding: 2rem;
    }

    .mascota-nombre {
      font-size: 1.75rem;
      font-weight: bold;
      color: #2e7d32;
      margin-bottom: 0.5rem;
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .mascota-especie {
      color: #6c757d;
      font-size: 1rem;
      margin-bottom: 1rem;
      font-style: italic;
      font-weight: 500;
    }

    .info-badge {
      border-radius: 10px;
      padding: 0.75rem;
      margin-bottom: 1rem;
      font-size: 0.875rem;
      font-weight: 500;
    }

    .fecha-perdida {
      background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%);
      border-left: 4px solid #ffc107;
      color: #856404;
    }

    .ubicacion {
      background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
      border-left: 4px solid #17a2b8;
      color: #0c5460;
    }

    .descripcion {
      background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
      border-left: 4px solid #6c757d;
      color: #495057;
      line-height: 1.5;
    }

    .acciones-reporte {
      border-top: 2px solid #e9ecef;
      padding-top: 1rem;
      margin-top: 1rem;
      position: relative;
      z-index: 5;
    }

    .btn-accion {
      padding: 0.5rem 1rem;
      border-radius: 8px;
      font-weight: 600;
      font-size: 0.875rem;
      transition: all 0.3s ease;
      text-decoration: none;
      display: inline-block;
      margin: 0.25rem;
      border: none;
      cursor: pointer;
    }

    .btn-success-accion {
      background: linear-gradient(135deg, #28a745 0%, #1e7e34 100%);
      color: white;
      box-shadow: 0 2px 8px rgba(40, 167, 69, 0.3);
    }

    .btn-success-accion:hover {
      background: linear-gradient(135deg, #1e7e34 0%, #155724 100%);
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(40, 167, 69, 0.4);
      color: white;
    }

    .btn-primary-accion {
      background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
      color: white;
      box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3);
    }

    .btn-primary-accion:hover {
      background: linear-gradient(135deg, #0056b3 0%, #004085 100%);
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(0, 123, 255, 0.4);
      color: white;
    }

    .btn-danger-accion {
      background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
      color: white;
      box-shadow: 0 2px 8px rgba(220, 53, 69, 0.3);
    }

    .btn-danger-accion:hover {
      background: linear-gradient(135deg, #c82333 0%, #bd2130 100%);
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(220, 53, 69, 0.4);
      color: white;
    }

    .status-info {
      background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
      border-left: 4px solid #28a745;
      color: #155724;
      padding: 1rem;
      border-radius: 10px;
      margin-bottom: 1rem;
      text-align: center;
    }

    .fecha-reporte {
      background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
      color: #6c757d;
      padding: 0.75rem;
      border-radius: 8px;
      text-align: center;
      margin-top: 1rem;
      font-size: 0.875rem;
    }

    .empty-state {
      text-align: center;
      padding: 4rem 2rem;
      color: #6c757d;
      background: white;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      margin-bottom: 2rem;
    }

    .empty-state-icon {
      font-size: 5rem;
      margin-bottom: 1.5rem;
      opacity: 0.6;
      color: #007bff;
    }

    .empty-state h3 {
      color: #2e7d32;
      font-weight: bold;
      margin-bottom: 1rem;
    }

    .empty-state p {
      font-size: 1.1rem;
      margin-bottom: 2rem;
      line-height: 1.6;
    }

    .footer-info {
      background: white;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
      padding: 2rem;
      text-align: center;
      margin-top: 2rem;
      border-top: 4px solid #2e7d32;
    }

    .footer-title {
      color: #2e7d32;
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 1rem;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
    }

    .footer-content {
      color: #495057;
      line-height: 1.7;
      font-size: 1rem;
    }

    @media (max-width: 768px) {
      .header-title {
        font-size: 2rem;
      }

      .reportes-grid {
        grid-template-columns: 1fr;
        gap: 1.5rem;
      }

      .card-body {
        padding: 1.5rem;
      }

      .mascota-nombre {
        font-size: 1.5rem;
      }

      .btn-accion {
        display: block;
        margin: 0.5rem 0;
        width: 100%;
      }

      .acciones-principales .btn-main,
      .acciones-principales .btn-secondary-main {
        display: block;
        margin: 0.5rem auto;
        width: 80%;
      }
    }

    /* Animación de entrada para las cards */
    .reporte-card {
      animation: fadeInScale 0.6s ease-out forwards;
      opacity: 0;
    }

    @keyframes fadeInScale {
      from {
        opacity: 0;
        transform: scale(0.9) translateY(20px);
      }
      to {
        opacity: 1;
        transform: scale(1) translateY(0);
      }
    }

    /* Escalonar las animaciones */
    .reporte-card:nth-child(1) { animation-delay: 0.1s; }
    .reporte-card:nth-child(2) { animation-delay: 0.2s; }
    .reporte-card:nth-child(3) { animation-delay: 0.3s; }
    .reporte-card:nth-child(4) { animation-delay: 0.4s; }
    .reporte-card:nth-child(5) { animation-delay: 0.5s; }
    .reporte-card:nth-child(6) { animation-delay: 0.6s; }
  </style>
</head>
<body>
<!-- Header principal -->
<div class="main-header">
  <div class="container">
    <div class="header-content">
      <h1 class="header-title">
        <i class="bi bi-file-text me-3"></i>Mis Reportes
      </h1>
      <p class="header-subtitle">Gestiona los reportes de tus mascotas perdidas</p>
    </div>
  </div>
</div>

<div class="container">
  <!-- Mostrar mensajes -->
  <%
    String mensaje = (String) request.getAttribute("mensaje");
    if (mensaje != null) {
  %>
  <div class="alert alert-geopet <%= mensaje.contains("Error") ? "alert-danger-geopet" : "alert-success-geopet" %>">
    <div class="d-flex align-items-center">
      <i class="bi bi-<%= mensaje.contains("Error") ? "exclamation-triangle-fill" : "check-circle-fill" %> me-2 fs-5"></i>
      <div><%= mensaje %></div>
    </div>
  </div>
  <% } %>

  <!-- Estadísticas -->
  <%
    List<ReporteConRelaciones> misReportes = (List<ReporteConRelaciones>) request.getAttribute("misReportes");
    int totalMisReportes = misReportes != null ? misReportes.size() : 0;
    int misReportesActivos = 0;
    int reportesEnCasa = 0;
    int reportesPausados = 0;
    int reportesEncontrados = 0;
    int reportesCerrados = 0;

    if (misReportes != null) {
      for (ReporteConRelaciones reporte : misReportes) {
        String estado = reporte.getReporte().getEstadoReporte();
        switch (estado) {
          case "Activo":
            misReportesActivos++;
            break;
          case "Cerrado":
            reportesCerrados++;
            break;
          case "En casa":
            reportesEnCasa++;
            break;
          case "Pausado":
            reportesPausados++;
            break;
          case "Encontrado":
            reportesEncontrados++;
            break;
        }
      }
    }
  %>
  <div class="stats-card">
    <h3 class="stats-title">
      <i class="bi bi-graph-up"></i>Mis Estadísticas
    </h3>
    <div class="stats-content">
      <p><span class="stats-number"><%= totalMisReportes %></span> reportes totales |
        <span class="stats-number"><%= misReportesActivos %></span> activos |
        <span class="stats-number"><%= reportesCerrados + reportesEnCasa + reportesEncontrados %></span> resueltos</p>
    </div>
  </div>

  <!-- Acciones principales -->
  <div class="acciones-principales">
    <a href="ReporteDesaparicionServlet?accion=registrar" class="btn-main">
      <i class="bi bi-plus-circle me-2"></i>Reportar Nueva Mascota Perdida
    </a>
    <a href="/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp" class="btn-secondary-main">
      <i class="bi bi-house me-2"></i>Ir al Inicio
    </a>
  </div>

  <!-- Grid de mis reportes -->
  <% if (misReportes != null && !misReportes.isEmpty()) { %>
  <div class="reportes-grid">
    <%
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      for (ReporteConRelaciones reporteRel : misReportes) {
        ReporteDesaparicion reporte = reporteRel.getReporte();
        Mascotas mascota = reporteRel.getMascota();
        Usuarios usuario = reporteRel.getUsuario();
        Especie especie = reporteRel.getEspecie();
        ImagenMascota imagen = reporteRel.getImagen();

        // Determinar el estado del reporte
        String estadoReporte = reporte.getEstadoReporte();
        boolean estaPerdido = "Activo".equals(estadoReporte);
        boolean estaEnCasa = "En casa".equals(estadoReporte);
        boolean estaPausado = "Pausado".equals(estadoReporte);
        boolean estaEncontrado = "Encontrado".equals(estadoReporte);
        boolean estaCerrado = "Cerrado".equals(estadoReporte);

        // Determinar el ícono del sexo
        String sexoIcon = "";
        String sexoClass = "";
        if ("Macho".equalsIgnoreCase(mascota.getSexo()) || "M".equalsIgnoreCase(mascota.getSexo())) {
          sexoIcon = "♂";
          sexoClass = "sexo-macho";
        } else if ("Hembra".equalsIgnoreCase(mascota.getSexo()) || "H".equalsIgnoreCase(mascota.getSexo())) {
          sexoIcon = "♀";
          sexoClass = "sexo-hembra";
        }

        // Determinar clases CSS según el estado
        String cardClass = "";
        String badgeClass = "";
        if (estaPerdido) {
          cardClass = "activo";
          badgeClass = "badge-activo";
        } else if (estaPausado) {
          cardClass = "pausado";
          badgeClass = "badge-pausado";
        } else if (estaEncontrado) {
          cardClass = "encontrado";
          badgeClass = "badge-encontrado";
        } else if (estaCerrado) {
          cardClass = "cerrado";
          badgeClass = "badge-cerrado";
        } else if (estaEnCasa) {
          cardClass = "en-casa";
          badgeClass = "badge-en-casa";
        }
    %>
    <div class="reporte-card <%= cardClass %>" onclick="window.location.href='ReporteDesaparicionServlet?accion=detalle_mi_reporte&id=<%= reporte.getReporteID() %>'">
      <div class="click-hint">
        <i class="bi bi-gear me-1"></i>Clic para gestionar
      </div>

      <div class="imagen-container">
        <% if (reporteRel.tieneImagen()) { %>
        <img src="<%= request.getContextPath() %>/ImagenMascotaServlet?action=obtener&mascotaId=<%= mascota.getMascotaID() %>"
             alt="Foto de <%= mascota.getNombre() %>"
             class="mascota-imagen"
             onerror="this.parentElement.innerHTML='<div class=\'imagen-placeholder\'><i class=\'bi bi-heart-fill\'></i></div>';">
        <% } else { %>
        <div class="imagen-placeholder">
          <i class="bi bi-heart-fill"></i>
        </div>
        <% } %>

        <!-- Badge de estado -->
        <div class="estado-badge <%= badgeClass %>">
          <% if (estaPerdido) { %>
          <i class="bi bi-exclamation-triangle me-1"></i>ACTIVO
          <% } else { %>
          <%= estadoReporte.toUpperCase() %>
          <% } %>
        </div>

        <!-- Ícono de sexo -->
        <% if (!sexoIcon.isEmpty()) { %>
        <div class="sexo-icon <%= sexoClass %>">
          <%= sexoIcon %>
        </div>
        <% } %>
      </div>

      <div class="card-body">
        <h3 class="mascota-nombre">
          <i class="bi bi-heart me-2"></i><%= mascota.getNombre() %>
        </h3>
        <p class="mascota-especie">
          <i class="bi bi-tag me-1"></i><%= especie.getNombre() %>
        </p>

        <div class="info-badge fecha-perdida">
          <i class="bi bi-calendar-x me-2"></i>
          <strong>Perdido el:</strong> <%= sdf.format(reporte.getFechaDesaparicion()) %>
        </div>

        <div class="info-badge ubicacion">
          <i class="bi bi-geo-alt me-2"></i>
          <strong>Última vez visto:</strong><br>
          <%= reporte.getUbicacionUltimaVez() %>
        </div>

        <% if (reporte.getDescripcionSituacion() != null &&
                !reporte.getDescripcionSituacion().trim().isEmpty()) { %>
        <div class="info-badge descripcion">
          <i class="bi bi-chat-text me-2"></i>
          <strong>Descripción de la situación:</strong><br>
          <%= reporte.getDescripcionSituacion().length() > 100 ?
                  reporte.getDescripcionSituacion().substring(0, 100) + "..." :
                  reporte.getDescripcionSituacion() %>
        </div>
        <% } %>

        <!-- ACCIONES DEL REPORTE -->
        <div class="acciones-reporte">
          <% if (estaPerdido || estaPausado) { %>
          <!-- Estado activo o pausado - mostrar acciones principales -->
          <div class="text-center mb-3">
            <a href="ReporteDesaparicionServlet?accion=marcar_encontrada&id=<%= reporte.getReporteID() %>"
               class="btn-accion btn-success-accion"
               onclick="event.stopPropagation(); return confirm('¿Está seguro de que su mascota ya está en casa? Esto cerrará el reporte.')">
              <i class="bi bi-house-check me-1"></i>Marcar como En Casa
            </a>

            <a href="ReporteDesaparicionServlet?accion=editar&id=<%= reporte.getReporteID() %>"
               class="btn-accion btn-primary-accion"
               onclick="event.stopPropagation();">
              <i class="bi bi-pencil me-1"></i>Editar
            </a>
          </div>
          <% } else if (estaEncontrado || estaCerrado || estaEnCasa) { %>
          <!-- Estado resuelto - mostrar información de éxito -->
          <div class="status-info">
            <% if (estaEncontrado) { %>
            <div class="d-flex align-items-center justify-content-center">
              <i class="bi bi-check-circle-fill me-2 fs-5"></i>
              <div>
                <strong>¡Mascota encontrada!</strong><br>
                <small>Este reporte indica que la mascota fue encontrada</small>
              </div>
            </div>
            <% } else if (estaCerrado || estaEnCasa) { %>
            <div class="d-flex align-items-center justify-content-center">
              <i class="bi bi-house-check-fill me-2 fs-5"></i>
              <div>
                <strong>¡Mascota en casa!</strong><br>
                <small>Esta mascota ya no está perdida</small>
              </div>
            </div>
            <% } %>
          </div>
          <% } %>

          <!-- Botón para eliminar (siempre disponible) -->
          <div class="text-center">
            <a href="ReporteDesaparicionServlet?accion=eliminar&id=<%= reporte.getReporteID() %>"
               class="btn-accion btn-danger-accion"
               onclick="event.stopPropagation(); return confirm('¿Está seguro de que desea eliminar este reporte? Esta acción no se puede deshacer.')">
              <i class="bi bi-trash me-1"></i>Eliminar
            </a>
          </div>
        </div>

        <!-- Fecha de reporte -->
        <div class="fecha-reporte">
          <i class="bi bi-calendar-plus me-1"></i>
          <strong>Reportado el:</strong> <%= sdf.format(reporte.getFecha_Registro()) %>
        </div>
      </div>
    </div>
    <% } %>
  </div>
  <% } else { %>
  <!-- Estado vacío -->
  <div class="empty-state">
    <div class="empty-state-icon">
      <i class="bi bi-file-text"></i>
    </div>
    <h3>No tienes reportes de mascotas</h3>
    <p>Aún no has reportado ninguna mascota como perdida. Esperamos que no sea necesario, pero si alguna vez necesitas ayuda para encontrar a tu mascota, estaremos aquí.</p>
    <a href="ReporteDesaparicionServlet?accion=registrar" class="btn-main">
      <i class="bi bi-plus-circle me-2"></i>Reportar Mascota Perdida
    </a>
  </div>
  <% } %>

  <!-- Footer informativo -->
  <div class="footer-info">
    <h4 class="footer-title">
      <i class="bi bi-lightbulb-fill"></i>Consejos para reportes efectivos
    </h4>
    <div class="footer-content">
      <p>Haz clic en cualquier tarjeta de mascota para acceder a la vista completa de gestión. Allí podrás cambiar el estado, gestionar comentarios y avistamientos, y mucho más.</p>

      <div class="row g-3 mt-3">
        <div class="col-md-4">
          <div class="d-flex align-items-center justify-content-center">
            <i class="bi bi-cursor-fill text-primary me-2 fs-5"></i>
            <div>
              <strong>Gestión:</strong><br>
              <small class="text-muted">Haz clic en las tarjetas para gestionar cada reporte</small>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="d-flex align-items-center justify-content-center">
            <i class="bi bi-bell-fill text-warning me-2 fs-5"></i>
            <div>
              <strong>Actualiza:</strong><br>
              <small class="text-muted">Mantén la información actualizada para mejor efectividad</small>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="d-flex align-items-center justify-content-center">
            <i class="bi bi-heart-fill text-success me-2 fs-5"></i>
            <div>
              <strong>Esperanza:</strong><br>
              <small class="text-muted">Muchas mascotas son encontradas gracias a reportes como el tuyo</small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
  // Variables globales
  let allCards = [];

  // Inicialización
  document.addEventListener('DOMContentLoaded', function() {
    allCards = Array.from(document.querySelectorAll('.reporte-card'));

    // Añadir efectos de carga escalonada
    allCards.forEach((card, index) => {
      card.style.animationDelay = `${(index % 6) * 0.1}s`;
    });

    // Mejorar efectos de hover y accesibilidad
    enhanceCardInteractions();

    // Contador animado para estadísticas
    animateStatsCounters();

    // Mostrar notificación de bienvenida si hay reportes activos
    showWelcomeNotification();
  });

  // Mejorar interacciones de las cards
  function enhanceCardInteractions() {
    allCards.forEach(card => {
      // Hacer las tarjetas focusables para accesibilidad
      card.setAttribute('tabindex', '0');
      card.setAttribute('role', 'button');
      card.setAttribute('aria-label', 'Ver detalles del reporte');

      // Mejorar efectos hover
      card.addEventListener('mouseenter', function() {
        this.style.cursor = 'pointer';
        playHoverSound();
      });

      // Soporte para navegación con teclado
      card.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          this.click();
        }
      });

      // Efecto de click visual
      card.addEventListener('mousedown', function() {
        this.style.transform = 'translateY(-5px) scale(0.98)';
      });

      card.addEventListener('mouseup', function() {
        this.style.transform = 'translateY(-10px) scale(1)';
      });

      card.addEventListener('mouseleave', function() {
        this.style.transform = 'translateY(0) scale(1)';
      });
    });
  }

  // Animar contadores de estadísticas
  function animateStatsCounters() {
    const statsNumbers = document.querySelectorAll('.stats-number');

    statsNumbers.forEach(stat => {
      const finalNumber = parseInt(stat.textContent);
      if (finalNumber > 0) {
        let currentNumber = 0;
        const increment = finalNumber / 25;

        const counter = setInterval(() => {
          currentNumber += increment;
          if (currentNumber >= finalNumber) {
            stat.textContent = finalNumber;
            clearInterval(counter);
          } else {
            stat.textContent = Math.floor(currentNumber);
          }
        }, 40);
      }
    });
  }

  // Efecto de sonido para hover (opcional)
  function playHoverSound() {
    if (document.body.classList.contains('user-interacted')) {
      try {
        const audioContext = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = audioContext.createOscillator();
        const gainNode = audioContext.createGain();

        oscillator.connect(gainNode);
        gainNode.connect(audioContext.destination);

        oscillator.frequency.setValueAtTime(500, audioContext.currentTime);
        gainNode.gain.setValueAtTime(0.03, audioContext.currentTime);
        gainNode.gain.exponentialRampToValueAtTime(0.001, audioContext.currentTime + 0.1);

        oscillator.start(audioContext.currentTime);
        oscillator.stop(audioContext.currentTime + 0.1);
      } catch (e) {
        // Ignorar errores de audio
      }
    }
  }

  // Marcar interacción del usuario
  document.addEventListener('click', function() {
    document.body.classList.add('user-interacted');
  }, { once: true });

  // Mostrar notificación de bienvenida
  function showWelcomeNotification() {
    const activeCards = allCards.filter(card =>
            card.classList.contains('activo') || card.classList.contains('pausado')
    );

    if (activeCards.length > 0) {
      setTimeout(() => {
        const toast = document.createElement('div');
        toast.className = 'position-fixed top-0 end-0 p-3';
        toast.style.zIndex = '1060';
        toast.innerHTML = `
                    <div class="toast show" role="alert">
                        <div class="toast-header bg-primary text-white">
                            <i class="bi bi-file-text me-2"></i>
                            <strong class="me-auto">Mis Reportes</strong>
                            <button type="button" class="btn-close btn-close-white" onclick="this.closest('.toast').parentElement.remove()"></button>
                        </div>
                        <div class="toast-body">
                            Tienes ${activeCards.length} reporte${activeCards.length > 1 ? 's' : ''} activo${activeCards.length > 1 ? 's' : ''}. ¡Mantén la información actualizada!
                        </div>
                    </div>
                `;

        document.body.appendChild(toast);

        // Auto-remover después de 6 segundos
        setTimeout(() => {
          if (toast.parentElement) {
            toast.remove();
          }
        }, 6000);
      }, 1500);
    }
  }

  // Función para confirmar acciones críticas
  function confirmarAccion(mensaje, callback) {
    const confirmed = confirm(mensaje);
    if (confirmed && typeof callback === 'function') {
      callback();
    }
    return confirmed;
  }

  // Función auxiliar para mostrar toasts personalizados
  function showToast(title, message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = 'position-fixed top-0 end-0 p-3';
    toast.style.zIndex = '1060';

    const bgClass = type === 'success' ? 'bg-success' :
            type === 'error' ? 'bg-danger' :
                    type === 'warning' ? 'bg-warning' : 'bg-primary';

    const iconClass = type === 'success' ? 'check-circle' :
            type === 'error' ? 'exclamation-triangle' :
                    type === 'warning' ? 'exclamation-triangle' : 'info-circle';

    toast.innerHTML = `
            <div class="toast show" role="alert">
                <div class="toast-header ${bgClass} text-white">
                    <i class="bi bi-${iconClass}-fill me-2"></i>
                    <strong class="me-auto">${title}</strong>
                    <button type="button" class="btn-close btn-close-white" onclick="this.closest('.toast').parentElement.remove()"></button>
                </div>
                <div class="toast-body">${message}</div>
            </div>
        `;

    document.body.appendChild(toast);

    setTimeout(() => {
      if (toast.parentElement) {
        toast.remove();
      }
    }, 5000);
  }

  // Funcionalidad de filtrado rápido (si hay muchos reportes)
  function initializeQuickFilter() {
    if (allCards.length > 8) {
      const filterContainer = document.createElement('div');
      filterContainer.className = 'text-center mb-4';
      filterContainer.innerHTML = `
                <div class="btn-group" role="group" aria-label="Filtros de estado">
                    <button type="button" class="btn btn-outline-primary active" data-filter="all">
                        <i class="bi bi-grid me-1"></i>Todos
                    </button>
                    <button type="button" class="btn btn-outline-danger" data-filter="activo">
                        <i class="bi bi-exclamation-triangle me-1"></i>Activos
                    </button>
                    <button type="button" class="btn btn-outline-success" data-filter="resuelto">
                        <i class="bi bi-check-circle me-1"></i>Resueltos
                    </button>
                </div>
            `;

      const grid = document.querySelector('.reportes-grid');
      if (grid) {
        grid.parentNode.insertBefore(filterContainer, grid);

        // Añadir funcionalidad a los filtros
        filterContainer.querySelectorAll('[data-filter]').forEach(btn => {
          btn.addEventListener('click', function() {
            // Actualizar botones activos
            filterContainer.querySelectorAll('.btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            // Aplicar filtro
            const filter = this.dataset.filter;

            allCards.forEach(card => {
              let show = true;

              if (filter === 'activo') {
                show = card.classList.contains('activo') || card.classList.contains('pausado');
              } else if (filter === 'resuelto') {
                show = card.classList.contains('en-casa') ||
                        card.classList.contains('encontrado') ||
                        card.classList.contains('cerrado');
              }

              if (show) {
                card.style.display = 'block';
                card.style.animation = 'fadeInScale 0.3s ease-out';
              } else {
                card.style.display = 'none';
              }
            });
          });
        });
      }
    }
  }

  // Funcionalidad adicional de scroll inteligente
  function addSmoothScrollHeader() {
    let lastScrollTop = 0;
    const header = document.querySelector('.main-header');

    window.addEventListener('scroll', function() {
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

      if (scrollTop > lastScrollTop && scrollTop > 100) {
        // Scrolling down
        header.style.transform = 'translateY(-20%)';
        header.style.opacity = '0.95';
      } else {
        // Scrolling up
        header.style.transform = 'translateY(0)';
        header.style.opacity = '1';
      }

      lastScrollTop = scrollTop;
    });
  }

  // Atajos de teclado para acciones rápidas
  document.addEventListener('keydown', function(e) {
    if (e.ctrlKey || e.metaKey) {
      switch(e.key) {
        case 'n':
          e.preventDefault();
          window.location.href = 'ReporteDesaparicionServlet?accion=registrar';
          break;
        case 'h':
          e.preventDefault();
          window.location.href = '/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp';
          break;
      }
    }
  });

  // Inicializar funcionalidades adicionales al cargar
  window.addEventListener('load', function() {
    // Añadir filtros si hay muchos reportes
    initializeQuickFilter();

    // Añadir scroll header inteligente
    addSmoothScrollHeader();

    // Precargar imágenes para mejor rendimiento
    const images = document.querySelectorAll('.mascota-imagen');
    images.forEach(img => {
      const imageLoader = new Image();
      imageLoader.src = img.src;
    });
  });

  // Función para manejar errores de imagen
  function handleImageError(img) {
    const placeholder = document.createElement('div');
    placeholder.className = 'imagen-placeholder';
    placeholder.innerHTML = '<i class="bi bi-heart-fill"></i>';
    img.parentElement.replaceChild(placeholder, img);
  }
</script>
</body>
</html>