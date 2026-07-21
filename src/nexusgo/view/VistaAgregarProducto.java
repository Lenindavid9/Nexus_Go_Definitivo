/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author USUARIO
 */
public class VistaAgregarProducto extends JPanel {

    public JTextField txtNombre, txtCantidad, txtPrecio, txtPrecioVenta, txtProveedor, txtStockMinimo;
    public JTextArea txtDescripcion;
    public JButton btnEditar, btnImagen, btnVolver;
    public JLabel lblNombreImagen;
    public JLabel lblTitulo; // ahora es campo de la clase, accesible desde el controlador

    public VistaAgregarProducto() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 40, 20, 40));

        // --- ENCABEZADO ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(Color.WHITE);

        lblTitulo = new JLabel("Agregar producto"); // valor inicial por defecto
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 30, 30));

        btnVolver = new JButton("< Volver al inicio");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnVolver, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // --- CUERPO DEL FORMULARIO ---
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBackground(Color.WHITE);
        panelCampos.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Estilo común para inputs
        Font fuenteLabels = new Font("SansSerif", Font.BOLD, 14);
        Color colorTextoLabels = new Color(60, 60, 60);

        // Nombre
        panelCampos.add(crearLabel("Nombre del producto", fuenteLabels, colorTextoLabels));
        txtNombre = crearTextField();
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(12));

        // Descripción
        panelCampos.add(crearLabel("Descripcion del producto", fuenteLabels, colorTextoLabels));
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelCampos.add(new JScrollPane(txtDescripcion));
        panelCampos.add(Box.createVerticalStrut(12));

        // Cantidad
        panelCampos.add(crearLabel("cantidad del producto por unidad", fuenteLabels, colorTextoLabels));
        txtCantidad = crearTextField();
        panelCampos.add(txtCantidad);
        panelCampos.add(Box.createVerticalStrut(12));

        // Precio
        panelCampos.add(crearLabel("Precio", fuenteLabels, colorTextoLabels));
        txtPrecio = crearTextField();
        panelCampos.add(txtPrecio);
        panelCampos.add(Box.createVerticalStrut(12));

        panelCampos.add(crearLabel("Precio de venta", fuenteLabels, colorTextoLabels));
        txtPrecioVenta = crearTextField();
        panelCampos.add(txtPrecioVenta);
        panelCampos.add(Box.createVerticalStrut(12));

        // Proveedor
        panelCampos.add(crearLabel("proveedor", fuenteLabels, colorTextoLabels));
        txtProveedor = crearTextField();
        panelCampos.add(txtProveedor);
        panelCampos.add(Box.createVerticalStrut(12));

        // Stock Mínimo (Alerta)
        panelCampos.add(crearLabel("Ingresar cantidad minima de producto para la activacion de la alerta", fuenteLabels, colorTextoLabels));
        txtStockMinimo = crearTextField();
        panelCampos.add(txtStockMinimo);
        panelCampos.add(Box.createVerticalStrut(15));

        // --- SUBIR IMAGEN ---
        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelImagen.setBackground(Color.WHITE);

        btnImagen = new JButton("Imagen del producto");
        btnImagen.setBackground(Color.WHITE);
        btnImagen.setForeground(new Color(230, 180, 40));
        btnImagen.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnImagen.setBorder(BorderFactory.createLineBorder(new Color(230, 180, 40), 1, true));
        btnImagen.setFocusPainted(false);
        btnImagen.setPreferredSize(new Dimension(160, 32));
        btnImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblNombreImagen = new JLabel("Ningún archivo seleccionado"); // antes: "tratamiento.png"
        lblNombreImagen.setForeground(new Color(120, 120, 120));
        lblNombreImagen.setFont(new Font("SansSerif", Font.PLAIN, 13));

        panelImagen.add(btnImagen);
        panelImagen.add(lblNombreImagen);
        panelCampos.add(panelImagen);

        add(new JScrollPane(panelCampos), BorderLayout.CENTER);

        // --- BOTÓN INFERIOR (EDITAR / GUARDAR) ---
        JPanel panelBotonAbajo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonAbajo.setBackground(Color.WHITE);
        panelBotonAbajo.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(254, 222, 79)); // Amarillo de tu paleta
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnEditar.setFocusPainted(false);
        btnEditar.setPreferredSize(new Dimension(180, 40));
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setBorder(BorderFactory.createLineBorder(new Color(254, 222, 79), 1, true)); // única línea de borde (se quitó la duplicada)

        panelBotonAbajo.add(btnEditar);
        add(panelBotonAbajo, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(color);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JTextField crearTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 38));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        return tf;
    }
}