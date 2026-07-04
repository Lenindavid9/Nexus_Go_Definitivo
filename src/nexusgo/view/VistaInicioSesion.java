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

    // Paleta de Colores
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_INPUT_BG = new Color(225, 225, 225); 
    private final Color COLOR_INPUT_BORDE = new Color(210, 210, 210);
    private final Color COLOR_GRIS_TEXTO = new Color(110, 110, 110);
    private final Color COLOR_DORADO = Color.decode("#EFB810"); 

    // Fuentes
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private final Font FUENTE_TEXTO_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_LINKS = new Font("Segoe UI", Font.BOLD, 12);

    public VistaInicioSesion() {
        super("Nexus Go - Inicio de Sesión");
        
        // 1. Imagen de Fondo
        this.fondo = new JLabel(new ImageIcon("src/nexusgo/img/fondito.jpg"));
        this.fondo.setLayout(new GridBagLayout()); 
        this.setContentPane(fondo);

        // 2. Tarjeta Blanca
        tarjetaLogin = new JPanel();
        tarjetaLogin.setLayout(new BoxLayout(tarjetaLogin, BoxLayout.Y_AXIS));
        tarjetaLogin.setBackground(COLOR_BLANCO);
        tarjetaLogin.setBorder(new EmptyBorder(35, 45, 35, 45)); 
        tarjetaLogin.setPreferredSize(new Dimension(390, 500)); // Ajustado un poco el alto por el ojo

        // 3. Logo
        JLabel lblLogo = new JLabel("NX");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLogo.setForeground(COLOR_DORADO);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Título
        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 5. Input Documento
        tNroIdentidad = new JTextField();
        configurarEstiloInput(tNroIdentidad, "  Ingrese su número de documento");
        
        // 6. Contenedor Horizontal para la Contraseña y mostrar contraseña
        JPanel panelContrasenaFila = new JPanel();
        panelContrasenaFila.setLayout(new BoxLayout(panelContrasenaFila, BoxLayout.X_AXIS));
        panelContrasenaFila.setOpaque(false); // Para que no tape el fondo blanco de la tarjeta
        panelContrasenaFila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        tContrasena = new JPasswordField();
        configurarEstiloInput(tContrasena, "  Ingresar su contraseña");
        
        btnVerContrasena = new JButton("ver"); // boton para habilitar ver
        btnVerContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnVerContrasena.setPreferredSize(new Dimension(45, 40));
        btnVerContrasena.setMaximumSize(new Dimension(45, 40));
        btnVerContrasena.setBackground(COLOR_INPUT_BG);
        btnVerContrasena.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        btnVerContrasena.setFocusPainted(false);
        btnVerContrasena.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Metemos el campo y el ojo juntos en la fila
        panelContrasenaFila.add(tContrasena);
        panelContrasenaFila.add(Box.createHorizontalStrut(5)); // Separación de 5px entre el campo y el ojo
        panelContrasenaFila.add(btnVerContrasena);

        // 7. Enlace Olvido
        lblOlvideContrasena = new JLabel("¿ Olvide su contraseña ?");
        lblOlvideContrasena.setFont(FUENTE_LINKS);
        lblOlvideContrasena.setForeground(COLOR_GRIS_TEXTO);
        lblOlvideContrasena.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblOlvideContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 8. Botón Entrar
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEntrar.setForeground(COLOR_BLANCO);
        btnEntrar.setBackground(COLOR_DORADO);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44)); 
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 9. Enlace Registro
        lblRegistrate = new JLabel("¡Regístrate aquí!");
        lblRegistrate.setFont(FUENTE_LINKS);
        lblRegistrate.setForeground(COLOR_GRIS_TEXTO); 
        lblRegistrate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistrate.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- ENSAMBLAJE DE LA TARJETA ---
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

        fondo.add(tarjetaLogin);

        // Ventana general
        setSize(900, 630);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void configurarEstiloInput(JTextField campo, String placeholder) {
        campo.setFont(FUENTE_TEXTO_INPUT);
        campo.setBackground(COLOR_INPUT_BG);
        campo.setForeground(Color.BLACK);
        campo.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        campo.setToolTipText(placeholder.trim());
    }


}
