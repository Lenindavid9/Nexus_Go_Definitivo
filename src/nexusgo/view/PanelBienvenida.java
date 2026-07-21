/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public class PanelBienvenida extends JPanel {
    
    private JLabel mensaje, titulo;

    public PanelBienvenida(String nombre, String rol) {
        // Usamos null layout para posicionar los elementos exactamente donde queramos
        setLayout(null);
        setOpaque(false);
        
//        class FondoPanel extends JPanel {
//    private Image imagen;
//
//    public FondoPanel(String rutaImagen) {
//        this.imagen = new ImageIcon(rutaImagen).getImage();
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
//    }
//}
//
//        
//        private fondo 
         // Un gris muy claro de fondo limpio

        // Configuración del Título
        titulo = new JLabel("Hola " + nombre + " (" + rol + ") a Nexus GO");
        titulo.setForeground(Color.white);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setBounds(400, 200, 600, 40);

        // Configuración del Mensaje
        mensaje = new JLabel("Espero que te encuentres súper bien el día de hoy.");
        mensaje.setForeground(Color.white);
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        mensaje.setBounds(400, 250, 600, 30);

        // Agregamos directamente al JPanel
        add(titulo);
        add(mensaje);
    }

}






