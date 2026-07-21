/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class VistaInventarioSupervisor extends JPanel {

    public JTextField buscador;
    public JTable tablaProductos;
    public JTable tablaHerramientas;
    public JTabbedPane tabs;

    private JPanel  panelProductos, panelHerramientas;
    public DefaultTableModel modeloProductos;
    public DefaultTableModel modeloHerramientas;

    public VistaInventarioSupervisor() {
        // Al ser un JPanel, usamos BorderLayout para que ocupe todo el espacio asignado
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- PANEL SUPERIOR (Buscador alineado a la derecha como en tu prototipo) ---
        

        // --- PESTAÑAS (TABS) ---
        tabs = new JTabbedPane();
        panelProductos = new JPanel(new BorderLayout());
        panelHerramientas = new JPanel(new BorderLayout());

        panelProductos.setOpaque(false);
        panelHerramientas.setOpaque(false);

        tabs.addTab("Productos", panelProductos);
        tabs.addTab("Herramientas", panelHerramientas);
        add(tabs, BorderLayout.CENTER);

        // --- SECCIÓN: PRODUCTOS (Sin botón agregar) ---
        String columnasProductos[] = {"Numero de referencia", "Nombre", "Precio", "Cantidad", "Proveedor"};
        modeloProductos = new DefaultTableModel(columnasProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Bloquear edición directa
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(30);
        tablaProductos.setOpaque(false);
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollProductos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        panelProductos.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // --- SECCIÓN: HERRAMIENTAS (Sin botón agregar) ---
        String columnasHerramientas[] = {"Código", "Nombre", "Estado", "Cantidad"};
        modeloHerramientas = new DefaultTableModel(columnasHerramientas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Bloquear edición directa
        };
        tablaHerramientas = new JTable(modeloHerramientas);
        tablaHerramientas.setRowHeight(30);
        tablaHerramientas.setOpaque(false);
        JScrollPane scrollHerramientas = new JScrollPane(tablaHerramientas);
        scrollHerramientas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollHerramientas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tablaHerramientas.getTableHeader().setReorderingAllowed(false);
        panelHerramientas.add(new JScrollPane(tablaHerramientas), BorderLayout.CENTER);
    }
    
    
}
