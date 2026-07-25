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
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;

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
import javax.swing.border.TitledBorder;
/**
 *
 * @author HOME
 */
public class VistaReservarCitas extends JPanel {

   
    public JComboBox<String> comboServicios; 
    
    // Componentes de la librería LGoodDatePicker para manejar fechas y horas
    public DatePicker datePickerFecha; // Selector de la fecha (el calendario)
    public TimePicker timePickerHora;  // Selector de la hora
    private DatePickerSettings configCalendario; // Guarda las reglas visuales del calendario
    
    public JTextArea txtObservaciones; // Área de texto para notas adicionales del cliente
    public JButton btnAgendar; // Botón principal para procesar la reserva
    public JTextField txtFechaHora; // Campo invisible de apoyo para compatibilidad con el controlador

    /* 
     * Lista que guardará las fechas que ya están ocupadas.
     * Esta lista se llena desde la base de datos cuando el controlador lo ordena.
     */
    private List<LocalDate> fechasOcupadas;

    // --- PALETA DE COLORES ---
    private final Color COLOR_DORADO_BOTON = new Color(250, 218, 94);
    private final Color COLOR_TEXTO_BOTON = new Color(139, 101, 8);
    private final Color COLOR_TEXTO_TITULO = new Color(40, 40, 40);
    private final Color COLOR_DIA_OCUPADO = new Color(255, 182, 193); // Un rosa pastel suave

    /**
     * CONSTRUCTOR DE LA VISTA
     * Aquí se "dibuja" y se acomoda todo en la pantalla al momento de iniciar el panel.
     */
    public VistaReservarCitas() {
        fechasOcupadas = new ArrayList<>();
        setOpaque(false); // Hacemos el fondo transparente para que se vea el fondo de la app principal
        setLayout(new GridBagLayout()); // Usamos GridBagLayout para centrar nuestra tarjeta en la pantalla

        /* 
         * 1. CREACIÓN DE LA TARJETA BLANCA (CONTENEDOR PRINCIPAL)
         * */
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

        /* 
         * 2. REGLAS DE POSICIONAMIENTO (GridBagConstraints)
         * */
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0); // Espacio entre cada fila
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes se estirarán a lo ancho
        gbc.gridx = 0; // Todo irá en la columna 0
        gbc.weightx = 1.0; // Ocupar todo el ancho disponible

        /*
         * 3. TÍTULO Y SUBTÍTULO
         **/
        JLabel lblTitulo = new JLabel("Reservar cita");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_TEXTO_TITULO);
        gbc.gridy = 0; // Fila 0
        tarjetaBlanca.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("<html>Complete los datos para consultar horarios disponibles y<br>reservar tu cita en tiempo real.</html>");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.GRAY);
        gbc.gridy = 1; // Fila 1
        tarjetaBlanca.add(lblSubtitulo, gbc);

        /* 
         * 4. SELECCIÓN DE PROFESIONAL / SERVICIO
         *  */
        JLabel lblServicio = new JLabel("Seleccione el profesional / servicio");
        lblServicio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblServicio.setForeground(Color.DARK_GRAY);
        gbc.gridy = 2; // Fila 2
        gbc.insets = new Insets(10, 0, 2, 0);
        tarjetaBlanca.add(lblServicio, gbc);

        comboServicios = new JComboBox<>();
        comboServicios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboServicios.setBackground(Color.WHITE);
        comboServicios.setPreferredSize(new Dimension(350, 38));
        gbc.gridy = 3; // Fila 3
        gbc.insets = new Insets(2, 0, 8, 0);
        tarjetaBlanca.add(comboServicios, gbc);

        /* 
         * 5. CONFIGURACIÓN DEL CALENDARIO Y RELOJ (LGoodDatePicker)
         *  */
        JPanel panelFechaHora = new JPanel(new BorderLayout(10, 0)); 
        panelFechaHora.setOpaque(false);

        // --- A. Reglas del Calendario ---
        configCalendario = new DatePickerSettings();

        // 1. Instanciamos PRIMERO el DatePicker para evitar la excepción java.lang.RuntimeException
        datePickerFecha = new DatePicker(configCalendario);
        datePickerFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        datePickerFecha.setPreferredSize(new Dimension(220, 38));
        datePickerFecha.setDateToToday(); // Iniciar siempre marcando el día de hoy

        // 2. AHORA SÍ aplicamos las políticas al DatePicker ya construido
        configCalendario.setVetoPolicy(new DateVetoPolicy() {
            @Override
            public boolean isDateAllowed(LocalDate date) {
                // Si la fecha es anterior a hoy, NO la permite (devuelve false)
                return !date.isBefore(LocalDate.now());
            }
        });

        configCalendario.setHighlightPolicy(new DateHighlightPolicy() {
            @Override
            public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
                // Comprueba si la fecha actual está en nuestra lista de "fechasOcupadas"
                if (fechasOcupadas.contains(date)) {
                    // Si está ocupada, la pinta de color Rosa pastel
                    return new HighlightInformation(COLOR_DIA_OCUPADO, Color.BLACK, "Día con citas programadas");
                }
                return null; 
            }
        });

        // --- B. Reglas de la Hora ---
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setFormatForDisplayTime("HH:mm"); // Formato 24hrs
        timeSettings.setFormatForMenuTimes("HH:mm");
        timeSettings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.ThirtyMinutes, null, null);
        
        timePickerHora = new TimePicker(timeSettings);
        timePickerHora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        timePickerHora.setPreferredSize(new Dimension(100, 38));

        // Agregamos ambos selectores al panel contenedor
        panelFechaHora.add(datePickerFecha, BorderLayout.CENTER);
        panelFechaHora.add(timePickerHora, BorderLayout.EAST);

        panelFechaHora.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Fecha y Hora de la cita (días rosas están ocupados)", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 11), Color.DARK_GRAY
        ));

        gbc.gridy = 4; // Fila 4
        gbc.insets = new Insets(4, 0, 8, 0);
        tarjetaBlanca.add(panelFechaHora, gbc);

        /* 
         * 6. CAMPO DE OBSERVACIONES
         *  */
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

        gbc.gridy = 5; // Fila 5
        tarjetaBlanca.add(scrollObs, gbc);

        /* 
         * 7. BOTÓN DE AGENDAR
         * */
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

        gbc.gridy = 6; // Fila 6
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 5, 0);
        tarjetaBlanca.add(btnAgendar, gbc);

        // Inicializar campo invisible
        txtFechaHora = new JTextField();

        // Finalmente, agregamos nuestra tarjeta blanca con todo su contenido al JPanel principal
        add(tarjetaBlanca);
    }

    /* 
     * MÉTODOS DE APOYO (Lógica de la vista)
     *  */

    /**
     * Este método recibe una lista de fechas (desde el controlador) que pertenecen a los días
     * en los que el peluquero ya tiene citas, y actualiza el calendario visualmente.
     * 
     * @param nuevasFechas Lista de fechas que se deben pintar de rosa.
     */
    public void marcarDiasOcupados(List<LocalDate> nuevasFechas) {
        this.fechasOcupadas = nuevasFechas;
        // Re-aplica la política para refrescar visualmente el calendario
        if (datePickerFecha != null && configCalendario != null) {
            datePickerFecha.getSettings().setHighlightPolicy(configCalendario.getHighlightPolicy());
        }
    }

    /**
     * Une la fecha del DatePicker y la hora del TimePicker en un solo texto (String).
     * Ideal para guardar directo en la Base de Datos.
     * 
     * @return Texto con formato "yyyy-MM-dd HH:mm:ss" (Ej: "2026-10-15 14:30:00")
     */
    public String getFechaHoraFormateada() {
        LocalDate fecha = datePickerFecha.getDate();
        LocalTime hora = timePickerHora.getTime();

        if (fecha == null || hora == null) {
            return ""; 
        }

        String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String horaFormateada = hora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String resultado = fechaFormateada + " " + horaFormateada;
        
        txtFechaHora.setText(resultado);
        
        return resultado;
    }
}
