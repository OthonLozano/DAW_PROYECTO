<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Avistamiento</title>
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
    <h2>Registrar Nuevo Avistamiento</h2>

    <form action="${pageContext.request.contextPath}/AvistamientoServlet" method="post">
        <input type="hidden" name="accion" value="registrar"/>

        <label for="r_reporte">ID del Reporte:</label>
        <input type="number" name="r_reporte" id="r_reporte" required/>

        <label for="r_usuarioreportante">ID del Usuario Reportante:</label>
        <input type="number" name="r_usuarioreportante" id="r_usuarioreportante" required/>

        <label for="fecha_avistamiento">Fecha del Avistamiento:</label>
        <input type="date" name="fecha_avistamiento" id="fecha_avistamiento" required/>

        <label for="ubicacion">Ubicación:</label>
        <input type="text" name="ubicacion" id="ubicacion" required/>

        <label for="descripcion">Descripción:</label>
        <textarea name="descripcion" id="descripcion" rows="3" required></textarea>

        <label for="contacto">Contacto:</label>
        <input type="text" name="contacto" id="contacto" required/>

        <label for="fecha_registro">Fecha de Registro:</label>
        <input type="date" name="fecha_registro" id="fecha_registro" required/>

        <label for="r_imagen">ID de la Imagen:</label>
        <input type="number" name="r_imagen" id="r_imagen" required/>

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
        <a href="${pageContext.request.contextPath}/AvistamientoServlet?accion=listar">Listar Avistamientos</a><br>
        <a href="${pageContext.request.contextPath}/index.jsp">Menú Principal</a>
    </div>
</div>
</body>
</html>
