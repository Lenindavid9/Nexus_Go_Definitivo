/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * @author INGRID
 */
public class Factura extends JPanel {
    
    private JPanel principal, infoFactura, pnlOpciones, pnlDatos, 
            tablaC, detalleT, linea1, linea2, pnlTotal;
    private JLabel lblEmpresa, lblGiro, lblOperario, lblCliente, lblFecha, lblEstado, SoP, 
            Cant, PrecU, total, lblFelicidades, lbltxtPagar, lblMonto, 
            lbltxtA, lbltxtJ, DSoP, DCant, DPrecU, Dtotal;   
    private JButton btnImprimir, btnEnviar, btnAnular, btnGuardarN;
    private JTextArea txtNota;

    private final Color COLOR_DORADO = new Color(184, 149, 78);
    private final Color COLOR_GRIS_CLARITO = new Color(220, 220, 220);

    public JPanel crearVistaFactura() {
        this.setLayout(new BorderLayout());
        
        principal = new JPanel(new GridLayout(1, 2, 40, 0));
        principal.setBackground(Color.WHITE);
        principal.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));

        
        
        infoFactura = new JPanel();
        infoFactura.setLayout(new BoxLayout(infoFactura, BoxLayout.Y_AXIS));
        infoFactura.setBackground(Color.WHITE);
        infoFactura.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GRIS_CLARITO, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        
        lblEmpresa = new JLabel("N E X U S");
        lblEmpresa.setFont(new Font("SansSerif", Font.BOLD, 34));
        lblEmpresa.setAlignmentX(CENTER_ALIGNMENT);
        
        lblGiro = new JLabel("SALÓN DE BELLEZA");
        lblGiro.setFont(new Font("SansSerif", Font.PLAIN, 22));
        lblGiro.setForeground(Color.GRAY);
        lblGiro.setAlignmentX(CENTER_ALIGNMENT);

        
        pnlDatos = new JPanel();
        pnlDatos.setLayout(new BoxLayout(pnlDatos, BoxLayout.Y_AXIS));
        pnlDatos.setOpaque(false);
        pnlDatos.setMaximumSize(new Dimension(450, 100));
        pnlDatos.setAlignmentX(CENTER_ALIGNMENT);
        
        lblOperario = new JLabel("Operario: Nombre_de_Usuario  /  Numero_de_identificacion");
        lblOperario.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblCliente = new JLabel("Cliente: Nombre_de_Usuario  /  Numero_de_identificacion");
        lblCliente.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblFecha = new JLabel("Fecha/Hora: 2026-05-28  /  11:05:22");
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblEstado = new JLabel("Cliente General");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 16));

        lblOperario.setAlignmentX(LEFT_ALIGNMENT);
        lblCliente.setAlignmentX(LEFT_ALIGNMENT);
        lblFecha.setAlignmentX(LEFT_ALIGNMENT);
        lblEstado.setAlignmentX(LEFT_ALIGNMENT);

        pnlDatos.add(lblOperario);
        pnlDatos.add(Box.createVerticalStrut(20));
        pnlDatos.add(lblCliente);
        pnlDatos.add(Box.createVerticalStrut(20));
        pnlDatos.add(lblFecha);
        pnlDatos.add(Box.createVerticalStrut(20));
        pnlDatos.add(lblEstado);


        //LINEA 1 DECORACION
        
        linea1 = new JPanel();
        linea1.setBackground(COLOR_GRIS_CLARITO);
        linea1.setMaximumSize(new Dimension(700, 2));
        linea1.setAlignmentX(CENTER_ALIGNMENT);

        tablaC = new JPanel(new GridLayout(1, 4, 10, 0));
        tablaC.setOpaque(false);
        tablaC.setMaximumSize(new Dimension(650, 25));
        tablaC.setAlignmentX(CENTER_ALIGNMENT);

        SoP = new JLabel("Servicio / Producto");
        Cant = new JLabel("Cant.", SwingConstants.CENTER);
        PrecU = new JLabel("Precio U.", SwingConstants.RIGHT);
        total = new JLabel("Total", SwingConstants.RIGHT);

        tablaC.add(SoP);
        tablaC.add(Cant);
        tablaC.add(PrecU);
        tablaC.add(total);

        for (Component comp : tablaC.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(new Font("SansSerif", Font.BOLD, 16));
            }
        }

        detalleT = new JPanel(new GridLayout(1, 4, 10, 0));
        detalleT.setOpaque(false);
        detalleT.setMaximumSize(new Dimension(650, 25));
        detalleT.setAlignmentX(CENTER_ALIGNMENT);

        DSoP = new JLabel("Balayage");
        DCant = new JLabel("1", SwingConstants.CENTER);
        DPrecU = new JLabel("$120.000", SwingConstants.RIGHT);
        Dtotal = new JLabel("$120.000", SwingConstants.RIGHT);

        detalleT.add(DSoP);
        detalleT.add(DCant);
        detalleT.add(DPrecU);
        detalleT.add(Dtotal);

        for (Component comp : detalleT.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setFont(new Font("SansSerif", Font.BOLD, 16));
            }
        }
       

        linea2 = new JPanel();
        linea2.setBackground(COLOR_GRIS_CLARITO);
        linea2.setMaximumSize(new Dimension(700, 2));
        linea2.setAlignmentX(CENTER_ALIGNMENT);

        lblFelicidades = new JLabel("¡Felicidades por tu lealtad! Se ha aplicado un descuento automático.");
        lblFelicidades.setFont(new Font("SansSerif", Font.ITALIC, 15));
        lblFelicidades.setForeground(COLOR_DORADO);
        lblFelicidades.setAlignmentX(CENTER_ALIGNMENT);
        
        
        pnlTotal = new JPanel(new BorderLayout());
        pnlTotal.setOpaque(false);
        pnlTotal.setMaximumSize(new Dimension(450, 40));
        pnlTotal.setAlignmentX(CENTER_ALIGNMENT);
        
        lbltxtPagar = new JLabel("PRECIO TOTAL A PAGAR");
        lbltxtPagar.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblMonto = new JLabel("$108.000");
        lblMonto.setFont(new Font("SansSerif", Font.BOLD, 22));
        pnlTotal.add(lbltxtPagar, BorderLayout.WEST);
        pnlTotal.add(lblMonto, BorderLayout.EAST);

        
        infoFactura.add(lblEmpresa);
        infoFactura.add(lblGiro);
        infoFactura.add(Box.createVerticalStrut(100));
        infoFactura.add(pnlDatos); 
        infoFactura.add(Box.createVerticalStrut(50));
        infoFactura.add(linea1);
        infoFactura.add(Box.createVerticalStrut(8));
        infoFactura.add(tablaC);
        infoFactura.add(Box.createVerticalStrut(8));
        infoFactura.add(detalleT);
        infoFactura.add(Box.createVerticalStrut(8));
        infoFactura.add(linea2);
        infoFactura.add(Box.createVerticalStrut(15));
        infoFactura.add(lblFelicidades);
        infoFactura.add(Box.createVerticalStrut(15));
        infoFactura.add(pnlTotal);
        
        
        pnlOpciones = new JPanel();
        pnlOpciones.setLayout(new BoxLayout(pnlOpciones, BoxLayout.Y_AXIS));
        pnlOpciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GRIS_CLARITO, 1),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        btnImprimir = new JButton("Imprimir Factura");
        btnImprimir.setBackground(COLOR_DORADO);
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnImprimir.setMaximumSize(new Dimension(400, 60));
        btnImprimir.setPreferredSize(new Dimension(400, 60));
        btnImprimir.setBorderPainted(false);
        btnImprimir.setAlignmentX(CENTER_ALIGNMENT);

        
        btnEnviar = new JButton("Enviar Factura");
        btnEnviar.setBackground(Color.WHITE);
        btnEnviar.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnEnviar.setMaximumSize(new Dimension(400, 60));
        btnEnviar.setPreferredSize(new Dimension(400, 60));
        btnEnviar.setBorder(BorderFactory.createLineBorder(COLOR_DORADO, 1));
        btnEnviar.setAlignmentX(CENTER_ALIGNMENT);

        
        lbltxtA = new JLabel("¿Hay algún error en el dinero?");
        lbltxtA.setFont(new Font("SansSerif", Font.BOLD, 17));
        lbltxtA.setAlignmentX(CENTER_ALIGNMENT);

        
        btnAnular = new JButton("Anular Factura");
        btnAnular.setBackground(Color.WHITE);
        btnAnular.setForeground(new Color(203, 67, 53));
        btnAnular.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnAnular.setMaximumSize(new Dimension(400, 60));
        btnAnular.setPreferredSize(new Dimension(400, 60));
        btnAnular.setBorder(BorderFactory.createLineBorder(new Color(203, 67, 53), 1));
        btnAnular.setAlignmentX(CENTER_ALIGNMENT);

        
        lbltxtJ = new JLabel("Justificación requerida para anulación:");
        lbltxtJ.setFont(new Font("SansSerif", Font.PLAIN, 17));
        lbltxtJ.setForeground(Color.DARK_GRAY);
        lbltxtJ.setAlignmentX(CENTER_ALIGNMENT);

        txtNota = new JTextArea("Escribir la nota de anulación aquí...");
        txtNota.setFont(new Font("SansSerif", Font.PLAIN, 15));
        txtNota.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtNota.setMaximumSize(new Dimension(400, 70));
        txtNota.setPreferredSize(new Dimension(400, 70));
        txtNota.setAlignmentX(CENTER_ALIGNMENT);

        btnGuardarN = new JButton("Guardar Nota");
        btnGuardarN.setBackground(new Color(62, 58, 46));
        btnGuardarN.setForeground(Color.WHITE);
        btnGuardarN.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnGuardarN.setMaximumSize(new Dimension(400, 60));
        btnGuardarN.setPreferredSize(new Dimension(400, 60));
        btnGuardarN.setBorderPainted(false);
        btnGuardarN.setAlignmentX(CENTER_ALIGNMENT);
        
        
        pnlOpciones.add(Box.createVerticalGlue());
        pnlOpciones.add(btnImprimir);
        pnlOpciones.add(Box.createVerticalStrut(12));
        pnlOpciones.add(btnEnviar);
        pnlOpciones.add(Box.createVerticalStrut(50));
        pnlOpciones.add(lbltxtA);
        pnlOpciones.add(Box.createVerticalStrut(8));
        pnlOpciones.add(btnAnular);
        pnlOpciones.add(Box.createVerticalStrut(25));
        pnlOpciones.add(lbltxtJ);
        pnlOpciones.add(Box.createVerticalStrut(10));
        pnlOpciones.add(txtNota);
        pnlOpciones.add(Box.createVerticalStrut(12));
        pnlOpciones.add(btnGuardarN);
        pnlOpciones.add(Box.createVerticalGlue());

        principal.add(infoFactura);
        principal.add(pnlOpciones);

        this.add(principal, BorderLayout.CENTER);

        return this;
    }  
    
public void setClienteRegistrado(String nombre, String numeroId) {
    lblCliente.setText("Cliente: " + nombre + " / " + numeroId);
    lblEstado.setText("Cliente Registrado");
}

public void setClienteGeneral() {
    lblCliente.setText("Cliente General");
    lblEstado.setText("Cliente General");
}

}