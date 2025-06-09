package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import Modelo.DAO.ImagenMascotaDAO;
import Modelo.JavaBeans.ImagenMascota;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

@WebServlet(name = "ImagenMascotaServlet", urlPatterns = {"/ImagenMascotaServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class ImagenMascotaServlet extends HttpServlet {

    private ImagenMascotaDAO imagenDAO = new ImagenMascotaDAO();

    // Directorio base para guardar las imágenes
    private static final String UPLOAD_DIR = "uploads/mascotas/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Test endpoint para verificar que el servlet funciona
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("{\"exito\": true, \"mensaje\": \"Servlet funcionando correctamente\"}");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG ImagenMascotaServlet: doPost ejecutado ===");

        try {
            // Debug completo de la request
            System.out.println("DEBUG: Content-Type de la request = " + request.getContentType());
            System.out.println("DEBUG: Content-Length = " + request.getContentLength());

            String action = request.getParameter("action");
            System.out.println("DEBUG: Action recibida = " + action);

            String mascotaIdParam = request.getParameter("mascotaId");
            System.out.println("DEBUG: MascotaId = " + mascotaIdParam);

            // Debug de todos los parámetros
            System.out.println("DEBUG: Todos los parámetros:");
            request.getParameterMap().forEach((key, values) -> {
                System.out.println("  " + key + " = " + String.join(", ", values));
            });

            // Debug de parts
            System.out.println("DEBUG: Intentando obtener parts...");
            Collection<Part> parts = request.getParts();
            System.out.println("DEBUG: Número de parts = " + parts.size());

            for (Part part : parts) {
                System.out.println("DEBUG: Part name = " + part.getName());
                System.out.println("DEBUG: Part size = " + part.getSize());
                System.out.println("DEBUG: Part content-type = " + part.getContentType());
            }

            if ("subir".equals(action)) {
                subirImagen(request, response);
            } else {
                System.out.println("ERROR: Acción no válida = " + action);
                enviarError(response, "Acción no válida: " + action);
            }

        } catch (Exception e) {
            System.out.println("ERROR: Exception en doPost = " + e.getMessage());
            e.printStackTrace();
            enviarError(response, "Error interno: " + e.getMessage());
        }
    }

    private void subirImagen(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        System.out.println("=== DEBUG: Método subirImagen ejecutado ===");

        try {
            String mascotaIdParam = request.getParameter("mascotaId");
            System.out.println("DEBUG: MascotaId en subirImagen = " + mascotaIdParam);

            if (mascotaIdParam == null || mascotaIdParam.trim().isEmpty()) {
                System.out.println("ERROR: ID de mascota no proporcionado");
                enviarError(response, "ID de mascota no proporcionado");
                return;
            }

            int mascotaId = Integer.parseInt(mascotaIdParam);
            System.out.println("DEBUG: MascotaId parseado = " + mascotaId);

            // Obtener el archivo subido
            Part filePart = request.getPart("imagen");
            if (filePart == null || filePart.getSize() == 0) {
                System.out.println("ERROR: No se ha seleccionado ningún archivo o está vacío");
                enviarError(response, "No se ha seleccionado ningún archivo");
                return;
            }

            System.out.println("DEBUG: Archivo recibido - Tamaño: " + filePart.getSize() + " bytes");
            System.out.println("DEBUG: Content-Type: " + filePart.getContentType());

            // Obtener el nombre original del archivo
            String nombreOriginal = getFileName(filePart);
            System.out.println("DEBUG: Nombre original: " + nombreOriginal);

            // Validar tipo de archivo
            if (!esImagenValida(filePart)) {
                enviarError(response, "Tipo de archivo no válido. Solo se permiten imágenes (JPG, PNG, GIF).");
                return;
            }

            // Generar nombre único para el archivo
            String extension = getFileExtension(nombreOriginal);
            String nombreUnico = "mascota_" + mascotaId + "_" + System.currentTimeMillis() + extension;
            System.out.println("DEBUG: Nombre único generado: " + nombreUnico);

            // Crear la estructura de directorios
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadPath = applicationPath + File.separator + UPLOAD_DIR;

            System.out.println("DEBUG: Application path: " + applicationPath);
            System.out.println("DEBUG: Upload path: " + uploadPath);

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("DEBUG: Directorio creado: " + created);
            }

            // Guardar el archivo físicamente
            String rutaCompleta = uploadPath + nombreUnico;
            System.out.println("DEBUG: Guardando archivo en: " + rutaCompleta);

            filePart.write(rutaCompleta);
            System.out.println("DEBUG: Archivo guardado exitosamente");

            // Crear el objeto ImagenMascota para la BD
            ImagenMascota imagen = new ImagenMascota();
            imagen.setR_Mascota(mascotaId);
            imagen.setURL_Imagen(UPLOAD_DIR + nombreUnico); // Ruta relativa para la BD
            imagen.setFecha_Carga(new Date());
            imagen.setEstatus("Alta");

            System.out.println("DEBUG: Objeto ImagenMascota creado:");
            System.out.println("  - Mascota ID: " + imagen.getR_Mascota());
            System.out.println("  - URL: " + imagen.getURL_Imagen());
            System.out.println("  - Fecha: " + imagen.getFecha_Carga());
            System.out.println("  - Estatus: " + imagen.getEstatus());

            // Guardar en la base de datos
            System.out.println("DEBUG: Guardando en base de datos...");
            boolean guardadoEnBD = imagenDAO.registrarImagen(imagen);

            if (guardadoEnBD) {
                System.out.println("DEBUG: ¡Imagen guardada exitosamente en BD!");

                // Respuesta de éxito
                String resultado = "{"
                        + "\"exito\": true,"
                        + "\"mensaje\": \"Imagen subida y guardada exitosamente\","
                        + "\"urlImagen\": \"" + imagen.getURL_Imagen() + "\","
                        + "\"nombreArchivo\": \"" + nombreUnico + "\""
                        + "}";

                enviarRespuestaJSON(response, resultado);

            } else {
                System.out.println("ERROR: Fallo al guardar en la base de datos");

                // Si falla la BD, eliminar el archivo físico
                File archivoGuardado = new File(rutaCompleta);
                if (archivoGuardado.exists()) {
                    boolean eliminado = archivoGuardado.delete();
                    System.out.println("DEBUG: Archivo físico eliminado tras fallo en BD: " + eliminado);
                }

                enviarError(response, "Error al guardar la imagen en la base de datos");
            }

        } catch (NumberFormatException e) {
            System.out.println("ERROR: ID de mascota inválido = " + e.getMessage());
            enviarError(response, "ID de mascota inválido");
        } catch (Exception e) {
            System.out.println("ERROR: Exception general = " + e.getMessage());
            e.printStackTrace();
            enviarError(response, "Error interno del servidor: " + e.getMessage());
        }
    }

    private String getFileName(Part part) {
        System.out.println("DEBUG: Obteniendo nombre del archivo...");
        String contentDisposition = part.getHeader("content-disposition");
        System.out.println("DEBUG: content-disposition = " + contentDisposition);

        if (contentDisposition == null) {
            return "archivo_sin_nombre.jpg";
        }

        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                System.out.println("DEBUG: Nombre extraído = " + fileName);
                return fileName.isEmpty() ? "archivo_sin_nombre.jpg" : fileName;
            }
        }
        return "archivo_sin_nombre.jpg";
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg"; // Extensión por defecto
    }

    private boolean esImagenValida(Part filePart) {
        String contentType = filePart.getContentType();
        if (contentType == null) return false;

        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }

    private void enviarRespuestaJSON(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();

        System.out.println("DEBUG: Respuesta JSON enviada = " + json);
    }

    private void enviarError(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String jsonError = "{\"exito\": false, \"mensaje\": \"" + mensaje + "\"}";
        out.print(jsonError);
        out.flush();

        System.out.println("DEBUG: Error enviado = " + jsonError);
    }
}