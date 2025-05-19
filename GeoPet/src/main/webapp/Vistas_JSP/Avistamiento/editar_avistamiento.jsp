<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Avistamiento" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Avistamiento</title>
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
    Avistamiento a = (Avistamiento) request.getAttribute("avistamiento");
%>

<div class="contenedor">
    <h2>Editar Avistamiento</h2>

    <form action="AvistamientoServlet" method="post">
        <input type="hidden" name="accion" value="actualizar"/>
        <input type="hidden" name="avistamientoid" value="<%= a.getAvistamientoID() %>"/>

        <label for="r_reporte">ID Reporte:</label>
        <input type="number" id="r_reporte" name="r_reporte" value="<%= a.getR_Reporte() %>" required/>

        <label for="r_usuarioreportante">ID Usuario Reportante:</label>
        <input type="number" id="r_usuarioreportante" name="r_usuarioreportante" value="<%= a.getR_UsuarioReportante() %>" required/>

        <label for="fecha_avistamiento">Fecha del Avistamiento:</label>
        <input type="date" id="fecha_avistamiento" name="fecha_avistamiento" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(a.getFecha_Avistamiento()) %>" required/>

        <label for="ubicacion">Ubicación:</label>
        <input type="text" id="ubicacion" name="ubicacion" value="<%= a.getUbicacion() %>" required/>

        <label for="descripcion">Descripción:</label>
        <textarea id="descripcion" name="descripcion" rows="3" required><%= a.getDescripcion() %></textarea>

        <label for="contacto">Contacto:</label>
        <input type="text" id="contacto" name="contacto" value="<%= a.getContacto() %>" required/>

        <label for="fecha_registro">Fecha de Registro:</label>
        <input type="date" id="fecha_registro" name="fecha_registro" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(a.getFecha_Registro()) %>" required/>

        <label for="r_imagen">ID Imagen:</label>
        <input type="number" id="r_imagen" name="r_imagen" value="<%= a.getR_Imagen() %>" required/>

        <div class="boton">
            <input type="submit" value="Actualizar"/>
        </div>
    </form>

    <div class="enlace">
        <a href="AvistamientoServlet?accion=listar">Volver a la lista</a>
    </div>
</div>
</body>
</html>
