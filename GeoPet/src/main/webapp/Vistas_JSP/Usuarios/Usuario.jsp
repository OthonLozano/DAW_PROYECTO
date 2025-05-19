<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page import="java.util.List" %>

<%
  // Verificar si el usuario está autenticado
  HttpSession sesion = request.getSession(false);
  if (sesion == null || sesion.getAttribute("usuario") == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  // Obtener la lista de usuarios
  List<Usuarios> usuarios = (List<Usuarios>) request.getAttribute("usuarios");
  if (usuarios == null) {
    response.sendRedirect("ServletUsuario");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Gestión de Usuarios</title>
  <style>
    body { font-family: Arial; background: #f4f4f4; margin: 0; padding: 20px; }
    h1 { text-align: center; color: #333; }
    table { width: 100%; border-collapse: collapse; background: #fff; }
    th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }
    form { margin: 20px auto; background: #fff; padding: 20px; border-radius: 8px; width: 90%; }
    input[type=text], input[type=email], input[type=password] {
      width: 100%; padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px;
    }
    input[type=submit] {
      background-color: #3498db; color: white; padding: 10px 20px;
      border: none; border-radius: 4px; cursor: pointer;
    }
    input[type=submit]:hover { background-color: #2980b9; }
    .acciones form { display: inline; }
  </style>
</head>
<body>

<h1>Gestión de Usuarios</h1>

<!-- Formulario para nuevo usuario -->
<form action="ServletUsuario" method="post">
  <h3>Nuevo Usuario</h3>
  <input type="text" name="nombre" placeholder="Nombre" required>
  <input type="text" name="apellidoPat" placeholder="Apellido Paterno">
  <input type="text" name="apellidoMat" placeholder="Apellido Materno">
  <input type="email" name="email" placeholder="Correo" required>
  <input type="password" name="contrasenia" placeholder="Contraseña" required>
  <input type="text" name="telefono" placeholder="Teléfono">
  <input type="text" name="direccion" placeholder="Dirección">
  <input type="text" name="ciudad" placeholder="Ciudad">
  <input type="submit" value="Agregar Usuario">
</form>

<!-- Lista de usuarios -->
<table>
  <thead>
  <tr>
    <th>ID</th><th>Nombre</th><th>Email</th><th>Teléfono</th><th>Ciudad</th><th>Acciones</th>
  </tr>
  </thead>
  <tbody>
  <%
    if (usuarios != null) {
      for (Usuarios u : usuarios) {  // Asegúrate de usar la clase Usuarios
  %>
  <tr>
    <td><%= u.getUsuarioID() %></td>
    <td><%= u.getNombre() + " " + u.getApellidoPat() + " " + u.getApellidoMat() %></td>
    <td><%= u.getEmail() %></td>
    <td><%= u.getTelefono() %></td>
    <td><%= u.getCiudad() %></td>
    <td class="acciones">
      <form action="EditarUsuario.jsp" method="get">
        <input type="hidden" name="id" value="<%= u.getUsuarioID() %>">
        <input type="submit" value="Editar">
      </form>
      <form action="EliminarUsuario" method="post">
        <input type="hidden" name="id" value="<%= u.getUsuarioID() %>">
        <input type="submit" value="Eliminar" onclick="return confirm('¿Eliminar este usuario?');">
      </form>
    </td>
  </tr>
  <%
      }
    }
  %>
  </tbody>
</table>

</body>
</html>
