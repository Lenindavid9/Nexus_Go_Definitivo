/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author USUARIO
 */

public class VistaInicioSesion extends JFrame {
    
    private JLabel fondo;
    private JPanel tarjetaLogin;
    public JTextField tNroIdentidad;
    public JPasswordField tContrasena;
    public JButton btnEntrar;
    public JButton btnVerContrasena; // El botón del ojo añadido de forma nativa
    public JLabel lblRegistrate; 
    public JLabel lblOlvideContrasena;

    /*Se centralizan todos los colores utilizados en la
      interfaz para mantener una apariencia uniforme y
      facilitar futuras modificaciones.*/
    private final Color COLOR_INPUT_BG = new Color(225, 225, 225); 
    private final Color COLOR_INPUT_BORDE = new Color(210, 210, 210);
    

    /*Se definen las fuentes utilizadas en toda la interfaz
      para mantener un diseño consistente*/
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FUENTE_TEXTO_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_LINKS = new Font("Segoe UI", Font.BOLD, 12);

    public VistaInicioSesion() {
        super("Nexus Go - Inicio de Sesión");
        
        /*Se definen las fuentes utilizadas en toda la interfaz
        para mantener un diseño consistente*/
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg"));
        
        /*GridBagLayout permite colocar
        la tarjeta blanca del centro. */
        this.fondo.setLayout(new GridBagLayout()); 
        
        /* La imagen pasa a convertirse en el contenido
        principal del fondo de la ventana.*/
        this.setContentPane(fondo);

        /* Panel donde estarán todos los controles
        del formulario.*/
        tarjetaLogin = new JPanel();
        
        /* BoxLayout organiza los componentes
        verticalmente, uno debajo del otro.*/
        tarjetaLogin.setLayout(new BoxLayout(tarjetaLogin, BoxLayout.Y_AXIS));
        tarjetaLogin.setBackground(Color.WHITE);
        
        /* EmptyBorder crea márgenes internos para que
        los componentes no queden pegados al borde.*/
        tarjetaLogin.setBorder(new EmptyBorder(35, 45, 35, 45)); 
        
        //Tamaño de la tarjeta
        tarjetaLogin.setPreferredSize(new Dimension(390, 500)); 

        // el supuesto logo
        JLabel lblLogo = new JLabel("NX");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLogo.setForeground(Color.decode("#EFB810")); 
        
        //Centra el logo dentro del panel.
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        tNroIdentidad = new JTextField();
        
        /*Se aplica el mismo estilo visual
        definido para todos los campos.*/
        configurarEstiloInput(tNroIdentidad, "  Ingrese su número de documento");
        
        /*Panel horizontal que agrupa el campo
        de contraseña y el botón "Ver". */
        JPanel panelContrasenaFila = new JPanel();
        panelContrasenaFila.setLayout(new BoxLayout(panelContrasenaFila, BoxLayout.X_AXIS));
        
        /* Hace transparente el panel para que
        conserve el color blanco de la tarjeta.*/
        panelContrasenaFila.setOpaque(false);
        panelContrasenaFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        tContrasena = new JPasswordField();
        configurarEstiloInput(tContrasena,"  Ingresar su contraseña");
        
        /* Botón utilizado para mostrar o ocultar
        la contraseña escrita.*/
        btnVerContrasena = new JButton("Ver"); // boton para habilitar ver
        btnVerContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnVerContrasena.setPreferredSize(new Dimension(45, 40));
        btnVerContrasena.setMaximumSize(new Dimension(45, 40));
        btnVerContrasena.setBackground(COLOR_INPUT_BG);
        btnVerContrasena.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        
        /* Elimina el borde de enfoque cuando
        el botón recibe el clic.*/
        btnVerContrasena.setFocusPainted(false);
        
        // Cambia el cursor por una mano        
        btnVerContrasena.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Metemos el campo y el ojo juntos en la fila
        panelContrasenaFila.add(tContrasena);
        panelContrasenaFila.add(Box.createHorizontalStrut(5)); // Separación de 5px entre el campo y el ojo
        panelContrasenaFila.add(btnVerContrasena);

        // Enlace Olvido contraseña
        lblOlvideContrasena = new JLabel("¿ Olvide su contraseña ?");
        lblOlvideContrasena.setFont(FUENTE_LINKS);
        lblOlvideContrasena.setForeground(Color.GRAY);
        lblOlvideContrasena.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblOlvideContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Botón Entrar
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBackground(Color.decode("#EFB810")); 
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44)); 
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Enlace Registro
        lblRegistrate = new JLabel("¡Regístrate aquí!");
        lblRegistrate.setFont(FUENTE_LINKS);
        lblRegistrate.setForeground(Color.GRAY); 
        lblRegistrate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistrate.setAlignmentX(Component.CENTER_ALIGNMENT);

        /*En esta sección se agregan todos los componentes
        al panel principal con el orden en que
        aparecerán.*/
        tarjetaLogin.add(lblLogo);
        tarjetaLogin.add(Box.createVerticalStrut(8));
        tarjetaLogin.add(lblTitulo);
        tarjetaLogin.add(Box.createVerticalStrut(25)); 
        
        tarjetaLogin.add(tNroIdentidad);
        tarjetaLogin.add(Box.createVerticalStrut(18));
        
        tarjetaLogin.add(panelContrasenaFila); // Añadimos la fila completa con el ojo armado
        tarjetaLogin.add(Box.createVerticalStrut(25));
        
        tarjetaLogin.add(lblOlvideContrasena);
        tarjetaLogin.add(Box.createVerticalStrut(15));
        
        tarjetaLogin.add(btnEntrar);
        tarjetaLogin.add(Box.createVerticalStrut(15));
        
        tarjetaLogin.add(lblRegistrate);

        // la tarjeta se coloca sobre la imagen de fondo
        fondo.add(tarjetaLogin);
        
        // se supone que es minimo permitido del tamaño de la ventana.....supuestamente
        setMinimumSize(new Dimension(500, 500));
        
        //Centra la ventana en la pantalla
        setLocationRelativeTo(null);
        
        //aqui se cierra todo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /* Método para aplicar el mismo estilo
    a todos los campos de texto del formulario.
    De esta manera se evita repetir el mismo código varias veces,
    haciendo el codigo sea más organizado y fácil de mantener.*/
    
    private void configurarEstiloInput(JTextField campo , String placeholder) {
        
        //Se aplica la fuente definida para los campos.
        campo.setFont(FUENTE_TEXTO_INPUT);
        
        //Color de fondo del campo
        campo.setBackground(COLOR_INPUT_BG);
        
        //Color del texto que escribirá el usuario.|
        campo.setForeground(Color.BLACK);
        
        //Se establece un borde sencillo alrededor del campo.
        campo.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        
        /* Se permite que el campo pueda crecer
        horizontalmente según el tamaño del panel.*/
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        
        /* El texto recibido se utiliza como ayuda
        cuando el usuario coloca el cursor arroba de el campo.*/
        campo.setToolTipText(placeholder.trim());
    }
}