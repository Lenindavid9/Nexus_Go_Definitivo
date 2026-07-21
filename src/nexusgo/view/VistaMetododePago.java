package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class VistaMetododePago extends JPanel {

    private JPanel principal, pnlClientes, pnlIngresoId, pnlMdP, pnlResumen, pnlbtnVolver, pnlOpcionesPago;
    private JLabel lbltxtIngreso, lbltxtMdP, lbltxtIndicacion, lblTotal;
    private JButton btnClienteR, btnClienteG, btnVolver, btnConfirmar, btnBuscarCliente;
    private JTextField numId;
    private JPanel pnlEfectivo, pnlTarjeta, pnlTransferencia;

    private final Color COLOR_DORADO = new Color(223, 205, 141);
    private final Color COLOR_GRIS_CLARO = new Color(245, 245, 245);
    private final Border BORDER_NORMAL = BorderFactory.createLineBorder(new Color(220, 220, 220), 1);
    private final Border BORDER_SELECCIONADO = BorderFactory.createLineBorder(COLOR_DORADO, 3);

    private String metodoSeleccionado = "Efectivo";

    public VistaMetododePago() {
        VistaMdP();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg");
        if (img.getImage() != null) {
            g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JPanel VistaMdP() {
        this.setLayout(new BorderLayout());

        // Panel principal
        principal = new JPanel();
        principal.setOpaque(false);
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Botón volver
        pnlbtnVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlbtnVolver.setOpaque(false);
        pnlbtnVolver.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        btnVolver = new JButton("< Volver");
        btnVolver.setPreferredSize(new Dimension(110, 35));
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVolver.setBackground(COLOR_DORADO);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(null);

        pnlbtnVolver.add(btnVolver);

        // Selección tipo cliente
        pnlClientes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlClientes.setOpaque(false);
        pnlClientes.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        btnClienteR = new JButton("Cliente Registrado");
        btnClienteR.setPreferredSize(new Dimension(190, 40));
        btnClienteR.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnClienteR.setBackground(COLOR_GRIS_CLARO);
        btnClienteR.setFocusPainted(false);

        btnClienteG = new JButton("Cliente General");
        btnClienteG.setPreferredSize(new Dimension(190, 40));
        btnClienteG.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnClienteG.setBackground(COLOR_DORADO);
        btnClienteG.setFocusPainted(false);

        pnlClientes.add(btnClienteR);
        pnlClientes.add(btnClienteG);

        // Campo de búsqueda ID cliente
        pnlIngresoId = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlIngresoId.setOpaque(false);
        pnlIngresoId.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        pnlIngresoId.setVisible(false);

        lbltxtIngreso = new JLabel("Número de Identificación: ");
        lbltxtIngreso.setFont(new Font("SansSerif", Font.BOLD, 15));

        numId = new JTextField();
        numId.setPreferredSize(new Dimension(220, 35));
        numId.setFont(new Font("SansSerif", Font.PLAIN, 14));

        btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.setPreferredSize(new Dimension(100, 35));
        btnBuscarCliente.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBuscarCliente.setBackground(COLOR_DORADO);
        btnBuscarCliente.setFocusPainted(false);
        btnBuscarCliente.setBorder(null);

        pnlIngresoId.add(lbltxtIngreso);
        pnlIngresoId.add(numId);
        pnlIngresoId.add(btnBuscarCliente);

        // Cabecera métodos de pago
        pnlMdP = new JPanel();
        pnlMdP.setOpaque(false);
        pnlMdP.setLayout(new BoxLayout(pnlMdP, BoxLayout.Y_AXIS));
        pnlMdP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        lbltxtMdP = new JLabel("Métodos de Pago");
        lbltxtMdP.setFont(new Font("SansSerif", Font.BOLD, 26));
        lbltxtMdP.setAlignmentX(CENTER_ALIGNMENT);

        lbltxtIndicacion = new JLabel("Selecciona un tipo de pago para procesar la compra.");
        lbltxtIndicacion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbltxtIndicacion.setForeground(Color.GRAY);
        lbltxtIndicacion.setAlignmentX(CENTER_ALIGNMENT);

        pnlMdP.add(lbltxtMdP);
        pnlMdP.add(Box.createVerticalStrut(5));
        pnlMdP.add(lbltxtIndicacion);

        // Opciones de tarjetas
        pnlOpcionesPago = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        pnlOpcionesPago.setOpaque(false);

        pnlEfectivo = crearTarjetaPago("Efectivo", "efectivo.png", true);
        pnlTarjeta = crearTarjetaPago("Tarjeta Débito/Crédito", "tarjeta.png", false);
        pnlTransferencia = crearTarjetaPago("Transferencia", "transferencia.png", false);

        pnlOpcionesPago.add(pnlEfectivo);
        pnlOpcionesPago.add(pnlTarjeta);
        pnlOpcionesPago.add(pnlTransferencia);

        // Resumen y botón de confirmación
        pnlResumen = new JPanel();
        pnlResumen.setOpaque(false);
        pnlResumen.setLayout(new BoxLayout(pnlResumen, BoxLayout.Y_AXIS));
        pnlResumen.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel lblTituloResumen = new JLabel("Resumen de Venta");
        lblTituloResumen.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTituloResumen.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSubResumen = new JLabel("Verifica el total antes de confirmar");
        lblSubResumen.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubResumen.setForeground(Color.GRAY);
        lblSubResumen.setAlignmentX(CENTER_ALIGNMENT);

        JPanel pnlTotalContenedor = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTotalContenedor.setOpaque(false);

        JLabel lblTotalTexto = new JLabel("Total Factura: ");
        lblTotalTexto.setFont(new Font("SansSerif", Font.BOLD, 18));

        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 20));

        pnlTotalContenedor.add(lblTotalTexto);
        pnlTotalContenedor.add(lblTotal);

        btnConfirmar = new JButton("Confirmar Pago");
        btnConfirmar.setPreferredSize(new Dimension(200, 42));
        btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnConfirmar.setBackground(COLOR_DORADO);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorder(null);
        btnConfirmar.setAlignmentX(CENTER_ALIGNMENT);

        pnlResumen.add(lblTituloResumen);
        pnlResumen.add(lblSubResumen);
        pnlResumen.add(pnlTotalContenedor);
        pnlResumen.add(Box.createVerticalStrut(5));
        pnlResumen.add(btnConfirmar);

        // Ensamble
        principal.add(pnlbtnVolver);
        principal.add(pnlClientes);
        principal.add(pnlIngresoId);
        principal.add(pnlMdP);
        principal.add(pnlOpcionesPago);
        principal.add(Box.createVerticalStrut(10));
        principal.add(pnlResumen);

        this.add(principal, BorderLayout.CENTER);
        return this;
    }

    private JPanel crearTarjetaPago(String titulo, String rutaImagen, boolean seleccionado) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 160));
        panel.setBackground(Color.WHITE);
        panel.setBorder(seleccionado ? BORDER_SELECCIONADO : BORDER_NORMAL);

        JButton btnImg = new JButton(new ImageIcon(rutaImagen));
        btnImg.setContentAreaFilled(false);
        btnImg.setBorderPainted(false);
        btnImg.setFocusPainted(false);
        btnImg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEstado = new JLabel("Disponible");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEstado.setForeground(new Color(34, 139, 34));
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(btnImg);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblTitulo);
        panel.add(lblEstado);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // Métodos mutadores para modificar el estado visual desde el controlador
    public void alternarModoCliente(boolean esRegistrado) {
        btnClienteR.setBackground(esRegistrado ? COLOR_DORADO : COLOR_GRIS_CLARO);
        btnClienteG.setBackground(esRegistrado ? COLOR_GRIS_CLARO : COLOR_DORADO);
        pnlIngresoId.setVisible(esRegistrado);
        revalidate();
        repaint();
    }

    public void seleccionarMetodo(String metodo) {
        pnlEfectivo.setBorder(BORDER_NORMAL);
        pnlTarjeta.setBorder(BORDER_NORMAL);
        pnlTransferencia.setBorder(BORDER_NORMAL);

        switch (metodo) {
            case "Efectivo":
                pnlEfectivo.setBorder(BORDER_SELECCIONADO);
                break;
            case "Tarjeta":
                pnlTarjeta.setBorder(BORDER_SELECCIONADO);
                break;
            case "Transferencia":
                pnlTransferencia.setBorder(BORDER_SELECCIONADO);
                break;
        }
        this.metodoSeleccionado = metodo;
    }

    // Getters y Setters
    public void setTotal(double total) {
        lblTotal.setText("$" + String.format("%,.2f", total));
    }

    public String getMetodoSeleccionado() {
        return metodoSeleccionado;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JButton getBtnConfirmar() {
        return btnConfirmar;
    }

    public JButton getBtnBuscarCliente() {
        return btnBuscarCliente;
    }

    public JTextField getNumId() {
        return numId;
    }

    public JButton getBtnClienteR() {
        return btnClienteR;
    }

    public JButton getBtnClienteG() {
        return btnClienteG;
    }

    public JPanel getPnlEfectivo() {
        return pnlEfectivo;
    }

    public JPanel getPnlTarjeta() {
        return pnlTarjeta;
    }

    public JPanel getPnlTransferencia() {
        return pnlTransferencia;
    }

}
