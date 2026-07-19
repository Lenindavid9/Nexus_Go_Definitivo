/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class VistaRegistroDeUsuario extends JFrame {

    // Componente para colocar la imagen de fondo de mármol
    private JLabel fondo;

    // Los dos paneles blancos principales (las tarjetas que flotan en el centro)
    private JPanel tarjetaIzquierda, tarjetaDerecha;

    // Cajas de texto para que el usuario escriba (Públicas para que el controlador las lea)
    // NOTA CLAVE: Conservamos tNroIdentificacion para que engrane con el Controlador
    public JTextField tNombre, tApellido, tNroIdentificacion, tCorreo;

    // Cajas especiales para contraseñas (ocultan las letras con puntos automáticos)
    public JPasswordField tContrasena, tConfirmar;

    // Lista desplegable para seleccionar la cédula o tarjeta
    public JComboBox<String> miTipoDocumento;

    // Botones de acción para registrarse o regresar al inicio de sesión
    public JButton btnRegistrarse, btnVolver;

    // Opciones fijas que se mostrarán dentro de la lista desplegable (ComboBox)
    private final String[] tiposDocumento = {
        "Seleccione su tipo de documento",
        "Cédula de ciudadanía",
        "Cédula de extranjería",
        "Tarjeta de identidad",
        "Permiso de permanencia temporal"
    };

    // --- PALETA DE COLORES (Definimos los tonos elegantes del diseño) ---
    private final Color COLOR_BLANCO = Color.WHITE; // Fondo de las tarjetas
    private final Color COLOR_GRIS_TEXTO = new Color(102, 102, 102); // Gris sutil para subtítulos
    private final Color COLOR_INPUT_BG = new Color(250, 250, 250); // Fondo gris muy claro para los inputs
    private final Color COLOR_INPUT_BORDE = new Color(218, 218, 218); // Borde delgado para las cajas
    private final Color COLOR_DORADO = Color.decode("#EFB810"); // Color corporativo de Nexus

    // --- FUENTES TIPOGRÁFICAS (Configuración del tamaño y grosor de las letras) ---
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 28); // Título grande "Registro"
    private final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 13); // Mensajes pequeños de bienvenida
    private final Font FUENTE_LABELS = new Font("Segoe UI", Font.BOLD, 13); // Texto encima de los campos (Ej: "Nombre")
    private final Font FUENTE_INPUTS = new Font("Segoe UI", Font.PLAIN, 14); // Letra que aparece al escribir en las cajas

    /**
     * Constructor: Aquí se construye la ventana y se acomodan todas las piezas
     * al abrirse.
     */
    public VistaRegistroDeUsuario() {
        // Le asignamos el título superior a la ventana de Windows
        super("Nexus Go! - Registro de Usuario");

        // 1. CONFIGURACIÓN DEL FONDO CON RUTA DE RECURSOS (Para evitar fallos de carga)
        java.net.URL urlFondo = getClass().getResource("/nexusgo/img/fondito.jpg");
        if (urlFondo != null) {
            this.fondo = new JLabel(new ImageIcon(urlFondo));
        } else {
            this.fondo = new JLabel(new ImageIcon("fondito.jpg")); // Respaldo local
        }

        // GridBagLayout sirve aquí para que las tarjetas blancas se queden perfectamente centradas en la pantalla
        this.fondo.setLayout(new GridBagLayout());

        // Convertimos el JLabel con la imagen en el panel principal de la ventana
        this.setContentPane(fondo);

        // --- CONSTRUCCIÓN TARJETA IZQUIERDA (Bienvenida y Datos Personales) ---
        tarjetaIzquierda = new JPanel();

        // BoxLayout organiza todos los elementos uno debajo del otro, como una torre
        tarjetaIzquierda.setLayout(new BoxLayout(tarjetaIzquierda, BoxLayout.Y_AXIS));
        tarjetaIzquierda.setBackground(COLOR_BLANCO); // Pintamos la tarjeta de blanco

        // EmptyBorder le da un "colchón" o margen interno para que las letras no toquen las esquinas
        tarjetaIzquierda.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Le fijamos un tamaño fijo ideal a la tarjeta izquierda
        tarjetaIzquierda.setPreferredSize(new Dimension(380, 520));

        // Creación y diseño de los textos del encabezado izquierdo
        JLabel lblLogoSimbolo = new JLabel("NX");
        lblLogoSimbolo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogoSimbolo.setForeground(COLOR_DORADO); // Iniciales en dorado
        lblLogoSimbolo.setAlignmentX(LEFT_ALIGNMENT); // Lo alineamos a la izquierda

        JLabel lblBienvenida = new JLabel("Bienvenido a Nexus Go!");
        lblBienvenida.setFont(FUENTE_SUBTITULO);
        lblBienvenida.setForeground(COLOR_GRIS_TEXTO);
        lblBienvenida.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblRegistro = new JLabel("Registro");
        lblRegistro.setFont(FUENTE_TITULO);
        lblRegistro.setForeground(Color.BLACK);
        lblRegistro.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSlogan = new JLabel("Es un honor ser quien te ayudamos a brillar!!");
        lblSlogan.setFont(FUENTE_SUBTITULO);
        lblSlogan.setForeground(COLOR_GRIS_TEXTO);
        lblSlogan.setAlignmentX(LEFT_ALIGNMENT);

        // Inicializamos las cajas de texto llamando a los métodos de estilo que están abajo
        tNombre = crearCampoTexto();
        tApellido = crearCampoTexto();
        miTipoDocumento = new JComboBox<>(tiposDocumento);
        estilizarComboBox(miTipoDocumento); // Le aplicamos el diseño plano al combo
        tNroIdentificacion = crearCampoTexto(); // Variable perfectamente emparejada con tu controlador

        // Agregamos los textos a la tarjeta izquierda poniendo espacios invisibles (Strut) en el medio
        tarjetaIzquierda.add(lblLogoSimbolo);
        tarjetaIzquierda.add(Box.createVerticalStrut(10)); // Espacio vertical de 10 píxeles
        tarjetaIzquierda.add(lblBienvenida);
        tarjetaIzquierda.add(lblRegistro);
        tarjetaIzquierda.add(lblSlogan);
        tarjetaIzquierda.add(Box.createVerticalStrut(20)); // Espacio libre antes de los campos

        // Insertamos los bloques ordenados (Etiqueta + Caja de texto) usando el método auxiliar
        agregarBloqueFormulario(tarjetaIzquierda, "Nombre", tNombre);
        agregarBloqueFormulario(tarjetaIzquierda, "Apellido", tApellido);
        agregarBloqueFormulario(tarjetaIzquierda, "Seleccione su tipo de documento", miTipoDocumento);
        agregarBloqueFormulario(tarjetaIzquierda, "Número de identificación", tNroIdentificacion);

        // --- CONSTRUCCIÓN TARJETA DERECHA (Credenciales y Botón de Acción) ---
        tarjetaDerecha = new JPanel();
        tarjetaDerecha.setLayout(new BoxLayout(tarjetaDerecha, BoxLayout.Y_AXIS)); // Formato torre vertical
        tarjetaDerecha.setBackground(COLOR_BLANCO);
        tarjetaDerecha.setBorder(new EmptyBorder(30, 40, 30, 40)); // Margen interno
        tarjetaDerecha.setPreferredSize(new Dimension(380, 520));

        // Creamos un pequeño panel invisible alineado a la derecha para poner el botón "Volver"
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelVolver.setBackground(COLOR_BLANCO);

        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVolver.setForeground(COLOR_GRIS_TEXTO);
        btnVolver.setBorderPainted(false); // Le quitamos el borde feo de los botones antiguos
        btnVolver.setContentAreaFilled(false); // Lo dejamos transparente para que parezca un link
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hace que aparezca la manito de clic del mouse
        panelVolver.add(btnVolver); // Metemos el botón en su alineador
        panelVolver.setAlignmentX(LEFT_ALIGNMENT);

        // Inicializamos las cajas de la derecha
        tCorreo = crearCampoTexto();
        tContrasena = crearCampoPassword();
        tConfirmar = crearCampoPassword();

        // Configuración estética del botón dorado de registro
        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrarse.setForeground(COLOR_BLANCO); // Letra blanca
        btnRegistrarse.setBackground(COLOR_DORADO); // Fondo dorado corporativo
        btnRegistrarse.setBorderPainted(false); // Sin bordes rígidos Windows
        btnRegistrarse.setFocusPainted(false); // Quita el cuadro punteado feo al hacer clic
        btnRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Activa la manito del mouse
        btnRegistrarse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Obliga al botón a ocupar todo el ancho con 45px de alto
        btnRegistrarse.setAlignmentX(LEFT_ALIGNMENT);

        // Agregamos las piezas a la tarjeta derecha
        tarjetaDerecha.add(panelVolver);
        tarjetaDerecha.add(Box.createVerticalStrut(30)); // Espacio en blanco para nivelar con la otra tarjeta

        // Insertamos los bloques de contraseña y correo
        agregarBloqueFormulario(tarjetaDerecha, "Correo electrónico", tCorreo);
        agregarBloqueFormulario(tarjetaDerecha, "Contraseña", tContrasena);
        agregarBloqueFormulario(tarjetaDerecha, "Confirme su contraseña", tConfirmar);
        tarjetaDerecha.add(Box.createVerticalStrut(25)); // Espacio antes de llegar al botón
        tarjetaDerecha.add(btnRegistrarse); // Añadimos el botón de guardado

        // --- ENSAMBLAJE FINAL EN EL CONTENEDOR PRINCIPAL ---
        // Creamos una cuadrícula invisible de 1 fila y 2 columnas para pegar las dos tarjetas de lado a lado
        JPanel contenedorTarjetas = new JPanel(new GridLayout(1, 2, 25, 0)); // 25 píxeles de espacio de separación en el centro
        contenedorTarjetas.setOpaque(false); // Lo dejamos transparente para que no tape el mármol del fondo
        contenedorTarjetas.add(tarjetaIzquierda);
        contenedorTarjetas.add(tarjetaDerecha);

        // Añadimos el bloque de las dos tarjetas encima del fondo con imagen
        fondo.add(contenedorTarjetas);

        // Ajustes finales de la ventana general
        setSize(950, 650); // Ancho y alto de la aplicación
        setLocationRelativeTo(null); // Centra la ventana en la mitad de la pantalla al abrirse
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra por completo el proceso de Java al dar clic en la 'X'
    }

    // --- MÉTODOS AUXILIARES DE ESTILIZACIÓN (Sirven para reutilizar código y no repetir diseño) ---
    private JTextField crearCampoTexto() {
        JTextField tf = new JTextField();
        tf.setFont(FUENTE_INPUTS);
        tf.setBackground(COLOR_INPUT_BG);
        tf.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return tf;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FUENTE_INPUTS);
        pf.setBackground(COLOR_INPUT_BG);
        pf.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        pf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return pf;
    }

    private void estilizarComboBox(JComboBox<String> cb) {
        cb.setFont(FUENTE_INPUTS);
        cb.setBackground(COLOR_INPUT_BG);
        cb.setBorder(new LineBorder(COLOR_INPUT_BORDE, 1));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    }

    private void agregarBloqueFormulario(JPanel panelDestino, String textoLabel, javax.swing.JComponent campoInput) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(FUENTE_LABELS);
        label.setForeground(Color.BLACK);
        label.setAlignmentX(LEFT_ALIGNMENT);
        campoInput.setAlignmentX(LEFT_ALIGNMENT);

        panelDestino.add(label);
        panelDestino.add(Box.createVerticalStrut(6));
        panelDestino.add(campoInput);
        panelDestino.add(Box.createVerticalStrut(14));
    }
}
