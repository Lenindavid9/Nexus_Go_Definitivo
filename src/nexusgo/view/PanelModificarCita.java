/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HOME
 */
public class PanelModificarCita extends JPanel {

    private JComboBox<String> comboServicios;
    private JTable tablaHorarios;
    private DefaultTableModel modeloTabla;
    private JButton btnCerrarSesion;
    private JButton btnActualizarSemana;
    private DatePicker pickerFechaSemana;
    private JPanel tarjetaBlanca;

    private final Color COLOR_DORADO = new Color(184, 134, 11);
    private final Color COLOR_OCUPADO = new Color(255, 235, 204);
    private final Color COLOR_PASADO = new Color(230, 230, 230);

    public PanelModificarCita() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- PANEL TOP ---
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelTop.setOpaque(false);

        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCerrarSesion.setForeground(COLOR_DORADO);
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setPreferredSize(new Dimension(150, 40));

        panelTop.add(btnCerrarSesion);
        add(panelTop, BorderLayout.NORTH);

        // --- TARJETA PRINCIPAL ---
        tarjetaBlanca = new JPanel(new BorderLayout(15, 15));
        tarjetaBlanca.setOpaque(false);
        tarjetaBlanca.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        add(tarjetaBlanca, BorderLayout.CENTER);

        // --- CONTROLES SUPERIORES ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelSuperior.setOpaque(false);

        JLabel lblTitulo = new JLabel("Agenda Semanal de Citas:");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo);

        // Configuración DatePicker
        DatePickerSettings dateSettings = new DatePickerSettings(new Locale("es", "ES"));
        dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");

        // 1. Instanciar DatePicker primero
        pickerFechaSemana = new DatePicker(dateSettings);

        // 2. Asignar VetoPolicy después de la instanciación
        dateSettings.setVetoPolicy(new DateVetoPolicy() {
            @Override
            public boolean isDateAllowed(LocalDate date) {
                return date != null && !date.isBefore(LocalDate.now());
            }
        });

        pickerFechaSemana.setDateToToday();
        panelSuperior.add(pickerFechaSemana);

        btnActualizarSemana = new JButton("Buscar Semana");
        btnActualizarSemana.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnActualizarSemana.setBackground(COLOR_DORADO);
        btnActualizarSemana.setForeground(Color.WHITE);
        btnActualizarSemana.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelSuperior.add(btnActualizarSemana);

        comboServicios = new JComboBox<>(new String[]{"Todos los servicios"});
        panelSuperior.add(comboServicios);

        tarjetaBlanca.add(panelSuperior, BorderLayout.NORTH);

        // --- TABLA REJILLA DE HORARIOS ---
        String[] columnas = {"Hora", "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        Object[][] datosVacios = generarHorariosBase();

        modeloTabla = new DefaultTableModel(datosVacios, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHorarios = new JTable(modeloTabla);
        tablaHorarios.setRowHeight(40);
        tablaHorarios.setGridColor(COLOR_DORADO);
        tablaHorarios.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaHorarios.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tablaHorarios.getTableHeader().setBackground(Color.WHITE);
        tablaHorarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHorarios.setCellSelectionEnabled(true);

        tablaHorarios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 0) {
                    c.setBackground(new Color(245, 245, 245));
                    c.setFont(new Font("SansSerif", Font.BOLD, 12));
                    c.setForeground(Color.BLACK);
                    return c;
                }

                LocalDate fechaBase = pickerFechaSemana.getDate() != null ? pickerFechaSemana.getDate() : LocalDate.now();
                LocalDate domingo = fechaBase.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                LocalDate fechaColumna = domingo.plusDays(column - 1);

                if (fechaColumna.isBefore(LocalDate.now())) {
                    c.setBackground(COLOR_PASADO);
                    c.setForeground(Color.GRAY);
                } else if (value != null && !value.toString().trim().isEmpty()) {
                    c.setBackground(COLOR_OCUPADO);
                    c.setForeground(new Color(120, 80, 0));
                    c.setFont(new Font("SansSerif", Font.BOLD, 12));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                if (isSelected && !fechaColumna.isBefore(LocalDate.now())) {
                    c.setBackground(COLOR_DORADO);
                    c.setForeground(Color.WHITE);
                }

                return c;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaHorarios);
        tarjetaBlanca.add(scrollTabla, BorderLayout.CENTER);
    }

    private Object[][] generarHorariosBase() {
        String[] horas = {
            "06:00 AM", "07:00 AM", "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM",
            "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM"
        };
        Object[][] matriz = new Object[horas.length][8];
        for (int i = 0; i < horas.length; i++) {
            matriz[i][0] = horas[i];
            for (int j = 1; j < 8; j++) {
                matriz[i][j] = "";
            }
        }
        return matriz;
    }

    public JComboBox<String> getComboServicios() {
        return comboServicios;
    }

    public JTable getTablaHorarios() {
        return tablaHorarios;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public JButton getBtnActualizarSemana() {
        return btnActualizarSemana;
    }

    public DatePicker getPickerFechaSemana() {
        return pickerFechaSemana;
    }
}
