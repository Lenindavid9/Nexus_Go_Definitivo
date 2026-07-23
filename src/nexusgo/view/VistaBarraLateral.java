/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author USUARIO
 */
public class VistaBarraLateral extends JPanel {

   public Container contenedor;
    public JPanel barraLateral, barraSuperior;
    public JButton bCasa, bInventario, misCitas;

    public VistaBarraLateral() {
        // 1. Definir dimensiones fijas para la barra y fondo blanco visible
        setPreferredSize(new Dimension(80, 600));
        setMinimumSize(new Dimension(80, 600));
        setBackground(Color.WHITE);
        setOpaque(true); // Hace visible el fondo blanco
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230))); // Sutil borde gris a la derecha

        // Layout con espacio entre botones
        setLayout(new GridLayout(8, 1, 0, 15)); // Aumentamos filas para que los botones no se deformen a lo alto

        // 2. Inicializar Botones
        bCasa = new JButton(new ImageIcon("src/nexusgo/img/inicio.png"));

        bCasa.setBorderPainted(false);   
        bCasa.setContentAreaFilled(false);
        bCasa.setFocusPainted(false);    
        bCasa.setOpaque(false);         
        
        misCitas = new JButton(new ImageIcon("src/nexusgo/img/inventario.png"));
        misCitas.setBorderPainted(false);
        misCitas.setContentAreaFilled(false);
        misCitas.setFocusPainted(false);
        misCitas.setOpaque(false);
        
        

        estilizarBoton(bCasa, "Inicio");


        bInventario = new JButton(new ImageIcon("src/nexusgo/img/ventas.png"));
        estilizarBoton(bInventario, "Inventario / Ventas");

        misCitas = new JButton(new ImageIcon("src/nexusgo/img/inventario.png"));
        estilizarBoton(misCitas, "Mis Citas");

        // 3. Ensamblado
        add(bCasa);
        add(bInventario);
        add(misCitas);
    }

    /**
     * Aplica el estilo transparente con cursor interactivo y texto alternativo por si falta la imagen.
     */
    private void estilizarBoton(JButton btn, String tooltip) {
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
    }

    // --- GETTERS PARA CONECTAR CON EL CONTROLADOR ---
    public JButton getBtnInicio() {
        return bCasa;
    }

    public JButton getBtnVentas() {
        return bInventario;
    }

    public JButton getBtnInventario() {
        return misCitas;
    }
    
}
