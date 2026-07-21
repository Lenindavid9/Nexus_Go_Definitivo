/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Container;
import java.awt.GridLayout;
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
        setLayout(new GridLayout(4, 1, 10, 10));

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

        bInventario = new JButton(new ImageIcon("src/nexusgo/img/ventas.png"));
        bInventario.setBorderPainted(false);
        bInventario.setContentAreaFilled(false);
        bInventario.setFocusPainted(false);
        bInventario.setOpaque(false);

        


        add(bCasa);
        add(bInventario);
        add(misCitas);
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
