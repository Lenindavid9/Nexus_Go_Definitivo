/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.GestorPermisos;
import nexusgo.model.PermisosInventario;
import nexusgo.model.Usuario;

/**
 *
 * @author USUARIO
 */
public class VistaOperarioInventario extends JPanel {

    // Componentes de acceso público para la gestión de eventos en el controlador
    public JButton btnAgregarProducto;
    public JButton btnAgregarHerramienta;
    public JButton cerrarSesion;
    public JTable tablaProductos;
    public JTable tablaHerramientas;
    public JTabbedPane tabs;
    
    // Campos de texto para la funcionalidad de búsqueda en tiempo real
    public JTextField txtBuscarProducto;
    public JTextField txtBuscarHerramienta;

    // Paneles contenedores principales
    private JPanel panelProductos;
    private JPanel panelHerramientas;
    
    // Configuración de paleta de colores de la interfaz
    private final Color COLOR_DORADO = new Color(184, 134, 11);

    /**
     * Constructor principal. Inicializa los componentes, distribuciones y vistas del panel.
     */
    public VistaOperarioInventario() {
        // Establece la distribución del contenedor principal
        setLayout(new BorderLayout());
        setOpaque(false);

        // Inicialización y configuración del contenedor de pestañas
        tabs = new JTabbedPane();
        tabs.setForeground(Color.BLACK);
        tabs.setOpaque(false);

        // Inicialización de los paneles que representan cada pestaña
        panelProductos = new JPanel(new BorderLayout());
        panelHerramientas = new JPanel(new BorderLayout());

        // Asignación de pestañas al JTabbedPane
        tabs.addTab("Productos", panelProductos);
        tabs.addTab("Herramientas", panelHerramientas);
        
        // Agrega el contenedor de pestañas al centro del panel principal
        add(tabs, BorderLayout.CENTER);

        // ------------------------------------------------------------------
        // SECCIÓN DE PRODUCTOS
        // ------------------------------------------------------------------
        
        // Panel superior que agrupa la acción de creación y el cuadro de búsqueda
        JPanel topProductos = new JPanel(new BorderLayout(10, 10));
        
        // Configuración del botón para agregar productos
        btnAgregarProducto = new JButton("+ Agregar Producto");
        btnAgregarProducto.setBackground(COLOR_DORADO);
        btnAgregarProducto.setForeground(Color.WHITE);

        // Subpanel para la entrada de texto de búsqueda de productos
        JPanel panelBusquedaProducto = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblBuscarProd = new JLabel("Buscar:");
        txtBuscarProducto = new JTextField(15);
        
        panelBusquedaProducto.add(lblBuscarProd);
        panelBusquedaProducto.add(txtBuscarProducto);

        // Distribución de controles en el panel superior de productos
        topProductos.add(btnAgregarProducto, BorderLayout.WEST);
        topProductos.add(panelBusquedaProducto, BorderLayout.EAST);

        // Creación del modelo y estructura de la tabla de productos
        String[] columnasProductos = {"Numero de referencia", "Nombre", "Precio", "Cantidad", "Proveedor"};
        DefaultTableModel modeloProductos = new DefaultTableModel(columnasProductos, 0);
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(30);
        tablaProductos.getTableHeader().setReorderingAllowed(false);

        // Configuración del panel de desplazamiento para la tabla de productos
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollProductos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollProductos.setColumnHeaderView(tablaProductos.getTableHeader());

        // Integración de los componentes al panel de productos
        panelProductos.add(topProductos, BorderLayout.NORTH);
        panelProductos.add(scrollProductos, BorderLayout.CENTER);

        // ------------------------------------------------------------------
        // SECCIÓN DE HERRAMIENTAS
        // ------------------------------------------------------------------
        
        // Panel superior que agrupa la acción de creación y el cuadro de búsqueda
        JPanel topHerramientas = new JPanel(new BorderLayout(10, 10));

        // Configuración del botón para agregar herramientas
        btnAgregarHerramienta = new JButton("+ Agregar Herramienta");
        btnAgregarHerramienta.setBackground(COLOR_DORADO);
        btnAgregarHerramienta.setForeground(Color.WHITE);

        // Subpanel para la entrada de texto de búsqueda de herramientas
        JPanel panelBusquedaHerramienta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblBuscarHerr = new JLabel("Buscar:");
        txtBuscarHerramienta = new JTextField(15);
        
        panelBusquedaHerramienta.add(lblBuscarHerr);
        panelBusquedaHerramienta.add(txtBuscarHerramienta);

        // Distribución de controles en el panel superior de herramientas
        topHerramientas.add(btnAgregarHerramienta, BorderLayout.WEST);
        topHerramientas.add(panelBusquedaHerramienta, BorderLayout.EAST);

        // Creación del modelo y estructura de la tabla de herramientas
        String[] columnasHerramientas = {"Código", "Nombre", "Estado", "Cantidad"};
        DefaultTableModel modeloHerramientas = new DefaultTableModel(columnasHerramientas, 0);
        tablaHerramientas = new JTable(modeloHerramientas);
        tablaHerramientas.setRowHeight(30);
        tablaHerramientas.getTableHeader().setReorderingAllowed(false);

        // Configuración del panel de desplazamiento para la tabla de herramientas
        JScrollPane scrollHerramientas = new JScrollPane(tablaHerramientas);
        scrollHerramientas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollHerramientas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollHerramientas.setColumnHeaderView(tablaHerramientas.getTableHeader());

        // Integración de los componentes al panel de herramientas
        panelHerramientas.add(topHerramientas, BorderLayout.NORTH);
        panelHerramientas.add(scrollHerramientas, BorderLayout.CENTER);
    }
}
