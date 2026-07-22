/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
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

    public VistaOperarioInventario() {
        // Al ser un JPanel, usamos BorderLayout para que ocupe todo el espacio asignado
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR (Buscador y Cerrar Sesión) ---
        

        // --- PESTAÑAS (TABS) ---
        tabs = new JTabbedPane();
        panelProductos = new JPanel(new BorderLayout());
        panelHerramientas = new JPanel(new BorderLayout());

        tabs.addTab("Productos", panelProductos);
        tabs.addTab("Herramientas", panelHerramientas);
        add(tabs, BorderLayout.CENTER);

        // --- SECCIÓN: PRODUCTOS ---
        // --- SECCIÓN: PRODUCTOS ---
btnAgregarProducto = new JButton("+ Agregar Producto");

String columnasProductos[] = {"Numero de referencia", "Nombre", "Precio", "Cantidad", "Proveedor"};
DefaultTableModel modeloProductos = new DefaultTableModel(columnasProductos, 0);
tablaProductos = new JTable(modeloProductos);
tablaProductos.setRowHeight(30);

// Scroll con encabezado fijo
JScrollPane scrollProductos = new JScrollPane(tablaProductos);
scrollProductos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
scrollProductos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// 🔑 fijar el header
scrollProductos.setColumnHeaderView(tablaProductos.getTableHeader());

panelProductos.add(scrollProductos, BorderLayout.CENTER);


        // --- SECCIÓN: HERRAMIENTAS ---
        // --- SECCIÓN: HERRAMIENTAS ---
btnAgregarHerramienta = new JButton("+ Agregar Herramienta");

String columnasHerramientas[] = {"Código", "Nombre", "Estado", "Cantidad"};
DefaultTableModel modeloHerramientas = new DefaultTableModel(columnasHerramientas, 0);
tablaHerramientas = new JTable(modeloHerramientas);
tablaHerramientas.setRowHeight(30);

JScrollPane scrollHerramientas = new JScrollPane(tablaHerramientas);
scrollHerramientas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
scrollHerramientas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// 🔑 fijar el header
scrollHerramientas.setColumnHeaderView(tablaHerramientas.getTableHeader());

panelHerramientas.add(scrollHerramientas, BorderLayout.CENTER);

    }
    
}


