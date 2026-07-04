/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;

public class HistorialPagos extends JPanel {
    private JPanel menu, principal;
    private JButton btnInicio, btnHistorialP, btnHistorialS;
    private JLabel TituloPrincipal, subTitulo, txtSub;
    private JComboBox<String> comboMeses;
    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;
    private final Color COLOR_DORADO = new Color(223, 205, 141);

    public JPanel VistaHistorialP() {
        this.setLayout(new BorderLayout());

        // Panel lateral de menú
        menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15));
        menu.setPreferredSize(new Dimension(150, 0));

        btnInicio = new JButton(new ImageIcon("inicio.png"));
        btnHistorialP = new JButton(new ImageIcon("HistorialPagos.png"));
        btnHistorialS = new JButton(new ImageIcon("HistorialServicios.png"));

        BotonMenu(btnInicio);
        BotonMenu(btnHistorialP);
        BotonMenu(btnHistorialS);

        menu.add(Box.createVerticalGlue());
        menu.add(btnInicio);
        menu.add(Box.createVerticalStrut(30));
        menu.add(btnHistorialP);
        menu.add(Box.createVerticalStrut(30));
        menu.add(btnHistorialS);
        menu.add(Box.createVerticalGlue());

        // Panel principal con fondo
        principal = new JPanel() {
            private Image fondo = new ImageIcon("fondo.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Títulos
        TituloPrincipal = new JLabel("Visualiza tus transacciones recientes y descarga los soportes de tus pagos");
        TituloPrincipal.setForeground(Color.BLACK);
        TituloPrincipal.setFont(new Font("SansSerif", Font.BOLD, 32));
        TituloPrincipal.setAlignmentX(CENTER_ALIGNMENT);

        subTitulo = new JLabel("Historial de Pagos");
        subTitulo.setForeground(Color.BLACK);
        subTitulo.setFont(new Font("SansSerif", Font.BOLD, 32));
        subTitulo.setAlignmentX(CENTER_ALIGNMENT);

        txtSub = new JLabel("Listado cronológico de transacciones (más reciente a más antiguo).");
        txtSub.setForeground(Color.BLACK);
        txtSub.setFont(new Font("SansSerif", Font.PLAIN, 25));
        txtSub.setAlignmentX(CENTER_ALIGNMENT);

        // JComboBox de meses
        UIManager.put("ComboBox.selectionBackground", new Color(240, 225, 180));
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        comboMeses = new JComboBox<>(meses);
        comboMeses.setBackground(COLOR_DORADO);
        comboMeses.setFont(new Font("SansSerif", Font.BOLD, 18));
        comboMeses.setMaximumSize(new Dimension(200, 40));
        comboMeses.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tabla vacía con títulos
        String[] columnas = {"Fecha", "Servicio / Producto", "Monto Total", "Acción"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPagos = new JTable(modeloTabla);

        // 🔑 Encabezados fijos y no movibles
        tablaPagos.getTableHeader().setReorderingAllowed(false);

        // Render y editor para el botón
        tablaPagos.getColumn("Acción").setCellRenderer(new ButtonRenderer());
        tablaPagos.getColumn("Acción").setCellEditor(new ButtonEditor());

        // ScrollPane para que los títulos queden fijos
        JScrollPane scrollTabla = new JScrollPane(tablaPagos);
        scrollTabla.setMaximumSize(new Dimension(800, 300));
        scrollTabla.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agregar componentes al panel principal
        principal.add(TituloPrincipal);
        principal.add(Box.createVerticalStrut(20));
        principal.add(subTitulo);
        principal.add(txtSub);
        principal.add(Box.createVerticalStrut(10));
        principal.add(comboMeses);
        principal.add(Box.createVerticalStrut(40));
        principal.add(scrollTabla);

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

    // Renderer para mostrar botón
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Descargar PDF");
            setBackground(COLOR_DORADO);
            setForeground(Color.BLACK);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor para que el botón sea clickeable
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;

        public ButtonEditor() {
            button = new JButton("Descargar PDF");
            button.setBackground(COLOR_DORADO);
            button.setForeground(Color.BLACK);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Descargar PDF";
        }
    }
}
