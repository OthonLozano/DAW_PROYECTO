<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.ReporteDesaparicion" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Reporte - GeoPet</title>
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
        }

        .header {
            text-align: center;
            margin-bottom: 30px;
            background: linear-gradient(135deg, #007bff, #0056b3);
            padding: 30px 20px;
            border-radius: 15px;
            color: white;
            box-shadow: 0 4px 15px rgba(0, 123, 255, 0.3);
        }

        .header h1 {
            font-size: 2rem;
            margin-bottom: 10px;
        }

        .header p {
            font-size: 1.1rem;
            opacity: 0.9;
        }

        .form-container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
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
            font-size: 14px;
        }

        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }

        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }

        .form-group textarea {
            resize: vertical;
            min-height: 100px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 0 10px;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-primary {
            background-color: #007bff;
            color: white;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #545b62;
        }

        .form-actions {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .info-box {
            background-color: #d1ecf1;
            border: 1px solid #bee5eb;
            color: #0c5460;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
        }

        .readonly-field {
            background-color: #f8f9fa;
            color: #6c757d;
        }

        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }

            .btn {
                display: block;
                margin: 10px 0;
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>✏️ Editar Reporte</h1>
        <p>Actualiza la información de tu mascota perdida</p>
    </div>

    <%
        ReporteDesaparicion reporte = (ReporteDesaparicion) request.getAttribute("reporte");
        String error = request.getParameter("error");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    %>

    <div class="form-container">
        <!-- Mostrar errores -->
        <% if (error != null) { %>
        <div class="mensaje error">
            <%
                switch (error) {
                    case "fallo_actualizacion":
                        out.print("Error al actualizar el reporte. Inténtalo nuevamente.");
                        break;
                    case "datos_invalidos":
                        out.print("Los datos proporcionados no son válidos.");
                        break;
                    default:
                        out.print("Ha ocurrido un error: " + error);
                }
            %>
        </div>
        <% } %>

        <div class="info-box">
            <strong>📝 Información:</strong> Edita los datos del reporte. Los campos de ID no se pueden modificar.
        </div>

        <form action="ReporteDesaparicionServlet" method="post">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="reporteid" value="<%= reporte.getReporteID() %>">
            <input type="hidden" name="r_mascota" value="<%= reporte.getR_Mascota() %>">

            <div class="form-row">
                <div class="form-group">
                    <label>🆔 ID del Reporte:</label>
                    <input type="text" value="<%= reporte.getReporteID() %>" readonly class="readonly-field">
                </div>
                <div class="form-group">
                    <label>🐾 ID de la Mascota:</label>
                    <input type="text" value="<%= reporte.getR_Mascota() %>" readonly class="readonly-field">
                </div>
            </div>

            <div class="form-group">
                <label for="fecha_desaparicion">📅 Fecha de Desaparición: *</label>
                <input type="date"
                       id="fecha_desaparicion"
                       name="fecha_desaparicion"
                       value="<%= sdf.format(reporte.getFechaDesaparicion()) %>"
                       required>
            </div>

            <div class="form-group">
                <label for="ubicacionultimavez">📍 Ubicación donde se vio por última vez: *</label>
                <input type="text"
                       id="ubicacionultimavez"
                       name="ubicacionultimavez"
                       value="<%= reporte.getUbicacionUltimaVez() != null ? reporte.getUbicacionUltimaVez() : "" %>"
                       placeholder="Ej: Parque Central, cerca de la fuente"
                       required
                       maxlength="200">
            </div>

            <div class="form-group">
                <label for="descripcionsituacion">📝 Descripción de la situación:</label>
                <textarea id="descripcionsituacion"
                          name="descripcionsituacion"
                          placeholder="Describe las circunstancias de la desaparición, comportamiento de la mascota, etc."
                          maxlength="500"><%= reporte.getDescripcionSituacion() != null ? reporte.getDescripcionSituacion() : "" %></textarea>
                <small style="color: #666;">Opcional - Máximo 500 caracteres</small>
            </div>

            <div class="form-group">
                <label for="recompensa">💰 Recompensa (opcional):</label>
                <input type="number"
                       id="recompensa"
                       name="recompensa"
                       value="<%= reporte.getRecompensa() %>"
                       min="0"
                       step="0.01"
                       placeholder="0.00">
                <small style="color: #666;">Ingresa 0 si no ofreces recompensa</small>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    ✅ Actualizar Reporte
                </button>
                <a href="ReporteDesaparicionServlet?accion=mis_reportes" class="btn btn-secondary">
                    ❌ Cancelar
                </a>
            </div>
        </form>
    </div>

    <!-- Footer informativo -->
    <div style="background: white; padding: 20px; border-radius: 10px; margin-top: 30px; text-align: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
        <h4>💡 Consejos para editar tu reporte</h4>
        <p>Mantén la información actualizada y precisa. Si tienes nueva información sobre la ubicación o circunstancias,
            actualiza el reporte para ayudar a quienes puedan encontrar a tu mascota.</p>
    </div>
</div>
</body>
</html>