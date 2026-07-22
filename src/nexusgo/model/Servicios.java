/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

/**
 *
 * @author USUARIO
 */
public class Servicios {

    private int idServicio;
    private String nombreServicio;
    private String descripcion;
    private int duracionMinutos;
    private double precio;
    private boolean activo;

    // Constructor vacío
    public Servicios() {
    }

    // Constructor completo
    public Servicios(int idServicio, String nombreServicio, String descripcion, int duracionMinutos, double precio, boolean activo) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
        this.activo = activo;
    }

    // Getters y Setters
    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Servicios{"
                + "idServicio=" + idServicio
                + ", nombreServicio='" + nombreServicio + '\''
                + ", descripcion='" + descripcion + '\''
                + ", duracionMinutos=" + duracionMinutos
                + ", precio=" + precio
                + ", activo=" + activo
                + '}';
    }
}
