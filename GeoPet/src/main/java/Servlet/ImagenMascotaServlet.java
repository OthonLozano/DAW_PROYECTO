package Servlet;

import Modelo.DAO.ImagenMascotaDAO;
import Modelo.JavaBeans.ImagenMascota;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ImagenMascotaServlet", urlPatterns = {"/ImagenMascotaServlet"})
public class ImagenMascotaServlet extends HttpServlet {

    ImagenMascotaDAO imagenDAO = new ImagenMascotaDAO();
    ImagenMascota imagen = new ImagenMascota();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                List<ImagenMascota> lista = imagenDAO.listarImagenes();
                request.setAttribute("imagenes", lista);
                request.getRequestDispatcher("Vistas_JSP/ImagenMascota/listar_imagenes.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                imagenDAO.eliminarImagen(idEliminar);
                response.sendRedirect("ImagenMascotaServlet?accion=listar");
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                ImagenMascota i = imagenDAO.buscarImagen(idEditar);
                request.setAttribute("imagen", i);
                request.getRequestDispatcher("Vistas_JSP/ImagenMascota/editar_imagen.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect("ImagenMascotaServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            switch (accion) {
                case "registrar":
                    imagen.setR_Mascota(Integer.parseInt(request.getParameter("r_mascota")));
                    imagen.setURL_Imagen(Integer.parseInt(request.getParameter("url_imagen")));
                    imagen.setFecha_Carga(parseDate(request.getParameter("fecha_carga")));
                    imagenDAO.registrarImagen(imagen);
                    request.setAttribute("mensaje", "¡Imagen registrada exitosamente!");
                    request.getRequestDispatcher("Vistas_JSP/ImagenMascota/registrar_imagen.jsp").forward(request, response);
                    break;

                case "actualizar":
                    imagen.setImagenID(Integer.parseInt(request.getParameter("imagenid")));
                    imagen.setR_Mascota(Integer.parseInt(request.getParameter("r_mascota")));
                    imagen.setURL_Imagen(Integer.parseInt(request.getParameter("url_imagen")));
                    imagen.setFecha_Carga(parseDate(request.getParameter("fecha_carga")));
                    imagenDAO.modificarImagen(imagen);
                    response.sendRedirect("ImagenMascotaServlet?accion=listar");
                    break;

                default:
                    response.sendRedirect("ImagenMascotaServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la imagen: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private Date parseDate(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(fecha);
    }
}