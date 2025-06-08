<!-- Footer -->
<footer class="geopet-footer">
    <div class="container">
        <div class="row">
            <div class="col-md-4 mb-3">
                <h5><i class="bi bi-geo-alt-fill me-2"></i>GeoPet</h5>
                <p>Sistema de gestión integral para el cuidado y seguimiento de mascotas.
                    Conectando dueños, veterinarios y amantes de los animales.</p>
                <div class="d-flex">
                    <a href="#" class="me-3"><i class="bi bi-facebook"></i></a>
                    <a href="#" class="me-3"><i class="bi bi-twitter"></i></a>
                    <a href="#" class="me-3"><i class="bi bi-instagram"></i></a>
                    <a href="#"><i class="bi bi-linkedin"></i></a>
                </div>
            </div>
            <div class="col-md-2 mb-3">
                <h6>Navegación</h6>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/index.jsp">Inicio</a></li>
                    <li><a href="${pageContext.request.contextPath}/mascotas?action=listar">Mascotas</a></li>
                    <li><a href="${pageContext.request.contextPath}/avistamientos?action=listar">Avistamientos</a></li>
                    <li><a href="${pageContext.request.contextPath}/usuarios?action=listar">Usuarios</a></li>
                </ul>
            </div>
            <div class="col-md-2 mb-3">
                <h6>Servicios</h6>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/reportes">Reportes</a></li>
                    <li><a href="${pageContext.request.contextPath}/comentarios?action=listar">Comentarios</a></li>
                    <li><a href="${pageContext.request.contextPath}/ayuda">Ayuda</a></li>
                    <li><a href="${pageContext.request.contextPath}/contacto">Contacto</a></li>
                </ul>
            </div>
            <div class="col-md-2 mb-3">
                <h6>Legal</h6>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/privacidad">Privacidad</a></li>
                    <li><a href="${pageContext.request.contextPath}/terminos">Términos</a></li>
                    <li><a href="${pageContext.request.contextPath}/cookies">Cookies</a></li>
                </ul>
            </div>
            <div class="col-md-2 mb-3">
                <h6>Contacto</h6>
                <ul class="list-unstyled">
                    <li><i class="bi bi-envelope me-2"></i>info@geopet.com</li>
                    <li><i class="bi bi-phone me-2"></i>+52 229 123 4567</li>
                    <li><i class="bi bi-geo-alt me-2"></i>Veracruz, México</li>
                </ul>
            </div>
        </div>
        <hr class="my-4">
        <div class="row align-items-center">
            <div class="col-md-6">
                <p class="mb-0">&copy; 2025 GeoPet. Todos los derechos reservados.</p>
            </div>
            <div class="col-md-6 text-md-end">
                <p class="mb-0">Desarrollado con <i class="bi bi-heart-fill text-danger"></i> para los amantes de las mascotas</p>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.bundle.min.js"></script>

<!-- GeoPet Custom JS (opcional) -->
<script>
    // Agregar clase active al enlace actual
    document.addEventListener('DOMContentLoaded', function() {
        const currentLocation = location.pathname;
        const menuItems = document.querySelectorAll('.navbar-nav .nav-link');

        menuItems.forEach(item => {
            if(item.getAttribute('href') === currentLocation) {
                item.classList.add('active');
            }
        });

        // Animación fade-in para el contenido principal
        const mainContent = document.querySelector('.main-container');
        if(mainContent) {
            mainContent.classList.add('fade-in');
        }
    });
</script>
</body>
</html>