<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Registrar Comentario</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: black;
      margin: 40px;
      padding: 20px;
      color: #333;
    }
    .contenedor {
      max-width: 700px;
      margin: 0 auto;
      background-color: #f8f4a7;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.2);
    }
    h2 {
      text-align: center;
      color: #0f75b6;
    }
    label {
      display: block;
      margin-top: 10px;
      font-weight: bold;
    }
    input, textarea {
      width: 100%;
      padding: 8px;
      margin-top: 5px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    input[type="submit"] {
      background-color: #2a5d84;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    input[type="submit"]:hover {
      background-color: #1b3f5a;
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
  <h2>Registrar Nuevo Comentario</h2>

  <form action="${pageContext.request.contextPath}/ComentariosServlet" method="post">
    <input type="hidden" name="accion" value="registrar"/>

    <label for="r_usuario">ID del Usuario:</label>
    <input type="number" name="r_usuario" id="r_usuario" required/>

    <label for="r_reporte">ID del Reporte:</label>
    <input type="number" name="r_reporte" id="r_reporte" required/>

    <label for="contenido">Contenido:</label>
    <textarea name="contenido" id="contenido" rows="3" required></textarea>

    <label for="fecha_comentario">Fecha del Comentario:</label>
    <input type="date" name="fecha_comentario" id="fecha_comentario" required/>

    <div class="boton">
      <input type="submit" value="Registrar"/>
    </div>

    <% String mensaje = (String) request.getAttribute("mensaje"); %>
    <% if (mensaje != null) { %>
    <div id="mensajeExito" style="color: green; font-weight: bold; text-align: center;">
      <%= mensaje %>
    </div>

    <script>
      setTimeout(function () {
        var mensaje = document.getElementById("mensajeExito");
        if (mensaje) {
          mensaje.style.display = "none";
        }
      }, 2000);
    </script>
    <% } %>
  </form>

  <div class="links">
    <a href="${pageContext.request.contextPath}/ComentariosServlet?accion=listar">Listar Comentarios</a><br>
    <a href="${pageContext.request.contextPath}/index.jsp">Menú Principal</a>
  </div>
</div>
</body>
</html>
