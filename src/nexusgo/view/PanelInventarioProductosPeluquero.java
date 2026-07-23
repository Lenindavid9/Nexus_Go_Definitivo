/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
public class PanelInventarioProductosPeluquero extends JPanel {
    private JTable tablaInventario;
    private JButton btnCerrarSesion;
    private JButton btnVolver;
    private JScrollPane scrollTabla;
    private JPanel tarjetaBlanca;

    private final Color COLOR_DORADO = new Color(184, 134, 11);
    public PanelInventarioProductosPeluquero() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- Barra superior con buscador y cerrar sesión ---
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        panelTop.setOpaque(false);


        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnCerrarSesion.setForeground(COLOR_DORADO);
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setPreferredSize(new Dimension(160, 45));
        panelTop.add(btnCerrarSesion, BorderLayout.EAST);
        
         

        add(panelTop, BorderLayout.NORTH);

        // --- Tarjeta blanca sólida ---
        tarjetaBlanca = new JPanel(new BorderLayout(10, 10));
        tarjetaBlanca.setOpaque(false);
        tarjetaBlanca.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(tarjetaBlanca, BorderLayout.CENTER);

        // --- Botón volver arriba a la derecha dentro de la tarjeta ---
        JPanel panelSuperiorTarjeta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperiorTarjeta.setOpaque(false);

        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        panelSuperiorTarjeta.add(btnVolver);

        tarjetaBlanca.add(panelSuperiorTarjeta, BorderLayout.NORTH);

        // --- Tabla inventario ---
        tablaInventario = new JTable();
        tablaInventario.setRowHeight(42);
        tablaInventario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaInventario.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 17));
        tablaInventario.setGridColor(COLOR_DORADO);
        tablaInventario.getTableHeader().setBackground(Color.WHITE);
        tablaInventario.getTableHeader().setReorderingAllowed(false);

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
    public JTable getTablaInventario() { return tablaInventario; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnVolver() { return btnVolver; }
}