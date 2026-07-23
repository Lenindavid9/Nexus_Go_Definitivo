/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.PromocionCombo;
import nexusgo.model.PromocionComboDao;
import nexusgo.model.PromocionDao;
import nexusgo.model.Servicios;
import nexusgo.model.ServicioDao;
import nexusgo.view.VistaAgregarPromocionCombo;

/**
 *
 * @author USUARIO
 */
public class ControladorAgregarPromocionCombo implements ActionListener {

  // Vistas y DAOs requeridos
    private final VistaAgregarPromocionCombo vista;
    private final PromocionDao promocionDao;
    private final ProductoDao productoDao;
    private final ServicioDao servicioDao;
    private ControladorPrincipalAdmiPeluqueria controladorPrincipal; // Permite volver o cerrar sesión desde la vista
    
    private final PromocionComboDao comboDao = new PromocionComboDao();
    private File archivoImagenSeleccionado; // Guarda la imagen elegida
    private String tipoSeleccionado; // "PROMOCION" o "COMBO"

    // --- CONSTRUCTORES ---

    // Constructor base de 4 parámetros
    public ControladorAgregarPromocionCombo(VistaAgregarPromocionCombo vista, 
                                            PromocionDao promocionDao, 
                                            ProductoDao productoDao, 
                                            ServicioDao servicioDao) {
        this(vista, promocionDao, productoDao, servicioDao, "PROMOCION", null);
    }

    // Constructor de 5 parámetros
    public ControladorAgregarPromocionCombo(VistaAgregarPromocionCombo vista, 
                                            PromocionDao promocionDao, 
                                            ProductoDao productoDao, 
                                            ServicioDao servicioDao,
                                            String tipoSeleccionado) {
        this(vista, promocionDao, productoDao, servicioDao, tipoSeleccionado, null);
    }

    // Constructor completo de 6 parámetros (con soporte de navegación)
    public ControladorAgregarPromocionCombo(VistaAgregarPromocionCombo vista, 
                                            PromocionDao promocionDao, 
                                            ProductoDao productoDao, 
                                            ServicioDao servicioDao,
                                            String tipoSeleccionado,
                                            ControladorPrincipalAdmiPeluqueria controladorPrincipal) {
        this.vista = vista;
        this.promocionDao = promocionDao;
        this.productoDao = productoDao;
        this.servicioDao = servicioDao;
        this.tipoSeleccionado = tipoSeleccionado;
        this.controladorPrincipal = controladorPrincipal;
        
        inicializarListeners();
        cargarListas();
    }

    // Escucha los eventos de los botones del formulario
    private void inicializarListeners() {
        if (this.vista.btnCargarImagen != null) {
            this.vista.btnCargarImagen.addActionListener(this);
        }
        if (this.vista.btnGuardar != null) {
            this.vista.btnGuardar.addActionListener(this);
        }
        if (this.vista.btnVolver != null) {
            this.vista.btnVolver.addActionListener(this);
        }
        if (this.vista.btnCerrarSesion != null) {
            this.vista.btnCerrarSesion.addActionListener(this);
        }
    }

    // Llena los JList/JComboBox con los productos y servicios desde MySQL
    private void cargarListas() {
        try {
            // Carga la lista de productos
            if (vista.getModeloListaProductos() != null) {
                vista.getModeloListaProductos().clear();
                List<Producto> productos = productoDao.listar();
                if (productos != null) {
                    for (Producto p : productos) {
                        vista.getModeloListaProductos().addElement(p); // Usa el toString() de Producto
                    }
                }
            }

            // Carga la lista de servicios activos
            if (vista.getModeloListaServicios() != null) {
                vista.getModeloListaServicios().clear();
                List<Servicios> servicios = servicioDao.listarServiciosActivos();
                if (servicios != null) {
                    for (Servicios s : servicios) {
                        vista.getModeloListaServicios().addElement(s); // Usa el toString() de Servicios
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al cargar las listas desde la base de datos:\n" + e.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Administra los clics de los botones
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vista.btnCargarImagen) {
            seleccionarImagen();
        } else if (source == vista.btnGuardar) {
            procesarGuardadoCombo();
        } else if (source == vista.btnVolver) {
            limpiarFormulario();
            // Regresa al panel central de bienvenida
            if (controladorPrincipal != null) {
                controladorPrincipal.mostrarPanelBienvenida();
            }
        } else if (source == vista.btnCerrarSesion) {
            // Cierra la sesión mediante el controlador principal
            if (controladorPrincipal != null) {
                controladorPrincipal.ejecutarCierreSesion();
            } else {
                limpiarFormulario();
            }
        }
    }

    // Abre el navegador de archivos para elegir una imagen
    private void seleccionarImagen() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imagen");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.jpg, *.png, *.jpeg)", "jpg", "png", "jpeg"));

            int resultado = fileChooser.showOpenDialog(vista);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                archivoImagenSeleccionado = fileChooser.getSelectedFile();
                vista.lblNombreImagen.setText(archivoImagenSeleccionado.getName()); // Muestra el nombre del archivo
            }
        } catch (SecurityException se) {
            JOptionPane.showMessageDialog(vista, "Permisos insuficientes para leer el archivo.", "Error de Permisos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Valida las entradas e inserta el combo/promoción en la BD
    private void procesarGuardadoCombo() {
        try {
            // 1. Obtener datos de los textos
            String nombreCombo = vista.getTxtNombreCombo().getText().trim();
            String descripcion = vista.getTxtDescripcionPromocion().getText().trim();
            String precioStr = vista.getTxtPrecioCombo().getText().trim();

            if (nombreCombo.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar un nombre.");
            }

            if (descripcion.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar una descripción.");
            }

            // 2. Obtener elementos seleccionados
            List<Producto> productosSeleccionados = vista.getListaProductos().getSelectedValuesList();
            List<Servicios> serviciosSeleccionados = vista.getListaServicios().getSelectedValuesList();

            if (productosSeleccionados.isEmpty() && serviciosSeleccionados.isEmpty()) {
                throw new IllegalStateException("Debe seleccionar al menos un producto o servicio.");
            }

            // 3. Capturar y validar fechas
            Date fechaInicio = vista.getDateChooserInicio().getDate();
            Date fechaFin = vista.getDateChooserFin().getDate();

            if (fechaInicio == null || fechaFin == null) {
                throw new IllegalArgumentException("Seleccione las fechas de inicio y finalización.");
            }

            if (fechaInicio.after(fechaFin)) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la final.");
            }

            // 4. Validar y parsear precio (evita "Data truncation" en MySQL)
            double precioCombo;
            try {
                precioStr = precioStr.replace(",", ".");
                precioCombo = Double.parseDouble(precioStr);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("El precio debe ser un número válido.");
            }

            if (precioCombo <= 0 || precioCombo > 9999999.99) {
                throw new IllegalArgumentException("El precio debe ser mayor a $0 y no superar $9,999,999.99.");
            }

            // 5. Crear objeto PromocionCombo con los datos recabados
            PromocionCombo combo = new PromocionCombo();
            combo.setNombreCombo(nombreCombo);
            combo.setDescripcion(descripcion);
            combo.setFechaInicio(fechaInicio);
            combo.setFechaFin(fechaFin);
            combo.setPrecioCombo(precioCombo);
            combo.setProductos(productosSeleccionados);
            combo.setServicios(serviciosSeleccionados);

            if (archivoImagenSeleccionado != null) {
                combo.setRutaImagen(archivoImagenSeleccionado.getAbsolutePath());
            } else {
                combo.setRutaImagen("default_combo.png");
            }

            // 6. Guardar mediante el DAO
            boolean registrado = comboDao.registrarComboConDetalles(combo);

            if (registrado) {
                JOptionPane.showMessageDialog(vista,
                        "¡Registro exitoso!\n"
                        + "• Promoción: " + nombreCombo + "\n"
                        + "• Productos incluidos: " + productosSeleccionados.size() + "\n"
                        + "• Servicios incluidos: " + serviciosSeleccionados.size(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                throw new RuntimeException("No se pudo completar la transacción SQL.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(vista, e.getMessage(), "Atención", JOptionPane.WARNING_MESSAGE);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(vista, "Error de Base de Datos:\n" + e.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Resetea los campos de la interfaz a su estado original
    private void limpiarFormulario() {
        vista.getTxtNombreCombo().setText("");
        vista.getTxtDescripcionPromocion().setText("");
        vista.getTxtPrecioCombo().setText("");

        vista.getDateChooserInicio().setDate(null);
        vista.getDateChooserFin().setDate(null);

        vista.getListaProductos().clearSelection();
        vista.getListaServicios().clearSelection();

        archivoImagenSeleccionado = null;
        vista.getLblNombreImagen().setText("No se ha seleccionado imagen");
    }

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

}
