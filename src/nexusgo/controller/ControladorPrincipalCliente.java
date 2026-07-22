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

        // 1. Enlace de botones de la cabecera superior
        if (this.vista.btnHistorial != null) {
            this.vista.btnHistorial.addActionListener(this);
        }
        if (this.vista.btnReservarCita != null) {
            this.vista.btnReservarCita.addActionListener(this);
        }
        if (this.vista.btnCerrarSesion != null) {
            this.vista.btnCerrarSesion.addActionListener(this);
        }

        // 2. Enlace de navegación de la barra lateral (Sidebar)
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

        // 3. Cargar datos del catálogo al iniciar
        cargarCatalogo();
    }

    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this(vista, 1);
    }

    /**
     * Consulta la Base de Datos mediante el DAO y puebla los grids de Productos y Promociones.
     */
    public void cargarCatalogo() {
        try {
            // Obtener registros de la BD
            this.listaProductos = productoDAO.listar();

            try {
                this.listaPromociones = productoDAO.listarPromociones();
            } catch (Exception exPromo) {
                System.err.println("Aviso: No se pudieron listar promociones: " + exPromo.getMessage());
                this.listaPromociones = null;
            }

            // Trazabilidad en consola
            System.out.println("--- Carga de Catálogo Nexus GO ---");
            System.out.println("Productos regulares encontrados: " + (listaProductos != null ? listaProductos.size() : 0));
            System.out.println("Promociones encontradas: " + (listaPromociones != null ? listaPromociones.size() : 0));

            // Reiniciar y limpiar la estructura visual
            this.vista.restaurarComponentesTienda();

            // Llenar Grid de Catálogo General (Genera tarjetas con Fotos)
            if (this.listaProductos != null && !this.listaProductos.isEmpty()) {
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

            // Llenar Grid de Promociones (Genera tarjetas con Fotos de Oferta)
            if (this.listaPromociones != null && !this.listaPromociones.isEmpty()) {
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

            // Refrescar contenedor principal
            if (this.vista.getContenidoCentralDinamico() != null) {
                this.vista.getContenidoCentralDinamico().revalidate();
                this.vista.getContenidoCentralDinamico().repaint();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    vista,
                    "Error al conectar con el catálogo de productos: " + ex.getMessage(),
                    "Error de Sistema",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Busca un producto por ID dentro de las listas locales en memoria.
     */
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

    // --- MANEJO DE EVENTOS DE MOUSE (CLICK EN TARJETAS DE PRODUCTOS) ---

    @Override
    public void mouseClicked(MouseEvent e) {
        Object origen = e.getSource();
        String idStr = null;

        if (origen instanceof JComponent) {
            JComponent comp = (JComponent) origen;
            idStr = comp.getName();

            // Si el componente hijo no tiene el ID, buscarlo en el padre (tarjeta)
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
                // Si el evento provino de un componente sin ID numérico
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // --- MANEJO DE BOTONES (ACTION LISTENERS) ---

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

    // --- MÉTODOS DE NAVEGACIÓN Y CARGA DE VISTAS ---

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
