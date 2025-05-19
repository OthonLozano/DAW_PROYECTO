<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Comentarios" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Comentario</title>
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
        input, textarea {
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
    Comentarios c = (Comentarios) request.getAttribute("comentario");
%>

<div class="contenedor">
    <h2>Editar Comentario</h2>

    <form action="ComentariosServlet" method="post">
        <input type="hidden" name="accion" value="actualizar"/>
        <input type="hidden" name="comentarioid" value="<%= c.getComentarioID() %>"/>

        <label for="r_usuario">ID Usuario:</label>
        <input type="number" id="r_usuario" name="r_usuario" value="<%= c.getR_Usuario() %>" required/>

        <label for="r_reporte">ID Reporte:</label>
        <input type="number" id="r_reporte" name="r_reporte" value="<%= c.getR_Reporte() %>" required/>

        <label for="contenido">Contenido:</label>
        <textarea id="contenido" name="contenido" rows="3" required><%= c.getContenido() %></textarea>

        <label for="fecha_comentario">Fecha del Comentario:</label>
        <input type="date" id="fecha_comentario" name="fecha_comentario" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(c.getFecha_Comentario()) %>" required/>

        <div class="boton">
            <input type="submit" value="Actualizar"/>
        </div>
    </form>

    <div class="enlace">
        <a href="ComentariosServlet?accion=listar">Volver a la lista</a>
    </div>
</div>
</body>
</html>
