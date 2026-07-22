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
import nexusgo.model.PromocionDao;
import nexusgo.model.ServicioDao;
import nexusgo.model.Servicios;
import nexusgo.model.Usuario;
import nexusgo.view.PanelAdmi;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.ReportesFinancieros;
import nexusgo.view.VistaAgregarPromocion;
import nexusgo.view.VistaAgregarPromocionServicio;
import nexusgo.view.VistaAgregarServicio;
import nexusgo.view.VistaHistorialMantenimiento;

/**
 *
 * @author INGRID
 */
public class ControladorPrincipalAdmiPeluqueria implements ActionListener {
    
  // Referencias a las vistas del módulo
    private final PanelAdmi vistaAdmin;
    private ReportesFinancieros panelReportes;
    private VistaHistorialMantenimiento panelHistorial;

    // Instancias de los DAO para el acceso a datos
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private final MantenimientoDao mantenimientoDao = new MantenimientoDao();
    private final ServicioDao servicioDao = new ServicioDao();
    private final PromocionDao promocionDao = new PromocionDao();

    // Datos del usuario con sesión activa
    private final Usuario usuarioLogueado;

    // Constructor principal
    public ControladorPrincipalAdmiPeluqueria(PanelAdmi vistaAdmin, Usuario usuarioLogueado) {
        this.vistaAdmin = vistaAdmin;
        this.usuarioLogueado = usuarioLogueado;

        try {
            // Inicializar vistas secundarias que se reutilizan
            this.panelReportes = new ReportesFinancieros();
            this.panelHistorial = new VistaHistorialMantenimiento();

            // Asignar eventos a los botones y componentes
            inicializarListeners();

            // Cargar la vista de bienvenida por defecto al centro
            cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al inicializar el módulo de administración: " + e.getMessage(),
                    "Error de Inicio", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para registrar los escuchadores de eventos
    private void inicializarListeners() {
        try {
            // Clic en la tarjeta de reportes financieros
            if (this.vistaAdmin.getPnlTarjeta() != null) {
                this.vistaAdmin.getPnlTarjeta().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cambiarPanelCentral(panelReportes);
                    }
                });
            }

            // Opciones del menú lateral
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

            // Botones directos del panel principal
            if (this.vistaAdmin.bServicios != null) {
                this.vistaAdmin.bServicios.addActionListener(this);
            }
            if (this.vistaAdmin.bPromociones != null) {
                this.vistaAdmin.bPromociones.addActionListener(this);
            }
            if (this.vistaAdmin.btnReporte != null) {
                this.vistaAdmin.btnReporte.addActionListener(this);
            }

            // Botones dentro del panel de reportes financieros
            if (this.panelReportes != null) {
                this.panelReportes.getBtnInicio().addActionListener(this);
                this.panelReportes.getBtnProcesar().addActionListener(this);
                this.panelReportes.getBtnCerrar().addActionListener(this);
                this.panelReportes.getBtnHistorialMH().addActionListener(this);
            }

            // Botón general de cerrar sesión
            if (this.vistaAdmin.getBtnCerrar() != null) {
                this.vistaAdmin.getBtnCerrar().addActionListener(this);
            }

        } catch (Exception e) {
            System.err.println("Error al vincular los eventos: " + e.getMessage());
        }
    }

    // Gestor central de eventos (clics y acciones)
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Navegación desde el menú lateral
            if (this.vistaAdmin.getMenuLateral() != null) {
                if (e.getSource() == vistaAdmin.getMenuLateral().bCasa) {
                    cambiarPanelCentral(new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol()));
                    return;
                }

                if (e.getSource() == vistaAdmin.getMenuLateral().bInventario) {
                    JOptionPane.showMessageDialog(vistaAdmin,
                            "Módulo de inventario en construcción.",
                            "NexusGO", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (e.getSource() == vistaAdmin.getMenuLateral().misCitas) {
                    mostrarHistorialMantenimiento();
                    return;
                }
            }

            // Botones principales
            if (e.getSource() == vistaAdmin.bServicios) {
                VistaAgregarServicio vistaServicios = new VistaAgregarServicio();
                new ControladorAgregarServicio(vistaServicios, servicioDao);
                cambiarPanelCentral(vistaServicios);
                return;
            }

            // Redirige directamente a Promoción de Servicios
            if (e.getSource() == vistaAdmin.bPromociones) {
                gestionarAperturaPromocionServicio();
                return;
            }

            if (e.getSource() == vistaAdmin.btnReporte) {
                cambiarPanelCentral(panelReportes);
                return;
            }

            // Acciones dentro de la vista de reportes
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

            // Cierre de sesión global
            if (e.getSource() == vistaAdmin.getBtnCerrar()) {
                ejecutarCierreSesion();
            }

        } catch (Exception ex) {
            System.err.println("Error procesando acción: " + ex.getMessage());
        }
    }

    // Abre de manera directa la vista configurada EXCLUSIVAMENTE para Promociones de Servicio
    private void gestionarAperturaPromocionServicio() {
        try {
            // Instancia de la vista específica requerida por el constructor
            VistaAgregarPromocionServicio vistaPromocion = new VistaAgregarPromocionServicio();

            // Cargar los servicios activos desde la base de datos si la vista tiene el combo
            List<Servicios> serviciosActivos = servicioDao.listarServiciosActivos();

            if (serviciosActivos != null && vistaPromocion.comboServicios != null) {
                vistaPromocion.comboServicios.removeAllItems();
                for (Servicios s : serviciosActivos) {
                    vistaPromocion.comboServicios.addItem(s);
                }
            }

            // Inicializar el sub-controlador con los 3 parámetros exactos requeridos
            new ControladorAgregarPromocionServicio(vistaPromocion, promocionDao, servicioDao);

            // Mostrar la vista en el centro del panel
            cambiarPanelCentral(vistaPromocion);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaAdmin,
                    "Error al abrir la vista de promociones de servicio: " + e.getMessage(),
                    "Error de Interfaz", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Genera la tabla del reporte financiero consultando las herramientas en BD
    private void ejecutarProcesamientoReporte() {
        try {
            String mesSeleccionado = panelReportes.getComboMes().getSelectedItem().toString();
            String anioSeleccionado = panelReportes.getComboAnio().getSelectedItem().toString();

            DefaultTableModel modelo = panelReportes.getModeloTabla();
            modelo.setRowCount(0); // Limpiar filas anteriores

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
                        "Reporte procesado correctamente para " + mesSeleccionado + " " + anioSeleccionado + ".",
                        "NexusGO - Reporte Financiero", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelReportes,
                        "No hay datos registrados para el período seleccionado.",
                        "Sin Datos", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panelReportes,
                    "Error consultando la base de datos: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Muestra la vista con el historial de mantenimientos realizados
    private void mostrarHistorialMantenimiento() {
        try {
            List<Mantenimiento> listaMantenimientos = herramientaDao.listarMantenimientosRealizados();

            String[] columnas = {"Número de referencia", "Nombre", "Marca", "Fecha / Hora"};
            DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Bloquear edición de celdas
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

            panelHistorial.setDatosHistorial(modelo);
            cambiarPanelCentral(panelHistorial);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaAdmin,
                    "Error consultando el historial de mantenimientos: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Confirmar y cerrar la ventana actual del administrador
    private void ejecutarCierreSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vistaAdmin,
                "¿Desea cerrar la sesión actual?",
                "NexusGO - Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            vistaAdmin.dispose();
        }
    }

    // Reemplazar dinámicamente la vista del centro de la pantalla
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
            System.err.println("Error al reemplazar el panel central: " + e.getMessage());
        }
    }
}
