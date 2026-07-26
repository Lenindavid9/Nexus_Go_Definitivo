/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import javax.swing.JDialog;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author USUARIO
 */
public class DialogDetalleModificarCita extends JDialog {

    private JTextField txtCliente, txtServicio, txtPrecio;
    private DatePicker pickerNuevaFecha;
    private TimePicker pickerNuevaHora;
    private JTextArea txtObservacion;
    private JButton btnGuardarCambios, btnCancelar;

    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public DialogDetalleModificarCita(Frame padre, boolean modal) {
        super(padre, modal);
        setTitle("Detalle y Modificación de Cita");
        setSize(480, 520);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(10, 10));

        // --- INFORMACIÓN DE LA CITA ---
        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelForm.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        txtCliente.setEditable(false);
        panelForm.add(txtCliente);

        panelForm.add(new JLabel("Servicio:"));
        txtServicio = new JTextField();
        txtServicio.setEditable(false);
        panelForm.add(txtServicio);

        panelForm.add(new JLabel("Precio Servicio ($):"));
        txtPrecio = new JTextField();
        txtPrecio.setEditable(false);
        panelForm.add(txtPrecio);

        panelForm.add(new JLabel("Nueva Fecha:"));
        DatePickerSettings dateSettings = new DatePickerSettings(new Locale("es", "ES"));
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");

        pickerNuevaFecha = new DatePicker(dateSettings);
        dateSettings.setVetoPolicy(new DateVetoPolicy() {
            @Override
            public boolean isDateAllowed(LocalDate date) {
                return date != null && !date.isBefore(LocalDate.now());
            }
        });
        pickerNuevaFecha.setDateToToday();
        panelForm.add(pickerNuevaFecha);

        panelForm.add(new JLabel("Nuevas Horas / Bloque:"));
        pickerNuevaHora = new TimePicker();
        panelForm.add(pickerNuevaHora);

        add(panelForm, BorderLayout.NORTH);

        // --- OBSERVACIÓN DEL REAGENDAMIENTO ---
        JPanel panelObs = new JPanel(new BorderLayout(5, 5));
        panelObs.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        JLabel lblObs = new JLabel("Observación de la modificación (Opcional):");
        lblObs.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelObs.add(lblObs, BorderLayout.NORTH);

        txtObservacion = new JTextArea(3, 20);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);
        panelObs.add(new JScrollPane(txtObservacion), BorderLayout.CENTER);

        add(panelObs, BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel panelBotones = new JPanel();
        btnGuardarCambios = new JButton("Reagendar / Modificar");
        btnGuardarCambios.setBackground(COLOR_DORADO);
        btnGuardarCambios.setForeground(Color.WHITE);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardarCambios);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarDatosCita(String cliente, String servicio, double precio, String fechaActual, String horaActual) {
        txtCliente.setText(cliente);
        txtServicio.setText(servicio);
        txtPrecio.setText(String.format("$%.2f", precio));
    }

    public DatePicker getPickerNuevaFecha() {
        return pickerNuevaFecha;
    }

    public TimePicker getPickerNuevaHora() {
        return pickerNuevaHora;
    }

    public JTextArea getTxtObservacion() {
        return txtObservacion;
    }

    public JButton getBtnGuardarCambios() {
        return btnGuardarCambios;
    }
}
