/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author INGRID
 */
public class ReportesFinancieros extends JPanel {

    private JPanel principal, panelGrafica;
    private JButton btnProcesar;
    private JComboBox<String> comboMes, comboAnio;
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;

    private final Color COLOR_DORADO = new Color(223, 205, 141);

    public JPanel VistaRF() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.white);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtros.setBackground(Color.white);

        LocalDate fechaActual = LocalDate.now();
        int anioActual = fechaActual.getYear();
        int mesActual = fechaActual.getMonthValue();

        comboAnio = new JComboBox<>();
        for (int i = anioActual - 10; i <= anioActual + 5; i++) {
            comboAnio.addItem(String.valueOf(i));
        }

        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril",
            "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        comboMes = new JComboBox<>();
        for (String mes : meses) {
            comboMes.addItem(mes);
        }

        btnProcesar = new JButton("Procesar Reporte");
        // Después de filtros.add(btnProcesar);
        btnProcesar.addActionListener(e -> {
            String mesSeleccionado = (String) comboMes.getSelectedItem();
            String anioSeleccionado = (String) comboAnio.getSelectedItem();

            // Ejemplo de datos según mes/año
            if (mesSeleccionado.equals("Enero") && anioSeleccionado.equals("2026")) {
                modeloTabla.setValueAt(1500, 0, 0);
                modeloTabla.setValueAt(300, 0, 1);
                modeloTabla.setValueAt(1200, 0, 2);
                modeloTabla.setValueAt("Tratamiento Capilar", 0, 3);
            } else {
                modeloTabla.setValueAt(800, 0, 0);
                modeloTabla.setValueAt(100, 0, 1);
                modeloTabla.setValueAt(700, 0, 2);
                modeloTabla.setValueAt("Corte Básico", 0, 3);
            }

         
    actualizarGrafica();
        });

        filtros.add(new JLabel("Mes:"));
        filtros.add(comboMes);
        filtros.add(new JLabel("Año:"));
        filtros.add(comboAnio);
        filtros.add(btnProcesar);

        principal.add(filtros);

        String[] columnas = {
            "Suma Servicios/Productos",
            "Suma Promociones/Descuentos",
            "Resta Descuentos al Total",
            "Servicio del Mes"
        };

        modeloTabla = new DefaultTableModel(columnas, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReporte = new JTable(modeloTabla);

        modeloTabla.setValueAt(1000, 0, 0); // Servicios/Productos
        modeloTabla.setValueAt(200, 0, 1);  // Promociones/Descuentos
        modeloTabla.setValueAt(800, 0, 2);  // Total Neto
        modeloTabla.setValueAt("Corte de Cabello", 0, 3); // Servicio del Mes

        tablaReporte.getTableHeader().setReorderingAllowed(false);
        tablaReporte.getTableHeader().setResizingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tablaReporte,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollTabla.setPreferredSize(new Dimension(600, 80));
        principal.add(Box.createVerticalStrut(20));
        principal.add(scrollTabla);
        principal.add(Box.createVerticalStrut(20));

        panelGrafica = new JPanel();
        panelGrafica.setLayout(new BoxLayout(panelGrafica, BoxLayout.Y_AXIS));
        panelGrafica.setBackground(Color.white);
        principal.add(panelGrafica);

        actualizarGrafica();
        this.add(principal, BorderLayout.CENTER);

        return this;

    }

    public JButton getBtnProcesar() {
        return btnProcesar;
    }

    public JComboBox<String> getComboMes() {
        return comboMes;
    }

    public JComboBox<String> getComboAnio() {
        return comboAnio;
    }

    public JTable getTablaReporte() {
        return tablaReporte;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    private ChartPanel crearGrafica() {
        Object val0 = modeloTabla.getValueAt(0, 0);
        Object val1 = modeloTabla.getValueAt(0, 1);
        Object val2 = modeloTabla.getValueAt(0, 2);

        double servicios = val0 != null ? Double.parseDouble(val0.toString()) : 0;
        double promociones = val1 != null ? Double.parseDouble(val1.toString()) : 0;
        double total = val2 != null ? Double.parseDouble(val2.toString()) : 0;

        if (servicios == 0 && promociones == 0 && total == 0) {
            return null;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(servicios, "Ingresos", "Servicios/Productos");
        dataset.addValue(promociones, "Ingresos", "Promociones/Descuentos");
        dataset.addValue(total, "Ingresos", "Total Neto");

        JFreeChart chart = ChartFactory.createBarChart(
                "Reporte Financiero", "Categoría", "Valor", dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        return chartPanel;
    }

    public void actualizarGrafica() {
        panelGrafica.removeAll();

        ChartPanel nuevaGrafica = crearGrafica();
        if (nuevaGrafica != null) {
            panelGrafica.add(Box.createVerticalStrut(20));
            panelGrafica.add(nuevaGrafica);
        }

        panelGrafica.revalidate();
        panelGrafica.repaint();
    }

}
