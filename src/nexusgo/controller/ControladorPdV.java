package nexusgo.controller;

import nexusgo.view.MetododePago;
import nexusgo.view.VistaPdV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class ControladorPdV implements ActionListener {

    private final VistaPdV vista;
    private MetododePago metodoPago;
    private JFrame framePago;

    public ControladorPdV(VistaPdV vista) {
        this.vista = vista;

        if (this.vista.getFacturarButton() != null) {
            this.vista.getFacturarButton().addActionListener(this);
        }
        if (this.vista.getReiniciarButton() != null) {
            this.vista.getReiniciarButton().addActionListener(this);
        }
    }
    
    private void enlazarEventosPago() {
        metodoPago.getBtnVolver().addActionListener(this);
        metodoPago.getBtnClienteR().addActionListener(this);
        metodoPago.getBtnClienteG().addActionListener(this);
        metodoPago.getBtnBuscarCliente().addActionListener(this);
        metodoPago.getBtnConfirmar().addActionListener(this);
        metodoPago.getBtnEfectivo().addActionListener(this);
        metodoPago.getBtnTarjeta().addActionListener(this);
        metodoPago.getBtnTransferencia().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.getFacturarButton()) {
            metodoPago = new MetododePago();
            metodoPago.setTotal(vista.getTotalVenta());

            enlazarEventosPago();

            framePago = new JFrame("Método de Pago - Nexus");
            framePago.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            framePago.setContentPane(metodoPago.VistaMdP());
            framePago.pack();
            framePago.setLocationRelativeTo(null); 
            framePago.setVisible(true);
        }
        else if (e.getSource() == vista.getReiniciarButton()) {
            vista.reiniciarContador();
            System.out.println("Contador y total reiniciados desde el Controlador");
        }

        if (metodoPago != null) {
            if (e.getSource() == metodoPago.getBtnVolver()) {
                framePago.dispose(); 
            } 
            else if (e.getSource() == metodoPago.getBtnClienteG()) {
                metodoPago.getBtnConfirmar().setEnabled(true);
            }
            else if (e.getSource() == metodoPago.getBtnBuscarCliente()) {
                metodoPago.getBtnConfirmar().setEnabled(true);
            }
            else if (e.getSource() == metodoPago.getBtnConfirmar()) {
                framePago.dispose(); 
                vista.reiniciarContador();
            }
        }
    }
}
