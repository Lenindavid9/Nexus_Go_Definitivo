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
   

    public VistaRegistrarSalida() {
        this.setBackground(Color.WHITE);
        this.setLayout(null); // Usamos diseño libre/absoluto para posicionar consetBounds

        // Título Principal
        lblTitulo = new JLabel("REGISTRAR SALIDA DE INSUMO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBounds(50, 30, 400, 30);
        add(lblTitulo);

        // Etiqueta de instrucción
        lblInstruccion = new JLabel("Cantidad a retirar:");
        lblInstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInstruccion.setBounds(80, 100, 150, 25);
        add(lblInstruccion);

        // Caja de texto para la cantidad
        txtCantidadSalida = new JTextField();
        txtCantidadSalida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCantidadSalida.setBounds(220, 100, 150, 30);
        add(txtCantidadSalida);

        // Botón Registrar Salida (Estilo Amarillo/Dorado Nexus)
        btnRegistrarSalida = new JButton("Registrar Salida");
        btnRegistrarSalida.setBackground(new Color(242, 194, 48)); // Color amarillo de tus capturas
        btnRegistrarSalida.setForeground(Color.BLACK);
        btnRegistrarSalida.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrarSalida.setBounds(90, 170, 140, 35);
        btnRegistrarSalida.setFocusable(false);
        add(btnRegistrarSalida);

        // Botón Volver (Estilo Gris/Oscuro Elegante)
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(70, 70, 70));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setBounds(250, 170, 120, 35);
        btnVolver.setFocusable(false);
        add(btnVolver);
    }

}
