<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page import="java.util.List" %>
<%
    // Verificar si el usuario está autenticado y es admin
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"Admin".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
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

        .table-responsive {
            border-radius: 10px;
            overflow: hidden;
        }

        .user-status {
            padding: 0.25rem 0.75rem;
            border-radius: 50px;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .status-active {
            background-color: #d4edda;
            color: #155724;
        }

        .status-inactive {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2><i class="fas fa-users me-2"></i>Lista de Usuarios</h2>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre Completo</th>
                        <th>Email</th>
                        <th>Teléfono</th>
                        <th>Ciudad</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Usuarios> usuarios = (List<Usuarios>) request.getAttribute("usuarios");
                        if (usuarios != null && !usuarios.isEmpty()) {
                            for (Usuarios u : usuarios) {
                    %>
                    <tr>
                        <td><%= u.getUsuarioID() %></td>
                        <td><%= u.getNombre() + " " + u.getApellidoPat() + " " + u.getApellidoMat() %></td>
                        <td><%= u.getEmail() %></td>
                        <td><%= u.getTelefono() %></td>
                        <td><%= u.getCiudad() %></td>
                        <td><%= u.getUsuario() %></td>
                        <td>
                                <span class="user-status <%= "Alta".equals(u.getEstatus()) ? "status-active" : "status-inactive" %>">
                                    <%= u.getEstatus() %>
                                </span>
                        </td>
                        <td>
                            <a href="ServletUsuario?accion=editar&id=<%= u.getUsuarioID() %>"
                               class="btn btn-action btn-edit me-2">
                                <i class="fas fa-edit me-1"></i>Editar
                            </a>
                            <a href="ServletUsuario?accion=eliminar&id=<%= u.getUsuarioID() %>"
                               onclick="return confirm('¿Estás seguro de eliminar este usuario?');"
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
                        <td colspan="8" class="text-center">No hay usuarios registrados.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="HomeAdmin.jsp" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Volver al Panel
                </a>
                <a href="RegistrarCliente.jsp" class="btn btn-primary">
                    <i class="fas fa-user-plus"></i> Registrar Nuevo Usuario
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>