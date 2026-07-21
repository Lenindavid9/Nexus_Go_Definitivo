/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.HerramientaDao;
import nexusgo.model.Herramientas;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.PanelInventarioProductosPeluquero;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaPrincipalPeluquero;

import nexusgo.view.VistaRegistrarSalida;


/**
 *
 * @author HOME
 */
public class ControladorInventarioPeluquero implements ActionListener {
   private final PanelInventarioProductosPeluquero panelInventario;
    private final VistaPrincipalPeluquero vistaPrincipal;

    private VistaRegistrarSalida panelSalidaInsumo;
    private final ProductoDao productoDao = new ProductoDao();
    private int idSeleccionado = -1;

    public ControladorInventarioPeluquero(PanelInventarioProductosPeluquero panelInventario, VistaPrincipalPeluquero vistaPrincipal) {
        this.panelInventario = panelInventario;
        this.vistaPrincipal = vistaPrincipal;

        try {
            this.panelSalidaInsumo = new VistaRegistrarSalida();

            inicializarListeners();
            cargarTablaInventario();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al inicializar el módulo de inventario: " + e.getMessage(),
                    "Error de Arranque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarListeners() {
        try {
            // 1. Escuchar clics en la única tabla del panel mediante el getter getTablaInventario()
            if (this.panelInventario.getTablaInventario() != null) {
                this.panelInventario.getTablaInventario().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int fila = panelInventario.getTablaInventario().getSelectedRow();
                        if (fila >= 0) {
                            lanzarMenuSalidaInsumo(fila);
                        }
                    }
                });
            }

            // 2. Listener para los botones de la vista de Registrar Salida
            if (this.panelSalidaInsumo != null) {
                if (this.panelSalidaInsumo.btnRegistrarSalida != null) {
                    this.panelSalidaInsumo.btnRegistrarSalida.addActionListener(this);
                }
                if (this.panelSalidaInsumo.btnVolver != null) {
                    this.panelSalidaInsumo.btnVolver.addActionListener(this);
                }
            }

            // 3. Listener para el botón volver de la vista principal del inventario
            if (this.panelInventario.getBtnVolver() != null) {
                this.panelInventario.getBtnVolver().addActionListener(this);
            }

        } catch (NullPointerException npe) {
            System.err.println("Error al enlazar listeners en ControladorInventarioPeluquero: " + npe.getMessage());
        }
    }

    private void lanzarMenuSalidaInsumo(int fila) {
        String[] opciones = {"Registrar Salida", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(
                panelInventario,
                "¿Desea registrar la salida de este insumo?",
                "NEXUS - Insumos Peluquería",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opciones, 
                opciones[0]
        );

        if (seleccion == 0) { // Registrar Salida
            idSeleccionado = (int) panelInventario.getTablaInventario().getValueAt(fila, 0);
            panelSalidaInsumo.txtCantidadSalida.setText("");
            cambiarPanelCentral(this.panelSalidaInsumo);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Confirmar salida de insumo
            if (panelSalidaInsumo != null && e.getSource() == panelSalidaInsumo.btnRegistrarSalida) {
                ejecutarRestaDeStock();
            }

            // Volver de la subvista de registro de salida al inventario principal
            if (panelSalidaInsumo != null && e.getSource() == panelSalidaInsumo.btnVolver) {
                cambiarPanelCentral(this.panelInventario);
                cargarTablaInventario();
            }

            // Botón volver del panel de inventario
            if (e.getSource() == panelInventario.getBtnVolver()) {
                if (vistaPrincipal != null) {
                    vistaPrincipal.restaurarComponentesPrincipales();
                }
            }

        } catch (Exception ex) {
            System.err.println("Error en evento de inventario peluquero: " + ex.getMessage());
        }
    }

    private void ejecutarRestaDeStock() {
        try {
            String textoCantidad = panelSalidaInsumo.txtCantidadSalida.getText().trim();

            if (textoCantidad.isEmpty()) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "Por favor ingrese la cantidad a retirar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidadARestar = Integer.parseInt(textoCantidad);

            if (cantidadARestar <= 0) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "La cantidad debe ser mayor a cero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (productoDao.registrarSalidaStock(idSeleccionado, cantidadARestar)) {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "¡Salida de insumo registrada con éxito!");
                cambiarPanelCentral(this.panelInventario);
                cargarTablaInventario();
            } else {
                JOptionPane.showMessageDialog(panelSalidaInsumo, "Error: Stock insuficiente en el inventario.", "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(panelSalidaInsumo, "Ingrese un número entero válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarTablaInventario() {
        try {
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre Producto", "Stock Actual"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            List<Producto> lista = productoDao.listar();
            if (lista != null) {
                for (Producto p : lista) {
                    modelo.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getStockActual()});
                }
            }

            // Inyectamos el modelo utilizando el método personalizado setDatosInventario(...) de tu vista
            panelInventario.setDatosInventario(modelo);

        } catch (Exception e) {
            System.err.println("Error al cargar la tabla de inventario para peluquería: " + e.getMessage());
        }
    }

    private void cambiarPanelCentral(JPanel nuevoPanel) {
        try {
            if (vistaPrincipal != null) {
                JPanel contenedorDinamico = vistaPrincipal.getContenidoCentralDinamico();
                if (contenedorDinamico != null) {
                    contenedorDinamico.removeAll();
                    contenedorDinamico.setLayout(new BorderLayout());
                    contenedorDinamico.add(nuevoPanel, BorderLayout.CENTER);
                    contenedorDinamico.revalidate();
                    contenedorDinamico.repaint();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al alternar paneles en VistaPrincipalPeluquero: " + e.getMessage());
        }
    }
}
