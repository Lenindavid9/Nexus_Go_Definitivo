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
        setLayout(new GridLayout(7, 1, 10, 10));

        bCasa = new JButton("Inicio");
        bInventario = new JButton("Ventas");
        misCitas = new JButton("Inventario");

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
