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
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f5f5f5;
      padding: 20px;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .header {
      text-align: center;
      margin-bottom: 30px;
      background: linear-gradient(135deg, #007bff, #0056b3);
      padding: 40px 20px;
      border-radius: 15px;
      color: white;
      box-shadow: 0 4px 15px rgba(0, 123, 255, 0.3);
    }

    .header h1 {
      font-size: 2.5rem;
      margin-bottom: 10px;
    }

    .header p {
      font-size: 1.2rem;
      opacity: 0.9;
    }

    .stats {
      background: white;
      padding: 20px;
      border-radius: 10px;
      margin-bottom: 30px;
      text-align: center;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .acciones-principales {
      text-align: center;
      margin-bottom: 30px;
    }

    .btn {
      display: inline-block;
      padding: 12px 24px;
      margin: 0 10px;
      text-decoration: none;
      border-radius: 8px;
      font-weight: bold;
      transition: all 0.3s ease;
      border: none;
      cursor: pointer;
      font-size: 14px;
    }

    .btn-primary {
      background-color: #007bff;
      color: white;
    }

    .btn-primary:hover {
      background-color: #0056b3;
    }

    .btn-danger {
      background-color: #dc3545;
      color: white;
    }

    .btn-danger:hover {
      background-color: #c82333;
    }

    .btn-success {
      background-color: #28a745;
      color: white;
    }

    .btn-success:hover {
      background-color: #218838;
    }

    .btn-secondary {
      background-color: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background-color: #545b62;
    }

    .btn-small {
      padding: 8px 16px;
      margin: 0 5px;
      font-size: 12px;
    }

    .mensaje {
      padding: 15px;
      margin-bottom: 20px;
      border-radius: 8px;
      text-align: center;
      font-weight: bold;
    }

    .mensaje.exito {
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }

    .mensaje.error {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .reportes-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
      gap: 25px;
      margin-bottom: 30px;
    }

    .reporte-card {
      background: white;
      border-radius: 15px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
      overflow: hidden;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
    }

    .reporte-card.perdido {
      border-left: 5px solid #dc3545;
    }

    .reporte-card.en-casa {
      border-left: 5px solid #28a745;
    }

    .reporte-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
    }

    .imagen-container {
      position: relative;
      height: 250px;
      overflow: hidden;
    }

    .mascota-imagen {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .reporte-card:hover .mascota-imagen {
      transform: scale(1.05);
    }

    .imagen-placeholder {
      width: 100%;
      height: 100%;
      background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-size: 64px;
    }

    .estado-badge {
      position: absolute;
      top: 15px;
      right: 15px;
      padding: 8px 16px;
      border-radius: 25px;
      font-size: 12px;
      font-weight: bold;
      text-transform: uppercase;
    }

    .badge-perdido {
      background-color: #dc3545;
      color: white;
      animation: pulse 2s infinite;
    }

    .badge-en-casa {
      background-color: #28a745;
      color: white;
    }

    @keyframes pulse {
      0% { box-shadow: 0 0 0 0 rgba(220, 53, 69, 0.7); }
      70% { box-shadow: 0 0 0 10px rgba(220, 53, 69, 0); }
      100% { box-shadow: 0 0 0 0 rgba(220, 53, 69, 0); }
    }

    .sexo-icon {
      position: absolute;
      top: 15px;
      left: 15px;
      width: 35px;
      height: 35px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      font-size: 18px;
    }

    .sexo-macho {
      background-color: #007bff;
      color: white;
    }

    .sexo-hembra {
      background-color: #e83e8c;
      color: white;
    }

    .card-body {
      padding: 25px;
    }

    .mascota-nombre {
      font-size: 26px;
      font-weight: bold;
      color: #333;
      margin-bottom: 5px;
    }

    .mascota-especie {
      color: #666;
      font-size: 16px;
      margin-bottom: 15px;
      font-style: italic;
    }

    .fecha-perdida {
      background-color: #fff3cd;
      border: 1px solid #ffeaa7;
      color: #856404;
      padding: 10px;
      border-radius: 8px;
      margin-bottom: 15px;
      font-size: 14px;
      text-align: center;
    }

    .ubicacion {
      background-color: #d1ecf1;
      border: 1px solid #bee5eb;
      color: #0c5460;
      padding: 10px;
      border-radius: 8px;
      margin-bottom: 15px;
      font-size: 14px;
    }

    .descripcion {
      background-color: #f8f9fa;
      padding: 12px;
      border-radius: 8px;
      margin-bottom: 15px;
      font-size: 14px;
      line-height: 1.4;
      color: #555;
    }

    .mascota-info {
      margin-bottom: 15px;
    }

    .info-item {
      margin-bottom: 8px;
      font-size: 14px;
    }

    .info-label {
      font-weight: bold;
      color: #555;
    }

    .info-value {
      color: #333;
    }

    .recompensa {
      background-color: #d4edda;
      border: 1px solid #c3e6cb;
      color: #155724;
      padding: 12px;
      border-radius: 8px;
      margin-bottom: 15px;
      text-align: center;
      font-weight: bold;
      font-size: 16px;
    }

    .acciones-reporte {
      border-top: 1px solid #eee;
      padding-top: 15px;
      margin-top: 15px;
      text-align: center;
    }

    .fecha-reporte {
      font-size: 12px;
      color: #666;
      text-align: center;
      margin-top: 10px;
      padding: 8px;
      background-color: #f8f9fa;
      border-radius: 5px;
    }

    .empty-state {
      text-align: center;
      padding: 60px 20px;
      color: #666;
    }

    .empty-state-icon {
      font-size: 64px;
      margin-bottom: 20px;
      opacity: 0.5;
    }

    .empty-state h3 {
      margin-bottom: 10px;
      color: #555;
    }

    .empty-state p {
      margin-bottom: 30px;
      font-size: 16px;
    }

    @media (max-width: 768px) {
      .reportes-grid {
        grid-template-columns: 1fr;
        gap: 20px;
      }

      .btn-small {
        display: block;
        margin: 5px 0;
        width: 100%;
      }
    }
  </style>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>📋 Mis Reportes</h1>
    <p>Gestiona los reportes de tus mascotas perdidas</p>
  </div>

  <!-- Mostrar mensajes -->
  <%
    String mensaje = (String) request.getAttribute("mensaje");
    if (mensaje != null) {
  %>
  <div class="mensaje <%= mensaje.contains("Error") ? "error" : "exito" %>">
    <%= mensaje %>
  </div>
  <% } %>

  <!-- Estadísticas -->
  <%
    List<ReporteConRelaciones> misReportes = (List<ReporteConRelaciones>) request.getAttribute("misReportes");
    int totalMisReportes = misReportes != null ? misReportes.size() : 0;
    int misReportesActivos = 0;
    int reportesEnCasa = 0;
    if (misReportes != null) {
      for (ReporteConRelaciones reporte : misReportes) {
        if ("Perdido".equals(reporte.getReporte().getEstadoReporte())) {
          misReportesActivos++;
        } else if ("En casa".equals(reporte.getReporte().getEstadoReporte())) {
          reportesEnCasa++;
        }
      }
    }
  %>
  <div class="stats">
    <h3>📊 Mis Estadísticas</h3>
    <p><strong><%= totalMisReportes %></strong> reportes totales |
      <strong><%= misReportesActivos %></strong> mascotas perdidas |
      <strong><%= reportesEnCasa %></strong> mascotas en casa</p>
  </div>

  <!-- Acciones principales -->
  <div class="acciones-principales">
    <a href="ReporteDesaparicionServlet?accion=registrar" class="btn btn-danger">
      🚨 Reportar Nueva Mascota Perdida
    </a>
    <a href="ReporteDesaparicionServlet?accion=listar" class="btn btn-secondary">
      👀 Ver Todas las Mascotas Perdidas
    </a>
    <a href="/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp" class="btn btn-secondary">
      🏠 Ir al Inicio
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
        boolean estaPerdido = "Activo".equals(reporte.getEstadoReporte());
        boolean estaEnCasa = "Cerrado".equals(reporte.getEstadoReporte());

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
    %>
    <div class="reporte-card <%= estaPerdido ? "perdido" : estaEnCasa ? "en-casa" : "" %>">
      <div class="imagen-container">
        <% if (reporteRel.tieneImagen()) { %>
        <!-- ✅ IMAGEN CORREGIDA: Usar ImagenMascotaServlet -->
        <img src="<%= request.getContextPath() %>/ImagenMascotaServlet?action=obtener&mascotaId=<%= mascota.getMascotaID() %>"
             alt="Foto de <%= mascota.getNombre() %>"
             class="mascota-imagen"
             onerror="this.parentElement.innerHTML='<div class=\'imagen-placeholder\'>🐾</div>';">
        <% } else { %>
        <div class="imagen-placeholder">
          🐾
        </div>
        <% } %>

        <!-- Badge de estado -->
        <div class="estado-badge <%= estaPerdido ? "badge-perdido" : estaEnCasa ? "badge-en-casa" : "" %>">
          <%= estaPerdido ? "PERDIDO" : estaEnCasa ? "EN CASA" : reporte.getEstadoReporte().toUpperCase() %>
        </div>

        <!-- Ícono de sexo -->
        <% if (!sexoIcon.isEmpty()) { %>
        <div class="sexo-icon <%= sexoClass %>">
          <%= sexoIcon %>
        </div>
        <% } %>
      </div>

      <div class="card-body">
        <h3 class="mascota-nombre"><%= mascota.getNombre() %></h3>
        <p class="mascota-especie"><%= especie.getNombre() %></p>

        <div class="fecha-perdida">
          📅 <strong><%= estaPerdido ? "Perdido el:" : "Perdido el:" %></strong> <%= sdf.format(reporte.getFechaDesaparicion()) %>
        </div>

        <div class="ubicacion">
          📍 <strong>Última vez visto:</strong><br>
          <%= reporte.getUbicacionUltimaVez() %>
        </div>

        <% if (reporte.getDescripcionSituacion() != null &&
                !reporte.getDescripcionSituacion().trim().isEmpty()) { %>
        <div class="descripcion">
          <strong>Descripción de la situación:</strong><br>
          <%= reporte.getDescripcionSituacion() %>
        </div>
        <% } %>

        <div class="mascota-info">
          <div class="info-item">
            <span class="info-label">Edad:</span>
            <span class="info-value"><%= mascota.getEdad() %> años</span>
          </div>
          <div class="info-item">
            <span class="info-label">Color:</span>
            <span class="info-value"><%= mascota.getColor() %></span>
          </div>
          <div class="info-item">
            <span class="info-label">Sexo:</span>
            <span class="info-value"><%= mascota.getSexo() %></span>
          </div>
          <% if (mascota.getCaracteristicasDistintivas() != null &&
                  !mascota.getCaracteristicasDistintivas().trim().isEmpty()) { %>
          <div class="info-item">
            <span class="info-label">Características distintivas:</span>
            <span class="info-value"><%= mascota.getCaracteristicasDistintivas() %></span>
          </div>
          <% } %>
        </div>

        <% if (reporte.getRecompensa() > 0) { %>
        <div class="recompensa">
          💰 <strong>Recompensa:</strong> $<%= String.format("%.2f", reporte.getRecompensa()) %>
        </div>
        <% } %>

        <!-- ACCIONES DEL REPORTE -->
        <div class="acciones-reporte">
          <% if (estaPerdido) { %>
          <!-- Botón para marcar como encontrada -->
          <a href="ReporteDesaparicionServlet?accion=marcar_encontrada&id=<%= reporte.getReporteID() %>"
             class="btn btn-success btn-small"
             onclick="return confirm('¿Está seguro de que su mascota ya está en casa? Esto cerrará el reporte.')">
            🏠 Marcar como En Casa
          </a>

          <!-- Botón para editar -->
          <a href="ReporteDesaparicionServlet?accion=editar&id=<%= reporte.getReporteID() %>"
             class="btn btn-primary btn-small">
            ✏️ Editar
          </a>
          <% } else if (estaEnCasa) { %>
          <div style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 8px; margin-bottom: 10px; text-align: center;">
            ✅ <strong>¡Mascota en casa!</strong><br>
            <small>Esta mascota ya no está perdida</small>
          </div>
          <% } %>

          <!-- Botón para eliminar (siempre disponible) -->
          <a href="ReporteDesaparicionServlet?accion=eliminar&id=<%= reporte.getReporteID() %>"
             class="btn btn-danger btn-small"
             onclick="return confirm('¿Está seguro de que desea eliminar este reporte? Esta acción no se puede deshacer.')">
            🗑️ Eliminar
          </a>
        </div>

        <!-- Fecha de reporte -->
        <div class="fecha-reporte">
          <strong>Reportado el:</strong> <%= sdf.format(reporte.getFecha_Registro()) %>
        </div>
      </div>
    </div>
    <% } %>
  </div>
  <% } else { %>
  <!-- Estado vacío -->
  <div class="empty-state">
    <div class="empty-state-icon">📋</div>
    <h3>No tienes reportes de mascotas</h3>
    <p>Aún no has reportado ninguna mascota como perdida. Esperamos que no sea necesario, pero si alguna vez necesitas ayuda para encontrar a tu mascota, estaremos aquí.</p>
    <a href="ReporteDesaparicionServlet?accion=registrar" class="btn btn-danger">
      🚨 Reportar Mascota Perdida
    </a>
  </div>
  <% } %>

  <!-- Footer informativo -->
  <div style="background: white; padding: 20px; border-radius: 10px; margin-top: 30px; text-align: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
    <h4>💡 Consejos para reportes efectivos</h4>
    <p>Mantén tu reporte actualizado con la información más reciente. Si tu mascota regresa a casa, no olvides marcarla como "En Casa" para que otros sepan que ya no está perdida.</p>
  </div>
</div>
</body>
</html>