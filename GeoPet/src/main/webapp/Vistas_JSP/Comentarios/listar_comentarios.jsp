<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Comentarios" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Lista de Comentarios</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: black;
      color: #333;
      padding: 40px;
    }

    .contenedor {
      max-width: 1000px;
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
  <h2>Lista de Comentarios</h2>

  <table>
    <tr>
      <th>Contenido</th>
      <th>ID Usuario</th>
      <th>ID Reporte</th>
      <th>Fecha Comentario</th>
      <th>Acciones</th>
    </tr>
    <%
      List<Comentarios> comentarios = (List<Comentarios>) request.getAttribute("comentarios");
      if (comentarios != null && !comentarios.isEmpty()) {
        for (Comentarios c : comentarios) {
    %>
    <tr>
      <td><%= c.getContenido() %></td>
      <td><%= c.getR_Usuario() %></td>
      <td><%= c.getR_Reporte() %></td>
      <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getFecha_Comentario()) %></td>
      <td>
        <a href="ComentariosServlet?accion=editar&id=<%= c.getComentarioID() %>">Editar</a> |
        <a href="ComentariosServlet?accion=eliminar&id=<%= c.getComentarioID() %>"
           onclick="return confirm('¿Estás seguro de eliminar este comentario?');">Eliminar</a>
      </td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="5">No hay comentarios registrados.</td>
    </tr>
    <% } %>
  </table>

  <div class="links">
    <a href="Vistas_JSP/Comentarios/registrar_comentario.jsp">Registrar Nuevo Comentario</a><br>
    <a href="index.jsp">Regresar al Menú Principal</a>
  </div>
</div>
</body>
</html>

