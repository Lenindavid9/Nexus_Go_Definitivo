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
import nexusgo.model.Servicios;
import nexusgo.model.ServicioDao;
import nexusgo.view.VistaAgregarPromocionCombo;

/**
 *
 * @author USUARIO
 */
public class ControladorAgregarPromocionCombo implements ActionListener {

    private final VistaAgregarPromocionCombo vista;
    private final ProductoDao productoDao;
    private final ServicioDao servicioDao;
    private final PromocionComboDao comboDao;

    private File archivoImagenSeleccionado;

    public ControladorAgregarPromocionCombo(VistaAgregarPromocionCombo vista) {
        this.vista = vista;
        this.productoDao = new ProductoDao();
        this.servicioDao = new ServicioDao();
        this.comboDao = new PromocionComboDao();

        // Enlazar Listeners
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnCargarImagen.addActionListener(this);
        this.vista.btnVolver.addActionListener(this);
        this.vista.btnCerrarSesion.addActionListener(this);

        // Cargar listas al iniciar la vista
        cargarListas();
    }

    private void cargarListas() {
        try {
            // Cargar Productos
            vista.getModeloListaProductos().clear();
            List<Producto> productos = productoDao.listar();
            if (productos != null) {
                for (Producto p : productos) {
                    vista.getModeloListaProductos().addElement(p);
                }
            }

            // Cargar Servicios
            vista.getModeloListaServicios().clear();
            List<Servicios> servicios = servicioDao.listarServicios();
            if (servicios != null) {
                for (Servicios s : servicios) {
                    vista.getModeloListaServicios().addElement(s);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al cargar productos o servicios desde la base de datos:\n" + e.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnCargarImagen) {
            seleccionarImagen();
        } else if (e.getSource() == vista.btnGuardar) {
            procesarGuardadoCombo();
        } else if (e.getSource() == vista.btnVolver) {
            // Acción de navegación hacia la pantalla anterior
        } else if (e.getSource() == vista.btnCerrarSesion) {
            // Acción de cierre de sesión
        }
    }

    private void seleccionarImagen() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imagen del Combo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.jpg, *.png, *.jpeg)", "jpg", "png", "jpeg"));

            int resultado = fileChooser.showOpenDialog(vista);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                archivoImagenSeleccionado = fileChooser.getSelectedFile();
                vista.lblNombreImagen.setText(archivoImagenSeleccionado.getName());
            }
        } catch (SecurityException se) {
            JOptionPane.showMessageDialog(vista,
                    "No se tienen permisos de lectura en el sistema de archivos para seleccionar esta imagen.",
                    "Error de Permisos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error insospechado al seleccionar el archivo: " + ex.getMessage(),
                    "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Contiene las validaciones de negocio y el control exhaustivo de
     * excepciones.
     */
    private void procesarGuardadoCombo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false); // Estricto con el formato de fechas

        try {
            // 1. CAPTURA Y VALIDACIÓN DE TEXTOS BÁSICOS
            String nombreCombo = vista.getTxtNombreCombo().getText().trim();
            String descripcion = vista.getTxtDescripcionPromocion().getText().trim();
            String fechaInicioStr = vista.getTxtFechaInicio().getText().trim();
            String fechaFinStr = vista.getTxtFechaFin().getText().trim();
            String precioStr = vista.getTxtPrecioCombo().getText().trim();

            if (nombreCombo.isEmpty() || nombreCombo.startsWith("Ej:")) {
                throw new IllegalArgumentException("Debe ingresar un nombre válido para el Kit/Combo.");
            }

            if (descripcion.isEmpty() || descripcion.startsWith("Ingrese")) {
                throw new IllegalArgumentException("Debe proporcionar una descripción para la promoción.");
            }

            // 2. VALIDACIÓN DE SELECCIÓN DE ITEMS (REGLA DE NEGOCIO DE COMBOS)
            List<Producto> productosSeleccionados = vista.getListaProductos().getSelectedValuesList();
            List<Servicios> serviciosSeleccionados = vista.getListaServicios().getSelectedValuesList();

            if (productosSeleccionados.isEmpty() && serviciosSeleccionados.isEmpty()) {
                throw new IllegalStateException("Debe seleccionar al menos un producto o un servicio para crear la promoción.");
            }

            // 3. VALIDACIÓN Y PARSEO DE PRECIO
            double precioCombo;
            try {
                // Reemplaza comas por puntos si el usuario usa formato decimal con coma
                precioStr = precioStr.replace(",", ".");
                precioCombo = Double.parseDouble(precioStr);
            } catch (NumberFormatException nfe) {
                // Lanzamos IllegalArgumentException para que caiga limpiamente en el bloque catch unificado
                throw new IllegalArgumentException("El campo de precio contiene caracteres no válidos. Ingrese solo números.");
            }

            if (precioCombo <= 0) {
                throw new IllegalArgumentException("El precio del combo debe ser un valor mayor a cero.");
            }

            // 4. VALIDACIÓN Y PARSEO DE FECHAS
            Date fechaInicio;
            Date fechaFin;
            try {
                fechaInicio = sdf.parse(fechaInicioStr);
                fechaFin = sdf.parse(fechaFinStr);
            } catch (ParseException pe) {
                throw new ParseException("Formato de fecha inválido. Utilice el formato: DD.MM.YYYY (Ej: 12.12.2026).", pe.getErrorOffset());
            }

            if (fechaInicio.after(fechaFin)) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de finalización.");
            }

            // 5. CONSTRUCCIÓN DEL OBJETO MODELO
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

            // 6. PERSISTENCIA EN BASE DE DATOS Y RESPUESTA
            boolean registrado = comboDao.registrarComboConDetalles(combo);

            if (registrado) {
                JOptionPane.showMessageDialog(vista,
                        "¡Promoción Combo '" + nombreCombo + "' registrada correctamente!\n"
                        + "• Productos vinculados: " + productosSeleccionados.size() + "\n"
                        + "• Servicios vinculados: " + serviciosSeleccionados.size(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
            } else {
                throw new RuntimeException("Error en BD: No se pudo completar la transacción del combo.");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Captura validaciones de campos requeridos, formato de precios y rangos
            JOptionPane.showMessageDialog(vista, e.getMessage(), "Atención - Datos Incompletos o Inválidos", JOptionPane.WARNING_MESSAGE);

        } catch (ParseException e) {
            // Captura errores cuando la fecha no cumple el formato esperado
            JOptionPane.showMessageDialog(vista, e.getMessage(), "Error de Formato en Fecha", JOptionPane.WARNING_MESSAGE);

        } catch (RuntimeException e) {
            // Captura errores generados durante la inserción en BD o errores inesperados del sistema
            JOptionPane.showMessageDialog(vista, "Ocurrió un error procesando el guardado:\n" + e.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            // Captura cualquier otra excepción no contemplada
            JOptionPane.showMessageDialog(vista, "Ocurrió un error inesperado: " + e.getMessage(), "Error Desconocido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        vista.getTxtNombreCombo().setText("Ej: Kit Renovación Total (Corte + Producto)");
        vista.getTxtDescripcionPromocion().setText("Ingrese detalles de la promoción o combo");
        vista.getTxtFechaInicio().setText("12.12.2026");
        vista.getTxtFechaFin().setText("31.12.2026");
        vista.getTxtPrecioCombo().setText("Ingrese el precio en pesos colombianos");

        vista.getListaProductos().clearSelection();
        vista.getListaServicios().clearSelection();

        archivoImagenSeleccionado = null;
        vista.getLblNombreImagen().setText("imagencombo.png");
    }

}
