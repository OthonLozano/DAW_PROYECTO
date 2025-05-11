<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar sesión</title>
    <style>
        body { font-family: Arial; background: #f4f4f4; height: 100vh; display: flex; justify-content: center; align-items: center; }
        .login-box { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); width: 300px; }
        input[type=text], input[type=password] { width: 100%; padding: 10px; margin-top: 10px; border-radius: 4px; border: 1px solid #ccc; }
        input[type=submit] { margin-top: 15px; background: #3498db; color: white; padding: 10px; border: none; width: 100%; border-radius: 4px; cursor: pointer; }
        input[type=submit]:hover { background: #2980b9; }
        .error { color: red; margin-top: 10px; }
    </style>

    <script>
        function validateForm() {
            var user = document.getElementById("identificador").value;
            var pass = document.getElementById("contrasenia").value;

            if (user == "" || pass == "") {
                alert("Usuario y contraseña son requeridos.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<div class="login-box">
    <h2>Iniciar Sesión</h2>

    <form method="post" action="ServletLogin" onsubmit="return validateForm();">
        <label for="identificador">Usuario o Email:</label>
        <input type="text" id="identificador" name="identificador" required aria-label="Nombre de usuario o email">

        <label for="contrasenia">Contraseña:</label>
        <input type="password" id="contrasenia" name="contrasenia" required aria-label="Contraseña">

        <input type="submit" value="Entrar">
    </form>
</div>
</body>
</html>
