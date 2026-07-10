/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author INGRID
 */
public class DineroEfectivo extends JPanel {

    private JPanel principal, pnlRecibido, pnlTotal, txtRecibido, Ingreso, pnlCambio;
    private JLabel lbltxtTotal, lblMonto, lbltxtRecibido, lbltxt2Recibido, lblCambio
            , lblCambioMonto;
    private JTextField monto;
    private JButton btnCalcularP, btnConfirmarPago;

    private final Color COLOR_VERDE_OSCURO = new Color(0, 128, 0);

    public JPanel VistaEfectivo() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);


        //Panel principal
        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.white);
        principal.setBorder(BorderFactory.createEmptyBorder(50, 250, 50, 250));

        //Panel arriba
        
        pnlTotal = new JPanel();
        pnlTotal.setLayout(new BoxLayout(pnlTotal, BoxLayout.Y_AXIS));
        pnlTotal.setOpaque(false);

        lbltxtTotal = new JLabel("Total a Pagar");
        lbltxtTotal.setFont(new Font("SansSerif", Font.BOLD, 36));
        lbltxtTotal.setAlignmentX(CENTER_ALIGNMENT);

        lblMonto = new JLabel("$108.000");
        lblMonto.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblMonto.setAlignmentX(CENTER_ALIGNMENT);

        pnlTotal.add(lbltxtTotal);
        pnlTotal.add(Box.createVerticalStrut(10));
        pnlTotal.add(lblMonto);

        // Panel mitad
        
        pnlRecibido = new JPanel();
        pnlRecibido.setLayout(new BoxLayout(pnlRecibido, BoxLayout.X_AXIS));
        pnlRecibido.setOpaque(false);
        pnlRecibido.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
        

        txtRecibido = new JPanel();
        txtRecibido.setLayout(new BoxLayout(txtRecibido, BoxLayout.Y_AXIS));
        txtRecibido.setOpaque(false);

        lbltxtRecibido = new JLabel("Dinero Recibido: $");
        lbltxtRecibido.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbltxtRecibido.setAlignmentX(LEFT_ALIGNMENT);

        lbltxt2Recibido = new JLabel("Ingrese el dinero recibido y el sistema calculará el cambio.");
        lbltxt2Recibido.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbltxt2Recibido.setAlignmentX(LEFT_ALIGNMENT);

        txtRecibido.add(lbltxtRecibido);
        txtRecibido.add(Box.createVerticalStrut(8));
        txtRecibido.add(lbltxt2Recibido);

        Ingreso = new JPanel();
        Ingreso.setLayout(new BoxLayout(Ingreso, BoxLayout.Y_AXIS));
        Ingreso.setOpaque(false);
        monto = new JTextField("$110.000");
        monto.setFont(new Font("SansSerif", Font.PLAIN, 14));
        monto.setMaximumSize(new Dimension(350, 35));
        monto.setAlignmentX(LEFT_ALIGNMENT);

        btnCalcularP = new JButton("Calcular Pago");
        btnCalcularP.setBackground(Color.BLACK);
        btnCalcularP.setForeground(Color.WHITE);
        btnCalcularP.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCalcularP.setMaximumSize(new Dimension(200, 40));
        btnCalcularP.setBorderPainted(false);
        btnCalcularP.setAlignmentX(LEFT_ALIGNMENT);

        Ingreso.add(monto);
        Ingreso.add(Box.createVerticalStrut(20));
        Ingreso.add(btnCalcularP);

        pnlRecibido.add(txtRecibido);
        pnlRecibido.add(Box.createHorizontalStrut(0));
        pnlRecibido.add(Ingreso);

        pnlCambio = new JPanel();
        pnlCambio.setLayout(new BoxLayout(pnlCambio, BoxLayout.Y_AXIS));
        pnlCambio.setOpaque(false);

        lblCambio = new JLabel("Cambio para el Cliente:");
        lblCambio.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblCambio.setForeground(COLOR_VERDE_OSCURO);
        lblCambio.setAlignmentX(CENTER_ALIGNMENT);

        lblCambioMonto = new JLabel("$ 2.000");
        lblCambioMonto.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblCambioMonto.setForeground(COLOR_VERDE_OSCURO);
        lblCambioMonto.setAlignmentX(CENTER_ALIGNMENT);

        btnConfirmarPago = new JButton("Confirmar Pago");
        btnConfirmarPago.setBackground(Color.BLACK);
        btnConfirmarPago.setForeground(Color.WHITE);
        btnConfirmarPago.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnConfirmarPago.setMaximumSize(new Dimension(200, 40));
        btnConfirmarPago.setBorderPainted(false);
        btnConfirmarPago.setAlignmentX(CENTER_ALIGNMENT);

        pnlCambio.add(lblCambio);
        pnlCambio.add(Box.createVerticalStrut(5));
        pnlCambio.add(lblCambioMonto);
        pnlCambio.add(Box.createVerticalStrut(10));
        pnlCambio.add(btnConfirmarPago);

        principal.add(pnlTotal);
        principal.add(Box.createVerticalStrut(70));
        principal.add(pnlRecibido);
        principal.add(Box.createVerticalStrut(70));
        principal.add(pnlCambio);

        this.add(principal, BorderLayout.CENTER);

        return this;
    }
}
