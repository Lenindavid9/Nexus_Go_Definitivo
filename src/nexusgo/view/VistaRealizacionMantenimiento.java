/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.Herramientas;

/**
 *
 * @author USUARIO
 */
public class VistaRealizacionMantenimiento extends JPanel{
    
   // Componentes de interfaz
    public JLabel lblTitulo;
    public JButton btnVolver;

    // Cambiado de JTextField a JComboBox para evitar errores de tipeo
    public JComboBox<String> cbHerramientas;
    public JTextField txtDescripcionTrabajo; // Campo para detallar la labor realizada

    public JButton btnFotoAntes;
    public JLabel lblPreviewAntes;

    public JButton btnFotoDespues;
    public JLabel lblPreviewDespues;

    public JTextField txtHorasInvertidas;
    public JTextField txtObservaciones;
    public JButton btnGuardar;

    // Referencias a archivos
    private File archivoImagenAntes;
    private File archivoImagenDespues;
    private List<Herramientas> listaHerramientasActuales;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaRealizacionMantenimiento() {


        this.setBounds(150, 100, 800, 600);
        this.setBackground(Color.WHITE);
        this.setLayout(null);

        // --- ENCABEZADO ---
        lblTitulo = new JLabel("Realización del mantenimiento", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBounds(0, 20, 800, 35);
        this.add(lblTitulo);


        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setBounds(520, 25, 150, 25);
        this.add(btnVolver);

        // --- CAMPO 1: SELECCIÓN DE HERRAMIENTA (JComboBox) ---
        JLabel lblHerramienta = new JLabel("Seleccione la herramienta:");
        lblHerramienta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHerramienta.setForeground(Color.BLACK);
        lblHerramienta.setBounds(240, 65, 320, 20);
        this.add(lblHerramienta);

        cbHerramientas = new JComboBox<>();
        cbHerramientas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbHerramientas.setBackground(Color.WHITE);
        cbHerramientas.setBounds(190, 88, 420, 32);
        this.add(cbHerramientas);

        // --- CAMPO 2: DESCRIPCIÓN DEL TRABAJO REALIZADO ---
        JLabel lblDescripcion = new JLabel("Descripción del trabajo realizado:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDescripcion.setForeground(Color.BLACK);
        lblDescripcion.setBounds(240, 125, 320, 20);
        this.add(lblDescripcion);

        txtDescripcionTrabajo = new JTextField();
        txtDescripcionTrabajo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescripcionTrabajo.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));
        txtDescripcionTrabajo.setBounds(190, 148, 420, 32);
        this.add(txtDescripcionTrabajo);

        // --- CAMPO 3: IMAGEN ANTES ---
        btnFotoAntes = crearBotonAdjuntar("Imagen de antes del mantenimiento");
        btnFotoAntes.setBounds(285, 192, 230, 32);
        this.add(btnFotoAntes);


        // Recuadro Preview Antes
        lblPreviewAntes = new JLabel("Sin foto", SwingConstants.CENTER);
        lblPreviewAntes.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblPreviewAntes.setForeground(Color.GRAY);
        lblPreviewAntes.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        lblPreviewAntes.setBounds(373, 230, 55, 55);
        this.add(lblPreviewAntes);

        // --- CAMPO 4: IMAGEN DESPUÉS ---
        btnFotoDespues = crearBotonAdjuntar("Imagen de después del mantenimiento");
        btnFotoDespues.setBounds(278, 292, 245, 32);
        this.add(btnFotoDespues);

        // Recuadro Preview Después
        lblPreviewDespues = new JLabel("Sin foto", SwingConstants.CENTER);
        lblPreviewDespues.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblPreviewDespues.setForeground(Color.GRAY);
        lblPreviewDespues.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        lblPreviewDespues.setBounds(373, 330, 55, 55);
        this.add(lblPreviewDespues);

        // --- CAMPO 5: HORAS INVERTIDAS ---
        JLabel lblHoras = new JLabel("Horas invertidas en el mantenimiento:");
        lblHoras.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHoras.setForeground(Color.BLACK);
        lblHoras.setBounds(240, 365, 320, 20);
        this.add(lblHoras);

        txtHorasInvertidas = new JTextField();
        txtHorasInvertidas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtHorasInvertidas.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));
        txtHorasInvertidas.setBounds(190, 388, 420, 32);
        this.add(txtHorasInvertidas);

        // --- CAMPO 6: OBSERVACIONES ---
        JLabel lblObs = new JLabel("Observaciones:");
        lblObs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblObs.setForeground(Color.BLACK);
        lblObs.setBounds(240, 428, 320, 20);
        this.add(lblObs);

        txtObservaciones = new JTextField();
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservaciones.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));
        txtObservaciones.setBounds(190, 451, 420, 35);
        this.add(txtObservaciones);

        // --- BOTÓN GUARDAR ---
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(COLOR_DORADO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setBounds(310, 500, 180, 42);
        this.add(btnGuardar);

        // Listeners para selección de archivos
        btnFotoAntes.addActionListener(e -> seleccionarImagenAntes());
        btnFotoDespues.addActionListener(e -> seleccionarImagenDespues());
    }

    /**
     * Carga dinámicamente las herramientas registradas en la base de datos dentro del JComboBox.
     */
    public void cargarHerramientas(List<Herramientas> lista) {
        this.listaHerramientasActuales = lista;
        cbHerramientas.removeAllItems();
        cbHerramientas.addItem("-- Seleccione una herramienta --");

        if (lista != null) {
            for (Herramientas h : lista) {
                cbHerramientas.addItem(h.getIdHerramienta() + " - " + h.getNombreHerramienta() + " [" + h.getEstadoActual() + "]");
            }
        }
    }

    /**
     * Selecciona automáticamente un ítem en el ComboBox según la herramienta elegida previamente.
     */
    public void seleccionarHerramientaPorId(int idHerramienta) {
        if (listaHerramientasActuales == null) return;
        for (int i = 0; i < listaHerramientasActuales.size(); i++) {
            if (listaHerramientasActuales.get(i).getIdHerramienta() == idHerramienta) {
                cbHerramientas.setSelectedIndex(i + 1); // +1 por el ítem por defecto
                break;
            }
        }
    }

    /**
     * Obtiene el objeto Herramientas seleccionado en el ComboBox.
     */
    public Herramientas getHerramientaSeleccionada() {
        int index = cbHerramientas.getSelectedIndex();
        if (index > 0 && listaHerramientasActuales != null && (index - 1) < listaHerramientasActuales.size()) {
            return listaHerramientasActuales.get(index - 1);
        }
        return null;
    }

    private JButton crearBotonAdjuntar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(COLOR_DORADO);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(COLOR_DORADO, 1));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void seleccionarImagenAntes() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar Imagen Antes del Mantenimiento");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes PNG & JPG", "png", "jpg", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoImagenAntes = chooser.getSelectedFile();
            mostrarVistaPrevia(archivoImagenAntes, lblPreviewAntes);
        }
    }

    private void seleccionarImagenDespues() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar Imagen Después del Mantenimiento");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes PNG & JPG", "png", "jpg", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoImagenDespues = chooser.getSelectedFile();
            mostrarVistaPrevia(archivoImagenDespues, lblPreviewDespues);
        }
    }

    private void mostrarVistaPrevia(File archivo, JLabel lblContenedor) {
        try {
            ImageIcon icon = new ImageIcon(archivo.getAbsolutePath());
            Image imgEscalada = icon.getImage().getScaledInstance(
                    lblContenedor.getWidth(),
                    lblContenedor.getHeight(),
                    Image.SCALE_SMOOTH
            );
            lblContenedor.setText("");
            lblContenedor.setIcon(new ImageIcon(imgEscalada));
        } catch (Exception e) {
            lblContenedor.setText("Error");
        }
    }

    public File getArchivoImagenAntes() {
        return archivoImagenAntes;
    }

    public File getArchivoImagenDespues() {
        return archivoImagenDespues;
    }
}
