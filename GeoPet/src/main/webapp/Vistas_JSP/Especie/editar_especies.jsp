<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Especie</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: black;
            padding: 40px;
        }
        .contenedor {
            max-width: 500px;
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
        input[type="text"] {
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
            color:#0f75b6;
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
    Especie especie = (Especie) request.getAttribute("especie");
%>

<div class="contenedor">
    <h2>Editar Especie</h2>

    <form action="EspecieServlet" method="post">
        <input type="hidden" name="accion" value="actualizar"/>
        <input type="hidden" name="id" value="<%= especie.getEspecieID() %>"/>

        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" value="<%= especie.getNombre() %>" required/>

        <label for="descripcion">Descripción:</label>
        <input type="text" id="descripcion" name="descripcion" value="<%= especie.getDescripcion() %>" required/>

        <div class="boton">
            <input type="submit" value="Actualizar"/>
        </div>

    </form>

    <div class="enlace">
        <li><a href="EspecieServlet?accion=listar">Listar Especies</a></li>
    </div>
</div>
</body>
</html>
