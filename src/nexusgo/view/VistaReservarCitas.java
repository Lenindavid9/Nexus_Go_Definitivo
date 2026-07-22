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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author HOME
 */
public class VistaReservarCitas extends JPanel {

    public JComboBox<String> comboServicios;
    public JDateChooser dateChooserFecha;
    public JSpinner spinnerHora;
    public JTextArea txtObservaciones;
    
    public JButton btnAgendar; // Botón "Reservar cita"
    public JButton btnVolver;  // Botón auxiliar para el controlador

    // Compatibilidad con el controlador
    public JTextField txtFechaHora; 

    private final Color COLOR_DORADO_BOTON = new Color(250, 218, 94);
    private final Color COLOR_TEXTO_BOTON = new Color(139, 101, 8);
    private final Color COLOR_TEXTO_TITULO = new Color(40, 40, 40);

    public VistaReservarCitas() {
        setOpaque(false);
        setLayout(new GridBagLayout()); // Centra la tarjeta blanca

        // 1. Tarjeta Blanca Flotante
        JPanel tarjetaBlanca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        tarjetaBlanca.setOpaque(false);
        tarjetaBlanca.setLayout(new GridBagLayout());
        tarjetaBlanca.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // --- TÍTULO Y SUBTÍTULO ---
        JLabel lblTitulo = new JLabel("Reservar cita");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        gbc.gridy = 0;
        tarjetaBlanca.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("<html>Complete los datos para consultar horarios disponibles y<br>reservar tu cita en tiempo real.</html>");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.GRAY);
        gbc.gridy = 1;
        tarjetaBlanca.add(lblSubtitulo, gbc);

        // --- SELECCIÓN DE SERVICIO ---
        JLabel lblServicio = new JLabel("Seleccione el tipo de servicio");
        lblServicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblServicio.setForeground(Color.DARK_GRAY);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 2, 0);
        tarjetaBlanca.add(lblServicio, gbc);

        comboServicios = new JComboBox<>();
        comboServicios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboServicios.setBackground(Color.WHITE);
        comboServicios.setPreferredSize(new Dimension(350, 38));
        gbc.gridy = 3;
        gbc.insets = new Insets(2, 0, 8, 0);
        tarjetaBlanca.add(comboServicios, gbc);

        // --- FECHA Y HORA (JCalendar + JSpinner) ---
        JPanel panelFechaHora = new JPanel(new BorderLayout(10, 0));
        panelFechaHora.setOpaque(false);

        // JDateChooser
        dateChooserFecha = new JDateChooser(new Date());
        dateChooserFecha.setDateFormatString("yyyy-MM-dd");
        dateChooserFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateChooserFecha.setBackground(Color.WHITE);
        dateChooserFecha.setPreferredSize(new Dimension(220, 38));

        // JSpinner para la hora
        SpinnerDateModel timeModel = new SpinnerDateModel();
        spinnerHora = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(timeEditor);
        spinnerHora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinnerHora.setPreferredSize(new Dimension(100, 38));

        panelFechaHora.add(dateChooserFecha, BorderLayout.CENTER);
        panelFechaHora.add(spinnerHora, BorderLayout.EAST);

        panelFechaHora.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Fecha y Hora de la cita", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 11), Color.DARK_GRAY
        ));

        gbc.gridy = 4;
        gbc.insets = new Insets(4, 0, 8, 0);
        tarjetaBlanca.add(panelFechaHora, gbc);

        // --- OBSERVACIONES CON SCROLL ---
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setOpaque(false);
        scrollObs.getViewport().setOpaque(false);
        scrollObs.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Observaciones (opcional)", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 11), Color.DARK_GRAY
        ));

        gbc.gridy = 5;
        tarjetaBlanca.add(scrollObs, gbc);

        // --- BOTÓN AGENDAR ---
        btnAgendar = new JButton("Reservar cita") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_DORADO_BOTON);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        btnAgendar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAgendar.setForeground(COLOR_TEXTO_BOTON);
        btnAgendar.setContentAreaFilled(false);
        btnAgendar.setBorderPainted(false);
        btnAgendar.setFocusPainted(false);
        btnAgendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgendar.setPreferredSize(new Dimension(200, 42));

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 5, 0);
        tarjetaBlanca.add(btnAgendar, gbc);

        // Instancias auxiliares de respaldo
        btnVolver = new JButton();
        txtFechaHora = new JTextField();

        add(tarjetaBlanca);
    }

    public String getFechaHoraFormateada() {
        Date fecha = dateChooserFecha.getDate();
        Date hora = (Date) spinnerHora.getValue();

        if (fecha == null) {
            return "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        return dateFormat.format(fecha) + " " + timeFormat.format(hora);
    }
}
