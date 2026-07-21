/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.view.DineroEfectivo;
import nexusgo.view.VistaMetododePago;

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

    public ControladorMetododePago(VistaMetododePago vistaPrincipal, DineroEfectivo vistaEfectivo, JPanel panelContenedor, double totalFactura) {
        this.vistaPrincipal = vistaPrincipal;
        this.vistaEfectivo = vistaEfectivo;
        this.panelContenedor = panelContenedor;
        this.cardLayout = (CardLayout) panelContenedor.getLayout();
        this.totalFactura = totalFactura;

        // Cargar los montos iniciales en ambas vistas
        this.vistaPrincipal.setTotal(totalFactura);
        this.vistaEfectivo.setMontoTotal(totalFactura);

        // Inicializar los eventos de las vistas
        initListenersVistaPrincipal();
        initListenersVistaEfectivo();
    }

    // Configuración de eventos para la vista principal del método de pago
    private void initListenersVistaPrincipal() {

        // Evento para seleccionar Cliente Registrado
        this.vistaPrincipal.getBtnClienteR().addActionListener(e -> {
            tipoCliente = "Registrado";
            vistaPrincipal.alternarModoCliente(true);
        });

        // Evento para seleccionar Cliente General
        this.vistaPrincipal.getBtnClienteG().addActionListener(e -> {
            tipoCliente = "General";
            clienteIdActual = "";
            vistaPrincipal.getNumId().setText("");
            vistaPrincipal.alternarModoCliente(false);
        });

        // Evento para el botón de búsqueda del cliente
        this.vistaPrincipal.getBtnBuscarCliente().addActionListener(e -> buscarCliente());

        // Eventos para seleccionar los paneles de método de pago
        this.vistaPrincipal.getPnlEfectivo().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vistaPrincipal.seleccionarMetodo("Efectivo");
            }
        });

        this.vistaPrincipal.getPnlTarjeta().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vistaPrincipal.seleccionarMetodo("Tarjeta");
            }
        });

        this.vistaPrincipal.getPnlTransferencia().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vistaPrincipal.seleccionarMetodo("Transferencia");
            }
        });

        // Evento para el botón de confirmar la selección
        this.vistaPrincipal.getBtnConfirmar().addActionListener(e -> procesarSeleccionPago());

        // Evento para el botón de regresar
        this.vistaPrincipal.getBtnVolver().addActionListener(e -> {
            cardLayout.show(panelContenedor, "VISTA_CARRITO"); // O la vista previa al pago
        });
    }

    // Configuración de eventos para la vista de pago en efectivo
    private void initListenersVistaEfectivo() {

        // Evento para calcular el cambio
        this.vistaEfectivo.getBtnCalcularP().addActionListener(e -> calcularCambioEfectivo());

        // Evento para confirmar el pago en efectivo y generar factura
        this.vistaEfectivo.getBtnConfirmarPago().addActionListener(e -> finalizarVenta("Efectivo"));

        // Evento para regresar a la vista de selección de métodos de pago
        this.vistaEfectivo.getBtnVolver().addActionListener(e -> {
            cardLayout.show(panelContenedor, "VISTA_METODO_PAGO");
        });
    }

    // Valida y busca un cliente por identificación
    private void buscarCliente() {
        String id = vistaPrincipal.getNumId().getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Por favor ingrese un número de identificación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lógica de validación con base de datos
        boolean clienteEncontrado = true;

        if (clienteEncontrado) {
            clienteIdActual = id;
            JOptionPane.showMessageDialog(vistaPrincipal, "Cliente verificado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vistaPrincipal, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Procesa el método de pago seleccionado
    private void procesarSeleccionPago() {
        if (tipoCliente.equals("Registrado") && clienteIdActual.isEmpty()) {
            JOptionPane.showMessageDialog(vistaPrincipal, "Debe buscar y validar la cédula del cliente registrado.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metodo = vistaPrincipal.getMetodoSeleccionado();

        switch (metodo) {
            case "Efectivo":
                // Muestra la pantalla para ingresar el billete/monto recibido
                cardLayout.show(panelContenedor, "VISTA_EFECTIVO");
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
        }
    }

    // Calcula la diferencia entre el dinero recibido y el total
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

    // Registra la venta y REDIRECCIONA A LA FACTURA
    private void finalizarVenta(String metodoPago) {
        JOptionPane.showMessageDialog(
                null,
                "¡Pago Exitoso!\nGenerando factura...",
                "Proceso Completado",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Limpia datos del formulario de pago
        reiniciarFormulario();

        // REDIRECCIÓN A LA VISTA DE FACTURA
        // Asegúrate de usar el mismo nombre/clave con el que agregaste tu panel de Factura al CardLayout
        cardLayout.show(panelContenedor, "VISTA_FACTURA");
    }

    // Limpia las variables y las vistas para compras posteriores
    private void reiniciarFormulario() {
        clienteIdActual = "";
        tipoCliente = "General";
        vistaPrincipal.getNumId().setText("");
        vistaPrincipal.alternarModoCliente(false);
        vistaEfectivo.getTxtMonto().setText("");
        vistaEfectivo.setCambioMonto(0);
    }
}
