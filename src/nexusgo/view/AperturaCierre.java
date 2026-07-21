/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import nexusgo.view.PanelAdmi;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AperturaCierre extends JPanel {
    private JPanel principal, pnlbtnVolver, apertura, cierre, confirmacion, infoCierre, infoApertura,infoConfirmacion, CalcularC;
    private JLabel imgApertura, tituloA, imgCierre, tituloC, lblsubC, lblMontoA, lbltxtMontoA,lblMontoVT, lbltxtMontoTV
            , tituloConf, lblsubConf, lblMontoF, lblsubA, lblMontoInicial;
    private JButton btnVolver, btnApertura, btnCalcular;
    private JTextField txtMontoInicial,txtMontoF;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);
    
    private JFrame ventanaPrincipal;
    private PanelAdmi panelAdmi;

    public AperturaCierre(JFrame ventanaPrincipal, PanelAdmi panelAdmi) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.panelAdmi = panelAdmi;
        VistaCaja();
    }

    
    public AperturaCierre() {
        VistaCaja();
    }

    @Override
    protected void paintComponent(Graphics g
    ) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg");
        g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    public JPanel VistaCaja() {

        this.setLayout(new BorderLayout());

        principal = new JPanel(); 
        principal.setOpaque(false); 
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
        
        
        apertura = new JPanel();
        apertura.setBackground(new Color(255, 255, 255, 150));
        apertura.setMaximumSize(new Dimension(900, 200));
        apertura.setBorder(null);
        
        imgApertura = new JLabel(new ImageIcon("apertura.png"));
        imgApertura.setPreferredSize(new Dimension(400, 250));
        imgApertura.setMaximumSize(new Dimension(300, 200));
        
        infoApertura = new JPanel();
        infoApertura.setOpaque(false);
        infoApertura.setPreferredSize(new Dimension(400, 270));
        infoApertura.setLayout(new BoxLayout(infoApertura, BoxLayout.Y_AXIS));

        tituloA = new JLabel("Apertura de Caja");
        tituloA.setFont(new Font("SansSerif", Font.BOLD, 33));
        tituloA.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblsubA = new JLabel("Registra el monto con el que inicia el día.");
        lblsubA.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblsubA.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblMontoInicial = new JLabel("Registre Monto Inicial");
        lblMontoInicial.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblMontoInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMontoInicial = new JTextField("$0");
        txtMontoInicial.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtMontoInicial.setPreferredSize(new Dimension(320, 40));   
        txtMontoInicial.setMaximumSize(new Dimension(320, 40));     
        txtMontoInicial.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        btnApertura = new JButton("Realizar Apertura");
        btnApertura.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnApertura.setBackground(COLOR_DORADO); 
        btnApertura.setForeground(Color.white);
        btnApertura.setMaximumSize(new Dimension(180, 50));
        btnApertura.setPreferredSize(new Dimension(150, 40)); 
        btnApertura.setBorder(null);
        
        apertura.add(imgApertura);
        infoApertura.add(tituloA);
        infoApertura.add(lblsubA);
        infoApertura.add(Box.createVerticalStrut(40));
        infoApertura.add(lblMontoInicial);
        infoApertura.add(Box.createVerticalStrut(10));
        infoApertura.add(txtMontoInicial);
        infoApertura.add(Box.createVerticalStrut(35));
        infoApertura.add(btnApertura);
        apertura.add(infoApertura);
        
        cierre = new JPanel();
        cierre.setBackground(new Color(255, 255, 255, 150));
        cierre.setMaximumSize(new Dimension(900, 200));
        cierre.setBorder(null);
        
        infoCierre = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        infoCierre.setOpaque(false);
        infoCierre.setPreferredSize(new Dimension(400, 270));
        infoCierre.setLayout(new BoxLayout(infoCierre, BoxLayout.Y_AXIS));
        
        tituloC = new JLabel("Cierre de Caja");
        tituloC.setFont(new Font("SansSerif", Font.BOLD, 33));
        tituloC.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblsubC = new JLabel("Registra el total final del día y cierra la jornada.");
        lblsubC.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblsubC.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblMontoA = new JLabel("Monto de Apertura:");
        lblMontoA.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMontoA.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lbltxtMontoA = new JLabel("$0");
        lbltxtMontoA.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbltxtMontoA.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblMontoVT = new JLabel("Monto de Ventas Totales:");
        lblMontoVT.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblMontoVT.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lbltxtMontoTV = new JLabel("$0");
        lbltxtMontoTV.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbltxtMontoTV.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        imgCierre = new JLabel(new ImageIcon("cierre.png"));
        imgCierre.setPreferredSize(new Dimension(400, 250));
        imgCierre.setMaximumSize(new Dimension(300, 200));
        
        infoCierre.add(Box.createVerticalStrut(15));
        infoCierre.add(tituloC);
        infoCierre.add(lblsubC);
        infoCierre.add(Box.createVerticalStrut(20));
        infoCierre.add(lblMontoA);
        infoCierre.add(lbltxtMontoA);
        infoCierre.add(Box.createVerticalStrut(15));
        infoCierre.add(lblMontoVT);
        infoCierre.add(lbltxtMontoTV);

        cierre.add(infoCierre);
        cierre.add(Box.createHorizontalStrut(20));
        cierre.add(imgCierre);
        
        confirmacion = new JPanel();
        confirmacion.setBackground(new Color(255, 255, 255, 150));
        confirmacion.setMaximumSize(new Dimension(900, 200));
        confirmacion.setBorder(null);
        
        
        infoConfirmacion = new JPanel();
        infoConfirmacion.setLayout(new BoxLayout(infoConfirmacion, BoxLayout.Y_AXIS));
        infoConfirmacion.setAlignmentY(Component.CENTER_ALIGNMENT);
        infoConfirmacion.setOpaque(false);
        infoConfirmacion.setPreferredSize(new Dimension(400, 270));
        
        
        tituloConf = new JLabel("Confirmacion de Cierre");
        tituloConf.setFont(new Font("SansSerif", Font.BOLD, 33));
        tituloConf.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblsubConf = new JLabel("Confirma el monto final y finaliza la jornada.");
        lblsubConf.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblsubConf.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        CalcularC = new JPanel();
        CalcularC.setLayout(new BoxLayout(CalcularC, BoxLayout.Y_AXIS));
        CalcularC.setOpaque(false);
        CalcularC.setPreferredSize(new Dimension(400, 270));
        
        lblMontoF = new JLabel("Ingrese el monto físico en caja");
        lblMontoF.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lblMontoF.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        
        txtMontoF = new JTextField("$");
        txtMontoF.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtMontoF.setPreferredSize(new Dimension(320, 40));   
        txtMontoF.setMaximumSize(new Dimension(320, 40));     
        txtMontoF.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        
        btnCalcular = new JButton("Calcular Cierre");
        btnCalcular.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnCalcular.setBackground(COLOR_DORADO); 
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setMaximumSize(new Dimension(180, 50));
        btnCalcular.setPreferredSize(new Dimension(150, 40)); 
        btnCalcular.setBorder(null);
        
        
        infoConfirmacion.add(Box.createVerticalGlue());
        infoConfirmacion.add(tituloConf);
        infoConfirmacion.add(lblsubConf);
        infoConfirmacion.add(Box.createVerticalGlue());
        
        
        confirmacion.add(infoConfirmacion);
        CalcularC.add(Box.createVerticalGlue());  
        CalcularC.add(lblMontoF);
        CalcularC.add(Box.createVerticalStrut(10));
        CalcularC.add(txtMontoF);
        CalcularC.add(Box.createVerticalStrut(20));
        CalcularC.add(btnCalcular);
        confirmacion.add(CalcularC);
        CalcularC.add(Box.createVerticalGlue()); 
        
        principal.add(pnlbtnVolver);
        principal.add(apertura);
        principal.add(cierre);
        principal.add(confirmacion);
        
        this.add(principal, BorderLayout.CENTER);
        return this;
    }

    public JTextField getTxtMontoInicial() {
        return txtMontoInicial;
    }

    public JButton getBtnApertura() {
        return btnApertura;
    }

    public JTextField getTxtMontoF() {
        return txtMontoF;
    }

    public JButton getBtnCalcular() {
        return btnCalcular;
    }

    public JLabel getLbltxtMontoA() {
        return lbltxtMontoA;
    }

    public JLabel getLbltxtMontoTV() {
        return lbltxtMontoTV;
    }
    
}
