<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, Modelo.JavaBeans.ImagenMascota, Modelo.DAO.ImagenMascotaDAO" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Gestión de Imágenes de Mascotas</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #f5f5f5;
      color: #333;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }

    .header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 2rem;
      border-radius: 10px;
      margin-bottom: 2rem;
      text-align: center;
    }

    .card {
      background: white;
      border-radius: 10px;
      padding: 1.5rem;
      margin-bottom: 2rem;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .form-group {
      margin-bottom: 1rem;
    }

    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: bold;
      color: #555;
    }

    input[type="text"], input[type="number"], input[type="file"], select {
      width: 100%;
      padding: 0.75rem;
      border: 2px solid #ddd;
      border-radius: 5px;
      font-size: 1rem;
      transition: border-color 0.3s;
    }

    input[type="text"]:focus, input[type="number"]:focus, input[type="file"]:focus, select:focus {
      outline: none;
      border-color: #667eea;
    }

    .btn {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 1rem;
      transition: transform 0.2s;
      margin-right: 0.5rem;
      margin-bottom: 0.5rem;
    }

    .btn:hover {
      transform: translateY(-2px);
    }

    .btn-danger {
      background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
    }

    .btn-success {
      background: linear-gradient(135deg, #51cf66 0%, #40c057 100%);
    }

    .btn-info {
      background: linear-gradient(135deg, #339af0 0%, #228be6 100%);
    }

    .gallery {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1.5rem;
      margin-top: 2rem;
    }

    .imagen-card {
      background: white;
      border-radius: 10px;
      overflow: hidden;
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
      transition: transform 0.3s;
    }

    .imagen-card:hover {
      transform: translateY(-5px);
    }

    .imagen-preview {
      width: 100%;
      height: 200px;
      object-fit: cover;
      background: #f8f9fa;
    }

    .imagen-info {
      padding: 1rem;
    }

    .mensaje {
      padding: 1rem;
      border-radius: 5px;
      margin-bottom: 1rem;
      font-weight: bold;
    }

    .mensaje.exito {
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }

    .mensaje.error {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .progress-bar {
      width: 100%;
      height: 20px;
      background-color: #e9ecef;
      border-radius: 10px;
      overflow: hidden;
      margin: 1rem 0;
    }

    .progress-fill {
      height: 100%;
      background: linear-gradient(90deg, #667eea, #764ba2);
      width: 0%;
      transition: width 0.3s;
    }

    .tabs {
      display: flex;
      border-bottom: 2px solid #e9ecef;
      margin-bottom: 2rem;
    }

    .tab {
      padding: 1rem 2rem;
      cursor: pointer;
      border: none;
      background: none;
      font-size: 1rem;
      color: #666;
      transition: all 0.3s;
    }

    .tab.active {
      color: #667eea;
      border-bottom: 2px solid #667eea;
    }

    .tab-content {
      display: none;
    }

    .tab-content.active {
      display: block;
    }

    .modal {
      display: none;
      position: fixed;
      z-index: 1000;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0,0,0,0.8);
    }

    .modal-content {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      max-width: 90%;
      max-height: 90%;
    }

    .modal img {
      max-width: 100%;
      max-height: 100%;
      border-radius: 10px;
    }

    .close {
      position: absolute;
      top: 15px;
      right: 35px;
      color: #f1f1f1;
      font-size: 40px;
      font-weight: bold;
      cursor: pointer;
    }

    @media (max-width: 768px) {
      .container {
        padding: 10px;
      }

      .gallery {
        grid-template-columns: 1fr;
      }

      .tabs {
        flex-direction: column;
      }
    }
  </style>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>🐾 Gestión de Imágenes de Mascotas</h1>
    <p>Administra las fotos de tus mascotas de forma fácil y rápida</p>
  </div>

  <!-- Mensajes -->
  <div id="mensajes"></div>

  <!-- Tabs -->
  <div class="tabs">
    <button class="tab active" onclick="cambiarTab('subir')">📤 Subir Imágenes</button>
    <button class="tab" onclick="cambiarTab('galeria')">🖼️ Galería</button>
    <button class="tab" onclick="cambiarTab('buscar')">🔍 Buscar</button>
    <button class="tab" onclick="cambiarTab('administrar')">⚙️ Administrar</button>
  </div>

  <!-- Tab: Subir Imágenes -->
  <div id="tab-subir" class="tab-content active">
    <div class="card">
      <h2>📤 Subir Nueva Imagen</h2>
      <form id="formSubirImagen" enctype="multipart/form-data">
        <div class="form-group">
          <label for="mascotaId">ID de la Mascota:</label>
          <input type="number" id="mascotaId" name="mascotaId" required>
        </div>
        <div class="form-group">
          <label for="imagen">Seleccionar Imagen:</label>
          <input type="file" id="imagen" name="imagen" accept="image/*" required>
          <small>Formatos permitidos: JPG, PNG, GIF, WebP (máximo 10MB)</small>
        </div>
        <div class="progress-bar" id="progressBar" style="display: none;">
          <div class="progress-fill" id="progressFill"></div>
        </div>
        <button type="submit" class="btn">🚀 Subir Imagen</button>
      </form>
    </div>
  </div>

  <!-- Tab: Galería -->
  <div id="tab-galeria" class="tab-content">
    <div class="card">
      <h2>🖼️ Galería de Imágenes</h2>
      <div class="form-group">
        <label for="filtroMascota">Filtrar por Mascota (opcional):</label>
        <input type="number" id="filtroMascota" placeholder="Ingresa ID de mascota">
        <button onclick="cargarImagenes()" class="btn btn-info">🔄 Cargar Imágenes</button>
      </div>
    </div>
    <div id="galeria" class="gallery"></div>
  </div>

  <!-- Tab: Buscar -->
  <div id="tab-buscar" class="tab-content">
    <div class="card">
      <h2>🔍 Buscar Imagen</h2>
      <div class="form-group">
        <label for="imagenIdBuscar">ID de la Imagen:</label>
        <input type="number" id="imagenIdBuscar" placeholder="Ingresa ID de imagen">
        <button onclick="buscarImagen()" class="btn btn-info">🔍 Buscar</button>
      </div>
      <div id="resultadoBusqueda"></div>
    </div>
  </div>

  <!-- Tab: Administrar -->
  <div id="tab-administrar" class="tab-content">
    <div class="card">
      <h2>⚙️ Administración</h2>

      <h3>🔢 Estadísticas</h3>
      <div class="form-group">
        <label for="mascotaStats">ID de Mascota para estadísticas:</label>
        <input type="number" id="mascotaStats" placeholder="ID de mascota">
        <button onclick="obtenerEstadisticas()" class="btn btn-info">📊 Ver Estadísticas</button>
      </div>
      <div id="estadisticas"></div>

      <hr style="margin: 2rem 0;">

      <h3>🗑️ Imágenes Eliminadas</h3>
      <button onclick="cargarImagenesEliminadas()" class="btn btn-info">👀 Ver Eliminadas</button>
      <div id="imagenesEliminadas"></div>
    </div>
  </div>
</div>

<!-- Modal para ver imágenes en grande -->
<div id="modalImagen" class="modal">
  <span class="close">&times;</span>
  <div class="modal-content">
    <img id="imagenModal" src="" alt="Imagen ampliada">
  </div>
</div>

<script>
  // Variables globales
  let imagenesCargadas = [];

  // Cambiar tabs
  function cambiarTab(tabName) {
    // Ocultar todos los contenidos
    const contents = document.querySelectorAll('.tab-content');
    contents.forEach(content => content.classList.remove('active'));

    // Desactivar todos los tabs
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => tab.classList.remove('active'));

    // Activar el tab seleccionado
    document.getElementById('tab-' + tabName).classList.add('active');
    event.target.classList.add('active');

    // Cargar contenido específico si es necesario
    if (tabName === 'galeria') {
      cargarImagenes();
    }
  }

  // Manejar formulario de subida
  document.getElementById('formSubirImagen').addEventListener('submit', function(e) {
    e.preventDefault();
    subirImagen();
  });

  // Función para subir imagen
  async function subirImagen() {
    const formData = new FormData();
    const mascotaId = document.getElementById('mascotaId').value;
    const imagenFile = document.getElementById('imagen').files[0];

    if (!imagenFile) {
      mostrarMensaje('Por favor selecciona una imagen', 'error');
      return;
    }

    formData.append('action', 'subir');
    formData.append('mascotaId', mascotaId);
    formData.append('imagen', imagenFile);

    try {
      mostrarProgreso(true);
      const response = await fetch('ImagenMascotaServlet', {
        method: 'POST',
        body: formData
      });

      const resultado = await response.json();

      if (resultado.exito) {
        mostrarMensaje('✅ ' + resultado.mensaje, 'exito');
        document.getElementById('formSubirImagen').reset();
      } else {
        mostrarMensaje('❌ ' + resultado.mensaje, 'error');
      }
    } catch (error) {
      mostrarMensaje('❌ Error de conexión: ' + error.message, 'error');
    } finally {
      mostrarProgreso(false);
    }
  }

  // Función para cargar imágenes
  async function cargarImagenes() {
    const filtroMascota = document.getElementById('filtroMascota').value;
    let url = 'ImagenMascotaServlet?action=listar';

    if (filtroMascota) {
      url = `ImagenMascotaServlet?action=listarPorMascota&mascotaId=${filtroMascota}`;
    }

    try {
      const response = await fetch(url);
      const imagenes = await response.json();
      imagenesCargadas = imagenes;
      mostrarGaleria(imagenes);
    } catch (error) {
      mostrarMensaje('❌ Error al cargar imágenes: ' + error.message, 'error');
    }
  }

  // Función para mostrar galería
  function mostrarGaleria(imagenes) {
    const galeria = document.getElementById('galeria');

    if (imagenes.length === 0) {
      galeria.innerHTML = '<p style="text-align: center; color: #666;">No se encontraron imágenes</p>';
      return;
    }

    galeria.innerHTML = imagenes.map(imagen => `
                <div class="imagen-card">
                    <img src="${imagen.URL_Imagen}"
                         alt="Imagen de mascota ${imagen.r_Mascota}"
                         class="imagen-preview"
                         onclick="abrirModal('${imagen.URL_Imagen}')"
                         onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZGRkIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPkltYWdlbiBubyBkaXNwb25pYmxlPC90ZXh0Pjwvc3ZnPg=='">
                    <div class="imagen-info">
                        <p><strong>🆔 ID:</strong> ${imagen.imagenID}</p>
                        <p><strong>🐕 Mascota:</strong> ${imagen.r_Mascota}</p>
                        <p><strong>📅 Fecha:</strong> ${new Date(imagen.fecha_Carga).toLocaleDateString()}</p>
                        <p><strong>📊 Estado:</strong> ${imagen.estatus}</p>
                        <div style="margin-top: 1rem;">
                            <button onclick="eliminarImagen(${imagen.imagenID}, false)" class="btn btn-danger">🗑️ Eliminar</button>
                            <button onclick="eliminarImagen(${imagen.imagenID}, true)" class="btn btn-danger">💀 Eliminar Físico</button>
                            ${imagen.estatus === 'Baja' ? `<button onclick="reactivarImagen(${imagen.imagenID})" class="btn btn-success">🔄 Reactivar</button>` : ''}
                        </div>
                    </div>
                </div>
            `).join('');
  }

  // Función para buscar imagen específica
  async function buscarImagen() {
    const imagenId = document.getElementById('imagenIdBuscar').value;

    if (!imagenId) {
      mostrarMensaje('Por favor ingresa un ID de imagen', 'error');
      return;
    }

    try {
      const response = await fetch(`ImagenMascotaServlet?action=buscar&imagenId=${imagenId}`);
      const imagen = await response.json();

      const resultado = document.getElementById('resultadoBusqueda');
      if (imagen.imagenID) {
        mostrarGaleria([imagen]);
        document.getElementById('resultadoBusqueda').innerHTML = '';
        // Cambiar a la tab de galería para mostrar el resultado
        cambiarTab('galeria');
      } else {
        resultado.innerHTML = '<p style="color: #666;">No se encontró la imagen</p>';
      }
    } catch (error) {
      mostrarMensaje('❌ Error al buscar imagen: ' + error.message, 'error');
    }
  }

  // Función para eliminar imagen
  async function eliminarImagen(imagenId, fisico = false) {
    const accion = fisico ? 'eliminarFisico' : 'eliminarLogico';
    const confirmacion = fisico ?
            '¿Estás seguro de eliminar FÍSICAMENTE esta imagen? Esta acción no se puede deshacer.' :
            '¿Estás seguro de eliminar esta imagen?';

    if (!confirm(confirmacion)) return;

    try {
      const response = await fetch('ImagenMascotaServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `action=${accion}&imagenId=${imagenId}`
      });

      const resultado = await response.json();

      if (resultado.exito) {
        mostrarMensaje('✅ ' + resultado.mensaje, 'exito');
        cargarImagenes(); // Recargar galería
      } else {
        mostrarMensaje('❌ ' + resultado.mensaje, 'error');
      }
    } catch (error) {
      mostrarMensaje('❌ Error: ' + error.message, 'error');
    }
  }

  // Función para reactivar imagen
  async function reactivarImagen(imagenId) {
    try {
      const response = await fetch('ImagenMascotaServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `action=reactivar&imagenId=${imagenId}`
      });

      const resultado = await response.json();

      if (resultado.exito) {
        mostrarMensaje('✅ ' + resultado.mensaje, 'exito');
        cargarImagenes();
      } else {
        mostrarMensaje('❌ ' + resultado.mensaje, 'error');
      }
    } catch (error) {
      mostrarMensaje('❌ Error: ' + error.message, 'error');
    }
  }

  // Función para obtener estadísticas
  async function obtenerEstadisticas() {
    const mascotaId = document.getElementById('mascotaStats').value;

    if (!mascotaId) {
      mostrarMensaje('Por favor ingresa un ID de mascota', 'error');
      return;
    }

    try {
      const [conteoResponse, verificacionResponse, principalResponse] = await Promise.all([
        fetch(`ImagenMascotaServlet?action=contar&mascotaId=${mascotaId}`),
        fetch(`ImagenMascotaServlet?action=verificar&mascotaId=${mascotaId}`),
        fetch(`ImagenMascotaServlet?action=obtenerPrincipal&mascotaId=${mascotaId}`)
      ]);

      const conteo = await conteoResponse.json();
      const verificacion = await verificacionResponse.json();
      const principal = await principalResponse.json();

      document.getElementById('estadisticas').innerHTML = `
                    <div class="card">
                        <h4>📊 Estadísticas de la Mascota ${mascotaId}</h4>
                        <p><strong>📸 Total de imágenes activas:</strong> ${conteo.contador}</p>
                        <p><strong>🖼️ Tiene imágenes:</strong> ${verificacion.tieneImagenes ? 'Sí' : 'No'}</p>
                        <p><strong>🌟 Imagen principal:</strong> ${principal.urlImagen || 'No disponible'}</p>
                        ${principal.urlImagen ? `<img src="${principal.urlImagen}" style="max-width: 200px; border-radius: 10px; margin-top: 1rem;" alt="Imagen principal">` : ''}
                    </div>
                `;
    } catch (error) {
      mostrarMensaje('❌ Error al obtener estadísticas: ' + error.message, 'error');
    }
  }

  // Función para cargar imágenes eliminadas
  async function cargarImagenesEliminadas() {
    try {
      const response = await fetch('ImagenMascotaServlet?action=listarEliminadas');
      const imagenes = await response.json();

      const container = document.getElementById('imagenesEliminadas');
      if (imagenes.length === 0) {
        container.innerHTML = '<p style="color: #666;">No hay imágenes eliminadas</p>';
      } else {
        container.innerHTML = `
                        <div class="gallery">
                            ${imagenes.map(imagen => `
                                <div class="imagen-card">
                                    <img src="${imagen.URL_Imagen}"
                                         alt="Imagen eliminada"
                                         class="imagen-preview"
                                         onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZGRkIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPkltYWdlbiBubyBkaXNwb25pYmxlPC90ZXh0Pjwvc3ZnPg=='">
                                    <div class="imagen-info">
                                        <p><strong>🆔 ID:</strong> ${imagen.imagenID}</p>
                                        <p><strong>🐕 Mascota:</strong> ${imagen.r_Mascota}</p>
                                        <p><strong>📅 Eliminada:</strong> ${new Date(imagen.fecha_Carga).toLocaleDateString()}</p>
                                        <button onclick="reactivarImagen(${imagen.imagenID})" class="btn btn-success">🔄 Reactivar</button>
                                        <button onclick="eliminarImagen(${imagen.imagenID}, true)" class="btn btn-danger">💀 Eliminar Físico</button>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    `;
      }
    } catch (error) {
      mostrarMensaje('❌ Error al cargar imágenes eliminadas: ' + error.message, 'error');
    }
  }

  // Función para mostrar mensajes
  function mostrarMensaje(mensaje, tipo) {
    const mensajes = document.getElementById('mensajes');
    mensajes.innerHTML = `<div class="mensaje ${tipo}">${mensaje}</div>`;

    // Auto-ocultar después de 5 segundos
    setTimeout(() => {
      mensajes.innerHTML = '';
    }, 5000);
  }

  // Función para mostrar/ocultar barra de progreso
  function mostrarProgreso(mostrar) {
    const progressBar = document.getElementById('progressBar');
    const progressFill = document.getElementById('progressFill');

    if (mostrar) {
      progressBar.style.display = 'block';
      progressFill.style.width = '100%';
    } else {
      setTimeout(() => {
        progressBar.style.display = 'none';
        progressFill.style.width = '0%';
      }, 500);
    }
  }

  // Modal para ver imágenes en grande
  function abrirModal(src) {
    const modal = document.getElementById('modalImagen');
    const img = document.getElementById('imagenModal');
    img.src = src;
    modal.style.display = 'block';
  }

  // Cerrar modal
  document.querySelector('.close').onclick = function() {
    document.getElementById('modalImagen').style.display = 'none';
  }

  // Cerrar modal al hacer click fuera de la imagen
  window.onclick = function(event) {
    const modal = document.getElementById('modalImagen');
    if (event.target == modal) {
      modal.style.display = 'none';
    }
  }

  // Cargar imágenes al iniciar
  document.addEventListener('DOMContentLoaded', function() {
    cargarImagenes();
  });
</script>
</body>
</html>