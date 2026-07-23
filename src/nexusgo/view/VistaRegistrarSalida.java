/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;


public class VistaRegistrarSalida extends JPanel {

    public JTextField txtCantidadSalida;
    public JButton btnRegistrarSalida;
    public JButton btnVolver;
    private JLabel lblTitulo;
    private JLabel lblInstruccion;
    
    private final Color COLOR_DORADO = new Color(184, 134, 11);
   

    public VistaRegistrarSalida() {
        this.setOpaque(false);
        this.setLayout(null); // Usamos diseño libre/absoluto para posicionar consetBounds

        // Título Principal
        lblTitulo = new JLabel("REGISTRAR SALIDA DE INSUMO", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.white);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBounds(550, 80, 300, 40);
        add(lblTitulo);

        // Etiqueta de instrucción
        lblInstruccion = new JLabel("Cantidad a retirar:");
        lblInstruccion.setForeground(Color.white);
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblInstruccion.setBounds(550, 140, 200, 30);
        add(lblInstruccion);

        // Caja de texto para la cantidad
        txtCantidadSalida = new JTextField();
        txtCantidadSalida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCantidadSalida.setBounds(550, 180, 200, 30);
        add(txtCantidadSalida);

        // Botón Registrar Salida (Estilo Amarillo/Dorado Nexus)
        btnRegistrarSalida = new JButton("Registrar Salida");
        btnRegistrarSalida.setBackground(COLOR_DORADO); // Color amarillo de tus capturas
        btnRegistrarSalida.setForeground(Color.WHITE);
        btnRegistrarSalida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegistrarSalida.setBounds(550, 230, 200, 40);
        btnRegistrarSalida.setFocusable(false);
        add(btnRegistrarSalida);

        // Botón Volver (Estilo Gris/Oscuro Elegante)
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(Color.white);
        btnVolver.setForeground(COLOR_DORADO);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVolver.setBounds(760, 230, 200, 40);
        btnVolver.setFocusable(false);
        add(btnVolver);
    }

}
