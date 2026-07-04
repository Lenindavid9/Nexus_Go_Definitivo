package nexusgo.controller;

import nexusgo.view.MetododePago;
import nexusgo.model.Cliente;
import nexusgo.model.ClienteDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import nexusgo.view.Factura;


public class ControladorMdP implements ActionListener {

    private MetododePago vista;
    private ClienteDao dao;

    public ControladorMdP(MetododePago vista) {
        this.vista = vista;
        this.dao = new ClienteDao();

        vista.getBtnVolver().addActionListener(this);
        vista.getBtnConfirmar().addActionListener(this);

        vista.getBtnClienteR().addActionListener(e -> {
            vista.getNumId().setEnabled(true);
            vista.getNumId().requestFocus();
        });

        vista.getBtnClienteG().addActionListener(e -> {
            vista.getNumId().setEnabled(false);  
            vista.getNumId().setText("");       
        });


        vista.getBtnBuscarCliente().addActionListener(e -> buscarCliente());
        vista.getBtnTarjeta().addActionListener(e -> vista.getBtnConfirmar().setEnabled(true));
    vista.getBtnTransferencia().addActionListener(e -> vista.getBtnConfirmar().setEnabled(true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Evento Volver
        if (e.getSource() == vista.getBtnVolver()) {
            JFrame frame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(vista);
            frame.dispose();
            System.out.println("Volviendo al Punto de Venta...");
        }else if (e.getSource() == vista.getBtnConfirmar()) {
        abrirFactura(); 
    }

    }

    private void buscarCliente() {
        String numeroId = vista.getNumId().getText().trim();

        if (numeroId.isEmpty()) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor ingresa un número de identificación.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = dao.buscarPorIdentificacion(numeroId);

        if (cliente != null) {
            JOptionPane.showMessageDialog(vista,
                    "Cliente existente: " + cliente.getNombre(),
                    "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista,
                    "Cliente no existe",
                    "Resultado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirFactura() {
        Factura factura = new Factura();
        factura.crearVistaFactura();

        if (vista.getNumId().isEnabled()) {
            String numeroId = vista.getNumId().getText().trim();
            Cliente cliente = dao.buscarPorIdentificacion(numeroId);
            if (cliente != null) {
                factura.setClienteRegistrado(cliente.getNombre(), cliente.getNumeroIdentificacion());
            } else {
                factura.setClienteGeneral();
            }
        } else {
            factura.setClienteGeneral();
        }

        JFrame ventanaFactura = new JFrame("Factura");
        ventanaFactura.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaFactura.getContentPane().add(factura);
        ventanaFactura.setSize(800, 600);
        ventanaFactura.setLocationRelativeTo(null);
        ventanaFactura.setVisible(true);
    }

}
