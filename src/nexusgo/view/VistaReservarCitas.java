/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import com.github.lgooddatepicker.optionalusertools.TimeVetoPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import nexusgo.model.HorarioNegocio;

/**
 *
 * @author HOME
 */
public class VistaReservarCitas extends JPanel {

    public JComboBox<String> comboServicios;

    public DatePicker datePickerFecha;
    private DatePickerSettings configCalendario;
    public JTextArea txtObservaciones;
    public JButton btnAgendar;
    public JTextField txtFechaHora;

    private JPanel panelSlots;
    private JLabel lblEstadoSlots;
    private JButton botonSeleccionado;
    private LocalTime horaSeleccionada;

    private final Color COLOR_DORADO_BOTON = new Color(250, 218, 94);
    private final Color COLOR_TEXTO_BOTON = new Color(139, 101, 8);
    private final Color COLOR_TEXTO_TITULO = new Color(40, 40, 40);

    private final Color COLOR_SLOT_DISPONIBLE = new Color(214, 245, 214);
    private final Color COLOR_SLOT_DISPONIBLE_TEXTO = new Color(30, 110, 30);
    private final Color COLOR_SLOT_OCUPADO = new Color(240, 240, 240);
    private final Color COLOR_SLOT_OCUPADO_TEXTO = new Color(160, 160, 160);
    private final Color COLOR_SLOT_SELECCIONADO = new Color(250, 218, 94);
    private final Color COLOR_SLOT_SELECCIONADO_TEXTO = new Color(90, 65, 5);

    public VistaReservarCitas() {
        setOpaque(false);
        setLayout(new GridBagLayout());

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

        JLabel lblServicio = new JLabel("Seleccione el profesional / servicio");
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

        JPanel panelFechaHora = new JPanel(new BorderLayout(10, 0));
        panelFechaHora.setOpaque(false);

        configCalendario = new DatePickerSettings();
        datePickerFecha = new DatePicker(configCalendario);
        datePickerFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        datePickerFecha.setPreferredSize(new Dimension(220, 38));
        datePickerFecha.setDateToToday();

        configCalendario.setVetoPolicy(date -> !date.isBefore(LocalDate.now()));

        panelFechaHora.add(datePickerFecha, BorderLayout.CENTER);
        panelFechaHora.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Fecha de la cita", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 11), Color.DARK_GRAY
        ));

        gbc.gridy = 4;
        gbc.insets = new Insets(4, 0, 8, 0);
        tarjetaBlanca.add(panelFechaHora, gbc);

        JPanel contenedorSlots = new JPanel(new BorderLayout(0, 6));
        contenedorSlots.setOpaque(false);

        lblEstadoSlots = new JLabel("Seleccione un servicio y una fecha para ver las horas disponibles.");
        lblEstadoSlots.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblEstadoSlots.setForeground(Color.GRAY);
        contenedorSlots.add(lblEstadoSlots, BorderLayout.NORTH);

        panelSlots = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panelSlots.setOpaque(false);

        JScrollPane scrollSlots = new JScrollPane(panelSlots);
        scrollSlots.setOpaque(false);
        scrollSlots.getViewport().setOpaque(false);
        scrollSlots.setPreferredSize(new Dimension(350, 140));
        scrollSlots.setBorder(BorderFactory.createEmptyBorder());
        scrollSlots.getVerticalScrollBar().setUnitIncrement(16);
        contenedorSlots.add(scrollSlots, BorderLayout.CENTER);

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        leyenda.setOpaque(false);
        leyenda.add(crearItemLeyenda(COLOR_SLOT_DISPONIBLE, "Disponible"));
        leyenda.add(crearItemLeyenda(COLOR_SLOT_OCUPADO, "Ocupado"));
        leyenda.add(crearItemLeyenda(COLOR_SLOT_SELECCIONADO, "Seleccionada"));
        contenedorSlots.add(leyenda, BorderLayout.SOUTH);

        contenedorSlots.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Hora de la cita", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 11), Color.DARK_GRAY
        ));

        gbc.gridy = 5;
        gbc.insets = new Insets(4, 0, 8, 0);
        tarjetaBlanca.add(contenedorSlots, gbc);

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

        gbc.gridy = 6;
        tarjetaBlanca.add(scrollObs, gbc);

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

        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 5, 0);
        tarjetaBlanca.add(btnAgendar, gbc);

        txtFechaHora = new JTextField();
        add(tarjetaBlanca);
    }

    private JPanel crearItemLeyenda(Color color, String texto) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        item.setOpaque(false);
        JLabel cuadro = new JLabel("  ");
        cuadro.setOpaque(true);
        cuadro.setBackground(color);
        cuadro.setPreferredSize(new Dimension(14, 14));
        cuadro.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        JLabel texto2 = new JLabel(texto);
        texto2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        texto2.setForeground(Color.DARK_GRAY);
        item.add(cuadro);
        item.add(texto2);
        return item;
    }

    public void mostrarSlots(List<HorarioNegocio.SlotDisponibilidad> slots, String mensaje) {
        panelSlots.removeAll();
        botonSeleccionado = null;
        horaSeleccionada = null;

        if (mensaje != null) {
            lblEstadoSlots.setText(mensaje);
        }

        if (slots == null || slots.isEmpty()) {
            lblEstadoSlots.setText(mensaje != null ? mensaje : "No hay horas disponibles para este día.");
        } else {
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            for (HorarioNegocio.SlotDisponibilidad slot : slots) {
                JButton botonHora = new JButton(slot.hora.format(formatoHora));
                botonHora.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                botonHora.setFocusPainted(false);
                botonHora.setPreferredSize(new Dimension(64, 30));
                botonHora.setHorizontalAlignment(SwingConstants.CENTER);
                botonHora.setBorder(BorderFactory.createLineBorder(new Color(190, 190, 190)));

                if (slot.disponible) {
                    botonHora.setBackground(COLOR_SLOT_DISPONIBLE);
                    botonHora.setForeground(COLOR_SLOT_DISPONIBLE_TEXTO);
                    botonHora.setContentAreaFilled(true);
                    botonHora.setOpaque(true);
                    botonHora.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    botonHora.setToolTipText("Disponible");
                    botonHora.addActionListener(e -> seleccionarSlot(botonHora, slot.hora));
                } else {
                    botonHora.setBackground(COLOR_SLOT_OCUPADO);
                    botonHora.setForeground(COLOR_SLOT_OCUPADO_TEXTO);
                    botonHora.setContentAreaFilled(true);
                    botonHora.setOpaque(true);
                    botonHora.setEnabled(false);
                    botonHora.setToolTipText("No disponible");
                }

                panelSlots.add(botonHora);
            }
        }

        panelSlots.revalidate();
        panelSlots.repaint();
    }

    private void seleccionarSlot(JButton boton, LocalTime hora) {
        if (botonSeleccionado != null) {
            botonSeleccionado.setBackground(COLOR_SLOT_DISPONIBLE);
            botonSeleccionado.setForeground(COLOR_SLOT_DISPONIBLE_TEXTO);
        }
        boton.setBackground(COLOR_SLOT_SELECCIONADO);
        boton.setForeground(COLOR_SLOT_SELECCIONADO_TEXTO);
        botonSeleccionado = boton;
        horaSeleccionada = hora;
    }

    public LocalTime getHoraSeleccionada() {
        return horaSeleccionada;
    }

    public void limpiarSeleccionHora() {
        horaSeleccionada = null;
        botonSeleccionado = null;
    }

    public String getFechaHoraFormateada() {
        LocalDate fecha = datePickerFecha.getDate();
        if (fecha == null || horaSeleccionada == null) {
            return "";
        }
        String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String horaFormateada = horaSeleccionada.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String resultado = fechaFormateada + " " + horaFormateada;
        txtFechaHora.setText(resultado);
        return resultado;
    }
}
