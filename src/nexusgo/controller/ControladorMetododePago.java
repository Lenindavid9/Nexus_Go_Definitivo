/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.Desktop;
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
import javax.swing.SwingWorker;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;

/**
 *
 * @author USUARIO
 */
public class ControladorMetododePago {

    private VistaMetododePago vistaPrincipal;
    private DineroEfectivo vistaEfectivo;
    private JPanel panelContenedor;

    private double totalFactura;
    private String clienteIdActual = "";
    private String tipoCliente = "General";
    private int idClienteRealBD = 0; // Guardará el PK (id_usuario) de la BD
    
    private UsuarioDao usuarioDao = new UsuarioDao();
    private FacturaDao facturaDao;
    private List<DetalleCarrito> carritoActual;
    private int idCajaActual = 0;

    // 1. Constructor Completo
     public ControladorMetododePago(VistaMetododePago vistaPago, DineroEfectivo vistaEfectivo, List<DetalleCarrito> carrito, double totalVenta, JPanel contenedorCentral) {
        this.vistaPrincipal = vistaPago;
        this.vistaEfectivo = vistaEfectivo;
        this.carritoActual = carrito;
        this.panelContenedor = contenedorCentral;
        this.facturaDao = new FacturaDao();
        this.idCajaActual = idCajaActual;
        inicializarControlador(totalVenta);
    }

    // 2. Constructor Sobrecargado (Compatibilidad)
     public ControladorMetododePago(VistaMetododePago vistaPago, List<DetalleCarrito> carrito, double totalVenta, JPanel contenedorCentral) {
        this.vistaPrincipal = vistaPago;
        this.vistaEfectivo = new DineroEfectivo();
        this.carritoActual = carrito;
        this.panelContenedor = contenedorCentral;
        this.facturaDao = new FacturaDao();
        this.idCajaActual = idCajaActual;
        inicializarControlador(totalVenta);
    }

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
                idClienteRealBD = 0;
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

    private void initListenersVistaEfectivo() {
        if (this.vistaEfectivo.getBtnCalcularP() != null) {
            this.vistaEfectivo.getBtnCalcularP().addActionListener(e -> calcularCambioEfectivo());
        }

        if (this.vistaEfectivo.getBtnConfirmarPago() != null) {
            this.vistaEfectivo.getBtnConfirmarPago().addActionListener(e -> finalizarVenta("Efectivo"));
        }
    }

    private void buscarCliente() {
        String numIdentificacion = vistaPrincipal.getNumId().getText().trim();
        if (numIdentificacion.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Por favor ingrese un número de identificación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Consultamos a la BD a través de UsuarioDao pasando el número de documento
        Usuario clienteEncontrado = usuarioDao.buscarUsuarioPorIdentificacion(numIdentificacion);

        if (clienteEncontrado != null) {
            clienteIdActual = numIdentificacion;
            idClienteRealBD = clienteEncontrado.getIdUsuario(); // Extraemos la clave primaria (PK) real
            JOptionPane.showMessageDialog(
                vistaPrincipal, 
                "Cliente verificado: " + clienteEncontrado.getNombre() + " " + clienteEncontrado.getApellido(), 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            idClienteRealBD = 0;
            clienteIdActual = "";
            JOptionPane.showMessageDialog(vistaPrincipal, "Cliente no encontrado en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarSeleccionPago() {
        if ("Registrado".equals(tipoCliente) && idClienteRealBD <= 0) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Debe buscar y validar una cédula registrada válida.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metodo = vistaPrincipal.getMetodoSeleccionado();

        switch (metodo) {
            case "Efectivo":
                cambiarVistaContenedor(vistaEfectivo);
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
            // 1. Instanciar y estructurar el objeto Factura
            Factura factura = new Factura();
            factura.setFechaVenta(new Date());
            factura.setSubtotal(totalFactura); // Asignar subtotal
            factura.setTotal(totalFactura);
            factura.setDetalles(carritoActual);

            // Asignar id_cliente según corresponda (0 para Cliente General -> NULL en BD)
            if ("Registrado".equals(tipoCliente) && idClienteRealBD > 0) {
                factura.setIdCliente(idClienteRealBD);
            } else {
                factura.setIdCliente(0); 
            }

            // Asignar ID de caja abierta por defecto (1) para cumplir restricción FK
            if (idCajaActual <= 0){
                JOptionPane.showMessageDialog(null, "No hay una caja abierta válida. No se puede registrar la venta.",
                        "Caja no disponible", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            factura.setIdCaja(idCajaActual);

            // 2. Guardar en Base de Datos a través del DAO
            boolean guardadoExitoso = facturaDao.guardarFactura(factura);

            if (guardadoExitoso) {
                JOptionPane.showMessageDialog(
                        null,
                        "¡Pago Exitoso!\nFactura registrada y guardada en la base de datos.",
                        "Proceso Completado",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // 3. Generar el PDF de la factura
                String rutaPdf = GeneradorFacturaPdf.generarPdf(factura);

                // 4. Instanciar la vista final de la factura
                VistaFactura vistaFactura = new VistaFactura(factura, carritoActual);

                // Evento: Abrir o imprimir PDF
                vistaFactura.getBtnImprimir().addActionListener(evt -> {
                    try {
                        if (rutaPdf != null && Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(new File(rutaPdf));
                        } else {
                            JOptionPane.showMessageDialog(vistaFactura, "No se pudo encontrar el archivo PDF.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vistaFactura, "Error al abrir el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                // Evento: Enviar correo en segundo plano (Evita congelar Swing)
                vistaFactura.getBtnEnviar().addActionListener(evt -> {
                    String correo = JOptionPane.showInputDialog(vistaFactura, "Ingrese el correo electrónico del cliente:");

                    if (correo != null && !correo.trim().isEmpty()) {
                        vistaFactura.getBtnEnviar().setEnabled(false);

                        new SwingWorker<Boolean, Void>() {
                            @Override
                            protected Boolean doInBackground() {
                                return GeneradorFacturaPdf.enviarCorreo(correo.trim(), rutaPdf);
                            }

                            @Override
                            protected void done() {
                                vistaFactura.getBtnEnviar().setEnabled(true);
                                try {
                                    boolean enviado = get();
                                    if (enviado) {
                                        JOptionPane.showMessageDialog(vistaFactura, "¡Factura enviada por correo exitosamente!", "Correo Enviado", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(vistaFactura, "No se pudo enviar el correo. Verifique la configuración SMTP.", "Error de Envío", JOptionPane.ERROR_MESSAGE);
                                    }
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(vistaFactura, "Error al enviar correo: " + ex.getMessage(), "Excepción", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }.execute();
                    }
                });

                // 5. Mostrar la pantalla final de factura y reiniciar variables del controlador
                cambiarVistaContenedor(vistaFactura);
                reiniciarFormulario();

            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Ocurrió un error al guardar la factura en la base de datos.\nRevise la consola para ver los detalles.",
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

    // Auxiliar para alternar paneles en el contenedor central
    private void cambiarVistaContenedor(JPanel nuevaVista) {
        if (panelContenedor != null) {
            panelContenedor.removeAll();
            panelContenedor.setLayout(new BorderLayout());
            panelContenedor.add(nuevaVista, BorderLayout.CENTER);
            panelContenedor.revalidate();
            panelContenedor.repaint();
        }
    }

    private void reiniciarFormulario() {
        clienteIdActual = "";
        idClienteRealBD = 0;
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
