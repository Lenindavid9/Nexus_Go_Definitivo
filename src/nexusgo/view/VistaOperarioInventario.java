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

    public JButton btnAgregarProducto, cerrarSesion, btnAgregarHerramienta;
    public JTable tablaProductos;
    public JTable tablaHerramientas;
    public JTabbedPane tabs;

    private JPanel panelProductos, panelHerramientas;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaOperarioInventario() {
        // Al ser un JPanel, usamos BorderLayout para que ocupe todo el espacio asignado
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- PANEL SUPERIOR (Buscador y Cerrar Sesión) ---
        // --- PESTAÑAS (TABS) ---
        tabs = new JTabbedPane();
        tabs.setForeground(Color.BLACK);
        tabs.setOpaque(false);
        

        panelProductos = new JPanel(new BorderLayout());
        panelHerramientas = new JPanel(new BorderLayout());

        tabs.addTab("Productos", panelProductos);
        tabs.addTab("Herramientas", panelHerramientas);
        add(tabs, BorderLayout.CENTER);

        // --- SECCIÓN: PRODUCTOS ---
        // --- SECCIÓN: PRODUCTOS ---
        btnAgregarProducto = new JButton("+ Agregar Producto");
        btnAgregarProducto.setBackground(COLOR_DORADO); // Amarillo Nexus
        btnAgregarProducto.setForeground(Color.WHITE);

        String columnasProductos[] = {"Numero de referencia", "Nombre", "Precio", "Cantidad", "Proveedor"};
        DefaultTableModel modeloProductos = new DefaultTableModel(columnasProductos, 0);
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(30);
        tablaProductos.getTableHeader().setReorderingAllowed(false);


// Scroll con encabezado fijo
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollProductos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// 🔑 fijar el header
        scrollProductos.setColumnHeaderView(tablaProductos.getTableHeader());

        panelProductos.add(btnAgregarProducto, BorderLayout.NORTH);
        panelProductos.add(scrollProductos, BorderLayout.CENTER);


        // --- SECCIÓN: HERRAMIENTAS ---
        // --- SECCIÓN: HERRAMIENTAS ---
        btnAgregarHerramienta = new JButton("+ Agregar Herramienta");
        btnAgregarHerramienta.setBackground(COLOR_DORADO); // Amarillo Nexus
        btnAgregarHerramienta.setForeground(Color.WHITE);

        String columnasHerramientas[] = {"Código", "Nombre", "Estado", "Cantidad"};
        DefaultTableModel modeloHerramientas = new DefaultTableModel(columnasHerramientas, 0);
        tablaHerramientas = new JTable(modeloHerramientas);
        tablaHerramientas.setRowHeight(30);
        
tablaHerramientas.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollHerramientas = new JScrollPane(tablaHerramientas);
        scrollHerramientas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollHerramientas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// 🔑 fijar el header
        scrollHerramientas.setColumnHeaderView(tablaHerramientas.getTableHeader());

        panelHerramientas.add(scrollHerramientas, BorderLayout.CENTER);
        
        panelHerramientas.add(btnAgregarHerramienta, BorderLayout.NORTH);
        panelHerramientas.add(scrollHerramientas, BorderLayout.CENTER);

    }

}
