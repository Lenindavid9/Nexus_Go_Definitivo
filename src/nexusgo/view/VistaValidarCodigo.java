/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author USUARIO
 */
public class VistaValidarCodigo extends JFrame{
    private Container contenedor;
    private JPanel vCodigo;
    private JLabel jVCodigo, fondo;
    private JButton enviar;
    public JTextField tVCodigo;
    private TitledBorder titulo;
    private GridLayout migrid;
    private FlowLayout miflow;
    
    public VistaValidarCodigo(){
        super("validacion_de_codigo");
        contenedor = getContentPane();

        //miflow = new FlowLayout();
        //contenedor.setLayout(miflow);
        //contenedor.setLayout(new GridBagLayout());
        this.fondo = new JLabel(new ImageIcon ("C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\nexusGo/fondito.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        this.setContentPane(fondo);

        titulo = new TitledBorder("Validar codigo");

        vCodigo = new JPanel();
        vCodigo.setBorder(titulo);
        vCodigo.setBackground(Color.WHITE);

        migrid = new GridLayout(8, 4, 8, 5);
        
        vCodigo.setLayout(migrid);

        jVCodigo = new JLabel("Ingrese el codigo de validacion");
        tVCodigo = new JTextField();

        //boton
        enviar = new JButton("Entrar");
        //colores
        enviar.setForeground(Color.WHITE);
        enviar.setBackground(Color.decode("#EFB810"));

        vCodigo.add(jVCodigo);
        vCodigo.add(tVCodigo);

        vCodigo.add(new JLabel());
        vCodigo.add(enviar);

        fondo.add(vCodigo);
    }
}