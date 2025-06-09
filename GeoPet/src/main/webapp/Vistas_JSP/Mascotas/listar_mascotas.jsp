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
        }

        .header h1 {
            color: #333;
            margin-bottom: 10px;
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

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #545b62;
        }

        .mascotas-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 25px;
            margin-bottom: 30px;
        }

        .mascota-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            overflow: hidden;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .mascota-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .imagen-container {
            position: relative;
            height: 200px;
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
            font-size: 48px;
        }

        .estado-badge {
            position: absolute;
            top: 10px;
            right: 10px;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            text-transform: uppercase;
        }

        .estado-perdido {
            background-color: #dc3545;
            color: white;
        }

        .estado-encontrado {
            background-color: #28a745;
            color: white;
        }

        .estado-en-casa {
            background-color: #17a2b8;
            color: white;
        }

        .sexo-icon {
            position: absolute;
            top: 10px;
            left: 10px;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 16px;
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
            padding: 20px;
        }

        .mascota-nombre {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin-bottom: 8px;
        }

        .mascota-especie {
            color: #666;
            font-size: 16px;
            margin-bottom: 15px;
            font-style: italic;
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

        .caracteristicas {
            background-color: #f8f9fa;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 15px;
            font-size: 14px;
            line-height: 1.4;
            color: #555;
        }

        .card-actions {
            display: flex;
            gap: 10px;
            border-top: 1px solid #eee;
            padding-top: 15px;
        }

        .btn-sm {
            padding: 8px 16px;
            font-size: 14px;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            transition: all 0.3s ease;
            flex: 1;
            text-align: center;
        }

        .btn-edit {
            background-color: #ffc107;
            color: #212529;
        }

        .btn-edit:hover {
            background-color: #e0a800;
        }

        .btn-delete {
            background-color: #dc3545;
            color: white;
        }

        .btn-delete:hover {
            background-color: #c82333;
        }

        .btn-photos {
            background-color: #17a2b8;
            color: white;
        }

        .btn-photos:hover {
            background-color: #138496;
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
            .mascotas-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }

            .acciones-principales {
                margin-bottom: 20px;
            }

            .btn {
                display: block;
                margin: 10px 0;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>🐾 Mis Mascotas</h1>
        <p>Administra la información de tus queridas mascotas</p>
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

    <!-- Acciones principales -->
    <div class="acciones-principales">
        <a href="MascotaServlet?accion=registrar" class="btn btn-primary">
            ➕ Registrar Nueva Mascota
        </a>
        <a href="/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp" class="btn btn-secondary">
            🏠 Ir al Inicio
        </a>
    </div>

    <!-- Grid de mascotas -->
    <%
        List<MascotaConRelaciones> mascotasRel = (List<MascotaConRelaciones>) request.getAttribute("mascotasConRelaciones");
        if (mascotasRel != null && !mascotasRel.isEmpty()) {
    %>
    <div class="mascotas-grid">
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
        %>
        <div class="mascota-card">
            <div class="imagen-container">
                <% if (mascotaRel.tieneImagen()) { %>
                <img src="<%= request.getContextPath() %>/<%= imagen.getURL_Imagen() %>"
                     alt="Foto de <%= mascota.getNombre() %>"
                     class="mascota-imagen">
                <% } else { %>
                <div class="imagen-placeholder">
                    🐾
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
                <h3 class="mascota-nombre"><%= mascota.getNombre() %></h3>
                <p class="mascota-especie"><%= especie.getNombre() %></p>

                <div class="mascota-info">
                    <div class="info-item">
                        <span class="info-label">Edad:</span>
                        <span class="info-value"><%= mascota.getEdad() %> meses </span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Color:</span>
                        <span class="info-value"><%= mascota.getColor() %></span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Sexo:</span>
                        <span class="info-value"><%= mascota.getSexo() %></span>
                    </div>
                </div>

                <% if (mascota.getCaracteristicasDistintivas() != null &&
                        !mascota.getCaracteristicasDistintivas().trim().isEmpty()) { %>
                <div class="caracteristicas">
                    <strong>Características distintivas:</strong><br>
                    <%= mascota.getCaracteristicasDistintivas() %>
                </div>
                <% } %>

                <div class="card-actions">
                    <a href="MascotaServlet?accion=editar&id=<%= mascota.getMascotaID() %>"
                       class="btn-sm btn-edit">
                        ✏️ Editar
                    </a>
                    <a href="Vistas_JSP/Mascotas/imagen_mascota_focused.jsp?mascotaId=<%= mascota.getMascotaID() %>&action=subir&nombre=<%= mascota.getNombre() %>"
                       class="btn-sm btn-photos">
                        📷 Fotos
                    </a>
                    <a href="MascotaServlet?accion=eliminar&id=<%= mascota.getMascotaID() %>"
                       class="btn-sm btn-delete"
                       onclick="return confirm('¿Estás seguro de eliminar a <%= mascota.getNombre() %>?');">
                        🗑️ Eliminar
                    </a>
                </div>
            </div>
        </div>
        <% } %>
    </div>
    <% } else { %>
    <!-- Estado vacío -->
    <div class="empty-state">
        <div class="empty-state-icon">🐾</div>
        <h3>No tienes mascotas registradas</h3>
        <p>¡Comienza registrando tu primera mascota!</p>
        <a href="MascotaServlet?accion=registrar" class="btn btn-primary">
            ➕ Registrar Mi Primera Mascota
        </a>
    </div>
    <% } %>
</div>
</body>
</html>