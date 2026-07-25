/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
    
    private JPanel principal;
    private JButton btnInicio, btnCerrar, btnHistorialMH, btnProcesar;
    private JComboBox<String> comboMes, comboAnio;
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;

    private final Color COLOR_CAFE_OSCURO = new Color(62, 58, 46);
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    public JPanel VistaRF() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        
        btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        

        btnInicio = new JButton("Inicio");
        btnHistorialMH = new JButton("Historial Mantenimiento Herramientas");

        BotonMenu(btnInicio);
        BotonMenu(btnHistorialMH);

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
        for (int i = anioActual; i <= anioActual + 5; i++) {
            comboAnio.addItem(String.valueOf(i));
        }

        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril",
            "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        comboMes = new JComboBox<>();
        for (int i = mesActual - 1; i < meses.length; i++) {
            comboMes.addItem(meses[i]);
        }

        btnProcesar = new JButton("Procesar Reporte");

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
        principal.add(crearGrafica());
        this.add(principal, BorderLayout.CENTER);

        return this;
    }

    private void BotonMenu(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 16));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setAlignmentX(LEFT_ALIGNMENT);
    }
    
    public JButton getBtnInicio() {
        return btnInicio;
    }

    public JButton getBtnCerrar() {
        return btnCerrar;
    }

    public JButton getBtnHistorialMH() {
        return btnHistorialMH;
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
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    Object val0 = modeloTabla.getValueAt(0, 0);
    Object val1 = modeloTabla.getValueAt(0, 1);
    Object val2 = modeloTabla.getValueAt(0, 2);

    double servicios = val0 != null ? Double.parseDouble(val0.toString()) : 0;
    double promociones = val1 != null ? Double.parseDouble(val1.toString()) : 0;
    double total = val2 != null ? Double.parseDouble(val2.toString()) : 0;

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
  
  
}