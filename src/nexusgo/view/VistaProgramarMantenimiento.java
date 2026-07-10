/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
/**
 *
 * @author USUARIO
 */
public class VistaProgramarMantenimiento extends JPanel{
    
    // Componentes públicos que el controlador va a escuchar o leer
    public JSpinner spinnerFechaHora;
    public JComboBox<String> cbTipoMantenimiento;
    public JTextField txtFallaProblema,txtObservaciones;
    public JButton btnImagen,btnGuardarMantenimiento, btnVolver;
    public JLabel lblNombreImagen;

    
    // Campo oculto para pasar el nombre o ID de la herramienta seleccionada
    public JTextField txtEquipo;

    public VistaProgramarMantenimiento() {
        // Configuración básica del Panel
        setBackground(Color.WHITE);
        setLayout(null);
        setSize(650, 650);

        // --- Botón Volver ---
        btnVolver = new JButton("< Volver");
        btnVolver.setBounds(500, 30, 90, 25);
        add(btnVolver);

        // --- Título ---
        JLabel lblTitulo = new JLabel("Programacion");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(40, 20, 200, 30);
        add(lblTitulo);

        // --- Campo invisible de control ---
        txtEquipo = new JTextField();
        txtEquipo.setVisible(false);
        add(txtEquipo);

        // --- 1. Entrada de Fecha y Hora ---
        JLabel lblFecha = new JLabel("Ingrese fecha que se va a realizar el mantenimiento");
        lblFecha.setBounds(40, 80, 400, 20);
        add(lblFecha);

        SpinnerDateModel modelo = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerFechaHora = new JSpinner(modelo);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerFechaHora, "yyyy-MM-dd HH:mm");
        spinnerFechaHora.setEditor(editor);
        spinnerFechaHora.setBounds(40, 105, 180, 30);
        add(spinnerFechaHora);

        // --- 2. Tipo de Mantenimiento ---
        JLabel lblTipo = new JLabel("tipo de mantenimiento");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTipo.setBounds(40, 160, 200, 20);
        add(lblTipo);

        JLabel lblSubTipo = new JLabel("Seleccione el tipo de mantenimiento");
        lblSubTipo.setForeground(Color.GRAY);
        lblSubTipo.setBounds(40, 180, 300, 20);
        add(lblSubTipo);

        String[] opciones = {"Seleccione su tipo de mantenimiento", "Preventivo", "Correctivo"};
        cbTipoMantenimiento = new JComboBox<>(opciones);
        cbTipoMantenimiento.setBounds(40, 205, 350, 30);
        add(cbTipoMantenimiento);

        // --- 3. Falla o Problema ---
        JLabel lblFalla = new JLabel("falla o problema que tiene la herramienta");
        lblFalla.setFont(new Font("Arial", Font.BOLD, 13));
        lblFalla.setBounds(40, 260, 400, 20);
        add(lblFalla);

        txtFallaProblema = new JTextField();
        txtFallaProblema.setBounds(40, 285, 350, 35);
        add(txtFallaProblema);

        // --- 4. Observaciones ---
        JLabel lblObs = new JLabel("Observaciones");
        lblObs.setFont(new Font("Arial", Font.BOLD, 13));
        lblObs.setBounds(40, 345, 200, 20);
        add(lblObs);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(40, 370, 350, 35);
        add(txtObservaciones);

        // --- 5. Botón de Adjuntar Foto ---
        btnImagen = new JButton("Imagen del producto");
        btnImagen.setBounds(40, 435, 160, 30);
        add(btnImagen);

        lblNombreImagen = new JLabel("tratamiento.png");
        lblNombreImagen.setForeground(Color.DARK_GRAY);
        lblNombreImagen.setBounds(210, 440, 200, 20);
        add(lblNombreImagen);

        // --- 6. Botón Guardar ---
        btnGuardarMantenimiento = new JButton("Guardar");
        btnGuardarMantenimiento.setBackground(new Color(255, 215, 0)); // Amarillo estándar
        btnGuardarMantenimiento.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardarMantenimiento.setBounds(40, 510, 150, 40);
        add(btnGuardarMantenimiento);
    }
    
}
