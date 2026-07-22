/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import nexusgo.model.Servicios;

/**
 *
 * @author USUARIO
 */
public class VistaAgregarPromocionServicio extends JPanel {

    // Componentes interactivos del formulario
    public JComboBox<Servicios> comboServicios;
    public JTextField txtDescripcionPromocion;
    public JTextField txtFechaInicio;
    public JTextField txtFechaFin;
    public JTextField txtPrecio;

    // Botones de acción y navegación
    public JButton btnCargarImagen;
    public JLabel lblNombreImagen;
    public JButton btnGuardar;
    public JButton btnVolver;
    public JButton btnCerrarSesion;

    public VistaAgregarPromocionServicio() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // --- PANEL SUPERIOR: Botón Cerrar Sesión ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        btnCerrarSesion = new JButton("cerrar sesion");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(255, 204, 0));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelSuperior.add(btnCerrarSesion, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // --- TARJETA CENTRAL BLANCA ---
        JPanel panelTarjeta = new JPanel(new GridBagLayout());
        panelTarjeta.setBackground(Color.WHITE);
        panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // HEADER: Título y enlace Volver al inicio
        JLabel lblTitulo = new JLabel("Registrar promocion servicio");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.BLACK);
        panelTarjeta.add(lblTitulo, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        btnVolver = new JButton("< Volver al inicio");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVolver.setForeground(new Color(80, 80, 80));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTarjeta.add(btnVolver, gbc);

        // 1. CAMPO: Nombre del servicio (JComboBox con servicios de BD)
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelTarjeta.add(crearEtiqueta("Nombre del servicio"), gbc);

        gbc.gridy++;
        comboServicios = new JComboBox<>();
        comboServicios.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comboServicios.setBackground(Color.WHITE);
        comboServicios.setPreferredSize(new Dimension(380, 38));
        comboServicios.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelTarjeta.add(comboServicios, gbc);

        // 2. CAMPO: Descripción de la promoción
        gbc.gridy++;
        panelTarjeta.add(crearEtiqueta("descripcion de la promocion"), gbc);

        gbc.gridy++;
        txtDescripcionPromocion = crearCampoTexto("Ingrese el nombre del producto");
        panelTarjeta.add(txtDescripcionPromocion, gbc);

        // 3. CAMPOS: Fecha de inicio y Fecha de finalización
        gbc.gridy++;
        JPanel panelFechas = new JPanel(new GridBagLayout());
        panelFechas.setOpaque(false);

        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        gbcF.weightx = 0.5;
        gbcF.insets = new Insets(0, 0, 0, 15);

        gbcF.gridx = 0;
        gbcF.gridy = 0;
        panelFechas.add(crearEtiqueta("Fecha de inicio"), gbcF);

        gbcF.gridx = 1;
        panelFechas.add(crearEtiqueta("Fecha de finalizacion"), gbcF);

        gbcF.gridx = 0;
        gbcF.gridy = 1;
        txtFechaInicio = crearCampoTexto("12.12.2022");
        panelFechas.add(txtFechaInicio, gbcF);

        gbcF.gridx = 1;
        txtFechaFin = crearCampoTexto("12.12.2022");
        panelFechas.add(txtFechaFin, gbcF);

        panelTarjeta.add(panelFechas, gbc);

        // 4. CAMPO: Precio
        gbc.gridy++;
        panelTarjeta.add(crearEtiqueta("Precio"), gbc);

        gbc.gridy++;
        txtPrecio = crearCampoTexto("Ingrese el precio en pesos colombianos");
        panelTarjeta.add(txtPrecio, gbc);

        // 5. CARGA DE IMAGEN
        gbc.gridy++;
        gbc.insets = new Insets(12, 10, 12, 10);
        JPanel panelImagen = new JPanel(new BorderLayout(15, 0));
        panelImagen.setOpaque(false);

        btnCargarImagen = new JButton("Imagen del servicio");
        btnCargarImagen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCargarImagen.setForeground(Color.GRAY);
        btnCargarImagen.setBackground(Color.WHITE);
        btnCargarImagen.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btnCargarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCargarImagen.setPreferredSize(new Dimension(160, 36));

        lblNombreImagen = new JLabel("imagenservicio.png");
        lblNombreImagen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblNombreImagen.setForeground(Color.DARK_GRAY);

        panelImagen.add(btnCargarImagen, BorderLayout.WEST);
        panelImagen.add(lblNombreImagen, BorderLayout.CENTER);
        panelTarjeta.add(panelImagen, gbc);

        // 6. BOTÓN PRINCIPAL: Guardar
        gbc.gridy++;
        gbc.insets = new Insets(15, 10, 5, 10);
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(new Color(255, 204, 0));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(200, 45));

        JPanel panelBotonGuardar = new JPanel(new BorderLayout());
        panelBotonGuardar.setOpaque(false);
        panelBotonGuardar.add(btnGuardar, BorderLayout.WEST);
        panelTarjeta.add(panelBotonGuardar, gbc);

        // Scroll para resoluciones ajustadas
        JScrollPane scrollTarjeta = new JScrollPane(panelTarjeta);
        scrollTarjeta.setBorder(null);
        add(scrollTarjeta, BorderLayout.CENTER);
    }

    // --- MÉTODOS GETTERS ---
    public JComboBox<Servicios> getComboServicios() {
        return comboServicios;
    }

    public JTextField getTxtDescripcionPromocion() {
        return txtDescripcionPromocion;
    }

    public JTextField getTxtFechaInicio() {
        return txtFechaInicio;
    }

    public JTextField getTxtFechaFin() {
        return txtFechaFin;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public JButton getBtnCargarImagen() {
        return btnCargarImagen;
    }

    public JLabel getLblNombreImagen() {
        return lblNombreImagen;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    // --- MÉTODOS AUXILIARES DE ESTILO ---
    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        field.setForeground(Color.GRAY);
        field.setPreferredSize(new Dimension(380, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

}
