<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Mascota</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: black;
            color: #333;
            padding: 40px;
        }

        .contenedor {
            max-width: 700px;
            margin: auto;
            background-color: #f8f4a7;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.3);
        }

        h2 {
            text-align: center;
            color: #0f75b6;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin: 10px 0 5px;
            font-weight: bold;
        }

        input, select {
            padding: 8px;
            border-radius: 5px;
            border: 1px solid #aaa;
        }

        input[type="submit"] {
            margin-top: 20px;
            background-color: #0f75b6;
            color: white;
            border: none;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #0c5a8c;
        }

        .links {
            text-align: center;
            margin-top: 20px;
        }

        .links a {
            color: #0f75b6;
            text-decoration: none;
            margin: 0 10px;
        }

        .links a:hover {
            text-decoration: underline;
            text-decoration-color: black;
        }

        .mensaje {
            background-color: #d4edda;
            color: #155724;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }

        .info-usuario {
            background-color: #e7f3ff;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
            color: #0f75b6;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="contenedor">
    <h2>Registrar Mascota</h2>

    <!-- Mostrar mensaje de éxito si existe -->
    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="mensaje">
        <%= request.getAttribute("mensaje") %>
    </div>
    <% } %>

    <!-- Mostrar información del usuario logueado (opcional) -->
    <%
        // Obtener el ID del usuario directamente de la sesión o del atributo
        Integer usuarioId = (Integer) request.getAttribute("usuarioId");
        if (usuarioId == null) {
            usuarioId = (Integer) session.getAttribute("usuarioId");
        }
        if (usuarioId != null) {
    %>
    <div class="info-usuario">
        Registrando mascota para el usuario ID: <%= usuarioId %>
    </div>
    <% } %>

    <form action="MascotaServlet" method="post">
        <input type="hidden" name="accion" value="registrar" />

        <!-- Campo oculto para el ID del usuario - toma el valor de la sesión -->
        <input type="hidden" name="r_usuario" value="<%= usuarioId != null ? usuarioId : session.getAttribute("usuarioId") %>" />

        <label for="nombre">Nombre:</label>
        <input type="text" name="nombre" required />

        <label for="r_especie">Especie ID:</label>
        <input type="number" name="r_especie" required />

        <label for="edad">Edad:</label>
        <input type="number" name="edad" required />

        <label for="sexo">Sexo:</label>
        <select name="sexo" required>
            <option value="">Seleccionar...</option>
            <option value="macho">Macho</option>
            <option value="hembra">Hembra</option>
        </select>

        <label for="color">Color:</label>
        <input type="text" name="color" required />

        <label for="caracteristicasdistintivas">Características Distintivas:</label>
        <input type="text" name="caracteristicasdistintivas" />

        <label for="microchip">¿Tiene microchip?</label>
        <select name="microchip" required>
            <option value="">Seleccionar...</option>
            <option value="true">Sí</option>
            <option value="false">No</option>
        </select>

        <label for="numero_microchip">Número de Microchip (opcional):</label>
        <input type="number" name="numero_microchip" />

        <label for="estado">Estado:</label>
        <select name="estado" required>
            <option value="">Seleccionar...</option>
            <option value="Perdida">Perdida</option>
            <option value="En casa">En casa</option>
        </select>

        <label for="fecha_registro">Fecha de Registro:</label>
        <input type="date" name="fecha_registro" required />

        <input type="submit" value="Registrar" />
    </form>

    <div class="links">
        <a href="MascotaServlet?accion=listar">Volver a la lista</a>
        <a href="index.jsp">Menú Principal</a>
    </div>
</div>
</body>
</html>