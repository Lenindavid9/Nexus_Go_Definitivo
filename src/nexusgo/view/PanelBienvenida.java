/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public class PanelBienvenida extends JPanel {
    
    private JLabel mensaje, titulo, fondo;

    public PanelBienvenida(String nombre, String rol) {
        
        // Fondo con FlowLayout
        this.fondo = new JLabel(new ImageIcon ("C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\nexusGo/fondito.jpg"));
        this.fondo.setOpaque(true);
        this.fondo.setLayout(new FlowLayout());
        
        
        // Usamos null layout para posicionar los elementos exactamente donde queramos
        setLayout(null);
        setBackground(new Color(245, 245, 245)); // Un gris muy claro de fondo limpio

        // Configuración del Título
        titulo = new JLabel("Hola " + nombre + " (" + rol + ") a Nexus GO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBounds(50, 50, 600, 40);

        // Configuración del Mensaje
        mensaje = new JLabel("Espero que te encuentres súper bien el día de hoy.");
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mensaje.setBounds(50, 100, 600, 30);

        // Agregamos directamente al JPanel
        add(titulo);
        add(mensaje);
        add(fondo);
    }

    

}
