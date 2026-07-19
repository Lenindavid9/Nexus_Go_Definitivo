package nexusgo.controller;

import nexusgo.view.MetododePago;
import nexusgo.model.Cliente;
import nexusgo.model.ClienteDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import nexusgo.view.Factura;

interface EstadoCliente {

    void seleccionarClienteGeneral();

    void seleccionarClienteRegistrado();

    void buscarCliente(String id);
}

class EstadoInicial implements EstadoCliente {

    private final ControladorMdP controlador;

    public EstadoInicial(ControladorMdP controlador) {
        this.controlador = controlador;
        controlador.habilitarMetodosPago(false);
        controlador.vista.getBtnConfirmar().setEnabled(false);
    }

    @Override
    public void seleccionarClienteGeneral() {
        controlador.habilitarMetodosPago(true);
        controlador.vista.getBtnConfirmar().setEnabled(false);
        controlador.setEstado(new EstadoClienteGeneral(controlador));
    }

    @Override
    public void seleccionarClienteRegistrado() {
        controlador.habilitarMetodosPago(false);
        controlador.vista.getNumId().setEnabled(true);
        controlador.vista.getBtnConfirmar().setEnabled(false);
        controlador.setEstado(new EstadoClienteRegistrado(controlador));
    }

    @Override
    public void buscarCliente(String id) {}
}

class EstadoClienteGeneral implements EstadoCliente {

    private final ControladorMdP controlador;

    public EstadoClienteGeneral(ControladorMdP controlador) {
        this.controlador = controlador;
        controlador.vista.getNumId().setEnabled(false);
        controlador.vista.getNumId().setText("");
        controlador.habilitarMetodosPago(true);
        controlador.vista.getBtnConfirmar().setEnabled(false);
    }

    @Override
    public void seleccionarClienteGeneral() {}

    @Override
    public void seleccionarClienteRegistrado() {
        controlador.vista.getNumId().setEnabled(true);
        controlador.habilitarMetodosPago(false);
        controlador.vista.getBtnConfirmar().setEnabled(false);
        controlador.setEstado(new EstadoClienteRegistrado(controlador));
    }

    @Override
    public void buscarCliente(String id) {}
}

class EstadoClienteRegistrado implements EstadoCliente {

    private final ControladorMdP controlador;

    public EstadoClienteRegistrado(ControladorMdP controlador) {
        this.controlador = controlador;
        controlador.habilitarMetodosPago(false);
        controlador.vista.getBtnConfirmar().setEnabled(false);
    }

    @Override
    public void seleccionarClienteGeneral() {
        controlador.setEstado(new EstadoClienteGeneral(controlador));
    }

    @Override
    public void seleccionarClienteRegistrado() {}

    @Override
    public void buscarCliente(String id) {
        Cliente cliente = controlador.dao.buscarPorIdentificacion(id);
        if (cliente != null) {
            JOptionPane.showMessageDialog(controlador.vista,
                    "Cliente existente: " + cliente.getNombre(),
                    "Resultado", JOptionPane.INFORMATION_MESSAGE);
            controlador.habilitarMetodosPago(true);
            controlador.vista.getBtnConfirmar().setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(controlador.vista,
                    "Cliente no existe",
                    "Resultado", JOptionPane.WARNING_MESSAGE);
            controlador.setEstado(new EstadoClienteGeneral(controlador));
        }
    }
}

public class ControladorMdP implements ActionListener {

    public MetododePago vista;
    public ClienteDao dao;
    private EstadoCliente estado;

    public ControladorMdP(MetododePago vista) {
        this.vista = vista;
        this.dao = new ClienteDao();
        this.estado = new EstadoInicial(this);

        vista.getBtnVolver().addActionListener(this);
        vista.getBtnConfirmar().addActionListener(this);

        vista.getBtnClienteR().addActionListener(e -> estado.seleccionarClienteRegistrado());
        vista.getBtnClienteG().addActionListener(e -> estado.seleccionarClienteGeneral());
        vista.getBtnBuscarCliente().addActionListener(e -> estado.buscarCliente(vista.getNumId().getText().trim()));

        vista.getBtnTarjeta().addActionListener(e -> vista.getBtnConfirmar().setEnabled(true));
        vista.getBtnTransferencia().addActionListener(e -> vista.getBtnConfirmar().setEnabled(true));
        vista.getBtnImgEfectivo().addActionListener(e -> vista.getBtnConfirmar().setEnabled(true));
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public void habilitarMetodosPago(boolean estado) {
        vista.getBtnTarjeta().setEnabled(estado);
        vista.getBtnTransferencia().setEnabled(estado);
        vista.getBtnImgEfectivo().setEnabled(estado);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnVolver()) {
            JFrame frame = (JFrame) javax.swing.SwingUtilities.getWindowAncestor(vista);
            frame.dispose();
        } else if (e.getSource() == vista.getBtnConfirmar()) {
            abrirFactura();
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
