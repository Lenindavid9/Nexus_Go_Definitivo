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
import nexusgo.model.Mantenimiento;
import nexusgo.model.MantenimientoDao;
import nexusgo.model.ProductoDao;
import nexusgo.model.Usuario;
import nexusgo.view.PanelAdmi;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.ReportesFinancieros;
import nexusgo.view.VistaHistorialMantenimiento;

/**
 *
 * @author INGRID
 */
public class ControladorPrincipalAdmiPeluqueria implements ActionListener {

    private final PanelAdmi vistaAdmin;
    private ReportesFinancieros panelReportes;
    private VistaHistorialMantenimiento panelHistorial;

    // Instancias de DAOs para la capa de datos
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private final MantenimientoDao mantenimientoDao = new MantenimientoDao();

    private final Usuario usuarioLogueado;

    public ControladorPrincipalAdmiPeluqueria(PanelAdmi vistaAdmin, Usuario usuarioLogueado) {
        this.vistaAdmin = vistaAdmin;
        this.usuarioLogueado = usuarioLogueado;

        try {
            this.panelReportes = new ReportesFinancieros();
            this.panelHistorial = new VistaHistorialMantenimiento();

            inicializarListeners();

            // Carga la pantalla inicial de bienvenida
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

            // LISTENERS A LA BARRA LATERAL
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

            // LISTENER PARA BOTÓN REPORTE
            if (this.vistaAdmin.btnReporte != null) {
                this.vistaAdmin.btnReporte.addActionListener(this);
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
                if (e.getSource() == vistaAdmin.getMenuLateral().bCasa) {
                    cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
                    return;
                }

                if (e.getSource() == vistaAdmin.getMenuLateral().bInventario) {
                    JOptionPane.showMessageDialog(vistaAdmin,
                            "Módulo de Inventario en integración.",
                            "NexusGO", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Acción de ver el Historial de Mantenimientos
                if (e.getSource() == vistaAdmin.getMenuLateral().misCitas) {
                    mostrarHistorialMantenimiento();
                    return;
                }
            }

            if (e.getSource() == vistaAdmin.btnReporte) {
                cambiarPanelCentral(panelReportes);
                return;
            }

            // --- NAVEGACIÓN DESDE REPORTES ---
            if (panelReportes != null) {
                if (e.getSource() == panelReportes.getBtnInicio()) {
                    cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
                }

                if (e.getSource() == panelReportes.getBtnHistorialMH()) {
                    mostrarHistorialMantenimiento();
                }

                if (e.getSource() == panelReportes.getBtnProcesar()) {
                    ejecutarProcesamientoReporte();
                }

                if (e.getSource() == panelReportes.getBtnCerrar()) {
                    ejecutarCierreSesion();
                }
            }

            if (e.getSource() == vistaAdmin.getBtnCerrar()) {
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

    // Consulta y llena la tabla del Historial de Mantenimientos
    private void mostrarHistorialMantenimiento() {
        try {
            // Obtenemos los registros cruzados desde HerramientaDao
            List<Mantenimiento> listaMantenimientos = herramientaDao.listarMantenimientosRealizados();

            String[] columnas = {"Número de referencia", "Nombre", "Marca", "Fecha / Hora"};
            DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            if (listaMantenimientos != null && !listaMantenimientos.isEmpty()) {
                for (Mantenimiento m : listaMantenimientos) {
                    modelo.addRow(new Object[]{
                        m.getIdMantenimiento(), 
                        m.getNombreHerramienta(),
                        m.getMarca() != null ? m.getMarca() : "Original",
                        m.getFechaHora() != null ? m.getFechaHora() : "N/A"
                    });
                }
            }

            // Inyectamos el modelo en la vista del historial
            panelHistorial.setDatosHistorial(modelo);

            // Cambiamos la vista central
            cambiarPanelCentral(panelHistorial);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaAdmin,
                    "Error al consultar la BD para el historial de mantenimientos: " + e.getMessage(),
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
