package nexusgo.view;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class VistaCambioRol extends JFrame {

    private Container contenedor;
    public DefaultTableModel modelo;
    private TitledBorder titulo;
    private JPanel datos;
    private JScrollPane miscroll;
    public JTable tabla;
    private GridLayout grid;
    public JButton btnCerrarSesion;
    public JComboBox unTipoRol;
    private JLabel fondo;
    public VistaBarraLateral miBarra;

    // Lista de roles
    private String tipoRol[] = {"Cliente", "Peluquero", "Admin. Sotfware", "Admin. Peluqueria", "Supervisor", "Operario",};

    public VistaCambioRol() {

        super("Administración de Usuarios");
        contenedor = getContentPane();

        // Se utilizará BorderLayout para distribuir los paneles
        contenedor.setLayout(new BorderLayout(20, 20));

        this.fondo = new JLabel(new ImageIcon ("src/nexusgo/img/marmol_mejorado.jpg"));
        this.fondo.setLayout(new BorderLayout(20,20));
        this.setContentPane(fondo);
        
        datos = new JPanel();
        datos.setBackground(Color.WHITE);
        datos.setLayout(new BorderLayout(10, 10));

        /*Se crea el modelo que almacenará toda la información de la tabla.
        El DefaultTableModel es el encargado de guardar los datos,
        mientras que el JTable solamente se encarga de mostrarlos.*/
        modelo = new DefaultTableModel();
        
        //se agg las columnas
        modelo.addColumn("NUMERO DE IDENTIDAD");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("ROL");
        modelo.addColumn("CORREO ELECTRONICO");

        /*La tabla será la encargada de mostrar la información 
        contenida dentro del DefaultTableModel.*/
        tabla = new JTable(modelo);

        // ComboBox que permitirá cambiar el rol
        JComboBox<String> comboRol = new JComboBox<>(tipoRol);

        // Se agrega el ComboBox únicamente a la columna "ROL"
        tabla.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboRol));

        // Define la altura inicial de cada fila de la tabla en 45 píxeles.
        tabla.setRowHeight(45);

        /*Hace que la tabla ocupe toda la altura disponible dentro del
        JScrollPane, incluso cuando existan pocas filas registradas.
        Esto evita que quede un espacio vacío debajo de la información.*/
        //tabla.setFillsViewportHeight(true);

        //Color de cuando tocas un usuario
        tabla.setSelectionBackground(Color.decode("#EFB810"));

        // Personalización del encabezado
        tabla.getTableHeader().setBackground(Color.WHITE);
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        /* Esto añade barras de desplazamiento cuando
        los datos supera el espacio dee la ventana*/
        miscroll = new JScrollPane(tabla);
        
        /*barra de desplazamiento vertical,
        esto permite recorrer todos los registros cuando la cantidad 
        de filas sea superior al espacio disponible.*/
        miscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /* Se crea un nuevo panel utilizando BorderLayout.
        Este panel actuará como el contenedor principal donde se
        mostrará la información de la ventana,
        en este caso la tabla de usuarios.*/
        JPanel panelCentro = new JPanel(new BorderLayout());

        /* Se agrega un margen interno de 40 píxeles en los cuatro lados
        del panel. Esto evita que la tabla quede pegada a los bordes
        de la ventana*/
        panelCentro.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //conste que lo puse negro pa diferenciar cual es el fondo, luego se acomoda
        panelCentro.setBackground(Color.BLACK);

        /*Se agrega el JScrollPane al panel central.
        Como el JScrollPane contiene la tabla, al agregar este
        también se estará mostrando la tabla junto con
        sus barras de desplazamiento*/
        panelCentro.add(miscroll);

        // Se agrega el panel central a la posición CENTER del BorderLayout.
        fondo.add(panelCentro, BorderLayout.CENTER);
        
        miBarra = new VistaBarraLateral();
        // Se agrega la barra lateral en la posicion OESTE del BorderLayout.
        fondo.add(miBarra,BorderLayout.WEST);
        miBarra.add(new JButton("Reportes"));
        
        // Se agrega el panel central a la posición CENTER del BorderLayout.
        fondo.add(panelCentro, BorderLayout.CENTER);
    }
}