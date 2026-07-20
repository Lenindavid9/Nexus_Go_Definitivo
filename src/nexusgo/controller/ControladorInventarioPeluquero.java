/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import nexusgo.view.PanelInventarioProductosPeluquero;
import nexusgo.view.VistaRegistrarSalida;
import nexusgo.view.VistaPrincipalPeluquero;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;

/**
 *
 * @author HOME
 */
public class ControladorInventarioPeluquero  {
   // Defino las referencias de las vistas y el acceso a datos
    private PanelInventarioProductosPeluquero vista;
    private VistaPrincipalPeluquero vistaPrincipal;
    private ProductoDao productoDao;

    // Constructor donde configuro la carga y los listeners del panel de inventario
    public ControladorInventarioPeluquero(PanelInventarioProductosPeluquero vista, VistaPrincipalPeluquero vistaPrincipal) {
        // Enlazo la vista del inventario y la ventana principal para poder intercambiar pantallas
        this.vista = vista;
        this.vistaPrincipal = vistaPrincipal;
        
        // Inicializo mi clase de acceso a datos de productos
        this.productoDao = new ProductoDao();
        
        // Cargo por primera vez el inventario completo desde la base de datos MySQL al abrir el panel
        cargarTablaProductos("");

        // Configuro el buscador dinámico para filtrar productos mientras el peluquero escribe
        this.vista.getTxtBuscar().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // No se requiere para capturar el texto final
            }
            @Override
            public void keyPressed(KeyEvent e) {
                // No se requiere antes de soltar la tecla
            }
            @Override
            public void keyReleased(KeyEvent e) {
                // Al levantar la tecla, capturo el texto y realizo la búsqueda dinámica en base de datos
                String textoBusqueda = vista.getTxtBuscar().getText().trim();
                
                if (textoBusqueda.equals("Q Search") || textoBusqueda.equals("")) {
                    cargarTablaProductos("");
                } else {
                    cargarTablaProductos(textoBusqueda);
                }
            }
        });

        // Configuro el evento de clic sobre las filas de la JTable
        this.vista.getTablaInventario().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtengo la fila seleccionada por el peluquero
                int filaSeleccionada = vista.getTablaInventario().getSelectedRow();
                
                // Si efectivamente seleccionó una fila válida
                if (filaSeleccionada != -1) {
                    // Obtengo el ID del producto guardado en la columna 0 de la tabla
                    String idString = vista.getTablaInventario().getValueAt(filaSeleccionada, 0).toString();
                    
                    try {
                        // Convertimos el ID de la tabla a entero para buscarlo
                        int idProducto = Integer.parseInt(idString);
                        
                        // Busco el producto correspondiente mediante el DAO
                        Producto productoSeleccionado = buscarProductoPorId(idProducto);
                        
                        // Si el producto existe, hago la redirección inmediata a la pantalla de registrar salida
                        if (productoSeleccionado != null) {
                            // Instancio la vista de salida de insumos
                            VistaRegistrarSalida vistaSalida = new VistaRegistrarSalida();
                             
                            // Reemplazo el panel actual por la vista de registro de salida
                            cambiarPanel(vistaSalida);
                        }
                    } catch (Exception ex) {
                        // Imprimo el error en consola en caso de fallo de conexión o consulta
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(vista, "Error al seleccionar el producto: " + ex.getMessage(), 
                                                      "Error del Sistema", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Configuro el botón de cerrar sesión
        this.vista.getBtnCerrarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(vista, "Cerrando sesión del Peluquero en NexusGO...");
            }
        });
    }

    /**
     * Consulta la base de datos y mapea las columnas exactas solicitadas en el diseño usando tus getters reales.
     */
    private void cargarTablaProductos(String filtro) {
        String[] columnas = {"ID Producto", "Nombre", "Cantidad (Stock)", "Descripción"};
        
        DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            // Evaluamos la búsqueda según lo que ofrece tu ProductoDao real
            if (filtro.isEmpty()) {
                // Si no hay filtro, listamos todo de forma normal
                List<Producto> listaProductos = productoDao.listar(); 
                if (listaProductos != null) {
                    for (Producto prod : listaProductos) {
                        modelo.addRow(new Object[]{
                            prod.getIdProducto(),
                            prod.getNombreProducto(),
                            prod.getStockActual(),
                            prod.getDescripcion()
                        });
                    }
                }
            } else {
                try {
                    // Si el usuario escribe algo, lo convertimos a ID numérico
                    int idFiltro = Integer.parseInt(filtro);
                    
                    // Tu DAO devuelve un Producto directo, no una lista. ¡Lo guardamos de forma precisa!
                    Producto prod = productoDao.buscarPorId(idFiltro);
                    
                    if (prod != null) {
                        modelo.addRow(new Object[]{
                            prod.getIdProducto(),
                            prod.getNombreProducto(),
                            prod.getStockActual(),
                            prod.getDescripcion()
                        });
                    }
                } catch (NumberFormatException e) {
                    // Si escribe letras en el buscador, no se agregará ninguna fila (queda limpia la tabla)
                }
            }

            // Enviamos el modelo procesado con los datos de MySQL a la JTable
            vista.setDatosInventario(modelo);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al conectar con la base de datos: " + ex.getMessage(), 
                                          "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Busca un producto por su ID apoyándose directamente en la función buscarPorId de tu DAO.
     */
    private Producto buscarProductoPorId(int idProducto) throws Exception {
        return productoDao.buscarPorId(idProducto);
    }

    /**
     * Intercambia el contenedor del Frame principal para desplegar la nueva pantalla.
     */
    private void cambiarPanel(JPanel nuevoPanel) {
        // Remuevo todas las pantallas activas del frame principal
        vistaPrincipal.getContentPane().removeAll();
        // Agrego la nueva vista de salida de productos
        vistaPrincipal.getContentPane().add(nuevoPanel);
        // Fuerza el redibujado de la interfaz gráfica de inmediato
        vistaPrincipal.revalidate();
        vistaPrincipal.repaint();
    }
}
