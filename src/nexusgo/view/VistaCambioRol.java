package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class VistaCambioRol extends JPanel {

    private DefaultTableModel modelo;
    private JTable tabla;
    private JScrollPane miscroll;
    private Image imagenFondo;

    // Roles disponibles para la celda tipo ComboBox
   private final String[] tiposDeRol = {
        "Cliente", 
        "Supervisor", 
        "Administrador_del_software", 
        "Administrador_de_la_peluqueria", 
        "Peluquero", 
        "Operario"
    };

    public VistaCambioRol() {
        // Cargar imagen de fondo opcional
        try {
            ImageIcon icon = new ImageIcon("src/nexusgo/img/marmol_mejorado.jpg");
            this.imagenFondo = icon.getImage();
        } catch (Exception e) {
            this.imagenFondo = null;
        }

        setLayout(new BorderLayout(20, 20));

        // 1. Definir Modelo de Tabla (Solo la columna ROL índice 3 es editable)
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        modelo.addColumn("NUMERO DE IDENTIDAD");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("ROL");
        modelo.addColumn("CORREO ELECTRONICO");

        // 2. Configurar la JTable
        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setSelectionBackground(Color.decode("#EFB810"));

        // Asignar el JComboBox editable a la columna ROL (Columna 3)
        JComboBox<String> comboRol = new JComboBox<>(tiposDeRol);
        tabla.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboRol));

        // Estilo de los encabezados
        tabla.getTableHeader().setBackground(Color.WHITE);
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        // 3. ScrollPane contenedor
        miscroll = new JScrollPane(tabla);
        miscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelCentro.setOpaque(false);
        panelCentro.add(miscroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Getters para el controlador
    public DefaultTableModel getModelo() {
        return modelo;
    }

    public JTable getTabla() {
        return tabla;
    }
}
