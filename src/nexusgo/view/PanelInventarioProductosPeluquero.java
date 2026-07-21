/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HOME
 */
public class PanelInventarioProductosPeluquero extends JPanel {
    private JTextField txtBuscar;
    private JTable tablaInventario;
    private JButton btnCerrarSesion;
    private JButton btnVolver;
    private JScrollPane scrollTabla;
    private JPanel tarjetaBlanca;

    public PanelInventarioProductosPeluquero() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- Barra superior con buscador y cerrar sesión ---
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setOpaque(false);

        txtBuscar = new JTextField("  Q Search");
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setBorder(new EmptyBorder(0, 15, 0, 15));
        txtBuscar.setOpaque(false);
        panelTop.add(txtBuscar, BorderLayout.CENTER);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(255, 213, 79));
        btnCerrarSesion.setFocusPainted(false);
        panelTop.add(btnCerrarSesion, BorderLayout.EAST);

        add(panelTop, BorderLayout.NORTH);

        // --- Tarjeta blanca sólida ---
        tarjetaBlanca = new JPanel(new BorderLayout(10, 10));
        tarjetaBlanca.setBackground(Color.WHITE);
        tarjetaBlanca.setOpaque(true);
        tarjetaBlanca.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(tarjetaBlanca, BorderLayout.CENTER);

        // --- Botón volver arriba a la derecha dentro de la tarjeta ---
        JPanel panelSuperiorTarjeta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperiorTarjeta.setOpaque(false);

        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnVolver.setForeground(new Color(100, 100, 100));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        panelSuperiorTarjeta.add(btnVolver);

        tarjetaBlanca.add(panelSuperiorTarjeta, BorderLayout.NORTH);

        // --- Tabla inventario ---
        tablaInventario = new JTable();
        tablaInventario.setRowHeight(42);
        tablaInventario.setShowGrid(false);
        tablaInventario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaInventario.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tablaInventario.getTableHeader().setBackground(Color.WHITE);

        scrollTabla = new JScrollPane(tablaInventario);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(null);

        tarjetaBlanca.add(scrollTabla, BorderLayout.CENTER);
    }

    // --- Método para inyectar datos desde la BD ---
    public void setDatosInventario(DefaultTableModel modeloBD) {
        tablaInventario.setModel(modeloBD);

        DefaultTableCellRenderer renderizadorInventario = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setBackground(Color.WHITE);
                cell.setForeground(new Color(50, 50, 50));
                cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(245, 215, 120)));

                if (isSelected) {
                    cell.setBackground(new Color(250, 250, 250));
                }
                return cell;
            }
        };

        for (int i = 0; i < tablaInventario.getColumnCount(); i++) {
            tablaInventario.getColumnModel().getColumn(i).setCellRenderer(renderizadorInventario);
        }

        revalidate();
        repaint();
    }

    // --- Getters ---
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JTable getTablaInventario() { return tablaInventario; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnVolver() { return btnVolver; }
}
