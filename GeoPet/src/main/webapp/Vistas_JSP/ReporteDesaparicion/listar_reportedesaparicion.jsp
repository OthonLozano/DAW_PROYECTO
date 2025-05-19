<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.ReporteDesaparicion" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Lista de Reportes de Desaparición</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: black;
      color: #333;
      padding: 40px;
    }

    .contenedor {
      max-width: 1100px;
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
  <h2>Lista de Reportes de Desaparición</h2>

  <table>
    <tr>
      <th>Mascota</th>
      <th>Usuario</th>
      <th>Fecha Desaparición</th>
      <th>Ubicación Última Vez</th>
      <th>Recompensa</th>
      <th>Estado</th>
      <th>Fecha Registro</th>
      <th>Acciones</th>
    </tr>
    <%
      List<ReporteDesaparicion> reportes = (List<ReporteDesaparicion>) request.getAttribute("reportes");
      if (reportes != null && !reportes.isEmpty()) {
        for (ReporteDesaparicion r : reportes) {
    %>
    <tr>
      <td><%= r.getR_Mascota() %></td>
      <td><%= r.getR_Usuario() %></td>
      <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(r.getFechaDesaparicion()) %></td>
      <td><%= r.getUbicacionUltimaVez() %></td>
      <td>$<%= r.getRecompensa() %></td>
      <td><%= r.getEstadoReporte() %></td>
      <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(r.getFecha_Registro()) %></td>
      <td>
        <a href="ReporteDesaparicionServlet?accion=editar&id=<%= r.getReporteID() %>">Editar</a> |
        <a href="ReporteDesaparicionServlet?accion=eliminar&id=<%= r.getReporteID() %>"
           onclick="return confirm('¿Estás seguro de eliminar este reporte?');">Eliminar</a>
      </td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="8">No hay reportes registrados.</td>
    </tr>
    <% } %>
  </table>

  <div class="links">
    <a href="Vistas_JSP/ReporteDesaparicion/registrar_reporte.jsp">Registrar Nuevo Reporte</a><br>
    <a href="index.jsp">Regresar al Menú Principal</a>
  </div>
</div>
</body>
</html>

