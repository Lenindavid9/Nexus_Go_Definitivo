/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Importación de Modelos y DAOs
import nexusgo.model.ProductoDao;
import nexusgo.model.PromocionDao;
import nexusgo.model.ServicioDao;
import nexusgo.model.Usuario;

// Importación de Vistas
import nexusgo.view.PanelAdmi;
import nexusgo.view.PanelBienvenida;
import nexusgo.view.ReportesFinancieros;
import nexusgo.view.VistaAgregarPromocionCombo;
import nexusgo.view.VistaAgregarPromocionProducto;
import nexusgo.view.VistaAgregarPromocionServicio;
import nexusgo.view.VistaAgregarServicio;
import nexusgo.view.VistaInicioSesion;

/**
 *
 * @author INGRID
 */
public class ControladorPrincipalAdmiPeluqueria implements ActionListener {

    private final PanelAdmi vistaAdmin;
    private final Usuario usuarioLogueado;

    // Instancias de DAOs para la gestión de datos
    private final PromocionDao promocionDao = new PromocionDao();
    private final ProductoDao productoDao = new ProductoDao();
    private final ServicioDao servicioDao = new ServicioDao();

    public ControladorPrincipalAdmiPeluqueria(PanelAdmi vistaAdmin, Usuario usuarioLogueado) {
        this.vistaAdmin = vistaAdmin;
        this.usuarioLogueado = usuarioLogueado;

        // Título personalizado para la ventana principal
        this.vistaAdmin.setTitle("NexusGO - Panel de Administración: " + usuarioLogueado.getNombre());

        // Vinculación de listeners de botones
        inicializarListeners();

        // Muestra la vista de bienvenida por defecto al iniciar
        mostrarPanelBienvenida();
    }

    private void inicializarListeners() {
        try {
            // Botón de Inicio en la barra lateral
            if (vistaAdmin.getMenuLateral() != null && vistaAdmin.getMenuLateral().bCasa != null) {
                vistaAdmin.getMenuLateral().bCasa.addActionListener(this);
            }

            // Botón para Gestión de Servicios
            if (vistaAdmin.bServicios != null) {
                vistaAdmin.bServicios.addActionListener(this);
            }

            // Botón para Opciones de Promociones y Combos
            if (vistaAdmin.bPromociones != null) {
                vistaAdmin.bPromociones.addActionListener(this);
            }

            // Botón para Reportes Financieros
            if (vistaAdmin.btnReporte != null) {
                vistaAdmin.btnReporte.addActionListener(this);
            }

            // Botón para Cerrar Sesión
            if (vistaAdmin.getBtnCerrar() != null) {
                vistaAdmin.getBtnCerrar().addActionListener(this);
            }

        } catch (NullPointerException npe) {
            System.err.println("Error al inicializar listeners en ControladorPrincipalAdmiPeluqueria: " + npe.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (vistaAdmin.getMenuLateral() != null && source == vistaAdmin.getMenuLateral().bCasa) {
            mostrarPanelBienvenida();

        } else if (source == vistaAdmin.bServicios) {
            abrirModuloAgregarServicios();

        } else if (source == vistaAdmin.bPromociones) {
            // Despliega el JOptionPane con las 3 opciones principales
            lanzarMenuDecisionPromociones();

        } else if (source == vistaAdmin.btnReporte) {
            ReportesFinancieros vistaReportes = new ReportesFinancieros();
            cambiarPanelCentral(vistaReportes.VistaRF());
        } else if (source == vistaAdmin.getBtnCerrar()) {
            ejecutarCierreSesion();
        }
    }

    /**
     * Muestra las 3 opciones de Promociones/Combos y direcciona a su respectiva vista y controlador.
     */
    private void lanzarMenuDecisionPromociones() {
        String[] opciones = {
            "Promoción Combo", 
            "Promoción Producto", 
            "Promoción Servicio", 
            "Cancelar"
        };

        int seleccion = JOptionPane.showOptionDialog(
                vistaAdmin,
                "¿Qué tipo de promoción o combo desea registrar?",
                "NexusGO - Selección de Módulo",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        switch (seleccion) {
            case 0: // Promoción Combo
                abrirModuloPromocionCombo();
                break;
                
            case 1: // Promoción Producto
                abrirModuloPromocionProducto();
                break;
                
            case 2: // Promoción Servicio
                abrirModuloPromocionServicio();
                break;

            default:
                // Cancelar o cerrar ventana
                break;
        }
    }

    // Muestra la vista de bienvenida
    public void mostrarPanelBienvenida() {
        PanelBienvenida bienvenida = new PanelBienvenida(usuarioLogueado.getNombre(), usuarioLogueado.getRol());
        cambiarPanelCentral(bienvenida);
    }

    // 1. Abrir módulo Promoción Combo
    private void abrirModuloPromocionCombo() {
        VistaAgregarPromocionCombo vista = new VistaAgregarPromocionCombo();
        new ControladorAgregarPromocionCombo(vista, promocionDao, productoDao, servicioDao, "COMBO", this);
        cambiarPanelCentral(vista);
    }

    // 2. Abrir módulo Promoción Producto
    private void abrirModuloPromocionProducto() {
        VistaAgregarPromocionProducto vista = new VistaAgregarPromocionProducto();
        new ControladorAgregarPromocionProducto(vista, promocionDao, productoDao, this);
        cambiarPanelCentral(vista);
    }

    // 3. Abrir módulo Promoción Servicio
    private void abrirModuloPromocionServicio() {
        VistaAgregarPromocionServicio vista = new VistaAgregarPromocionServicio();
        new ControladorAgregarPromocionServicio(vista, promocionDao, servicioDao, this);
        cambiarPanelCentral(vista);
    }

    // Abrir módulo Servicios simples
    private void abrirModuloAgregarServicios() {
        VistaAgregarServicio vistaServicios = new VistaAgregarServicio();
        new ControladorAgregarServicio(vistaServicios, servicioDao);
        cambiarPanelCentral(vistaServicios);
    }

    /**
     * Reemplaza dinámicamente el panel central dentro del contenedor principal.
     */
    private void cambiarPanelCentral(JPanel nuevoPanel) {
        try {
            JPanel contenedorCentral = vistaAdmin.getContenidoCentral();
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(nuevoPanel, BorderLayout.CENTER);

            SwingUtilities.invokeLater(() -> {
                contenedorCentral.revalidate();
                contenedorCentral.repaint();
            });
        } catch (Exception e) {
            System.err.println("Error al reemplazar el panel central: " + e.getMessage());
        }
    }

    /**
     * Confirma la salida del usuario y redirige a la ventana de inicio de sesión.
     */
    public void ejecutarCierreSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                vistaAdmin,
                "¿Desea cerrar la sesión del Administrador?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            vistaAdmin.dispose();
            VistaInicioSesion vistaLogin = new VistaInicioSesion();
            new ControladorInicioSesion(vistaLogin);
            vistaLogin.setVisible(true);
        }
    }
}
