/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
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
    public JDateChooser fechaProgramacion; // Fecha de registro/agenda
    public JSpinner spinnerHora;       

    public JComboBox<String> cbTipoMantenimiento;
    public JTextField txtFallaProblema, txtObservaciones;
    public JButton btnImagen, btnGuardarMantenimiento, btnVolver;
    public JLabel lblNombreImagen;

    // Archivo de imagen seleccionado
    private File archivoImagenSeleccionado;

    // Campo oculto para el equipo/herramienta
    public JTextField txtEquipo;

    public VistaProgramarMantenimiento() {
        // Configuración dinámica del Panel
        setBackground(Color.WHITE);
        setLayout(null);
        setPreferredSize(new Dimension(650, 600));

        // --- Botón Volver ---
        btnVolver = new JButton("< Volver");
        btnVolver.setBounds(500, 20, 90, 25);
        add(btnVolver);

        // --- Título ---
        JLabel lblTitulo = new JLabel("Programación de Mantenimiento");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBounds(40, 20, 400, 30);
        add(lblTitulo);

        // --- Campo invisible de control ---
        txtEquipo = new JTextField();
        txtEquipo.setVisible(false);
        add(txtEquipo);

        // --- 1. SECCIÓN DE FECHA PROGRAMACIÓN Y HORA ---
        
        // Fecha Programación
        JLabel lblFechaProg = new JLabel("Fecha Programación:");
        lblFechaProg.setFont(new Font("Arial", Font.BOLD, 13));
        lblFechaProg.setBounds(40, 70, 160, 20);
        add(lblFechaProg);

        fechaProgramacion = new JDateChooser();
        fechaProgramacion.setDateFormatString("yyyy-MM-dd");
        fechaProgramacion.setDate(new Date()); 
        fechaProgramacion.setBounds(40, 93, 160, 30);
        add(fechaProgramacion);

        // Hora
        JLabel lblHora = new JLabel("Hora (HH:mm):");
        lblHora.setFont(new Font("Arial", Font.BOLD, 13));
        lblHora.setBounds(220, 70, 110, 20);
        add(lblHora);

        SpinnerDateModel modeloHora = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        spinnerHora = new JSpinner(modeloHora);
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(editorHora);
        spinnerHora.setBounds(220, 93, 110, 30);
        add(spinnerHora);

        // --- 2. Tipo de Mantenimiento ---
        JLabel lblTipo = new JLabel("Tipo de mantenimiento");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTipo.setBounds(40, 145, 200, 20);
        add(lblTipo);

        String[] opciones = {"Seleccione su tipo de mantenimiento", "Preventivo", "Correctivo"};
        cbTipoMantenimiento = new JComboBox<>(opciones);
        cbTipoMantenimiento.setBounds(40, 168, 320, 30);
        add(cbTipoMantenimiento);

        // --- 3. Falla o Problema ---
        JLabel lblFalla = new JLabel("Falla o problema que tiene la herramienta");
        lblFalla.setFont(new Font("Arial", Font.BOLD, 13));
        lblFalla.setBounds(40, 215, 400, 20);
        add(lblFalla);

        txtFallaProblema = new JTextField();
        txtFallaProblema.setBounds(40, 238, 320, 35);
        add(txtFallaProblema);

        // --- 4. Observaciones ---
        JLabel lblObs = new JLabel("Observaciones");
        lblObs.setFont(new Font("Arial", Font.BOLD, 13));
        lblObs.setBounds(40, 290, 200, 20);
        add(lblObs);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(40, 313, 320, 35);
        add(txtObservaciones);

        // --- 5. Botón Adjuntar Imagen ---
        btnImagen = new JButton("Imagen del equipo");
        btnImagen.setBounds(40, 370, 160, 30);
        add(btnImagen);

        lblNombreImagen = new JLabel("Ninguna imagen seleccionada");
        lblNombreImagen.setForeground(Color.DARK_GRAY);
        lblNombreImagen.setBounds(210, 375, 380, 20);
        add(lblNombreImagen);

        btnImagen.addActionListener(e -> abrirExploradorArchivos());

        // --- 6. Botón Guardar ---
        btnGuardarMantenimiento = new JButton("Guardar");
        btnGuardarMantenimiento.setBackground(new Color(255, 215, 0));
        btnGuardarMantenimiento.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardarMantenimiento.setBounds(40, 430, 150, 40);
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
