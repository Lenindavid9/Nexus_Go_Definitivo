package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MetododePago extends JPanel {
    private JPanel principal, pnlClientes, pnlIngresoId, pnlMdP, pnlResumen, pnlbtnVolver;
    private JLabel lbltxtIngreso, lbltxtMdP, lbltxtIndicacion, lblTotal;
    private JButton btnClienteR, btnClienteG, btnVolver, btnConfirmar, btnBuscarCliente, btnImgEfectivo, btnImgTarjeta, btnImgTransferencia;
    private JTextField numId;
    
    private final Color COLOR_DORADO = new Color(223, 205, 141);
 public MetododePago() {
        VistaMdP(); // inicializa la interfaz al crear el objeto
    }
    public JPanel VistaMdP() {
        this.setLayout(new BorderLayout());

        //PANEL PRINCIPAL
        principal = new JPanel() {
            private Image fondo = new ImageIcon("fondo.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        //PANEL BOTON < VOLVER
        pnlbtnVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlbtnVolver.setOpaque(false);
        pnlbtnVolver.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));

        btnVolver = new JButton("< Volver");
        btnVolver.setPreferredSize(new Dimension(110, 40)); 
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnVolver.setBackground(COLOR_DORADO);
        btnVolver.setBorder(null);
        
        pnlbtnVolver.add(btnVolver);
        
        
        //PANEL CLIENTES
        pnlClientes = new JPanel();
        pnlClientes.setOpaque(false);
        pnlClientes.setLayout(new BoxLayout(pnlClientes, BoxLayout.X_AXIS));
        pnlClientes.setPreferredSize(new Dimension(400, 60)); 
        pnlClientes.setMaximumSize(new Dimension(400, 45));

        //BOTON CLIENTE REGISTRADO
        btnClienteR = new JButton("Cliente Registrado");
        btnClienteR.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnClienteR.setBackground(COLOR_DORADO); 
        btnClienteR.setBorder(null);
        btnClienteR.setMaximumSize(new Dimension(180, 50));

        //BOTON CLIENTE GENERAL
        btnClienteG = new JButton("Cliente General");
        btnClienteG.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnClienteG.setBackground(COLOR_DORADO); 
        btnClienteG.setBorder(null);
        btnClienteG.setMaximumSize(new Dimension(180, 50));

        pnlClientes.add(btnClienteR);
        pnlClientes.add(Box.createHorizontalStrut(20));
        pnlClientes.add(btnClienteG);

        //PANEL INGRESO NUMERO DE IDENTIFICACION
        pnlIngresoId = new JPanel();
        pnlIngresoId.setLayout(new BoxLayout(pnlIngresoId, BoxLayout.X_AXIS));
        pnlIngresoId.setOpaque(false);
        pnlIngresoId.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        lbltxtIngreso = new JLabel("Número de Identificación");
        lbltxtIngreso.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbltxtIngreso.setAlignmentX(CENTER_ALIGNMENT);

        numId = new JTextField();
        numId.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numId.setMaximumSize(new Dimension(350, 35));
        numId.setAlignmentX(CENTER_ALIGNMENT);
        btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnBuscarCliente.setBackground(COLOR_DORADO);
        btnBuscarCliente.setBorder(null);

        pnlIngresoId.add(lbltxtIngreso);
        pnlIngresoId.add(Box.createHorizontalStrut(10));
        pnlIngresoId.add(numId);
        pnlIngresoId.add(Box.createHorizontalStrut(10));
        pnlIngresoId.add(btnBuscarCliente);


        //PANEL METODOS DE PAGO
        pnlMdP = new JPanel();
        pnlMdP.setLayout(new BoxLayout(pnlMdP, BoxLayout.Y_AXIS));
        pnlMdP.setOpaque(false);

        lbltxtMdP = new JLabel("Métodos de Pago");
        lbltxtMdP.setForeground(Color.BLACK);
        lbltxtMdP.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbltxtMdP.setAlignmentX(CENTER_ALIGNMENT);

        lbltxtIndicacion = new JLabel("Selecciona un método de pago");
        lbltxtIndicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbltxtIndicacion.setForeground(Color.BLACK);
        lbltxtIndicacion.setAlignmentX(CENTER_ALIGNMENT);

        // CONTENEDOR DE OPCIONES 
        JPanel pnlContenedorOpciones = new JPanel(new GridBagLayout());
        pnlContenedorOpciones.setOpaque(false);

        JPanel pnlOpcionesPago = new JPanel(new GridLayout(1, 3, 15, 0)); 
        pnlOpcionesPago.setOpaque(false);
        pnlOpcionesPago.setPreferredSize(new Dimension(650, 150)); 

        // --- Subopción 1: EFECTIVO ---
        JPanel pnlEfectivo = new JPanel();
        pnlEfectivo.setLayout(new BoxLayout(pnlEfectivo, BoxLayout.Y_AXIS));
        pnlEfectivo.setBackground(Color.WHITE);
        pnlEfectivo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        btnImgEfectivo = new JButton(new ImageIcon("efectivo.png"));
        btnImgEfectivo.setAlignmentX(CENTER_ALIGNMENT);
        btnImgEfectivo.setContentAreaFilled(false);
        btnImgEfectivo.setBorderPainted(false);

        JLabel lblTituloEfectivo = new JLabel("Efectivo");
        lblTituloEfectivo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloEfectivo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblEstadoEfectivo = new JLabel("Disponible");
        lblEstadoEfectivo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoEfectivo.setForeground(Color.GREEN.darker());
        lblEstadoEfectivo.setAlignmentX(CENTER_ALIGNMENT);

        pnlEfectivo.add(Box.createVerticalStrut(8)); 
        pnlEfectivo.add(btnImgEfectivo);
        pnlEfectivo.add(Box.createVerticalStrut(2));
        pnlEfectivo.add(lblTituloEfectivo);
        pnlEfectivo.add(Box.createVerticalStrut(2));
        pnlEfectivo.add(lblEstadoEfectivo);
        pnlOpcionesPago.add(pnlEfectivo);

        // --- Subopción 2: TARJETA ---
        JPanel pnlTarjeta = new JPanel();
        pnlTarjeta.setLayout(new BoxLayout(pnlTarjeta, BoxLayout.Y_AXIS));
        pnlTarjeta.setBackground(Color.WHITE);
        pnlTarjeta.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        btnImgTarjeta = new JButton(new ImageIcon("tarjeta.png"));
        btnImgTarjeta.setAlignmentX(CENTER_ALIGNMENT);
        btnImgTarjeta.setContentAreaFilled(false);
        btnImgTarjeta.setBorderPainted(false);

        JLabel lblTituloTarjeta = new JLabel("Tarjeta Débito/Crédito");
        lblTituloTarjeta.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloTarjeta.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblEstadoTarjeta = new JLabel("Disponible");
        lblEstadoTarjeta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoTarjeta.setForeground(Color.GREEN.darker());
        lblEstadoTarjeta.setAlignmentX(CENTER_ALIGNMENT);

        pnlTarjeta.add(Box.createVerticalStrut(8));
        pnlTarjeta.add(btnImgTarjeta);
        pnlTarjeta.add(Box.createVerticalStrut(2));
        pnlTarjeta.add(lblTituloTarjeta);
        pnlTarjeta.add(Box.createVerticalStrut(2));
        pnlTarjeta.add(lblEstadoTarjeta);
        pnlOpcionesPago.add(pnlTarjeta);

        // --- Subopción 3: TRANSFERENCIA ---
        JPanel pnlTransferencia = new JPanel();
        pnlTransferencia.setLayout(new BoxLayout(pnlTransferencia, BoxLayout.Y_AXIS));
        pnlTransferencia.setBackground(Color.WHITE);
        pnlTransferencia.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        btnImgTransferencia = new JButton(new ImageIcon("transferencia.png"));
        btnImgTransferencia.setAlignmentX(CENTER_ALIGNMENT);
        btnImgTransferencia.setContentAreaFilled(false);
        btnImgTransferencia.setBorderPainted(false);

        JLabel lblTituloTransferencia = new JLabel("Transferencia");
        lblTituloTransferencia.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloTransferencia.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblEstadoTransferencia = new JLabel("Disponible");
        lblEstadoTransferencia.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoTransferencia.setForeground(Color.GREEN.darker());
        lblEstadoTransferencia.setAlignmentX(CENTER_ALIGNMENT);

        // --- Subopción 3: TRANSFERENCIA ---
         pnlTransferencia = new JPanel();
        pnlTransferencia.setLayout(new BoxLayout(pnlTransferencia, BoxLayout.Y_AXIS));
        pnlTransferencia.setBackground(Color.WHITE);
        pnlTransferencia.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

         btnImgTransferencia = new JButton(new ImageIcon("transferencia.png"));
        btnImgTransferencia.setAlignmentX(CENTER_ALIGNMENT);
        btnImgTransferencia.setContentAreaFilled(false);
        btnImgTransferencia.setBorderPainted(false);

         lblTituloTransferencia = new JLabel("Transferencia");
        lblTituloTransferencia.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloTransferencia.setAlignmentX(CENTER_ALIGNMENT);

         lblEstadoTransferencia = new JLabel("Disponible");
        lblEstadoTransferencia.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoTransferencia.setForeground(Color.GREEN.darker());
        lblEstadoTransferencia.setAlignmentX(CENTER_ALIGNMENT);

        pnlTransferencia.add(Box.createVerticalStrut(8));
        pnlTransferencia.add(btnImgTransferencia);
        pnlTransferencia.add(Box.createVerticalStrut(2));
        pnlTransferencia.add(lblTituloTransferencia);
        pnlTransferencia.add(Box.createVerticalStrut(2));
        pnlTransferencia.add(lblEstadoTransferencia);
        pnlOpcionesPago.add(pnlTransferencia);
        
        // Ensamblaje final del panel de métodos de pago
        pnlMdP.add(Box.createVerticalStrut(20)); 
        pnlMdP.add(lbltxtMdP);
        pnlMdP.add(Box.createVerticalStrut(5));
        pnlMdP.add(lbltxtIndicacion);
        pnlContenedorOpciones.add(pnlOpcionesPago); 
        pnlMdP.add(pnlContenedorOpciones);
        pnlMdP.add(Box.createVerticalStrut(15)); 


        // =========================================================================
        // 6. CONFIGURACIÓN DEL PANEL RESUMEN DE VENTA
        // =========================================================================
        pnlResumen = new JPanel(new GridLayout(1, 2, 100, 0));
        pnlResumen.setOpaque(false);

        // --- Resumen Izquierda (Títulos y Confirmación) ---
        JPanel pnlResumenIzquierda = new JPanel();
        pnlResumenIzquierda.setLayout(new BoxLayout(pnlResumenIzquierda, BoxLayout.Y_AXIS));
        pnlResumenIzquierda.setBackground(Color.WHITE);
        pnlResumenIzquierda.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblResumenTitulo = new JLabel("Resumen de Venta");
        lblResumenTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblResumenTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblResumenVerifica = new JLabel("Verifica el total antes de confirmar.");
        lblResumenVerifica.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblResumenVerifica.setForeground(Color.BLACK);
        lblResumenVerifica.setAlignmentX(LEFT_ALIGNMENT);

        btnConfirmar = new JButton("Confirmar Pago");
        btnConfirmar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnConfirmar.setBackground(COLOR_DORADO);
        btnConfirmar.setBorder(null);
        btnConfirmar.setMaximumSize(new Dimension(180, 50));
        btnConfirmar.setEnabled(false);
        
        pnlResumenIzquierda.add(Box.createVerticalGlue());
        pnlResumenIzquierda.add(lblResumenTitulo);
        pnlResumenIzquierda.add(lblResumenVerifica);
        pnlResumenIzquierda.add(Box.createVerticalStrut(15));
        pnlResumenIzquierda.add(btnConfirmar);
        pnlResumenIzquierda.add(Box.createVerticalGlue());

        // --- Resumen Derecha (Detalle del Total Factura) ---
        JPanel pnlResumenDerecha = new JPanel();
        pnlResumenDerecha.setLayout(new BoxLayout(pnlResumenDerecha, BoxLayout.Y_AXIS));
        pnlResumenDerecha.setBackground(Color.WHITE);
        pnlResumenDerecha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTotalFactura = new JLabel("Total Factura");
        lblTotalFactura.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotalFactura.setAlignmentX(LEFT_ALIGNMENT);

        lblTotal = new JLabel("$");
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTotal.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblNexus = new JLabel("N E X U S");
        lblNexus.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblNexus.setForeground(Color.GRAY);
        lblNexus.setAlignmentX(LEFT_ALIGNMENT);

        pnlResumenDerecha.add(Box.createVerticalGlue());
        pnlResumenDerecha.add(lblTotalFactura);
        pnlResumenDerecha.add(Box.createVerticalStrut(8));
        pnlResumenDerecha.add(lblTotal);
        pnlResumenDerecha.add(Box.createVerticalStrut(8));
        pnlResumenDerecha.add(lblNexus);
        pnlResumenDerecha.add(Box.createVerticalGlue());

        // Construcción del panel resumen
        pnlResumen.add(pnlResumenIzquierda);
        pnlResumen.add(pnlResumenDerecha);

        principal.add(pnlbtnVolver);
        principal.add(pnlClientes);
        principal.add(pnlIngresoId);
        principal.add(pnlMdP);
        principal.add(pnlResumen);

        // Envío de la vista armada al contenedor principal
        this.add(principal, BorderLayout.CENTER);
        return this;
    }

    public void setTotal(double total) {
        lblTotal.setText("$" + String.format("%,.2f", total));
    }
    public JButton getBtnVolver() {
        return btnVolver;
    }

    public JButton getBtnConfirmar() {
        return btnConfirmar;
    }

    public JLabel getLblTotal() {
        return lblTotal;
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
    
    public JButton getBtnEfectivo() {
        return btnImgEfectivo;
    }

    public JButton getBtnTarjeta() {
        return btnImgTarjeta;
    }

    public JButton getBtnTransferencia() {
        return btnImgTransferencia;
    }

}
