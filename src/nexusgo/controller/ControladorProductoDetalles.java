/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaProductoDetalles;

/**
 *
 * @author HOME
 */
public class ControladorProductoDetalles implements ActionListener {
    
    private final VistaProductoDetalles vistaDetalles;
    private final VistaPrincipalCliente vistaPadre;
    private final ProductoDao productoDAO;
    private final int idProducto;

    /*
     * En el constructor recibimos la vista de detalles, la vista principal para
     * gestionar la navegación dinámica y el ID del producto seleccionado.
     */
    public ControladorProductoDetalles(VistaProductoDetalles vistaDetalles, VistaPrincipalCliente vistaPadre, int idProducto) {
        this.vistaDetalles = vistaDetalles;
        this.vistaPadre = vistaPadre;
        this.idProducto = idProducto;
        this.productoDAO = new ProductoDao();

        // Asignación de eventos a los botones de volver y a la barra lateral
        this.vistaDetalles.btnVolver.addActionListener(this);
        

        // Carga de la información del producto
        cargarInformacionProducto();
    }

    /*
     * Consultamos el producto mediante el DAO y enviamos únicamente los tipos
     * primitivos a la vista para mantener el patrón MVC desacoplado.
     */
    private void cargarInformacionProducto() {
        try {
            Producto producto = productoDAO.buscarPorId(this.idProducto);

            if (producto != null) {
                this.vistaDetalles.mostrarDetalleProducto(
                    producto.getNombreProducto(),
                    producto.getPrecioCompra(),
                    producto.getDescripcion(),
                    producto.getUrlImagen()
                );
            } else {
                JOptionPane.showMessageDialog(vistaPadre, 
                        "No se encontró la información del producto.",
                        "Producto No Encontrado", 
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaPadre, 
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de Sistema", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Manejo de acciones para regresar al catálogo o interactuar con la barra lateral.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Volver al catálogo principal
       
        // Ir al historial de compras
//        if (e.getSource() == vistaDetalles.sidebar.btnHistorial) {
//            JOptionPane.showMessageDialog(vistaPadre, 
//                    "Cargando el historial de compras...", 
//                    "Historial de Compras", 
//                    JOptionPane.INFORMATION_MESSAGE);
//        }

        // Ir a la sección de mis citas
        
    }
}
