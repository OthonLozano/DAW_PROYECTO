package Modelo.JavaBeans;
import java.io.Serializable;
import java.util.Date;

public class ImagenMascota implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ImagenID;
    private int R_Mascota;
    private int URL_Imagen;
    private Date Fecha_Carga;

    public ImagenMascota() {
    }

    public int getImagenID() {
        return ImagenID;
    }

    public void setImagenID(int imagenID) {
        ImagenID = imagenID;
    }

    public int getR_Mascota() {
        return R_Mascota;
    }

    public void setR_Mascota(int r_Mascota) {
        R_Mascota = r_Mascota;
    }

    public int getURL_Imagen() {
        return URL_Imagen;
    }

    public void setURL_Imagen(int URL_Imagen) {
        this.URL_Imagen = URL_Imagen;
    }

    public Date getFecha_Carga() {
        return Fecha_Carga;
    }

    public void setFecha_Carga(Date fecha_Carga) {
        Fecha_Carga = fecha_Carga;
    }
}
