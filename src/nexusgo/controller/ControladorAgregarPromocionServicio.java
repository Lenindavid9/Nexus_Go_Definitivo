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
import nexusgo.model.Promocion;
import nexusgo.model.PromocionDao;
import nexusgo.model.ServicioDao;
import nexusgo.model.Servicios;
import nexusgo.view.VistaAgregarPromocionServicio;

/**
 *
 * @author USUARIO
 */
public class ControladorAgregarPromocionServicio implements ActionListener {

    private final VistaAgregarPromocionServicio vista;
    private final PromocionDao promocionDao;
    private final ServicioDao servicioDao;
    private ControladorPrincipalAdmiPeluqueria controladorPrincipal; // Referencia para la navegación
    private File imagenSeleccionada;

    // Constructor de 3 parámetros (compatibilidad)
    public ControladorAgregarPromocionServicio(VistaAgregarPromocionServicio vista, PromocionDao promocionDao, ServicioDao servicioDao) {
        this(vista, promocionDao, servicioDao, null);
    }

    // Constructor de 4 parámetros (incluye ControladorPrincipalAdmiPeluqueria)
    public ControladorAgregarPromocionServicio(VistaAgregarPromocionServicio vista,
            PromocionDao promocionDao,
            ServicioDao servicioDao,
            ControladorPrincipalAdmiPeluqueria controladorPrincipal) {
        this.vista = vista;
        this.promocionDao = promocionDao;
        this.servicioDao = servicioDao;
        this.controladorPrincipal = controladorPrincipal;

        inicializarListeners();
    }

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

    private void ejecutarRegistroPromocion() {
        try {
            // 1. VALIDACIÓN: Selección del Servicio en el ComboBox
            Servicios servicioSeleccionado = (Servicios) vista.comboServicios.getSelectedItem();
            if (servicioSeleccionado == null) {
                throw new IllegalArgumentException("Debe seleccionar un servicio válido de la lista.");
            }

            // 2. VALIDACIÓN: Descripción
            String descripcion = vista.txtDescripcionPromocion.getText().trim();
            if (descripcion.isEmpty() || descripcion.equalsIgnoreCase("Ingrese la descripcion de la promocion")) {
                throw new IllegalArgumentException("Debe ingresar una descripción para la promoción.");
            }

            // 3. VALIDACIÓN: Extracción de Fechas desde JDateChooser
            Date fechaInicio = vista.dateFechaInicio.getDate();
            Date fechaFin = vista.dateFechaFin.getDate();

            if (fechaInicio == null || fechaFin == null) {
                throw new IllegalArgumentException("Debe seleccionar la fecha de inicio y de finalización.");
            }

            if (fechaFin.before(fechaInicio)) {
                throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            }

            // 4. VALIDACIÓN: Precio / Descuento
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

            // Cálculo dinámico del porcentaje de descuento relativo al precio del servicio
            double precioOriginalServicio = servicioSeleccionado.getPrecio();
            double porcentajeCalculado = 0.0;
            if (precioOriginalServicio > 0 && precioPromocional < precioOriginalServicio) {
                porcentajeCalculado = ((precioOriginalServicio - precioPromocional) / precioOriginalServicio) * 100.0;
            }

            // 5. CONSTRUCCIÓN DEL OBJETO MODELO
            Promocion promocion = new Promocion();
            promocion.setIdServicio(servicioSeleccionado.getIdServicio());
            promocion.setIdProducto(null); // Explícitamente Nulo para Promociones de Servicio
            promocion.setPorcentajeDescuento(porcentajeCalculado);
            promocion.setFechaInicio(fechaInicio);
            promocion.setFechaFin(fechaFin);
            promocion.setEstado("ACTIVA");

            if (imagenSeleccionada != null) {
                // promocion.setRutaImagen(imagenSeleccionada.getAbsolutePath());
            }

            // 6. INSERCIÓN EN BASE DE DATOS
            boolean exito = promocionDao.guardarPromocion(promocion);

            if (exito) {
                JOptionPane.showMessageDialog(vista,
                        "¡Promoción de servicio registrada exitosamente en NexusGO!",
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
            limpiarCampos();
            if (controladorPrincipal != null) {
                controladorPrincipal.mostrarPanelBienvenida();
            }
        } catch (Exception ex) {
            mostrarError("Error al procesar el retorno.", ex);
        }
    }

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

    private void limpiarCampos() {
        try {
            if (vista.comboServicios != null && vista.comboServicios.getItemCount() > 0) {
                vista.comboServicios.setSelectedIndex(0);
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
                vista.lblNombreImagen.setText("imagenservicio.png");
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
