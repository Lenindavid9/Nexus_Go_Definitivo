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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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
    private final VistaAgregarProducto panelFormulario;
    private final VistaAgregarHerramienta panelFormularioHerramienta;
    private final VistaRegistrarSalida panelSalidaInsumo;
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private int idSeleccionado = -1;

    // Clasificadores de filas para habilitar el filtrado por expresiones regulares en tiempo real
    private TableRowSorter<DefaultTableModel> sorterProductos;
    private TableRowSorter<DefaultTableModel> sorterHerramientas;

    /**
     * Constructor del controlador. Inicializa componentes, carga registros desde base de datos
     * y registra los escuchadores de eventos.
     */
    public ControladorInventarioOperario(VistaOperarioInventario panelInventario, Usuario usuarioLogueado, JPanel contenedorCentral) {
        this.panelInventario = panelInventario;
        this.usuarioLogueado = usuarioLogueado;
        this.contenedorCentral = contenedorCentral;
        this.panelFormulario = new VistaAgregarProducto();
        this.panelFormularioHerramienta = new VistaAgregarHerramienta();
        this.panelSalidaInsumo = new VistaRegistrarSalida();

        // Consulta inicial y renderizado de datos en las tablas de la interfaz
        listarProductosEnTabla();
        listarHerramientasEnTabla();

        // Inicialización de escuchadores de eventos sobre botones, campos de texto y tablas
        inicializarListeners();
        
        // Aplicación de reglas de acceso de acuerdo al rol del usuario
        aplicarPermisosPorRol();
    }

    /**
     * Habilita o deshabilita componentes de la interfaz según los permisos asignados.
     */
    private void aplicarPermisosPorRol() {
        if (panelInventario.btnAgregarProducto != null) {
            panelInventario.btnAgregarProducto.setVisible(true);
        }

        if (panelInventario.btnAgregarHerramienta != null) {
            panelInventario.btnAgregarHerramienta.setVisible(true);
        }
    }

    /**
     * Registra los escuchadores de eventos (ActionListener, MouseListener, KeyListener) 
     * en las vistas y componentes correspondientes.
     */
    private void inicializarListeners() {
        // Asignación de ActionListener a los botones principales de la vista
        if (panelInventario.btnAgregarProducto != null) {
            panelInventario.btnAgregarProducto.addActionListener(this);
        }
        if (panelInventario.btnAgregarHerramienta != null) {
            panelInventario.btnAgregarHerramienta.addActionListener(this);
        }
        if (panelInventario.cerrarSesion != null) {
            panelInventario.cerrarSesion.addActionListener(this);
        }

        // Asignación de KeyListener para la búsqueda filtrada de productos
        if (panelInventario.txtBuscarProducto != null) {
            panelInventario.txtBuscarProducto.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    filtrarProductos();
                }
            });
        }
        
       
        // Asignación de KeyListener para la búsqueda filtrada de herramientas
        if (panelInventario.txtBuscarHerramienta != null) {
            panelInventario.txtBuscarHerramienta.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    filtrarHerramientas();
                }
            });
        }

        // MouseListener para capturar selección de filas en la tabla de productos
        if (panelInventario.tablaProductos != null) {
            panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaProductos.getSelectedRow();
                    if (fila >= 0) {
                        lanzarMenuDecision("Producto", fila);
                    }
                }
            });
        }

        // MouseListener para capturar selección de filas en la tabla de herramientas
        if (panelInventario.tablaHerramientas != null) {
            panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    if (fila >= 0) {
                        lanzarMenuDecision("Herramienta", fila);
                    }
                }
            });
        }
    }

    /**
     * Aplica un RowFilter al TableRowSorter de la tabla de productos
     * según el texto ingresado en la caja de búsqueda.
     */
    private void filtrarProductos() {
        if (sorterProductos != null) {
            String texto = panelInventario.txtBuscarProducto.getText().trim();
            if (texto.isEmpty()) {
                sorterProductos.setRowFilter(null);
            } else {
                // Filtra aplicando expresión regular insensible a mayúsculas/minúsculas (?i)
                sorterProductos.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        }
    }

    /**
     * Aplica un RowFilter al TableRowSorter de la tabla de herramientas
     * según el texto ingresado en la caja de búsqueda.
     */
    private void filtrarHerramientas() {
        if (sorterHerramientas != null) {
            String texto = panelInventario.txtBuscarHerramienta.getText().trim();
            if (texto.isEmpty()) {
                sorterHerramientas.setRowFilter(null);
            } else {
                // Filtra aplicando expresión regular insensible a mayúsculas/minúsculas (?i)
                sorterHerramientas.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        }
    }

    /**
     * Despliega una ventana emergente de opciones al hacer clic sobre un registro de la tabla.
     */
    private void lanzarMenuDecision(String tipo, int filaVisual) {
        // Conversión del índice de la fila en la vista al índice real del modelo cuando hay filtros activos
        int fila;
        if (tipo.equals("Producto")) {
            fila = panelInventario.tablaProductos.convertRowIndexToModel(filaVisual);
        } else {
            fila = panelInventario.tablaHerramientas.convertRowIndexToModel(filaVisual);
        }

        String[] opciones = {"Registrar Salida", "Editar", "Eliminar"};

        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción desea realizar con el registro seleccionado?",
                "NEXUS - Panel de Control", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) {
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getModel().getValueAt(fila, 0);
                ControladorRegistrarSalida controlRegistSalida = new ControladorRegistrarSalida(panelSalidaInsumo, contenedorCentral, panelInventario, idSeleccionado, this::listarProductosEnTabla);
                cambiarPanelCentral(this.panelSalidaInsumo);
            } else {
                JOptionPane.showMessageDialog(panelInventario, "Las herramientas cambian por estado físico, no numérico.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (seleccion == 1) {
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getModel().getValueAt(fila, 0);
                ControladorAgregarProducto controlAggProduc = new ControladorAgregarProducto(panelFormulario, contenedorCentral, panelInventario, idSeleccionado, this::listarProductosEnTabla);
                cambiarPanelCentral(this.panelFormulario);
            } else {
                idSeleccionado = (int) panelInventario.tablaHerramientas.getModel().getValueAt(fila, 0);
                String nombreHerramienta = panelInventario.tablaHerramientas.getModel().getValueAt(fila, 1).toString();
                ControladorAgregarHerramienta controlAggHerra = new ControladorAgregarHerramienta(panelFormularioHerramienta, contenedorCentral, panelInventario, idSeleccionado, nombreHerramienta, this::listarHerramientasEnTabla);
                cambiarPanelCentral(this.panelFormularioHerramienta);
            }

        } else if (seleccion == 2) {
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getModel().getValueAt(fila, 0);
                eliminarProducto(idSeleccionado);
            } else {
                idSeleccionado = (int) panelInventario.tablaHerramientas.getModel().getValueAt(fila, 0);
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

    /**
     * Remueve los componentes del contenedor secundario e inserta el nuevo panel solicitado.
     */
    private void cambiarPanelCentral(JPanel panelNuevo) {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(panelNuevo, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();
        } else {
            System.out.println("Error: 'contenedorCentral' es nulo en el controlador.");
        }
    }

    /**
     * Elimina una entidad de producto por ID invocando al DAO correspondiente y refresca la tabla.
     */
    private void eliminarProducto(int id) {
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente este insumo?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            if (productoDao.eliminar(id) > 0) {
                listarProductosEnTabla();
            }
        }
    }

    /**
     * Elimina una entidad de herramienta por ID invocando al DAO correspondiente y refresca la tabla.
     */
    private void eliminarHerramienta(int id) {
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente esta herramienta?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            if (herramientaDao.eliminar(id) > 0) {
                listarHerramientasEnTabla();
            }
        }
    }

    /**
     * Consulta la base de datos a través de ProductoDao, construye el modelo no editable,
     * asigna el TableRowSorter y puebla la JTable de productos.
     */
    public void listarProductosEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Compra", "Stock Actual", "Stock Mínimo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Asignación del modelo a la vista
            panelInventario.tablaProductos.setModel(modeloBlindado);

            // Inicialización e integración del ordenador/filtrador sobre el modelo de la tabla
            sorterProductos = new TableRowSorter<>(modeloBlindado);
            panelInventario.tablaProductos.setRowSorter(sorterProductos);

            // Consulta de datos al DAO
            List<Producto> lista = productoDao.listar();

            if (lista != null) {
                for (Producto p : lista) {
                    modeloBlindado.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getPrecioCompra(), p.getStockActual(), p.getStockMinimo()});
                }
            }
            // Re-aplica el filtro si ya existía texto escrito previamente en el campo de texto
            filtrarProductos();

        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage() + " ControladorInventarioOperario");
        }
    }

    /**
     * Consulta la base de datos a través de HerramientaDao, construye el modelo no editable,
     * asigna el TableRowSorter y puebla la JTable de herramientas.
     */
    public void listarHerramientasEnTabla() {
        try {
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Estado", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Asignación del modelo a la vista
            panelInventario.tablaHerramientas.setModel(modeloBlindado);

            // Inicialización e integración del ordenador/filtrador sobre el modelo de la tabla
            sorterHerramientas = new TableRowSorter<>(modeloBlindado);
            panelInventario.tablaHerramientas.setRowSorter(sorterHerramientas);

            // Consulta de datos al DAO
            List<Herramientas> lista = herramientaDao.listar();

            if (lista != null) {
                for (Herramientas h : lista) {
                    modeloBlindado.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Herramienta"});
                }
            }
            // Re-aplica el filtro si ya existía texto escrito previamente en el campo de texto
            filtrarHerramientas();

        } catch (Exception e) {
            System.out.println("Error al listar herramientas: " + e.getMessage());
        }
    }

    /**
     * Cierra la ventana activa actual mediante reflexión y despliega la vista de inicio de sesión.
     */
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
            ControladorInicioSesion ininSesion = new ControladorInicioSesion(loginVista);
            loginVista.setLocationRelativeTo(null);
            loginVista.setVisible(true);
        }
    }
}
