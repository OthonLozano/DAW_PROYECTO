<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page session="true" %>
<%
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"SuperAdmin".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Panel SuperAdmin</title>
</head>
<body>
<h1>Bienvenido, SuperAdmin <%= user.getNombre() %>!</h1>
<p>Desde aquí puedes:</p>
<ul>
    <li>Gestionar Administradores</li>
    <li>Ver todos los usuarios</li>
    <li>Ver reportes globales</li>
    <li>Añadir SuperAdmin</li>
    <!-- añade más enlaces de alto nivel -->
</ul>

<form action="${pageContext.request.contextPath}/LogoutServlet" method="post">
    <button type="submit">Cerrar Sesión</button>
</form>
</body>
</html>
