package Modelo.JavaBeans;

/**
 * Clase para almacenar una mascota con sus relaciones
 * Reutiliza los JavaBeans existentes
 */
public class MascotaConRelaciones {

    private Mascotas mascota;
    private Especie especie;
    private ImagenMascota imagen; // Puede ser null si no tiene imagen

    // Constructores
    public MascotaConRelaciones() {}

    public MascotaConRelaciones(Mascotas mascota, Especie especie, ImagenMascota imagen) {
        this.mascota = mascota;
        this.especie = especie;
        this.imagen = imagen;
    }

    // Getters y Setters
    public Mascotas getMascota() {
        return mascota;
    }

    public void setMascota(Mascotas mascota) {
        this.mascota = mascota;
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

    // Métodos de conveniencia para acceso directo
    public boolean tieneImagen() {
        return imagen != null && imagen.getURL_Imagen() != null && !imagen.getURL_Imagen().trim().isEmpty();
    }

    public String getUrlImagen() {
        return tieneImagen() ? imagen.getURL_Imagen() : null;
    }

    @Override
    public String toString() {
        return "MascotaConRelaciones{" +
                "mascota=" + (mascota != null ? mascota.getNombre() : "null") +
                ", especie=" + (especie != null ? especie.getNombre() : "null") +
                ", tieneImagen=" + tieneImagen() +
                '}';
    }
}