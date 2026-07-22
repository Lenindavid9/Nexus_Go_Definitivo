/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author USUARIO
 */
public class VistaProgramarMantenimiento extends JPanel{
    
   // Componentes públicos accesibles desde el controlador
    public JDateChooser selectorFecha; 
    public JSpinner spinnerHora;       
    
    // --- NUEVO: Componentes JCalendar para Inicio y Fin de Promoción ---
    public JDateChooser fechaInicioPromocion;
    public JDateChooser fechaFinPromocion;

    public JComboBox<String> cbTipoMantenimiento;
    public JTextField txtFallaProblema, txtObservaciones;
    public JButton btnImagen, btnGuardarMantenimiento, btnVolver;
    public JLabel lblNombreImagen;

    // Archivo de imagen seleccionado
    private File archivoImagenSeleccionado;

    // Campo oculto para el equipo/herramienta
    public JTextField txtEquipo;

    public VistaProgramarMantenimiento() {
        // Configuración básica del Panel (Aumentamos el alto a 750 para dar espacio)
        setBackground(Color.WHITE);
        setLayout(null);
        setSize(650, 750);

        // --- Botón Volver ---
        btnVolver = new JButton("< Volver");
        btnVolver.setBounds(500, 30, 90, 25);
        add(btnVolver);

        // --- Título ---
        JLabel lblTitulo = new JLabel("Programacion de Mantenimiento");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBounds(40, 20, 400, 30);
        add(lblTitulo);

        // --- Campo invisible de control ---
        txtEquipo = new JTextField();
        txtEquipo.setVisible(false);
        add(txtEquipo);

        // --- 1. Entrada de Fecha (Mantenimiento) y Hora ---
        JLabel lblFecha = new JLabel("Seleccione la fecha del mantenimiento");
        lblFecha.setBounds(40, 75, 400, 20);
        add(lblFecha);

        selectorFecha = new JDateChooser();
        selectorFecha.setDateFormatString("yyyy-MM-dd");
        selectorFecha.setDate(new Date()); 
        selectorFecha.setBounds(40, 98, 200, 30);
        add(selectorFecha);

        JLabel lblHora = new JLabel("Hora (HH:mm)");
        lblHora.setBounds(260, 75, 120, 20);
        add(lblHora);

        SpinnerDateModel modeloHora = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        spinnerHora = new JSpinner(modeloHora);
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(editorHora);
        spinnerHora.setBounds(260, 98, 130, 30);
        add(spinnerHora);

        // --- 2. NUEVA SECCIÓN: FECHAS DE PROMOCIÓN (JCalendar) ---
        JLabel lblSeccionPromo = new JLabel("Vigencia de la Promoción");
        lblSeccionPromo.setFont(new Font("Arial", Font.BOLD, 13));
        lblSeccionPromo.setBounds(40, 145, 300, 20);
        add(lblSeccionPromo);

        // Fecha Inicio Promoción
        JLabel lblFechaInicio = new JLabel("Inicio de promoción:");
        lblFechaInicio.setBounds(40, 170, 150, 20);
        add(lblFechaInicio);

        fechaInicioPromocion = new JDateChooser();
        fechaInicioPromocion.setDateFormatString("yyyy-MM-dd");
        fechaInicioPromocion.setDate(new Date()); // Fecha por defecto: Hoy
        fechaInicioPromocion.setBounds(40, 193, 170, 30);
        add(fechaInicioPromocion);

        // Fecha Fin Promoción
        JLabel lblFechaFin = new JLabel("Fin de promoción:");
        lblFechaFin.setBounds(230, 170, 150, 20);
        add(lblFechaFin);

        fechaFinPromocion = new JDateChooser();
        fechaFinPromocion.setDateFormatString("yyyy-MM-dd");
        fechaFinPromocion.setDate(new Date()); // Fecha por defecto: Hoy
        fechaFinPromocion.setBounds(230, 193, 170, 30);
        add(fechaFinPromocion);

        // --- 3. Tipo de Mantenimiento ---
        JLabel lblTipo = new JLabel("Tipo de mantenimiento");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTipo.setBounds(40, 240, 200, 20);
        add(lblTipo);

        JLabel lblSubTipo = new JLabel("Seleccione el tipo de mantenimiento");
        lblSubTipo.setForeground(Color.GRAY);
        lblSubTipo.setBounds(40, 260, 300, 20);
        add(lblSubTipo);

        String[] opciones = {"Seleccione su tipo de mantenimiento", "Preventivo", "Correctivo"};
        cbTipoMantenimiento = new JComboBox<>(opciones);
        cbTipoMantenimiento.setBounds(40, 283, 350, 30);
        add(cbTipoMantenimiento);

        // --- 4. Falla o Problema ---
        JLabel lblFalla = new JLabel("Falla o problema que tiene la herramienta");
        lblFalla.setFont(new Font("Arial", Font.BOLD, 13));
        lblFalla.setBounds(40, 330, 400, 20);
        add(lblFalla);

        txtFallaProblema = new JTextField();
        txtFallaProblema.setBounds(40, 353, 350, 35);
        add(txtFallaProblema);

        // --- 5. Observaciones ---
        JLabel lblObs = new JLabel("Observaciones");
        lblObs.setFont(new Font("Arial", Font.BOLD, 13));
        lblObs.setBounds(40, 405, 200, 20);
        add(lblObs);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(40, 428, 350, 35);
        add(txtObservaciones);

        // --- 6. Botón de Adjuntar Foto ---
        btnImagen = new JButton("Imagen del equipo");
        btnImagen.setBounds(40, 485, 160, 30);
        add(btnImagen);

        lblNombreImagen = new JLabel("Ninguna imagen seleccionada");
        lblNombreImagen.setForeground(Color.DARK_GRAY);
        lblNombreImagen.setBounds(210, 490, 380, 20);
        add(lblNombreImagen);

        btnImagen.addActionListener(e -> abrirExploradorArchivos());

        // --- 7. Botón Guardar ---
        btnGuardarMantenimiento = new JButton("Guardar");
        btnGuardarMantenimiento.setBackground(new Color(255, 215, 0));
        btnGuardarMantenimiento.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardarMantenimiento.setBounds(40, 550, 150, 40);
        add(btnGuardarMantenimiento);
    }

    private void abrirExploradorArchivos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen de evidencia");
        
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de Imagen (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filtro);

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoImagenSeleccionado = fileChooser.getSelectedFile();
            lblNombreImagen.setText(archivoImagenSeleccionado.getName());
            lblNombreImagen.setForeground(new Color(0, 128, 0)); 
        }
    }

    public File getArchivoImagenSeleccionado() {
        return archivoImagenSeleccionado;
    }
}
