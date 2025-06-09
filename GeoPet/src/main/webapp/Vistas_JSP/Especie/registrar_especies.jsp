<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Registrar Especie</title>
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
            max-width: 800px;
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

        .form-floating {
            margin-bottom: 1.5rem;
        }

        .form-floating > .form-control {
            border-radius: 8px;
            border: 2px solid var(--primary-color);
            padding: 1rem 0.75rem;
            height: calc(3.5rem + 2px);
            line-height: 1.25;
        }

        .form-floating > .form-control:focus {
            border-color: var(--secondary-color);
            box-shadow: 0 0 0 0.25rem rgba(170, 150, 218, 0.25);
        }

        .form-floating > label {
            padding: 1rem 0.75rem;
            color: var(--text-color);
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

        .alert {
            border-radius: 8px;
            border: none;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2><i class="fas fa-dog me-2"></i>Registrar Nueva Especie</h2>
        </div>
        <div class="card-body">
            <%
                String mensaje = (String) request.getAttribute("mensaje");
                if (mensaje != null) {
                    String tipo = (String) request.getAttribute("tipo");
                    String alertClass = tipo.equals("success") ? "alert-success" : "alert-danger";
            %>
                <div class="alert <%= alertClass %>" role="alert">
                    <%= mensaje %>
                </div>
            <% } %>

            <form action="EspecieServlet" method="post">
                <div class="form-floating mb-3">
                    <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre de la especie" required>
                    <label for="nombre"><i class="fas fa-tag me-2"></i>Nombre de la especie</label>
                </div>

                <div class="form-floating mb-4">
                    <textarea class="form-control" id="descripcion" name="descripcion" placeholder="Descripción" style="height: 100px" required></textarea>
                    <label for="descripcion"><i class="fas fa-align-left me-2"></i>Descripción</label>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="EspecieServlet?accion=listar" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Volver a la Lista
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> Guardar Especie
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>