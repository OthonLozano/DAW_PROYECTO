<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Mascota</title>
</head>
<body>
<div class="contenedor">
    <h2>Registrar Mascota</h2>

    <!-- Mostrar mensaje de éxito si existe -->
    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="mensaje">
        <%= request.getAttribute("mensaje") %>
    </div>
    <% } %>

    <!-- Debug de especies -->
    <%
        List<Especie> especies = (List<Especie>) request.getAttribute("especies");
        if (especies == null) {
    %>
    <div class="debug">Debug: Lista de especies es null</div>
    <%
    } else {
    %>
    <div class="debug">Debug: Se encontraron <%= especies.size() %> especies</div>
    <%
        }
    %>

    <!-- Mostrar información del usuario logueado -->
    <%
        // Obtener el ID del usuario directamente de la sesión o del atributo
        Integer usuarioId = (Integer) request.getAttribute("usuarioId");
        if (usuarioId == null) {
            usuarioId = (Integer) session.getAttribute("usuarioId");
        }
        if (usuarioId != null) {
    %>
    <div class="info-usuario">
        Registrando mascota para el usuario ID: <%= usuarioId %>
    </div>
    <%
    } else {
    %>
    <div class="mensaje" style="background-color: #f8d7da; color: #721c24;">
        <strong>AVISO:</strong> No hay usuario logueado en la sesión.
    </div>
    <%
        }
    %>

    <form action="MascotaServlet" method="post">
        <input type="hidden" name="accion" value="registrar" />

        <label for="nombre">Nombre:</label>
        <input type="text" name="nombre" required />

        <label for="r_especie">Especie:</label>
        <select name="r_especie" required>
            <option value="">Seleccionar especie...</option>
            <%
                if (especies != null && !especies.isEmpty()) {
                    for (Especie especie : especies) {
            %>
            <option value="<%= especie.getEspecieID() %>"><%= especie.getNombre() %></option>
            <%
                }
            } else {
            %>
            <option value="" disabled>No hay especies disponibles</option>
            <%
                }
            %>
        </select>

        <label for="edad">Edad (en meses):</label>
        <input type="number" name="edad" required min="0" />

        <label for="sexo">Sexo:</label>
        <select name="sexo" required>
            <option value="">Seleccionar...</option>
            <option value="Macho">Macho</option>
            <option value="Hembra">Hembra</option>
        </select>

        <label for="color">Color:</label>
        <input type="text" name="color" required />

        <label for="caracteristicasdistintivas">Características Distintivas:</label>
        <input type="text" name="caracteristicasdistintivas" />

        <label for="microchip">¿Tiene microchip?</label>
        <select name="microchip" required>
            <option value="">Seleccionar...</option>
            <option value="true">Sí</option>
            <option value="false">No</option>
        </select>

        <label for="numero_microchip">Número de Microchip (opcional):</label>
        <input type="number" name="numero_microchip" />

        <label for="estado">Estado:</label>
        <select name="estado" required>
            <option value="">Seleccionar...</option>
            <option value="Perdida">Perdida</option>
            <option value="En casa">En casa</option>
        </select>

        <label for="fecha_registro">Fecha de Registro:</label>
        <input type="date" name="fecha_registro" id="fecha_registro" required />

        <input type="submit" value="Registrar" />
    </form>

    <div class="links">
        <a href="MascotaServlet?accion=listar">Volver a la lista</a>
        <a href="index.jsp">Menú Principal</a>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var fechaInput = document.getElementById('fecha_registro');

        // Obtener la fecha actual en la zona horaria local
        var today = new Date();
        var year = today.getFullYear();
        var month = String(today.getMonth() + 1).padStart(2, '0');
        var day = String(today.getDate()).padStart(2, '0');
        var todayString = year + '-' + month + '-' + day;

        // Establecer la fecha actual como valor por defecto
        fechaInput.value = todayString;

        // Establecer la fecha máxima como hoy
        fechaInput.max = todayString;

        // Establecer una fecha mínima (10 años atrás)
        var tenYearsAgo = new Date();
        tenYearsAgo.setFullYear(tenYearsAgo.getFullYear() - 10);
        var minYear = tenYearsAgo.getFullYear();
        var minMonth = String(tenYearsAgo.getMonth() + 1).padStart(2, '0');
        var minDay = String(tenYearsAgo.getDate()).padStart(2, '0');
        fechaInput.min = minYear + '-' + minMonth + '-' + minDay;
    });
</script>
</body>
</html>