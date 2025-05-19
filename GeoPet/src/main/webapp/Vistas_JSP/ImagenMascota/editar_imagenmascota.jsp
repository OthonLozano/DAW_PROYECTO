<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.ImagenMascota" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Imagen de Mascota</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: black;
            padding: 40px;
        }

        .contenedor {
            max-width: 700px;
            margin: 0 auto;
            background-color: #f8f4a7;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            color: #0f75b6;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            box-sizing: border-box;
        }

        .boton {
            margin-top: 20px;
            text-align: center;
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

        .enlace {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
    </style>
</head>
<body>
<%
    ImagenMascota i = (ImagenMascota) request.getAttribute("imagen");
%>

<div class="contenedor">
    <h2>Editar Imagen de Mascota</h2>

    <form action="ImagenMascotaServlet" method="post">
        <input type="hidden" name="accion" value="actualizar"/>
        <input type="hidden" name="imagenid" value="<%= i.getImagenID() %>"/>

        <label for="r_mascota">ID Mascota:</label>
        <input type="number" id="r_mascota" name="r_mascota" value="<%= i.getR_Mascota() %>" required/>

        <label for="url_imagen">ID de URL de Imagen:</label>
        <input type="number" id="url_imagen" name="url_imagen" value="<%= i.getURL_Imagen() %>" required/>

        <label for="fecha_carga">Fecha de Carga:</label>
        <input type="date" id="fecha_carga" name="fecha_carga" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(i.getFecha_Carga()) %>" required/>

        <div class="boton">
            <input type="submit" value="Actualizar"/>
        </div>
    </form>

    <div class="enlace">
        <a href="ImagenMascotaServlet?accion=listar">Volver a la lista</a>
    </div>
</div>
</body>
</html>