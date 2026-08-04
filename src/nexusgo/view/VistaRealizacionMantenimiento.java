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
public class VistaRealizacionMantenimiento extends JPanel {

    public JLabel lblTitulo;
    public JButton btnVolver;

    public JComboBox<String> cbHerramientas;
    public JTextField txtDescripcionTrabajo;

    public JButton btnFotoDespues;
    public JLabel lblPreviewDespues;

    public JComboBox<String> cbHorasInvertidas;
    public JTextField txtObservaciones;
    public JButton btnGuardar;

    private File archivoImagenDespues;
    private List<Herramientas> listaHerramientasActuales;

    private final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaRealizacionMantenimiento() {

        this.setBounds(150, 100, 800, 600);
        this.setBackground(Color.WHITE);
        this.setLayout(null);

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

        btnFotoDespues = crearBotonAdjuntar("Imagen de después del mantenimiento (obligatoria)");
        btnFotoDespues.setBounds(255, 192, 295, 32);
        this.add(btnFotoDespues);
        
        //Cuadro de foto previa 

        lblPreviewDespues = new JLabel("Sin foto", SwingConstants.CENTER);
        lblPreviewDespues.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblPreviewDespues.setForeground(Color.GRAY);
        lblPreviewDespues.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        lblPreviewDespues.setBounds(373, 230, 55, 55);
        this.add(lblPreviewDespues);

        JLabel lblHoras = new JLabel("Horas invertidas en el mantenimiento:");
        lblHoras.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHoras.setForeground(Color.BLACK);
        lblHoras.setBounds(240, 305, 320, 20);
        this.add(lblHoras);

        String[] opcionesHoras = {
            "-- Seleccione las horas --",
            "0.5", "1.0", "1.5", "2.0", "2.5", "3.0",
            "3.5", "4.0", "4.5", "5.0", "6.0", "8.0", "10.0"
        };

        cbHorasInvertidas = new JComboBox<>(opcionesHoras);
        cbHorasInvertidas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbHorasInvertidas.setBackground(Color.WHITE);
        cbHorasInvertidas.setBounds(190, 328, 420, 32);
        this.add(cbHorasInvertidas);

        JLabel lblObs = new JLabel("Observaciones:");
        lblObs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblObs.setForeground(Color.BLACK);
        lblObs.setBounds(240, 368, 320, 20);
        this.add(lblObs);

        txtObservaciones = new JTextField();
        txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservaciones.setBorder(new LineBorder(new Color(210, 210, 210), 1, true));
        txtObservaciones.setBounds(190, 391, 420, 35);
        this.add(txtObservaciones);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(COLOR_DORADO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setBounds(310, 450, 180, 42);
        this.add(btnGuardar);

        btnFotoDespues.addActionListener(e -> seleccionarImagenDespues());
    }

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

    public void seleccionarHerramientaPorId(int idHerramienta) {
        if (listaHerramientasActuales == null) {
            return;
        }
        for (int i = 0; i < listaHerramientasActuales.size(); i++) {
            if (listaHerramientasActuales.get(i).getIdHerramienta() == idHerramienta) {
                cbHerramientas.setSelectedIndex(i + 1);
                break;
            }
        }
    }

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
            // Image.SCALE_SMOOTH asegura que la imagen no pierda calidad visual al reducirse
            );
            lblContenedor.setText("");
            lblContenedor.setIcon(new ImageIcon(imgEscalada));
        } catch (Exception e) {
            lblContenedor.setText("Error");
        }
    }

    public File getArchivoImagenDespues() {
        return archivoImagenDespues;
    }

    public void limpiarFormulario() {
        if (cbHerramientas.getItemCount() > 0) {
            cbHerramientas.setSelectedIndex(0);
        }
        txtDescripcionTrabajo.setText("");
        cbHorasInvertidas.setSelectedIndex(0);
        txtObservaciones.setText("");
        this.archivoImagenDespues = null;
        lblPreviewDespues.setIcon(null);
        lblPreviewDespues.setText("Sin foto");
    }
}
