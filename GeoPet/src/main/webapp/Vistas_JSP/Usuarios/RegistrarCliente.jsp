<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Registro de Cliente</title></head>
<body>
  <h2>Registro de Cliente</h2>
  <form action="${pageContext.request.contextPath}/RegistrarClienteServlet" method="post">
    <label>Nombre:</label>
    <input type="text" name="nombre" required /><br/>
    <label>Apellido Paterno:</label>
    <input type="text" name="apellidoPat" required /><br/>
    <label>Apellido Materno:</label>
    <input type="text" name="apellidoMat" required /><br/>
    <label>Email:</label>
    <input type="email" name="email" required /><br/>
    <label>Contraseña:</label>
    <input type="password" name="contrasenia" required /><br/>
    <label>Teléfono:</label>
    <input type="text" name="telefono" required /><br/>
    <label>Dirección:</label>
    <input type="text" name="direccion" required /><br/>
    <label>Ciudad:</label>
    <input type="text" name="ciudad" required /><br/>
    <button type="submit">Registrarse</button>
  </form>

  <c:if test="${not empty msg}">
    <p style="color:green">${msg}</p>
  </c:if>
  <c:if test="${not empty error}">
    <p style="color:red">${error}</p>
  </c:if>

  <p><a href="${pageContext.request.contextPath}/LoginServlet">Volver al login</a></p>
</body>
</html>
