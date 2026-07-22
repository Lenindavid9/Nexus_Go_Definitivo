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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import nexusgo.model.Producto;
import nexusgo.model.Servicios;

/**
 *
 * @author USUARIO
 */
public class VistaAgregarPromocionCombo extends JPanel{
    
    // Componentes interactivos del formulario
    public JTextField txtNombreCombo;
    public JTextField txtDescripcionPromocion;
    public JTextField txtFechaInicio;
    public JTextField txtFechaFin;
    public JTextField txtPrecioCombo;

    // Listas de Selección Múltiple
    public DefaultListModel<Producto> modeloListaProductos;
    public JList<Producto> listaProductos;
    
    public DefaultListModel<Servicios> modeloListaServicios;
    public JList<Servicios> listaServicios;

    // Botones de acción y navegación
    public JButton btnCargarImagen;
    public JLabel lblNombreImagen;
    public JButton btnGuardar;
    public JButton btnVolver;
    public JButton btnCerrarSesion;

    public VistaAgregarPromocionCombo() {
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
        JLabel lblTitulo = new JLabel("Registrar Promoción Combo / Kit");
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

        // 1. CAMPO: Nombre del Combo
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelTarjeta.add(crearEtiqueta("Nombre del Kit / Combo"), gbc);

        gbc.gridy++;
        txtNombreCombo = crearCampoTexto("Ej: Kit Renovación Total (Corte + Producto)");
        panelTarjeta.add(txtNombreCombo, gbc);

        // 2. SELECCIÓN MÚLTIPLE: Productos y Servicios en Paralelo
        gbc.gridy++;
        JPanel panelListasSeleccion = new JPanel(new GridBagLayout());
        panelListasSeleccion.setOpaque(false);

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.fill = GridBagConstraints.BOTH;
        gbcL.weightx = 0.5;
        gbcL.weighty = 1.0;
        gbcL.insets = new Insets(0, 0, 0, 10);

        // --- Lista de Productos ---
        gbcL.gridx = 0;
        gbcL.gridy = 0;
        panelListasSeleccion.add(crearEtiqueta("Seleccione Productos (Mantenga CTRL o SHIFT)"), gbcL);

        gbcL.gridy = 1;
        modeloListaProductos = new DefaultListModel<>();
        listaProductos = new JList<>(modeloListaProductos);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaProductos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollProductos = new JScrollPane(listaProductos);
        scrollProductos.setPreferredSize(new Dimension(200, 110));
        scrollProductos.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelListasSeleccion.add(scrollProductos, gbcL);

        // --- Lista de Servicios ---
        gbcL.insets = new Insets(0, 10, 0, 0);
        gbcL.gridx = 1;
        gbcL.gridy = 0;
        panelListasSeleccion.add(crearEtiqueta("Seleccione Servicios (Mantenga CTRL o SHIFT)"), gbcL);

        gbcL.gridy = 1;
        modeloListaServicios = new DefaultListModel<>();
        listaServicios = new JList<>(modeloListaServicios);
        listaServicios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaServicios.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollServicios = new JScrollPane(listaServicios);
        scrollServicios.setPreferredSize(new Dimension(200, 110));
        scrollServicios.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panelListasSeleccion.add(scrollServicios, gbcL);

        panelTarjeta.add(panelListasSeleccion, gbc);

        // 3. CAMPO: Descripción de la promoción
        gbc.gridy++;
        panelTarjeta.add(crearEtiqueta("Descripción de la promoción"), gbc);

        gbc.gridy++;
        txtDescripcionPromocion = crearCampoTexto("Ingrese detalles de la promoción o combo");
        panelTarjeta.add(txtDescripcionPromocion, gbc);

        // 4. CAMPOS: Fecha de inicio y Fecha de finalización
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
        panelFechas.add(crearEtiqueta("Fecha de finalización"), gbcF);

        gbcF.gridx = 0;
        gbcF.gridy = 1;
        txtFechaInicio = crearCampoTexto("12.12.2026");
        panelFechas.add(txtFechaInicio, gbcF);

        gbcF.gridx = 1;
        txtFechaFin = crearCampoTexto("31.12.2026");
        panelFechas.add(txtFechaFin, gbcF);

        panelTarjeta.add(panelFechas, gbc);

        // 5. CAMPO: Precio Combo
        gbc.gridy++;
        panelTarjeta.add(crearEtiqueta("Precio Especial del Combo / Kit"), gbc);

        gbc.gridy++;
        txtPrecioCombo = crearCampoTexto("Ingrese el precio en pesos colombianos");
        panelTarjeta.add(txtPrecioCombo, gbc);

        // 6. CARGA DE IMAGEN
        gbc.gridy++;
        gbc.insets = new Insets(12, 10, 12, 10);
        JPanel panelImagen = new JPanel(new BorderLayout(15, 0));
        panelImagen.setOpaque(false);

        btnCargarImagen = new JButton("Imagen del Combo");
        btnCargarImagen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCargarImagen.setForeground(Color.GRAY);
        btnCargarImagen.setBackground(Color.WHITE);
        btnCargarImagen.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btnCargarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCargarImagen.setPreferredSize(new Dimension(160, 36));

        lblNombreImagen = new JLabel("imagencombo.png");
        lblNombreImagen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblNombreImagen.setForeground(Color.DARK_GRAY);

        panelImagen.add(btnCargarImagen, BorderLayout.WEST);
        panelImagen.add(lblNombreImagen, BorderLayout.CENTER);
        panelTarjeta.add(panelImagen, gbc);

        // 7. BOTÓN PRINCIPAL: Guardar
        gbc.gridy++;
        gbc.insets = new Insets(15, 10, 5, 10);
        btnGuardar = new JButton("Guardar Promo Combo");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(new Color(255, 204, 0));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(240, 45));

        JPanel panelBotonGuardar = new JPanel(new BorderLayout());
        panelBotonGuardar.setOpaque(false);
        panelBotonGuardar.add(btnGuardar, BorderLayout.WEST);
        panelTarjeta.add(panelBotonGuardar, gbc);

        // Scroll contenedor para pantallas con baja resolución
        JScrollPane scrollTarjeta = new JScrollPane(panelTarjeta);
        scrollTarjeta.setBorder(null);
        add(scrollTarjeta, BorderLayout.CENTER);
    }

    // --- MÉTODOS GETTERS ---
    public JTextField getTxtNombreCombo() { return txtNombreCombo; }
    public JTextField getTxtDescripcionPromocion() { return txtDescripcionPromocion; }
    public JTextField getTxtFechaInicio() { return txtFechaInicio; }
    public JTextField getTxtFechaFin() { return txtFechaFin; }
    public JTextField getTxtPrecioCombo() { return txtPrecioCombo; }

    public JList<Producto> getListaProductos() { return listaProductos; }
    public DefaultListModel<Producto> getModeloListaProductos() { return modeloListaProductos; }
    
    public JList<Servicios> getListaServicios() { return listaServicios; }
    public DefaultListModel<Servicios> getModeloListaServicios() { return modeloListaServicios; }

    public JButton getBtnCargarImagen() { return btnCargarImagen; }
    public JLabel getLblNombreImagen() { return lblNombreImagen; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }

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
