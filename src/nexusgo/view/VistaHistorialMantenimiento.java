/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USUARIO
 */
public class VistaHistorialMantenimiento extends JPanel {

    private JTable tablaHistorial;
    private JScrollPane scrollTabla;
    private JPanel tarjetaBlancaRedondeada;

    public VistaHistorialMantenimiento() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- Tarjeta blanca redondeada (Diseño Figma) ---
        tarjetaBlancaRedondeada = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tarjetaBlancaRedondeada.setOpaque(false);
        tarjetaBlancaRedondeada.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Tabla del Historial de Mantenimientos ---
        tablaHistorial = new JTable();
        tablaHistorial.setRowHeight(48);
        tablaHistorial.setShowGrid(false);
        tablaHistorial.setIntercellSpacing(new Dimension(0, 0));
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaHistorial.setBackground(Color.WHITE);

        // Header
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaHistorial.getTableHeader().setBackground(Color.WHITE);
        tablaHistorial.getTableHeader().setForeground(new Color(60, 60, 60));
        tablaHistorial.getTableHeader().setOpaque(true);

        // ScrollPane
        scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(null);
        scrollTabla.setOpaque(false);

        tarjetaBlancaRedondeada.add(scrollTabla, BorderLayout.CENTER);
        add(tarjetaBlancaRedondeada, BorderLayout.CENTER);

        // Cargar modelo inicial por defecto
        inicializarModeloTabla();
    }

    private void inicializarModeloTabla() {
        // Encabezados específicos para el historial de eventos de mantenimiento
        String[] columnas = {"ID Mantenimiento", "Herramienta", "Descripción / Tipo", "Fecha / Hora"};
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setDatosHistorial(modelo);
    }

    /**
     * Inyecta el modelo de datos generado desde MantenimientoDao / Controlador
     */
    public void setDatosHistorial(DefaultTableModel modeloBD) {
        tablaHistorial.setModel(modeloBD);

        DefaultTableCellRenderer renderizadorEstiloFigma = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setBackground(Color.WHITE);
                cell.setForeground(new Color(50, 50, 50));

                // Borde inferior decorativo estilo maqueta
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(245, 215, 120)));

                if (isSelected) {
                    cell.setBackground(new Color(250, 245, 230));
                }
                return cell;
            }
        };

        for (int i = 0; i < tablaHistorial.getColumnCount(); i++) {
            tablaHistorial.getColumnModel().getColumn(i).setCellRenderer(renderizadorEstiloFigma);
        }

        revalidate();
        repaint();
    }

    public JTable getTablaHistorial() {
        return tablaHistorial;
    }
}
