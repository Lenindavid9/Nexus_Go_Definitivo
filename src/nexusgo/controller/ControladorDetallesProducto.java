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
import nexusgo.view.VistaDetallesProducto;
/**
 *
 * @author HOME
 */
public class ControladorDetallesProducto implements ActionListener {
    
  private final VistaDetallesProducto vista;
    private final Producto producto;
    private final ControladorPrincipalCliente controladorPrincipal;

    /*
     * Constructor que enlaza los datos del producto, la vista de detalles y el controlador principal.
     */
    public ControladorDetallesProducto(VistaDetallesProducto vista, Producto producto, ControladorPrincipalCliente controladorPrincipal) {
        this.vista = vista;
        this.producto = producto;
        this.controladorPrincipal = controladorPrincipal;

        // Escuchar el botón de regresar de la vista de detalles
        this.vista.btnVolver.addActionListener(this);

        mostrarDatosProducto();
    }

    /*
     * Inyecta la información del producto en los componentes visuales del panel.
     */
    private void mostrarDatosProducto() {
        if (producto != null) {
            // Corregido con los nombres exactos de los componentes de la vista
            vista.lblNombreProducto.setText(producto.getNombreProducto().toUpperCase());
            vista.lblPrecioProducto.setText("$ " + String.format("%,.0f", producto.getPrecioCompra()));
            
            if (producto.getDescripcion()!= null && !producto.getDescripcion().isEmpty()) {
                vista.txtDescripcion.setText(producto.getDescripcion());
            } else {
                vista.txtDescripcion.setText("Este producto no cuenta con una descripción detallada registrada.");
            }
            
            // Cargar y escalar la imagen directamente sobre el JLabel de la vista
            String rutaImagen = producto.getUrlImagen();
            if (rutaImagen == null || rutaImagen.isEmpty()) {
                rutaImagen = "src/nexusgo/img/default.jpg";
            }
            
            ImageIcon imgOriginal = new ImageIcon(rutaImagen);
            if (imgOriginal.getImage() != null) {
                Image escalada = imgOriginal.getImage().getScaledInstance(340, 260, Image.SCALE_SMOOTH);
                vista.lblImagenProducto.setText("");
                vista.lblImagenProducto.setIcon(new ImageIcon(escalada));
            }
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
