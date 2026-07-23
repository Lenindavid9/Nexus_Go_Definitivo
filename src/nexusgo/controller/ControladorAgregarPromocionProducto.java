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
public class ControladorAgregarPromocionProducto implements ActionListener {

    // Componentes visuales y de persistencia de datos
    private final VistaAgregarPromocionProducto vista;
    private final PromocionDao promocionDao;
    private final ProductoDao productoDao;
    private ControladorPrincipalAdmiPeluqueria controladorPrincipal; // Permite la navegación entre vistas
    private File imagenSeleccionada;

    // Constructor de 3 parámetros (Mantiene compatibilidad con invocaciones antiguas)
    public ControladorAgregarPromocionProducto(VistaAgregarPromocionProducto vista, PromocionDao promocionDao, ProductoDao productoDao) {
        this(vista, promocionDao, productoDao, null);
    }

    // Constructor principal de 4 parámetros
    public ControladorAgregarPromocionProducto(VistaAgregarPromocionProducto vista, PromocionDao promocionDao, ProductoDao productoDao, ControladorPrincipalAdmiPeluqueria controladorPrincipal) {
        this.vista = vista;
        this.promocionDao = promocionDao;
        this.productoDao = productoDao;
        this.controladorPrincipal = controladorPrincipal;

        inicializarListeners();
    }

    // Vincula los eventos de la vista con este controlador
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

    // Captura y distribuye las acciones según el botón presionado
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

    // Procesa el formulario, valida las entradas y registra la nueva promoción
    private void ejecutarRegistroPromocion() {
        try {
            // Validación de producto seleccionado
            Producto productoSeleccionado = (Producto) vista.comboProductos.getSelectedItem();
            if (productoSeleccionado == null) {
                throw new IllegalArgumentException("Debe seleccionar un producto válido de la lista.");
            }

            // Validación del texto descriptivo
            String descripcion = vista.txtDescripcionPromocion.getText().trim();
            if (descripcion.isEmpty() || descripcion.equalsIgnoreCase("Ingrese la descripcion de la promocion")) {
                throw new IllegalArgumentException("Debe ingresar una descripción para la promoción.");
            }

            // Validación del rango de fechas
            Date fechaInicio = vista.dateFechaInicio.getDate();
            Date fechaFin = vista.dateFechaFin.getDate();

            if (fechaInicio == null || fechaFin == null) {
                throw new IllegalArgumentException("Debe seleccionar la fecha de inicio y de finalización.");
            }

            if (fechaFin.before(fechaInicio)) {
                throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            }

            // Validación de formato numérico del precio promocional
            String strPrecio = vista.txtPrecio.getText().trim();
            if (strPrecio.isEmpty() || strPrecio.equalsIgnoreCase("Ingrese el precio en pesos colombianos")) {
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

            // Cálculo dinámico del porcentaje de descuento respecto al precio original del producto
            double precioOriginalProducto = productoSeleccionado.getPrecioCompra();
            double porcentajeCalculado = 0.0;
            if (precioOriginalProducto > 0 && precioPromocional < precioOriginalProducto) {
                porcentajeCalculado = ((precioOriginalProducto - precioPromocional) / precioOriginalProducto) * 100.0;
            }

            // Mapeo y construcción del modelo de datos Promocion
            Promocion promocion = new Promocion();
            promocion.setIdProducto(productoSeleccionado.getIdProducto());
            promocion.setIdServicio(null); // Es nulo porque es una promoción de producto, no de servicio
            promocion.setPorcentajeDescuento(porcentajeCalculado);
            promocion.setFechaInicio(fechaInicio);
            promocion.setFechaFin(fechaFin);
            promocion.setEstado("ACTIVA");

            if (imagenSeleccionada != null) {
                // Se asigna la ruta de la imagen en caso de que el modelo maneje ese campo
                // promocion.setRutaImagen(imagenSeleccionada.getAbsolutePath());
            }

            // Guardado en la base de datos a través del DAO
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

    // Muestra una ventana de selección de archivos para elegir una imagen
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

    // Regresa al panel principal de administración
    private void ejecutarVolver() {
        try {
            limpiarCampos();
            if (controladorPrincipal != null) {
                controladorPrincipal.mostrarPanelBienvenida();
            }
        } catch (Exception ex) {
            mostrarError("Error al procesar el retorno.", ex);
        }
    }

    // Cierra la sesión activa del usuario
    private void ejecutarCierreSesion() {
        try {
            if (controladorPrincipal != null) {
                controladorPrincipal.ejecutarCierreSesion();
            } else {
                limpiarCampos();
            }
        } catch (Exception ex) {
            mostrarError("Error al cerrar la sesión.", ex);
        }
    }

    // Restablece los campos de la interfaz a sus valores por defecto
    private void limpiarCampos() {
        try {
            if (vista.comboProductos != null && vista.comboProductos.getItemCount() > 0) {
                vista.comboProductos.setSelectedIndex(0);
            }
            if (vista.txtDescripcionPromocion != null) {
                vista.txtDescripcionPromocion.setText("Ingrese la descripcion de la promocion");
            }
            if (vista.dateFechaInicio != null) {
                vista.dateFechaInicio.setDate(null);
            }
            if (vista.dateFechaFin != null) {
                vista.dateFechaFin.setDate(null);
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

    // Método auxiliar para centralizar el despliegue de mensajes de error
    private void mostrarError(String mensajeContexto, Exception ex) {
        System.err.println(mensajeContexto + " -> Details: " + ex.getMessage());
        JOptionPane.showMessageDialog(vista,
                mensajeContexto + "\nDetalle: " + ex.getMessage(),
                "Error de Sistema", JOptionPane.ERROR_MESSAGE);
    }
}
