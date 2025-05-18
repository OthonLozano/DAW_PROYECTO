<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page session="true" %>
<%
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"Admin".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Panel de Administración</title>
</head>
<body>
<h1>Bienvenido, <%= user.getNombre() %> <%= user.getApellidoPat() %>!</h1>
<p>Desde aquí puedes gestionar tu aplicación:</p>
<ul>
    <li><a href="${pageContext.request.contextPath}/EspecieServlet?accion=listar">Gestionar Especies</a></li>
    <li><a href="${pageContext.request.contextPath}/RazaServlet?accion=listar">Gestionar Razas</a></li>
    <li>Ver Reportes de Desaparición</li>
</ul>

<form action="${pageContext.request.contextPath}/LogoutServlet" method="post">
    <button type="submit">Cerrar Sesión</button>
</form>
</body>
</html>
