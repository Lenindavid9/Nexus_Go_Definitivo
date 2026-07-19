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

public class VistaValidarIdentificacion extends JFrame {

    private Container contenedor;
    private JPanel identificacion;
    private JLabel jIdentificacion, fondo;
    public JButton confirmar;
    public JTextField tIdentificacion;
    private TitledBorder titulo;
    private GridLayout migrid;
    private FlowLayout miflow;

    public VistaValidarIdentificacion() {
        super("Validacion_de_identificacion");

        /*Se obtiene el contenedor principal.
        Este contenedor permite agregar y organizar todos los
        componentes gráficos de la interfaz*/
        contenedor = getContentPane();

        //fondo de marbolo
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        this.setContentPane(fondo);

        /*Se crea título en la parte superior para identificar el panel donde
        el usuario ingresará su numero de identidad.*/
        titulo = new TitledBorder("Validar identificacion");

        /*Se crea un panel que contendrá las componentes necesarios
        para que el usuario ingrese su número de identificación.*/
        identificacion = new JPanel();

        // Se asigna al panel el borde con título.
        identificacion.setBorder(titulo);

        // se establece el color blanco como fondo del panel
        identificacion.setBackground(Color.WHITE);

        /*Se crea un GridLayout de 8 filas, 4 columnas, horizontal de 8
        y vertical de 5.*/
        migrid = new GridLayout(8, 4, 8, 5);

        //Se asigna el GridLayout al panel para organizar automáticamente
        identificacion.setLayout(migrid);

        /*Se crea la etiqueta que solicita al usuario ingresar
        su numero de identificacion*/
        jIdentificacion = new JLabel("Ingrese su número de identificación");

        /*Se crea el campo de texto donde el usuario escribirá
        su numero de identificacion.*/
        tIdentificacion = new JTextField();

        //Boton
        confirmar = new JButton("Confirmar");

        //colores
        confirmar.setForeground(Color.WHITE);
        confirmar.setBackground(Color.decode("#EFB810"));

        // Se agrega la etiqueta al panel.
        identificacion.add(jIdentificacion);

        /* Se agrega el campo de texto donde el usuario escribirá
        su número de identificación.*/
        identificacion.add(tIdentificacion);

        // Se agrega una etiqueta vacía que actúa como espacio dentro del GridLayout
        identificacion.add(new JLabel());

        // Se agrega el botón "Confirmar".
        identificacion.add(confirmar);

        fondo.add(identificacion);
    }
}
