/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.Frame;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
import nexusgo.view.VistaChecklistHerramientas;

/**
 *
 * @author USUARIO
 */
public class ControladorChecklistHerramientas {

    private final VistaChecklistHerramientas vista;
    private final HerramientaDao herramientaDao;
    private final int idCita;

    public ControladorChecklistHerramientas(Frame owner, int idCita, String infoCita) {
        this.idCita = idCita;
        this.herramientaDao = new HerramientaDao();
        this.vista = new VistaChecklistHerramientas(owner, infoCita);

        inicializarListeners();
        cargarHerramientasDisponibles();
    }

    private void inicializarListeners() {
        vista.getBtnGuardar().addActionListener(e -> guardarChecklist());
        vista.getBtnCancelar().addActionListener(e -> vista.dispose());
    }

    private void cargarHerramientasDisponibles() {
        SwingWorker<List<Herramientas>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Herramientas> doInBackground() {
                return herramientaDao.listar();
            }

            @Override
            protected void done() {
                try {
                    vista.cargarHerramientas(get());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista,
                            "No se pudo cargar el inventario de herramientas: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void guardarChecklist() {
        List<Integer> seleccionadas = vista.getIdsHerramientasSeleccionadas();

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return herramientaDao.reservarHerramientasParaCita(idCita, seleccionadas);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(vista,
                                seleccionadas.isEmpty()
                                ? "No se reservó ninguna herramienta para esta cita."
                                : "Herramientas reservadas correctamente. Quedan en estado OCUPADA.",
                                "NexusGO", JOptionPane.INFORMATION_MESSAGE);
                        vista.dispose();
                    } else {
                        JOptionPane.showMessageDialog(vista,
                                "No se pudieron guardar las herramientas seleccionadas.",
                                "Error de Almacenamiento", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista,
                            "Error al guardar el checklist: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void mostrar() {
        vista.setVisible(true);
    }

}
