<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GeoPet - Editar Perfil</title>
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Font Awesome -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
  <!-- Custom CSS -->
  <link href="${pageContext.request.contextPath}/Resources/CSS/custom.css" rel="stylesheet">
</head>
<body class="bg-geopet-light">
<%@include file="Vistas_JSP/Common/header.jsp"%>

<main class="main-container">
  <div class="container py-5">
    <!-- Mensajes de error -->
    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="fas fa-exclamation-circle"></i> ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <!-- Profile Card -->
    <div class="geopet-card mb-5 fade-in">
      <div class="geopet-card-header">
        <h2><i class="fas fa-user-edit me-2"></i>Editar Perfil</h2>
      </div>
      <div class="geopet-card-body">
        <form action="${pageContext.request.contextPath}/UsuariosServlet" method="post" id="editarPerfilForm">
          <input type="hidden" name="id" value="${usuario.usuarioID}">
          
          <div class="row">
            <div class="col-md-4">
              <div class="form-floating mb-3">
                <input type="text" class="form-control" id="nombre" name="nombre"
                       value="${usuario.nombre}" required maxlength="50" />
                <label for="nombre"><i class="fas fa-user me-2"></i>Nombre</label>
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-floating mb-3">
                <input type="text" class="form-control" id="apellidoPat" name="apellidoPat"
                       value="${usuario.apellidoPat}" maxlength="50" />
                <label for="apellidoPat"><i class="fas fa-user me-2"></i>Apellido Paterno</label>
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-floating mb-3">
                <input type="text" class="form-control" id="apellidoMat" name="apellidoMat"
                       value="${usuario.apellidoMat}" maxlength="50" />
                <label for="apellidoMat"><i class="fas fa-user me-2"></i>Apellido Materno</label>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="form-floating mb-3">
                <input type="email" class="form-control" id="email" name="email"
                       value="${usuario.email}" required maxlength="100" />
                <label for="email"><i class="fas fa-envelope me-2"></i>Correo Electrónico</label>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-floating mb-3">
                <input type="tel" class="form-control" id="telefono" name="telefono"
                       value="${usuario.telefono}" maxlength="15" />
                <label for="telefono"><i class="fas fa-phone me-2"></i>Teléfono</label>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="form-floating mb-3">
                <input type="text" class="form-control" id="direccion" name="direccion"
                       value="${usuario.direccion}" maxlength="200" />
                <label for="direccion"><i class="fas fa-map-marker-alt me-2"></i>Dirección</label>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-floating mb-3">
                <input type="text" class="form-control" id="ciudad" name="ciudad"
                       value="${usuario.ciudad}" maxlength="50" />
                <label for="ciudad"><i class="fas fa-city me-2"></i>Ciudad</label>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="form-floating mb-3">
                <input type="password" class="form-control" id="contrasenia" name="contrasenia"
                       placeholder="Nueva Contraseña" minlength="6" />
                <label for="contrasenia"><i class="fas fa-lock me-2"></i>Nueva Contraseña (opcional)</label>
                <div class="form-text">Deja en blanco para mantener la contraseña actual</div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-floating mb-3">
                <input type="password" class="form-control" id="confirmarContrasenia"
                       name="confirmarContrasenia" placeholder="Confirmar Contraseña" />
                <label for="confirmarContrasenia"><i class="fas fa-lock me-2"></i>Confirmar Contraseña</label>
              </div>
            </div>
          </div>

          <div class="d-flex justify-content-between align-items-center mt-4">
            <a href="${pageContext.request.contextPath}/Vistas_JSP/Usuarios/HomeCliente.jsp"
               class="btn btn-outline-secondary">
              <i class="fas fa-arrow-left me-2"></i>Volver
            </a>
            <button type="submit" class="btn btn-geopet-primary">
              <i class="fas fa-save me-2"></i>Guardar Cambios
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</main>

<%@include file="Vistas_JSP/Common/footer.jsp"%>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom JS -->
<script>
  document.addEventListener('DOMContentLoaded', function() {
    // Animaciones
    const animatedElements = document.querySelectorAll('.fade-in');

    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('animate');
        }
      });
    }, { threshold: 0.1 });

    animatedElements.forEach(el => observer.observe(el));

    // Validación de contraseñas
    const contraseniaInput = document.getElementById('contrasenia');
    const confirmarContraseniaInput = document.getElementById('confirmarContrasenia');
    const form = document.getElementById('editarPerfilForm');

    function validarContrasenias() {
      const contrasenia = contraseniaInput.value;
      const confirmar = confirmarContraseniaInput.value;

      if (contrasenia && contrasenia !== confirmar) {
        confirmarContraseniaInput.setCustomValidity('Las contraseñas no coinciden');
      } else {
        confirmarContraseniaInput.setCustomValidity('');
      }
    }

    contraseniaInput.addEventListener('input', validarContrasenias);
    confirmarContraseniaInput.addEventListener('input', validarContrasenias);

    // Validación del formulario
    form.addEventListener('submit', function(event) {
      const contrasenia = contraseniaInput.value;
      const confirmar = confirmarContraseniaInput.value;

      // Si se ingresó una contraseña, validar que coincidan
      if (contrasenia && contrasenia !== confirmar) {
        event.preventDefault();
        alert('Las contraseñas no coinciden');
        return;
      }

      // Si se ingresó una contraseña, validar longitud mínima
      if (contrasenia && contrasenia.length < 6) {
        event.preventDefault();
        alert('La contraseña debe tener al menos 6 caracteres');
        return;
      }

      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }

      form.classList.add('was-validated');
    }, false);

    // Validación en tiempo real del email
    const emailInput = document.getElementById('email');
    emailInput.addEventListener('input', function() {
      const email = this.value;
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (email && !emailRegex.test(email)) {
        this.setCustomValidity('Por favor, ingrese un email válido');
      } else {
        this.setCustomValidity('');
      }
    });

    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
      const alerts = document.querySelectorAll('.alert');
      alerts.forEach(function(alert) {
        const bsAlert = new bootstrap.Alert(alert);
        bsAlert.close();
      });
    }, 5000);
  });
</script>
</body>
</html>