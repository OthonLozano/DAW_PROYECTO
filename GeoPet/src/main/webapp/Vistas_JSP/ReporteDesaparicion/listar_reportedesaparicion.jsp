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
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            min-height: 100vh;
        }

        .main-header {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
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
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="lost-pets" width="40" height="40" patternUnits="userSpaceOnUse"><g fill="rgba(255,255,255,0.1)"><circle cx="20" cy="15" r="3"/><path d="M15 20h10v2h-10z"/><path d="M17 25h6v1h-6z"/></g></pattern></defs><rect width="100" height="100" fill="url(%23lost-pets)"/></svg>');
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
            border-top: 4px solid #dc3545;
            transition: all 0.3s ease;
        }

        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
        }

        .stats-title {
            color: #dc3545;
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
            color: #dc3545;
            font-weight: bold;
            font-size: 1.25rem;
        }

        .btn-main {
            background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 0.875rem 2rem;
            border-radius: 12px;
            font-size: 1rem;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            box-shadow: 0 4px 15px rgba(0, 123, 255, 0.3);
            margin: 0.5rem;
        }

        .btn-main:hover {
            background: linear-gradient(135deg, #0056b3 0%, #004085 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 123, 255, 0.4);
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
            border-left: 5px solid #dc3545;
        }

        .reporte-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
        }

        .click-hint {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: rgba(220, 53, 69, 0.95);
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
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 4rem;
        }

        .perdido-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 25px;
            font-size: 0.75rem;
            font-weight: bold;
            text-transform: uppercase;
            z-index: 5;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
            animation: pulse 2s infinite;
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
            color: #dc3545;
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
            color: #dc3545;
        }

        .empty-state h3 {
            color: #dc3545;
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
            border-top: 4px solid #dc3545;
        }

        .footer-title {
            color: #dc3545;
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 1rem;
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

            .btn-main,
            .btn-secondary-main {
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
                <i class="bi bi-exclamation-triangle-fill me-3"></i>Mascotas Perdidas
            </h1>
            <%
                Boolean usuarioLogueado = (Boolean) request.getAttribute("usuarioLogueado");
                boolean esUsuarioLogueado = usuarioLogueado != null && usuarioLogueado;
            %>
            <% if (esUsuarioLogueado) { %>
            <p class="header-subtitle">Mascotas perdidas reportadas por otros usuarios - Ayúdalos a encontrar a sus compañeros</p>
            <% } else { %>
            <p class="header-subtitle">Ayuda a reunir familias con sus mascotas perdidas</p>
            <% } %>
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
    <div class="stats-card">
        <h3 class="stats-title">
            <i class="bi bi-graph-up"></i>Estadísticas de Reportes
        </h3>
        <div class="stats-content">
            <% if (esUsuarioLogueado) { %>
            <p><span class="stats-number"><%= totalReportes %></span> mascotas perdidas de otros usuarios necesitan tu ayuda</p>
            <% } else { %>
            <p><span class="stats-number"><%= totalReportes %></span> reportes totales | <span class="stats-number"><%= reportesActivos %></span> mascotas aún perdidas</p>
            <% } %>
        </div>
    </div>

    <!-- Acciones principales -->
    <div class="text-center mb-4">
        <a href="/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp" class="btn-secondary-main">
            <i class="bi bi-house me-2"></i>Ir al Inicio
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
        <div class="reporte-card" onclick="location.href='ReporteDesaparicionServlet?accion=detalle&id=<%= reporte.getReporteID() %>'">
            <div class="click-hint">
                <i class="bi bi-eye me-1"></i>Ver detalles
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

                <!-- Badge de perdido -->
                <div class="perdido-badge">
                    <i class="bi bi-exclamation-triangle me-1"></i>PERDIDO
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
            </div>
        </div>
        <% } %>
    </div>
    <% } else { %>
    <!-- Estado vacío -->
    <div class="empty-state">
        <div class="empty-state-icon">
            <i class="bi bi-heart-fill"></i>
        </div>
        <% if (esUsuarioLogueado) { %>
        <h3>No hay reportes de otros usuarios</h3>
        <p>¡Excelente! Actualmente no hay otras mascotas reportadas como perdidas en tu área.
            Revisa tus propios reportes desde "Mis Reportes".</p>
        <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=mis_reportes" class="btn-main">
            <i class="bi bi-file-text me-2"></i>Ver Mis Reportes
        </a>
        <% } else { %>
        <h3>No hay reportes de mascotas perdidas</h3>
        <p>¡Que buena noticia! Actualmente no hay mascotas reportadas como perdidas.</p>
        <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=registrar" class="btn-main">
            <i class="bi bi-plus-circle me-2"></i>Reportar Mascota Perdida
        </a>
        <% } %>
    </div>
    <% } %>

    <!-- Footer informativo -->
    <div class="footer-info">
        <h4 class="footer-title">
            <i class="bi bi-lightbulb-fill me-2"></i>¿Encontraste una de estas mascotas?
        </h4>
        <div class="footer-content">
            <% if (esUsuarioLogueado) { %>
            <p>Si encontraste una mascota que coincide con alguno de estos reportes,
                contacta inmediatamente al número de teléfono indicado.
                Tu ayuda puede reunir a una familia con su querida mascota.</p>
            <div class="row g-3 mt-3">
                <div class="col-md-4">
                    <div class="d-flex align-items-center justify-content-center">
                        <i class="bi bi-cursor-fill text-danger me-2 fs-5"></i>
                        <div>
                            <strong>Nota:</strong><br>
                            <small class="text-muted">Tus propios reportes no aparecen aquí. Para gestionarlos, ve a "Mis Reportes".</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="d-flex align-items-center justify-content-center">
                        <i class="bi bi-eye-fill text-primary me-2 fs-5"></i>
                        <div>
                            <strong>Consejo:</strong><br>
                            <small class="text-muted">Haz clic en cualquier tarjeta para ver más detalles de la mascota.</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="d-flex align-items-center justify-content-center">
                        <i class="bi bi-heart-fill text-success me-2 fs-5"></i>
                        <div>
                            <strong>Ayuda:</strong><br>
                            <small class="text-muted">Tu ayuda puede reunir a una familia con su querida mascota.</small>
                        </div>
                    </div>
                </div>
            </div>
            <% } else { %>
            <p>Si encontraste una mascota que coincide con alguno de estos reportes,
                contacta inmediatamente al número de teléfono indicado.
                Tu ayuda puede reunir a una familia con su querida mascota.</p>
            <div class="row g-3 mt-3">
                <div class="col-md-6">
                    <div class="d-flex align-items-center justify-content-center">
                        <i class="bi bi-eye-fill text-primary me-2 fs-5"></i>
                        <div>
                            <strong>Consejo:</strong><br>
                            <small class="text-muted">Haz clic en cualquier tarjeta para ver más detalles de la mascota.</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="d-flex align-items-center justify-content-center">
                        <i class="bi bi-heart-fill text-success me-2 fs-5"></i>
                        <div>
                            <strong>Ayuda:</strong><br>
                            <small class="text-muted">Muchas mascotas son encontradas gracias a reportes como estos.</small>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>
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
    });

    // Mejorar interacciones de las cards
    function enhanceCardInteractions() {
        allCards.forEach(card => {
            // Hacer las tarjetas focusables para accesibilidad
            card.setAttribute('tabindex', '0');
            card.setAttribute('role', 'button');
            card.setAttribute('aria-label', 'Ver detalles del reporte');

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

    // Funcionalidad de filtrado rápido
    function initializeQuickFilter() {
        if (allCards.length > 6) {
            const filterContainer = document.createElement('div');
            filterContainer.className = 'text-center mb-4';
            filterContainer.innerHTML = `
                <div class="btn-group" role="group" aria-label="Filtros de mascotas perdidas">
                    <button type="button" class="btn btn-outline-danger active" data-filter="all">
                        <i class="bi bi-grid me-1"></i>Todas las mascotas
                    </button>
                    <button type="button" class="btn btn-outline-primary" data-filter="perros">
                        <i class="bi bi-heart me-1"></i>Perros
                    </button>
                    <button type="button" class="btn btn-outline-info" data-filter="gatos">
                        <i class="bi bi-heart-fill me-1"></i>Gatos
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

                            if (filter !== 'all') {
                                const especieText = card.querySelector('.mascota-especie').textContent.toLowerCase();
                                if (filter === 'perros') {
                                    show = especieText.includes('perro') || especieText.includes('canino');
                                } else if (filter === 'gatos') {
                                    show = especieText.includes('gato') || especieText.includes('felino');
                                }
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

    // Función para mostrar toasts personalizados
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



    // Atajos de teclado para acciones rápidas
    document.addEventListener('keydown', function(e) {
        if (e.ctrlKey || e.metaKey) {
            switch(e.key) {
                case 'h':
                    e.preventDefault();
                    window.location.href = '/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp';
                    break;
                case 'r':
                    e.preventDefault();
                    location.reload();
                    break;
            }
        }
    });

    // Inicializar funcionalidades adicionales al cargar
    window.addEventListener('load', function() {
        // Añadir filtros si hay muchas mascotas
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

    // Efecto de parallax suave en el header
    window.addEventListener('scroll', function() {
        const scrolled = window.pageYOffset;
        const header = document.querySelector('.main-header');
        if (header) {
            header.style.transform = `translateY(${scrolled * 0.2}px)`;
        }
    });

    // Añadir efectos de sonido suaves (opcional)
    function playClickSound() {
        try {
            const audioContext = new (window.AudioContext || window.webkitAudioContext)();
            const oscillator = audioContext.createOscillator();
            const gainNode = audioContext.createGain();

            oscillator.connect(gainNode);
            gainNode.connect(audioContext.destination);

            oscillator.frequency.setValueAtTime(800, audioContext.currentTime);
            gainNode.gain.setValueAtTime(0.02, audioContext.currentTime);
            gainNode.gain.exponentialRampToValueAtTime(0.001, audioContext.currentTime + 0.1);

            oscillator.start(audioContext.currentTime);
            oscillator.stop(audioContext.currentTime + 0.1);
        } catch (e) {
            // Ignorar errores de audio
        }
    }

    // Añadir sonido a las cards al hacer click
    allCards.forEach(card => {
        card.addEventListener('click', function() {
            playClickSound();
        });
    });
</script>
</body>
</html>