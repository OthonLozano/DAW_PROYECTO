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

import java.io.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

        System.out.println("=== DEBUG ImagenMascotaServlet: doGet ejecutado ===");

        String action = request.getParameter("action");
        System.out.println("DEBUG: Action recibida en GET = " + action);

        if ("obtener".equals(action)) {
            obtenerImagen(request, response);
        } else {
            // Test endpoint para verificar que el servlet funciona
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"exito\": true, \"mensaje\": \"Servlet funcionando correctamente\"}");
            out.flush();
        }
    }

    private void obtenerImagen(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mascotaIdStr = request.getParameter("mascotaId");

        System.out.println("=== DEBUG OBTENER IMAGEN ===");
        System.out.println("DEBUG: Parámetro mascotaId recibido: " + mascotaIdStr);

        if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
            System.out.println("ERROR: mascotaId es null o vacío");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de mascota requerido");
            return;
        }

        try {
            int mascotaId = Integer.parseInt(mascotaIdStr.trim());
            System.out.println("DEBUG: mascotaId parseado: " + mascotaId);

            // Obtener las imágenes de la mascota usando tu método existente
            List<ImagenMascota> imagenes = imagenDAO.listarImagenesPorMascota(mascotaId);
            System.out.println("DEBUG: Número de imágenes encontradas: " +
                    (imagenes != null ? imagenes.size() : 0));

            if (imagenes == null || imagenes.isEmpty()) {
                System.out.println("WARNING: No se encontraron imágenes para la mascota " + mascotaId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontraron imágenes");
                return;
            }

            // Usar la primera imagen encontrada
            ImagenMascota imagen = imagenes.get(0);
            String rutaImagen = imagen.getURL_Imagen();
            System.out.println("DEBUG: Ruta de imagen obtenida: " + rutaImagen);

            // Construir la ruta completa del archivo
            String applicationPath = getServletContext().getRealPath("");
            String rutaCompleta = applicationPath + File.separator + rutaImagen;
            System.out.println("DEBUG: Application path: " + applicationPath);
            System.out.println("DEBUG: Ruta completa del archivo: " + rutaCompleta);

            File archivoImagen = new File(rutaCompleta);
            System.out.println("DEBUG: ¿Archivo existe? " + archivoImagen.exists());
            System.out.println("DEBUG: ¿Es archivo? " + archivoImagen.isFile());
            System.out.println("DEBUG: Tamaño del archivo: " + archivoImagen.length() + " bytes");
            System.out.println("DEBUG: Ruta absoluta: " + archivoImagen.getAbsolutePath());

            if (!archivoImagen.exists() || !archivoImagen.isFile()) {
                System.out.println("ERROR: El archivo de imagen no existe: " + rutaCompleta);

                // Intentar con diferentes variaciones de la ruta
                String[] rutasAlternativas = {
                        applicationPath + rutaImagen,                    // sin File.separator
                        applicationPath + "/" + rutaImagen,             // con /
                        applicationPath + "\\" + rutaImagen,            // con \
                        rutaImagen                                      // ruta tal como está
                };

                System.out.println("DEBUG: Probando rutas alternativas...");
                boolean encontrado = false;
                for (String rutaAlt : rutasAlternativas) {
                    File archivoAlt = new File(rutaAlt);
                    System.out.println("DEBUG: Probando: " + rutaAlt + " -> Existe: " + archivoAlt.exists());
                    if (archivoAlt.exists() && archivoAlt.isFile()) {
                        archivoImagen = archivoAlt;
                        rutaCompleta = rutaAlt;
                        encontrado = true;
                        System.out.println("DEBUG: ✅ Archivo encontrado en: " + rutaAlt);
                        break;
                    }
                }

                if (!encontrado) {
                    System.out.println("ERROR: No se pudo encontrar el archivo en ninguna ruta");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo de imagen no encontrado");
                    return;
                }
            }

            // Determinar el tipo de contenido
            String mimeType = getServletContext().getMimeType(archivoImagen.getName());
            if (mimeType == null) {
                String extension = rutaImagen.substring(rutaImagen.lastIndexOf('.') + 1).toLowerCase();
                switch (extension) {
                    case "jpg":
                    case "jpeg":
                        mimeType = "image/jpeg";
                        break;
                    case "png":
                        mimeType = "image/png";
                        break;
                    case "gif":
                        mimeType = "image/gif";
                        break;
                    case "webp":
                        mimeType = "image/webp";
                        break;
                    default:
                        mimeType = "application/octet-stream";
                }
            }

            System.out.println("DEBUG: MIME type detectado: " + mimeType);

            // Configurar la respuesta
            response.setContentType(mimeType);
            response.setContentLength((int) archivoImagen.length());

            // Configurar headers para caché
            response.setHeader("Cache-Control", "max-age=3600"); // 1 hora
            response.setDateHeader("Last-Modified", archivoImagen.lastModified());

            // Enviar el archivo
            try (FileInputStream fileInputStream = new FileInputStream(archivoImagen);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                 OutputStream outputStream = response.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                int totalBytes = 0;

                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }

                outputStream.flush();
                System.out.println("DEBUG: ✅ Imagen enviada exitosamente. Bytes enviados: " + totalBytes);

            } catch (IOException e) {
                System.out.println("ERROR: Error al enviar la imagen: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

        } catch (NumberFormatException e) {
            System.out.println("ERROR: ID de mascota inválido: " + mascotaIdStr);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de mascota inválido");
        } catch (Exception e) {
            System.out.println("ERROR: Error inesperado al obtener imagen: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
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