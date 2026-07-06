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
public class VistaNuevaContrasena extends JFrame{
    private Container contenedor;
    private JPanel recuperar;
    private JLabel fondo, jContrasena, jConfirmar;
    private JButton cambiar;
    public JTextField tContrasena, tConfirmar;
    private TitledBorder titulo;
    private GridLayout migrid;
    private FlowLayout miflow;
    
    public VistaNuevaContrasena(){
        super("Reestablecer_contraseña");
        contenedor = getContentPane();

        //miflow = new FlowLayout();
        //contenedor.setLayout(miflow);
        //contenedor.setLayout(new GridBagLayout());
        this.fondo = new JLabel(new ImageIcon ("C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\nexusGo/fondito.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        this.setContentPane(fondo);

        titulo = new TitledBorder("Cambiar contraseeña");

        recuperar = new JPanel();
        recuperar.setBorder(titulo);
        recuperar.setBackground(Color.WHITE);

        migrid = new GridLayout(8, 4, 8, 5);
        
        recuperar.setLayout(migrid);

        jContrasena = new JLabel("Ingrese su nueva contraseña");
        tContrasena = new JTextField();
        
        jConfirmar = new JLabel("Confirma la contraseña");
        tConfirmar = new JTextField();
        
        //boton
        cambiar = new JButton("Cambiar");
        //colores
        cambiar.setForeground(Color.WHITE);
        cambiar.setBackground(Color.decode("#EFB810"));

        recuperar.add(jContrasena);
        recuperar.add(tContrasena);
        
        recuperar.add(jConfirmar);
        recuperar.add(tConfirmar);

        recuperar.add(new JLabel());
        recuperar.add(cambiar);

        fondo.add(recuperar);
    }
}
