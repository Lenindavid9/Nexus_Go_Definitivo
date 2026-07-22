/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import nexusgo.model.Producto;
import nexusgo.view.VistaProductoDetalles;
/**
 *
 * @author HOME
 */
public class ControladorDetallesProducto implements ActionListener {
    
  private final VistaProductoDetalles vista;
    private final Producto producto;
    private final ControladorPrincipalCliente controladorPrincipal;

    /**
     * Constructor que enlaza los datos del producto, la vista de detalles y el controlador principal.
     */
    public ControladorDetallesProducto(VistaProductoDetalles vista, Producto producto, ControladorPrincipalCliente controladorPrincipal) {
        this.vista = vista;
        this.producto = producto;
        this.controladorPrincipal = controladorPrincipal;

        // Escuchar el botón de regresar de la vista de detalles
        this.vista.btnVolver.addActionListener(this);

        mostrarDatosProducto();
    }

    /**
     * Envía la información del modelo hacia la vista de forma desacoplada.
     */
    private void mostrarDatosProducto() {
    if (producto != null) {
        // Se envía getPrecioVenta() para mostrar el precio correcto al cliente
        this.vista.mostrarDetalleProducto(
            producto.getNombreProducto(),
            producto.getPrecioVenta(), // 
            producto.getDescripcion(),
            producto.getUrlImagen()
        );
    }
}

    @Override
    public void actionPerformed(ActionEvent e) {
        // Regresar al catálogo principal restaurando el panel
        if (e.getSource() == vista.btnVolver) {
            controladorPrincipal.restaurarTiendaYCatalogo();
        }
    }
}
