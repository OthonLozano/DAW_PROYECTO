package JavaBeans;
import java.io.Serializable;
import java.util.Date;

public class Comentarios implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ComentarioID;
    private int R_Usuario;
    private int R_Reporte;
    private String Contenido;
    private Date Fecha_Comentario;

    public Comentarios() {
    }

    public int getComentarioID() {
        return ComentarioID;
    }

    public void setComentarioID(int comentarioID) {
        ComentarioID = comentarioID;
    }

    public int getR_Usuario() {
        return R_Usuario;
    }

    public void setR_Usuario(int r_Usuario) {
        R_Usuario = r_Usuario;
    }

    public int getR_Reporte() {
        return R_Reporte;
    }

    public void setR_Reporte(int r_Reporte) {
        R_Reporte = r_Reporte;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public Date getFecha_Comentario() {
        return Fecha_Comentario;
    }

    public void setFecha_Comentario(Date fecha_Comentario) {
        Fecha_Comentario = fecha_Comentario;
    }
}
