/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;
import java.util.Date;

/**
 *
 * @author INGRID
 */
public class Factura {
    
    private int idFactura;
    private int idVenta;
    private int idCliente;
    private int idCaja;
    private double subtotal;
    private double descuentoAplicado;
    private double total;
    private Date fechaEmision;

    // Constructor completo
    public Factura(int idFactura, int idVenta, int idCliente, int idCaja, double subtotal, double descuentoAplicado, double total, Date fechaEmision) {
        this.idFactura = idFactura;
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.idCaja = idCaja;
        this.subtotal = subtotal;
        this.descuentoAplicado = descuentoAplicado;
        this.total = total;
        this.fechaEmision = fechaEmision;
    }

    // Constructor vacío (Esencial para DAOs/Mapeos de base de datos)
    public Factura() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    // Alias para compatibilidad directa con la VistaFactura
    public Date getFechaVenta() {
        return fechaEmision;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", idVenta=" + idVenta +
                ", idCliente=" + idCliente +
                ", idCaja=" + idCaja +
                ", subtotal=" + subtotal +
                ", descuentoAplicado=" + descuentoAplicado +
                ", total=" + total +
                ", fechaEmision=" + fechaEmision +
                '}';
    }
}
