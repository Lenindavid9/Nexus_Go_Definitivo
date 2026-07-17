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
public class VistaValidarCodigo extends JFrame {

    private Container contenedor;
    private JPanel vCodigo;
    private JLabel jVCodigo, fondo;
    public JButton entrar;
    public JTextField tVCodigo;
    private TitledBorder titulo;
    private GridLayout migrid;
    private FlowLayout miflow;

    public VistaValidarCodigo() {
        super("validacion_de_codigo");

        /*Se obtiene el contenedor principal.
        Este contenedor permite agregar y organizar todos los
        componentes gráficos de la interfaz*/
        contenedor = getContentPane();

        this.fondo = new JLabel(new ImageIcon("C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\nexusGo/fondito.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        this.setContentPane(fondo);

        /*Se crea título en la parte superior para identificar el panel donde
        el usuario ingresará el codigo.*/
        titulo = new TitledBorder("Validar codigo");

        /*Se crea el panel que contendrá todos los componentes relacionados
        con la validacion del codigo*/
        vCodigo = new JPanel();

        // Se asigna el borde con título al panel
        vCodigo.setBorder(titulo);

        // Se coloca el color blanco como fondo del panel
        vCodigo.setBackground(Color.WHITE);

        /*Se crea un GridLayout de 8 filas, 4 columnas, horizontal de 8
        y vertical de 5.*/
        migrid = new GridLayout(8, 4, 8, 5);

        //Se asigna el GridLayout al panel para organizar automáticamente
        vCodigo.setLayout(migrid);

        /*Se crea la etiqueta que solicita al usuario ingresar
        el codigo mandado al correo*/
        jVCodigo = new JLabel("Ingrese el codigo de validacion");

        /*Se crea el campo de texto donde el usuario escribirá
        el codigo mandado*/
        tVCodigo = new JTextField();

        //boton
        entrar = new JButton("Entrar");

        //colores
        entrar.setForeground(Color.WHITE);
        entrar.setBackground(Color.decode("#EFB810"));

        // Se agrega la etiqueta de el codigo al panel.        
        vCodigo.add(jVCodigo);
        
        // Se agrega el campo donde el usuario escribirá el codigo solicitado.
        vCodigo.add(tVCodigo);

        // Se agrega una etiqueta vacía que actúa como espacio dentro del GridLayout
        vCodigo.add(new JLabel());
        
        // Se agrega el botón "Entrar".
        vCodigo.add(entrar);

        fondo.add(vCodigo);
    }
}
