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
<html>
<head>
  <title>Agregar Fotos</title>
</head>
<body>
<h1>Agregar Fotos de <%= nombreMascota %></h1>

<!-- DEBUG: Mostrar valores en pantalla -->
<div style="background: #f0f0f0; padding: 10px; margin: 10px 0; border: 1px solid #ccc;">
  <strong>DEBUG INFO:</strong><br>
  Parámetro recibido: <%= mascotaIdParam %><br>
  Mascota ID parseado: <%= mascotaId %><br>
  Nombre mascota: <%= nombreMascota %>
</div>

<div id="mensajes"></div>

<form id="formSubirImagen" enctype="multipart/form-data">
  <label>Seleccionar Imagen:</label>
  <input type="file" id="imagen" name="imagen" accept="image/*" required>
  <button type="submit">Subir Imagen</button>
</form>

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

    function mostrarMensaje(mensaje, tipo) {
      document.getElementById('mensajes').innerHTML = '<div style="padding: 10px; margin: 10px 0; background: ' +
              (tipo === 'exito' ? '#d4edda' : '#f8d7da') + ';">' + mensaje + '</div>';
    }

    function redirigirAHome() {
      // Esperar 2 segundos antes de redirigir para que el usuario vea el mensaje
      setTimeout(() => {
        console.log('DEBUG: Redirigiendo a HomeCliente...');
        // Puedes ajustar la URL según tu estructura de proyecto
        window.location.href = '/GeoPet_war_exploded/Vistas_JSP/Usuarios/HomeCliente.jsp';
        // O si usas un servlet para HomeCliente:
        // window.location.href = '/GeoPet_war_exploded/HomeClienteServlet';
      }, 2000);
    }

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
            mostrarMensaje('Imagen subida exitosamente para mascota ID: ' + mascotaIdFinal + '. Redirigiendo...', 'exito');
            // Limpiar el formulario
            document.getElementById('imagen').value = '';

            // NUEVA FUNCIONALIDAD: Redirigir después del éxito
            redirigirAHome();

          } else {
            mostrarMensaje('Error: ' + resultado.mensaje, 'error');
          }
        } else {
          console.log('DEBUG: Response NO OK, status:', response.status);
          const responseText = await response.text();
          console.log('DEBUG: Response text:', responseText);
          mostrarMensaje('Error de servidor (Status: ' + response.status + ')', 'error');
        }
      } catch (error) {
        console.log('DEBUG: Error en fetch:', error);
        mostrarMensaje('Error: ' + error.message, 'error');
      }
    });
  })();
</script>
</body>
</html>