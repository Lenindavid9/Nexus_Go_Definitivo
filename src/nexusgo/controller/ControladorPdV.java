package nexusgo.controller;

import Nexus_Vista.MetododePago;
import Nexus_Vista.VistaPdV; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class ControladorPdV implements ActionListener {

    private VistaPdV vista;

    public ControladorPdV(VistaPdV vista) {
        this.vista = vista;

        vista.getFacturarButton().addActionListener(this);
        vista.getReiniciarButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getFacturarButton()) {
            MetododePago metodoPago = new MetododePago();

            JFrame frame = new JFrame("Método de Pago - Nexus");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(metodoPago.VistaMdP());
            metodoPago.setTotal(vista.getTotalVenta());
            frame.pack();
            frame.setLocationRelativeTo(null); 
            frame.setVisible(true);
        }
        else if (e.getSource() == vista.getReiniciarButton()) {
            vista.reiniciarContador();
            System.out.println("Contador y total reiniciados desde el Controlador");
        }
    }
}
