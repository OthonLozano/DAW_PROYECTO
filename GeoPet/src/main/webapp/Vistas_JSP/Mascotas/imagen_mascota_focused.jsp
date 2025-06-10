<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String mascotaIdParam = request.getParameter("mascotaId");
  String nombreMascota = request.getParameter("nombre");
  int mascotaId = 0;

  // DEBUG: Imprimir parámetros recibidos
  System.out.println("=== DEBUG JSP AGREGAR FOTOS ===");
  System.out.println("DEBUG: mascotaIdParam recibido = " + mascotaIdParam);
  System.out.println("DEBUG: nombreMascota recibido = " + nombreMascota);
  System.out.println("DEBUG: Todos los parámetros de la request:");
  java.util.Enumeration<String> parameterNames = request.getParameterNames();
  while(parameterNames.hasMoreElements()) {
    String paramName = parameterNames.nextElement();
    String paramValue = request.getParameter(paramName);
    System.out.println("  " + paramName + " = " + paramValue);
  }

  if (mascotaIdParam != null && !mascotaIdParam.isEmpty()) {
    try {
      mascotaId = Integer.parseInt(mascotaIdParam);
      System.out.println("DEBUG: mascotaId parseado = " + mascotaId);
    } catch (NumberFormatException e) {
      System.out.println("ERROR: No se pudo parsear mascotaId: " + e.getMessage());
      mascotaId = 0;
    }
  } else {
    System.out.println("ERROR: mascotaIdParam es null o vacío");
  }

  if (mascotaId <= 0) {
    System.out.println("ERROR: mascotaId inválido, redirigiendo...");
    response.sendRedirect("MascotaServlet?accion=listar");
    return;
  }

  System.out.println("DEBUG: JSP continuará con mascotaId = " + mascotaId);
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GeoPet - Agregar Fotos</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
  <!-- Font Awesome -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

  <style>
    .login-container {
      min-height: calc(100vh - 140px);
      display: flex;
      align-items: center;
      padding: 2rem 0;
    }

    .login-card {
      background: white;
      border-radius: 0.5rem;
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      max-width: 800px;
      width: 100%;
      margin: 0 auto;
      border-top: 5px solid #2e7d32;
    }

    .login-header {
      background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
      color: white;
      text-align: center;
      padding: 2rem;
    }

    .login-header h2 {
      font-weight: 700;
    }

    .login-body {
      padding: 2rem;
    }

    .form-control {
      border-radius: 0.375rem;
      padding: 0.75rem 1rem;
      border: 1px solid #e0e0e0;
      margin-bottom: 1.25rem;
    }

    .form-control:focus {
      border-color: #2e7d32;
      box-shadow: 0 0 0 0.2rem rgba(46, 125, 50, 0.25);
    }

    .btn-login {
      background: linear-gradient(135deg, #2e7d32 0%, #4caf50 100%);
      border: none;
      border-radius: 0.375rem;
      padding: 0.75rem 1.5rem;
      font-weight: 600;
      color: white;
      transition: all 0.3s ease;
    }

    .btn-login:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
      background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
    }

    .btn-secondary {
      background: #6c757d;
      border: none;
      border-radius: 0.375rem;
      padding: 0.75rem 1.5rem;
      font-weight: 600;
      color: white;
      transition: all 0.3s ease;
    }

    .btn-secondary:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
      background: #5a6268;
    }

    .debug-info {
      background-color: #f8f9fa;
      border: 1px solid #dee2e6;
      border-radius: 0.375rem;
      padding: 1rem;
      margin-bottom: 1.5rem;
      font-size: 0.875rem;
      color: #6c757d;
    }

    .upload-area {
      border: 2px dashed #dee2e6;
      border-radius: 0.5rem;
      padding: 2rem;
      text-align: center;
      background-color: #f8f9fa;
      transition: all 0.3s ease;
      margin-bottom: 1.5rem;
    }

    .upload-area:hover {
      border-color: #2e7d32;
      background-color: #f1f8e9;
    }

    .upload-area.dragover {
      border-color: #2e7d32;
      background-color: #e8f5e8;
      transform: scale(1.02);
    }

    .upload-icon {
      font-size: 3rem;
      color: #6c757d;
      margin-bottom: 1rem;
    }

    .alert-custom {
      border-radius: 0.375rem;
      padding: 1rem;
      margin-bottom: 1.5rem;
      border: none;
    }

    .alert-success-custom {
      background-color: #d4edda;
      color: #155724;
    }

    .alert-error-custom {
      background-color: #f8d7da;
      color: #721c24;
    }

    .loading-spinner {
      display: none;
      text-align: center;
      margin: 1rem 0;
    }

    .pet-info {
      background: linear-gradient(135deg, #e8f5e8 0%, #f1f8e9 100%);
      border-radius: 0.5rem;
      padding: 1.5rem;
      margin-bottom: 1.5rem;
      border-left: 4px solid #2e7d32;
    }
  </style>
</head>
<body class="bg-light">

<main class="login-container">
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <div class="login-card">
          <div class="login-header">
            <h2 class="mb-3">
              <i class="bi bi-camera-fill me-2"></i>Agregar Fotos de Mascota
            </h2>
            <p class="mb-0">Sube imágenes para crear un álbum de tu mascota</p>
          </div>
          <div class="login-body">
            <!-- Información de la mascota -->
            <div class="pet-info">
              <div class="d-flex align-items-center mb-2">
                <i class="bi bi-heart-fill text-success me-2 fs-4"></i>
                <h4 class="mb-0">
                  <%= nombreMascota != null ? nombreMascota : "Mascota" %>
                </h4>
              </div>
              <p class="text-muted mb-0">
                <i class="bi bi-tag me-1"></i>ID de Mascota: <strong><%= mascotaId %></strong>
              </p>
            </div>

            <!-- DEBUG INFO (solo en desarrollo) -->
            <div class="debug-info">
              <div class="d-flex align-items-center mb-2">
                <i class="bi bi-bug me-2"></i>
                <strong>DEBUG INFO:</strong>
              </div>
              <small>
                Parámetro recibido: <code><%= mascotaIdParam %></code><br>
                Mascota ID parseado: <code><%= mascotaId %></code><br>
                Nombre mascota: <code><%= nombreMascota %></code>
              </small>
            </div>

            <!-- Área de mensajes -->
            <div id="mensajes"></div>

            <!-- Formulario de subida -->
            <form id="formSubirImagen" enctype="multipart/form-data">
              <div class="upload-area" id="uploadArea">
                <div class="upload-icon">
                  <i class="bi bi-cloud-upload"></i>
                </div>
                <h5 class="mb-3">Selecciona una imagen</h5>
                <p class="text-muted mb-3">Arrastra y suelta tu imagen aquí o haz clic para seleccionar</p>

                <div class="form-floating mb-3">
                  <input class="form-control" id="imagen" name="imagen" type="file" accept="image/*" required>
                  <label for="imagen"><i class="bi bi-image me-2"></i>Seleccionar Archivo</label>
                </div>

                <small class="text-muted">
                  <i class="bi bi-info-circle me-1"></i>
                  Formatos soportados: JPG, PNG, GIF (máx. 5MB)
                </small>
              </div>

              <!-- Loading spinner -->
              <div class="loading-spinner" id="loadingSpinner">
                <div class="spinner-border text-success" role="status">
                  <span class="visually-hidden">Subiendo...</span>
                </div>
                <p class="mt-2 text-muted">Subiendo imagen...</p>
              </div>

              <div class="d-flex justify-content-between align-items-center">
                <a href="MascotaServlet?accion=listar" class="btn btn-secondary">
                  <i class="bi bi-arrow-left"></i> Volver a la Lista
                </a>
                <button type="submit" class="btn btn-login" id="btnSubir">
                  <i class="bi bi-cloud-upload"></i> Subir Imagen
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
  (function() {
    'use strict';

    // DEBUG: Verificar múltiples formas de obtener el ID
    const mascotaIdFromJSP = <%= mascotaId %>;
    const mascotaIdFromParam = '<%= mascotaIdParam %>';
    const mascotaIdParsed = parseInt('<%= mascotaIdParam %>');

    console.log('=== DEBUG JAVASCRIPT ===');
    console.log('DEBUG: mascotaIdFromJSP:', mascotaIdFromJSP);
    console.log('DEBUG: mascotaIdFromParam:', mascotaIdFromParam);
    console.log('DEBUG: mascotaIdParsed:', mascotaIdParsed);
    console.log('DEBUG: URL actual:', window.location.href);
    console.log('DEBUG: Query string:', window.location.search);

    // Usar el valor más confiable
    const mascotaIdFinal = mascotaIdFromJSP;
    console.log('DEBUG: mascotaIdFinal que se enviará:', mascotaIdFinal);

    // Referencias a elementos del DOM
    const uploadArea = document.getElementById('uploadArea');
    const imagenInput = document.getElementById('imagen');
    const loadingSpinner = document.getElementById('loadingSpinner');
    const btnSubir = document.getElementById('btnSubir');

    function mostrarMensaje(mensaje, tipo) {
      const alertClass = tipo === 'exito' ? 'alert-success-custom' : 'alert-error-custom';
      const icon = tipo === 'exito' ? 'bi-check-circle-fill' : 'bi-exclamation-triangle-fill';

      document.getElementById('mensajes').innerHTML = `
        <div class="alert-custom ${alertClass}">
          <i class="${icon} me-2"></i>${mensaje}
        </div>
      `;
    }

    function toggleLoading(show) {
      if (show) {
        loadingSpinner.style.display = 'block';
        btnSubir.disabled = true;
        btnSubir.innerHTML = '<i class="spinner-border spinner-border-sm me-2"></i>Subiendo...';
      } else {
        loadingSpinner.style.display = 'none';
        btnSubir.disabled = false;
        btnSubir.innerHTML = '<i class="bi bi-cloud-upload"></i> Subir Imagen';
      }
    }

    function redirigirAHome() {
      // Esperar 2 segundos antes de redirigir para que el usuario vea el mensaje
      setTimeout(() => {
        console.log('DEBUG: Redirigiendo a HomeCliente...');
        // Puedes ajustar la URL según tu estructura de proyecto
        window.location.href = '/GeoPet_war_exploded/MascotaServlet?accion=listar';
        // O si usas un servlet para HomeCliente:
        // window.location.href = '/GeoPet_war_exploded/HomeClienteServlet';
      }, 2000);
    }

    // Drag and drop functionality
    uploadArea.addEventListener('dragover', function(e) {
      e.preventDefault();
      uploadArea.classList.add('dragover');
    });

    uploadArea.addEventListener('dragleave', function(e) {
      e.preventDefault();
      uploadArea.classList.remove('dragover');
    });

    uploadArea.addEventListener('drop', function(e) {
      e.preventDefault();
      uploadArea.classList.remove('dragover');

      const files = e.dataTransfer.files;
      if (files.length > 0) {
        imagenInput.files = files;
        // Trigger change event para validar archivo
        imagenInput.dispatchEvent(new Event('change'));
      }
    });

    // Validación de archivo
    imagenInput.addEventListener('change', function() {
      const file = this.files[0];
      if (file) {
        // Validar tamaño (5MB max)
        if (file.size > 5 * 1024 * 1024) {
          mostrarMensaje('El archivo es demasiado grande. Máximo 5MB.', 'error');
          this.value = '';
          return;
        }

        // Validar tipo
        if (!file.type.startsWith('image/')) {
          mostrarMensaje('Por favor selecciona un archivo de imagen válido.', 'error');
          this.value = '';
          return;
        }

        // Mostrar preview del archivo seleccionado
        const fileName = file.name;
        const fileSize = (file.size / 1024 / 1024).toFixed(2);
        mostrarMensaje(`Archivo seleccionado: ${fileName} (${fileSize} MB)`, 'exito');
      }
    });

    document.getElementById('formSubirImagen').addEventListener('submit', async function(e) {
      e.preventDefault();
      console.log('=== DEBUG ENVÍO ===');
      console.log('DEBUG: Formulario enviado');

      const imagenFile = document.getElementById('imagen').files[0];
      console.log('DEBUG: Archivo seleccionado:', imagenFile);

      if (!imagenFile) {
        console.log('DEBUG: No hay archivo');
        mostrarMensaje('Por favor selecciona una imagen', 'error');
        return;
      }

      toggleLoading(true);
      document.getElementById('mensajes').innerHTML = '';

      const formData = new FormData();
      formData.append('action', 'subir');
      formData.append('mascotaId', mascotaIdFinal);
      formData.append('imagen', imagenFile);

      console.log('DEBUG: FormData creado con mascotaId:', mascotaIdFinal);

      // DEBUG: Verificar contenido del FormData
      console.log('DEBUG: Contenido del FormData:');
      for (let pair of formData.entries()) {
        console.log('  ' + pair[0] + ': ' + pair[1]);
      }

      try {
        console.log('DEBUG: Iniciando fetch...');
        const response = await fetch('/GeoPet_war_exploded/ImagenMascotaServlet', {
          method: 'POST',
          body: formData
        });

        console.log('DEBUG: Response recibido, status:', response.status);

        if (response.ok) {
          console.log('DEBUG: Response OK, parseando JSON...');
          const resultado = await response.json();
          console.log('DEBUG: JSON parseado:', resultado);

          if (resultado.exito) {
            mostrarMensaje(`
              <strong>¡Éxito!</strong> Imagen subida correctamente para ${nombreMascota || 'la mascota'} (ID: ${mascotaIdFinal}).
              <br><small>Redirigiendo al inicio en unos segundos...</small>
            `, 'exito');

            // Limpiar el formulario
            document.getElementById('imagen').value = '';

            // NUEVA FUNCIONALIDAD: Redirigir después del éxito
            redirigirAHome();

          } else {
            mostrarMensaje('<strong>Error:</strong> ' + resultado.mensaje, 'error');
          }
        } else {
          console.log('DEBUG: Response NO OK, status:', response.status);
          const responseText = await response.text();
          console.log('DEBUG: Response text:', responseText);
          mostrarMensaje(`<strong>Error del servidor:</strong> Status ${response.status}`, 'error');
        }
      } catch (error) {
        console.log('DEBUG: Error en fetch:', error);
        mostrarMensaje('<strong>Error:</strong> ' + error.message, 'error');
      } finally {
        toggleLoading(false);
      }
    });
  })();
</script>
</body>
</html>