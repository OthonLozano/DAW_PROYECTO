<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Modelo.JavaBeans.Especie" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GeoPet - Registrar Mascota</title>
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
            max-width: 1000px;
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

        .form-control, .form-select {
            border-radius: 0.375rem;
            padding: 0.75rem 1rem;
            border: 1px solid #e0e0e0;
            margin-bottom: 1.25rem;
        }

        .form-control:focus, .form-select:focus {
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

        .alert-success {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
            border-radius: 0.375rem;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .alert-warning {
            background-color: #fff3cd;
            border-color: #ffeaa7;
            color: #856404;
            border-radius: 0.375rem;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .alert-info {
            background-color: #d1ecf1;
            border-color: #bee5eb;
            color: #0c5460;
            border-radius: 0.375rem;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .debug {
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 0.75rem;
            margin-bottom: 1rem;
            font-size: 0.875rem;
            color: #6c757d;
        }
    </style>
</head>
<body class="bg-light">

<main class="login-container">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-11">
                <div class="login-card">
                    <div class="login-header">
                        <h2 class="mb-3">
                            <i class="bi bi-heart-fill me-2"></i>Registrar Nueva Mascota
                        </h2>
                        <p class="mb-0">Completa el formulario para registrar a tu mascota</p>
                    </div>
                    <div class="login-body">
                        <!-- Mostrar mensaje de éxito si existe -->
                        <% if (request.getAttribute("mensaje") != null) { %>
                        <div class="alert alert-success">
                            <i class="bi bi-check-circle me-2"></i><%= request.getAttribute("mensaje") %>
                        </div>
                        <% } %>

                        <!-- Debug de especies -->
                        <%
                            List<Especie> especies = (List<Especie>) request.getAttribute("especies");
                            if (especies == null) {
                        %>
                        <div class="debug">
                            <i class="bi bi-bug me-2"></i>Debug: Lista de especies es null
                        </div>
                        <%
                        } else {
                        %>
                        <div class="debug">
                            <i class="bi bi-info-circle me-2"></i>Debug: Se encontraron <%= especies.size() %> especies
                        </div>
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
                        <div class="alert alert-info">
                            <i class="bi bi-person-check me-2"></i>Registrando mascota para el usuario ID: <%= usuarioId %>
                        </div>
                        <%
                        } else {
                        %>
                        <div class="alert alert-warning">
                            <i class="bi bi-exclamation-triangle me-2"></i><strong>AVISO:</strong> No hay usuario logueado en la sesión.
                        </div>
                        <%
                            }
                        %>

                        <form action="MascotaServlet" method="post">
                            <input type="hidden" name="accion" value="registrar" />

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="nombre" name="nombre" type="text" placeholder="Nombre" required />
                                        <label for="nombre"><i class="bi bi-heart me-2"></i>Nombre</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <select class="form-select" id="r_especie" name="r_especie" required>
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
                                        <label for="r_especie"><i class="bi bi-paw me-2"></i>Especie</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="edad" name="edad" type="number" placeholder="Edad" required min="0" />
                                        <label for="edad"><i class="bi bi-calendar-date me-2"></i>Edad (en meses)</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <select class="form-select" id="sexo" name="sexo" required>
                                            <option value="">Seleccionar...</option>
                                            <option value="Macho">Macho</option>
                                            <option value="Hembra">Hembra</option>
                                        </select>
                                        <label for="sexo"><i class="bi bi-gender-ambiguous me-2"></i>Sexo</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="color" name="color" type="text" placeholder="Color" required />
                                        <label for="color"><i class="bi bi-palette me-2"></i>Color</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="caracteristicasdistintivas" name="caracteristicasdistintivas" type="text" placeholder="Características Distintivas" />
                                        <label for="caracteristicasdistintivas"><i class="bi bi-card-text me-2"></i>Características Distintivas</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <select class="form-select" id="microchip" name="microchip" required>
                                            <option value="">Seleccionar...</option>
                                            <option value="true">Sí</option>
                                            <option value="false">No</option>
                                        </select>
                                        <label for="microchip"><i class="bi bi-cpu me-2"></i>¿Tiene microchip?</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="numero_microchip" name="numero_microchip" type="number" placeholder="Número de Microchip" />
                                        <label for="numero_microchip"><i class="bi bi-hash me-2"></i>Número de Microchip (opcional)</label>
                                    </div>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <select class="form-select" id="estado" name="estado" required>
                                            <option value="">Seleccionar...</option>
                                            <option value="Perdida">Perdida</option>
                                            <option value="En casa">En casa</option>
                                        </select>
                                        <label for="estado"><i class="bi bi-house me-2"></i>Estado</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="fecha_registro" name="fecha_registro" type="date" placeholder="Fecha de Registro" required />
                                        <label for="fecha_registro"><i class="bi bi-calendar-event me-2"></i>Fecha de Registro</label>
                                    </div>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between align-items-center mt-4">
                                <div>
                                    <a href="index.jsp" class="btn btn-secondary">
                                        <i class="bi bi-house"></i> Menú Principal
                                    </a>
                                </div>
                                <button type="submit" class="btn btn-login">
                                    <i class="bi bi-save"></i> Registrar Mascota
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