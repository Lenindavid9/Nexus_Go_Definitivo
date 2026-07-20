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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author INGRID
 */
public class ReportesFinancieros extends JPanel {
    
    private JPanel titulo, menu, principal;
    private JButton btnInicio, btnCerrar, btnHistorialMH, btnProcesar;
    private JLabel logoyNombre;
    private JPanel OpcTitulo;
    private JComboBox<String> comboMes, comboAnio;
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;

    private final Color COLOR_CAFE_OSCURO = new Color(62, 58, 46);
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    public JPanel VistaRF() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        titulo = new JPanel(new BorderLayout());
        titulo.setBackground(COLOR_CAFE_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        Icon iconLogo = new ImageIcon("logo.png"); 
        logoyNombre = new JLabel("Reportes Financieros  - N E X U S", iconLogo, SwingConstants.LEFT);
        logoyNombre.setForeground(Color.WHITE);
        logoyNombre.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.add(logoyNombre, BorderLayout.WEST);

        OpcTitulo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        OpcTitulo.setOpaque(false);
        
        btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setBorderPainted(false);
        
        OpcTitulo.add(btnCerrar);
        titulo.add(OpcTitulo, BorderLayout.EAST);

        menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(COLOR_DORADO);
        menu.setPreferredSize(new Dimension(250, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15)); 

        btnInicio = new JButton("Inicio");
        btnHistorialMH = new JButton("Historial Mantenimiento Herramientas");

        BotonMenu(btnInicio);
        BotonMenu(btnHistorialMH);

        menu.add(btnInicio);
        menu.add(Box.createVerticalStrut(20));
        menu.add(btnHistorialMH);

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

        tablaReporte.getTableHeader().setReorderingAllowed(false);
        tablaReporte.getTableHeader().setResizingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tablaReporte,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollTabla.setPreferredSize(new Dimension(600, 80));
        principal.add(Box.createVerticalStrut(20));
        principal.add(scrollTabla);
        
        this.add(titulo, BorderLayout.NORTH);
        this.add(menu, BorderLayout.WEST);
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
}