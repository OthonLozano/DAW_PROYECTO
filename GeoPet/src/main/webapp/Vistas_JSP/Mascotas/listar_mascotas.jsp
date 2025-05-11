<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Mascotas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: black;
            color: #333;
            padding: 40px;
        }

        .contenedor {
            max-width: 900px;
            margin: 0 auto;
            background-color: #f8f4a7;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.2);
        }

        h2 {
            text-align: center;
            color: #0f75b6;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
        }

        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #aaa;
        }

        th {
            background-color: #2a5d84;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        a {
            display: inline-block;
            margin-top: 15px;
            color: #0f75b6;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
            text-decoration-color: black;
            text-decoration-thickness: 2px;
        }

        .links {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="contenedor">
    <h2>Lista de Mascotas</h2>

    <table>
        <tr>
            <th>Nombre</th>
            <th>Especie</th>
            <th>Edad</th>
            <th>Sexo</th>
            <th>Acciones</th>
        </tr>
        <%
            List<Mascotas> mascotas = (List<Mascotas>) request.getAttribute("mascotas");
            if (mascotas != null && !mascotas.isEmpty()) {
                for (Mascotas m : mascotas) {
        %>
        <tr>
            <td><%= m.getNombre() %></td>
            <td><%= m.getR_Especie() %></td>
            <td><%= m.getEdad() %></td>
            <td><%= m.getSexo() %></td>
            <td>
                <a href="MascotaServlet?accion=editar&id=<%= m.getMascotaID() %>">Editar</a> |
                <a href="MascotaServlet?accion=eliminar&id=<%= m.getMascotaID() %>"
                   onclick="return confirm('¿Estás seguro de eliminar esta mascota?');">Eliminar</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="6">No hay mascotas registradas.</td>
        </tr>
        <% } %>
    </table>

    <div class="links">
        <a href="registrarMascota.jsp">Registrar Nueva Mascota</a><br>
        <a href="index.jsp">Regresar al Menú Principal</a>
    </div>
</div>
</body>
</html>
