/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author HOME
 */
public class VistaDetallesProducto extends JFrame {
    // Declaro mis componentes públicos para que el controlador los pueda leer y rellenar fácilmente
    public JLabel lblImagenProducto;
    public JLabel lblNombreProducto;
    public JLabel lblPrecioProducto;
    public JTextArea txtDescripcion;
    public JButton btnVolver;

    public VistaDetallesProducto() {
        // Le pongo el título a mi ventana flotante de detalles
        setTitle("Nexus GO - Detalle de Producto");
        // Defino un tamaño cómodo para que quepan la imagen y los textos sin verse apretados
        setSize(850, 550);
        // Le indico que cuando el usuario cierre esta ventana solo destruya esta y no todo el programa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Hago que la ventana aparezca perfectamente centrada en la pantalla de mi computadora
        setLocationRelativeTo(null);
        // Organizo mi disposición general con márgenes de 15 píxeles a los lados para que no se pegue nada
        setLayout(new BorderLayout(15, 15));

        // Instancio mi barra lateral existente para mantener el mismo menú en el lado izquierdo
        VistaBarraLateral barraLateral = new VistaBarraLateral(); 
        // Meto la barra lateral en la zona oeste de mi contenedor principal
        add(barraLateral, BorderLayout.WEST);

        // Creo un panel central dividido a la mitad en dos columnas iguales para separar la foto de la info
        JPanel panelCuerpo = new JPanel(new GridLayout(1, 2, 20, 0));
        // Le pongo fondo blanco a mi panel central para mantener un estilo limpio y minimalista
        panelCuerpo.setBackground(Color.WHITE);

        // Creo el panel contenedor que va a sostener la foto del producto a la izquierda
        JPanel panelImagen = new JPanel(new BorderLayout());
        // Le asigno el fondo morado característico de mi diseño para que resalte la foto
        panelImagen.setBackground(new Color(203, 174, 242)); 
        
        // Inicializo mi etiqueta de imagen centrando el texto temporal que lleva dentro
        lblImagenProducto = new JLabel("[ Imagen ]", SwingConstants.CENTER);
        // Hago que el texto temporal sea blanco para que combine con el fondo morado
        lblImagenProducto.setForeground(Color.WHITE);
        // Acomodo la etiqueta justo en el centro de mi contenedor de imagen
        panelImagen.add(lblImagenProducto, BorderLayout.CENTER);
        // Meto todo el bloque de la imagen dentro de mi panel central dividido
        panelCuerpo.add(panelImagen);

        // Creo el panel de la derecha donde voy a organizar toda la información del producto
        JPanel panelInfo = new JPanel();
        // Configuro un diseño de caja vertical para que los datos se organicen uno debajo del otro
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        // Le pongo fondo blanco al bloque de información para que se vea elegante
        panelInfo.setBackground(Color.WHITE);

        // Instancio mi botón de volver con una flecha indicativa
        btnVolver = new JButton("< Volver");
        // Alineo mi botón hacia el extremo izquierdo de mi panel de datos
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Agrego el botón de regresar al inicio de mi panel de información
        panelInfo.add(btnVolver);
        // Meto un espacio invisible de 15 píxeles debajo del botón para que no se pegue con el título
        panelInfo.add(Box.createVerticalStrut(15));

        // Inicializo la etiqueta con un texto temporal antes de que el controlador cargue el verdadero
        lblNombreProducto = new JLabel("Cargando Producto...");
        // Le asigno una fuente Segoe UI en negrita y tamaño 20 para que actúe como título principal
        lblNombreProducto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        // Alineo el título hacia la izquierda de la tarjeta
        lblNombreProducto.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Meto el título de mi producto al panel
        panelInfo.add(lblNombreProducto);
        // Dejo un espacio de 5 píxeles de separación vertical antes de poner el precio
        panelInfo.add(Box.createVerticalStrut(5));

        // Inicializo la etiqueta del precio con un valor base en ceros
        lblPrecioProducto = new JLabel("$0.000");
        // Le configuro la fuente en negrita con un tamaño de 16 para que se lea con claridad
        lblPrecioProducto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // Le pongo un color gris oscuro para diferenciarlo visualmente del título del producto
        lblPrecioProducto.setForeground(Color.DARK_GRAY);
        // Alineo el precio también hacia el extremo izquierdo
        lblPrecioProducto.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Agrego mi etiqueta del precio al panel
        panelInfo.add(lblPrecioProducto);
        // Genero un espacio de separación de 15 píxeles antes de pintar el bloque de la descripción
        panelInfo.add(Box.createVerticalStrut(15));

        // Inicializo mi caja de texto multilínea para la descripción detallada del artículo
        txtDescripcion = new JTextArea("Descripción detallada...");
        // Le coloco una tipografía plana y limpia de tamaño 13 para facilitar la lectura
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        // Activo el ajuste de línea automático para que el texto no se salga horizontalmente de la pantalla
        txtDescripcion.setLineWrap(true);
        // Le indico que corte las líneas exactamente en los espacios de las palabras y no a mitad de una letra
        txtDescripcion.setWrapStyleWord(true);
        // Desactivo la edición para que el cliente final solo pueda leer el texto y no modificarlo
        txtDescripcion.setEditable(false);
        
        // Meto mi caja de descripción dentro de un panel con barras de desplazamiento automático
        JScrollPane scroll = new JScrollPane(txtDescripcion);
        // Le elimino los bordes grisáceos por defecto del scroll para mantener mi diseño minimalista
        scroll.setBorder(null);
        // Alineo mi bloque con scroll hacia la izquierda
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Agrego el scroll con la descripción al panel de información
        panelInfo.add(scroll);

        // Meto todo mi panel de información ya armado en la columna derecha de mi panel central dividido
        panelCuerpo.add(panelInfo);

        // Ubico todo mi panel de cuerpo combinado en el centro exacto de la pantalla principal
        add(panelCuerpo, BorderLayout.CENTER);
}
}
