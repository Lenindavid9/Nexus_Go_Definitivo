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
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaPagos.getModel();
        modelo.setRowCount(0); // Limpiar filas previas

        listaFacturas = facturaDao.obtenerFacturasPorCliente(usuarioLogueado.getIdUsuario());

        if (listaFacturas != null && !listaFacturas.isEmpty()) {
            for (Factura f : listaFacturas) {
                String concepto = resolverConceptoFactura(f);

                modelo.addRow(new Object[]{
                    f.getFechaVenta(),
                    concepto,
                    "$ " + String.format("%.2f", f.getTotal()),
                    "Descargar PDF"
                });
            }
        } else {
            System.out.println("No se encontraron facturas para el usuario ID: " + usuarioLogueado.getIdUsuario());
        }

        // Refrescar el componente visual
        vista.tablaPagos.revalidate();
        vista.tablaPagos.repaint();
    }

    /**
     * Determina si la factura corresponde a un servicio de cita o a productos comprados.
     */
    private String resolverConceptoFactura(Factura f) {
        if (f.getNombreServicio() != null && !f.getNombreServicio().isEmpty()) {
            return "Servicio: " + f.getNombreServicio();
        }

        List<DetalleCarrito> detalles = f.getDetalles();
        if (detalles != null && !detalles.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (DetalleCarrito item : detalles) {
                sb.append(item.getNombreProducto()).append(", ");
            }
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
                int filaVista = vista.tablaPagos.getSelectedRow();
                int columnaSeleccionada = vista.tablaPagos.getSelectedColumn();

                if (filaVista != -1 && columnaSeleccionada == 3) {
                    // Convertir índice visual al del modelo por si la tabla fue ordenada
                    int filaModelo = vista.tablaPagos.convertRowIndexToModel(filaVista);
                    Factura facturaSeleccionada = listaFacturas.get(filaModelo);

                    String rutaPdf = GeneradorFacturaPdf.generarPdf(facturaSeleccionada);

                    if (rutaPdf != null && !rutaPdf.trim().isEmpty()) {
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
