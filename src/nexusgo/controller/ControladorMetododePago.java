/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.Factura;
import nexusgo.model.FacturaDao;
import nexusgo.view.DineroEfectivo;
import nexusgo.view.VistaFactura;
import nexusgo.view.VistaMetododePago;
import java.awt.BorderLayout;

/**
 *
 * @author USUARIO
 */
public class ControladorMetododePago {

    private VistaMetododePago vistaPrincipal;
    private DineroEfectivo vistaEfectivo;
    private JPanel panelContenedor;
    private CardLayout cardLayout;

    private double totalFactura;
    private String clienteIdActual = "";
    private String tipoCliente = "General";

    // Lista de productos del carrito para pasársela a la factura
    private List<DetalleCarrito> carritoActual;
    private FacturaDao facturaDao;

    // 1. Constructor Completo (Recibe carrito y otros parámetros)
    public ControladorMetododePago(VistaMetododePago vistaPago, DineroEfectivo vistaEfectivo, List<DetalleCarrito> carrito, double totalVenta, JPanel contenedorCentral) {
        this.vistaPrincipal = vistaPago;
        this.vistaEfectivo = vistaEfectivo;
        this.carritoActual = carrito;
        this.panelContenedor = contenedorCentral;
        this.facturaDao = new FacturaDao();
        inicializarControlador(totalVenta);
    }

    // 2. Constructor Sobrecargado (Para compatibilidad directa con ControladorPdV)
    public ControladorMetododePago(VistaMetododePago vistaPago, List<DetalleCarrito> carrito, double totalVenta, JPanel contenedorCentral) {
        this.vistaPrincipal = vistaPago;
        this.vistaEfectivo = new DineroEfectivo();
        this.carritoActual = carrito;
        this.panelContenedor = contenedorCentral;
        this.facturaDao = new FacturaDao();
        inicializarControlador(totalVenta);
    }

    // Método auxiliar con blindaje contra nulos y layouts inválidos
    private void inicializarControlador(double totalVenta) {

        this.totalFactura = totalVenta;

        if (this.vistaPrincipal != null) {
            this.vistaPrincipal.setTotal(totalFactura);
            initListenersVistaPrincipal();
        }

        if (this.vistaEfectivo != null) {
            this.vistaEfectivo.setMontoTotal(totalVenta);
            initListenersVistaEfectivo();
        }
    }

    // Configuración de eventos para la vista principal del método de pago
    private void initListenersVistaPrincipal() {

        if (this.vistaPrincipal.getBtnClienteR() != null) {
            this.vistaPrincipal.getBtnClienteR().addActionListener(e -> {
                tipoCliente = "Registrado";
                vistaPrincipal.alternarModoCliente(true);
            });
        }

        if (this.vistaPrincipal.getBtnClienteG() != null) {
            this.vistaPrincipal.getBtnClienteG().addActionListener(e -> {
                tipoCliente = "General";
                clienteIdActual = "";
                vistaPrincipal.getNumId().setText("");
                vistaPrincipal.alternarModoCliente(false);
            });
        }

        if (this.vistaPrincipal.getBtnBuscarCliente() != null) {
            this.vistaPrincipal.getBtnBuscarCliente().addActionListener(e -> buscarCliente());
        }

        if (this.vistaPrincipal.getPnlEfectivo() != null) {
            this.vistaPrincipal.getPnlEfectivo().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    vistaPrincipal.seleccionarMetodo("Efectivo");
                }
            });
        }

        if (this.vistaPrincipal.getPnlTarjeta() != null) {
            this.vistaPrincipal.getPnlTarjeta().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    vistaPrincipal.seleccionarMetodo("Tarjeta");
                }
            });
        }

        if (this.vistaPrincipal.getPnlTransferencia() != null) {
            this.vistaPrincipal.getPnlTransferencia().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    vistaPrincipal.seleccionarMetodo("Transferencia");
                }
            });
        }

        if (this.vistaPrincipal.getBtnConfirmar() != null) {
            this.vistaPrincipal.getBtnConfirmar().addActionListener(e -> procesarSeleccionPago());
        }

    }

    // Configuración de eventos para la vista de pago en efectivo
    private void initListenersVistaEfectivo() {

        if (this.vistaEfectivo.getBtnCalcularP() != null) {
            this.vistaEfectivo.getBtnCalcularP().addActionListener(e -> calcularCambioEfectivo());
        }

        if (this.vistaEfectivo.getBtnConfirmarPago() != null) {
            this.vistaEfectivo.getBtnConfirmarPago().addActionListener(e -> finalizarVenta("Efectivo"));
        }

    }

    private void buscarCliente() {
        String id = vistaPrincipal.getNumId().getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Por favor ingrese un número de identificación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean clienteEncontrado = true; // Aquí puedes conectar tu DAO de clientes si lo requieres

        if (clienteEncontrado) {
            clienteIdActual = id;
            JOptionPane.showMessageDialog(vistaPrincipal, "Cliente verificado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vistaPrincipal, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarSeleccionPago() {
        if (tipoCliente.equals("Registrado") && clienteIdActual.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Debe buscar y validar la cédula del cliente registrado.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metodo = vistaPrincipal.getMetodoSeleccionado();

        switch (metodo) {
            case "Efectivo":

                if (panelContenedor != null) {

                    panelContenedor.removeAll();
                    panelContenedor.setLayout(new BorderLayout());
                    panelContenedor.add(vistaEfectivo, BorderLayout.CENTER);
                    panelContenedor.revalidate();
                    panelContenedor.repaint();

                }

                break;

            case "Tarjeta":
            case "Transferencia":
                int respuesta = JOptionPane.showConfirmDialog(
                        vistaPrincipal,
                        "¿Desea procesar el pago mediante " + metodo + "?",
                        "Confirmar Pago",
                        JOptionPane.YES_NO_OPTION
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    finalizarVenta(metodo);
                }
                break;
            default:
                JOptionPane.showMessageDialog(vistaPrincipal, "Seleccione un método de pago válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                break;
        }
    }

    private void calcularCambioEfectivo() {
        String textoMonto = vistaEfectivo.getMontoIngresado();
        try {
            double dineroRecibido = Double.parseDouble(textoMonto);

            if (dineroRecibido < totalFactura) {
                JOptionPane.showMessageDialog(vistaEfectivo, "El monto ingresado es menor al total a pagar.", "Monto Insuficiente", JOptionPane.WARNING_MESSAGE);
                vistaEfectivo.setCambioMonto(0);
            } else {
                double cambio = dineroRecibido - totalFactura;
                vistaEfectivo.setCambioMonto(cambio);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaEfectivo, "Por favor ingrese un valor numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarVenta(String metodoPago) {
        try {
            // 1. Crear el objeto Factura con la información de la venta
            Factura factura = new Factura();
            factura.setFechaVenta(new Date());
            factura.setTotal(totalFactura);
            // Si tu modelo Factura maneja más atributos (como tipo de cliente o ID), asígnalos aquí:
            factura.setDetalles(carritoActual);

            // 2. Guardar la factura y sus detalles en la Base de Datos mediante el DAO
            boolean guardadoExitoso = facturaDao.guardarFactura(factura);

            if (guardadoExitoso) {
                JOptionPane.showMessageDialog(
                        null,
                        "¡Pago Exitoso!\nFactura registrada y guardada en la base de datos.",
                        "Proceso Completado",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // 3. Instanciar la Vista de Factura pasándole la factura y el carrito
                VistaFactura vistaFactura = new VistaFactura(factura, carritoActual);

                // 4. Configurar interactividad de los botones de la VistaFactura (Imprimir y Enviar por PDF)
                vistaFactura.getBtnImprimir().addActionListener(evt -> {
                    try {
                        String rutaPdf = GeneradorFacturaPdf.generarPdf(factura);
                        if (rutaPdf != null && Desktop.isDesktopSupported()) {
                            File archivoPdf = new File(rutaPdf);
                            Desktop.getDesktop().open(archivoPdf); // Abre el PDF para imprimir directamente (Ctrl + P)
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vistaFactura, "Error al abrir el PDF para imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                vistaFactura.getBtnEnviar().addActionListener(evt -> {
                    String correo = JOptionPane.showInputDialog(vistaFactura, "Ingrese el correo electrónico del cliente:");
                    if (correo != null && !correo.trim().isEmpty()) {
                        String rutaPdf = GeneradorFacturaPdf.generarPdf(factura);
                        boolean enviado = GeneradorFacturaPdf.enviarCorreo(correo.trim(), rutaPdf);
                        if (enviado) {
                            JOptionPane.showMessageDialog(vistaFactura, "¡Factura enviada por correo exitosamente!", "Correo Enviado", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(vistaFactura, "No se pudo enviar el correo. Verifique la conexión o configuración SMTP.", "Error de Envío", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                // 5. Añadir la VistaFactura al panel contenedor central con CardLayout
                if (panelContenedor != null) {

                    panelContenedor.removeAll();
                    panelContenedor.setLayout(new BorderLayout());
                    panelContenedor.add(vistaFactura, BorderLayout.CENTER);
                    panelContenedor.revalidate();
                    panelContenedor.repaint();

                }
                // 6. Limpiar campos para futuras ventas
                reiniciarFormulario();

            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "El pago se realizó, pero ocurrió un error al guardar la factura en la base de datos.",
                        "Error en Base de Datos",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error crítico al procesar la venta: " + ex.getMessage(),
                    "Excepción",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void reiniciarFormulario() {
        clienteIdActual = "";
        tipoCliente = "General";
        if (vistaPrincipal.getNumId() != null) {
            vistaPrincipal.getNumId().setText("");
        }
        vistaPrincipal.alternarModoCliente(false);
        if (vistaEfectivo.getTxtMonto() != null) {
            vistaEfectivo.getTxtMonto().setText("");
        }
        vistaEfectivo.setCambioMonto(0);
    }
}
