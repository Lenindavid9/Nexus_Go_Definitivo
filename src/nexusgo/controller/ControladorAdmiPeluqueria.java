/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

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

    // Instancias de DAOs existentes para consultas
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();

    private final Usuario usuarioLogueado;

    public ControladorAdmiPeluqueria(PanelAdmi vistaAdmin, Usuario usuarioLogueado) {
        this.vistaAdmin = vistaAdmin;
        this.usuarioLogueado = usuarioLogueado;

        try {
            // Inicialización de la vista secundaria del módulo
            this.panelReportes = new ReportesFinancieros();

            inicializarListeners();

            // Cargar por defecto el panel de bienvenida al iniciar
            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico al inicializar el módulo de Administrador: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // Clic en la tarjeta central "Gestión de Reportes Financieros" (Acceso Rápido)
            this.vistaAdmin.getPnlTarjeta().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cambiarPanelCentral(panelReportes);
                }
            });

            // Listeners para los botones de la vista ReportesFinancieros
            this.panelReportes.getBtnInicio().addActionListener(this);
            this.panelReportes.getBtnProcesar().addActionListener(this);
            this.panelReportes.getBtnCerrar().addActionListener(this);
            this.panelReportes.getBtnHistorialMH().addActionListener(this);

            // Listener de cerrar sesión en la ventana principal si está presente
            if (this.vistaAdmin.getBtnCerrar() != null) {
                this.vistaAdmin.getBtnCerrar().addActionListener(this);
            }

        } catch (NullPointerException npe) {
            System.err.println("Error al enlazar los listeners del Administrador: " + npe.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // --- NAVEGACIÓN ---
            
            // Botón Inicio -> Regresar a la pantalla de bienvenida
            if (e.getSource() == panelReportes.getBtnInicio()) {
                cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
            }

            // Botón Historial Mantenimiento Herramientas
            if (e.getSource() == panelReportes.getBtnHistorialMH()) {
                mostrarHistorialMantenimiento();
            }

            // --- ACCIONES PRINCIPALES ---

            // Botón Procesar Reporte
            if (e.getSource() == panelReportes.getBtnProcesar()) {
                ejecutarProcesamientoReporte();
            }

            // Cerrar Sesión
            if (e.getSource() == panelReportes.getBtnCerrar() || e.getSource() == vistaAdmin.getBtnCerrar()) {
                ejecutarCierreSesion();
            }

        } catch (Exception ex) {
            System.err.println("Error en enrutamiento de eventos: " + ex.getMessage());
        }
    }

    private void ejecutarProcesamientoReporte() {
        try {
            String mesSeleccionado = panelReportes.getComboMes().getSelectedItem().toString();
            String anioSeleccionado = panelReportes.getComboAnio().getSelectedItem().toString();

            DefaultTableModel modelo = panelReportes.getModeloTabla();
            modelo.setRowCount(0); // Limpia datos previos

            // Llenado de fila consolidada (simulando totales provenientes de la lógica de negocio/DAOs)
            modelo.addRow(new Object[]{
                "$ 365,000",        // Suma Servicios / Productos
                "$ 12,000",         // Suma Promociones / Descuentos
                "$ 353,000",        // Resta Descuentos al Total (Neto)
                "Balayage (3)"      // Servicio más solicitado del mes
            });

            JOptionPane.showMessageDialog(panelReportes,
                    "Reporte Mensual Generado Correctamente para " + mesSeleccionado + " " + anioSeleccionado + ".",
                    "NEXUS GO - Reporte Financiero", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelReportes,
                    "Error al procesar el reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarHistorialMantenimiento() {
        try {
            List<Herramientas> lista = herramientaDao.listar();
            if (lista != null && !lista.isEmpty()) {
                JOptionPane.showMessageDialog(panelReportes,
                        "Cargando historial de " + lista.size() + " herramientas registradas.",
                        "Historial Mantenimiento", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelReportes,
                        "No se encontraron herramientas o mantenimientos en el sistema.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar herramientas: " + e.getMessage());
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
            // Aquí se redirige al Login según el flujo del proyecto
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {
        try {
            // Reemplaza el panel central del JFrame principal
            vistaAdmin.setContentPane(panelNuevo);
            vistaAdmin.revalidate();
            vistaAdmin.repaint();
        } catch (Exception e) {
            System.err.println("Error en el enrutador dinámico de vistas: " + e.getMessage());
        }
    }



}
