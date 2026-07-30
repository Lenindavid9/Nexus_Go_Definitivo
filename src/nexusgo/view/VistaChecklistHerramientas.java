/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import nexusgo.model.Herramientas;

/**
 *
 * @author USUARIO
 */
public class VistaChecklistHerramientas extends JDialog {
    
    private final JPanel panelLista;
    private final JButton btnGuardar;
    private final JButton btnCancelar;
    private final List<JCheckBox> checkboxes = new ArrayList<>();
    private final List<Herramientas> herramientas = new ArrayList<>();

    private static final Color COLOR_DORADO = new Color(184, 134, 11);

    public VistaChecklistHerramientas(Frame owner, String infoCita) {
        super(owner, "Herramientas para la cita", true);
        setSize(420, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        // Encabezado
        JPanel panelNorte = new JPanel(new BorderLayout());
        
        JLabel lblTitulo = new JLabel("Selecciona las herramientas a usar", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(15, 10, 5, 10));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        if (infoCita != null && !infoCita.isBlank()) {
            JLabel lblCita = new JLabel("<html><center>" + infoCita + "</center></html>", SwingConstants.CENTER);
            lblCita.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblCita.setForeground(Color.GRAY);
            lblCita.setBorder(new EmptyBorder(0, 10, 10, 10));
            panelNorte.add(lblCita, BorderLayout.CENTER);
        }
        add(panelNorte, BorderLayout.NORTH);

        // Lista dinámica de ítems
        panelLista = new JPanel(new GridLayout(0, 1, 5, 8));
        panelLista.setBorder(new EmptyBorder(10, 20, 10, 20));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll, BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setBorder(new EmptyBorder(10, 10, 15, 10));

        btnCancelar = new JButton("Omitir");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelBotones.add(btnCancelar);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(COLOR_DORADO);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        panelBotones.add(btnGuardar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga dinámicamente la lista de herramientas disponibles como checkboxes.
     * Las herramientas ocupadas se despliegan deshabilitadas.
     */
    public void cargarHerramientas(List<Herramientas> lista) {
        panelLista.removeAll();
        checkboxes.clear();
        herramientas.clear();

        if (lista == null || lista.isEmpty()) {
            panelLista.add(new JLabel("No hay herramientas registradas en el inventario."));
        } else {
            for (Herramientas h : lista) {
                boolean ocupada = "OCUPADA".equalsIgnoreCase(h.getDisponibilidad());
                String etiqueta = h.getNombreHerramienta() + "  [" + h.getEstadoActual() + "]"
                        + (ocupada ? "  - Ocupada" : "");

                JCheckBox cb = new JCheckBox(etiqueta);
                cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                cb.setEnabled(!ocupada);
                cb.setOpaque(false);

                checkboxes.add(cb);
                herramientas.add(h);
                panelLista.add(cb);
            }
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    /**
     * Devuelve los IDs de las herramientas seleccionadas.
     */
    public List<Integer> getIdsHerramientasSeleccionadas() {
        List<Integer> seleccionadas = new ArrayList<>();
        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected()) {
                seleccionadas.add(herramientas.get(i).getIdHerramienta());
            }
        }
        return seleccionadas;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
    
}
