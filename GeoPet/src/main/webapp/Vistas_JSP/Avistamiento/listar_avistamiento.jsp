<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Avistamiento" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Lista de Avistamientos</title>
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
  <h2>Lista de Avistamientos</h2>

  <table>
    <tr>
      <th>Ubicación</th>
      <th>Descripción</th>
      <th>Contacto</th>
      <th>Fecha Avistamiento</th>
      <th>Acciones</th>
    </tr>
    <%
      List<Avistamiento> avistamientos = (List<Avistamiento>) request.getAttribute("avistamientos");
      if (avistamientos != null && !avistamientos.isEmpty()) {
        for (Avistamiento a : avistamientos) {
    %>
    <tr>
      <td><%= a.getUbicacion() %></td>
      <td><%= a.getDescripcion() %></td>
      <td><%= a.getContacto() %></td>
      <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(a.getFecha_Avistamiento()) %></td>
      <td>
        <a href="AvistamientoServlet?accion=editar&id=<%= a.getAvistamientoID() %>">Editar</a> |
        <a href="AvistamientoServlet?accion=eliminar&id=<%= a.getAvistamientoID() %>"
           onclick="return confirm('¿Estás seguro de eliminar este avistamiento?');">Eliminar</a>
      </td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="5">No hay avistamientos registrados.</td>
    </tr>
    <% } %>
  </table>

  <div class="links">
    <a href="Vistas_JSP/Avistamientos/registrar_avistamiento.jsp">Registrar Nuevo Avistamiento</a><br>
    <a href="index.jsp">Regresar al Menú Principal</a>
  </div>
</div>
</body>
</html>
