<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            max-width: 900px;
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
            padding: 0.75rem;
            font-weight: 600;
            width: 100%;
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
                            <i class="bi bi-heart-fill me-2"></i>Registrar Nueva Mascota
                        </h2>
                        <p class="mb-0">Completa el formulario para registrar a tu mascota</p>
                    </div>
                    <div class="login-body">
                        <form action="SvMascota" method="POST">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="num_cliente" name="num_cliente" type="text" placeholder="Número de Cliente" required />
                                        <label for="num_cliente"><i class="bi bi-person-badge me-2"></i>Número de Cliente</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="alias" name="alias" type="text" placeholder="Alias" required />
                                        <label for="alias"><i class="bi bi-tag me-2"></i>Alias</label>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="especie" name="especie" type="text" placeholder="Especie" required />
                                        <label for="especie"><i class="bi bi-paw me-2"></i>Especie</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="raza" name="raza" type="text" placeholder="Raza" required />
                                        <label for="raza"><i class="bi bi-tag-fill me-2"></i>Raza</label>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="color_pelo" name="color_pelo" type="text" placeholder="Color de Pelo" required />
                                        <label for="color_pelo"><i class="bi bi-palette me-2"></i>Color de Pelo</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="fecha_nac" name="fecha_nac" type="date" placeholder="Fecha de Nacimiento" required />
                                        <label for="fecha_nac"><i class="bi bi-calendar-date me-2"></i>Fecha de Nacimiento</label>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="vacunas" name="vacunas" type="text" placeholder="Vacunas" required />
                                        <label for="vacunas"><i class="bi bi-shield-check me-2"></i>Vacunas</label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" id="observaciones" name="observaciones" type="text" placeholder="Observaciones" required />
                                        <label for="observaciones"><i class="bi bi-chat-text me-2"></i>Observaciones</label>
                                    </div>
                                </div>
                            </div>
                            <div class="d-flex justify-content-between mt-4">
                                <a href="index.jsp" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Volver
                                </a>
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
</body>
</html> 