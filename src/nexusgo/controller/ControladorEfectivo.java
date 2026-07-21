/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.Factura;
import nexusgo.model.FacturaDao;
import nexusgo.view.DineroEfectivo;
import nexusgo.view.VistaFactura;
/**
 *
 * @author USUARIO
 */
public class ControladorEfectivo implements ActionListener {
    
    private final DineroEfectivo vistaEfectivo;
    private final List<DetalleCarrito> carrito;
    private final double totalVenta;
    private final JPanel contenedorCentral;
    private final FacturaDao facturaDao;
    
    private double cambioCalculado = 0.0;

    public ControladorEfectivo(DineroEfectivo vistaEfectivo, List<DetalleCarrito> carrito, double totalVenta, JPanel contenedorCentral) {
        this.vistaEfectivo = vistaEfectivo;
        this.carrito = carrito;
        this.totalVenta = totalVenta;
        this.contenedorCentral = contenedorCentral;
        this.facturaDao = new FacturaDao();

        // Cargar el total a pagar en la vista
        this.vistaEfectivo.setMontoTotal(totalVenta);

        // Enlazar listeners de los botones de la vista
        this.vistaEfectivo.getBtnCalcularP().addActionListener(this);
        this.vistaEfectivo.getBtnConfirmarPago().addActionListener(this);
        this.vistaEfectivo.getBtnVolver().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaEfectivo.getBtnCalcularP()) {
            calcularCambio();
        } 
        else if (e.getSource() == vistaEfectivo.getBtnConfirmarPago()) {
            procesarPago();
        } 
        else if (e.getSource() == vistaEfectivo.getBtnVolver()) {
            // Regresar al panel anterior
            if (contenedorCentral != null) {
                contenedorCentral.remove(vistaEfectivo);
                contenedorCentral.revalidate();
                contenedorCentral.repaint();
            }
        }
    }

    private void calcularCambio() {
        try {
            String textoIngresado = vistaEfectivo.getMontoIngresado();
            if (textoIngresado.isEmpty()) {
                JOptionPane.showMessageDialog(vistaEfectivo, "Ingrese la cantidad recibida.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double dineroRecibido = Double.parseDouble(textoIngresado);

            if (dineroRecibido < totalVenta) {
                JOptionPane.showMessageDialog(vistaEfectivo, "El dinero recibido es insuficiente.", "Monto Insuficiente", JOptionPane.ERROR_MESSAGE);
                vistaEfectivo.setCambioMonto(0.0);
                cambioCalculado = -1;
            } else {
                cambioCalculado = dineroRecibido - totalVenta;
                vistaEfectivo.setCambioMonto(cambioCalculado);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vistaEfectivo, "Ingrese un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarPago() {
        if (cambioCalculado < 0) {
            JOptionPane.showMessageDialog(vistaEfectivo, "Verifique que el dinero recibido sea suficiente antes de confirmar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear la factura y registrar en la BD
        Factura factura = new Factura( 0, 1, 1, 1,totalVenta,0.0,    totalVenta,new Date(),    carrito);
        boolean exito = facturaDao.guardarFactura(factura);

        if (exito) {
            JOptionPane.showMessageDialog(vistaEfectivo, "Pago confirmado. Generando factura...", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Pasar a la pantalla final de la Factura
            VistaFactura vistaFactura = new VistaFactura(factura, carrito);
            
            if (contenedorCentral != null) {
                contenedorCentral.removeAll();
                contenedorCentral.setLayout(new BorderLayout());
                contenedorCentral.add(vistaFactura, BorderLayout.CENTER);
                contenedorCentral.revalidate();
                contenedorCentral.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(vistaEfectivo, "Ocurrió un error al registrar la factura en la base de datos.", "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
