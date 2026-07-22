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
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.PromocionCombo;
import nexusgo.model.PromocionComboDao;
import nexusgo.model.ServicioDao;
import nexusgo.model.Servicios;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaHistorialCita;
import nexusgo.view.VistaHstorialPagos;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaProductoDetalles;
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
    private final ServicioDao servicioDAO; 
    private final PromocionComboDao promocionComboDAO;

    // Listas independientes en memoria
    private List<Producto> listaProductos;
    private List<Producto> listaPromociones;
    private List<PromocionCombo> listaCombos;
    private List<Servicios> listaServicios; 

    private final int idUsuarioLogueado;

    private ControladorReservarCita controladorReservarCita;
    private ControladorHistorialCita controladorHistorialCita;
    private ControladorHistorialPagos controladorHistorialPagos;
    private ControladorDetallesProducto controladorDetallesProducto;

    public ControladorPrincipalCliente(VistaPrincipalCliente vista, int idUsuarioLogueado) {
        this.vista = vista;
        this.idUsuarioLogueado = idUsuarioLogueado;
        
        this.productoDAO = new ProductoDao();
        this.servicioDAO = new ServicioDao(); 
        this.promocionComboDAO = new PromocionComboDao();

        if (this.vista.btnHistorial != null) this.vista.btnHistorial.addActionListener(this);
        if (this.vista.btnReservarCita != null) this.vista.btnReservarCita.addActionListener(this);
        if (this.vista.btnCerrarSesion != null) this.vista.btnCerrarSesion.addActionListener(this);

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
            // 1. Cargar Productos
            this.listaProductos = productoDAO.listar();

            // 2. Cargar Promociones
            try {
                this.listaPromociones = productoDAO.listarPromociones();
            } catch (Exception ex) {
                this.listaPromociones = null;
            }

            // 3. Cargar Combos
            try {
                this.listaCombos = promocionComboDAO.listarCombosActivos();
            } catch (Exception ex) {
                this.listaCombos = null;
            }

            // 4. Cargar Servicios
            try {
                this.listaServicios = servicioDAO.listarServiciosActivos();
            } catch (Exception ex) {
                this.listaServicios = null;
            }

            this.vista.restaurarComponentesTienda();

            // Renderizado de Productos
            if (this.listaProductos != null) {
                for (Producto p : this.listaProductos) {
                    this.vista.agregarTarjetaProducto(p.getIdProducto(), p.getNombreProducto(), p.getPrecioVenta(), p.getUrlImagen(), this);
                }
            }

            // Renderizado de Promociones
            if (this.listaPromociones != null) {
                for (Producto promo : this.listaPromociones) {
                    this.vista.agregarTarjetaPromocion(promo.getIdProducto(), promo.getNombreProducto(), promo.getPrecioVenta(), promo.getUrlImagen(), this);
                }
            }

            // Renderizado de Combos
            if (this.listaCombos != null) {
                for (PromocionCombo combo : this.listaCombos) {
                    this.vista.agregarTarjetaCombo(combo.getIdPromocion(), combo.getNombreCombo(), combo.getPrecioCombo(), combo.getRutaImagen(), this);
                }
            }

            // Renderizado de Servicios
            if (this.listaServicios != null) {
                for (Servicios serv : this.listaServicios) {
                    this.vista.agregarTarjetaServicio(serv.getIdServicio(), serv.getNombreServicio(), serv.getPrecio(), "/nexusgo/img/default.jpg", this);
                }
            }

            if (this.vista.getContenidoCentralDinamico() != null) {
                this.vista.getContenidoCentralDinamico().revalidate();
                this.vista.getContenidoCentralDinamico().repaint();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Producto buscarYAdaptarElemento(String identificadorCompleto) {
        if (identificadorCompleto == null) return null;

        if (identificadorCompleto.startsWith("PROD_")) {
            int id = Integer.parseInt(identificadorCompleto.replace("PROD_", ""));
            if (listaProductos != null) {
                for (Producto prod : listaProductos) if (prod.getIdProducto() == id) return prod;
            }
        } else if (identificadorCompleto.startsWith("PROMO_")) {
            int id = Integer.parseInt(identificadorCompleto.replace("PROMO_", ""));
            if (listaPromociones != null) {
                for (Producto promo : listaPromociones) if (promo.getIdProducto() == id) return promo;
            }
        } else if (identificadorCompleto.startsWith("COMBO_")) {
            int id = Integer.parseInt(identificadorCompleto.replace("COMBO_", ""));
            if (listaCombos != null) {
                for (PromocionCombo combo : listaCombos) {
                    if (combo.getIdPromocion() == id) {
                        Producto p = new Producto();
                        p.setIdProducto(combo.getIdPromocion());
                        p.setNombreProducto(combo.getNombreCombo());
                        p.setPrecioVenta(combo.getPrecioCombo());
                        p.setDescripcion(combo.getDescripcion());
                        p.setUrlImagen(combo.getRutaImagen());
                        return p;
                    }
                }
            }
        } else if (identificadorCompleto.startsWith("SERV_")) {
            int id = Integer.parseInt(identificadorCompleto.replace("SERV_", ""));
            if (listaServicios != null) {
                for (Servicios serv : listaServicios) {
                    if (serv.getIdServicio() == id) {
                        Producto p = new Producto();
                        p.setIdProducto(serv.getIdServicio());
                        p.setNombreProducto(serv.getNombreServicio());
                        p.setPrecioVenta(serv.getPrecio());
                        p.setDescripcion(serv.getDescripcion());
                        p.setUrlImagen("/nexusgo/img/default.jpg");
                        return p;
                    }
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
                Producto elementoSeleccionado = buscarYAdaptarElemento(idStr);

                if (elementoSeleccionado != null) {
                    VistaProductoDetalles panelDetalle = new VistaProductoDetalles();
                    this.controladorDetallesProducto = new ControladorDetallesProducto(panelDetalle, elementoSeleccionado, this);

                    JPanel contenedor = vista.getContenidoCentralDinamico();
                    if (contenedor != null) {
                        contenedor.removeAll();
                        contenedor.add(panelDetalle);
                        contenedor.revalidate();
                        contenedor.repaint();
                        hacerScrollArriba(contenedor);
                    }
                }
            } catch (NumberFormatException ignored) {}
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
            abrirVistaReservarCitas();
        } else if (origen == vista.btnHistorial) {
            abrirVistaHistorialCitas();
        } else if (origen == vista.btnCerrarSesion) {
            confirmarCerrarSesion();
        }
    }

    private void confirmarCerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Estás seguro de que deseas cerrar tu sesión en Nexus GO?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            vista.dispose();
            VistaInicioSesion login = new VistaInicioSesion();
            new ControladorInicioSesion(login);
            login.setVisible(true);
        }
    }

    public void abrirVistaHistorialCitas() {
        VistaHistorialCita panelHistorial = new VistaHistorialCita();
        this.controladorHistorialCita = new ControladorHistorialCita(panelHistorial, vista, this.idUsuarioLogueado);

        JPanel contenedor = vista.getContenidoCentralDinamico();
        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.add(panelHistorial);
            contenedor.revalidate();
            contenedor.repaint();
            hacerScrollArriba(contenedor);
        }
    }

    public void abrirVistaHistorialPagos() {
        VistaHstorialPagos panelPagos = new VistaHstorialPagos();

        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = usuarioDao.obtenerPorId(this.idUsuarioLogueado);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setIdUsuario(this.idUsuarioLogueado);
        }

        this.controladorHistorialPagos = new ControladorHistorialPagos(panelPagos, usuario);

        JPanel contenedor = vista.getContenidoCentralDinamico();
        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.add(panelPagos);
            contenedor.revalidate();
            contenedor.repaint();
            hacerScrollArriba(contenedor);
        }
    }

    private void abrirVistaReservarCitas() {
        VistaReservarCitas panelReserva = new VistaReservarCitas();
        this.controladorReservarCita = new ControladorReservarCita(panelReserva, vista, this.idUsuarioLogueado);

        JPanel contenedor = vista.getContenidoCentralDinamico();
        if (contenedor != null) {
            contenedor.removeAll();
            contenedor.add(panelReserva);
            contenedor.revalidate();
            contenedor.repaint();
            hacerScrollArriba(contenedor);
        }
    }

    public void restaurarTiendaYCatalogo() {
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
