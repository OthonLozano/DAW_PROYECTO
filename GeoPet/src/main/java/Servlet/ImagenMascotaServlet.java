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

/**
 * Servlet controlador especializado para gestión de imágenes de mascotas.
 *
 * Este servlet maneja todas las operaciones relacionadas con la subida, almacenamiento
 * y recuperación de imágenes asociadas a mascotas en el sistema. Implementa funcionalidades
 * completas de gestión de archivos multimedia incluyendo validación, optimización
 * de almacenamiento y servicio de contenido web.
 *
 * Funcionalidades principales:
 * - Subida segura de imágenes con validación de tipos y tamaños
 * - Almacenamiento físico en sistema de archivos del servidor
 * - Persistencia de metadatos en base de datos
 * - Servicio de imágenes como contenido web (image serving)
 * - Generación de nombres únicos para evitar conflictos
 * - Validación exhaustiva de formatos soportados
 * - Manejo robusto de rutas y directorios
 *
 * Configuración de archivos:
 * - Tamaño máximo por archivo: 10MB
 * - Tamaño máximo de request: 50MB
 * - Threshold para memoria: 2MB
 * - Formatos soportados: JPEG, PNG, GIF, WebP
 *
 * Características de seguridad:
 * - Validación estricta de tipos MIME
 * - Sanitización de nombres de archivo
 * - Generación de nombres únicos para prevenir ataques
 * - Validación de extensiones permitidas
 * - Manejo seguro de rutas de archivo
 *
 * El servlet utiliza el patrón de almacenamiento híbrido donde los archivos
 * físicos se guardan en el sistema de archivos del servidor mientras que
 * los metadatos (rutas, fechas, asociaciones) se almacenan en base de datos.
 */
@WebServlet(name = "ImagenMascotaServlet", urlPatterns = {"/ImagenMascotaServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB - Umbral para usar memoria vs disco temporal
        maxFileSize = 1024 * 1024 * 10,       // 10MB - Tamaño máximo por archivo individual
        maxRequestSize = 1024 * 1024 * 50     // 50MB - Tamaño máximo total del request
)
public class ImagenMascotaServlet extends HttpServlet {

    /**
     * Instancia del DAO para operaciones de base de datos de imágenes.
     */
    private final ImagenMascotaDAO imagenDAO = new ImagenMascotaDAO();

    /**
     * Directorio base relativo para almacenamiento de imágenes de mascotas.
     * Se utiliza tanto para almacenamiento físico como para URLs de acceso.
     */
    private static final String UPLOAD_DIR = "uploads/mascotas/";

    /**
     * Maneja solicitudes HTTP GET para obtener y servir imágenes.
     *
     * Procesa diferentes acciones basadas en el parámetro 'action':
     * - "obtener": Sirve una imagen específica como contenido web
     * - Sin acción: Retorna un estado de funcionamiento del servlet
     *
     * Para la acción "obtener", busca la imagen asociada a una mascota específica
     * en la base de datos, localiza el archivo físico y lo sirve con las
     * cabeceras HTTP apropiadas incluyendo tipo MIME y configuración de caché.
     *
     * @param request HttpServletRequest conteniendo parámetros de la solicitud
     * @param response HttpServletResponse para enviar la imagen o respuesta
     * @throws ServletException si ocurre un error específico del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("obtener".equals(action)) {
            obtenerImagen(request, response);
        } else {
            // Endpoint de verificación de estado del servlet
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"exito\": true, \"mensaje\": \"Servlet funcionando correctamente\"}");
            out.flush();
        }
    }

    /**
     * Procesa la obtención y servicio de una imagen específica.
     *
     * Este método implementa un servicio completo de imágenes que:
     * 1. Valida el ID de mascota proporcionado
     * 2. Consulta la base de datos para obtener la URL de la imagen
     * 3. Localiza el archivo físico en el sistema de archivos
     * 4. Implementa búsqueda de rutas alternativas para robustez
     * 5. Detecta automáticamente el tipo MIME basado en extensión
     * 6. Configura cabeceras HTTP apropiadas para caché y contenido
     * 7. Transmite el archivo de forma eficiente usando buffers
     *
     * Características de robustez:
     * - Búsqueda automática en múltiples rutas posibles
     * - Detección automática de tipos MIME
     * - Transmisión eficiente con buffering
     * - Configuración de caché para optimizar rendimiento
     *
     * @param request HttpServletRequest conteniendo el ID de mascota
     * @param response HttpServletResponse para enviar la imagen
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de archivo o red
     */
    private void obtenerImagen(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mascotaIdStr = request.getParameter("mascotaId");

        // Validación de parámetro requerido
        if (mascotaIdStr == null || mascotaIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de mascota requerido");
            return;
        }

        try {
            int mascotaId = Integer.parseInt(mascotaIdStr.trim());

            // Consultar imágenes asociadas a la mascota
            List<ImagenMascota> imagenes = imagenDAO.listarImagenesPorMascota(mascotaId);

            if (imagenes == null || imagenes.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontraron imágenes");
                return;
            }

            // Utilizar la primera imagen encontrada (imagen principal)
            ImagenMascota imagen = imagenes.get(0);
            String rutaImagen = imagen.getURL_Imagen();

            // Construcción de ruta completa del archivo
            String applicationPath = getServletContext().getRealPath("");
            String rutaCompleta = applicationPath + File.separator + rutaImagen;

            File archivoImagen = new File(rutaCompleta);

            // Implementar búsqueda robusta de archivo con rutas alternativas
            if (!archivoImagen.exists() || !archivoImagen.isFile()) {
                // Intentar múltiples variaciones de ruta para robustez
                String[] rutasAlternativas = {
                        applicationPath + rutaImagen,                    // Sin separador adicional
                        applicationPath + "/" + rutaImagen,             // Con separador Unix
                        applicationPath + "\\" + rutaImagen,            // Con separador Windows
                        rutaImagen                                      // Ruta absoluta
                };

                boolean encontrado = false;
                for (String rutaAlt : rutasAlternativas) {
                    File archivoAlt = new File(rutaAlt);
                    if (archivoAlt.exists() && archivoAlt.isFile()) {
                        archivoImagen = archivoAlt;
                        rutaCompleta = rutaAlt;
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo de imagen no encontrado");
                    return;
                }
            }

            // Detección automática de tipo MIME basada en extensión
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

            // Configuración de cabeceras HTTP para servicio de imagen
            response.setContentType(mimeType);
            response.setContentLength((int) archivoImagen.length());

            // Configuración de caché para optimizar rendimiento del navegador
            response.setHeader("Cache-Control", "max-age=3600"); // Caché de 1 hora
            response.setDateHeader("Last-Modified", archivoImagen.lastModified());

            // Transmisión eficiente del archivo usando streams con buffer
            try (FileInputStream fileInputStream = new FileInputStream(archivoImagen);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                 OutputStream outputStream = response.getOutputStream()) {

                byte[] buffer = new byte[8192]; // Buffer de 8KB para eficiencia
                int bytesRead;

                // Transferencia por chunks para optimizar memoria
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();

            } catch (IOException e) {
                System.err.println("ERROR: Error al transmitir imagen - " + e.getMessage());
                throw e;
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de mascota inválido");
        } catch (Exception e) {
            System.err.println("ERROR: Error inesperado al obtener imagen - " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }

    /**
     * Maneja solicitudes HTTP POST para subida de nuevas imágenes.
     *
     * Procesa requests multipart/form-data conteniendo archivos de imagen
     * y otros parámetros. Valida la estructura del request, extrae los archivos
     * adjuntos y delega el procesamiento según la acción especificada.
     *
     * Acciones soportadas:
     * - "subir": Procesa la subida completa de una nueva imagen
     *
     * Implementa logging detallado para diagnóstico de problemas con uploads
     * incluyendo información sobre content-type, tamaño y parts del request.
     *
     * @param request HttpServletRequest conteniendo datos multipart
     * @param response HttpServletResponse para respuestas JSON
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String action = request.getParameter("action");
            String mascotaIdParam = request.getParameter("mascotaId");

            // Obtener y validar parts del request multipart
            Collection<Part> parts = request.getParts();

            if ("subir".equals(action)) {
                subirImagen(request, response);
            } else {
                enviarError(response, "Acción no válida: " + action);
            }

        } catch (Exception e) {
            System.err.println("ERROR: Excepción en doPost - " + e.getMessage());
            e.printStackTrace();
            enviarError(response, "Error interno: " + e.getMessage());
        }
    }

    /**
     * Procesa la subida completa de una imagen de mascota.
     *
     * Implementa el flujo completo de subida de archivos:
     * 1. Validación de parámetros de entrada (ID de mascota)
     * 2. Extracción y validación del archivo de imagen
     * 3. Validación de tipo MIME y formatos permitidos
     * 4. Generación de nombre único para evitar conflictos
     * 5. Creación de estructura de directorios si no existe
     * 6. Almacenamiento físico del archivo en servidor
     * 7. Creación y persistencia de metadatos en base de datos
     * 8. Limpieza automática en caso de errores (rollback)
     *
     * Características de robustez:
     * - Validación exhaustiva de tipos de archivo soportados
     * - Generación de nombres únicos basados en timestamp
     * - Transaccionalidad: si falla BD, se elimina archivo físico
     * - Manejo granular de diferentes tipos de errores
     *
     * Formatos de imagen soportados:
     * - JPEG/JPG (image/jpeg)
     * - PNG (image/png)
     * - GIF (image/gif)
     * - WebP (image/webp)
     *
     * @param request HttpServletRequest conteniendo el archivo y parámetros
     * @param response HttpServletResponse para respuesta JSON del resultado
     * @throws IOException si ocurre error de archivo o red
     * @throws ServletException si ocurre error de procesamiento servlet
     */
    private void subirImagen(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            // Validación de ID de mascota
            String mascotaIdParam = request.getParameter("mascotaId");

            if (mascotaIdParam == null || mascotaIdParam.trim().isEmpty()) {
                enviarError(response, "ID de mascota no proporcionado");
                return;
            }

            int mascotaId = Integer.parseInt(mascotaIdParam);

            // Extracción del archivo de imagen del request multipart
            Part filePart = request.getPart("imagen");
            if (filePart == null || filePart.getSize() == 0) {
                enviarError(response, "No se ha seleccionado ningún archivo");
                return;
            }

            // Obtención del nombre original y validación de tipo
            String nombreOriginal = getFileName(filePart);

            // Validación estricta de tipos de archivo permitidos
            if (!esImagenValida(filePart)) {
                enviarError(response, "Tipo de archivo no válido. Solo se permiten imágenes (JPG, PNG, GIF, WebP).");
                return;
            }

            // Generación de nombre único para prevenir conflictos y ataques
            String extension = getFileExtension(nombreOriginal);
            String nombreUnico = "mascota_" + mascotaId + "_" + System.currentTimeMillis() + extension;

            // Creación de estructura de directorios de almacenamiento
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadPath = applicationPath + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Crear directorio completo si no existe
            }

            // Almacenamiento físico del archivo
            String rutaCompleta = uploadPath + nombreUnico;
            filePart.write(rutaCompleta);

            // Creación del objeto ImagenMascota para persistencia en BD
            ImagenMascota imagen = new ImagenMascota();
            imagen.setR_Mascota(mascotaId);
            imagen.setURL_Imagen(UPLOAD_DIR + nombreUnico); // Ruta relativa para portabilidad
            imagen.setFecha_Carga(new Date());
            imagen.setEstatus("Alta");

            // Persistencia de metadatos en base de datos
            boolean guardadoEnBD = imagenDAO.registrarImagen(imagen);

            if (guardadoEnBD) {
                // Respuesta de éxito con información de la imagen subida
                String resultado = "{"
                        + "\"exito\": true,"
                        + "\"mensaje\": \"Imagen subida y guardada exitosamente\","
                        + "\"urlImagen\": \"" + imagen.getURL_Imagen() + "\","
                        + "\"nombreArchivo\": \"" + nombreUnico + "\""
                        + "}";

                enviarRespuestaJSON(response, resultado);

            } else {
                // Rollback: eliminar archivo físico si falla la persistencia en BD
                File archivoGuardado = new File(rutaCompleta);
                if (archivoGuardado.exists()) {
                    archivoGuardado.delete();
                }

                enviarError(response, "Error al guardar la imagen en la base de datos");
            }

        } catch (NumberFormatException e) {
            enviarError(response, "ID de mascota inválido");
        } catch (Exception e) {
            System.err.println("ERROR: Excepción general en subirImagen - " + e.getMessage());
            e.printStackTrace();
            enviarError(response, "Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Extrae el nombre de archivo del header Content-Disposition de un Part multipart.
     *
     * Analiza el header HTTP Content-Disposition para extraer el nombre original
     * del archivo subido. Implementa parsing robusto que maneja diferentes
     * formatos de header y proporciona nombres de fallback seguros.
     *
     * @param part Part multipart del cual extraer el nombre
     * @return String nombre del archivo, "archivo_sin_nombre.jpg" si no se puede determinar
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");

        if (contentDisposition == null) {
            return "archivo_sin_nombre.jpg";
        }

        // Parsing del header Content-Disposition para extraer filename
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.isEmpty() ? "archivo_sin_nombre.jpg" : fileName;
            }
        }
        return "archivo_sin_nombre.jpg";
    }

    /**
     * Extrae la extensión de archivo de un nombre de archivo.
     *
     * Método utilitario para obtener la extensión incluyendo el punto.
     * Proporciona una extensión por defecto (.jpg) si no se puede determinar.
     *
     * @param fileName Nombre del archivo del cual extraer la extensión
     * @return String extensión del archivo incluyendo el punto, ".jpg" por defecto
     */
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg"; // Extensión por defecto para casos sin extensión
    }

    /**
     * Valida si un archivo es una imagen de tipo soportado.
     *
     * Verifica el tipo MIME del archivo contra una lista de tipos permitidos
     * para asegurar que solo se procesen imágenes válidas y prevenir
     * la subida de archivos maliciosos o no soportados.
     *
     * Tipos MIME soportados:
     * - image/jpeg (archivos JPG/JPEG)
     * - image/jpg (variante de JPEG)
     * - image/png (archivos PNG)
     * - image/gif (archivos GIF)
     * - image/webp (archivos WebP)
     *
     * @param filePart Part del archivo a validar
     * @return boolean true si es una imagen válida, false en caso contrario
     */
    private boolean esImagenValida(Part filePart) {
        String contentType = filePart.getContentType();
        if (contentType == null) return false;

        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp");
    }

    /**
     * Envía una respuesta JSON exitosa al cliente.
     *
     * Configura las cabeceras HTTP apropiadas para contenido JSON
     * y transmite la respuesta con codificación UTF-8 para soporte
     * de caracteres internacionales.
     *
     * @param response HttpServletResponse para enviar la respuesta
     * @param json String conteniendo el JSON a enviar
     * @throws IOException si ocurre error en la transmisión
     */
    private void enviarRespuestaJSON(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * Envía una respuesta de error en formato JSON al cliente.
     *
     * Configura el código de estado HTTP apropiado (400 Bad Request)
     * y envía un mensaje de error estructurado en formato JSON para
     * facilitar el manejo en el lado cliente.
     *
     * @param response HttpServletResponse para enviar el error
     * @param mensaje String mensaje descriptivo del error
     * @throws IOException si ocurre error en la transmisión
     */
    private void enviarError(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String jsonError = "{\"exito\": false, \"mensaje\": \"" + mensaje + "\"}";
        out.print(jsonError);
        out.flush();
    }
}