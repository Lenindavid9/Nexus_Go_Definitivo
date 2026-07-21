/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.HerramientaDao;
import nexusgo.model.Herramientas;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.view.PanelAdmi;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.ReportesFinancieros;

/**
 *
 * @author INGRID
 */
public class ControladorAdmiPeluqueria implements ActionListener {
    
   private final PanelAdmi vistaAdmin;
    private ReportesFinancieros panelReportes;

    // Instancias de DAOs existentes para consultas reales a la BD
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();

    private final Usuario usuarioLogueado;

    public ControladorAdmiPeluqueria(PanelAdmi vistaAdmin, Usuario usuarioLogueado) {
        this.vistaAdmin = vistaAdmin;
        this.usuarioLogueado = usuarioLogueado;

        try {
            this.panelReportes = new ReportesFinancieros();

            inicializarListeners();

            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Administrador: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Clic en la tarjeta central "Gestión de Reportes Financieros"
            if (this.vistaAdmin.getPnlTarjeta() != null) {
                this.vistaAdmin.getPnlTarjeta().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cambiarPanelCentral(panelReportes);
                    }
                });
            }

            // LISTENERS DIRECTOS A LOS BOTONES DE TU VISTABARRALATERAL
            if (this.vistaAdmin.getMenuLateral() != null) {
                if (this.vistaAdmin.getMenuLateral().bCasa != null) {
                    this.vistaAdmin.getMenuLateral().bCasa.addActionListener(this);
                }
                if (this.vistaAdmin.getMenuLateral().bInventario != null) {
                    this.vistaAdmin.getMenuLateral().bInventario.addActionListener(this);
                }
                if (this.vistaAdmin.getMenuLateral().misCitas != null) {
                    this.vistaAdmin.getMenuLateral().misCitas.addActionListener(this);
                }
            }

            // LISTENER PARA EL BOTÓN REPORTE DE PANELADMI
            if (this.vistaAdmin.getBtnReporte() != null) {
                this.vistaAdmin.getBtnReporte().addActionListener(this);
            }

            // Listeners para los botones de la vista ReportesFinancieros
            if (this.panelReportes != null) {
                this.panelReportes.getBtnInicio().addActionListener(this);
                this.panelReportes.getBtnProcesar().addActionListener(this);
                this.panelReportes.getBtnCerrar().addActionListener(this);
                this.panelReportes.getBtnHistorialMH().addActionListener(this);
            }

            if (this.vistaAdmin.getBtnCerrar() != null) {
                this.vistaAdmin.getBtnCerrar().addActionListener(this);
            }

        } catch (Exception e) {
            System.err.println("Error al enlazar los listeners del Administrador: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // --- NAVEGACIÓN BARRA LATERAL ---
            if (this.vistaAdmin.getMenuLateral() != null) {
                // Botón bCasa -> Inicio
                if (e.getSource() == vistaAdmin.getMenuLateral().bCasa) {
                    cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
                    return;
                }
            }

            // Botón btnReporte -> Abre el panel de Reportes Financieros
            if (e.getSource() == vistaAdmin.getBtnReporte()) {
                cambiarPanelCentral(panelReportes);
                return;
            }

            // --- NAVEGACIÓN Y ACCIONES DESDE REPORTES ---
            if (e.getSource() == panelReportes.getBtnInicio()) {
                cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
            }

            if (e.getSource() == panelReportes.getBtnHistorialMH()) {
                mostrarHistorialMantenimiento();
            }

            if (e.getSource() == panelReportes.getBtnProcesar()) {
                ejecutarProcesamientoReporte();
            }

            if (e.getSource() == panelReportes.getBtnCerrar() || e.getSource() == vistaAdmin.getBtnCerrar()) {
                ejecutarCierreSesion();
            }

        } catch (Exception ex) {
            System.err.println("Error en enrutamiento de eventos: " + ex.getMessage());
        }
    }

    // Consulta de datos reales usando la base de datos
    private void ejecutarProcesamientoReporte() {
        try {
            String mesSeleccionado = panelReportes.getComboMes().getSelectedItem().toString();
            String anioSeleccionado = panelReportes.getComboAnio().getSelectedItem().toString();

            DefaultTableModel modelo = panelReportes.getModeloTabla();
            modelo.setRowCount(0);

            List<Herramientas> listaHerramientas = herramientaDao.listar();

            if (listaHerramientas != null && !listaHerramientas.isEmpty()) {
                for (Herramientas h : listaHerramientas) {
                    modelo.addRow(new Object[]{
                        h.getIdHerramienta(),
                        h.getNombreHerramienta(),
                        h.getEstadoActual(),
                        "Registro BD"
                    });
                }
                JOptionPane.showMessageDialog(panelReportes,
                        "Reporte procesado con datos de la BD para " + mesSeleccionado + " " + anioSeleccionado + ".",
                        "NEXUS GO - Reporte Financiero", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelReportes,
                        "No se encontraron registros en la base de datos para el período seleccionado.",
                        "Sin Datos", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelReportes,
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarHistorialMantenimiento() {
        try {
            List<Herramientas> lista = herramientaDao.listar();
            if (lista != null && !lista.isEmpty()) {
                JOptionPane.showMessageDialog(panelReportes,
                        "Se encontraron " + lista.size() + " herramientas registradas en la base de datos.",
                        "Historial Mantenimiento", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelReportes,
                        "No se encontraron herramientas o mantenimientos en el sistema.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelReportes,
                    "Error al consultar la BD: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarCierreSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vistaAdmin,
                "¿Está seguro de que desea cerrar la sesión actual?",
                "NEXUS GO - Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            vistaAdmin.dispose();
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        try {
            BorderLayout layout = (BorderLayout) vistaAdmin.getContentPane().getLayout();
            if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                vistaAdmin.getContentPane().remove(layout.getLayoutComponent(BorderLayout.CENTER));
            }
            vistaAdmin.getContentPane().add(panelNuevo, BorderLayout.CENTER);
            vistaAdmin.revalidate();
            vistaAdmin.repaint();
        } catch (Exception e) {
            System.err.println("Error en el enrutador dinámico de vistas: " + e.getMessage());
        }
    }

    
}
