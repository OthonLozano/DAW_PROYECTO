<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.ReporteConRelaciones" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Lista de Reportes</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">

    <style>
        :root {
            --primary-color: #a8d8ea;
            --secondary-color: #aa96da;
            --accent-color: #fcbad3;
            --background-color: #f8f9fa;
            --text-color: #2c3e50;
        }
        
        body {
            background-color: var(--background-color);
            color: var(--text-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            background: white;
            margin-bottom: 2rem;
        }

        .card-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 1.5rem;
            border: none;
        }

        .card-header h2 {
            margin: 0;
            font-size: 1.8rem;
            font-weight: 600;
        }

        .card-body {
            padding: 2rem;
        }

        .table {
            margin-bottom: 0;
        }

        .table thead th {
            background-color: var(--primary-color);
            color: var(--text-color);
            border: none;
            padding: 1rem;
            font-weight: 500;
        }

        .table tbody td {
            vertical-align: middle;
            padding: 1rem;
            border-color: #f0f0f0;
        }

        .table tbody tr:nth-child(even) {
            background-color: #f8f9fa;
        }

        .btn-action {
            padding: 0.5rem 1rem;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.3s ease;
            border: none;
        }

        .btn-delete {
            background-color: #ff6b6b;
            color: white;
        }

        .btn-delete:hover {
            background-color: #ff5252;
            color: white;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: var(--primary-color);
            border: none;
            border-radius: 8px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            color: var(--text-color);
            transition: all 0.3s ease;
        }

        .btn-secondary:hover {
            background: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(170, 150, 218, 0.3);
            color: white;
        }

        .table-responsive {
            border-radius: 10px;
            overflow: hidden;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2><i class="fas fa-exclamation-triangle me-2"></i>Lista de Reportes</h2>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Mascota</th>
                            <th>Especie</th>
                            <th>Fecha Desaparición</th>
                            <th>Ubicación</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<ReporteConRelaciones> reportes = (List<ReporteConRelaciones>) request.getAttribute("reportes");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            if (reportes != null && !reportes.isEmpty()) {
                                for (ReporteConRelaciones reporte : reportes) {
                        %>
                        <tr>
                            <td><%= reporte.getMascota().getNombre() %></td>
                            <td><%= reporte.getEspecie().getNombre() %></td>
                            <td><%= sdf.format(reporte.getReporte().getFechaDesaparicion()) %></td>
                            <td><%= reporte.getReporte().getUbicacion() %></td>
                            <td>
                                <a href="ReporteDesaparicionServlet?accion=eliminar&id=<%= reporte.getReporte().getReporteID() %>"
                                   onclick="return confirm('¿Estás seguro de eliminar este reporte?');"
                                   class="btn btn-action btn-delete">
                                    <i class="fas fa-trash-alt me-1"></i>Eliminar
                                </a>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="5" class="text-center">No hay reportes registrados.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>

            <div class="d-flex justify-content-start mt-4">
                <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/HomeAdmin.jsp" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Volver al Panel de Administración
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 