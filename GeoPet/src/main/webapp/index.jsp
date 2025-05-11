<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu Principal</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: black;
            margin: 40px;
            padding: 20px;
            color: #333;
        }
        .contenedor {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f8f4a7;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #0f75b6;
        }
        section {
            background-color: #ffffff;
            border: 1px solid #ccc;
            border-radius: 8px;
            padding: 15px 25px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        h3 {
            color: #60baf4;
        }
        ul {
            list-style-type: none;
            padding-left: 0;
        }
        li {
            margin: 10px 0;
        }
        a {
            text-decoration: none;
            color: #9dd6fb;
            font-weight: bold;
        }
        a:hover {
            text-decoration: underline;
            text-decoration-color: yellow;
            text-decoration-thickness: 3px;
        }
    </style>
</head>
<body>
<div class = "contenedor">
    <h2>BIENVENIDOS AL MINI PROYECTO A PATIN >.<</h2>

    <section>
        <h3>Seccion Especies</h3>
        <ul>
            <li><a href="Vistas_JSP/Especie/registrar_especies.jsp">Registrar Especies</a></li>
            <li><a href="EspecieServlet?accion=listar">Listar Especies</a></li>
        </ul>
    </section>

    <section>
        <h3>Seccion Mascota</h3>
        <ul>
            <li><a href="registrarMascota.jsp">Registrar Mascota</a></li>
            <li><a href="MascotaServlet?accion=listar">Listar Mascota</a></li>
        </ul>
    </section>
</div>
</body>
</html>
