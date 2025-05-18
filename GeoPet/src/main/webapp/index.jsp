<%@ page import="Modelo.JavaBeans.Usuarios" %>
<%@ page session="true" %>
<%
    // Si ya hay un usuario en sesión, redirige a su dashboard
    Usuarios user = (Usuarios) session.getAttribute("usuario");
    if (user != null) {
        String rol = user.getUsuario();  // "SuperAdmin", "Admin" o "Cliente"
        String ctx = request.getContextPath();
        switch (rol) {
            case "SuperAdmin":
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeSuperAdmin.jsp");
                break;
            case "Admin":
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeAdmin.jsp");
                break;
            case "Cliente":
                response.sendRedirect(ctx + "/Vistas_JSP/Usuarios/HomeCliente.jsp");
                break;
            default:
                // sesión inválida por rol extraño: invalidamos y a login
                session.invalidate();
                response.sendRedirect(ctx + "/login.jsp?error=Rol+no+válido");
        }
        return;  // importante: nada más de este JSP se ejecuta
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Iniciar Sesión</title>
</head>
<body>
<h2>Iniciar Sesión</h2>
<form action="${pageContext.request.contextPath}/LoginServlet" method="post">
    <label for="email">Email:</label>
    <input type="email" name="email" required /><br/>
    <label for="contrasenia">Contraseña:</label>
    <input type="password" name="contrasenia" required /><br/>
    <button type="submit">Ingresar</button>
</form>

<c:if test="${param.error != null}">
    <p style="color:red">${param.error}</p>
</c:if>

<p>¿No tienes cuenta?
    <a href="${pageContext.request.contextPath}/RegistrarClienteServlet">Regístrate aquí</a>
</p>
</body>
</html>
