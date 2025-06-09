<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportar Mascota Perdida - GeoPet</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f5f5f5;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #dc3545, #c82333);
            color: white;
            padding: 30px;
            text-align: center;
        }

        .header h1 {
            font-size: 2rem;
            margin-bottom: 10px;
        }

        .form-container {
            padding: 40px;
        }

        .mensaje {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            text-align: center;
            font-weight: bold;
        }

        .mensaje.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }

        .form-group.required label::after {
            content: " *";
            color: #dc3545;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: #dc3545;
            box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.1);
        }

        select.form-control {
            cursor: pointer;
        }

        textarea.form-control {
            resize: vertical;
            min-height: 100px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .help-text {
            font-size: 14px;
            color: #666;
            margin-top: 5px;
        }

        .btn-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background-color: #dc3545;
            color: white;
        }

        .btn-primary:hover {
            background-color: #c82333;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #545b62;
        }

        .alert {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
        }

        .alert-icon {
            font-size: 18px;
            margin-right: 10px;
        }

        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }

            .btn-group {
                flex-direction: column;
            }

            .form-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>🚨 Reportar Mascota Perdida</h1>
        <p>Completa la información para ayudar a encontrar tu mascota</p>
    </div>

    <div class="form-container">
        <!-- Mostrar mensajes de error -->
        <%
            String mensaje = (String) request.getAttribute("mensaje");
            if (mensaje != null) {
        %>
        <div class="mensaje error">
            <%= mensaje %>
        </div>
        <% } %>

        <!-- Alerta informativa -->
        <div class="alert">
            <span class="alert-icon">⚠️</span>
            <strong>Importante:</strong> Asegúrate de proporcionar información precisa y actualizada.
            Esto ayudará a que más personas puedan identificar y contactarte sobre tu mascota.
        </div>

        <!-- Verificar que el usuario tenga mascotas -->
        <%
            List<Mascotas> mascotasUsuario = (List<Mascotas>) request.getAttribute("mascotasUsuario");
            if (mascotasUsuario == null || mascotasUsuario.isEmpty()) {
        %>
        <div class="alert">
            <span class="alert-icon">❌</span>
            <strong>No tienes mascotas registradas.</strong><br>
            Primero debes <a href="MascotaServlet?accion=registrar">registrar tu mascota</a>
            antes de poder reportar su desaparición.
        </div>
        <div class="btn-group">
            <a href="MascotaServlet?accion=registrar" class="btn btn-primary">
                Registrar Mascota
            </a>
            <a href="ReporteDesaparicionServlet?accion=listar" class="btn btn-secondary">
                Volver a Reportes
            </a>
        </div>
        <% } else { %>

        <!-- Formulario de reporte -->
        <form action="ReporteDesaparicionServlet" method="post">
            <input type="hidden" name="accion" value="registrar">

            <!-- Selección de mascota -->
            <div class="form-group required">
                <label for="mascota_id">Mascota Perdida</label>
                <select name="mascota_id" id="mascota_id" class="form-control" required>
                    <option value="">Selecciona tu mascota...</option>
                    <%
                        for (Mascotas mascota : mascotasUsuario) {
                    %>
                    <option value="<%= mascota.getMascotaID() %>">
                        <%= mascota.getNombre() %> - <%= mascota.getEdad() %> años
                    </option>
                    <% } %>
                </select>
            </div>

            <!-- Fecha y ubicación -->
            <div class="form-row">
                <div class="form-group required">
                    <label for="fecha_desaparicion">Fecha de Desaparición</label>
                    <input type="date" name="fecha_desaparicion" id="fecha_desaparicion"
                           class="form-control" required>
                    <div class="help-text">¿Cuándo fue la última vez que viste a tu mascota?</div>
                </div>

                <div class="form-group required">
                    <label for="estado_reporte">Estado del Reporte</label>
                    <select name="estado_reporte" id="estado_reporte" class="form-control" required>
                        <option value="">Selecciona el estado...</option>
                        <option value="Perdido">Perdido</option>
                        <option value="Encontrado">Encontrado</option>
                    </select>
                </div>
            </div>

            <!-- Ubicación última vez -->
            <div class="form-group required">
                <label for="ubicacion_ultima_vez">Ubicación Donde se Perdió</label>
                <input type="text" name="ubicacion_ultima_vez" id="ubicacion_ultima_vez"
                       class="form-control" required maxlength="64"
                       placeholder="Ej: Parque Central, Colonia Centro, cerca del mercado...">
                <div class="help-text">Sé lo más específico posible sobre el lugar</div>
            </div>

            <!-- Descripción de la situación -->
            <div class="form-group">
                <label for="descripcion_situacion">Descripción de la Situación</label>
                <textarea name="descripcion_situacion" id="descripcion_situacion"
                          class="form-control" maxlength="64" rows="4"
                          placeholder="Describe las circunstancias de la desaparición: ¿se escapó del patio? ¿se perdió durante un paseo? ¿otros detalles importantes?"></textarea>
                <div class="help-text">Proporciona detalles que puedan ayudar en la búsqueda</div>
            </div>

            <!-- Recompensa -->
            <div class="form-group">
                <label for="recompensa">Recompensa (Opcional)</label>
                <input type="number" name="recompensa" id="recompensa"
                       class="form-control" min="0" step="0.01"
                       placeholder="0.00">
                <div class="help-text">Si ofreces una recompensa por encontrar tu mascota</div>
            </div>

            <!-- Botones -->
            <div class="btn-group">
                <button type="submit" class="btn btn-primary">
                    🚨 Registrar Reporte
                </button>
                <a href="ReporteDesaparicionServlet?accion=listar" class="btn btn-secondary">
                    Cancelar
                </a>
            </div>
        </form>

        <% } %>
    </div>
</div>

<script>
    // Establecer la fecha máxima como hoy
    document.addEventListener('DOMContentLoaded', function() {
        const fechaInput = document.getElementById('fecha_desaparicion');
        const today = new Date().toISOString().split('T')[0];
        fechaInput.setAttribute('max', today);
    });

    // Validación del formulario
    document.querySelector('form')?.addEventListener('submit', function(e) {
        const mascotaSelect = document.getElementById('mascota_id');
        const fechaInput = document.getElementById('fecha_desaparicion');
        const ubicacionInput = document.getElementById('ubicacion_ultima_vez');
        const estadoSelect = document.getElementById('estado_reporte');

        if (!mascotaSelect.value) {
            alert('Por favor selecciona una mascota.');
            e.preventDefault();
            return;
        }

        if (!fechaInput.value) {
            alert('Por favor ingresa la fecha de desaparición.');
            e.preventDefault();
            return;
        }

        if (!ubicacionInput.value.trim()) {
            alert('Por favor describe la ubicación donde se perdió.');
            e.preventDefault();
            return;
        }

        if (!estadoSelect.value) {
            alert('Por favor selecciona el estado del reporte.');
            e.preventDefault();
            return;
        }

        // Confirmar envío
        if (!confirm('¿Estás seguro de que quieres registrar este reporte de desaparición?')) {
            e.preventDefault();
        }
    });
</script>
</body>
</html>