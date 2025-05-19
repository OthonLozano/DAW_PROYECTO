<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Raza</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: black;
            color: #333;
            padding: 40px;
        }

        .contenedor {
            max-width: 700px;
            margin: auto;
            background-color: #f8f4a7;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.3);
        }

        h2 {
            text-align: center;
            color: #0f75b6;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin: 10px 0 5px;
            font-weight: bold;
        }

        input {
            padding: 8px;
            border-radius: 5px;
            border: 1px solid #aaa;
        }

        input[type="submit"] {
            margin-top: 20px;
            background-color: #0f75b6;
            color: white;
            border: none;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #0c5a8c;
        }

        .links {
            text-align: center;
            margin-top: 20px;
        }

        .links a {
            color: #0f75b6;
            text-decoration: none;
            margin: 0 10px;
        }

        .links a:hover {
            text-decoration: underline;
            text-decoration-color: black;
        }
    </style>
</head>
<body>
<div class="contenedor">
    <h2>Registrar Nueva Raza</h2>
    <form action="RazaServlet" method="post">
        <input type="hidden" name="accion" value="registrar" />

        <label for="nombre">Nombre:</label>
        <input type="text" name="nombre" required />

        <label for="descripcion">Descripción:</label>
        <input type="text" name="descripcion" required />

        <input type="submit" value="Registrar" />
    </form>

    <div class="links">
        <a href="RazaServlet?accion=listar">Volver a la lista</a>
        <a href="index.jsp">Menú Principal</a>
    </div>
</div>
</body>
</html>
