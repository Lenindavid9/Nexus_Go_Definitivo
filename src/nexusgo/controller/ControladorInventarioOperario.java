/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//
package nexusgo.controller;

import java.awt.BorderLayout;
import nexusgo.model.Herramientas;
import nexusgo.model.HerramientaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaAgregarProducto;
import nexusgo.view.VistaOperarioInventario;
import nexusgo.model.Usuario;
import nexusgo.view.VistaAgregarHerramienta;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaRegistrarSalida;

/**
 *
 * @author USUARIO
 */
public class ControladorInventarioOperario implements ActionListener {

    private final VistaOperarioInventario panelInventario;
    private final JPanel contenedorCentral;
    private final Usuario usuarioLogueado;
    private final VistaAgregarProducto panelFormulario;
    private final VistaAgregarHerramienta panelFormularioHerramienta;
    private final VistaRegistrarSalida panelSalidaInsumo;
    private final ProductoDao productoDao = new ProductoDao();
    private final HerramientaDao herramientaDao = new HerramientaDao();
    private int idSeleccionado = -1;

    //Recibe la vista principal del inventario, el usuario que inició sesión y el panel central
    public ControladorInventarioOperario(VistaOperarioInventario panelInventario, Usuario usuarioLogueado, JPanel contenedorCentral) {

        this.panelInventario = panelInventario;
        this.usuarioLogueado = usuarioLogueado;
        this.contenedorCentral = contenedorCentral;
        this.panelFormulario = new VistaAgregarProducto();
        this.panelFormularioHerramienta = new VistaAgregarHerramienta();
        this.panelSalidaInsumo = new VistaRegistrarSalida();

        /*Despues que el controlador ha recibido las cosas necesarias, se registran los eventos
        de los botones, se cargan los datos almacenados en la base de datos dentro de las tablas
        y finalmente se aplican los permisos correspondientes según este rol.*/
        inicializarListeners();
        listarProductosEnTabla();
        listarHerramientasEnTabla();
        aplicarPermisosPorRol();
    }

    //Este método se encarga de configurar qué opciones estarán disponibles
    private void aplicarPermisosPorRol() {

        /*Se verifica que el botón para agregar productos exista antes de modificarlo
        Si existe, se hace visible para que el Operario pueda utilizar esta función.*/
        if (panelInventario.btnAgregarProducto != null) {
            panelInventario.btnAgregarProducto.setVisible(true);
        }

        //Lo mismo para este boton
        if (panelInventario.btnAgregarHerramienta != null) {
            panelInventario.btnAgregarHerramienta.setVisible(true);
        }
    }

    private void inicializarListeners() {

        /*Se verifica que el botón exista antes de asignarle el ActionListener,
        Cuando el usuario haga click en este botón, el evento será
        recibido por el método actionPerformed().*/
        if (panelInventario.btnAgregarProducto != null) {
            panelInventario.btnAgregarProducto.addActionListener(this);
        }
        if (panelInventario.btnAgregarHerramienta != null) {
            panelInventario.btnAgregarHerramienta.addActionListener(this);
        }

        //Se registra el evento del botón de cerrar sesión para detectar cuándo el usuario desea salir del sistema
        if (panelInventario.cerrarSesion != null) {
            panelInventario.cerrarSesion.addActionListener(this);
        }

        //Se verifica que la tabla de productos exista antes de registrar el evento
        if (panelInventario.tablaProductos != null) {

            //Se agrega un MouseListener para detectar las acciones sobre la tabla
            panelInventario.tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    // Se obtiene la fila que el usuario seleccionó
                    int fila = panelInventario.tablaProductos.getSelectedRow();

                    /* Se verifica que realmente exista en la fila seleccionada.
                    Si el fila es mayor o igual a cero entonces el usuario hizo clic sobre un registro
                    con datos.*/
                    if (fila >= 0) //Se llama al método encargado de mostrar el menú de opciones del producto seleccionado
                    {
                        lanzarMenuDecision("Producto", fila);
                    }
                }
            });
        }

        //Se verifica que la tabla de herramientas exista antes de registrar el evento
        if (panelInventario.tablaHerramientas != null) {

            /*Se agrega un MouseListener para detectar cuándo el usuario hace click
            sobre alguno de los registros de la tabla.*/
            panelInventario.tablaHerramientas.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    // Se obtiene la fila que fue seleccionada por el Operario
                    int fila = panelInventario.tablaHerramientas.getSelectedRow();
                    /*Se verifica que realmente exista una fila seleccionada.
                    Si la fila es mayor o igual a cero entonces el usuario hizo clic sobre
                    un registro con datos.*/
                    if (fila >= 0) //Se llama al método encargado de mostrar el menú de opciones de la herramienta seleccionado
                    {
                        lanzarMenuDecision("Herramienta", fila);
                    }
                }
            });
        }
    }

    //Este método muestra un menú de opciones cuando el usuario selecciona un registro de la tabla.
    private void lanzarMenuDecision(String tipo, int fila) {

        //Se crea un arreglo que aparecerán en la ventana de selección.
        String[] opciones = {"Registrar Salida", "Editar", "Eliminar"};

        /*Se muestra una ventana con las diferentes acciones que
        el usuario puede realizar sobre el registro seleccionado.
    
        El método devuelve la posición de la opción elegida:
        0 = Registrar Salida
        1 = Editar
        2 = Eliminar
        -1 = El usuario cerró la ventana sin seleccionar ninguna opción.*/
        int seleccion = JOptionPane.showOptionDialog(panelInventario,
                "¿Qué acción desea realizar con el registro seleccionado?",
                "NEXUS - Panel de Control", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        // Se verifica si el operario escogio "Registrar Salida".
        if (seleccion == 0) {

            // Se comprueba si el registro seleccionado corresponde a un producto
            if (tipo.equals("Producto")) {

                /*Se obtiene el identificador del producto seleccionado.
                Se toma el valor de la primera columna de la tabla,
                donde se almacena el ID del registro*/
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);

                /*Se llama al controlador encargado de gestionar
                el registro de la salida del producto.
                También se envía al método listarProductosEnTabla()
                para actualizar la tabla automáticamente cuando finalice la operació*/
                ControladorRegistrarSalida controlRegistSalida = new ControladorRegistrarSalida(panelSalidaInsumo, contenedorCentral, panelInventario, idSeleccionado, this::listarProductosEnTabla);

                // Se reemplaza el contenido del panel central por la vista
                cambiarPanelCentral(this.panelSalidaInsumo);
            } else {

                //Si el registro corresponde a una herramienta,se le informa lo sig
                JOptionPane.showMessageDialog(panelInventario, "Las herramientas cambian por estado físico, no numérico.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }

            // Se verifica si el operario escogio "Editar".
        } else if (seleccion == 1) {

            //Si se trata de un producto, se prepara el formulario para editar su información.
            if (tipo.equals("Producto")) {

                // Se obtiene el identificador del producto seleccionado.
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);

                //Se llama al controlador encargado de la edición del producto seleccionado.
                ControladorAgregarProducto controlAggProduc = new ControladorAgregarProducto(panelFormulario, contenedorCentral, panelInventario, idSeleccionado, this::listarProductosEnTabla);

                //Se muestra el formulario de edición.
                cambiarPanelCentral(this.panelFormulario);
            } else {

                //Si se trata de una herramienta, se obtiene el identificador del registro seleccionado
                idSeleccionado = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);

                // También se obtiene el nombre de la herramienta,
                String nombreHerramienta = panelInventario.tablaHerramientas.getValueAt(fila, 1).toString();

                //Se llama el controlador encargado de administrar la edición de herramientas.
                ControladorAgregarHerramienta controlAggHerra = new ControladorAgregarHerramienta(panelFormularioHerramienta, contenedorCentral, panelInventario, idSeleccionado, nombreHerramienta, this::listarHerramientasEnTabla);

                // Se muestra el formulario para editar
                cambiarPanelCentral(this.panelFormularioHerramienta);
            }

            // Se verifica si el operario escogio "Eliminar".
        } else if (seleccion == 2) {

            //Si el registro corresponde a un producto, se obtiene su identificador y se elimina
            if (tipo.equals("Producto")) {
                idSeleccionado = (int) panelInventario.tablaProductos.getValueAt(fila, 0);
                eliminarProducto(idSeleccionado);
            } else {

                //Si el registro corresponde a una herramienta, se obtiene su identificador y se elimina
                idSeleccionado = (int) panelInventario.tablaHerramientas.getValueAt(fila, 0);
                eliminarHerramienta(idSeleccionado);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Se obtiene el componente que generó el evento.
        Object src = e.getSource();

        //Se verifica si el evento fue generado por el botón de cerrar sesión.
        if (src == panelInventario.cerrarSesion) {
            //Se ejecuta el método encargado de cerrar la sesión y regresar al inicio.
            ejecutarCerrarSesion();

            // Se verifica si el botón presionado fue el de agregar producto.
        } else if (src == panelInventario.btnAgregarProducto) {

            /*Se llama el controlador encargado de administrar el formulario para registrar un nuevo producto.
            
            También se envía al método que actualizará automáticamente
            la tabla de productos cuando finalice el registro.*/
            ControladorAgregarProducto controlAggProduc = new ControladorAgregarProducto(panelFormulario, contenedorCentral, panelInventario, this::listarProductosEnTabla);

            // Se muestra el formulario de registro de productos
            cambiarPanelCentral(this.panelFormulario);

            // Se verifica si el botón presionado corresponde al registro de herramientas.
        } else if (src == panelInventario.btnAgregarHerramienta) {

            //Se crea el controlador encargado del formulario para registrar nuevas herramientas
            ControladorAgregarHerramienta controlAggHerra = new ControladorAgregarHerramienta(panelFormularioHerramienta, contenedorCentral, panelInventario, this::listarHerramientasEnTabla);

            // Se muestra el formulario correspondiente
            cambiarPanelCentral(this.panelFormularioHerramienta);
        }
    }

    private void cambiarPanelCentral(JPanel panelNuevo) {

        // Se verifica que el panel central exista antes de intentar
        if (contenedorCentral != null) {

            // Se eliminan todos los componentes que actualmente se encuentran dentro del panel.
            contenedorCentral.removeAll();

            // Se establece un BorderLayout para organizar correctamente
            contenedorCentral.setLayout(new BorderLayout());

            // Se agrega el nuevo panel en la posición central del controlador
            contenedorCentral.add(panelNuevo, BorderLayout.CENTER);

            // Se actualiza la estructura del panel para que los cambios sean reconocidos
            contenedorCentral.revalidate();

            // Se vuelve el panel en pantalla para mostrar inmediatamente el nuevo contenido
            contenedorCentral.repaint();
        } else {

            //esto es por si pasa un error no queriendo mostrar el panel
            System.out.println("Error: 'contenedorCentral' es nulo en el controlador.");
        }
    }

    private void eliminarProducto(int id) {

        // Antes de eliminar el registro, se solicita una confirmación.
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente este insumo?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Se verifica que haya confirmado la eliminación.
        if (confirmar == JOptionPane.YES_OPTION) {

            /* Se llama al DAO para eliminar el producto de la base de datos utilizando su identificador
            
            Si el resultado es mayor que cero, entonces que
        // la eliminación fue realizada correctamente*/
            if (productoDao.eliminar(id) > 0) {

                // Se actualiza nuevamente la tabla.
                listarProductosEnTabla();
            }
        }
    }

    private void eliminarHerramienta(int id) {

        // Se solicita confirmación antes de eliminar el registro.
        int confirmar = JOptionPane.showConfirmDialog(panelInventario, "¿Eliminar permanentemente esta herramienta?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        // Se verifica que el usuario haya aceptado la eliminación.
        if (confirmar == JOptionPane.YES_OPTION) {

            // Se elimina la herramienta utilizando su identificador.
            if (herramientaDao.eliminar(id) > 0) {

                // Se vuelve a cargar la información de la tabla
                listarHerramientasEnTabla();
            }
        }
    }

    // Este método consulta todos los productos almacenados en la base de datos
    public void listarProductosEnTabla() {
        try {

            // Se crea la tabla con los encabezados
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio Compra", "Stock Actual", "Stock Mínimo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Se asigna el nuevo modelo a la tabla de productos.
            panelInventario.tablaProductos.setModel(modeloBlindado);

            // Se obtiene desde la base de datos la lista completa de productos registrados.
            List<Producto> lista = productoDao.listar();

            // Se verifica que la consulta haya devuelto información.
            if (lista != null) {

                // Se recorre la lista de productos 
                for (Producto p : lista) {

                    // Cada producto se agrega como una nueva fila dentro de la tabla.
                    modeloBlindado.addRow(new Object[]{p.getIdProducto(), p.getNombreProducto(), p.getPrecioCompra(), p.getStockActual(), p.getStockMinimo()});
                }
            }
        } catch (Exception e) {

            //Se puso para saber donde buscar
            System.out.println("Error al listar productos: " + e.getMessage() + "ControladorInventarioOperario");
        }
    }

    // Este método consulta todas las herramientas registradas en loa base de datos.
    public void listarHerramientasEnTabla() {
        try {

            // Se crea un nuevo modelo para la tabla con esos encabezado
            DefaultTableModel modeloBlindado = new DefaultTableModel(new Object[]{"ID", "Nombre", "Estado", "Tipo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Se asigna el nuevo modelo a la tabla de herramientas.
            panelInventario.tablaHerramientas.setModel(modeloBlindado);

            // Se obtiene desde la base de datos la lista de herramientas registradas.
            List<Herramientas> lista = herramientaDao.listar();
            // Se verifica que la consulta haya devuelto información.
            if (lista != null) {

                // Se recorre cada herramienta obtenida de la base de datos
                for (Herramientas h : lista) {

                    // Se agrega una nueva fila con la información de cada herramienta.
                    modeloBlindado.addRow(new Object[]{h.getIdHerramienta(), h.getNombreHerramienta(), h.getEstadoActual(), "Herramienta"});
                }
            }
        } catch (Exception e) {

            // Si ocurre algún problema durante la consulta, se muestra el mensaje de error en la consola
            System.out.println("Error al listar herramientas: " + e.getMessage());
        }
    }

    private void ejecutarCerrarSesion() {

        // Se muestra una ventana de confirmación para verificar si en vrd quiere salir
        int confirmar = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión en NEXUS?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);

        // Se verifica que el usuario haya seleccionado la opción "Sí".
        if (confirmar == JOptionPane.YES_OPTION) {

            // Se obtiene la ventana principal que contiene el inventario
            Object ventana = panelInventario.getTopLevelAncestor();

            // Se comprueba que la ventana exista antes de intentar cerrarla.
            if (ventana != null) {
                try {

                    /*Se llama al método dispose() de la ventana mediante reflexión.
                    Esto permite cerrar la ventana sin importar la clase específica
                    a la que pertenezca, siempre que dicha clase tenga un método dispose().*/
                    ventana.getClass().getMethod("dispose").invoke(ventana);
                } catch (Exception ex) {

                    //es de prueba por si sale algun error
                    System.err.println("No se pudo cerrar la ventana: " + ex.getMessage());
                }
            }

            // Se crea una nueva instancia de la ventana
            VistaInicioSesion loginVista = new VistaInicioSesion();

            // Se llama el controlador encargado del inicio de sesion
            ControladorInicioSesion ininSesion = new ControladorInicioSesion(loginVista);

            // Se posiciona la ventana en el centro de la pantalla
            loginVista.setLocationRelativeTo(null);

            // Finalmente, se muestra nuevamente la ventana de inicio de sesion
            loginVista.setVisible(true);
        }
    }
}
