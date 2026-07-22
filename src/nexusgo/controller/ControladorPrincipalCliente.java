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
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaHistorialCita;
import nexusgo.view.VistaHstorialPagos;
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

        // Enlace de botones de la barra superior
        if (this.vista.btnHistorial != null) {
            this.vista.btnHistorial.addActionListener(this); // Mis Citas / Historial
        }
        if (this.vista.btnReservarCita != null) {
            this.vista.btnReservarCita.addActionListener(this); // Exclusivo para Reservar Cita
        }
        if (this.vista.btnCerrarSesion != null) {
            this.vista.btnCerrarSesion.addActionListener(this);
        }

        // Enlace de botones del Sidebar (Únicamente: Casa, Mis Citas y Pagos)
        if (this.vista.sidebar != null) {
            if (this.vista.sidebar.bCasa != null) {
                this.vista.sidebar.bCasa.addActionListener(e -> restaurarTiendaYCatalogo());
            }
            if (this.vista.sidebar.misCitas != null) {
                this.vista.sidebar.misCitas.addActionListener(e -> abrirVistaHistorialCitas());
            }
            if (this.vista.sidebar.bInventario != null) {
                this.vista.sidebar.bInventario.addActionListener(e -> abrirVistaHistorialPagos());
            }
        }

        cargarCatalogo();
    }

    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this(vista, 1);
    }

    public void cargarCatalogo() {
        try {
            this.listaProductos = productoDAO.listar();

            try {
                this.listaPromociones = productoDAO.listarPromociones();
            } catch (Exception exPromo) {
                System.err.println("Aviso: No se pudieron listar promociones: " + exPromo.getMessage());
                this.listaPromociones = null;
            }

            this.vista.limpiarGridProductos();
            this.vista.limpiarGridPromociones();

            if (this.listaProductos != null) {
                for (Producto p : this.listaProductos) {
                    this.vista.agregarTarjetaProducto(
                            p.getIdProducto(),
                            p.getNombreProducto(),
                            p.getPrecioVenta(),
                            p.getUrlImagen(),
                            this
                    );
                }
            }

            if (this.listaPromociones != null) {
                for (Producto promo : this.listaPromociones) {
                    this.vista.agregarTarjetaPromocion(
                            promo.getIdProducto(),
                            promo.getNombreProducto(),
                            promo.getPrecioVenta(),
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
                if (prod.getIdProducto() == id) {
                    return prod;
                }
            }
        }
        if (listaPromociones != null) {
            for (Producto promo : listaPromociones) {
                if (promo.getIdProducto() == id) {
                    return promo;
                }
            }
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object origen = e.getSource();
        String idStr = null;

        if (origen instanceof JComponent) {
            JComponent comp = (JComponent) origen;
            idStr = comp.getName();

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

                    JPanel contenedor = vista.getContenidoCentralDinamico();
                    if (contenedor != null) {
                        contenedor.removeAll();
                        contenedor.add(panelDetalle);
                        contenedor.revalidate();
                        contenedor.repaint();
                        hacerScrollArriba(contenedor);
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();

        // Botón exclusivo superior
        if (origen == vista.btnReservarCita) {
            abrirVistaReservarCitas();
        }

        if (origen == vista.btnHistorial) {
            abrirVistaHistorialCitas();
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

    /**
     * Carga el módulo de Historial de Citas.
     */
    public void abrirVistaHistorialCitas() {
        VistaHistorialCita panelHistorial = new VistaHistorialCita();
        new ControladorHistorialCita(panelHistorial, vista, this.idUsuarioLogueado);

        JPanel contenedor = vista.getContenidoCentralDinamico();
        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.add(panelHistorial);
            contenedor.revalidate();
            contenedor.repaint();
            hacerScrollArriba(contenedor);
        }
    }

    /**
     * Carga el módulo de Historial de Pagos / Facturas.
     */
    public void abrirVistaHistorialPagos() {
        VistaHstorialPagos panelPagos = new VistaHstorialPagos();

        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = usuarioDao.obtenerPorId(this.idUsuarioLogueado);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setIdUsuario(this.idUsuarioLogueado);
        }

        new ControladorHistorialPagos(panelPagos, usuario);

        JPanel contenedor = vista.getContenidoCentralDinamico();
        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.add(panelPagos);
            contenedor.revalidate();
            contenedor.repaint();
            hacerScrollArriba(contenedor);
        }
    }

    /**
     * Carga el módulo para reservar nuevas citas (Accedido únicamente desde
     * btnReservarCita).
     */
    private void abrirVistaReservarCitas() {
        VistaReservarCitas panelReserva = new VistaReservarCitas();
        new ControladorReservarCita(panelReserva, vista, this.idUsuarioLogueado);

        JPanel contenedor = vista.getContenidoCentralDinamico();
        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.add(panelReserva);
            contenedor.revalidate();
            contenedor.repaint();
            hacerScrollArriba(contenedor);
        }
    }

    /**
     * Regresa a la vista principal / catálogo (Accedido desde el botón Casa).
     */
    public void restaurarTiendaYCatalogo() {
        this.vista.restaurarComponentesTienda();
        cargarCatalogo();
    }

    private void hacerScrollArriba(JComponent componente) {
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, componente);
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }
}
