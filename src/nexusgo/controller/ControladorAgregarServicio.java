/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import nexusgo.model.ServicioDao;
import nexusgo.model.Servicios;
import nexusgo.view.VistaAgregarServicio;
/**
 *
 * @author USUARIO
 */
public class ControladorAgregarServicio implements ActionListener{
    
    // Referencias al modelo y la vista
    private final VistaAgregarServicio vista;
    private final ServicioDao servicioDao;

    /*
     * Constructor principal del controlador.
     * Recibe e inyecta las dependencias necesarias de la vista y el DAO.
     */
    public ControladorAgregarServicio(VistaAgregarServicio vista, ServicioDao servicioDao) {
        if (vista == null || servicioDao == null) {
            throw new IllegalArgumentException("La vista y el ServicioDao no pueden ser nulos.");
        }
        this.vista = vista;
        this.servicioDao = servicioDao;

        // Enlazar los componentes activos de la vista
        inicializarListeners();
    }

    /*
     * Suscribe los botones de la vista al listener de acciones.
     */
    private void inicializarListeners() {
        try {
            if (this.vista.btnGuardar != null) {
                this.vista.btnGuardar.addActionListener(this);
            }
            if (this.vista.btnVolver != null) {
                this.vista.btnVolver.addActionListener(this);
            }
            if (this.vista.btnCargarImagen != null) {
                this.vista.btnCargarImagen.addActionListener(this);
            }
            if (this.vista.btnCerrarSesion != null) {
                this.vista.btnCerrarSesion.addActionListener(this);
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar los listeners de la vista: " + e.getMessage());
        }
    }

    /*
     * Captura y despacha los eventos generados en la interfaz gráfica.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Accion: Boton Guardar
            if (this.vista.btnGuardar != null && e.getSource() == this.vista.btnGuardar) {
                guardarServicio();
                return;
            }

            // Accion: Boton Volver
            if (this.vista.btnVolver != null && e.getSource() == this.vista.btnVolver) {
                accionVolver();
                return;
            }

            // Accion: Boton Cargar Imagen
            if (this.vista.btnCargarImagen != null && e.getSource() == this.vista.btnCargarImagen) {
                cargarImagen();
                return;
            }

            // Accion: Boton Cerrar Sesion
            if (this.vista.btnCerrarSesion != null && e.getSource() == this.vista.btnCerrarSesion) {
                cerrarSesion();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Se produjo un fallo inesperado durante la ejecución: " + ex.getMessage(),
                    "Error de Ejecución", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Método encargado del procesamiento, validación y almacenamiento del servicio en la BD.
     */
    private void guardarServicio() {
        try {
            // Control de existencia de los componentes requeridos en la vista
            if (vista.txtNombreServicio == null || vista.txtPrecio == null) {
                throw new NullPointerException("Los componentes de entrada de la vista no han sido instanciados.");
            }

            // Extracción de datos con limpieza de placeholders
            String nombre = obtenerTextoValido(vista.txtNombreServicio, "Ingrese el nombre del servicio");
            String descripcion = obtenerTextoValido(vista.txtDescripcion, "Ingrese una breve descripción");
            String precioTexto = obtenerTextoValido(vista.txtPrecio, "Ingrese el precio en pesos colombianos");

            // --- CÁLCULO DE DURACIÓN EN MINUTOS ---
            int horas = 0;
            if (vista.spinDuracionHoras != null) {
                horas = (Integer) vista.spinDuracionHoras.getValue();
            }

            int minutos = 0;
            if (vista.comboDuracionMinutos != null && vista.comboDuracionMinutos.getSelectedItem() != null) {
                String comboTexto = vista.comboDuracionMinutos.getSelectedItem().toString();
                comboTexto = comboTexto.replaceAll("[^0-9]", "");
                if (!comboTexto.isEmpty()) {
                    minutos = Integer.parseInt(comboTexto);
                }
            }

            int totalDuracionMinutos = (horas * 60) + minutos;

            // 1. Validar campos obligatorios
            if (nombre.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(vista,
                        "Por favor complete los campos obligatorios: Nombre del servicio y Precio.",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Validación de conversión para el precio
            double precio;
            try {
                precio = Double.parseDouble(precioTexto.replace(',', '.'));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista,
                        "El formato del precio es incorrecto. Ingrese un valor numérico válido (Ej: 25000 o 25000.50).",
                        "Formato Numérico Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (precio <= 0) {
                JOptionPane.showMessageDialog(vista,
                        "El precio ingresado debe ser mayor a cero.",
                        "Precio Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Construcción del objeto modelo Servicios
            Servicios nuevoServicio = new Servicios();
            nuevoServicio.setNombreServicio(nombre);
            nuevoServicio.setDescripcion(descripcion);
            nuevoServicio.setDuracionMinutos(totalDuracionMinutos);
            nuevoServicio.setPrecio(precio);
            nuevoServicio.setActivo(true); // Se garantiza que ingrese como servicio activo (1)

            // Invocación del DAO para persistencia en base de datos
            boolean registrado = servicioDao.registrarServicio(nuevoServicio);

            if (registrado) {
                JOptionPane.showMessageDialog(vista,
                        "Servicio '" + nombre + "' guardado exitosamente con una duración de " + totalDuracionMinutos + " min.",
                        "NexusGO - Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista,
                        "Ocurrió un problema al intentar registrar el servicio en la base de datos.",
                        "Error de Almacenamiento", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NullPointerException npe) {
            JOptionPane.showMessageDialog(vista,
                    "Error al intentar leer la vista: " + npe.getMessage(),
                    "Error de Componente", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error en el canal de datos o en la consulta SQL: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Método auxiliar para evitar que los textos del placeholder de la vista se lean como datos reales.
     */
    private String obtenerTextoValido(JTextField txtField, String placeholder) {
        if (txtField == null) return "";
        String texto = txtField.getText().trim();
        return texto.equals(placeholder) ? "" : texto;
    }

    /*
     * Acción para examinar y cargar la imagen del servicio.
     */
    private void cargarImagen() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar imagen del servicio");
            
            // Filtro para formatos de imagen
            FileNameExtensionFilter filtro = 
                    new FileNameExtensionFilter("Archivos de Imagen (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg");
            fileChooser.setFileFilter(filtro);

            int resultado = fileChooser.showOpenDialog(vista);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();
                if (vista.lblNombreImagen != null) {
                    vista.lblNombreImagen.setText(archivoSeleccionado.getName());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo cargar el archivo seleccionado: " + e.getMessage(),
                    "Error al Cargar Imagen", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Acción del botón Volver
     */
    private void accionVolver() {
        JOptionPane.showMessageDialog(vista, "Regresando al menú principal...", "NexusGO", JOptionPane.INFORMATION_MESSAGE);
    }

    /*
     * Acción para cerrar sesión
     */
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de que desea cerrar la sesión actual?",
                "NexusGO - Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            Window window = SwingUtilities.getWindowAncestor(vista);
            if (window != null) {
                window.dispose();
            }
        }
    }

    /*
     * Restablece los campos de texto, el spinner y los combos a sus valores iniciales.
     */
    private void limpiarCampos() {
        try {
            if (vista.txtNombreServicio != null) vista.txtNombreServicio.setText("Ingrese el nombre del servicio");
            if (vista.txtDescripcion != null) vista.txtDescripcion.setText("Ingrese una breve descripción");
            if (vista.spinDuracionHoras != null) vista.spinDuracionHoras.setValue(1);
            if (vista.comboDuracionMinutos != null) vista.comboDuracionMinutos.setSelectedIndex(0);
            if (vista.txtPrecio != null) vista.txtPrecio.setText("Ingrese el precio en pesos colombianos");
            if (vista.lblNombreImagen != null) vista.lblNombreImagen.setText("imagenservicio.png");

            // Dejar el cursor en el primer campo
            if (vista.txtNombreServicio != null) {
                vista.txtNombreServicio.requestFocus();
            }
        } catch (Exception e) {
            System.err.println("Error al reiniciar los componentes de entrada: " + e.getMessage());
        }
    }
}
