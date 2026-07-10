/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao; // Tu manejador de base de datos
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaReservarCitas;

/**
 *
 * @author USUARIO
 */
public class ControladorPrincipalCliente implements ActionListener {

    private VistaPrincipalCliente vista;
    private ProductoDao productoDAO;

    /**
     * Constructor que enlaza la vista y activa la escucha de todos sus
     * componentes.
     *
     * @param
     */
    public ControladorPrincipalCliente(VistaPrincipalCliente vista) {
        this.vista = vista;
        this.productoDAO = new ProductoDao(); // Conexión al gestor de base de datos de Laragon

        // 1. Escuchar el botón base "Inicio" (bCasa) de tu componente modular VistaBarraLateral
        this.vista.sidebar.bCasa.addActionListener(this);

        // 2. Escuchar los dos botones agregados dinámicamente a la barra lateral del cliente
        this.vista.historial.addActionListener(this);
        this.vista.CitasVigentes.addActionListener(this);

        // 3. Escuchar el botón de cerrar sesión ubicado en el panel central dinámico
        this.vista.btnCerrarSesion.addActionListener(this);

        // Al iniciar el controlador, cargamos automáticamente la cuadrícula de productos
        cargarCatalogo();
    }

    /**
     * Extrae los productos desde Laragon a través del DAO y refresca las
     * tarjetas de la interfaz.
     */
    private void cargarCatalogo() {
        try {
            // Consultamos la lista de registros activos
            List<Producto> lista = productoDAO.listar();

            // Le pasamos la lista de objetos al método renderizador de tu vista
            this.vista.cargarProductosEnInterfaz(lista);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al conectar con el catálogo de productos: " + ex.getMessage(),
                    "Error de Sistema",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Clic en el botón "Inicio" (Recarga o refresca el catálogo de productos)
        if (e.getSource() == vista.sidebar.bCasa) {
            cargarCatalogo();
        }

        // Clic en el botón "Historial" (Creado en tu barra lateral)
        if (e.getSource() == vista.historial) {
            JOptionPane.showMessageDialog(vista,
                    "Abriendo el historial completo de tus citas atendidas y productos comprados.",
                    "Módulo Historial",
                    JOptionPane.INFORMATION_MESSAGE);
            // En el futuro enlazas tu vista aquí:
            // new ControladorHistorial(new VistaHistorial()).iniciar();
        }

        // Clic en el botón central "Cerrar Sesión"
        if (e.getSource() == vista.btnCerrarSesion) {
            int respuesta = JOptionPane.showConfirmDialog(vista,
                    "¿Estás seguro de que deseas cerrar tu sesión en Nexus GO?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                vista.dispose(); // Cierra la interfaz actual del cliente liberando memoria

                // Aquí vuelves a llamar tu pantalla de Login para dejar el sistema listo:
                // VistaLogin login = new VistaLogin();
                // new ControladorLogin(login);
                // login.setVisible(true);
            }
        }

        if (e.getSource() == vista.CitasVigentes) {

            VistaReservarCitas panelReserva = new VistaReservarCitas();
            new ControladorReservarCita(panelReserva, vista, 1);

            // Cambiamos la pantalla de inmediato
            vista.getContenidoCentralDinamico().removeAll();
            vista.getContenidoCentralDinamico().add(panelReserva);
            vista.getContenidoCentralDinamico().revalidate();
            vista.getContenidoCentralDinamico().repaint();
        }

        if (e.getSource() == vista.CitasVigentes) {

            VistaReservarCitas panelReserva = new VistaReservarCitas();

            // Instanciamos su propio controlador (Pasamos: vista formulario, vista principal, ID de usuario)
            new ControladorReservarCita(panelReserva, vista, 1);

            // Cambiamos la pantalla de inmediato
            vista.getContenidoCentralDinamico().removeAll();
            vista.getContenidoCentralDinamico().add(panelReserva);
            vista.getContenidoCentralDinamico().revalidate();
            vista.getContenidoCentralDinamico().repaint();
        }

    }

}
