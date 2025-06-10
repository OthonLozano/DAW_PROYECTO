<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page import="java.util.List" %>
<%
    String tipoLista = (String) request.getAttribute("tipoLista");
    if (tipoLista == null) tipoLista = "todos";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Lista de Usuarios</title>
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
            --success-color: #28a745;
            --danger-color: #dc3545;
            --warning-color: #ffc107;
            --info-color: #17a2b8;
        }

        body {
            background-color: var(--background-color);
            color: var(--text-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .container {
            max-width: 1400px;
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
            white-space: nowrap;
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
            margin: 0.2rem;
        }

        .btn-edit {
            background-color: var(--secondary-color);
            color: white;
        }

        .btn-edit:hover {
            background-color: var(--accent-color);
            color: white;
            transform: translateY(-2px);
        }

        .btn-delete {
            background-color: var(--danger-color);
            color: white;
        }

        .btn-delete:hover {
            background-color: #c82333;
            color: white;
            transform: translateY(-2px);
        }

        .btn-restore {
            background-color: var(--success-color);
            color: white;
        }

        .btn-restore:hover {
            background-color: #218838;
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

        .btn-primary {
            background: linear-gradient(135deg, var(--secondary-color), var(--accent-color));
            border: none;
            border-radius: 8px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(252, 186, 211, 0.3);
        }

        .btn-info {
            background-color: var(--info-color);
            border: none;
            border-radius: 8px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-info:hover {
            background-color: #138496;
            transform: translateY(-2px);
            color: white;
        }

        .btn-warning {
            background-color: var(--warning-color);
            border: none;
            border-radius: 8px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
            color: var(--text-color);
            transition: all 0.3s ease;
        }

        .btn-warning:hover {
            background-color: #e0a800;
            transform: translateY(-2px);
            color: var(--text-color);
        }

        .table-responsive {
            border-radius: 10px;
            overflow: hidden;
        }

        .alert-success, .alert-danger, .alert-info {
            border-radius: 10px;
            margin-bottom: 1rem;
        }

        .status-badge {
            padding: 0.4rem 0.8rem;
            border-radius: 50px;
            font-size: 0.8rem;
            font-weight: 600;
            text-transform: uppercase;
        }

        .status-alta {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status-baja {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .filter-tabs {
            margin-bottom: 1.5rem;
        }

        .filter-tabs .nav-link {
            border-radius: 10px;
            margin-right: 0.5rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .filter-tabs .nav-link.active {
            background: linear-gradient(135deg, var(--secondary-color), var(--accent-color));
            color: white;
        }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            .table-responsive {
                font-size: 0.875rem;
            }

            .btn-action {
                padding: 0.4rem 0.8rem;
                font-size: 0.8rem;
                margin: 0.1rem;
            }

            .filter-tabs .nav-link {
                font-size: 0.875rem;
                padding: 0.5rem 1rem;
                margin-bottom: 0.5rem;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <div class="d-flex justify-content-between align-items-center">
                <h2><i class="fas fa-users me-2"></i>Lista de Usuarios</h2>
                <div class="text-end">
                    <small class="opacity-75">
                        Mostrando:
                        <% if ("activos".equals(tipoLista)) { %>
                        <span class="badge bg-success">Solo Activos</span>
                        <% } else if ("eliminados".equals(tipoLista)) { %>
                        <span class="badge bg-danger">Solo Eliminados</span>
                        <% } else { %>
                        <span class="badge bg-info">Todos los Usuarios</span>
                        <% } %>
                    </small>
                </div>
            </div>
        </div>
        <div class="card-body">

            <!-- Tabs de filtros -->
            <div class="filter-tabs">
                <ul class="nav nav-pills" role="tablist">
                    <li class="nav-item" role="presentation">
                        <a class="nav-link <%= "todos".equals(tipoLista) ? "active" : "" %>"
                           href="<%= request.getContextPath() %>/UsuariosServlet?accion=listar">
                            <i class="fas fa-list me-1"></i>Todos
                        </a>
                    </li>
                </ul>
            </div>

            <!-- Mensajes de éxito o error -->
            <%
                String success = request.getParameter("success");
                String error = request.getParameter("error");
                if (success != null) {
            %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i> <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% }
                if (error != null) {
            %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle"></i> <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% } %>

            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre Completo</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Teléfono</th>
                        <th>Ciudad</th>
                        <% if (!"eliminados".equals(tipoLista)) { %>
                        <th>Estado</th>
                        <% } %>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Usuarios> usuarios = (List<Usuarios>) request.getAttribute("usuarios");
                        if (usuarios != null && !usuarios.isEmpty()) {
                            for (Usuarios u : usuarios) {
                                String estatus = u.getEstatus() != null ? u.getEstatus() : "Alta";
                    %>
                    <tr>
                        <td><span class="fw-bold text-primary"><%= u.getUsuarioID() %></span></td>
                        <td><%= u.getNombre() + " " + u.getApellidoPat() + " " + u.getApellidoMat() %></td>
                        <td><%= u.getEmail() %></td>
                        <td>
                            <span class="badge bg-primary"><%= u.getUsuario() %></span>
                        </td>
                        <td><%= u.getTelefono() %></td>
                        <td><%= u.getCiudad() %></td>
                        <% if (!"eliminados".equals(tipoLista)) { %>
                        <td>
                                <span class="status-badge status-<%= estatus.toLowerCase() %>">
                                    <%= estatus %>
                                </span>
                        </td>
                        <% } %>
                        <td>
                            <% if ("eliminados".equals(tipoLista)) { %>
                            <!-- Botones para usuarios eliminados -->
                            <a href="<%= request.getContextPath() %>/UsuariosServlet?accion=restaurar&id=<%= u.getUsuarioID() %>"
                               onclick="return confirm('¿Estás seguro de restaurar este usuario?');"
                               class="btn btn-action btn-restore">
                                <i class="fas fa-undo me-1"></i>Restaurar
                            </a>
                            <% } %>
                            <% if ("Alta".equals(estatus)) { %>
                            <a href="<%= request.getContextPath() %>/UsuariosServlet?accion=eliminar&id=<%= u.getUsuarioID() %>"
                               onclick="return confirm('¿Estás seguro de eliminar este usuario?');"
                               class="btn btn-action btn-delete">
                                <i class="fas fa-trash-alt me-1"></i>Eliminar
                            </a>
                            <% } else { %>
                            <a href="<%= request.getContextPath() %>/UsuariosServlet?accion=restaurar&id=<%= u.getUsuarioID() %>"
                               onclick="return confirm('¿Estás seguro de restaurar este usuario?');"
                               class="btn btn-action btn-restore">
                                <i class="fas fa-undo me-1"></i>Restaurar
                            </a>
                            <% } %>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="8" class="text-center">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i>
                                <% if ("eliminados".equals(tipoLista)) { %>
                                No hay usuarios eliminados.
                                <% } else if ("activos".equals(tipoLista)) { %>
                                No hay usuarios activos.
                                <% } else { %>
                                No hay usuarios registrados.
                                <% } %>
                            </div>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <!-- Footer con estadísticas y botones -->
            <div class="row mt-4">
                <div class="col-md-4 d-flex align-items-center">
                    <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Volver al Panel
                    </a>
                </div>

                <div class="col-md-4 text-center">
                    <div class="text-muted">
                        <small>
                            <i class="fas fa-users me-1"></i>
                            <% if ("eliminados".equals(tipoLista)) { %>
                            Usuarios eliminados: <%= (usuarios != null ? usuarios.size() : 0) %>
                            <% } else if ("activos".equals(tipoLista)) { %>
                            Usuarios activos: <%= (usuarios != null ? usuarios.size() : 0) %>
                            <% } else { %>
                            Total de usuarios: <%= (usuarios != null ? usuarios.size() : 0) %>
                            <% } %>
                        </small>
                    </div>
                </div>

                <div class="col-md-4 text-end">
                    <% if (!"eliminados".equals(tipoLista)) { %>
                    <a href="<%= request.getContextPath() %>/Vistas_JSP/Usuarios/registrar_usuarios.jsp" class="btn btn-primary">
                        <i class="fas fa-user-plus me-2"></i>Registrar Nuevo Usuario
                    </a>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Auto-ocultar alertas después de 5 segundos
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            setTimeout(() => {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }, 5000);
        });
    });
</script>

</body>
</html>