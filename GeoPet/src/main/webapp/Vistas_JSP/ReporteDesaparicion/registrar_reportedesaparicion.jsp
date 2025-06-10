<%
    HttpSession debugSession = request.getSession(false);
    Integer debugUsuarioId = (Integer) (debugSession != null ? debugSession.getAttribute("usuarioId") : null);
    out.println("<!-- DEBUG: Usuario ID en sesión = " + debugUsuarioId + " -->");

    List<Mascotas> debugMascotas = (List<Mascotas>) request.getAttribute("mascotasUsuario");
    out.println("<!-- DEBUG: Mascotas recibidas = " + (debugMascotas != null ? debugMascotas.size() : "NULL") + " -->");
%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Mascotas" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportar Mascota Perdida - GeoPet</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
            min-height: 100vh;
            padding: 1rem;
        }

        .main-container {
            max-width: 900px;
            margin: 0 auto;
            background: white;
            border-radius: 25px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            animation: slideInUp 0.8s ease-out;
        }

        @keyframes slideInUp {
            from {
                opacity: 0;
                transform: translateY(50px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .main-header {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            padding: 3rem 2rem;
            text-align: center;
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
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="search" width="25" height="25" patternUnits="userSpaceOnUse"><g fill="rgba(255,255,255,0.1)"><circle cx="10" cy="10" r="6" fill="none" stroke="currentColor" stroke-width="1.5"/><path d="m15 15 4 4"/><circle cx="8" cy="8" r="1" fill="currentColor"/></g></pattern></defs><rect width="100" height="100" fill="url(%23search)"/></svg>');
            pointer-events: none;
        }

        .header-content {
            position: relative;
            z-index: 1;
        }

        .header-title {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 1rem;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
            animation: fadeInDown 0.8s ease-out;
        }

        .header-subtitle {
            font-size: 1.2rem;
            opacity: 0.9;
            margin-bottom: 0;
            animation: fadeInUp 0.8s ease-out 0.2s both;
        }

        @keyframes fadeInDown {
            from { opacity: 0; transform: translateY(-30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        @keyframes fadeInUp {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .form-container {
            padding: 3rem;
        }

        .alert-geopet {
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            border: none;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            position: relative;
        }

        .alert-danger-geopet {
            background: linear-gradient(135deg, #f8d7da 0%, #f5c2c7 100%);
            color: #721c24;
            border-left: 4px solid #dc3545;
        }

        .alert-warning-geopet {
            background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%);
            color: #856404;
            border-left: 4px solid #ffc107;
        }

        .alert-info-geopet {
            background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
            color: #0c5460;
            border-left: 4px solid #17a2b8;
        }

        .form-section {
            background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
            border-radius: 20px;
            padding: 2rem;
            margin-bottom: 2rem;
            border-top: 4px solid #dc3545;
            transition: all 0.3s ease;
            animation: slideInLeft 0.6s ease-out forwards;
            opacity: 0;
        }

        .form-section:hover {
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
            transform: translateY(-3px);
        }

        .form-section:nth-child(1) { animation-delay: 0.1s; }
        .form-section:nth-child(2) { animation-delay: 0.2s; }
        .form-section:nth-child(3) { animation-delay: 0.3s; }
        .form-section:nth-child(4) { animation-delay: 0.4s; }

        @keyframes slideInLeft {
            from { opacity: 0; transform: translateX(-30px); }
            to { opacity: 1; transform: translateX(0); }
        }

        .section-title {
            color: #dc3545;
            font-size: 1.25rem;
            font-weight: bold;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 0.75rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .form-label .required {
            color: #dc3545;
            font-weight: bold;
        }

        .form-control, .form-select {
            border: 2px solid #e9ecef;
            border-radius: 12px;
            padding: 0.875rem 1rem;
            font-size: 1rem;
            transition: all 0.3s ease;
            background-color: #fff;
        }

        .form-control:focus, .form-select:focus {
            border-color: #dc3545;
            box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25);
            transform: translateY(-2px);
        }

        .form-text {
            color: #6c757d;
            font-size: 0.875rem;
            margin-top: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.25rem;
        }

        .input-group-text {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            border: 2px solid #dc3545;
            font-weight: 600;
        }

        .btn-primary-geopet {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 1rem 2.5rem;
            border-radius: 12px;
            font-size: 1.1rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
            position: relative;
            overflow: hidden;
        }

        .btn-primary-geopet:hover {
            background: linear-gradient(135deg, #c82333 0%, #bd2130 100%);
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(220, 53, 69, 0.4);
            color: white;
        }

        .btn-primary-geopet:active {
            transform: translateY(-1px);
        }

        .btn-secondary-geopet {
            background: linear-gradient(135deg, #6c757d 0%, #495057 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 1rem 2.5rem;
            border-radius: 12px;
            font-size: 1.1rem;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-secondary-geopet:hover {
            background: linear-gradient(135deg, #495057 0%, #343a40 100%);
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(108, 117, 125, 0.4);
            color: white;
        }

        .btn-info-geopet {
            background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
            border: none;
            color: white;
            font-weight: 600;
            padding: 1rem 2.5rem;
            border-radius: 12px;
            font-size: 1.1rem;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-info-geopet:hover {
            background: linear-gradient(135deg, #138496 0%, #0c5460 100%);
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(23, 162, 184, 0.4);
            color: white;
        }

        .mascota-preview {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            margin-top: 1rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            display: none;
            animation: fadeInScale 0.5s ease-out;
            border: 2px solid #dc3545;
        }

        @keyframes fadeInScale {
            from { opacity: 0; transform: scale(0.9); }
            to { opacity: 1; transform: scale(1); }
        }

        .mascota-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .mascota-avatar {
            width: 70px;
            height: 70px;
            border-radius: 50%;
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 2rem;
            font-weight: bold;
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
        }

        .step-indicator {
            display: flex;
            justify-content: center;
            margin-bottom: 2rem;
        }

        .step {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            border-radius: 25px;
            background: #e9ecef;
            color: #6c757d;
            margin: 0 0.5rem;
            transition: all 0.3s ease;
            font-weight: 600;
        }

        .step.active {
            background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            color: white;
            transform: scale(1.1);
            box-shadow: 0 4px 15px rgba(220, 53, 69, 0.3);
        }

        .character-count {
            position: absolute;
            bottom: 0.5rem;
            right: 0.75rem;
            font-size: 0.75rem;
            color: #6c757d;
            background: white;
            padding: 0 0.25rem;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .floating-label {
            position: relative;
        }

        .no-mascotas-card {
            background: white;
            border-radius: 20px;
            padding: 3rem;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            border-top: 4px solid #ffc107;
            animation: bounceIn 0.8s ease-out;
        }

        @keyframes bounceIn {
            0% { opacity: 0; transform: scale(0.3); }
            50% { opacity: 1; transform: scale(1.05); }
            70% { transform: scale(0.9); }
            100% { transform: scale(1); }
        }

        .no-mascotas-icon {
            font-size: 5rem;
            color: #ffc107;
            margin-bottom: 1.5rem;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.1); }
            100% { transform: scale(1); }
        }

        .form-control.is-valid,
        .form-select.is-valid {
            border-color: #28a745;
            box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25);
        }

        .form-control.is-invalid,
        .form-select.is-invalid {
            border-color: #dc3545;
            box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25);
        }

        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            display: none;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .loading-spinner {
            width: 50px;
            height: 50px;
            border: 5px solid #f3f3f3;
            border-top: 5px solid #dc3545;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        @media (max-width: 768px) {
            .main-container {
                margin: 0.5rem;
                border-radius: 20px;
            }

            .form-container {
                padding: 2rem 1.5rem;
            }

            .header-title {
                font-size: 2rem;
            }

            .btn-primary-geopet,
            .btn-secondary-geopet,
            .btn-info-geopet {
                width: 100%;
                margin-bottom: 0.5rem;
            }

            .step-indicator {
                flex-direction: column;
                align-items: center;
            }

            .step {
                margin: 0.25rem 0;
                width: 200px;
                justify-content: center;
            }

            .mascota-info {
                flex-direction: column;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<div class="loading-overlay" id="loadingOverlay">
    <div class="loading-spinner"></div>
</div>

<div class="main-container">
    <div class="main-header">
        <div class="header-content">
            <h1 class="header-title">
                <i class="bi bi-search-heart me-3"></i>Reportar Mascota Perdida
            </h1>
            <p class="header-subtitle">Completa la información para ayudar a encontrar tu mascota</p>
        </div>
    </div>

    <div class="form-container">
        <!-- Mostrar mensajes de error -->
        <%
            String mensaje = (String) request.getAttribute("mensaje");
            if (mensaje != null) {
        %>
        <div class="alert-geopet alert-danger-geopet">
            <div class="d-flex align-items-center">
                <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
                <div>
                    <strong>Error:</strong> <%= mensaje %>
                </div>
            </div>
        </div>
        <% } %>

        <!-- Alerta informativa -->
        <div class="alert-geopet alert-info-geopet">
            <div class="d-flex align-items-start">
                <i class="bi bi-info-circle-fill me-3 fs-4"></i>
                <div>
                    <strong>Importante:</strong> Asegúrate de proporcionar información precisa y actualizada.
                    Esto ayudará a que más personas puedan identificar y contactarte sobre tu mascota.
                </div>
            </div>
        </div>

        <!-- Verificar que el usuario tenga mascotas -->
        <%
            List<Mascotas> mascotasUsuario = (List<Mascotas>) request.getAttribute("mascotasUsuario");
            if (mascotasUsuario == null || mascotasUsuario.isEmpty()) {
        %>
        <div class="no-mascotas-card">
            <div class="no-mascotas-icon">
                <i class="bi bi-plus-circle-dotted"></i>
            </div>
            <h3 class="text-warning mb-3">No tienes mascotas registradas</h3>
            <p class="mb-4 text-muted">
                Primero debes registrar tu mascota antes de poder reportar su desaparición.
                El registro es rápido y nos ayuda a tener toda la información necesaria.
            </p>
            <div class="d-flex gap-3 justify-content-center flex-wrap">
                <a href="MascotaServlet?accion=registrar" class="btn-info-geopet">
                    <i class="bi bi-plus-circle me-2"></i>Registrar Mascota
                </a>
                <a href="ReporteDesaparicionServlet?accion=listar" class="btn-secondary-geopet">
                    <i class="bi bi-arrow-left me-2"></i>Volver a Reportes
                </a>
            </div>
        </div>
        <% } else { %>

        <!-- Indicador de pasos -->
        <div class="step-indicator">
            <div class="step active" id="step1">
                <i class="bi bi-1-circle-fill"></i>
                Seleccionar Mascota
            </div>
            <div class="step" id="step2">
                <i class="bi bi-2-circle"></i>
                Información del Reporte
            </div>
            <div class="step" id="step3">
                <i class="bi bi-3-circle"></i>
                Confirmar y Enviar
            </div>
        </div>

        <!-- Formulario de reporte -->
        <form action="ReporteDesaparicionServlet" method="post" id="reporteForm">
            <input type="hidden" name="accion" value="registrar">

            <!-- Sección 1: Selección de mascota -->
            <div class="form-section">
                <h4 class="section-title">
                    <i class="bi bi-heart-fill"></i>Seleccionar Mascota
                </h4>

                <div class="mb-4">
                    <label for="mascota_id" class="form-label">
                        <i class="bi bi-tag"></i>Mascota Perdida <span class="required">*</span>
                    </label>
                    <select name="mascota_id" id="mascota_id" class="form-select" required>
                        <option value="">Selecciona tu mascota...</option>
                        <%
                            for (Mascotas mascota : mascotasUsuario) {
                        %>
                        <option value="<%= mascota.getMascotaID() %>"
                                data-nombre="<%= mascota.getNombre() %>"
                                data-edad="<%= mascota.getEdad() %>"
                                data-sexo="<%= mascota.getSexo() != null ? mascota.getSexo() : "No especificado" %>"
                                data-color="<%= mascota.getColor() != null ? mascota.getColor() : "No especificado" %>">
                            <%= mascota.getNombre() %> - <%= mascota.getEdad() %> meses
                        </option>
                        <% } %>
                    </select>
                    <div class="form-text">
                        <i class="bi bi-info-circle"></i>
                        Selecciona la mascota que se ha perdido
                    </div>
                </div>

                <!-- Preview de mascota seleccionada -->
                <div id="mascotaPreview" class="mascota-preview">
                    <div class="mascota-info">
                        <div class="mascota-avatar" id="mascotaAvatar">
                            <i class="bi bi-heart-fill"></i>
                        </div>
                        <div>
                            <h5 class="mb-1 text-primary" id="mascotaNombre">Nombre de la mascota</h5>
                            <small class="text-muted">
                                <span id="mascotaDetalles">Detalles de la mascota</span>
                            </small>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Sección 2: Información del reporte -->
            <div class="form-section">
                <h4 class="section-title">
                    <i class="bi bi-calendar-event"></i>Información del Reporte
                </h4>

                <div class="row g-4">
                    <div class="col-md-6">
                        <label for="fecha_desaparicion" class="form-label">
                            <i class="bi bi-calendar-x"></i>Fecha de Desaparición <span class="required">*</span>
                        </label>
                        <input type="date" name="fecha_desaparicion" id="fecha_desaparicion"
                               class="form-control" required>
                        <div class="form-text">
                            <i class="bi bi-clock"></i>
                            ¿Cuándo fue la última vez que viste a tu mascota?
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label for="estado_reporte" class="form-label">
                            <i class="bi bi-activity"></i>Estado del Reporte <span class="required">*</span>
                        </label>
                        <select name="estado_reporte" id="estado_reporte" class="form-select" required>
                            <option value="">Selecciona el estado...</option>
                            <option value="Activo" selected>Activo - Búsqueda en curso</option>
                            <option value="Cerrado">Cerrado - No buscar más</option>
                        </select>
                        <div class="form-text">
                            <i class="bi bi-info-circle"></i>
                            "Activo" significa que la búsqueda está en curso
                        </div>
                    </div>
                </div>

                <div class="mt-4">
                    <label for="ubicacion_ultima_vez" class="form-label">
                        <i class="bi bi-geo-alt"></i>Ubicación Donde se Perdió <span class="required">*</span>
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-map"></i>
                        </span>
                        <input type="text" name="ubicacion_ultima_vez" id="ubicacion_ultima_vez"
                               class="form-control" required maxlength="64"
                               placeholder="Ej: Parque Central, Colonia Centro, cerca del mercado...">
                    </div>
                    <div class="form-text">
                        <i class="bi bi-exclamation-triangle"></i>
                        Sé lo más específico posible sobre el lugar
                    </div>
                </div>
            </div>

            <!-- Sección 3: Detalles adicionales -->
            <div class="form-section">
                <h4 class="section-title">
                    <i class="bi bi-chat-text"></i>Detalles Adicionales
                </h4>

                <div class="mb-4">
                    <label for="descripcion_situacion" class="form-label">
                        <i class="bi bi-file-text"></i>Descripción de la Situación
                    </label>
                    <div class="floating-label">
                        <textarea name="descripcion_situacion" id="descripcion_situacion"
                                  class="form-control" maxlength="64" rows="4"
                                  placeholder="Describe las circunstancias de la desaparición: ¿se escapó del patio? ¿se perdió durante un paseo? ¿otros detalles importantes?"
                                  oninput="updateCharCount(this, 64)"></textarea>
                        <div class="character-count" id="descripcion-count">0/64</div>
                    </div>
                    <div class="form-text">
                        <i class="bi bi-lightbulb"></i>
                        Proporciona detalles que puedan ayudar en la búsqueda
                    </div>
                </div>

                <div class="mb-4">
                    <label for="recompensa" class="form-label">
                        <i class="bi bi-currency-dollar"></i>Recompensa (Opcional)
                    </label>
                    <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input type="number" name="recompensa" id="recompensa"
                               class="form-control" min="0" step="0.01"
                               placeholder="0.00">
                    </div>
                    <div class="form-text">
                        <i class="bi bi-gift"></i>
                        Si ofreces una recompensa por encontrar tu mascota
                    </div>
                </div>
            </div>

            <!-- Sección 4: Confirmación -->
            <div class="form-section">
                <h4 class="section-title">
                    <i class="bi bi-check-circle"></i>Confirmar y Enviar
                </h4>

                <div class="alert-geopet alert-warning-geopet">
                    <div class="d-flex align-items-start">
                        <i class="bi bi-exclamation-triangle-fill me-3 fs-4"></i>
                        <div>
                            <strong>Antes de enviar, verifica:</strong>
                            <ul class="mt-2 mb-0">
                                <li>La información de tu mascota es correcta</li>
                                <li>La fecha y ubicación son precisas</li>
                                <li>Has proporcionado todos los detalles importantes</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div class="d-flex gap-3 justify-content-center flex-wrap mt-4">
                    <button type="submit" class="btn-primary-geopet" id="submitBtn">
                        <i class="bi bi-send me-2"></i>Registrar Reporte
                    </button>
                    <a href="ReporteDesaparicionServlet?accion=listar" class="btn-secondary-geopet">
                        <i class="bi bi-x-circle me-2"></i>Cancelar
                    </a>
                </div>
            </div>
        </form>

        <% } %>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // Variables globales
    let currentStep = 1;
    const totalSteps = 3;

    // Inicialización
    document.addEventListener('DOMContentLoaded', function() {
        const fechaInput = document.getElementById('fecha_desaparicion');
        const today = new Date().toISOString().split('T')[0];
        fechaInput.setAttribute('max', today);

        initializeMascotaPreview();
        initializeValidation();
        updateCharCount(document.getElementById('descripcion_situacion'), 64);
    });

    // Función para actualizar el contador de caracteres
    function updateCharCount(textarea, maxLength) {
        const currentLength = textarea.value.length;
        const countElement = document.getElementById('descripcion-count');

        if (countElement) {
            countElement.textContent = `${currentLength}/${maxLength}`;

            if (currentLength > maxLength * 0.9) {
                countElement.style.color = '#dc3545';
            } else if (currentLength > maxLength * 0.8) {
                countElement.style.color = '#ffc107';
            } else {
                countElement.style.color = '#6c757d';
            }
        }
    }

    // Inicializar preview de mascota
    function initializeMascotaPreview() {
        const mascotaSelect = document.getElementById('mascota_id');
        const preview = document.getElementById('mascotaPreview');

        mascotaSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];

            if (this.value) {
                const nombre = selectedOption.dataset.nombre;
                const edad = selectedOption.dataset.edad;
                const sexo = selectedOption.dataset.sexo;
                const color = selectedOption.dataset.color;

                document.getElementById('mascotaNombre').textContent = nombre;
                document.getElementById('mascotaDetalles').textContent =
                    `${edad} meses • ${sexo} • ${color}`;

                const avatar = document.getElementById('mascotaAvatar');
                avatar.textContent = nombre.charAt(0).toUpperCase();

                preview.style.display = 'block';
                updateStepIndicator(2);
            } else {
                preview.style.display = 'none';
                updateStepIndicator(1);
            }
        });
    }

    // Actualizar indicador de pasos
    function updateStepIndicator(step) {
        const steps = document.querySelectorAll('.step');

        steps.forEach((stepElement, index) => {
            const stepNumber = index + 1;
            const icon = stepElement.querySelector('i');

            if (stepNumber <= step) {
                stepElement.classList.add('active');
                icon.className = `bi bi-${stepNumber}-circle-fill`;
            } else {
                stepElement.classList.remove('active');
                icon.className = `bi bi-${stepNumber}-circle`;
            }
        });

        currentStep = step;
    }

    // Inicializar validación en tiempo real
    function initializeValidation() {
        const form = document.getElementById('reporteForm');
        const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');

        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });

            input.addEventListener('input', function() {
                if (this.classList.contains('is-invalid')) {
                    validateField(this);
                }
            });
        });
    }

    // Validar campo individual
    function validateField(field) {
        const value = field.value.trim();
        let isValid = true;
        let errorMessage = '';

        if (field.hasAttribute('required') && !value) {
            isValid = false;
            errorMessage = 'Este campo es obligatorio';
        } else if (field.type === 'date' && value) {
            const selectedDate = new Date(value);
            const today = new Date();

            if (selectedDate > today) {
                isValid = false;
                errorMessage = 'La fecha no puede ser futura';
            }
        } else if (field.name === 'ubicacion_ultima_vez' && value && value.length < 5) {
            isValid = false;
            errorMessage = 'Proporciona una ubicación más específica';
        } else if (field.name === 'recompensa' && value && parseFloat(value) < 0) {
            isValid = false;
            errorMessage = 'La recompensa no puede ser negativa';
        }

        if (isValid) {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
            removeErrorMessage(field);
        } else {
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');
            showErrorMessage(field, errorMessage);
        }

        return isValid;
    }

    // Mostrar mensaje de error
    function showErrorMessage(field, message) {
        removeErrorMessage(field);

        const errorDiv = document.createElement('div');
        errorDiv.className = 'invalid-feedback d-block';
        errorDiv.innerHTML = `<i class="bi bi-exclamation-circle me-1"></i>${message}`;

        field.parentNode.appendChild(errorDiv);
    }

    // Remover mensaje de error
    function removeErrorMessage(field) {
        const errorDiv = field.parentNode.querySelector('.invalid-feedback');
        if (errorDiv) {
            errorDiv.remove();
        }
    }

    // Validación del formulario completo
    document.getElementById('reporteForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const mascotaSelect = document.getElementById('mascota_id');
        const fechaInput = document.getElementById('fecha_desaparicion');
        const ubicacionInput = document.getElementById('ubicacion_ultima_vez');
        const estadoSelect = document.getElementById('estado_reporte');

        let isFormValid = true;
        const fields = [mascotaSelect, fechaInput, ubicacionInput, estadoSelect];

        fields.forEach(field => {
            if (!validateField(field)) {
                isFormValid = false;
            }
        });

        if (!isFormValid) {
            showToast('Error de validación', 'Por favor corrige los errores antes de continuar', 'error');

            const firstError = document.querySelector('.is-invalid');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                firstError.focus();
            }
            return;
        }

        updateStepIndicator(3);

        const mascotaNombre = mascotaSelect.options[mascotaSelect.selectedIndex].dataset.nombre;
        const fechaDesaparicion = fechaInput.value;
        const ubicacion = ubicacionInput.value;

        const confirmMessage = `¿Estás seguro de que quieres registrar este reporte?\n\n` +
            `Mascota: ${mascotaNombre}\n` +
            `Fecha: ${fechaDesaparicion}\n` +
            `Ubicación: ${ubicacion}\n\n` +
            `Una vez registrado, el reporte será visible públicamente.`;

        if (confirm(confirmMessage)) {
            showLoadingState();

            setTimeout(() => {
                this.submit();
            }, 1000);
        } else {
            updateStepIndicator(2);
        }
    });

    // Mostrar estado de carga
    function showLoadingState() {
        const overlay = document.getElementById('loadingOverlay');
        const submitBtn = document.getElementById('submitBtn');

        overlay.style.display = 'flex';
        submitBtn.innerHTML = '<i class="bi bi-hourglass-split me-2"></i>Registrando...';
        submitBtn.disabled = true;
    }

    // Función auxiliar para mostrar toasts
    function showToast(title, message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = 'position-fixed top-0 end-0 p-3';
        toast.style.zIndex = '1060';

        const bgClass = type === 'success' ? 'bg-success' :
            type === 'error' ? 'bg-danger' :
                type === 'warning' ? 'bg-warning' : 'bg-primary';

        const iconClass = type === 'success' ? 'check-circle' :
            type === 'error' ? 'exclamation-triangle' :
                type === 'warning' ? 'exclamation-triangle' : 'info-circle';

        toast.innerHTML = `
            <div class="toast show" role="alert">
                <div class="toast-header ${bgClass} text-white">
                    <i class="bi bi-${iconClass}-fill me-2"></i>
                    <strong class="me-auto">${title}</strong>
                    <button type="button" class="btn-close btn-close-white" onclick="this.closest('.toast').parentElement.remove()"></button>
                </div>
                <div class="toast-body">${message}</div>
            </div>
        `;

        document.body.appendChild(toast);

        setTimeout(() => {
            if (toast.parentElement) {
                toast.remove();
            }
        }, 5000);
    }

    // Validación de recompensa en tiempo real
    document.getElementById('recompensa').addEventListener('input', function() {
        const value = parseFloat(this.value);

        if (value < 0) {
            this.value = 0;
        }

        if (value > 999999) {
            this.value = 999999;
            showToast('Límite de recompensa', 'La recompensa no puede exceder $999,999', 'warning');
        }
    });

    // Efectos visuales en campos
    document.querySelectorAll('.form-control, .form-select').forEach(field => {
        field.addEventListener('focus', function() {
            this.parentElement.style.transform = 'scale(1.02)';
        });

        field.addEventListener('blur', function() {
            this.parentElement.style.transform = 'scale(1)';
        });
    });

    // Atajos de teclado
    document.addEventListener('keydown', function(e) {
        if (e.ctrlKey || e.metaKey) {
            switch(e.key) {
                case 's':
                    e.preventDefault();
                    document.getElementById('reporteForm').requestSubmit();
                    break;
                case 'Escape':
                    if (confirm('¿Seguro que quieres cancelar? Se perderán los datos no guardados.')) {
                        window.location.href = 'ReporteDesaparicionServlet?accion=listar';
                    }
                    break;
            }
        }
    });

    // Autoguardado y restauración de datos
    function enableAutoSave() {
        const form = document.getElementById('reporteForm');

        setInterval(() => {
            const currentData = {};

            form.querySelectorAll('input, select, textarea').forEach(field => {
                if (field.name && field.value) {
                    currentData[field.name] = field.value;
                }
            });

            sessionStorage.setItem('reporteFormData', JSON.stringify(currentData));
        }, 30000);
    }

    function restoreFormData() {
        const savedData = sessionStorage.getItem('reporteFormData');

        if (savedData) {
            try {
                const data = JSON.parse(savedData);

                Object.keys(data).forEach(fieldName => {
                    const field = document.querySelector(`[name="${fieldName}"]`);
                    if (field && !field.value) {
                        field.value = data[fieldName];

                        if (field.tagName === 'SELECT') {
                            field.dispatchEvent(new Event('change'));
                        }
                    }
                });

                showToast('Datos restaurados', 'Se han restaurado los datos de una sesión anterior', 'info');
            } catch (e) {
                console.error('Error al restaurar datos:', e);
            }
        }
    }

    // Limpiar datos guardados al enviar exitosamente
    window.addEventListener('beforeunload', function() {
        if (document.getElementById('submitBtn').disabled) {
            sessionStorage.removeItem('reporteFormData');
        }
    });

    // Funcionalidades adicionales al cargar
    window.addEventListener('load', function() {
        enableAutoSave();

        // Mostrar toast de bienvenida
        setTimeout(() => {
            showToast('Formulario de Reporte', 'Completa todos los campos para ayudar a encontrar tu mascota', 'info');
        }, 1500);
    });
</script>
</body>
</html>