/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaProductoDetalles; // Confirma el nombre de tu clase Vista
import nexusgo.view.VistaReservarCitas;

/**
 *
 * @author USUARIO
 */

/**
 * Controlador principal para la interfaz de cliente en Nexus GO.
 */
public class ControladorPrincipalCliente implements ActionListener, MouseListener {

    private final VistaPrincipalCliente vista;
    private final ProductoDao productoDAO;
    private List<Producto> listaProductos;
    private List<Producto> listaPromociones;
    private final int idUsuarioLogueado;

    public ControladorPrincipalCliente(VistaPrincipalCliente vista, int idUsuarioLogueado) {
        this.vista = vista;
        this.idUsuarioLogueado = idUsuarioLogueado;
        this.productoDAO = new ProductoDao();

        // Enlace de eventos del menú lateral (Sidebar)
        if (this.vista.sidebar != null) {
            if (this.vista.sidebar.bCasa != null) this.vista.sidebar.bCasa.addActionListener(this);
            if (this.vista.sidebar.misCitas != null) this.vista.sidebar.misCitas.addActionListener(this);
        }

        // Enlace de eventos de botones en la cabecera / vista
        if (this.vista.btnHistorial != null) this.vista.btnHistorial.addActionListener(this);
        if (this.vista.btnReservarCita != null) this.vista.btnReservarCita.addActionListener(this);
        if (this.vista.btnCerrarSesion != null) this.vista.btnCerrarSesion.addActionListener(this);

        cargarCatalogo();
    }

    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this(vista, 1);
    }

    public void cargarCatalogo() {
        try {
            this.listaProductos = productoDAO.listar();
            this.listaPromociones = productoDAO.listarPromociones();

            this.vista.limpiarGridProductos();
            this.vista.limpiarGridPromociones();

            // Cargar productos regulares (Usando precio de venta para el cliente)
            if (this.listaProductos != null) {
                for (Producto p : this.listaProductos) {
                    this.vista.agregarTarjetaProducto(
                        p.getIdProducto(),
                        p.getNombreProducto(),
                        p.getPrecioVenta(), // Ajustado a Precio de Venta
                        p.getUrlImagen(),
                        this
                    );
                }
            }

            // Cargar promociones
            if (this.listaPromociones != null) {
                for (Producto promo : this.listaPromociones) {
                    this.vista.agregarTarjetaPromocion(
                        promo.getIdProducto(),
                        promo.getNombreProducto(),
                        promo.getPrecioVenta(), // Ajustado a Precio de Venta
                        promo.getUrlImagen(),
                        this
                    );
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al conectar con el catálogo de productos: " + ex.getMessage(),
                    "Error de Sistema",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Producto buscarProductoPorId(int id) {
        if (listaProductos != null) {
            for (Producto prod : listaProductos) {
                if (prod.getIdProducto() == id) return prod;
            }
        }
        if (listaPromociones != null) {
            for (Producto promo : listaPromociones) {
                if (promo.getIdProducto() == id) return promo;
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object origen = e.getSource();
        String idStr = null;

        // Búsqueda del ID en el componente cliqueado o en su contenedor padre
        if (origen instanceof JComponent) {
            JComponent comp = (JComponent) origen;
            idStr = comp.getName();
            
            // Si el clic fue en un JLabel interno sin ID, buscamos en el panel padre
            if (idStr == null && comp.getParent() != null) {
                idStr = comp.getParent().getName();
            }
        }

        if (idStr != null) {
            try {
                int idProducto = Integer.parseInt(idStr);
                Producto productoSeleccionado = buscarProductoPorId(idProducto);

                if (productoSeleccionado != null) {
                    VistaProductoDetalles panelDetalle = new VistaProductoDetalles();
                    new ControladorDetallesProducto(panelDetalle, productoSeleccionado, this);

                    vista.getContenidoCentralDinamico().removeAll();
                    vista.getContenidoCentralDinamico().add(panelDetalle);
                    vista.getContenidoCentralDinamico().revalidate();
                    vista.getContenidoCentralDinamico().repaint();
                }
            } catch (NumberFormatException ex) {
                // Ignorar componentes cuyo Name no sea numérico
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();

        if (origen == vista.btnReservarCita) {
            VistaReservarCitas panelReserva = new VistaReservarCitas();
            new ControladorReservarCita(panelReserva, vista, this.idUsuarioLogueado);

            vista.getContenidoCentralDinamico().removeAll();
            vista.getContenidoCentralDinamico().add(panelReserva);
            vista.getContenidoCentralDinamico().revalidate();
            vista.getContenidoCentralDinamico().repaint();
        }

        if (vista.sidebar != null && origen == vista.sidebar.bCasa) {
            restaurarTiendaYCatalogo();
        }

        if (origen == vista.btnHistorial) {
            JOptionPane.showMessageDialog(vista,
                    "Cargando el historial de compras y citas atendidas del usuario...",
                    "Historial",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        if (vista.sidebar != null && origen == vista.sidebar.misCitas) {
            JOptionPane.showMessageDialog(vista,
                    "Cargando citas agendadas (vigentes y pasadas)...",
                    "Mis Citas",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        if (origen == vista.btnCerrarSesion) {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Estás seguro de que deseas cerrar tu sesión en Nexus GO?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                vista.dispose();
                VistaInicioSesion login = new VistaInicioSesion();
                new ControladorInicioSesion(login);
                login.setVisible(true);
            }
        }
    }

    public void restaurarTiendaYCatalogo() {
        this.vista.restaurarComponentesTienda();
        cargarCatalogo();
    }
}

