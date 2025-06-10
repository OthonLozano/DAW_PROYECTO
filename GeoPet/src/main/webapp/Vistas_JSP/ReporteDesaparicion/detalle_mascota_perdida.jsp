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
    <title>Mascota Perdida - GeoPet</title>
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
            background: linear-gradient(135deg, #ff6b6b, #ff8e53);
            color: white;
            padding: 1rem;
            border-radius: 10px;
            font-weight: bold;
            font-size: 1.1rem;
            text-align: center;
            margin-bottom: 1.5rem;
            box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
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
            color: #ff6b6b;
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
            background-color: #ff6b6b;
            color: white;
        }

        .status-closed {
            background-color: #6c757d;
            color: white;
        }

        .contact-card {
            background: linear-gradient(135deg, #e8f5e8 0%, #f1f8e9 100%);
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            border-left: 4px solid #2e7d32;
        }

        .reward-card {
            background: linear-gradient(135deg, #4caf50, #66bb6a);
            color: white;
            padding: 1.5rem;
            border-radius: 15px;
            text-align: center;
            margin-bottom: 1.5rem;
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
        }

        .contact-btn {
            background: linear-gradient(135deg, #2196f3, #42a5f5);
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: bold;
            cursor: pointer;
            width: 100%;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .contact-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);
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

        .form-card {
            background-color: #f8f9fa;
            padding: 1.5rem;
            border-radius: 10px;
            margin-bottom: 1.5rem;
            border: 1px solid #e9ecef;
        }

        .form-control:focus {
            border-color: #2e7d32;
            box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
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

        .btn-warning-geopet {
            background: linear-gradient(135deg, #ff6b6b, #ff8e53);
            border: none;
            color: white;
            font-weight: 600;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-warning-geopet:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
            background: linear-gradient(135deg, #ff5252, #ff7043);
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

        .btn-sm-geopet {
            padding: 0.375rem 0.75rem;
            font-size: 0.75rem;
            border-radius: 4px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-edit {
            background-color: #ffc107;
            color: #212529;
            border: none;
        }

        .btn-edit:hover {
            background-color: #e0a800;
            color: #212529;
            transform: translateY(-1px);
        }

        .btn-delete {
            background-color: #dc3545;
            color: white;
            border: none;
        }

        .btn-delete:hover {
            background-color: #c82333;
            color: white;
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

        .alert-warning-geopet {
            background-color: #fff3cd;
            color: #856404;
            border-left: 4px solid #ffc107;
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

        @media (max-width: 768px) {
            .pet-title {
                font-size: 1.5rem;
            }

            .pet-image-card {
                position: relative;
                top: auto;
            }
        }
    </style>
</head>
<body>
<%
    // Obtener datos del request usando ReporteConRelaciones
    ReporteConRelaciones reporteCompleto = (ReporteConRelaciones) request.getAttribute("reporteCompleto");

    if (reporteCompleto == null || reporteCompleto.getReporte() == null || reporteCompleto.getMascota() == null) {
        response.sendRedirect("ReporteDesaparicionServlet?accion=listar");
        return;
    }

    // Extraer objetos individuales para facilitar el uso
    ReporteDesaparicion reporte = reporteCompleto.getReporte();
    Mascotas mascota = reporteCompleto.getMascota();
    Usuarios propietario = reporteCompleto.getUsuario();
    Especie especie = reporteCompleto.getEspecie();
    ImagenMascota imagen = reporteCompleto.getImagen();

    // Declarar usuarioLogueado aquí para que esté disponible en todo el JSP
    Integer usuarioLogueado = (Integer) request.getAttribute("usuarioLogueado");
%>

<!-- Header principal -->
<div class="main-header">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-6">
                <a href="ReporteDesaparicionServlet?accion=listar" class="back-btn">
                    <i class="bi bi-arrow-left me-2"></i>Volver a la lista
                </a>
            </div>
            <div class="col-md-6 text-md-end">
                <h1 class="h3 mb-0 text-white">
                    <i class="bi bi-heart-fill me-2"></i>Mascota Perdida
                </h1>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <!-- Status Banner -->
    <div class="status-banner">
        <i class="bi bi-geo-alt-fill me-2"></i>Domicilio de la mascota
    </div>

    <!-- Location Info -->
    <div class="location-card">
        <div class="row g-4">
            <div class="col-md-4">
                <div class="location-label">Ciudad</div>
                <div class="location-value"><%= propietario != null && propietario.getCiudad() != null ? propietario.getCiudad() : "No especificado" %></div>
            </div>
            <div class="col-md-4">
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

                <!-- Tabs -->
                <ul class="nav nav-tabs nav-tabs-geopet" id="petTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="general-tab" data-bs-toggle="tab" data-bs-target="#general"
                                type="button" role="tab">
                            <i class="bi bi-info-circle me-2"></i>Datos Generales
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="disappearance-tab" data-bs-toggle="tab" data-bs-target="#disappearance"
                                type="button" role="tab">
                            <i class="bi bi-search me-2"></i>Información de desaparición
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
                                        <span class="status-badge <%= reporte.getEstadoReporte().equals("Activo") ? "status-active" : "status-closed" %>">
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

                    <!-- Disappearance Info Tab -->
                    <div class="tab-pane fade" id="disappearance" role="tabpanel">
                        <div class="mt-3">
                            <div class="info-item mb-3">
                                <div class="info-label">Ubicación de última vez:</div>
                                <div class="info-value"><%= reporte.getUbicacionUltimaVez() %></div>
                            </div>
                            <div class="info-item mb-3">
                                <div class="info-label">Fecha de desaparición:</div>
                                <div class="info-value"><%= reporte.getFechaDesaparicion() %></div>
                            </div>
                            <div class="info-item mb-3">
                                <div class="info-label">Descripción de la situación:</div>
                                <div class="info-value">
                                    <%= reporte.getDescripcionSituacion() != null && !reporte.getDescripcionSituacion().trim().isEmpty()
                                            ? reporte.getDescripcionSituacion()
                                            : "No se proporcionó descripción adicional." %>
                                </div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">Estatus de reporte:</div>
                                <div class="info-value">
                                    <span class="status-badge <%= reporte.getEstadoReporte().equals("Activo") ? "status-active" : "status-closed" %>">
                                        <%= reporte.getEstadoReporte() %>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Contact Section -->
                <div class="contact-card mt-4">
                    <h3 class="section-title">
                        <i class="bi bi-person-lines-fill"></i>Información de Contacto
                    </h3>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <strong>Propietario:</strong><br>
                            <%= reporteCompleto.getNombreCompleto() %>
                        </div>
                        <div class="col-md-6">
                            <strong>Teléfono:</strong><br>
                            <%= propietario != null && propietario.getTelefono() != null ? propietario.getTelefono() : "No proporcionado" %>
                        </div>
                        <div class="col-md-6">
                            <strong>Email:</strong><br>
                            <%= propietario != null && propietario.getEmail() != null ? propietario.getEmail() : "No proporcionado" %>
                        </div>
                        <div class="col-md-6">
                            <strong>Fecha del reporte:</strong><br>
                            <%= reporte.getFecha_Registro() %>
                        </div>
                    </div>
                </div>

                <% if (reporte.getRecompensa() > 0) { %>
                <div class="reward-card">
                    <h3 class="mb-0">
                        <i class="bi bi-currency-dollar me-2"></i>Recompensa: $<%= String.format("%.2f", reporte.getRecompensa()) %>
                    </h3>
                </div>
                <% } %>
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

    <!-- Sección de Comentarios -->
    <div class="section-card">
        <h3 class="section-title">
            <i class="bi bi-chat-dots"></i>Comentarios
            <span class="count-badge">
                <%= request.getAttribute("totalComentarios") != null ? request.getAttribute("totalComentarios") : 0 %>
            </span>
        </h3>

        <!-- Formulario para agregar comentario (solo si está logueado) -->
        <% if (usuarioLogueado != null) { %>
        <div class="form-card">
            <h5 class="mb-3">
                <i class="bi bi-plus-circle me-2"></i>Agregar comentario
            </h5>
            <form action="ComentariosServlet" method="post">
                <input type="hidden" name="accion" value="agregar">
                <input type="hidden" name="reporteId" value="<%= reporteCompleto.getReporte().getReporteID() %>">
                <div class="mb-3">
                    <textarea name="contenido" class="form-control" rows="4"
                              placeholder="Escribe tu comentario aquí..." required></textarea>
                </div>
                <button type="submit" class="btn btn-primary-geopet">
                    <i class="bi bi-send me-2"></i>Agregar Comentario
                </button>
            </form>
        </div>
        <% } else { %>
        <div class="alert alert-geopet alert-warning-geopet">
            <i class="bi bi-info-circle me-2"></i><strong>Información:</strong>
            <a href="login.jsp" class="text-decoration-none">Inicia sesión</a> para agregar comentarios.
        </div>
        <% } %>

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

                    <!-- Botones de editar/eliminar (solo para el propietario) -->
                    <% if (usuarioLogueado != null && usuarioLogueado.equals(comentarioRel.getComentario().getR_Usuario())) { %>
                    <div class="d-flex gap-2">
                        <button onclick="editarComentario(<%= comentarioRel.getComentario().getComentarioID() %>, '<%= comentarioRel.getComentario().getContenido().replace("'", "\\'").replace("\r", "").replace("\n", "\\n") %>')"
                                class="btn btn-sm-geopet btn-edit">
                            <i class="bi bi-pencil"></i> Editar
                        </button>
                        <button onclick="eliminarComentario(<%= comentarioRel.getComentario().getComentarioID() %>)"
                                class="btn btn-sm-geopet btn-delete">
                            <i class="bi bi-trash"></i> Eliminar
                        </button>
                    </div>
                    <% } %>
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
            <p class="mb-0">Sé el primero en comentar sobre este reporte.</p>
        </div>
        <% } %>
    </div>

    <!-- Sección de Avistamientos -->
    <div class="section-card">
        <h3 class="section-title">
            <i class="bi bi-eye"></i>Avistamientos
            <span class="count-badge" style="background-color: #ff6b6b;">
                <%= request.getAttribute("totalAvistamientos") != null ? request.getAttribute("totalAvistamientos") : 0 %>
            </span>
        </h3>

        <!-- Formulario para reportar avistamiento (solo si está logueado) -->
        <% if (usuarioLogueado != null) { %>
        <div class="form-card">
            <h5 class="mb-3">
                <i class="bi bi-plus-circle me-2"></i>Reportar avistamiento
            </h5>
            <form action="AvistamientoServlet" method="post">
                <input type="hidden" name="accion" value="agregar">
                <input type="hidden" name="reporteId" value="<%= reporteCompleto.getReporte().getReporteID() %>">

                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <label class="form-label fw-bold">
                            <i class="bi bi-calendar-event me-1"></i>Fecha del avistamiento:
                        </label>
                        <input type="date" name="fechaAvistamiento" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-bold">
                            <i class="bi bi-geo-alt me-1"></i>Ubicación:
                        </label>
                        <input type="text" name="ubicacion" class="form-control"
                               placeholder="Ej: Parque Central, cerca del lago" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">
                        <i class="bi bi-card-text me-1"></i>Descripción del avistamiento:
                    </label>
                    <textarea name="descripcion" class="form-control" rows="4"
                              placeholder="Describe lo que viste: comportamiento, estado, con quién estaba..." required></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">
                        <i class="bi bi-telephone me-1"></i>Contacto (opcional):
                    </label>
                    <input type="text" name="contacto" class="form-control"
                           placeholder="Tu teléfono o email para contacto directo">
                </div>

                <button type="submit" class="btn btn-warning-geopet">
                    <i class="bi bi-eye me-2"></i>Reportar Avistamiento
                </button>
            </form>
        </div>
        <% } else { %>
        <div class="alert alert-geopet alert-warning-geopet">
            <i class="bi bi-info-circle me-2"></i><strong>Información:</strong>
            <a href="login.jsp" class="text-decoration-none">Inicia sesión</a> para reportar avistamientos.
        </div>
        <% } %>

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

                    <!-- Botones de editar/eliminar (solo para el propietario) -->
                    <% if (usuarioLogueado != null && usuarioLogueado.equals(avistamientoRel.getAvistamiento().getR_UsuarioReportante())) { %>
                    <div class="d-flex gap-2">
                        <button onclick="editarAvistamiento(<%= avistamientoRel.getAvistamiento().getAvistamientoID() %>)"
                                class="btn btn-sm-geopet btn-edit">
                            <i class="bi bi-pencil"></i> Editar
                        </button>
                        <button onclick="eliminarAvistamiento(<%= avistamientoRel.getAvistamiento().getAvistamientoID() %>)"
                                class="btn btn-sm-geopet btn-delete">
                            <i class="bi bi-trash"></i> Eliminar
                        </button>
                    </div>
                    <% } %>
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
            <p class="mb-0">Si has visto a esta mascota, ¡reporta tu avistamiento!</p>
        </div>
        <% } %>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // ==================== JAVASCRIPT PARA COMENTARIOS Y AVISTAMIENTOS ====================

    // Función para editar comentario
    function editarComentario(comentarioId, contenidoActual) {
        const nuevoContenido = prompt('Editar comentario:', contenidoActual);
        if (nuevoContenido !== null && nuevoContenido.trim() !== '') {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'ComentariosServlet';

            const accion = document.createElement('input');
            accion.type = 'hidden';
            accion.name = 'accion';
            accion.value = 'editar';

            const comentarioIdInput = document.createElement('input');
            comentarioIdInput.type = 'hidden';
            comentarioIdInput.name = 'comentarioId';
            comentarioIdInput.value = comentarioId;

            const reporteId = document.createElement('input');
            reporteId.type = 'hidden';
            reporteId.name = 'reporteId';
            reporteId.value = '<%= reporteCompleto.getReporte().getReporteID() %>';

            const contenido = document.createElement('input');
            contenido.type = 'hidden';
            contenido.name = 'contenido';
            contenido.value = nuevoContenido;

            form.appendChild(accion);
            form.appendChild(comentarioIdInput);
            form.appendChild(reporteId);
            form.appendChild(contenido);

            document.body.appendChild(form);
            form.submit();
        }
    }

    // Función para eliminar comentario
    function eliminarComentario(comentarioId) {
        if (confirm('¿Estás seguro de que quieres eliminar este comentario?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'ComentariosServlet';

            const accion = document.createElement('input');
            accion.type = 'hidden';
            accion.name = 'accion';
            accion.value = 'eliminar';

            const comentarioIdInput = document.createElement('input');
            comentarioIdInput.type = 'hidden';
            comentarioIdInput.name = 'comentarioId';
            comentarioIdInput.value = comentarioId;

            const reporteId = document.createElement('input');
            reporteId.type = 'hidden';
            reporteId.name = 'reporteId';
            reporteId.value = '<%= reporteCompleto.getReporte().getReporteID() %>';

            form.appendChild(accion);
            form.appendChild(comentarioIdInput);
            form.appendChild(reporteId);

            document.body.appendChild(form);
            form.submit();
        }
    }

    // Función para editar avistamiento
    function editarAvistamiento(avistamientoId) {
        // Por ahora un mensaje simple - se puede implementar un modal más elaborado
        alert('Funcionalidad de edición de avistamientos en desarrollo.\nPor el momento, puedes eliminar y crear uno nuevo si necesitas hacer cambios.');
    }

    // Función para eliminar avistamiento
    function eliminarAvistamiento(avistamientoId) {
        if (confirm('¿Estás seguro de que quieres eliminar este avistamiento?')) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'AvistamientoServlet';

            const accion = document.createElement('input');
            accion.type = 'hidden';
            accion.name = 'accion';
            accion.value = 'eliminar';

            const avistamientoIdInput = document.createElement('input');
            avistamientoIdInput.type = 'hidden';
            avistamientoIdInput.name = 'avistamientoId';
            avistamientoIdInput.value = avistamientoId;

            const reporteId = document.createElement('input');
            reporteId.type = 'hidden';
            reporteId.name = 'reporteId';
            reporteId.value = '<%= reporteCompleto.getReporte().getReporteID() %>';

            form.appendChild(accion);
            form.appendChild(avistamientoIdInput);
            form.appendChild(reporteId);

            document.body.appendChild(form);
            form.submit();
        }
    }

    // Configurar fecha máxima para avistamientos (no puede ser futura)
    document.addEventListener('DOMContentLoaded', function() {
        const fechaInput = document.querySelector('input[name="fechaAvistamiento"]');
        if (fechaInput) {
            const today = new Date();
            const todayString = today.toISOString().split('T')[0];
            fechaInput.max = todayString;
        }
    });
</script>
</body>
</html>