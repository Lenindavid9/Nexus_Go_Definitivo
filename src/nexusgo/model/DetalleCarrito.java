/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.model;

/**
 *
 * @author USUARIO
 */
public class DetalleCarrito {

    private int idProducto;
    private String nombre;
    private String nombreProducto;
    
      public DetalleCarrito(int idProducto, String nombre, double precioUnitario, int cantidad) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    private double precioUnitario;
    private int cantidad;

  

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

}
