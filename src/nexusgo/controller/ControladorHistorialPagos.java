/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.Factura;
import nexusgo.model.FacturaDao;
import nexusgo.model.Usuario;
import nexusgo.view.VistaHstorialPagos;


/**
 *
 * @author USUARIO
 */
public class ControladorHistorialPagos {
    
    private final VistaHstorialPagos vista;
    private final FacturaDao facturaDao;
    private final Usuario usuarioLogueado;
    private List<Factura> listaFacturas;

    // Constructor corregido con el nombre de clase VistaHstorialPagos
    public ControladorHistorialPagos(VistaHstorialPagos vista, Usuario usuarioLogueado) {
        this.vista = vista;
        this.usuarioLogueado = usuarioLogueado;
        this.facturaDao = new FacturaDao();

        // Inicializar vista con datos y eventos
        cargarHistorialPagos();
        configurarEventos();
    }

    /**
     * Carga las facturas registradas en la BD para el cliente en la JTable.
     */
    public void cargarHistorialPagos() {
        // Obtener el modelo directamente de la tabla en la vista
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaPagos.getModel();
        modelo.setRowCount(0); // Limpiar filas previas

        // Consultar facturas del cliente
        listaFacturas = facturaDao.obtenerFacturasPorCliente(usuarioLogueado.getIdUsuario());

        if (listaFacturas != null) {
            for (Factura f : listaFacturas) {

                // Construir concepto dinámico (Servicio o lista de productos)
                String concepto = resolverConceptoFactura(f);

                // Agregar fila al modelo de la tabla
                modelo.addRow(new Object[]{
                    f.getFechaVenta(),
                    concepto,
                    "$ " + String.format("%.2f", f.getTotal()),
                    "Descargar PDF" // Etiqueta o botón de acción
                });
            }
        }
    }

    /**
     * Determina si la factura corresponde a un servicio de cita o a productos comprados.
     */
    private String resolverConceptoFactura(Factura f) {
        // Si proviene de una cita con servicio
        if (f.getNombreServicio() != null && !f.getNombreServicio().isEmpty()) {
            return "Servicio: " + f.getNombreServicio();
        }

        // Si tiene productos en el detalle
        List<DetalleCarrito> detalles = f.getDetalles();
        if (detalles != null && !detalles.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (DetalleCarrito item : detalles) {
                sb.append(item.getNombreProducto()).append(", ");
            }
            // Eliminar la última coma
            String productos = sb.toString();
            return productos.endsWith(", ") 
                ? productos.substring(0, productos.length() - 2) 
                : productos;
        }

        return "Compra General / Servicio";
    }

    /**
     * Escucha el clic sobre la fila seleccionada para descargar el PDF y enviarlo por correo.
     */
    private void configurarEventos() {
        vista.tablaPagos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = vista.tablaPagos.getSelectedRow();
                int columnaSeleccionada = vista.tablaPagos.getSelectedColumn();

                // Columna 3 corresponde a la columna de 'Acción' (Descargar PDF)
                if (filaSeleccionada != -1 && columnaSeleccionada == 3) {

                    Factura facturaSeleccionada = listaFacturas.get(filaSeleccionada);

                    // 1. Generar el documento PDF
                    String rutaPdf = GeneradorFacturaPdf.generarPdf(facturaSeleccionada);

                    if (rutaPdf != null) {
                        // 2. Enviar comprobante por correo al email del usuario logueado
                        String correoDestino = usuarioLogueado.getCorreo();
                        boolean enviado = GeneradorFacturaPdf.enviarCorreo(correoDestino, rutaPdf);

                        if (enviado) {
                            JOptionPane.showMessageDialog(
                                vista,
                                "Factura guardada exitosamente en:\n" + rutaPdf +
                                "\n\nSe ha enviado una copia a: " + correoDestino,
                                "Comprobante Generado",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                vista,
                                "El PDF se guardó en el equipo, pero no se pudo enviar el correo de respaldo.",
                                "Aviso de Envío",
                                JOptionPane.WARNING_MESSAGE
                            );
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                            vista,
                            "Error al intentar generar la factura en PDF.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }
}
