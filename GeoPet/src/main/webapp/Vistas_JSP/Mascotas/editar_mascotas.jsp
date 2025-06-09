<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Mascota</title>
</head>
<body>
<div class="contenedor">
    <h2>Editar Mascota</h2>

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

    <%
        Mascotas m = (Mascotas) request.getAttribute("mascota");
        if (m != null) {
    %>
    <div class="info-usuario">
        Editando mascota "<%= m.getNombre() %>" (ID: <%= m.getMascotaID() %>)
    </div>

    <form action="MascotaServlet" method="post">
        <input type="hidden" name="accion" value="actualizar" />
        <input type="hidden" name="mascotaid" value="<%= m.getMascotaID() %>" />

        <label for="nombre">Nombre:</label>
        <input type="text" name="nombre" value="<%= m.getNombre() %>" required />

        <label for="r_especie">Especie:</label>
        <select name="r_especie" required>
            <option value="">Seleccionar especie...</option>
            <%
                if (especies != null && !especies.isEmpty()) {
                    for (Especie especie : especies) {
                        boolean selected = especie.getEspecieID() == m.getR_Especie();
            %>
            <option value="<%= especie.getEspecieID() %>" <%= selected ? "selected" : "" %>>
                <%= especie.getNombre() %>
            </option>
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
        <input type="number" name="edad" value="<%= m.getEdad() %>" required min="0" />

        <label for="sexo">Sexo:</label>
        <select name="sexo" required>
            <option value="">Seleccionar...</option>
            <option value="Macho" <%= "Macho".equalsIgnoreCase(m.getSexo()) ? "selected" : "" %>>Macho</option>
            <option value="Hembra" <%= "Hembra".equalsIgnoreCase(m.getSexo()) ? "selected" : "" %>>Hembra</option>
        </select>

        <label for="color">Color:</label>
        <input type="text" name="color" value="<%= m.getColor() %>" required />

        <label for="caracteristicasdistintivas">Características Distintivas:</label>
        <input type="text" name="caracteristicasdistintivas" value="<%= m.getCaracteristicasDistintivas() != null ? m.getCaracteristicasDistintivas() : "" %>" />

        <label for="microchip">¿Tiene microchip?</label>
        <select name="microchip" required>
            <option value="">Seleccionar...</option>
            <option value="true" <%= m.isMicrochip() ? "selected" : "" %>>Sí</option>
            <option value="false" <%= !m.isMicrochip() ? "selected" : "" %>>No</option>
        </select>

        <label for="numero_microchip">Número de Microchip (opcional):</label>
        <input type="number" name="numero_microchip" value="<%= m.getNumero_Microchip() != 0 ? String.valueOf(m.getNumero_Microchip()) : "" %>" />

        <label for="estado">Estado:</label>
        <select name="estado" required>
            <option value="">Seleccionar...</option>
            <option value="Perdida" <%= "Perdida".equalsIgnoreCase(m.getEstado()) ? "selected" : "" %>>Perdida</option>
            <option value="En casa" <%= "En casa".equalsIgnoreCase(m.getEstado()) ? "selected" : "" %>>En casa</option>
        </select>

        <label for="fecha_registro">Fecha de Registro:</label>
        <input type="date" name="fecha_registro" id="fecha_registro" value="<%= m.getFecha_Registro() %>" required />

        <input type="submit" value="Actualizar" />
    </form>

    <%
    } else {
    %>
    <div class="mensaje" style="background-color: #f8d7da; color: #721c24;">
        <strong>ERROR:</strong> No se encontró la mascota a editar.
    </div>
    <%
        }
    %>

    <div class="links">
        <a href="MascotaServlet?accion=listar">Volver a la lista</a>
        <a href="EspecieServlet?accion=listar">Gestionar Especies</a>
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