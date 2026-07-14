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
    private DefaultTableModel modelo; // Lo volvemos atributo para usarlo en el método público


    public VistaHistorialCita() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 1. TÍTULO PRINCIPAL
        lblTitulo = new JLabel("Historial de servicios vigentes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(62, 58, 46));
        this.add(lblTitulo, BorderLayout.NORTH);

        // 2. CONTENEDOR GRIS CLARO
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

        // 3. TABLA DE DATOS (Vacía por defecto)
        String[] columnas = {"Nombre del servicio.", "Horario fecha y hora", "Valor"};

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modelo);
        tablaCitas.setRowHeight(40);
        tablaCitas.setBackground(new Color(245, 245, 245));
        tablaCitas.setFont(new Font("SansSerif", Font.PLAIN, 14));

        tablaCitas.setGridColor(new Color(223, 205, 141));
        tablaCitas.setShowHorizontalLines(true);
        tablaCitas.setShowVerticalLines(false);

        tablaCitas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaCitas.getTableHeader().setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));

        contenedorBlanco.add(scrollPane, BorderLayout.CENTER);
        this.add(contenedorBlanco, BorderLayout.CENTER);
    }

    /**
     * MÉTODO CLAVE: Permite al controlador limpiar la tabla y rellenarla con
     * los datos frescos que traiga desde el DAO de la Base de Datos.
     */
    public void limpiarYAgregarFila(Object[] fila) {
        modelo.addRow(fila);
    }

    public void limpiarTabla() {
        modelo.setRowCount(0);
    }
}
