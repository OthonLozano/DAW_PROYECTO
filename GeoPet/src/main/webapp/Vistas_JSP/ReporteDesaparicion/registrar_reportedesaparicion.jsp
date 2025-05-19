<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Reporte de Desaparición</title>
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

        input, textarea, select {
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
    <h2>Registrar Nuevo Reporte de Desaparición</h2>

    <form action="${pageContext.request.contextPath}/ReporteDesaparicionServlet" method="post">
        <input type="hidden" name="accion" value="registrar"/>

        <label for="r_mascota">ID Mascota:</label>
        <input type="number" name="r_mascota" id="r_mascota" required/>

        <label for="r_usuario">ID Usuario:</label>
        <input type="number" name="r_usuario" id="r_usuario" required/>

        <label for="fecha_desaparicion">Fecha de Desaparición:</label>
        <input type="date" name="fecha_desaparicion" id="fecha_desaparicion" required/>

        <label for="ubicacionultimavez">Ubicación Última Vez Vista:</label>
        <input type="text" name="ubicacionultimavez" id="ubicacionultimavez" required/>

        <label for="recompensa">Recompensa:</label>
        <input type="number" step="0.01" name="recompensa" id="recompensa" required/>

        <label for="estadoreporte">Estado del Reporte:</label>
        <select name="estadoreporte" id="estadoreporte">
            <option value="Abierto">Abierto</option>
            <option value="Cerrado">Cerrado</option>
        </select>

        <label for="fecha_registro">Fecha de Registro:</label>
        <input type="date" name="fecha_registro" id="fecha_registro" required/>

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
        <a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=listar">Listar Reportes</a><br>
        <a href="${pageContext.request.contextPath}/index.jsp">Menú Principal</a>
    </div>
</div>
</body>
</html>