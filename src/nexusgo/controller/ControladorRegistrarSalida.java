/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaRegistrarSalida;

/**
 *
 * @author USUARIO
 */
public class ControladorRegistrarSalida implements ActionListener {

    private final VistaRegistrarSalida panelSalidaInsumo;
    private final JPanel contenedorCentral;
    private final JPanel panelInventarioPadre;
    private final ProductoDao productoDao;

    private final int idProductoSeleccionado;
    private final Runnable alVolverCallback; // Callback para refrescar la tabla del panel principal al volver

    public ControladorRegistrarSalida(VistaRegistrarSalida panelSalidaInsumo, JPanel contenedorCentral , JPanel panelInventarioPadre , int idProductoSeleccionado, Runnable alVolverCallback){
        this.panelSalidaInsumo = panelSalidaInsumo;
        this.contenedorCentral = contenedorCentral;
        this.panelInventarioPadre = panelInventarioPadre;
        this.idProductoSeleccionado = idProductoSeleccionado;
        this.alVolverCallback = alVolverCallback;
        this.productoDao = new ProductoDao();

        // Limpiar el campo de cantidad al iniciar
        this.panelSalidaInsumo.txtCantidadSalida.setText("");

        inicializarListeners();
    }

    private void inicializarListeners() {
        if (panelSalidaInsumo.btnRegistrarSalida != null) {
            panelSalidaInsumo.btnRegistrarSalida.addActionListener(this);
        }
        if (panelSalidaInsumo.btnVolver != null) {
            panelSalidaInsumo.btnVolver.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == panelSalidaInsumo.btnRegistrarSalida) {
            ejecutarRestaDeStock();
        } else if (src == panelSalidaInsumo.btnVolver) {
            volverAlPanelPrincipal();
        }
    }

    private void ejecutarRestaDeStock() {
        try {
            int cantidadARestar = Integer.parseInt(panelSalidaInsumo.txtCantidadSalida.getText().trim());
            if (cantidadARestar <= 0) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "La cantidad debe ser mayor a cero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (productoDao.registrarSalidaStock(idProductoSeleccionado, cantidadARestar)) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "¡Transacción exitosa! El stock se actualizó.");
                volverAlPanelPrincipal();
            } else {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "Error: Inventario insuficiente.", "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(panelSalidaInsumo, "Ingrese un número entero válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlPanelPrincipal() {
        if (contenedorCentral != null) {
            contenedorCentral.removeAll();
            contenedorCentral.setLayout(new BorderLayout());
            contenedorCentral.add(panelInventarioPadre, BorderLayout.CENTER);
            contenedorCentral.revalidate();
            contenedorCentral.repaint();

            if (alVolverCallback != null) {
                alVolverCallback.run(); // Refresca la tabla de productos
            }
        }
    }
}
