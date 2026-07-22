/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class PromocionCombo {

    private int idPromocion;
    private String nombreCombo;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private double precioCombo;
    private String rutaImagen;

    // Listas con los productos y servicios incluidos en el combo
    private List<Producto> productos;
    private List<Servicios> servicios;

    public PromocionCombo() {
    }

    public PromocionCombo(int idPromocion, String nombreCombo, String descripcion, Date fechaInicio, Date fechaFin, double precioCombo, String rutaImagen, List<Producto> productos, List<Servicios> servicios) {
        this.idPromocion = idPromocion;
        this.nombreCombo = nombreCombo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioCombo = precioCombo;
        this.rutaImagen = rutaImagen;
        this.productos = productos;
        this.servicios = servicios;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getNombreCombo() {
        return nombreCombo;
    }

    public void setNombreCombo(String nombreCombo) {
        this.nombreCombo = nombreCombo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(double precioCombo) {
        this.precioCombo = precioCombo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Servicios> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicios> servicios) {
        this.servicios = servicios;
    }

}
