/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class VistaHstorialPagos extends JPanel{
    
    public JTable tablaPagos;
    public DefaultTableModel modelo;

    public VistaHstorialPagos() {
        setLayout(new BorderLayout());

        // Columnas exactas de tu diseño
        String[] columnas = {"ID", "Fecha", "Servicio / Producto", "Monto Total", "Acción"};
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evitar que el usuario edite la tabla directamente
            }
        };

        tablaPagos = new JTable(modelo);
        tablaPagos.setRowHeight(40);
        
        // Ocultar columna ID si no deseas mostrarla visualmente pero sí usarla internamente
        tablaPagos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaPagos.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scroll = new JScrollPane(tablaPagos);
        add(scroll, BorderLayout.CENTER);
    }
    
}
