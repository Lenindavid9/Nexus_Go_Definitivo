/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author USUARIO
 */
public class VistaAgregarServicio extends JPanel {

    // Componentes del formulario
    public JTextField txtNombreServicio;
    public JTextField txtDescripcion;
    public JSpinner spinDuracionHoras;
    public JComboBox<String> comboDuracionMinutos;
    public JTextField txtPrecio;
    public JButton btnCargarImagen;
    public JLabel lblNombreImagen;
    public JButton btnGuardar;
    public JButton btnVolver;
    public JButton btnCerrarSesion;

    // Constantes
    private final Color COLOR_DORADO = new Color(184, 134, 11);
    private static final Dimension TAMAÑO_BOTON = new Dimension(200, 45);

    public VistaAgregarServicio() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // 1. PANEL SUPERIOR
        JPanel panelSuperiorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperiorDerecho.setOpaque(false);
        panelSuperiorDerecho.setBorder(new EmptyBorder(15, 0, 0, 25));

        btnCerrarSesion = new JButton("Cerrar Sesión");
        estiloBotones(btnCerrarSesion);
        panelSuperiorDerecho.add(btnCerrarSesion);
        add(panelSuperiorDerecho, BorderLayout.NORTH);

        // 2. PANEL CONTENIDO (Centrado absoluto)
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setOpaque(false);
        
        JPanel tarjetaBlanca = new JPanel(new GridBagLayout());
        tarjetaBlanca.setOpaque(false);
        tarjetaBlanca.setBorder(new EmptyBorder(30, 45, 30, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0); // Espaciado entre filas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER; // FUERZA EL CENTRADO
        int row = 0;

        // Encabezado
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitulo = new JLabel("Registrar servicio");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        
        btnVolver = new JButton("< Volver");
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 15));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnVolver, BorderLayout.EAST);
        gbc.gridy = row++; tarjetaBlanca.add(panelHeader, gbc);

        // Campos
        gbc.gridy = row++; tarjetaBlanca.add(campodeTexto("Nombre del servicio"), gbc);
        txtNombreServicio = campoJtextField("Ingrese el nombre del servicio");
        gbc.gridy = row++; tarjetaBlanca.add(txtNombreServicio, gbc);

        gbc.gridy = row++; tarjetaBlanca.add(campodeTexto("Descripción del servicio"), gbc);
        txtDescripcion = campoJtextField("Ingrese una breve descripción");
        gbc.gridy = row++; tarjetaBlanca.add(txtDescripcion, gbc);

        // Duración
        gbc.gridy = row++; tarjetaBlanca.add(campodeTexto("Duración estimada"), gbc);
        JPanel panelDuracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelDuracion.setOpaque(false);
        spinDuracionHoras = new JSpinner(new SpinnerNumberModel(1, 0, 24, 1));
        spinDuracionHoras.setPreferredSize(new Dimension(100, 40));
        spinDuracionHoras.setFont(new Font("SansSerif", Font.PLAIN, 15));
        comboDuracionMinutos = new JComboBox<>(new String[]{"00 min", "15 min", "30 min", "45 min"});
        comboDuracionMinutos.setPreferredSize(new Dimension(120, 40));
        comboDuracionMinutos.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panelDuracion.add(spinDuracionHoras);
        panelDuracion.add(new JLabel("Horas"){{setForeground(Color.WHITE); setFont(new Font("SansSerif", Font.PLAIN, 15));}});
        panelDuracion.add(comboDuracionMinutos);
        gbc.gridy = row++; tarjetaBlanca.add(panelDuracion, gbc);

        // Precio
        gbc.gridy = row++; tarjetaBlanca.add(campodeTexto("Precio"), gbc);
        txtPrecio = campoJtextField("Ingrese el precio en pesos");
        gbc.gridy = row++; tarjetaBlanca.add(txtPrecio, gbc);

        // Imagen
        btnCargarImagen = new JButton("Imagen del servicio");
        estiloBotones(btnCargarImagen);
        lblNombreImagen = new JLabel("imagenservicio.png");
        lblNombreImagen.setForeground(Color.WHITE);
        lblNombreImagen.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelImagen.setOpaque(false);
        panelImagen.add(btnCargarImagen); panelImagen.add(lblNombreImagen);
        gbc.gridy = row++; tarjetaBlanca.add(panelImagen, gbc);

        // Guardar
        btnGuardar = new JButton("Guardar");
        estiloBotones(btnGuardar);
        JPanel panelBotonGuardar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelBotonGuardar.setOpaque(false);
        panelBotonGuardar.add(btnGuardar);
        gbc.gridy = row++; tarjetaBlanca.add(panelBotonGuardar, gbc);

        // Añadimos la tarjeta al centro del panel de contenido
        panelContenido.add(tarjetaBlanca, new GridBagConstraints()); 
        
        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setOpaque(false); scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void estiloBotones(JButton btn) {
        btn.setBackground(COLOR_DORADO);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel campodeTexto(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 19));
        return lbl;
    }

    private JTextField campoJtextField(String placeholder) {
        JTextField txt = new JTextField(placeholder);
        txt.setPreferredSize(new Dimension(550, 40));
        txt.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txt.setForeground(new Color(150, 150, 150));
        txt.setBorder(new LineBorder(Color.GRAY, 1));
        
        txt.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { 
                if(txt.getText().equals(placeholder)) { txt.setText(""); txt.setForeground(Color.BLACK); } 
            }
            public void focusLost(FocusEvent e) { 
                if(txt.getText().trim().isEmpty()) { txt.setText(placeholder); txt.setForeground(new Color(150, 150, 150)); } 
            }
        });
        return txt;
    }
}