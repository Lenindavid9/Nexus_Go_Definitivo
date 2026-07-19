package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
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

    private JPanel principal, pnlClientes, pnlIngresoId, pnlMdP, pnlResumen, pnlbtnVolver, pnlOpcionesPago;
    private JLabel lbltxtIngreso, lbltxtMdP, lbltxtIndicacion, lblTotal, lblTituloEfectivo, lblEstadoEfectivo;
    private JButton btnClienteR, btnClienteG, btnVolver, btnConfirmar, btnBuscarCliente, btnImgEfectivo, btnImgTarjeta, btnImgTransferencia;
    private JTextField numId;
    private JPanel pnlEfectivo, pnlTransferencia, pnlTarjeta;

    private final Color COLOR_DORADO = new Color(223, 205, 141);

    public MetododePago() {
        VistaMdP();
    }

    public JPanel VistaMdP() {
        
        this.setLayout(new BorderLayout());
           
//                                            PANEL PRINCIPAL ;) (Aqui se va agregar todo)

        principal = new JPanel() {
            private Image fondo = new ImageIcon("fondo.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        //Este es para organizar todo hacia abajo, no podemos usar flowlayout ya que se pondria todo a un lado
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        
        //espaci de los bordes del panel principal
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        
        //                                       PANEL DEL BOTON VOLVER ;)
        
        //Creamos el panel del boton volver para dejarlo en la parte superior derecha usando el flowlayout
        pnlbtnVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlbtnVolver.setOpaque(false);
        pnlbtnVolver.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        //Aqui es importante usar el setPreferredSise ya que le decimos al layout el tamaño que queremos para el boton volver
        btnVolver = new JButton("< Volver");
        btnVolver.setPreferredSize(new Dimension(110, 40));
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnVolver.setBackground(COLOR_DORADO);
        btnVolver.setBorder(null);

        //se añade el JButton al JPanel
        pnlbtnVolver.add(btnVolver);
        
        
        //                                           PANEL DE LOS CLIENTES ;)
        

        
        //Aqui creamos el panel de los clientes donde se usa tambien el flowlayout ya que necesitamos que se vea uno al lado del otro no debajo
        pnlClientes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlClientes.setOpaque(false);
        pnlClientes.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        //Aqui creamos los botones cliente registrado y cliente general ambos con el mismo tamaño el mismo todo para que se vean simetricos
        btnClienteR = new JButton("Cliente Registrado");
        btnClienteR.setPreferredSize(new Dimension(190, 45));
        btnClienteR.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnClienteR.setBackground(COLOR_DORADO);
        btnClienteR.setBorder(null);

        btnClienteG = new JButton("Cliente General");
        btnClienteG.setPreferredSize(new Dimension(190, 45));
        btnClienteG.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnClienteG.setBackground(COLOR_DORADO);
        btnClienteG.setBorder(null);

        //se añaden los JButton al JPanel
        pnlClientes.add(btnClienteR);
        pnlClientes.add(btnClienteG);

        
        //                                       PANEL PARA INGRESAR EL NUMERO DE IDENTIFICACION DEL CLIENTE REGISTRADO ;)
        
        
        
        //Aqui creamos el panel tambien con un flowlayout por que necesitamos que todo quede a un lado no debajo
        pnlIngresoId = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pnlIngresoId.setOpaque(false);
        pnlIngresoId.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
//      

        //label del texto a mostrar
        lbltxtIngreso = new JLabel("Número de Identificación: ");
        lbltxtIngreso.setFont(new Font("SansSerif", Font.BOLD, 18));

        //textfiel para ingresar el numero de identificacion
        numId = new JTextField();
        numId.setPreferredSize(new Dimension(250, 35));
        numId.setFont(new Font("SansSerif", Font.PLAIN, 14));

        //boton de buscar numero de identificacion del cliente registrado
        btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.setPreferredSize(new Dimension(100, 35));
        btnBuscarCliente.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnBuscarCliente.setBackground(COLOR_DORADO);
        btnBuscarCliente.setBorder(null);

        //agregar label,textfield y boton al panel de ingreso numero de identificacion
        pnlIngresoId.add(lbltxtIngreso);
        pnlIngresoId.add(numId);
        pnlIngresoId.add(btnBuscarCliente);

        
        //                                  PANEL DE METODOS DE PAGO ;)
        
        //Aqui creamos el panel donde usamos el boxlayout ya que queremos que el subtitulo quede debajo del titulo y no al lado
        pnlMdP = new JPanel();
        pnlMdP.setLayout(new BoxLayout(pnlMdP, BoxLayout.Y_AXIS));
        pnlMdP.setOpaque(false);
        pnlMdP.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        //Titulo
        lbltxtMdP = new JLabel("Métodos de Pago");
        lbltxtMdP.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbltxtMdP.setAlignmentX(CENTER_ALIGNMENT); //usamos esto para que el texto quede en el centro ya que por defecto se pone a la izquierda

        //Subtitulo
        lbltxtIndicacion = new JLabel("Selecciona un método de pago");
        lbltxtIndicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbltxtIndicacion.setAlignmentX(CENTER_ALIGNMENT);

        //Agregamos los JLabel al JPanel
        pnlMdP.add(lbltxtMdP);
        pnlMdP.add(lbltxtIndicacion);
        
        
        
        //                                     PANEL DE OPCIONES DE PAGO ;)
        
        //Para este usamos un flowlayout  ya que despues vamos a agregarle las opciones que estan en un boxlayout 
        pnlOpcionesPago = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlOpcionesPago.setOpaque(false);


        
        //                                     PANEL DE EFECTIVO ;)
        
        //Aqui creamos el panel de efectivo donde usamos el box para que todo su contenido quede debajo dle otro
        pnlEfectivo = new JPanel();
        pnlEfectivo.setLayout(new BoxLayout(pnlEfectivo, BoxLayout.Y_AXIS));
        pnlEfectivo.setPreferredSize(new Dimension(240, 200));

        btnImgEfectivo = new JButton(new ImageIcon("efectivo.png"));
        //usamos esto para que no se vea el diseño del boton predeterminado (esta feo)
        btnImgEfectivo.setContentAreaFilled(false);
        btnImgEfectivo.setBorderPainted(false);
        btnImgEfectivo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTituloEfectivo = new JLabel("Efectivo");
        lblTituloEfectivo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloEfectivo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblEstadoEfectivo = new JLabel("Disponible");
        lblEstadoEfectivo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoEfectivo.setForeground(Color.GREEN.darker());
        lblEstadoEfectivo.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Aqui agregamos todo el contenido al JPanel
        pnlEfectivo.add(Box.createVerticalGlue());
        pnlEfectivo.add(btnImgEfectivo);
        pnlEfectivo.add(Box.createVerticalStrut(8));
        pnlEfectivo.add(lblTituloEfectivo);
        pnlEfectivo.add(lblEstadoEfectivo);
        pnlEfectivo.add(Box.createVerticalGlue());

        //Aqui guardamos todo el pnale de efectivo en el de metodo de pago donde se vera al lado de las otras opciones 
        pnlOpcionesPago.add(pnlEfectivo);

//                                  AQUI SE DUPLICA TODO  PARA TARJETA Y TRANSFERENCIA Y SE CAMBIAN LOS NOMBRES Y LA IMAGEN
        
//                                  PANEL TARJETA ;)
        pnlTarjeta = new JPanel();
        pnlTarjeta.setLayout(new BoxLayout(pnlTarjeta, BoxLayout.Y_AXIS));
        pnlTarjeta.setPreferredSize(new Dimension(240, 200));

        btnImgTarjeta = new JButton(new ImageIcon("tarjeta.png"));
        btnImgTarjeta.setContentAreaFilled(false);
        btnImgTarjeta.setBorderPainted(false);
        btnImgTarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTituloTarjeta = new JLabel("Tarjeta Débito/Crédito");
        lblTituloTarjeta.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloTarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEstadoTarjeta = new JLabel("Disponible");
        lblEstadoTarjeta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoTarjeta.setForeground(Color.GREEN.darker());
        lblEstadoTarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlTarjeta.add(Box.createVerticalGlue());
        pnlTarjeta.add(btnImgTarjeta);
        pnlTarjeta.add(Box.createVerticalStrut(8));
        pnlTarjeta.add(lblTituloTarjeta);
        pnlTarjeta.add(lblEstadoTarjeta);
        pnlTarjeta.add(Box.createVerticalGlue());

        pnlOpcionesPago.add(pnlTarjeta);

//                                  PANEL TRANSFERENCIA ;)

        pnlTransferencia = new JPanel();
        pnlTransferencia.setLayout(new BoxLayout(pnlTransferencia, BoxLayout.Y_AXIS));
        pnlTransferencia.setPreferredSize(new Dimension(240, 200));

        btnImgTransferencia = new JButton(new ImageIcon("transferencia.png"));
        btnImgTransferencia.setContentAreaFilled(false);
        btnImgTransferencia.setBorderPainted(false);
        btnImgTransferencia.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTituloTransferencia = new JLabel("Transferencia");
        lblTituloTransferencia.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloTransferencia.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEstadoTransferencia = new JLabel("Disponible");
        lblEstadoTransferencia.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEstadoTransferencia.setForeground(Color.GREEN.darker());
        lblEstadoTransferencia.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlTransferencia.add(Box.createVerticalGlue());
        pnlTransferencia.add(btnImgTransferencia);
        pnlTransferencia.add(Box.createVerticalStrut(8));
        pnlTransferencia.add(lblTituloTransferencia);
        pnlTransferencia.add(lblEstadoTransferencia);
        pnlTransferencia.add(Box.createVerticalGlue());

        pnlOpcionesPago.add(pnlTransferencia);

        
        //                                  PANEL TARJETA ;)
        
        //Aqui creamos el panel del resumen de pago 
        pnlResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlResumen.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        pnlResumen.setOpaque(false);

        JLabel lblTotalFactura = new JLabel("Total Factura: ");
        lblTotalFactura.setFont(new Font("SansSerif", Font.BOLD, 18));

        lblTotal = new JLabel("$");
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 18));

        btnConfirmar = new JButton("Confirmar Pago");
        btnConfirmar.setPreferredSize(new Dimension(180, 40));
        btnConfirmar.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnConfirmar.setBackground(COLOR_DORADO);
        btnConfirmar.setBorder(null);

        pnlResumen.add(lblTotalFactura);
        pnlResumen.add(lblTotal);
        pnlResumen.add(btnConfirmar);

        
        //Aqui agregamos todos los paneles al panel principal
        principal.add(pnlbtnVolver);
        principal.add(pnlClientes);
        principal.add(pnlIngresoId);
        principal.add(pnlMdP);
        principal.add(pnlOpcionesPago);
        principal.add(pnlResumen);

        //Aqui ponemos el panel principal aqui (this) centrado
        this.add(principal, BorderLayout.CENTER);
        //lo retornamos para que se pueda ver 
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

    public JButton getBtnImgEfectivo() {
        return btnImgEfectivo;
    }

    public JButton getBtnTarjeta() {
        return btnImgTarjeta;
    }

    public JButton getBtnTransferencia() {
        return btnImgTransferencia;
    }

}
