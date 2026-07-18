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
    private List<Producto> listaProductos; // Guarda en memoria los productos cargados para buscarlos facilmente

    /**
     * Constructor que enlaza la vista y activa la escucha de todos los componentes.
     * @param vista Vista principal de la interfaz de cliente.
     */
    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this.vista = vista;
        this.productoDAO = new ProductoDao(); 

        // 1. Escuchar el boton base "Inicio" (bCasa) de tu componente modular VistaBarraLateral
        this.vista.sidebar.bCasa.addActionListener(this);

        // 2. Escuchar los dos botones de la barra lateral del cliente
        this.vista.historial.addActionListener(this);
        this.vista.CitasVigentes.addActionListener(this);

        // 3. Escuchar el boton de cerrar sesion
        this.vista.btnCerrarSesion.addActionListener(this);
        
        // Al iniciar, cargamos de inmediato el catalogo de productos
        cargarCatalogo();
    }

    /**
     * Trae los productos desde la base de datos y le pide a la vista que los dibuje.
     */
    private void cargarCatalogo() {
        try {
            // Guardamos la lista de productos en nuestra variable global
            this.listaProductos = productoDAO.listar();
            
            // Enviamos la lista y "this" (este controlador que escucha los clics de raton) a la vista
            this.vista.cargarProductosEnInterfaz(this.listaProductos);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al conectar con el catalogo de productos: " + ex.getMessage(),
                    "Error de Sistema",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
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

    // =========================================================================
    //               EVENTOS DE MOUSE (CLIC EN LAS TARJETAS)
    // =========================================================================
    @Override
    public void mouseClicked(MouseEvent e) {
        // Verificamos si el usuario hizo clic sobre una tarjeta (JPanel)
        if (e.getSource() instanceof JPanel) {
            JPanel tarjetaPresionada = (JPanel) e.getSource();
            
            // Si la tarjeta tiene asignado un "Name" (que contiene el ID del producto)
            if (tarjetaPresionada.getName() != null) {
                try {
                    int idProducto = Integer.parseInt(tarjetaPresionada.getName());
                    
                    // Buscamos el producto correspondiente en nuestra lista en memoria
                    Producto productoSeleccionado = buscarProductoPorId(idProducto);

                    if (productoSeleccionado != null) {
                        // Abrimos la vista de detalles de forma limpia pasandole el producto
                        VistaDetallesProducto vistaDetalle = new VistaDetallesProducto();
                        new ControladorDetallesProducto(vistaDetalle, productoSeleccionado);
                        vistaDetalle.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    // Previene errores en caso de que algun panel tenga un nombre no numerico
                }
            }
        }
    }

    // Metodos obligatorios de la interfaz MouseListener (se dejan vacios)
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // =========================================================================
    //               EVENTOS DE ACCION (BOTONES FIJOS)
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) {

        // Clic en el boton "Citas Vigentes" (Abre la vista de reservas y cambia el panel central)
        if (e.getSource() == vista.CitasVigentes) {
            JOptionPane.showMessageDialog(vista,
                    "Mostrando tus proximas citas agendadas en la peluqueria.",
                    "Mis Citas",
                    JOptionPane.INFORMATION_MESSAGE);

            VistaReservarCitas panelReserva = new VistaReservarCitas();

            // Instanciamos su propio controlador (Pasamos: vista formulario, vista principal, ID de usuario de prueba)
            new ControladorReservarCita(panelReserva, vista, 1);

            // Cambiamos la pantalla de inmediato en el panel dinamico central de tu vista
            vista.getContenidoCentralDinamico().removeAll();
            vista.getContenidoCentralDinamico().add(panelReserva);
            vista.getContenidoCentralDinamico().revalidate();
            vista.getContenidoCentralDinamico().repaint();
        }

        // Clic en el boton "Inicio" (Recarga y refresca el catalogo de productos)
        if (e.getSource() == vista.sidebar.bCasa) {
            cargarCatalogo();
        }

        // Clic en el boton "Historial"
        if (e.getSource() == vista.historial) {
            JOptionPane.showMessageDialog(vista,
                    "Abriendo el historial completo de tus citas atendidas y productos comprados.",
                    "Modulo Historial",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Clic en el boton "Cerrar Sesion"
        if (e.getSource() == vista.btnCerrarSesion) {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Estás seguro de que deseas cerrar tu sesion en Nexus GO?",
                    "Cerrar Sesion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                vista.dispose(); // Cierra la interfaz actual liberando memoria
            }
        }
    }
   
}

