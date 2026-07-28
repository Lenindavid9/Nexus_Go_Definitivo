/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import nexusgo.model.Producto;
import nexusgo.model.Servicios;

/**
 *
 * @author USUARIO
 */
public class VistaAgregarPromocionCombo extends JPanel {

    // Componentes del Formulario
    private JTextField txtNombreCombo;
    private JTextField txtDescripcionPromocion;
    private JTextField txtPrecioCombo;

    // Componentes de Fecha (JCalendar)
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFin;

    // Listas para Selección Múltiple
    private JList<Producto> listaProductos;
    private DefaultListModel<Producto> modeloListaProductos;

    private JList<Servicios> listaServicios;
    private DefaultListModel<Servicios> modeloListaServicios;

    // Botones e Indicadores
    public JButton btnCargarImagen;
    public JButton btnGuardar;
    public JLabel lblNombreImagen;

    public VistaAgregarPromocionCombo() {
        setLayout(null);
        setOpaque(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        Font fontTitulo = new Font("SansSerif", Font.BOLD, 22);
        Font fontLabel = new Font("SansSerif", Font.BOLD, 16);
        Font fontTexto = new Font("SansSerif", Font.PLAIN, 13);

        // --- ENCABEZADO Y NAVEGACIÓN ---
        JLabel lblTitulo = new JLabel("Registrar Promoción Combo / Kit");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setBounds(40, 20, 400, 30);
        add(lblTitulo);

        // --- NOMBRE DEL COMBO ---
        JLabel lblNombre = new JLabel("Nombre del Kit / Combo");
        lblNombre.setFont(fontLabel);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setBounds(40, 70, 300, 20);
        add(lblNombre);

        txtNombreCombo = new JTextField();
        txtNombreCombo.setBounds(40, 95, 690, 30);
        txtNombreCombo.setFont(fontTexto);
        add(txtNombreCombo);

        // --- SELECCIÓN DE PRODUCTOS Y SERVICIOS ---
        JLabel lblProductos = new JLabel("Seleccione Productos");
        lblProductos.setFont(fontLabel);
        lblProductos.setForeground(Color.WHITE);
        lblProductos.setBounds(40, 140, 320, 20);
        add(lblProductos);

        modeloListaProductos = new DefaultListModel<>();
        listaProductos = new JList<>(modeloListaProductos);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollProductos = new JScrollPane(listaProductos);
        scrollProductos.setBounds(40, 165, 330, 120);
        add(scrollProductos);

        JLabel lblServicios = new JLabel("Seleccione Servicios");
        lblServicios.setFont(fontLabel);
        lblServicios.setForeground(Color.WHITE);
        lblServicios.setBounds(400, 140, 330, 20);
        add(lblServicios);

        modeloListaServicios = new DefaultListModel<>();
        listaServicios = new JList<>(modeloListaServicios);
        listaServicios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollServicios = new JScrollPane(listaServicios);
        scrollServicios.setBounds(400, 165, 330, 120);
        add(scrollServicios);

        // --- DESCRIPCIÓN ---
        JLabel lblDesc = new JLabel("Descripción de la promoción");
        lblDesc.setFont(fontLabel);
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setBounds(40, 300, 300, 20);
        add(lblDesc);

        txtDescripcionPromocion = new JTextField();
        txtDescripcionPromocion.setBounds(40, 325, 690, 30);
        txtDescripcionPromocion.setFont(fontTexto);
        add(txtDescripcionPromocion);

        // --- COMPONENTES JDATECHOOSER (FECHAS) ---
        JLabel lblFechaInicio = new JLabel("Fecha de inicio");
        lblFechaInicio.setFont(fontLabel);
        lblFechaInicio.setForeground(Color.WHITE);
        lblFechaInicio.setBounds(40, 370, 200, 20);
        add(lblFechaInicio);

        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setBounds(40, 395, 330, 30);
        dateChooserInicio.setDateFormatString("dd.MM.yyyy");
        add(dateChooserInicio);

        JLabel lblFechaFin = new JLabel("Fecha de finalización");
        lblFechaFin.setFont(fontLabel);
        lblFechaFin.setForeground(Color.WHITE);
        lblFechaFin.setBounds(400, 370, 200, 20);
        add(lblFechaFin);

        dateChooserFin = new JDateChooser();
        dateChooserFin.setBounds(400, 395, 330, 30);
        dateChooserFin.setDateFormatString("dd.MM.yyyy");
        add(dateChooserFin);

        // RESTICCION: Bloquea la selección de fechas anteriores al día actual
        Date hoy = new Date();
        dateChooserInicio.setMinSelectableDate(hoy);
        dateChooserFin.setMinSelectableDate(hoy);

        // --- PRECIO Y CARGA DE IMAGEN ---
        JLabel lblPrecio = new JLabel("Precio Especial del Combo / Kit");
        lblPrecio.setFont(fontLabel);
        lblPrecio.setForeground(Color.WHITE);
        lblPrecio.setBounds(40, 440, 300, 20);
        add(lblPrecio);

        txtPrecioCombo = new JTextField();
        txtPrecioCombo.setBounds(40, 465, 690, 30);
        txtPrecioCombo.setFont(fontTexto);
        add(txtPrecioCombo);

        btnCargarImagen = new JButton("Imagen del Combo");
        btnCargarImagen.setBounds(40, 510, 160, 35);
        btnCargarImagen.setForeground(Color.WHITE);
        btnCargarImagen.setBackground(Color.WHITE);
        add(btnCargarImagen);

        lblNombreImagen = new JLabel("No se ha seleccionado imagen");
        lblNombreImagen.setFont(fontTexto);
        lblNombreImagen.setForeground(Color.WHITE);
        lblNombreImagen.setBounds(210, 515, 300, 25);
        add(lblNombreImagen);

        // --- BOTÓN DE GUARDADO ---
        btnGuardar = new JButton("Guardar Promo Combo");
        btnGuardar.setBounds(40, 565, 230, 40);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBackground(new Color(255, 193, 7)); // Color corporativo NexusGO
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        add(btnGuardar);
    }

    // --- GETTERS ---
    public JTextField getTxtNombreCombo() {
        return txtNombreCombo;
    }

    public JTextField getTxtDescripcionPromocion() {
        return txtDescripcionPromocion;
    }

    public JTextField getTxtPrecioCombo() {
        return txtPrecioCombo;
    }

    public JDateChooser getDateChooserInicio() {
        return dateChooserInicio;
    }

    public JDateChooser getDateChooserFin() {
        return dateChooserFin;
    }

    public JList<Producto> getListaProductos() {
        return listaProductos;
    }

    public DefaultListModel<Producto> getModeloListaProductos() {
        return modeloListaProductos;
    }

    public JList<Servicios> getListaServicios() {
        return listaServicios;
    }

    public DefaultListModel<Servicios> getModeloListaServicios() {
        return modeloListaServicios;
    }

    public JLabel getLblNombreImagen() {
        return lblNombreImagen;
    }
}
