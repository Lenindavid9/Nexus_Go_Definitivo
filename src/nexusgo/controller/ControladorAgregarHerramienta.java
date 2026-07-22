/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.HerramientaDao;
import nexusgo.model.Herramientas;
import nexusgo.view.VistaAgregarHerramienta;;

/**
 *
 * @author USUARIO
 */
public class ControladorAgregarHerramienta implements ActionListener {

    private final VistaAgregarHerramienta panelFormularioHerramienta;
    private final JPanel contenedorCentral;
    private final JPanel panelInventarioPadre;
    private final HerramientaDao herramientaDao;

    private int idSeleccionado = -1;
    private final Runnable alVolverCallback; // Callback para refrescar la tabla al regresar

    // Constructor para REGISTRAR una nueva herramienta
    public ControladorAgregarHerramienta(VistaAgregarHerramienta panelFormularioHerramienta, JPanel contenedorCentral, JPanel panelInventarioPadre, Runnable alVolverCallback) {
        this.panelFormularioHerramienta = panelFormularioHerramienta;
        this.contenedorCentral = contenedorCentral;
        this.panelInventarioPadre = panelInventarioPadre;
        this.alVolverCallback = alVolverCallback;
        this.herramientaDao = new HerramientaDao();

        limpiarCamposFormularioHerramienta();
        this.panelFormularioHerramienta.btnEditar.setText("Guardar");

        inicializarListeners();
    }

    // Constructor para EDITAR una herramienta existente
    public ControladorAgregarHerramienta(VistaAgregarHerramienta panelFormularioHerramienta, JPanel contenedorCentral, JPanel panelInventarioPadre, int idHerramienta, String nombreHerramienta, Runnable alVolverCallback) {
        this.panelFormularioHerramienta = panelFormularioHerramienta;
        this.contenedorCentral = contenedorCentral;
        this.panelInventarioPadre = panelInventarioPadre;
        this.idSeleccionado = idHerramienta;
        this.alVolverCallback = alVolverCallback;
        this.herramientaDao = new HerramientaDao();

        // Cargar datos a editar
        this.panelFormularioHerramienta.txtIdHerramienta.setText(String.valueOf(idHerramienta));
        this.panelFormularioHerramienta.txtIdHerramienta.setEditable(false);
        this.panelFormularioHerramienta.txtNombre.setText(nombreHerramienta);
        this.panelFormularioHerramienta.btnEditar.setText("Editar");

        inicializarListeners();
    }

    private void inicializarListeners() {
        if (panelFormularioHerramienta.btnVolver != null) {
            panelFormularioHerramienta.btnVolver.addActionListener(this);
        }
        if (panelFormularioHerramienta.btnEditar != null) {
            panelFormularioHerramienta.btnEditar.addActionListener(this);
        }
        if (panelFormularioHerramienta.btnImagen != null) {
            panelFormularioHerramienta.btnImagen.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == panelFormularioHerramienta.btnImagen) {
            buscarYCopiarImagen();
        } else if (src == panelFormularioHerramienta.btnEditar) {
            if ("Guardar".equals(panelFormularioHerramienta.btnEditar.getText())) {
                registrarNuevaHerramienta();
            } else {
                actualizarHerramienta();
            }
        } else if (src == panelFormularioHerramienta.btnVolver) {
            volverAlPanelPrincipal();
        }
    }

    private void registrarNuevaHerramienta() {
        try {
            Herramientas nuevaHerramienta = new Herramientas();
            nuevaHerramienta.setIdHerramienta(Integer.parseInt(panelFormularioHerramienta.txtIdHerramienta.getText().trim()));
            nuevaHerramienta.setNombreHerramienta(panelFormularioHerramienta.txtNombre.getText().trim());
            nuevaHerramienta.setEstadoActual("EXCELENTE");

            if (herramientaDao.agregar(nuevaHerramienta) > 0) {
                JOptionPane.showMessageDialog(panelFormularioHerramienta, "¡Herramienta registrada exitosamente!");
                volverAlPanelPrincipal();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormularioHerramienta, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarHerramienta() {
        try {
            Herramientas h = new Herramientas();
            h.setIdHerramienta(idSeleccionado);
            h.setNombreHerramienta(panelFormularioHerramienta.txtNombre.getText().trim());
            h.setEstadoActual("EXCELENTE");

            if (herramientaDao.editar(h) > 0) {
                JOptionPane.showMessageDialog(panelFormularioHerramienta, "¡Herramienta modificada correctamente!");
                volverAlPanelPrincipal();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelFormularioHerramienta, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarYCopiarImagen() {
        JFileChooser selector = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
        selector.setFileFilter(filtro);

        int resultado = selector.showOpenDialog(panelFormularioHerramienta);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                File archivoSeleccionado = selector.getSelectedFile();
                String nombreOriginal = archivoSeleccionado.getName();
                String nombreLimpio = System.currentTimeMillis() + "_herr_" + nombreOriginal.replaceAll("\\s+", "_");

                Path directorioDestino = Paths.get("img");
                if (!Files.exists(directorioDestino)) {
                    Files.createDirectories(directorioDestino);
                }

                Path destino = directorioDestino.resolve(nombreLimpio);
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                panelFormularioHerramienta.lblNombreImagen.setText(nombreLimpio);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelFormularioHerramienta, "Error de transferencia de archivo: " + ex.getMessage(), "Error de Imagen", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCamposFormularioHerramienta() {
        panelFormularioHerramienta.txtIdHerramienta.setText("");
        panelFormularioHerramienta.txtIdHerramienta.setEditable(true);
        panelFormularioHerramienta.txtNombre.setText("");
        panelFormularioHerramienta.lblNombreImagen.setText("ningún archivo seleccionado");
    }

    private void volverAlPanelPrincipal() {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(panelInventarioPadre, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();

            if (alVolverCallback != null) {
                alVolverCallback.run(); // Refresca la tabla de herramientas
            }
        }
    }
}
