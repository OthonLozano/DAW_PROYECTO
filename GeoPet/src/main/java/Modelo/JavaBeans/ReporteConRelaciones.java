package Modelo.JavaBeans;

/**
 * Clase para almacenar un reporte de desaparición con sus relaciones
 * Reutiliza los JavaBeans existentes
 */
public class ReporteConRelaciones {

    private ReporteDesaparicion reporte;
    private Mascotas mascota;
    private Usuarios usuario;
    private Especie especie;
    private ImagenMascota imagen; // Imagen principal de la mascota (puede ser null)

    // Constructores
    public ReporteConRelaciones() {}

    public ReporteConRelaciones(ReporteDesaparicion reporte, Mascotas mascota,
                                Usuarios usuario, Especie especie, ImagenMascota imagen) {
        this.reporte = reporte;
        this.mascota = mascota;
        this.usuario = usuario;
        this.especie = especie;
        this.imagen = imagen;
    }

    // Getters y Setters
    public ReporteDesaparicion getReporte() {
        return reporte;
    }

    public void setReporte(ReporteDesaparicion reporte) {
        this.reporte = reporte;
    }

    public Mascotas getMascota() {
        return mascota;
    }

    public void setMascota(Mascotas mascota) {
        this.mascota = mascota;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public ImagenMascota getImagen() {
        return imagen;
    }

    public void setImagen(ImagenMascota imagen) {
        this.imagen = imagen;
    }

    // Métodos de conveniencia
    public boolean tieneImagen() {
        return imagen != null && imagen.getURL_Imagen() != null && !imagen.getURL_Imagen().trim().isEmpty();
    }

    public String getUrlImagen() {
        return tieneImagen() ? imagen.getURL_Imagen() : null;
    }

    public String getNombreCompleto() {
        if (usuario != null) {
            return usuario.getNombre() + " " + usuario.getApellidoMat();
        }
        return "Usuario desconocido";
    }

    public boolean esReporteActivo() {
        return reporte != null && "Alta".equalsIgnoreCase(reporte.getEstatus()) &&
                ("Activo".equalsIgnoreCase(reporte.getEstadoReporte()) ||
                        "Perdido".equalsIgnoreCase(reporte.getEstadoReporte()));
    }

    @Override
    public String toString() {
        return "ReporteConRelaciones{" +
                "reporte=" + (reporte != null ? reporte.getReporteID() : "null") +
                ", mascota=" + (mascota != null ? mascota.getNombre() : "null") +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                ", especie=" + (especie != null ? especie.getNombre() : "null") +
                ", tieneImagen=" + tieneImagen() +
                '}';
    }
}