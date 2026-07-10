/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author HOME
 */
public class VistaReservarCitas extends JPanel {

    // Componentes públicos que usará tu controlador
    public JButton btnVolver;
    public JComboBox<String> comboServicios;
    public JTextField txtFechaHora;
    public JButton btnAgendar;

    public VistaReservarCitas() {
        this.setLayout(new BorderLayout(0, 20));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- PANEL SUPERIOR: TÍTULO Y VOLVER ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JLabel lblTitulo = new JLabel("RESERVAR NUEVA CITA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(62, 58, 46));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelSuperior.add(btnVolver, BorderLayout.EAST); // <-- CORREGIDO: Derecha

        this.add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL: FORMULARIO SENCILLO ---
        JPanel panelFormulario = new JPanel(new GridLayout(5, 1, 0, 10));
        panelFormulario.setOpaque(false);

        // 1. Selección de Servicio
        panelFormulario.add(new JLabel("Seleccione el Servicio / Producto:"));
        comboServicios = new JComboBox<>();
        panelFormulario.add(comboServicios);

        // 2. Ingreso de Fecha y Hora
        panelFormulario.add(new JLabel("Fecha y Hora (Ej: 2026-07-15 14:30):"));
        txtFechaHora = new JTextField();
        panelFormulario.add(txtFechaHora);

        // 3. Botón de Acción
        btnAgendar = new JButton("Confirmar Reserva");
        btnAgendar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgendar.setBackground(Color.decode("#EFB810")); // Dorado corporativo
        btnAgendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFormulario.add(btnAgendar);

        // Añadimos el formulario centrado
        this.add(panelFormulario, BorderLayout.CENTER);
    }
}
