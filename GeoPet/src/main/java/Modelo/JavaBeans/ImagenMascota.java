package Modelo.JavaBeans;

import java.io.Serializable;
import java.util.Date;

public class ImagenMascota implements Serializable {
    private static final long serialVersionUID = 1L;

    private int imagenID;
    private int r_Mascota;
    private String URL_Imagen;
    private Date fecha_Carga;
    private String estatus; // Nuevo campo: "Alta" o "Baja"

    // Constructores
    public ImagenMascota() {
        this.fecha_Carga = new Date();
        this.estatus = "Alta"; // Por defecto siempre "Alta"
    }

    public ImagenMascota(int r_Mascota, String URL_Imagen) {
        this.r_Mascota = r_Mascota;
        this.URL_Imagen = URL_Imagen;
        this.fecha_Carga = new Date();
        this.estatus = "Alta";
    }

    public ImagenMascota(int imagenID, int r_Mascota, String URL_Imagen, Date fecha_Carga, String estatus) {
        this.imagenID = imagenID;
        this.r_Mascota = r_Mascota;
        this.URL_Imagen = URL_Imagen;
        this.fecha_Carga = fecha_Carga;
        this.estatus = estatus;
    }

    // Getters y Setters
    public int getImagenID() {
        return imagenID;
    }

    public void setImagenID(int imagenID) {
        this.imagenID = imagenID;
    }

    public int getR_Mascota() {
        return r_Mascota;
    }

    public void setR_Mascota(int r_Mascota) {
        this.r_Mascota = r_Mascota;
    }

    public String getURL_Imagen() {
        return URL_Imagen;
    }

    public void setURL_Imagen(String URL_Imagen) {
        this.URL_Imagen = URL_Imagen;
    }

    public Date getFecha_Carga() {
        return fecha_Carga;
    }

    public void setFecha_Carga(Date fecha_Carga) {
        this.fecha_Carga = fecha_Carga;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    // Métodos de utilidad para el estatus
    public boolean estaActiva() {
        return "Alta".equalsIgnoreCase(estatus);
    }

    public boolean estaDadaDeBaja() {
        return "Baja".equalsIgnoreCase(estatus);
    }

    public void darDeAlta() {
        this.estatus = "Alta";
    }

    public void darDeBaja() {
        this.estatus = "Baja";
    }

    // Métodos de utilidad existentes
    public boolean tieneImagen() {
        return URL_Imagen != null && !URL_Imagen.trim().isEmpty();
    }

    public String getNombreArchivo() {
        if (URL_Imagen != null && URL_Imagen.contains("/")) {
            String[] partes = URL_Imagen.split("/");
            return partes[partes.length - 1];
        }
        return URL_Imagen;
    }

    public String getExtension() {
        if (URL_Imagen != null && URL_Imagen.contains(".")) {
            return URL_Imagen.substring(URL_Imagen.lastIndexOf("."));
        }
        return "";
    }

    public boolean esImagenValida() {
        if (!tieneImagen()) return false;

        String extension = getExtension().toLowerCase();
        return extension.equals(".jpg") || extension.equals(".jpeg") ||
                extension.equals(".png") || extension.equals(".gif") ||
                extension.equals(".webp");
    }

    // Método toString actualizado
    @Override
    public String toString() {
        return "ImagenMascota{" +
                "imagenID=" + imagenID +
                ", r_Mascota=" + r_Mascota +
                ", URL_Imagen='" + URL_Imagen + '\'' +
                ", fecha_Carga=" + fecha_Carga +
                ", estatus='" + estatus + '\'' +
                '}';
    }

    // Método equals para comparaciones
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ImagenMascota that = (ImagenMascota) obj;
        return imagenID == that.imagenID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(imagenID);
    }
}