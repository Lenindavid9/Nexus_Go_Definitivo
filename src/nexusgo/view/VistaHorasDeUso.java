/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author USUARIO
 */
public class VistaHorasDeUso extends JFrame {
    
    public JTextField tHoras;
    public JButton btnGuardar;
    public JButton btnCancelar;

    public VistaHorasDeUso() {
        setTitle("Nexus GO - Horas de Uso");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10)); // Organiza todo en una cuadrícula simple de 3 filas y 2 columnas

        // Fila 1: Texto de indicación y caja para escribir las horas
        add(new JLabel("  Horas de Uso:"));
        tHoras = new JTextField();
       
       

        // Fila 2: Espacio en blanco para que no se pegue todo (opcional)
        add(new JLabel(""));
        add(new JLabel(""));

        // Fila 3: Los dos botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);
    }
    
    
    
}
