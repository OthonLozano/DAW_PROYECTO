<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="Modelo.JavaBeans.ReporteConRelaciones" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>
<%@ page import="Modelo.JavaBeans.ReporteDesaparicion" %>
<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<%@ page import="Modelo.JavaBeans.ImagenMascota" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Mascota - <%= request.getAttribute("reporteCompleto") != null ? ((ReporteConRelaciones)request.getAttribute("reporteCompleto")).getMascota().getNombre() : "Detalle" %> - GeoPet</title>
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
        }

        .main-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            padding: 1.5rem 0;
            margin-bottom: 2rem;
        }

        .back-btn {
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            text-decoration: none;
            transition: all 0.3s ease;
            backdrop-filter: blur(10px);
        }

        .back-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        }

        .owner-badge {
            background: linear-gradient(135deg, #4caf50, #66bb6a);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-size: 0.875rem;
            font-weight: bold;
            box-shadow: 0 2px 10px rgba(76, 175, 80, 0.3);
        }

        .status-banner {
            background: linear-gradient(135deg, #2196f3, #42a5f5);
            color: white;
            padding: 1rem;
            border-radius: 10px;
            font-weight: bold;
            font-size: 1.1rem;
            text-align: center;
            margin-bottom: 1.5rem;
            box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
        }

        .breadcrumb-geopet {
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .breadcrumb {
            margin-bottom: 0;
        }

        .breadcrumb-item a {
            color: #2e7d32;
            text-decoration: none;
        }

        .status-banner {
            background: linear-gradient(135deg, #2196f3, #42a5f5);
            color: white;
            padding: 1rem;
            border-radius: 10px;
            font-weight: bold;
            font-size: 1.1rem;
            text-align: center;
            margin-bottom: 1.5rem;
            box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);
        }

        .stats-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
            text-align: center;
            transition: all 0.3s ease;
            border-top: 4px solid #2e7d32;
        }

        .stats-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        }

        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            color: #2196f3;
            margin-bottom: 0.5rem;
        }

        .stat-label {
            color: #666;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .location-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
            margin-bottom: 2rem;
            border-top: 4px solid #2e7d32;
        }

        .location-label {
            color: #2196f3;
            font-size: 0.875rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
        }

        .location-value {
            color: #333;
            font-size: 1rem;
            font-weight: 500;
        }

        .pet-image-card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            position: sticky;
            top: 20px;
            margin-bottom: 2rem;
        }

        .pet-image {
            width: 100%;
            height: 400px;
            object-fit: cover;
        }

        .pet-image-placeholder {
            width: 100%;
            height: 400px;
            background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: #666;
            font-size: 1.125rem;
        }

        .image-actions {
            padding: 1rem;
            border-top: 1px solid #e9ecef;
            background-color: #f8f9fa;
        }

        .pet-info-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            padding: 2rem;
            margin-bottom: 2rem;
        }

        .pet-title {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 1.5rem;
            color: #2e7d32;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .gender-symbol {
            padding: 0.5rem;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            font-weight: bold;
        }

        .gender-macho {
            background-color: rgba(33, 150, 243, 0.2);
            color: #2196f3;
        }

        .gender-hembra {
            background-color: rgba(233, 30, 99, 0.2);
            color: #e91e63;
        }

        .quick-actions {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            border-left: 4px solid #2e7d32;
        }

        .action-btn {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            transition: all 0.3s ease;
        }

        .action-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
            color: white;
        }

        .action-btn.btn-warning-style {
            background: linear-gradient(135deg, #ffc107, #ffb300);
            color: #212529;
        }

        .action-btn.btn-warning-style:hover {
            background: linear-gradient(135deg, #e0a800, #ff8f00);
            color: #212529;
        }

        .action-btn.btn-info-style {
            background: linear-gradient(135deg, #17a2b8, #20c997);
        }

        .nav-tabs-geopet {
            border-bottom: 2px solid #e9ecef;
            margin-bottom: 1.5rem;
        }

        .nav-tabs-geopet .nav-link {
            border: none;
            color: #6c757d;
            font-weight: 600;
            padding: 1rem 1.5rem;
            border-bottom: 3px solid transparent;
            transition: all 0.3s ease;
        }

        .nav-tabs-geopet .nav-link:hover {
            background-color: #f8f9fa;
            color: #2e7d32;
        }

        .nav-tabs-geopet .nav-link.active {
            color: #2e7d32;
            border-bottom-color: #2e7d32;
            background-color: transparent;
        }

        .info-item {
            margin-bottom: 1rem;
        }

        .info-label {
            font-weight: bold;
            color: #666;
            font-size: 0.875rem;
            margin-bottom: 0.25rem;
        }

        .info-value {
            font-size: 1rem;
            color: #333;
        }

        .status-badge {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: bold;
            text-transform: uppercase;
        }

        .status-active {
            background-color: #28a745;
            color: white;
        }

        .status-closed {
            background-color: #6c757d;
            color: white;
        }

        .status-found {
            background-color: #17a2b8;
            color: white;
        }

        .section-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
            margin-bottom: 2rem;
        }

        .section-title {
            font-size: 1.25rem;
            font-weight: bold;
            color: #2e7d32;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .count-badge {
            background-color: #2196f3;
            color: white;
            padding: 0.25rem 0.5rem;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: bold;
        }

        .comment-item {
            background-color: #f8f9fa;
            border-left: 4px solid #2196f3;
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 0 8px 8px 0;
            transition: all 0.3s ease;
        }

        .comment-item:hover {
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .avistamiento-item {
            background-color: #fff5f5;
            border-left: 4px solid #ff6b6b;
            padding: 1.25rem;
            margin-bottom: 1.25rem;
            border-radius: 0 8px 8px 0;
            transition: all 0.3s ease;
        }

        .avistamiento-item:hover {
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .contact-btn {
            background: linear-gradient(135deg, #28a745, #34ce57);
            color: white;
            border: none;
            padding: 0.375rem 0.75rem;
            border-radius: 4px;
            font-size: 0.75rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .contact-btn:hover {
            background: linear-gradient(135deg, #218838, #28a745);
            transform: translateY(-1px);
        }

        .alert-geopet {
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 1.5rem;
            border: none;
        }

        .alert-success-geopet {
            background-color: #d4edda;
            color: #155724;
            border-left: 4px solid #28a745;
        }

        .alert-danger-geopet {
            background-color: #f8d7da;
            color: #721c24;
            border-left: 4px solid #dc3545;
        }

        .empty-state {
            text-align: center;
            color: #666;
            padding: 3rem 2rem;
        }

        .empty-state-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        .modal-geopet {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 1050;
            backdrop-filter: blur(5px);
        }

        .modal-content-geopet {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            width: 90%;
            max-width: 500px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            animation: modalSlideIn 0.3s ease-out;
        }

        @keyframes modalSlideIn {
            from {
                opacity: 0;
                transform: translateY(-50px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .btn-primary-geopet {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-primary-geopet:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
            background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
        }

        .btn-secondary-geopet {
            background: #6c757d;
            border: none;
            color: white;
            font-weight: 600;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-secondary-geopet:hover {
            background: #5a6268;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
        }

        .form-control:focus {
            border-color: #2e7d32;
            box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
        }

        @media (max-width: 768px) {
            .pet-title {
                font-size: 1.5rem;
            }

            .pet-image-card {
                position: relative;
                top: auto;
            }

            .quick-actions .row > div {
                margin-bottom: 0.5rem;
            }
        }
    </style>
</head>
<body>
<%
    // Obtener datos del request usando ReporteConRelaciones
    ReporteConRelaciones reporteCompleto = (ReporteConRelaciones) request.getAttribute("reporteCompleto");

    if (reporteCompleto == null || reporteCompleto.getReporte() == null || reporteCompleto.getMascota() == null) {
        response.sendRedirect("ReporteDesaparicionServlet?accion=mis_reportes");
        return;
    }

    // Extraer objetos individuales para facilitar el uso
    ReporteDesaparicion reporte = reporteCompleto.getReporte();
    Mascotas mascota = reporteCompleto.getMascota();
    Usuarios propietario = reporteCompleto.getUsuario();
    Especie especie = reporteCompleto.getEspecie();
    ImagenMascota imagen = reporteCompleto.getImagen();

    // Usuario logueado (el propietario)
    Integer usuarioLogueado = (Integer) request.getAttribute("usuarioLogueado");
%>

<!-- Header principal -->
<div class="main-header">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6">
                <a href="ReporteDesaparicionServlet?accion=mis_reportes" class="back-btn">
                    <i class="bi bi-arrow-left me-2"></i>Volver a Mis Reportes
                </a>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <!-- Status Banner -->
    <div class="status-banner">
        <i class="bi bi-gear me-2"></i>Gestionar Reporte de <%= mascota.getNombre() %>
    </div>

    <!-- Stats Section -->
    <div class="row g-4 mb-4">
        <div class="col-md-4">
            <div class="stats-card">
                <div class="stat-number"><%= request.getAttribute("totalComentarios") != null ? request.getAttribute("totalComentarios") : 0 %></div>
                <div class="stat-label">
                    <i class="bi bi-chat-dots me-1"></i>Comentarios Recibidos
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card">
                <div class="stat-number"><%= request.getAttribute("totalAvistamientos") != null ? request.getAttribute("totalAvistamientos") : 0 %></div>
                <div class="stat-label">
                    <i class="bi bi-eye me-1"></i>Avistamientos Reportados
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card">
                <div class="stat-number">
                    <%
                        java.util.Date fechaDesaparicion = reporte.getFechaDesaparicion();
                        java.util.Date fechaActual = new java.util.Date();
                        long diffInMillies = fechaActual.getTime() - fechaDesaparicion.getTime();
                        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
                    %>
                    <%= diffInDays %>
                </div>
                <div class="stat-label">
                    <i class="bi bi-calendar-x me-1"></i>Días Perdido
                </div>
            </div>
        </div>
    </div>


    <div class="status-banner">
        <i class="bi bi-geo-alt-fill me-2"></i>Domicilio de la mascota
    </div>

    <!-- Location Info -->
    <div class="location-card">
        <div class="row g-4">
            <div class="col-md-6">
                <div class="location-label">Ciudad</div>
                <div class="location-value"><%= propietario != null && propietario.getCiudad() != null ? propietario.getCiudad() : "No especificado" %></div>
            </div>
            <div class="col-md-6">
                <div class="location-label">Dirección</div>
                <div class="location-value"><%= propietario != null && propietario.getDireccion() != null ? propietario.getDireccion() : "No especificado" %></div>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="row g-4">
        <!-- Pet Image -->
        <div class="col-lg-4">
            <div class="pet-image-card">
                <% if (reporteCompleto.tieneImagen()) { %>
                <img src="<%= request.getContextPath() %>/ImagenMascotaServlet?action=obtener&mascotaId=<%= mascota.getMascotaID() %>"
                     alt="<%= mascota.getNombre() %>"
                     class="pet-image">
                <% } else { %>
                <div class="pet-image-placeholder">
                    <i class="bi bi-image fs-1 me-2"></i>Sin foto disponible
                </div>
                <% } %>

            </div>
        </div>

        <!-- Pet Info -->
        <div class="col-lg-8">
            <div class="pet-info-card">
                <h1 class="pet-title">
                    <%= mascota.getNombre().toUpperCase() %>
                    <%
                        String genderClass = "";
                        String genderSymbol = "";
                        if (mascota.getSexo() != null && mascota.getSexo().equals("Macho")) {
                            genderClass = "gender-macho";
                            genderSymbol = "♂";
                        } else {
                            genderClass = "gender-hembra";
                            genderSymbol = "♀";
                        }
                    %>
                    <span class="gender-symbol <%= genderClass %>">
                        <%= genderSymbol %>
                    </span>
                </h1>

                <!-- Quick Actions -->
                <div class="quick-actions">
                    <h5 class="mb-3">
                        <i class="bi bi-lightning me-2"></i>Acciones Rápidas
                    </h5>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <a href="ReporteDesaparicionServlet?accion=editar&id=<%= reporte.getReporteID() %>"
                               class="action-btn btn-warning-style w-100">
                                <i class="bi bi-pencil-square"></i>Editar Información
                            </a>
                        </div>
                        <div class="col-md-6">
                            <a href="MascotaServlet?accion=editar&id=<%= mascota.getMascotaID() %>"
                               class="action-btn btn-info-style w-100">
                                <i class="bi bi-heart"></i>Editar Mascota
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Mensajes de éxito/error -->
                <% if (request.getAttribute("mensaje") != null) { %>
                <div class="alert alert-geopet alert-success-geopet">
                    <i class="bi bi-check-circle-fill me-2"></i><strong>Éxito:</strong> <%= request.getAttribute("mensaje") %>
                </div>
                <% } %>

                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-geopet alert-danger-geopet">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i><strong>Error:</strong> <%= request.getAttribute("error") %>
                </div>
                <% } %>

                <!-- Tabs -->
                <ul class="nav nav-tabs nav-tabs-geopet" id="petTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="general-tab" data-bs-toggle="tab" data-bs-target="#general"
                                type="button" role="tab">
                            <i class="bi bi-info-circle me-2"></i>Datos Generales
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="reporte-tab" data-bs-toggle="tab" data-bs-target="#reporte"
                                type="button" role="tab">
                            <i class="bi bi-file-text me-2"></i>Información del Reporte
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="interacciones-tab" data-bs-toggle="tab" data-bs-target="#interacciones"
                                type="button" role="tab">
                            <i class="bi bi-people me-2"></i>Comentarios y Avistamientos
                        </button>
                    </li>
                </ul>

                <!-- Tab Content -->
                <div class="tab-content" id="petTabsContent">
                    <!-- General Info Tab -->
                    <div class="tab-pane fade show active" id="general" role="tabpanel">
                        <div class="row g-3 mt-2">
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Nombre:</div>
                                    <div class="info-value"><%= mascota.getNombre() %></div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Edad:</div>
                                    <div class="info-value"><%= mascota.getEdad() %> meses</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Sexo:</div>
                                    <div class="info-value"><%= mascota.getSexo() != null ? mascota.getSexo() : "No especificado" %></div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Color:</div>
                                    <div class="info-value"><%= mascota.getColor() != null ? mascota.getColor() : "No especificado" %></div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Especie:</div>
                                    <div class="info-value"><%= especie != null ? especie.getNombre() : "No especificada" %></div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Microchip:</div>
                                    <div class="info-value"><%= mascota.isMicrochip() ? "Sí" : "No" %></div>
                                </div>
                            </div>
                            <% if (mascota.isMicrochip()) {
                                String numeroMicrochip = String.valueOf(mascota.getNumero_Microchip());
                                if (numeroMicrochip != null && !numeroMicrochip.equals("null") && !numeroMicrochip.trim().isEmpty()) { %>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Número de Microchip:</div>
                                    <div class="info-value"><%= numeroMicrochip %></div>
                                </div>
                            </div>
                            <% } } %>
                            <div class="col-md-6">
                                <div class="info-item">
                                    <div class="info-label">Estado del reporte:</div>
                                    <div class="info-value">
                                        <span class="status-badge <%= reporte.getEstadoReporte().equals("Activo") ? "status-active" :
                                            reporte.getEstadoReporte().equals("Encontrado") ? "status-found" : "status-closed" %>">
                                            <%= reporte.getEstadoReporte() %>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="info-item">
                                    <div class="info-label">Características distintivas:</div>
                                    <div class="info-value">
                                        <%= mascota.getCaracteristicasDistintivas() != null && !mascota.getCaracteristicasDistintivas().trim().isEmpty()
                                                ? mascota.getCaracteristicasDistintivas()
                                                : "No se especificaron características distintivas." %>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Report Info Tab -->
                    <div class="tab-pane fade" id="reporte" role="tabpanel">
                        <div class="mt-3">
                            <div class="info-item mb-3">
                                <div class="info-label">Fecha de desaparición:</div>
                                <div class="info-value"><%= reporte.getFechaDesaparicion() %></div>
                            </div>

                            <div class="info-item mb-3">
                                <div class="info-label">Ubicación donde se perdió:</div>
                                <div class="info-value"><%= reporte.getUbicacionUltimaVez() %></div>
                            </div>

                            <div class="info-item mb-3">
                                <div class="info-label">Descripción de la situación:</div>
                                <div class="info-value">
                                    <%= reporte.getDescripcionSituacion() != null && !reporte.getDescripcionSituacion().trim().isEmpty()
                                            ? reporte.getDescripcionSituacion()
                                            : "No se proporcionó descripción adicional." %>
                                </div>
                            </div>

                            <% if (reporte.getRecompensa() > 0) { %>
                            <div class="info-item mb-3">
                                <div class="info-label">Recompensa ofrecida:</div>
                                <div class="info-value text-success fw-bold fs-5">
                                    <i class="bi bi-currency-dollar me-1"></i>$<%= String.format("%.2f", reporte.getRecompensa()) %>
                                </div>
                            </div>
                            <% } %>

                            <div class="info-item mb-3">
                                <div class="info-label">Fecha del reporte:</div>
                                <div class="info-value"><%= reporte.getFecha_Registro() %></div>
                            </div>

                            <div class="info-item">
                                <div class="info-label">Estado actual:</div>
                                <div class="info-value">
                                    <span class="status-badge <%= reporte.getEstadoReporte().equals("Activo") ? "status-active" :
                                        reporte.getEstadoReporte().equals("Encontrado") ? "status-found" : "status-closed" %>">
                                        <%= reporte.getEstadoReporte() %>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Interactions Tab -->
                    <div class="tab-pane fade" id="interacciones" role="tabpanel">
                        <div class="mt-3">
                            <!-- Sección de Comentarios -->
                            <div class="section-card">
                                <h4 class="section-title">
                                    <i class="bi bi-chat-dots"></i>Comentarios Recibidos
                                    <span class="count-badge">
                                        <%= request.getAttribute("totalComentarios") != null ? request.getAttribute("totalComentarios") : 0 %>
                                    </span>
                                </h4>

                                <!-- Lista de comentarios -->
                                <%
                                    @SuppressWarnings("unchecked")
                                    java.util.List<Modelo.JavaBeans.ComentariosConRelaciones> comentarios =
                                            (java.util.List<Modelo.JavaBeans.ComentariosConRelaciones>) request.getAttribute("comentarios");

                                    if (comentarios != null && !comentarios.isEmpty()) {
                                %>
                                <div class="comentarios-lista">
                                    <% for (Modelo.JavaBeans.ComentariosConRelaciones comentarioRel : comentarios) { %>
                                    <div class="comment-item">
                                        <div class="d-flex justify-content-between align-items-start mb-2">
                                            <div>
                                                <strong class="text-primary fs-6">
                                                    <i class="bi bi-person-circle me-1"></i><%= comentarioRel.getNombreCompleto() %>
                                                </strong>
                                                <small class="text-muted d-block mt-1">
                                                    <i class="bi bi-clock me-1"></i>
                                                    <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(comentarioRel.getComentario().getFecha_Comentario()) %>
                                                </small>
                                            </div>
                                        </div>
                                        <p class="mb-0 text-dark lh-base">
                                            <%= comentarioRel.getComentario().getContenido() %>
                                        </p>
                                    </div>
                                    <% } %>
                                </div>
                                <% } else { %>
                                <div class="empty-state">
                                    <div class="empty-state-icon">
                                        <i class="bi bi-chat-dots"></i>
                                    </div>
                                    <h5 class="mb-2">Aún no hay comentarios</h5>
                                    <p class="mb-0">Los comentarios de otras personas aparecerán aquí.</p>
                                </div>
                                <% } %>
                            </div>

                            <!-- Sección de Avistamientos -->
                            <div class="section-card">
                                <h4 class="section-title">
                                    <i class="bi bi-eye"></i>Avistamientos Reportados
                                    <span class="count-badge" style="background-color: #ff6b6b;">
                                        <%= request.getAttribute("totalAvistamientos") != null ? request.getAttribute("totalAvistamientos") : 0 %>
                                    </span>
                                </h4>

                                <!-- Lista de avistamientos -->
                                <%
                                    @SuppressWarnings("unchecked")
                                    java.util.List<Modelo.JavaBeans.AvistamientoConRelaciones> avistamientos =
                                            (java.util.List<Modelo.JavaBeans.AvistamientoConRelaciones>) request.getAttribute("avistamientos");

                                    if (avistamientos != null && !avistamientos.isEmpty()) {
                                %>
                                <div class="avistamientos-lista">
                                    <% for (Modelo.JavaBeans.AvistamientoConRelaciones avistamientoRel : avistamientos) { %>
                                    <div class="avistamiento-item">
                                        <div class="d-flex justify-content-between align-items-start mb-3">
                                            <div>
                                                <strong class="text-danger fs-6">
                                                    <i class="bi bi-person-circle me-1"></i><%= avistamientoRel.getNombreCompleto() %>
                                                </strong>
                                                <div class="text-muted small mt-2">
                                                    <div><i class="bi bi-calendar-check me-1"></i>Visto el:
                                                        <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(avistamientoRel.getAvistamiento().getFecha_Avistamiento()) %>
                                                    </div>
                                                    <div><i class="bi bi-geo-alt me-1"></i>Ubicación:
                                                        <%= avistamientoRel.getAvistamiento().getUbicacion() %>
                                                    </div>
                                                    <div><i class="bi bi-clock me-1"></i>Reportado el:
                                                        <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(avistamientoRel.getAvistamiento().getFecha_Registro()) %>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Botón para contactar al reportante -->
                                            <button onclick="contactarReportante('<%= avistamientoRel.getNombreCompleto() %>', '<%= avistamientoRel.getAvistamiento().getContacto() != null ? avistamientoRel.getAvistamiento().getContacto() : "" %>')"
                                                    class="contact-btn">
                                                <i class="bi bi-telephone me-1"></i>Contactar
                                            </button>
                                        </div>

                                        <div class="bg-white p-3 rounded mb-2">
                                            <p class="mb-0 text-dark lh-base">
                                                <strong>Descripción:</strong><br>
                                                <%= avistamientoRel.getAvistamiento().getDescripcion() %>
                                            </p>
                                        </div>

                                        <% if (avistamientoRel.getAvistamiento().getContacto() != null && !avistamientoRel.getAvistamiento().getContacto().trim().isEmpty()) { %>
                                        <div class="bg-info bg-opacity-10 p-2 rounded border border-info border-opacity-25">
                                            <small class="text-info-emphasis">
                                                <i class="bi bi-telephone me-1"></i><strong>Contacto:</strong>
                                                <%= avistamientoRel.getAvistamiento().getContacto() %>
                                            </small>
                                        </div>
                                        <% } %>
                                    </div>
                                    <% } %>
                                </div>
                                <% } else { %>
                                <div class="empty-state">
                                    <div class="empty-state-icon">
                                        <i class="bi bi-eye-slash"></i>
                                    </div>
                                    <h5 class="mb-2">Aún no hay avistamientos reportados</h5>
                                    <p class="mb-0">Los avistamientos de otras personas aparecerán aquí.</p>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal para cambiar imagen -->
<div id="modalImagen" class="modal-geopet" style="display: none;">
    <div class="modal-content-geopet">
        <h3 class="mb-4">
            <i class="bi bi-camera me-2"></i>Cambiar Foto de <%= mascota.getNombre() %>
        </h3>
        <form action="ImagenMascotaServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="actualizar">
            <input type="hidden" name="mascotaId" value="<%= mascota.getMascotaID() %>">
            <input type="hidden" name="reporteId" value="<%= reporte.getReporteID() %>">

            <div class="mb-4">
                <label class="form-label fw-bold">
                    <i class="bi bi-image me-1"></i>Seleccionar nueva imagen:
                </label>
                <input type="file" name="imagen" accept="image/*" required class="form-control">
                <div class="form-text">
                    Formatos soportados: JPG, PNG, GIF (máx. 5MB)
                </div>
            </div>

            <div class="d-flex gap-3 justify-content-end">
                <button type="button" onclick="cerrarModalImagen()" class="btn btn-secondary-geopet">
                    <i class="bi bi-x-circle me-1"></i>Cancelar
                </button>
                <button type="submit" class="btn btn-primary-geopet">
                    <i class="bi bi-check-circle me-1"></i>Actualizar Foto
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Función para cambiar estado del reporte
    function cambiarEstadoReporte(nuevoEstado) {
        const mensajes = {
            'Activo': '¿Reactivar la búsqueda de tu mascota?',
            'Pausado': '¿Pausar temporalmente la búsqueda?',
            'Cerrado': '¿Cerrar definitivamente este reporte?',
            'Encontrado': '¡Excelente! ¿Confirmas que encontraste a tu mascota?'
        };

        if (confirm(mensajes[nuevoEstado])) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'ReporteDesaparicionServlet';

            const accion = document.createElement('input');
            accion.type = 'hidden';
            accion.name = 'accion';
            accion.value = 'cambiar_estado';

            const reporteId = document.createElement('input');
            reporteId.type = 'hidden';
            reporteId.name = 'reporteId';
            reporteId.value = '<%= reporte.getReporteID() %>';

            const estado = document.createElement('input');
            estado.type = 'hidden';
            estado.name = 'nuevoEstado';
            estado.value = nuevoEstado;

            form.appendChild(accion);
            form.appendChild(reporteId);
            form.appendChild(estado);

            document.body.appendChild(form);
            form.submit();
        }
    }



    function contactarReportante(nombre, contacto) {
        let mensaje = `Información de contacto para ${nombre}:`;

        if (contacto && contacto.trim() !== '') {
            mensaje += `\n\nContacto: ${contacto}`;
        } else {
            mensaje += `\n\nEsta persona no proporcionó información de contacto adicional.`;
            mensaje += `\nPuedes intentar contactar a través del administrador del sistema.`;
        }

        alert(mensaje);
    }

    // Cerrar modal si se hace clic fuera de él
    document.getElementById('modalImagen').addEventListener('click', function(e) {
        if (e.target === this) {
            cerrarModalImagen();
        }
    });

    // Cerrar modal con tecla Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            cerrarModalImagen();
        }
    });

    // Mostrar notificación de bienvenida
    window.addEventListener('load', function() {
        const estado = '<%= reporte.getEstadoReporte() %>';
        const totalAvistamientos = <%= request.getAttribute("totalAvistamientos") != null ? request.getAttribute("totalAvistamientos") : 0 %>;
        const totalComentarios = <%= request.getAttribute("totalComentarios") != null ? request.getAttribute("totalComentarios") : 0 %>;

        if (totalAvistamientos > 0 || totalComentarios > 0) {
            setTimeout(() => {
                if (totalAvistamientos > 0) {
                    // Crear una notificación más elegante
                    const notification = document.createElement('div');
                    notification.style.cssText = `
                        position: fixed;
                        top: 20px;
                        right: 20px;
                        background: linear-gradient(135deg, #28a745, #34ce57);
                        color: white;
                        padding: 1rem 1.5rem;
                        border-radius: 10px;
                        box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
                        z-index: 1060;
                        animation: slideInRight 0.5s ease-out;
                        max-width: 300px;
                    `;

                    notification.innerHTML = `
                        <div style="display: flex; align-items: center; gap: 0.5rem;">
                            <i class="bi bi-eye-fill" style="font-size: 1.2rem;"></i>
                            <div>
                                <strong>¡Tienes ${totalAvistamientos} avistamiento(s)!</strong>
                                <div style="font-size: 0.875rem; margin-top: 0.25rem;">
                                    Revisa la pestaña "Comentarios y Avistamientos"
                                </div>
                            </div>
                            <button onclick="this.parentElement.parentElement.remove()"
                                    style="background: none; border: none; color: white; font-size: 1.2rem; cursor: pointer; margin-left: auto;">
                                ×
                            </button>
                        </div>
                    `;

                    // Agregar animación CSS
                    const style = document.createElement('style');
                    style.textContent = `
                        @keyframes slideInRight {
                            from { transform: translateX(100%); opacity: 0; }
                            to { transform: translateX(0); opacity: 1; }
                        }
                    `;
                    document.head.appendChild(style);

                    document.body.appendChild(notification);

                    // Auto-remover después de 5 segundos
                    setTimeout(() => {
                        if (notification.parentElement) {
                            notification.remove();
                        }
                    }, 5000);
                }
            }, 1000);
        }
    });
</script>
</body>
</html>