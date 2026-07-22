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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.model.Promocion;
import nexusgo.model.PromocionDao;
import nexusgo.view.VistaAgregarPromocionProducto;
/**
 *
 * @author USUARIO
 */
public class ControladorAgregarPromocionProducto implements ActionListener{
    
    private final VistaAgregarPromocionProducto vista;
    private final PromocionDao promocionDao;
    private final ProductoDao productoDao;
    private File imagenSeleccionada;

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd.MM.yyyy");

    public ControladorAgregarPromocionProducto(VistaAgregarPromocionProducto vista, PromocionDao promocionDao, ProductoDao productoDao) {
        this.vista = vista;
        this.promocionDao = promocionDao;
        this.productoDao = productoDao;

        FORMATO_FECHA.setLenient(false); // Validación estricta para días y meses reales

        inicializarListeners();
    }

    /**
     * Suscribe los eventos de la vista al controlador comprobando posibles referencias nulas.
     */
    private void inicializarListeners() {
        try {
            if (this.vista.btnGuardar != null) {
                this.vista.btnGuardar.addActionListener(this);
            }
            if (this.vista.btnCargarImagen != null) {
                this.vista.btnCargarImagen.addActionListener(this);
            }
            if (this.vista.btnVolver != null) {
                this.vista.btnVolver.addActionListener(this);
            }
            if (this.vista.btnCerrarSesion != null) {
                this.vista.btnCerrarSesion.addActionListener(this);
            }
        } catch (NullPointerException e) {
            mostrarError("Error al vincular los componentes de la interfaz", e);
        } catch (Exception e) {
            mostrarError("Error inesperado en la inicialización de eventos", e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Object origen = e.getSource();

            if (origen == vista.btnGuardar) {
                ejecutarRegistroPromocion();
            } else if (origen == vista.btnCargarImagen) {
                ejecutarCargaImagen();
            } else if (origen == vista.btnVolver) {
                ejecutarVolver();
            } else if (origen == vista.btnCerrarSesion) {
                ejecutarCierreSesion();
            }
        } catch (Exception ex) {
            mostrarError("Ocurrió un fallo no controlado durante la acción", ex);
        }
    }

    /**
     * Valida los datos introducidos por el usuario y procesa la inserción en BD.
     */
    private void ejecutarRegistroPromocion() {
        try {
            // 1. VALIDACIÓN: Selección del Producto en el ComboBox
            Producto productoSeleccionado = (Producto) vista.comboProductos.getSelectedItem();
            if (productoSeleccionado == null) {
                throw new IllegalArgumentException("Debe seleccionar un producto válido de la lista.");
            }

            // 2. VALIDACIÓN: Descripción
            String descripcion = vista.txtDescripcionPromocion.getText().trim();
            if (descripcion.isEmpty() || descripcion.equalsIgnoreCase("Ingrese el nombre del producto")) {
                throw new IllegalArgumentException("Debe ingresar una descripción para la promoción.");
            }

            // 3. VALIDACIÓN: Conversión y formato de Fechas
            String strFechaInicio = vista.txtFechaInicio.getText().trim();
            String strFechaFin = vista.txtFechaFin.getText().trim();

            if (strFechaInicio.isEmpty() || strFechaFin.isEmpty()) {
                throw new IllegalArgumentException("Las fechas de inicio y finalización no pueden estar vacías.");
            }

            Date fechaInicio;
            Date fechaFin;

            try {
                fechaInicio = FORMATO_FECHA.parse(strFechaInicio);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("La fecha de inicio no tiene un formato válido (DD.MM.YYYY). Ej: 12.12.2026");
            }

            try {
                fechaFin = FORMATO_FECHA.parse(strFechaFin);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("La fecha de finalización no tiene un formato válido (DD.MM.YYYY). Ej: 12.12.2026");
            }

            if (fechaFin.before(fechaInicio)) {
                throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            }

            // 4. VALIDACIÓN: Precio / Descuento
            String strPrecio = vista.txtPrecio.getText().trim();
            if (strPrecio.isEmpty() || strPrecio.equalsIgnoreCase("Ingrese el precio ")) {
                throw new IllegalArgumentException("Debe ingresar el valor numérico para la promoción.");
            }

            double precioPromocional;
            try {
                precioPromocional = Double.parseDouble(strPrecio.replace(",", "."));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El valor ingresado para el precio debe ser estrictamente numérico.");
            }

            if (precioPromocional <= 0) {
                throw new IllegalArgumentException("El precio promocional debe ser mayor a cero.");
            }

            // Cálculo dinámico del porcentaje de descuento relativo al precio del producto
            double precioOriginalProducto = productoSeleccionado.getPrecioCompra();
            double porcentajeCalculado = 0.0;
            if (precioOriginalProducto > 0 && precioPromocional < precioOriginalProducto) {
                porcentajeCalculado = ((precioOriginalProducto - precioPromocional) / precioOriginalProducto) * 100.0;
            }

            // 5. CONSTRUCCIÓN DEL OBJETO MODELO
            Promocion promocion = new Promocion();
            promocion.setIdServicio(null); // Explícitamente Nulo para Promociones de Producto
            promocion.setIdProducto(productoSeleccionado.getIdProducto());
            promocion.setPorcentajeDescuento(porcentajeCalculado);
            promocion.setFechaInicio(fechaInicio);
            promocion.setFechaFin(fechaFin);
            promocion.setEstado("ACTIVA");

            // Si el modelo Promocion soporta la ruta o nombre de la imagen:
            if (imagenSeleccionada != null) {
                // promocion.setRutaImagen(imagenSeleccionada.getAbsolutePath());
                // promocion.setNombreImagen(imagenSeleccionada.getName());
            }

            // 6. INSERCIÓN EN BASE DE DATOS
            boolean exito = promocionDao.guardarPromocion(promocion);

            if (exito) {
                JOptionPane.showMessageDialog(vista,
                        "¡Promoción de producto registrada exitosamente en NexusGO!",
                        "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                throw new Exception("El DAO no pudo insertar el registro. Verifique la conexión a la base de datos.");
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (ClassCastException ex) {
            mostrarError("Ocurrió un error al procesar el objeto seleccionado en el desplegable.", ex);
        } catch (Exception ex) {
            mostrarError("Ocurrió un fallo crítico durante el proceso de guardado.", ex);
        }
    }

    /**
     * Despliega el explorador de archivos para asociar imágenes a la promoción.
     */
    private void ejecutarCargaImagen() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar Imagen de la Promoción");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Imagen (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));

            int resultado = fileChooser.showOpenDialog(vista);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                imagenSeleccionada = fileChooser.getSelectedFile();

                if (!imagenSeleccionada.exists() || !imagenSeleccionada.isFile()) {
                    throw new IllegalArgumentException("El archivo seleccionado no es válido o no existe.");
                }

                vista.lblNombreImagen.setText(imagenSeleccionada.getName());
            }
        } catch (SecurityException ex) {
            mostrarError("Permisos insuficientes para acceder al sistema de archivos.", ex);
        } catch (Exception ex) {
            mostrarError("Error inesperado al cargar el archivo de imagen.", ex);
        }
    }

    private void ejecutarVolver() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                    "¿Desea salir del formulario de registro?",
                    "NexusGO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                limpiarCampos();
                // Si la vista está en un panel contenedor, puedes notificar al controlador principal para removerla o cerrarla.
            }
        } catch (Exception ex) {
            mostrarError("Error al procesar el retorno.", ex);
        }
    }

    private void ejecutarCierreSesion() {
        try {
            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro que desea cerrar sesión?",
                    "Cerrar Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Destruir marco o invocar controlador de Login
                System.exit(0);
            }
        } catch (Exception ex) {
            mostrarError("Error al cerrar la sesión.", ex);
        }
    }

    private void limpiarCampos() {
        try {
            if (vista.comboProductos != null && vista.comboProductos.getItemCount() > 0) {
                vista.comboProductos.setSelectedIndex(0);
            }
            if (vista.txtDescripcionPromocion != null) {
                vista.txtDescripcionPromocion.setText("Ingrese el nombre del producto");
            }
            if (vista.txtFechaInicio != null) {
                vista.txtFechaInicio.setText("12.12.2026");
            }
            if (vista.txtFechaFin != null) {
                vista.txtFechaFin.setText("12.12.2026");
            }
            if (vista.txtPrecio != null) {
                vista.txtPrecio.setText("Ingrese el precio en pesos colombianos");
            }
            if (vista.lblNombreImagen != null) {
                vista.lblNombreImagen.setText("imagenproducto.png");
            }
            imagenSeleccionada = null;
        } catch (Exception e) {
            System.err.println("Error no crítico al limpiar los campos: " + e.getMessage());
        }
    }

    private void mostrarError(String mensajeContexto, Exception ex) {
        System.err.println(mensajeContexto + " -> Details: " + ex.getMessage());
        JOptionPane.showMessageDialog(vista,
                mensajeContexto + "\nDetalle: " + ex.getMessage(),
                "Error de Sistema", JOptionPane.ERROR_MESSAGE);
    }
    
}
