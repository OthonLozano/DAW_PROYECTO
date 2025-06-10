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
  <title>Mascotas Perdidas - GeoPet</title>
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
      background: linear-gradient(135deg, #dc3545, #c82333);
      padding: 40px 20px;
      border-radius: 15px;
      color: white;
      box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
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

    .btn-secondary {
      background-color: #6c757d;
      color: white;
    }

    .btn-secondary:hover {
      background-color: #545b62;
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
      grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
      gap: 25px;
      margin-bottom: 30px;
    }

    .reporte-card {
      background: white;
      border-radius: 15px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
      overflow: hidden;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      border-left: 5px solid #dc3545;
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

    .perdido-badge {
      position: absolute;
      top: 15px;
      right: 15px;
      background-color: #dc3545;
      color: white;
      padding: 8px 16px;
      border-radius: 25px;
      font-size: 12px;
      font-weight: bold;
      text-transform: uppercase;
      animation: pulse 2s infinite;
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

    .contacto {
      border-top: 1px solid #eee;
      padding-top: 15px;
      background-color: #f8f9fa;
      margin: -25px -25px 0 -25px;
      padding: 15px 25px;
    }

    .contacto-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .contacto-datos {
      font-size: 14px;
    }

    .contacto-nombre {
      font-weight: bold;
      color: #333;
      margin-bottom: 4px;
    }

    .contacto-telefono {
      color: #007bff;
      text-decoration: none;
    }

    .contacto-telefono:hover {
      text-decoration: underline;
    }

    .fecha-reporte {
      font-size: 12px;
      color: #666;
      text-align: right;
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

      .contacto-info {
        flex-direction: column;
        align-items: flex-start;
      }

      .fecha-reporte {
        text-align: left;
        margin-top: 10px;
      }
    }
  </style>
</head>
<body>
<div class="container">
  <%
    Boolean usuarioLogueado = (Boolean) request.getAttribute("usuarioLogueado");
    boolean esUsuarioLogueado = usuarioLogueado != null && usuarioLogueado;
  %>

  <div class="header">
    <h1>🚨 Mascotas Perdidas</h1>
    <% if (esUsuarioLogueado) { %>
    <p>Mascotas perdidas reportadas por otros usuarios - Ayúdalos a encontrar a sus compañeros</p>
    <% } else { %>
    <p>Ayuda a reunir familias con sus mascotas perdidas</p>
    <% } %>
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
    List<ReporteConRelaciones> reportes = (List<ReporteConRelaciones>) request.getAttribute("reportesCompletos");
    int totalReportes = reportes != null ? reportes.size() : 0;
    int reportesActivos = 0;
    if (reportes != null) {
      for (ReporteConRelaciones reporte : reportes) {
        if (reporte.esReporteActivo()) {
          reportesActivos++;
        }
      }
    }
  %>
  <div class="stats">
    <h3>📊 Estadísticas de Reportes</h3>
    <% if (esUsuarioLogueado) { %>
    <p><strong><%= totalReportes %></strong> mascotas perdidas de otros usuarios necesitan tu ayuda</p>
    <% } else { %>
    <p><strong><%= totalReportes %></strong> reportes totales | <strong><%= reportesActivos %></strong> mascotas aún perdidas</p>
    <% } %>
  </div>

  <!-- Acciones principales -->
  <div class="acciones-principales">
    <a href="/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeAdmin.jsp" class="btn btn-secondary">
      🏠 Volver a Admin
    </a>
  </div>

  <!-- Grid de reportes -->
  <% if (reportes != null && !reportes.isEmpty()) { %>
  <div class="reportes-grid">
    <%
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      for (ReporteConRelaciones reporteRel : reportes) {
        ReporteDesaparicion reporte = reporteRel.getReporte();
        Mascotas mascota = reporteRel.getMascota();
        Usuarios usuario = reporteRel.getUsuario();
        Especie especie = reporteRel.getEspecie();
        ImagenMascota imagen = reporteRel.getImagen();

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
    <div class="reporte-card">
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

        <!-- Badge de perdido -->
        <div class="perdido-badge">
          PERDIDO
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
          📅 <strong>Perdido el:</strong> <%= sdf.format(reporte.getFechaDesaparicion()) %>
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
      </div>

      <div class="contacto">
        <div class="contacto-info">
          <div class="contacto-datos">
            <div class="contacto-nombre">
              👤 <%= reporteRel.getNombreCompleto() %>
            </div>
            <% if (usuario.getTelefono() != null && !usuario.getTelefono().trim().isEmpty()) { %>
            <a href="tel:<%= usuario.getTelefono() %>" class="contacto-telefono">
              📞 <%= usuario.getTelefono() %>
            </a>
            <% } %>
          </div>
          <div class="fecha-reporte">
            Reportado el<br>
            <%= sdf.format(reporte.getFecha_Registro()) %>
          </div>
        </div>
      </div>
    </div>
    <% } %>
  </div>
  <% } else { %>
  <!-- Estado vacío -->
  <div class="empty-state">
    <div class="empty-state-icon">🐾</div>
    <% if (esUsuarioLogueado) { %>
    <h3>No hay reportes de otros usuarios</h3>
    <p>¡Excelente! Actualmente no hay otras mascotas reportadas como perdidas en tu área.
      Revisa tus propios reportes desde "Mis Reportes".</p>
    <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=mis_reportes" class="btn btn-primary">
      📋 Ver Mis Reportes
    </a>
    <% } else { %>
    <h3>No hay reportes de mascotas perdidas</h3>
    <p>¡Que buena noticia! Actualmente no hay mascotas reportadas como perdidas.</p>
    <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=registrar" class="btn btn-danger">
      🚨 Reportar Mascota Perdida
    </a>
    <% } %>
  </div>
  <% } %>

</div>
</body>
</html>