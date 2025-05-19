<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Registrar Imagen de Mascota</title>
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

    input {
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
  <h2>Registrar Imagen de Mascota</h2>

  <form action="${pageContext.request.contextPath}/ImagenMascotaServlet" method="post">
    <input type="hidden" name="accion" value="registrar"/>

    <label for="r_mascota">ID Mascota:</label>
    <input type="number" name="r_mascota" id="r_mascota" required/>

    <label for="url_imagen">ID URL de Imagen:</label>
    <input type="number" name="url_imagen" id="url_imagen" required/>

    <label for="fecha_carga">Fecha de Carga:</label>
    <input type="date" name="fecha_carga" id="fecha_carga" required/>

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
    <a href="${pageContext.request.contextPath}/ImagenMascotaServlet?accion=listar">Listar Imágenes</a><br>
    <a href="${pageContext.request.contextPath}/index.jsp">Menú Principal</a>
  </div>
</div>
</body>
</html>