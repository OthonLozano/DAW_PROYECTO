<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page session="true" %>
<%
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user == null || !"Cliente".equals(user.getUsuario())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=Acceso+no+autorizado");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Panel de Cliente</title>
</head>
<body>
<h1>¡Hola, <%= user.getNombre() %>!</h1>
<p>Bienvenido a tu área de cliente. Aquí puedes:</p>
<ul>
    <li><a href="${pageContext.request.contextPath}/MascotaServlet?accion=listar">Ver mis Mascotas</a></li>
    <li><a href="${pageContext.request.contextPath}/MascotaServlet?accion=nuevo">Registrar Nueva Mascota</a></li>
    <li><a href="${pageContext.request.contextPath}/ReporteDesaparicionServlet?accion=misReportes">Ver mis Reportes</a></li>
    <li><a href="${pageContext.request.contextPath}/AvistamientoServlet?accion=listar">Buscar Avistamientos</a></li>
    <li><a href="${pageContext.request.contextPath}/ComentarioServlet?accion=misComentarios">Mis Comentarios</a></li>
</ul>

<form action="${pageContext.request.contextPath}/LogoutServlet" method="post">
    <button type="submit">Cerrar Sesión</button>
</form>
</body>
</html>
