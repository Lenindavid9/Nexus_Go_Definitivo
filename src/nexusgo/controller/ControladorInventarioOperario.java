/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//
package nexusgo.controller;

import java.awt.BorderLayout;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaAgregarProducto;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.model.Usuario;
import nexusgo.view.VistaAgregarHerramienta;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaRegistrarSalida;

/**
 *
 * @author USUARIO
 */
public class ControladorInventarioOperario implements ActionListener {

    private final VistaOperarioInventario panelInventario;
    private final JPanel contenedorCentral;
    private final Usuario usuarioLogueado;

    // Paneles de formulario manejados centralmente
    private final VistaAgregarProducto panelFormulario;
    private final VistaAgregarHerramienta panelFormularioHerramienta;
    private final VistaRegistrarSalida panelSalidaInsumo;

    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private int idSeleccionado = -1;

    public ControladorInventarioOperario(VistaOperarioInventario panelInventario, Usuario usuarioLogueado, JPanel contenedorCentral) {
        this.panelInventario = panelInventario;
        this.usuarioLogueado = usuarioLogueado;
        this.contenedorCentral = contenedorCentral;

        // 1. Instanciación centralizada de sub-vistas
        this.panelFormulario = new VistaAgregarProducto();
        this.panelFormularioHerramienta = new VistaAgregarHerramienta();
        this.panelSalidaInsumo = new VistaRegistrarSalida();

        // 2. Vinculación de eventos y carga inicial
        inicializarListeners();
        listarProductosEnTabla();
        listarHerramientasEnTabla();
        aplicarPermisosPorRol();
    }

    private void aplicarPermisosPorRol() {
        if (panelInventario.btnAgregarProducto != null) panelInventario.btnAgregarProducto.setVisible(true);
        if (panelInventario.btnAgregarHerramienta != null) panelInventario.btnAgregarHerramienta.setVisible(true);
    }

    private void inicializarListeners() {
        // Listeners del panel principal de inventario
        if (panelInventario.btnAgregarProducto != null) panelInventario.btnAgregarProducto.addActionListener(this);
        if (panelInventario.btnAgregarHerramienta != null) panelInventario.btnAgregarHerramienta.addActionListener(this);
        if (panelInventario.cerrarSesion != null) panelInventario.cerrarSesion.addActionListener(this);

        // Listeners de tablas
        if (panelInventario.tablaProductos != null) {
            panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaProductos.getSelectedRow();
                    if (fila >= 0) lanzarMenuDecision("Producto", fila);
                }
            });
        }

        if (panelInventario.tablaHerramientas != null) {
            panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    if (fila >= 0) lanzarMenuDecision("Herramienta", fila);
                }
            });
        }
    }

    private void lanzarMenuDecision(String tipo, int fila) {
        String[] opciones = {"Registrar Salida", "Editar", "Eliminar"};
        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción desea realizar con el registro seleccionado?",
                "NEXUS - Panel de Control",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) { // Registrar Salida
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                
                // Delegado a ControladorRegistrarSalida
                ControladorRegistrarSalida controlRegistSalida = new ControladorRegistrarSalida(panelSalidaInsumo, contenedorCentral, panelInventario, idSeleccionado, this::listarProductosEnTabla);
                cambiarPanelCentral(this.panelSalidaInsumo);
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Las herramientas cambian por estado físico, no numérico.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (seleccion == 1) { // Editar
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                
                ControladorAgregarProducto controlAggProduc = new ControladorAgregarProducto(panelFormulario, contenedorCentral, panelInventario, idSeleccionado, this::listarProductosEnTabla);
                cambiarPanelCentral(this.panelFormulario);
            } else {
                idSeleccionado = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                String nombreHerramienta = panelInventario.tablaHerramientas.getValueAt(fila, 1).toString();
                
                // Delegado a ControladorAgregarHerramienta
                ControladorAgregarHerramienta controlAggHerra = new ControladorAgregarHerramienta(panelFormularioHerramienta, contenedorCentral, panelInventario, idSeleccionado, nombreHerramienta, this::listarHerramientasEnTabla);
                cambiarPanelCentral(this.panelFormularioHerramienta);
            }
            // Eliminar
        } else if (seleccion == 2) {
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                eliminarProducto(idSeleccionado);
            } else {
                idSeleccionado = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                eliminarHerramienta(idSeleccionado);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == panelInventario.cerrarSesion) {
            ejecutarCerrarSesion();
        } else if (src == panelInventario.btnAgregarProducto) {
            
            ControladorAgregarProducto controlAggProduc = new ControladorAgregarProducto(panelFormulario, contenedorCentral, panelInventario, this::listarProductosEnTabla);
            cambiarPanelCentral(this.panelFormulario);
        } else if (src == panelInventario.btnAgregarHerramienta) {
            
            ControladorAgregarHerramienta controlAggHerra = new ControladorAgregarHerramienta(panelFormularioHerramienta, contenedorCentral, panelInventario, this::listarHerramientasEnTabla);
            cambiarPanelCentral(this.panelFormularioHerramienta);
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(panelNuevo, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();
        } else {
            System.err.println("Error: 'contenedorCentral' es nulo en el controlador.");
        }
    }

    private void eliminarProducto(int id) {
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente este insumo?", 
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (productoDao.eliminar(id) > 0) {
                listarProductosEnTabla();
            }
        }
    }

    private void eliminarHerramienta(int id) {
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente esta herramienta?", 
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (herramientaDao.eliminar(id) > 0) {
                listarHerramientasEnTabla();
            }
        }
    }

    public void listarProductosEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Compra", "Stock Actual", "Stock Mínimo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaProductos.setModel(modeloBlindado);
            List<Producto> lista = productoDao.listar();
            if (lista != null) {
                for (Producto p : lista) {
                    modeloBlindado.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getPrecioCompra(), p.getStockActual(), p.getStockMinimo()});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void listarHerramientasEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Estado", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            panelInventario.tablaHerramientas.setModel(modeloBlindado);
            List<Herramientas> lista = herramientaDao.listar();
            if (lista != null) {
                for (Herramientas h : lista) {
                    modeloBlindado.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Herramienta"});
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar herramientas: " + e.getMessage());
        }
    }

    private void ejecutarCerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión en NEXUS?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            Object ventana = panelInventario.getTopLevelAncestor();
            if (ventana != null) {
                try {
                    ventana.getClass().getMethod("dispose").invoke(ventana);
                } catch (Exception ex) {
                    System.err.println("No se pudo cerrar la ventana: " + ex.getMessage());
                }
            }

            VistaInicioSesion loginVista = new VistaInicioSesion();
            new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }
}