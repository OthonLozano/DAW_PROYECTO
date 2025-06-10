<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.JavaBeans.ReporteDesaparicion" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Reporte - GeoPet</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            min-height: 100vh;
        }

        .main-header {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
            position: relative;
            overflow: hidden;
        }

        .main-header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="75" cy="75" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="50" cy="10" r="0.5" fill="rgba(255,255,255,0.1)"/><circle cx="10" cy="60" r="0.5" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
            pointer-events: none;
        }

        .header-content {
            position: relative;
            z-index: 1;
            text-align: center;
        }

        .header-title {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }

        .header-subtitle {
            font-size: 1.1rem;
            opacity: 0.9;
            margin-bottom: 0;
        }

        .form-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 2.5rem;
            margin-bottom: 2rem;
            border-top: 4px solid #2e7d32;
        }

        .form-section-title {
            color: #2e7d32;
            font-size: 1.25rem;
            font-weight: bold;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid #e9ecef;
        }

        .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.25rem;
        }

        .form-label .required {
            color: #dc3545;
            font-weight: bold;
        }

        .form-control, .form-select {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 0.75rem 1rem;
            font-size: 0.95rem;
            transition: all 0.3s ease;
            background-color: #fff;
        }

        .form-control:focus, .form-select:focus {
            border-color: #2e7d32;
            box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
            transform: translateY(-1px);
        }

        .form-control.readonly-field {
            background-color: #f8f9fa;
            color: #6c757d;
            border-color: #ced4da;
            cursor: not-allowed;
        }

        .form-text {
            color: #6c757d;
            font-size: 0.85rem;
            margin-top: 0.25rem;
        }

        .btn-primary-geopet {
            background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 0.875rem 2rem;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
        }

        .btn-primary-geopet:hover {
            background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(46, 125, 50, 0.4);
            color: white;
        }

        .btn-secondary-geopet {
            background: #6c757d;
            border: none;
            color: white;
            font-weight: 600;
            padding: 0.875rem 2rem;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-secondary-geopet:hover {
            background: #5a6268;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
            color: white;
        }

        .alert-geopet {
            border-radius: 12px;
            padding: 1rem;
            margin-bottom: 1.5rem;
            border: none;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .alert-danger-geopet {
            background: linear-gradient(135deg, #f8d7da 0%, #f5c2c7 100%);
            color: #721c24;
            border-left: 4px solid #dc3545;
        }

        .alert-info-geopet {
            background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
            color: #0c5460;
            border-left: 4px solid #17a2b8;
        }

        .info-card {
            background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
            border: none;
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            border-left: 4px solid #2196f3;
            box-shadow: 0 2px 10px rgba(33, 150, 243, 0.1);
        }

        .tips-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 2rem;
            margin-top: 2rem;
            border-top: 4px solid #4caf50;
        }

        .tips-title {
            color: #2e7d32;
            font-size: 1.25rem;
            font-weight: bold;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .tips-content {
            color: #495057;
            line-height: 1.6;
        }

        .form-actions {
            margin-top: 2rem;
            padding-top: 1.5rem;
            border-top: 2px solid #e9ecef;
            text-align: center;
        }

        .input-group-text {
            background-color: #f8f9fa;
            border: 2px solid #e9ecef;
            border-right: none;
            color: #495057;
        }

        .input-group .form-control {
            border-left: none;
        }

        .floating-label {
            position: relative;
        }

        .character-count {
            position: absolute;
            bottom: 0.5rem;
            right: 0.75rem;
            font-size: 0.75rem;
            color: #6c757d;
            background: white;
            padding: 0 0.25rem;
        }

        @media (max-width: 768px) {
            .header-title {
                font-size: 2rem;
            }

            .form-container {
                padding: 1.5rem;
            }

            .btn-primary-geopet,
            .btn-secondary-geopet {
                width: 100%;
                margin-bottom: 0.5rem;
            }
        }

        /* Animaciones */
        .form-container {
            animation: slideInUp 0.6s ease-out;
        }

        @keyframes slideInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .form-control:focus {
            animation: focusPulse 0.3s ease-out;
        }

        @keyframes focusPulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.02); }
            100% { transform: scale(1); }
        }
    </style>
</head>
<body>
<%
    ReporteDesaparicion reporte = (ReporteDesaparicion) request.getAttribute("reporte");
    String error = request.getParameter("error");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>

<!-- Header principal -->
<div class="main-header">
    <div class="container">
        <div class="header-content">
            <h1 class="header-title">
                <i class="bi bi-pencil-square me-3"></i>Editar Reporte
            </h1>
            <p class="header-subtitle">Actualiza la información de tu mascota perdida</p>
        </div>
    </div>
</div>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <!-- Mostrar errores -->
            <% if (error != null) { %>
            <div class="alert alert-geopet alert-danger-geopet">
                <div class="d-flex align-items-center">
                    <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
                    <div>
                        <strong>Error:</strong>
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
                </div>
            </div>
            <% } %>

            <!-- Información importante -->
            <div class="info-card">
                <div class="d-flex align-items-start">
                    <i class="bi bi-info-circle-fill me-3 fs-4 text-primary"></i>
                    <div>
                        <strong>Información importante:</strong>
                        <p class="mb-0 mt-1">Edita los datos del reporte para mantener la información actualizada. Los campos de ID no se pueden modificar para preservar la integridad de los datos.</p>
                    </div>
                </div>
            </div>

            <!-- Formulario principal -->
            <div class="form-container">
                <h2 class="form-section-title">
                    <i class="bi bi-file-text"></i>Información del Reporte
                </h2>

                <form action="ReporteDesaparicionServlet" method="post" id="editForm">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="hidden" name="reporteid" value="<%= reporte.getReporteID() %>">
                    <input type="hidden" name="r_mascota" value="<%= reporte.getR_Mascota() %>">

                    <!-- IDs del reporte (solo lectura) -->
                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="bi bi-hash"></i>ID del Reporte
                            </label>
                            <input type="text" class="form-control readonly-field"
                                   value="<%= reporte.getReporteID() %>" readonly>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">
                                <i class="bi bi-heart"></i>ID de la Mascota
                            </label>
                            <input type="text" class="form-control readonly-field"
                                   value="<%= reporte.getR_Mascota() %>" readonly>
                        </div>
                    </div>

                    <!-- Fecha de desaparición -->
                    <div class="mb-4">
                        <label for="fecha_desaparicion" class="form-label">
                            <i class="bi bi-calendar-event"></i>Fecha de Desaparición <span class="required">*</span>
                        </label>
                        <input type="date"
                               class="form-control"
                               id="fecha_desaparicion"
                               name="fecha_desaparicion"
                               value="<%= sdf.format(reporte.getFechaDesaparicion()) %>"
                               required>
                        <div class="form-text">Selecciona la fecha exacta en que desapareció tu mascota</div>
                    </div>

                    <!-- Ubicación -->
                    <div class="mb-4">
                        <label for="ubicacionultimavez" class="form-label">
                            <i class="bi bi-geo-alt"></i>Ubicación donde se vio por última vez <span class="required">*</span>
                        </label>
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="bi bi-map"></i>
                            </span>
                            <input type="text"
                                   class="form-control"
                                   id="ubicacionultimavez"
                                   name="ubicacionultimavez"
                                   value="<%= reporte.getUbicacionUltimaVez() != null ? reporte.getUbicacionUltimaVez() : "" %>"
                                   placeholder="Ej: Parque Central, cerca de la fuente"
                                   required
                                   maxlength="200">
                        </div>
                        <div class="form-text">Proporciona la ubicación más específica posible</div>
                    </div>

                    <!-- Descripción de la situación -->
                    <div class="mb-4">
                        <label for="descripcionsituacion" class="form-label">
                            <i class="bi bi-chat-text"></i>Descripción de la situación
                        </label>
                        <div class="floating-label">
                            <textarea class="form-control"
                                      id="descripcionsituacion"
                                      name="descripcionsituacion"
                                      rows="4"
                                      placeholder="Describe las circunstancias de la desaparición, comportamiento de la mascota, si estaba asustada, etc."
                                      maxlength="500"
                                      oninput="updateCharCount(this, 500)"><%= reporte.getDescripcionSituacion() != null ? reporte.getDescripcionSituacion() : "" %></textarea>
                            <div class="character-count" id="descripcion-count">
                                <%= reporte.getDescripcionSituacion() != null ? reporte.getDescripcionSituacion().length() : 0 %>/500
                            </div>
                        </div>
                        <div class="form-text">
                            <i class="bi bi-lightbulb"></i>
                            Opcional - Incluye detalles que puedan ayudar a identificar a tu mascota
                        </div>
                    </div>

                    <!-- Recompensa -->
                    <div class="mb-4">
                        <label for="recompensa" class="form-label">
                            <i class="bi bi-currency-dollar"></i>Recompensa (opcional)
                        </label>
                        <div class="input-group">
                            <span class="input-group-text">$</span>
                            <input type="number"
                                   class="form-control"
                                   id="recompensa"
                                   name="recompensa"
                                   value="<%= reporte.getRecompensa() %>"
                                   min="0"
                                   step="0.01"
                                   placeholder="0.00">
                        </div>
                        <div class="form-text">
                            <i class="bi bi-info-circle"></i>
                            Ingresa 0 si no ofreces recompensa. Una recompensa puede incentivar a más personas a ayudar.
                        </div>
                    </div>

                    <!-- Botones de acción -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary-geopet me-3">
                            <i class="bi bi-check-circle me-2"></i>Actualizar Reporte
                        </button>
                        <a href="ReporteDesaparicionServlet?accion=mis_reportes" class="btn btn-secondary-geopet">
                            <i class="bi bi-x-circle me-2"></i>Cancelar
                        </a>
                    </div>
                </form>
            </div>

            <!-- Tarjeta de consejos -->
            <div class="tips-card">
                <h4 class="tips-title">
                    <i class="bi bi-lightbulb-fill"></i>Consejos para editar tu reporte
                </h4>
                <div class="tips-content">
                    <p class="mb-3">
                        <strong>Mantén la información actualizada y precisa.</strong> Si tienes nueva información sobre la ubicación o circunstancias, actualiza el reporte para ayudar a quienes puedan encontrar a tu mascota.
                    </p>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <div class="d-flex align-items-start">
                                <i class="bi bi-check-circle text-success me-2 mt-1"></i>
                                <div>
                                    <strong>Sé específico</strong><br>
                                    <small class="text-muted">Proporciona ubicaciones exactas y detalles específicos</small>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="d-flex align-items-start">
                                <i class="bi bi-check-circle text-success me-2 mt-1"></i>
                                <div>
                                    <strong>Actualiza regularmente</strong><br>
                                    <small class="text-muted">Mantén la información lo más actual posible</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Función para actualizar el contador de caracteres
    function updateCharCount(textarea, maxLength) {
        const currentLength = textarea.value.length;
        const countElement = document.getElementById('descripcion-count');

        if (countElement) {
            countElement.textContent = `${currentLength}/${maxLength}`;

            // Cambiar color según la proximidad al límite
            if (currentLength > maxLength * 0.9) {
                countElement.style.color = '#dc3545';
            } else if (currentLength > maxLength * 0.8) {
                countElement.style.color = '#ffc107';
            } else {
                countElement.style.color = '#6c757d';
            }
        }
    }

    // Validación del formulario
    document.getElementById('editForm').addEventListener('submit', function(e) {
        const fechaDesaparicion = document.getElementById('fecha_desaparicion').value;
        const ubicacion = document.getElementById('ubicacionultimavez').value.trim();

        // Validar que la fecha no sea futura
        const today = new Date();
        const selectedDate = new Date(fechaDesaparicion);

        if (selectedDate > today) {
            e.preventDefault();
            alert('La fecha de desaparición no puede ser futura.');
            return false;
        }

        // Validar ubicación
        if (ubicacion.length < 5) {
            e.preventDefault();
            alert('Por favor, proporciona una ubicación más específica (mínimo 5 caracteres).');
            return false;
        }

        // Confirmación antes de enviar
        const confirmed = confirm('¿Estás seguro de que quieres actualizar la información del reporte?');
        if (!confirmed) {
            e.preventDefault();
            return false;
        }
    });

    // Establecer fecha máxima como hoy
    document.addEventListener('DOMContentLoaded', function() {
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('fecha_desaparicion').max = today;

        // Inicializar contador de caracteres
        const textarea = document.getElementById('descripcionsituacion');
        if (textarea) {
            updateCharCount(textarea, 500);
        }
    });

    // Efectos visuales en los inputs
    document.querySelectorAll('.form-control:not(.readonly-field)').forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'scale(1.02)';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.transform = 'scale(1)';
        });
    });

    // Validación en tiempo real para la recompensa
    document.getElementById('recompensa').addEventListener('input', function() {
        const value = parseFloat(this.value);
        if (value < 0) {
            this.value = 0;
        }
        if (value > 999999) {
            this.value = 999999;
        }
    });
</script>
</body>
</html>