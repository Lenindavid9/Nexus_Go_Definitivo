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
public class VistaNuevaContrasena extends JFrame {

    private Container contenedor;
    private JPanel recuperar;
    private JLabel fondo, jContrasena, jConfirmar;
    public JButton cambiar;
    public JTextField tContrasena, tConfirmar;
    private TitledBorder titulo;
    private GridLayout migrid;
    private FlowLayout miflow;

    public VistaNuevaContrasena() {
        super("Reestablecer_contraseña");

        /*Se obtiene el contenedor principal.
        Este contenedor permite agregar y organizar todos los
        componentes gráficos de la interfaz*/
        contenedor = getContentPane();

        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        this.setContentPane(fondo);

        /*Se crea título en la parte superior para identificar el panel donde
        el usuario ingresará la nueva contraseña.*/
        titulo = new TitledBorder("Cambiar contraseña");

        /*Se crea el panel que contendrá todos los componentes relacionados
        con el cambio de contraseñ*/
        recuperar = new JPanel();

        // Se asigna el borde con título al panel
        recuperar.setBorder(titulo);

        // Se coloca el color blanco como fondo del panel
        recuperar.setBackground(Color.WHITE);

        /*Se crea un GridLayout de 8 filas, 4 columnas, horizontal de 8
        y vertical de 5.*/
        migrid = new GridLayout(8, 4, 8, 5);

        //Se asigna el GridLayout al panel para organizar automáticamente
        recuperar.setLayout(migrid);

        /*Se crea la etiqueta que solicita al usuario ingresar
        su nueva contraseña*/
        jContrasena = new JLabel("Ingrese su nueva contraseña");

        /*Se crea el campo de texto donde el usuario escribirá
        la nueva contraseña.*/
        tContrasena = new JTextField();

        //Se crea la etiqueta que solicita confirmar la contraseña ingresada
        jConfirmar = new JLabel("Confirma la contraseña");

        /*Se crea el segundo campo donde el usuario deberá
        escribir nuevamente la contraseña para verificar que ambas
        coincidan*/
        tConfirmar = new JTextField();

        //boton
        cambiar = new JButton("Cambiar");

        //colores
        cambiar.setForeground(Color.WHITE);
        cambiar.setBackground(Color.decode("#EFB810"));

        // Se agrega la etiqueta de la nueva contraseña al panel.
        recuperar.add(jContrasena);

        // Se agrega el campo donde el usuario escribirá la nueva contraseña.
        recuperar.add(tContrasena);

        // Se agrega la etiqueta para confirmar la contraseña.
        recuperar.add(jConfirmar);

        /* Se agrega el campo donde el usuario volverá a escribir
        la contraseña para confirmar que es correcta.*/
        recuperar.add(tConfirmar);

        // Se agrega una etiqueta vacía que actúa como espacio dentro del GridLayout
        recuperar.add(new JLabel());

        // Se agrega el botón "Cambiar".
        recuperar.add(cambiar);

        /* aqui el panel que contiene todos los componentes
        se agrega sobre el JLabel que funciona como fondo de la ventana.*/
        fondo.add(recuperar);
    }
}
