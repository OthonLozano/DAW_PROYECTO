<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.MascotaConRelaciones" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<%@ page import="Modelo.JavaBeans.ImagenMascota" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Mascotas - GeoPet</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .main-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 2rem;
        }

        .mascota-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            overflow: hidden;
            transition: all 0.3s ease;
            height: 100%;
        }

        .mascota-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .imagen-container {
            position: relative;
            height: 220px;
            overflow: hidden;
        }

        .mascota-imagen {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
        }

        .mascota-card:hover .mascota-imagen {
            transform: scale(1.05);
        }

        .imagen-placeholder {
            width: 100%;
            height: 100%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 3rem;
        }

        .estado-badge {
            position: absolute;
            top: 10px;
            right: 10px;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: bold;
            text-transform: uppercase;
            backdrop-filter: blur(10px);
        }

        .estado-perdido {
            background-color: rgba(220, 53, 69, 0.9);
            color: white;
        }

        .estado-encontrado {
            background-color: rgba(40, 167, 69, 0.9);
            color: white;
        }

        .estado-en-casa {
            background-color: rgba(23, 162, 184, 0.9);
            color: white;
        }

        .sexo-icon {
            position: absolute;
            top: 10px;
            left: 10px;
            width: 35px;
            height: 35px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 1.2rem;
            backdrop-filter: blur(10px);
        }

        .sexo-macho {
            background-color: rgba(0, 123, 255, 0.9);
            color: white;
        }

        .sexo-hembra {
            background-color: rgba(232, 62, 140, 0.9);
            color: white;
        }

        .caracteristicas {
            background-color: #f8f9fa;
            padding: 0.75rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            font-size: 0.875rem;
            line-height: 1.4;
            color: #6c757d;
        }

        .btn-action {
            border-radius: 8px;
            font-weight: 600;
            padding: 0.5rem 1rem;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
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

        .btn-photos {
            background-color: #17a2b8;
            color: white;
            border: none;
        }

        .btn-photos:hover {
            background-color: #138496;
            color: white;
            transform: translateY(-1px);
        }

        .empty-state {
            text-align: center;
            padding: 4rem 2rem;
            color: #6c757d;
        }

        .empty-state-icon {
            font-size: 4rem;
            margin-bottom: 1.5rem;
            opacity: 0.5;
        }

        .info-item {
            margin-bottom: 0.5rem;
            font-size: 0.875rem;
        }

        .info-label {
            font-weight: 600;
            color: #495057;
        }

        .mascota-nombre {
            font-weight: 700;
            color: #2e7d32;
            margin-bottom: 0.5rem;
        }

        .mascota-especie {
            color: #6c757d;
            font-style: italic;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
<!-- Header principal -->
<div class="main-header">
    <div class="container">
        <div class="row">
            <div class="col-12 text-center">
                <h1 class="display-4 fw-bold mb-3">
                    <i class="bi bi-heart-fill me-3"></i>Mis Mascotas
                </h1>
                <p class="lead mb-0">Administra la información de tus queridas mascotas</p>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <!-- Mostrar mensajes -->
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
    <div class="alert <%= mensaje.contains("Error") ? "alert-danger" : "alert-success" %> alert-dismissible fade show" role="alert">
        <i class="<%= mensaje.contains("Error") ? "bi bi-exclamation-triangle-fill" : "bi bi-check-circle-fill" %> me-2"></i>
        <%= mensaje %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <!-- Acciones principales -->
    <div class="row mb-4">
        <div class="col-12 text-center">
            <a href="MascotaServlet?accion=registrar" class="btn btn-success btn-lg me-3">
                <i class="bi bi-plus-circle me-2"></i>Registrar Nueva Mascota
            </a>
            <a href="/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp" class="btn btn-outline-secondary btn-lg">
                <i class="bi bi-house me-2"></i>Ir al Inicio
            </a>
        </div>
    </div>

    <!-- Grid de mascotas -->
    <%
        List<MascotaConRelaciones> mascotasRel = (List<MascotaConRelaciones>) request.getAttribute("mascotasConRelaciones");
        if (mascotasRel != null && !mascotasRel.isEmpty()) {
    %>
    <div class="row g-4">
        <%
            for (MascotaConRelaciones mascotaRel : mascotasRel) {
                Mascotas mascota = mascotaRel.getMascota();
                Especie especie = mascotaRel.getEspecie();
                ImagenMascota imagen = mascotaRel.getImagen();

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

                // Determinar el estado
                String estadoClass = "";
                String estadoTexto = mascota.getEstado();
                if ("Perdido".equalsIgnoreCase(estadoTexto)) {
                    estadoClass = "estado-perdido";
                } else if ("Encontrado".equalsIgnoreCase(estadoTexto)) {
                    estadoClass = "estado-encontrado";
                } else {
                    estadoClass = "estado-en-casa";
                }

                // URL de la imagen corregida
                String urlImagen = request.getContextPath() + "/ImagenMascotaServlet?action=obtener&mascotaId=" + mascota.getMascotaID();
        %>
        <div class="col-lg-4 col-md-6 col-sm-12">
            <div class="card mascota-card">
                <div class="imagen-container">
                    <% if (mascotaRel.tieneImagen()) { %>
                    <img src="<%= urlImagen %>"
                         alt="Foto de <%= mascota.getNombre() %>"
                         class="mascota-imagen"
                         data-mascota-id="<%= mascota.getMascotaID() %>"
                         onerror="this.parentElement.innerHTML='<div class=\'imagen-placeholder\'><i class=\'bi bi-image\' style=\'font-size: 3rem;\'></i></div>';">
                    <% } else { %>
                    <div class="imagen-placeholder">
                        <i class="bi bi-camera" style="font-size: 3rem;"></i>
                    </div>
                    <% } %>

                    <!-- Ícono de sexo -->
                    <% if (!sexoIcon.isEmpty()) { %>
                    <div class="sexo-icon <%= sexoClass %>">
                        <%= sexoIcon %>
                    </div>
                    <% } %>

                    <!-- Badge de estado -->
                    <div class="estado-badge <%= estadoClass %>">
                        <%= estadoTexto %>
                    </div>
                </div>

                <div class="card-body">
                    <h5 class="mascota-nombre"><%= mascota.getNombre() %></h5>
                    <p class="mascota-especie"><%= especie.getNombre() %></p>

                    <div class="mascota-info mb-3">
                        <div class="info-item">
                            <span class="info-label">Edad:</span>
                            <span class="text-muted"><%= mascota.getEdad() %> meses</span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Color:</span>
                            <span class="text-muted"><%= mascota.getColor() %></span>
                        </div>
                        <div class="info-item">
                            <span class="info-label">Sexo:</span>
                            <span class="text-muted"><%= mascota.getSexo() %></span>
                        </div>
                    </div>

                    <% if (mascota.getCaracteristicasDistintivas() != null &&
                            !mascota.getCaracteristicasDistintivas().trim().isEmpty()) { %>
                    <div class="caracteristicas">
                        <strong>Características distintivas:</strong><br>
                        <%= mascota.getCaracteristicasDistintivas() %>
                    </div>
                    <% } %>

                    <div class="d-flex gap-2 flex-wrap">
                        <a href="MascotaServlet?accion=editar&id=<%= mascota.getMascotaID() %>"
                           class="btn btn-action btn-edit flex-fill">
                            <i class="bi bi-pencil"></i> Editar
                        </a>
                        <a href="MascotaServlet?accion=eliminar&id=<%= mascota.getMascotaID() %>"
                           class="btn btn-action btn-delete flex-fill"
                           onclick="return confirm('¿Estás seguro de eliminar a <%= mascota.getNombre() %>?');">
                            <i class="bi bi-trash"></i> Eliminar
                        </a>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>
    <% } else { %>
    <!-- Estado vacío -->
    <div class="empty-state">
        <div class="empty-state-icon">
            <i class="bi bi-heart"></i>
        </div>
        <h3 class="mb-3">No tienes mascotas registradas</h3>
        <p class="mb-4">¡Comienza registrando tu primera mascota!</p>
        <a href="MascotaServlet?accion=registrar" class="btn btn-success btn-lg">
            <i class="bi bi-plus-circle me-2"></i>Registrar Mi Primera Mascota
        </a>
    </div>
    <% } %>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>