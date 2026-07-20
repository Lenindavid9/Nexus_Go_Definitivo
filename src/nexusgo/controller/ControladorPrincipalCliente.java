/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao; // Tu manejador de base de datos
import nexusgo.view.VistaProductoDetalles;
import nexusgo.view.VistaInicioSesion;
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
    private List<Producto> listaPromociones;
    private int idUsuarioLogueado;

    /*
     * En el constructor recibo la vista principal del cliente y el identificador del usuario
     * autenticado. Asigno los escuchadores a los botones correspondientes de la barra lateral
     * (Casa, Historial, Mis Citas) y ejecuto la carga inicial de datos.
     */
    public ControladorPrincipalCliente(VistaPrincipalCliente vista, int idUsuarioLogueado) {
        this.vista = vista;
        this.idUsuarioLogueado = idUsuarioLogueado;
        this.productoDAO = new ProductoDao();

        // Enlace de eventos de la barra lateral de la vista
        this.vista.sidebar.bCasa.addActionListener(this);       // 1. Inicio / Catálogo
        this.vista.btnHistorial.addActionListener(this);  
        this.vista.sidebar.misCitas.addActionListener(this);    // 3. Mis Citas (vigentes y pasadas)
        
        // Enlace de eventos para botones de la cabecera / cuerpo
        this.vista.btnReservarCita.addActionListener(this);
        this.vista.btnCerrarSesion.addActionListener(this);

        // Cargo el catálogo inicial
        cargarCatalogo();
    }

    /*
     * Constructor secundario de respaldo por compatibilidad.
     */
    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this(vista, 1);
    }

    /*
     * Consulto los productos al DAO (Modelo) y se los paso a la Vista descomponiéndolos
     * en tipos primitivos para no romper la separación de capas MVC.
     */
    public void cargarCatalogo() {
        try {
            this.listaProductos = productoDAO.listar();
            this.listaPromociones = productoDAO.listarPromociones();

            this.vista.limpiarGridProductos();
            this.vista.limpiarGridPromociones();

            // Cargo los productos regulares en la vista
            if (this.listaProductos != null) {
                for (Producto p : this.listaProductos) {
                    this.vista.agregarTarjetaProducto(
                        p.getIdProducto(),
                        p.getNombreProducto(),
                        p.getPrecioCompra(),
                        p.getUrlImagen(),
                        this
                    );
                }
            }

            // Cargo las promociones en la vista
            if (this.listaPromociones != null) {
                for (Producto promo : this.listaPromociones) {
                    this.vista.agregarTarjetaPromocion(
                        promo.getIdProducto(),
                        promo.getNombreProducto(),
                        promo.getPrecioCompra(),
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

    /*
     * Método auxiliar para obtener el objeto Producto correspondiente a un ID clickeado.
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

    /*
     * Capturo los clics de las tarjetas para identificar qué producto seleccionó el usuario.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Object origen = e.getSource();
        String idStr = null;

        if (origen instanceof JPanel) {
            idStr = ((JPanel) origen).getName();
        } else if (origen instanceof JLabel) {
            idStr = ((JLabel) origen).getName();
        }

        if (idStr != null) {
            try {
                int idProducto = Integer.parseInt(idStr);
                Producto productoSeleccionado = buscarProductoPorId(idProducto);

                if (productoSeleccionado != null) {
                    // Queda listo a la espera de la pantalla detallada del producto que me indiques
                    System.out.println("Producto seleccionado: " + productoSeleccionado.getNombreProducto() + " (ID: " + idProducto + ")");
                }
            } catch (NumberFormatException ex) {
                // Se ignora si el nombre del componente no es numérico
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    /*
     * Gestor de eventos para los botones de navegación y acciones.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Botón central "Reservar Cita"
        if (e.getSource() == vista.btnReservarCita) {
            VistaReservarCitas panelReserva = new VistaReservarCitas();
            new ControladorReservarCita(panelReserva, vista, this.idUsuarioLogueado);

            vista.getContenidoCentralDinamico().removeAll();
            vista.getContenidoCentralDinamico().add(panelReserva);
            vista.getContenidoCentralDinamico().revalidate();
            vista.getContenidoCentralDinamico().repaint();
        }

        // Ícono 1: "Casa" (Volver al catálogo principal)
        if (e.getSource() == vista.sidebar.bCasa) {
            restaurarTiendaYCatalogo();
        }

        // Ícono 2: "Historial de Compras"
        if (e.getSource() == vista.btnHistorial) {
            JOptionPane.showMessageDialog(vista,
                    "Cargando el historial de compras del usuario...",
                    "Historial de Compras",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Ícono 3: "Mis Citas" (Vigentes, pasadas y por haber)
        if (e.getSource() == vista.sidebar.misCitas) {
            JOptionPane.showMessageDialog(vista,
                    "Cargando citas agendadas (vigentes y pasadas)...",
                    "Mis Citas",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Botón superior "Cerrar Sesión"
        if (e.getSource() == vista.btnCerrarSesion) {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Estás seguro de que deseas cerrar sesión?",
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

    /*
     * Restauro la vista central del catálogo y refresco la consulta de productos.
     */
    public void restaurarTiendaYCatalogo() {
        this.vista.restaurarComponentesTienda();
        cargarCatalogo();
    }
}

