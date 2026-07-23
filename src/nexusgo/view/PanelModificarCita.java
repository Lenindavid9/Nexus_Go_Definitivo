/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class PanelModificarCita extends JPanel {
    private JComboBox<String> comboServicios;
    private JTable tablaHorarios;
    private JButton btnCerrarSesion;
    private JButton btnGuardar;
    private JButton btnImagen;
    private JPanel tarjetaBlanca;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public PanelModificarCita() {
        // Layout principal
        setLayout(new BorderLayout());
        setOpaque(false);

        // Botón cerrar sesión arriba a la derecha
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        panelTop.setOpaque(false);
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnCerrarSesion.setForeground(COLOR_DORADO);
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setPreferredSize(new Dimension(160, 45)); 

        panelTop.add(btnCerrarSesion);
        add(panelTop, BorderLayout.NORTH);

        // Tarjeta blanca sólida
        tarjetaBlanca = new JPanel(new BorderLayout(15, 15));
        tarjetaBlanca.setOpaque(false);
        tarjetaBlanca.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(tarjetaBlanca, BorderLayout.CENTER);

        // Panel superior dentro de la tarjeta (título + combo)
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSuperior.setOpaque(false);

        JLabel lblTitulo = new JLabel("Modificar varias citas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        comboServicios = new JComboBox<>(new String[]{
            "Seleccione un servicio", "Alisado", "Corte", "Tinte", "Repolarización"
        });
        panelSuperior.add(comboServicios);

        tarjetaBlanca.add(panelSuperior, BorderLayout.NORTH);

        // Tabla de horarios
        String[] columnas = {"Hora", "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        Object[][] datos = {
            {"6:00 AM", "", "Corte", "", "", "Tinte", "Alisado", ""},
            {"7:00 AM", "", "", "", "Corte", "", "", ""},
            {"8:00 AM", "", "", "Alisado", "", "Tinte", "", ""},
            {"9:00 AM", "", "Tinte", "", "", "", "Corte", ""},
            {"10:00 AM", "", "Corte", "", "Tinte", "", "", ""},
            {"11:00 AM", "", "", "Alisado", "", "Alisado", "", ""}
        };

        DefaultTableModel modeloTabla = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHorarios = new JTable(modeloTabla);
        tablaHorarios.setRowHeight(30);
        tablaHorarios.setGridColor(COLOR_DORADO);
        tablaHorarios.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaHorarios.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 17));
        tablaHorarios.getTableHeader().setBackground(Color.WHITE);

        JScrollPane scrollTabla = new JScrollPane(tablaHorarios);
        tarjetaBlanca.add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelInferior.setOpaque(false);

        btnImagen = new JButton("Imagen producto/servicio");
        btnImagen.setPreferredSize(new Dimension(250, 45));
        btnImagen.setFont(new Font("SansSerif", Font.BOLD, 17));
        btnImagen.setBackground(COLOR_DORADO);
        btnImagen.setForeground(Color.white);

        panelInferior.add(btnImagen);
        

        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 17));
        btnGuardar.setForeground(COLOR_DORADO);
        btnGuardar.setBackground(Color.WHITE);
        btnGuardar.setPreferredSize(new Dimension(160, 45)); 
        panelInferior.add(btnGuardar);

        tarjetaBlanca.add(panelInferior, BorderLayout.SOUTH);
    }

    // Getters
    public JComboBox<String> getComboServicios() { return comboServicios; }
    public JTable getTablaHorarios() { return tablaHorarios; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnImagen() { return btnImagen; }
}