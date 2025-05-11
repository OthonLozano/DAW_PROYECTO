<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Especies</title>
    <style>
        body {
          font-family: Arial, sans-serif;
          background-color: black;
          color: #333;
          padding: 40px;
        }

        .contenedor {
          max-width: 800px;
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
    <h2>Lista de Especies</h2>

    <table>
      <tr>
        <th>Nombre</th>
        <th>Descripción</th>
        <th>Acciones</th>
      </tr>
      <%
        List<Especie> especies = (List<Especie>) request.getAttribute("especies");
        if (especies != null && !especies.isEmpty()) {
          for (Especie es : especies) {
      %>
      <tr>
        <td><%= es.getNombre() %></td>
        <td><%= es.getDescripcion() %></td>
        <td>
          <a href="EspecieServlet?accion=editar&id=<%= es.getEspecieID() %>">Editar</a>
          |
          <a href="EspecieServlet?accion=eliminar&id=<%= es.getEspecieID() %>"
             onclick="return confirm('¿Estás seguro de eliminar esta especie?');">Eliminar</a>
        </td>
      </tr>
      <%
        }
      } else {
      %>
      <tr>
        <td colspan="3">No hay especies registradas.</td>
      </tr>
      <% } %>
    </table>

    <div class="links">
      <a href="Vistas_JSP/Especie/registrar_especies.jsp">Registrar Nueva Especie</a><br>
      <a href="index.jsp">Regresar al Menú Principal</a>
    </div>
  </div>
</body>
</html>
