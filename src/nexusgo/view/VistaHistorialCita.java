/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;




/**
 *
 * @author HOME
 */
public class VistaHistorialCita extends JPanel {

   private JLabel lblTitulo;
    public JButton btnVolver;
    public JTable tablaCitas;
    private JScrollPane scrollPane;
    private JPanel contenedorBlanco;
    private DefaultTableModel modelo;

    public VistaHistorialCita() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 1. TÍTULO
        lblTitulo = new JLabel("Historial de servicios vigentes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(62, 58, 46));
        this.add(lblTitulo, BorderLayout.NORTH);

        // 2. CONTENEDOR PRINCIPAL
        contenedorBlanco = new JPanel(new BorderLayout());
        contenedorBlanco.setBackground(new Color(245, 245, 245));
        contenedorBlanco.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Botón Volver
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVolver.setOpaque(false);
        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        panelVolver.add(btnVolver);
        contenedorBlanco.add(panelVolver, BorderLayout.NORTH);

        // 3. TABLA
        String[] columnas = {"Nombre del servicio.", "Horario fecha y hora", "Valor"};

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modelo);
        tablaCitas.setRowHeight(40);
        tablaCitas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaCitas.setFillsViewportHeight(true);
        tablaCitas.setGridColor(new Color(223, 205, 141));
        tablaCitas.setShowHorizontalLines(true);
        tablaCitas.setShowVerticalLines(false);

        // Encabezado
        tablaCitas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaCitas.getTableHeader().setBackground(new Color(230, 230, 230));
        tablaCitas.getTableHeader().setReorderingAllowed(false);

        // Centrado de contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablaCitas.getColumnCount(); i++) {
            tablaCitas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        contenedorBlanco.add(scrollPane, BorderLayout.CENTER);
        this.add(contenedorBlanco, BorderLayout.CENTER);
    }

    public void agregarFila(Object[] fila) {
        if (modelo != null) {
            modelo.addRow(fila);
        }
    }

    public void limpiarTabla() {
        if (modelo != null) {
            modelo.setRowCount(0);
        }
    }
}
