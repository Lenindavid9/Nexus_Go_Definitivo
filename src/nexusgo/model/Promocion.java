/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

import java.util.Date;

/**
 *
 * @author USUARIO
 */
public class Promocion {
    
    private int idPromocion;
    private int idProducto;
    private double porcentajeDescuento;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    
    // Campos auxiliares opcionales (útiles para Join con Producto en Vistas)
    private String nombreProducto;
    private double precioOriginal;

    // Constructor vacío
    public Promocion() {
    }

    // Constructor completo
    public Promocion(int idPromocion, int idProducto, double porcentajeDescuento, Date fechaInicio, Date fechaFin, String estado) {
        this.idPromocion = idPromocion;
        this.idProducto = idProducto;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Getters y Setters Básicos
    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Getters y Setters Auxiliares
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    // Método de conveniencia para calcular el precio final con el descuento aplicado
    public double getPrecioConDescuento() {
        if (this.precioOriginal > 0) {
            return this.precioOriginal - (this.precioOriginal * (this.porcentajeDescuento / 100.0));
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "Promocion{" +
                "idPromocion=" + idPromocion +
                ", idProducto=" + idProducto +
                ", porcentajeDescuento=" + porcentajeDescuento +
                ", estado='" + estado + '\'' +
                '}';
    }
}
