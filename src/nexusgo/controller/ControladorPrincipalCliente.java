/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao; // Tu manejador de base de datos
import nexusgo.view.VistaDetallesProducto;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaReservarCitas;

/**
 *
 * @author USUARIO
 */
public class ControladorPrincipalCliente implements ActionListener, MouseListener {
    
   private final VistaPrincipalCliente vista;
    private final ProductoDao productoDAO;
    private List<Producto> listaProductos;

    /*
     * Constructor que enlaza la vista y activa la escucha de todos los componentes.
     * @param vista Vista principal de la interfaz de cliente.
     */
    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this.vista = vista;
        this.productoDAO = new ProductoDao(); 

        // 1. Escuchar el botón base "Inicio" de la barra lateral modular
        this.vista.sidebar.bCasa.addActionListener(this);

        // 2. Escuchar los botones adicionales del cliente
        this.vista.historial.addActionListener(this);
        this.vista.CitasVigentes.addActionListener(this);

        // 3. Escuchar el botón de cerrar sesión
        this.vista.btnCerrarSesion.addActionListener(this);
        
        // Al iniciar, cargamos de inmediato el catálogo de productos
        cargarCatalogo();
    }

    /*
     * Trae los productos desde la base de datos y le pide a la vista que los dibuje.
     */
    public void cargarCatalogo() {
        try {
            this.listaProductos = productoDAO.listar();
            
            // Pasamos "this" para enlazar los eventos de mouse a las tarjetas autogeneradas
            this.vista.cargarProductosEnInterfaz(this.listaProductos, this);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al conectar con el catálogo de productos: " + ex.getMessage(),
                    "Error de Sistema",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Busca un producto en la lista en memoria usando su ID.
     */
    private Producto buscarProductoPorId(int id) {
        if (listaProductos != null) {
            for (Producto prod : listaProductos) {
                if (prod.getIdProducto() == id) {
                    return prod;
                }
            }
        }
        return null;
    }

    // Eventos de mouse para gestionar los clics en las tarjetas de productos
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof JPanel) {
            JPanel tarjetaPresionada = (JPanel) e.getSource();
            
            if (tarjetaPresionada.getName() != null) {
                try {
                    int idProducto = Integer.parseInt(tarjetaPresionada.getName());
                    Producto productoSeleccionado = buscarProductoPorId(idProducto);

                    if (productoSeleccionado != null) {
                        // Cambiamos el contenedor central dinámico para mostrar el detalle elegantemente
                        VistaDetallesProducto panelDetalle = new VistaDetallesProducto();
                        
                        // Instanciamos su respectivo controlador pasándole la referencia del principal para poder regresar
                        new ControladorDetallesProducto(panelDetalle, productoSeleccionado, this);
                        
                        vista.getContenidoCentralDinamico().removeAll();
                        vista.getContenidoCentralDinamico().add(panelDetalle);
                        vista.getContenidoCentralDinamico().revalidate();
                        vista.getContenidoCentralDinamico().repaint();
                    }
                } catch (NumberFormatException ex) {
                    // Previene excepciones si se procesa un nombre no numérico
                }
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        // Clic en el botón "Citas Vigentes"
        if (e.getSource() == vista.CitasVigentes) {
            VistaReservarCitas panelReserva = new VistaReservarCitas();
            new ControladorReservarCita(panelReserva, vista, 1);

            vista.getContenidoCentralDinamico().removeAll();
            vista.getContenidoCentralDinamico().add(panelReserva);
            vista.getContenidoCentralDinamico().revalidate();
            vista.getContenidoCentralDinamico().repaint();
        }

        // Clic en el botón "Inicio" (Recarga el catálogo restaurando la vista base)
        if (e.getSource() == vista.sidebar.bCasa) {
            // Limpiamos los paneles internos y re-inyectamos los componentes por defecto de la tienda
            vista.restaurarComponentesTienda();
            cargarCatalogo();
        }

        // Clic en el botón "Historial"
        if (e.getSource() == vista.historial) {
            JOptionPane.showMessageDialog(vista,
                    "Abriendo el historial completo de tus citas atendidas y productos comprados.",
                    "Módulo Historial",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Clic en el botón "Cerrar Sesión"
        if (e.getSource() == vista.btnCerrarSesion) {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Estás seguro de que deseas cerrar tu sesión en Nexus GO?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                vista.dispose();
            }
        }
    }
    public void restaurarTiendaYCatalogo() {
    this.vista.restaurarComponentesTienda();
    cargarCatalogo();
}
}

