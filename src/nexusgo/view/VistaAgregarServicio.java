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

/**
 *
 * @author USUARIO
 */
public class VistaAgregarServicio extends JPanel{
    
    
   // Componentes del formulario
    public JTextField txtNombreServicio;  // Campo para el nombre del servicio
    public JTextField txtDescripcion;     // Campo para la descripción
    public JSpinner spinDuracionHoras;    // Selector numérico para las horas de duración
    public JComboBox<String> comboDuracionMinutos; // Selector rápido para fracciones de hora (00, 15, 30, 45 min)
    public JTextField txtPrecio;           // Campo de texto para el precio
    public JButton btnCargarImagen;       // Botón para examinar/subir imagen
    public JLabel lblNombreImagen;         // Etiqueta que muestra el nombre del archivo seleccionado
    public JButton btnGuardar;            // Botón principal de guardado

    // Botones de navegación
    public JButton btnVolver;              // Enlace de retorno "< Volver"
    public JButton btnCerrarSesion;        // Botón flotante superior "cerrar sesion"

    // Constantes de color
    private static final Color COLOR_DORADO = new Color(223, 205, 141);
    private static final Color COLOR_DORADO_OSCURO = new Color(185, 160, 90);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_TEXTO_TITULO = new Color(30, 30, 30);
    private static final Color COLOR_TEXTO_LABEL = new Color(70, 70, 70);
    private static final Color COLOR_TEXTO_MUTED = new Color(150, 150, 150);

    /**
     * Constructor principal de la vista
     */
    public VistaAgregarServicio() {
        // Establece el layout principal del panel como BorderLayout
        setLayout(new BorderLayout());
        setOpaque(false);

        // Contenedor central para la tarjeta (sin barra lateral)
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tarjeta blanca redondeada
        JPanel tarjetaBlanca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(COLOR_TARJETA);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

                g2.setColor(new Color(220, 220, 220));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 25, 25));
                g2.dispose();
            }
        };
        tarjetaBlanca.setOpaque(false);
        tarjetaBlanca.setLayout(new GridBagLayout());
        tarjetaBlanca.setBorder(new EmptyBorder(30, 45, 30, 45));
        tarjetaBlanca.setPreferredSize(new Dimension(650, 680));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        int row = 0;

        // Encabezado con título y enlace de regreso
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Registrar servicio");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);

        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVolver.setForeground(COLOR_TEXTO_LABEL);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnVolver, BorderLayout.EAST);

        gbc.gridy = row++;
        tarjetaBlanca.add(panelHeader, gbc);

        gbc.gridy = row++;
        tarjetaBlanca.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        // 1. Campo Nombre del Servicio
        gbc.gridy = row++;
        tarjetaBlanca.add(crearLabelCampo("Nombre del servicio"), gbc);

        txtNombreServicio = crearTextFieldRedondeado("Ingrese el nombre del servicio");
        gbc.gridy = row++;
        tarjetaBlanca.add(txtNombreServicio, gbc);

        // 2. Campo Descripción
        gbc.gridy = row++;
        tarjetaBlanca.add(crearLabelCampo("Descripción del servicio"), gbc);

        txtDescripcion = crearTextFieldRedondeado("Ingrese una breve descripción");
        gbc.gridy = row++;
        tarjetaBlanca.add(txtDescripcion, gbc);

        // 3. Campo Duración Optimizado (Horas y Minutos)
        gbc.gridy = row++;
        tarjetaBlanca.add(crearLabelCampo("Duración estimada"), gbc);

        JPanel panelDuracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelDuracion.setOpaque(false);

        // Spinner para horas (de 0 a 24)
        spinDuracionHoras = new JSpinner(new SpinnerNumberModel(1, 0, 24, 1));
        spinDuracionHoras.setPreferredSize(new Dimension(80, 38));
        spinDuracionHoras.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel lblHoras = new JLabel("Horas");
        lblHoras.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblHoras.setForeground(COLOR_TEXTO_LABEL);

        // Selector para minutos aproximados
        String[] minutosOpciones = {"00 min", "15 min", "30 min", "45 min"};
        comboDuracionMinutos = new JComboBox<>(minutosOpciones);
        comboDuracionMinutos.setPreferredSize(new Dimension(100, 38));
        comboDuracionMinutos.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panelDuracion.add(spinDuracionHoras);
        panelDuracion.add(lblHoras);
        panelDuracion.add(Box.createRigidArea(new Dimension(10, 0)));
        panelDuracion.add(comboDuracionMinutos);

        gbc.gridy = row++;
        tarjetaBlanca.add(panelDuracion, gbc);

        // 4. Campo Precio
        gbc.gridy = row++;
        tarjetaBlanca.add(crearLabelCampo("Precio"), gbc);

        txtPrecio = crearTextFieldRedondeado("Ingrese el precio en pesos colombianos");
        gbc.gridy = row++;
        tarjetaBlanca.add(txtPrecio, gbc);

        gbc.gridy = row++;
        tarjetaBlanca.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        // Subida de imagen
        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelImagen.setOpaque(false);

        btnCargarImagen = new JButton("Imagen del servicio") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_TARJETA);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(COLOR_DORADO);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCargarImagen.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCargarImagen.setForeground(COLOR_DORADO_OSCURO);
        btnCargarImagen.setContentAreaFilled(false);
        btnCargarImagen.setBorderPainted(false);
        btnCargarImagen.setFocusPainted(false);
        btnCargarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCargarImagen.setPreferredSize(new Dimension(170, 38));

        lblNombreImagen = new JLabel("imagenservicio.png");
        lblNombreImagen.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblNombreImagen.setForeground(COLOR_TEXTO_MUTED);

        panelImagen.add(btnCargarImagen);
        panelImagen.add(lblNombreImagen);

        gbc.gridy = row++;
        tarjetaBlanca.add(panelImagen, gbc);

        gbc.gridy = row++;
        tarjetaBlanca.add(Box.createRigidArea(new Dimension(0, 15)), gbc);

        // Botón Guardar
        btnGuardar = new JButton("Guardar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_DORADO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(190, 45));

        JPanel panelBotonGuardar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonGuardar.setOpaque(false);
        panelBotonGuardar.add(btnGuardar);

        gbc.gridy = row++;
        tarjetaBlanca.add(panelBotonGuardar, gbc);

        panelContenido.add(tarjetaBlanca);

        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Panel superior para el botón de cerrar sesión
        JPanel panelSuperiorDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperiorDerecho.setOpaque(false);
        panelSuperiorDerecho.setBorder(new EmptyBorder(15, 0, 0, 25));

        btnCerrarSesion = new JButton("cerrar sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_DORADO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setPreferredSize(new Dimension(140, 36));

        panelSuperiorDerecho.add(btnCerrarSesion);

        // OverlayLayout para mantener el botón superior sobre el scroll
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));

        panelSuperiorDerecho.setAlignmentX(1.0f);
        panelSuperiorDerecho.setAlignmentY(0.0f);
        scrollPane.setAlignmentX(0.5f);
        scrollPane.setAlignmentY(0.5f);

        layeredPane.add(panelSuperiorDerecho, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(scrollPane, JLayeredPane.DEFAULT_LAYER);

        add(layeredPane, BorderLayout.CENTER);
    }

    // Métodos auxiliares de creación de componentes
    private JLabel crearLabelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(COLOR_TEXTO_LABEL);
        return lbl;
    }

    private JTextField crearTextFieldRedondeado(String placeholder) {
        JTextField txt = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(new Color(210, 210, 210));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        txt.setOpaque(false);
        txt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txt.setForeground(COLOR_TEXTO_MUTED);
        txt.setBorder(new EmptyBorder(8, 12, 8, 12));
        txt.setPreferredSize(new Dimension(0, 38));

        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(COLOR_TEXTO_TITULO);
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (txt.getText().trim().isEmpty()) {
                    txt.setText(placeholder);
                    txt.setForeground(COLOR_TEXTO_MUTED);
                }
            }
        });

        return txt;
    }
}
