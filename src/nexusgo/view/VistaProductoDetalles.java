/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author HOME
 */
public class VistaProductoDetalles extends JPanel {

    
    public JButton btnVolver;
    private JLabel lblImagenGrande;
    private JLabel lblNombreProducto;
    private JLabel lblPrecioProducto;
    private JTextArea txtDescripcion;

    /*
     * En el constructor maquetamos la vista respetando el diseño de la imagen:
     * Un panel contenedor central flotante sobre el fondo de mármol, con la imagen
     * del producto a la izquierda y la tarjeta blanca de información a la derecha.
     */
    public VistaProductoDetalles() {
        this.setLayout(new BorderLayout());

     

        // Panel contenedor central
        JPanel panelContenedorCentral = new JPanel(new GridBagLayout());
        panelContenedorCentral.setOpaque(false);

        // Contenedor principal de los detalles (Imagen + Tarjeta)
        JPanel panelDetalleLayout = new JPanel();
        panelDetalleLayout.setLayout(new BoxLayout(panelDetalleLayout, BoxLayout.X_AXIS));
        panelDetalleLayout.setOpaque(false);

        // 1. Panel de la imagen destacada (Lado Izquierdo)
        JPanel panelImagenContenedor = new JPanel(new GridBagLayout());
        panelImagenContenedor.setPreferredSize(new Dimension(420, 480));
        panelImagenContenedor.setOpaque(false); // Tono morado similar al diseño

        lblImagenGrande = new JLabel();
        panelImagenContenedor.add(lblImagenGrande);

        // 2. Tarjeta blanca flotante de información (Lado Derecho)
        JPanel tarjetaInfo = new JPanel();
        tarjetaInfo.setLayout(new BoxLayout(tarjetaInfo, BoxLayout.Y_AXIS));
        tarjetaInfo.setOpaque(false);
        tarjetaInfo.setPreferredSize(new Dimension(360, 480));
        tarjetaInfo.setBorder(new EmptyBorder(20, 25, 25, 25));

        // Botón < Volver
        btnVolver = new JButton("< Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setForeground(Color.BLACK);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setAlignmentX(LEFT_ALIGNMENT);

        // Nombre del Producto
        lblNombreProducto = new JLabel("CARGANDO NOMBRE...");
        lblNombreProducto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNombreProducto.setForeground(Color.BLACK);
        lblNombreProducto.setAlignmentX(LEFT_ALIGNMENT);

        // Precio
        lblPrecioProducto = new JLabel("$0");
        lblPrecioProducto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblPrecioProducto.setForeground(new Color(50, 50, 50));
        lblPrecioProducto.setAlignmentX(LEFT_ALIGNMENT);

        // Descripción del producto
        txtDescripcion = new JTextArea("Cargando descripción del producto...");
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFocusable(false);
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setBorder(null);
        txtDescripcion.setAlignmentX(LEFT_ALIGNMENT);

        // Ensamblamos la tarjeta de información
        tarjetaInfo.add(btnVolver);
        tarjetaInfo.add(Box.createVerticalStrut(20));
        tarjetaInfo.add(lblNombreProducto);
        tarjetaInfo.add(Box.createVerticalStrut(10));
        tarjetaInfo.add(lblPrecioProducto);
        tarjetaInfo.add(Box.createVerticalStrut(15));
        tarjetaInfo.add(txtDescripcion);

        // Unimos ambos lados
        panelDetalleLayout.add(panelImagenContenedor);
        panelDetalleLayout.add(Box.createHorizontalStrut(15));
        panelDetalleLayout.add(tarjetaInfo);

        panelContenedorCentral.add(panelDetalleLayout);

        this.add(panelContenedorCentral, BorderLayout.CENTER);
    }

    /*
     * Método visual puro: Recibe solo datos primitivos enviadas por el controlador
     * para actualizar la interfaz dinámicamente según el producto seleccionado.
     */
    public void mostrarDetalleProducto(String nombre, double precio, String descripcion, String rutaImagen) {
        lblNombreProducto.setText(nombre != null ? nombre.toUpperCase() : "");
        lblPrecioProducto.setText("$ " + precio);
        txtDescripcion.setText(descripcion != null && !descripcion.isEmpty() ? descripcion : "Sin descripción disponible.");

        if (rutaImagen == null || rutaImagen.isEmpty()) {
            rutaImagen = "src/nexusgo/img/producto1.jpg";
        }

        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getImage() != null) {
            Image imgEscalada = icon.getImage().getScaledInstance(300, 380, Image.SCALE_SMOOTH);
            lblImagenGrande.setIcon(new ImageIcon(imgEscalada));
        }
    }

}
