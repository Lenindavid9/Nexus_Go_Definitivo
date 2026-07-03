/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

/**
 *
 * @author USUARIO
 */
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

public class VistaValidarIdentificacion extends JFrame{
    private Container contenedor;
    private JPanel identificacion;
    private JLabel jIdentificacion, fondo;
    public JButton confirmar;
    public JTextField tIdentificacion;
    private TitledBorder titulo;
    private GridLayout migrid;
    private FlowLayout miflow;
    
    public VistaValidarIdentificacion(){
        super("Validacion_de_identificacion");
        contenedor = getContentPane();

        //miflow = new FlowLayout();
        //contenedor.setLayout(miflow);
        //contenedor.setLayout(new GridBagLayout());
        //fondo de marbolo
        this.fondo = new JLabel(new ImageIcon ("C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\nexusGo/fondito.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        this.setContentPane(fondo);

        titulo = new TitledBorder("Validar identificacion");

        identificacion = new JPanel();
        identificacion.setBorder(titulo);
        identificacion.setBackground(Color.WHITE);

        migrid = new GridLayout(8, 4, 8, 5);
        
        identificacion.setLayout(migrid);

        jIdentificacion = new JLabel("Ingrese su número de identificación");
        tIdentificacion = new JTextField();
        
        //Boton
        confirmar = new JButton("Confirmar");
        //colores
        confirmar.setForeground(Color.WHITE);
        confirmar.setBackground(Color.decode("#EFB810"));

        identificacion.add(jIdentificacion);
        identificacion.add(tIdentificacion);

        identificacion.add(new JLabel());
        identificacion.add(confirmar);        

        fondo.add(identificacion);
    }
}