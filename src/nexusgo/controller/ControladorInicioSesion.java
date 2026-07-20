
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.PanelAdmi;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaPrincipalAdminSoftware;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaPrincipalOperario;
import nexusgo.view.VistaPrincipalPeluquero;
import nexusgo.view.VistaPrincipalSupervisor;
import nexusgo.view.VistaValidarIdentificacion;
import nexusgo.view.VistaRegistroDeUsuario;

/**
 *
 * @author USUARIO
 */
public class ControladorInicioSesion implements ActionListener {

    private final VistaInicioSesion vistaLogin;

    /*A través de UsuarioDao se realizan consultas para verificar
    si el usuario existe y si sus credenciales son correctas.*/
    private final UsuarioDao usuarioDao;

    /*Variable utilizada para que contraseña este oculta o visible.
    true = La contraseña esta oculta.
    false = La contraseña se muestra.
    Esta variable cambia de valor del booleano cada vez que el usuario
    presiona el botón "Ver" */
    private boolean ocultarClave = true;

    public ControladorInicioSesion(VistaInicioSesion vistaLogin) {
        //Guarda la referencia de la ventana.
        this.vistaLogin = vistaLogin;
        //objeto encargado de realizar las consultas en la base de datos
        this.usuarioDao = new UsuarioDao();
        //registran todos los eventos de la interfaz.
        inicializarListeners();
    }

    /*Enlaza los componentes interactivos de la vista con sus respectivos escuchadores.
    Cada componente recibe un "escuchador"
    (osea el Listener), el cual estará pendiente de las acciones
    realizadas por el usuario */
    private void inicializarListeners() {

        // Escuchar el botón dorado "Entrar"
        this.vistaLogin.btnEntrar.addActionListener(this);

        // Accion para ver la contraseña 
        // Asegúrate de que en tu VistaInicioSesion declaraste un JButton llamado 'btnVerContrasena'
        if (this.vistaLogin.btnVerContrasena != null) {

            //Se agrega un ActionListener solamente para controlar el botón "Ver".
            this.vistaLogin.btnVerContrasena.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //Si la contraseña ACTUALMENTE está oculta...
                    if (ocultarClave) {
                        /*El valor 0 elimina temporalmente el
                        carácter utilizado para ocultar la contraseña,
                        permitiendo ver el texto real.
                        Se muestra la contraseña ( 0 quita el caracter ocultador)*/
                        vistaLogin.tContrasena.setEchoChar((char) 0);

                        // se mantiene el texto del botón
                        vistaLogin.btnVerContrasena.setText("ver");

                        /*Se actualiza el estado indicando que
                        ahora la contraseña está visible.*/
                        ocultarClave = false;
                    } else {
                        // Vuelve a poner los asteriscos por defecto
                        vistaLogin.tContrasena.setEchoChar('*');

                        /* Se actualiza el estado indicando que
                        la contraseña vuelve a estar oculta.*/
                        vistaLogin.btnVerContrasena.setText("ver");

                        ocultarClave = true;
                    }
                }
            });
        }

        /*esta es la escuchar el clic sobre el enlace interactivo "¡Regístrate aquí!"
        Como la etiqueta (JLabel) no poseen eventos
        de clic propios, se utiliza MouseListener para
        detectar cuando el usuario hace clic sobre ella. */
        this.vistaLogin.lblRegistrate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Primero se cierra la ventana actual de inicio de sesión.
                vistaLogin.dispose();

                //Se abre la ventana donde podran registrarse
                VistaRegistroDeUsuario vistaRegistro = new VistaRegistroDeUsuario();

                // tambien se imvoca su controlador correspondiente
                ControladorRegistroDeUsuarios controladorRegistro = new ControladorRegistroDeUsuarios(vistaRegistro);

                //Se centra la ventana en la pantalla.
                vistaRegistro.setLocationRelativeTo(null);

                //muestra la ventana
                vistaRegistro.setVisible(true);
            }
        });

        // Escuchar el enlace de "¿Olvidé mi contraseña?", eslo mismo que pasa con el JPanel de registrarse
        this.vistaLogin.lblOlvideContrasena.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Primero se cierra la ventana actual de inicio de sesión.
                vistaLogin.dispose();

                /*Se abre la ventana donde podran ingresar su numero de identificacion
                para mirar si existe en la bases de batos*/
                VistaValidarIdentificacion validarIdentificacion = new VistaValidarIdentificacion();

                // tambien se imvoca su controlador correspondiente
                ControladorValidarIdentificacion controladorValidarIdentificacion = new ControladorValidarIdentificacion(validarIdentificacion);

                //Se establece el tamaño inicial que tendrá la ventana.
                validarIdentificacion.setSize(450, 450);

                // La ventana se abre por completo en toda la pantalla
                validarIdentificacion.setExtendedState(validarIdentificacion.MAXIMIZED_BOTH);

                //muestra la ventana
                validarIdentificacion.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnEntrar) {
            /* Se llama al método encargado de realizar
            todo el proceso de autenticación del usuario*/
            ejecutarLogin();
        }
    }

    /* Aquí se obtienen los datos escritos por el usuario,
        se validan y despues se consulta la base de datos
        para verificar si las credenciales son correctas */
    private void ejecutarLogin() {
        try {
            //Se obtiene el número de documento en String escrito por el usuario
            String texIdentificacion = vistaLogin.tNroIdentidad.getText();

            /*Se obtiene la contraseña.
            getPassword() devuelve un arreglo de caracteres,
            por lo que primero se convierte en String
            y se eliminan posibles espacios innecesarios.
            (Lo de los espacion lo puse solamente por si las moscas pero no es obliatorio aplicarlo)*/
            String contrasena = new String(vistaLogin.tContrasena.getPassword()).trim();

            /*Antes de consultar la base de datos se verifica
            que ambos campos contengan información válida
            
            con esto también se evita que el usuario intente iniciar
            sesión dejando los textos de ayuda*/
            if (texIdentificacion.isEmpty() || contrasena.isEmpty()
                    || texIdentificacion.equals("Ingrese su número de documento")
                    || contrasena.equals("Ingresar su contraseña")) {

                // Si alguno de los campos no es válido y se le informa al usuario
                JOptionPane.showMessageDialog(vistaLogin,
                        "Por favor, Debe completar los campos vacíos para poder iniciar sesión”", "Campos Vacíos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try{
            
            /*Se obtiene el texto del campo de identificación,
            si tiene, se eliminan los espacios en blanco al inicio y al final
            que por error pudo el usuario haber ingresado y se verificar qque el valor sea un número entero.*/
            Integer.parseInt(vistaLogin.tNroIdentidad.getText().trim());
        }catch (NumberFormatException e){
            
            /*Si falla, significa que el usuario ingresó un valor que no es 
            un número(puede ser un . o una , o cualquier otra cosa)*/
            JOptionPane.showMessageDialog(vistaLogin,"Recuerda que debes ingresar el numero de identificacion y eso solo puede contener datos numericos (Sin puntos).",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /*Después de comprobar que es válido, se convierte definitivamente el String 
        y se almacena en una variable int para utilizarla mas a delant edonde obliga el formato int*/
        int identificacion = Integer.parseInt(texIdentificacion);

            /*Se declara una variable de tipo Usuario y se inicializa con el valor null.
            Esto se hace porque al inicio, todavía no se sabe si las credenciales
            ingresadas pertenecen en vrd a un usuario registrado en la base de datos.
            Más adelante esta variable almacenará toda la información del usuario
            que haya iniciado sesión correctamente.*/
            Usuario usuarioLogueado = null;
            try {

                /*Se intenta autenticar al usuario utilizando el numero de identificacion
                y la contraseña que ingreso.
                Recuenrden que el metodo autenticarUsuario() hace que consute en la base de datos
                para verificar si el registro en vrd coincida con esos datos
                Si las credenciales son correctas, devuelve un objeto de tipo Usuario
                con toda la información ( osea el nombre, rol, correo, etc.),
                y se almacena en la variable usuarioLogueado, Si las credenciales no existen,
                devolverá null, diciendo que no fue posible autenticar al usuario.*/
                usuarioLogueado = usuarioDao.autenticarUsuario(identificacion, contrasena);
            } catch (Exception sqlEx) {

                /* Si durante la consulta ocurre un problema, como que la base de datos
                esté apagada, el servidor MySQL de Laragon no se esté ejecutándose por X razon,
                exista un error de conexión o cualquier otra cosa de la base de datos,
                el sistema entra en esta exepcion.
                En lugar que la aplicación se cierre inesperadamente,
                se captura la excepción para informar al usuario de manera clara
                ue ocurrió un problema de conexión.*/
                JOptionPane.showMessageDialog(vistaLogin,
                        "Error crítico de conexión con la base de datos MySQL (Laragon).\nVerifique que el servicio esté activo.\nDetalle: "
                        + sqlEx.getMessage(),
                        "Error de Conexión", JOptionPane.ERROR_MESSAGE);

                /* Esto evita que el sistema continúe intentando iniciar sesión
                cuando no fue posible consultar la información en la base de datos.*/
                return;
            }

            /* Se verifica si la variable usuarioLogueado contenga un objeto que si sea válido,
               lo que significa que el método encontró un usuario
               registrado en la base de datos y las credenciales fueron correctas.
            
               Si la variable sigue siendo null, significa que el usuario no existe
               o que la identificación, o la contraseña no coinciden.*/
            if (usuarioLogueado != null) {
                String nombreReal = usuarioLogueado.getNombre();
                String rolReal = usuarioLogueado.getRol();

                //nesaje de Bienvenida para cualquier usuario
                JOptionPane.showMessageDialog(vistaLogin, "¡Bienvenido " + nombreReal + " al Sistema NEXUS!");

                /* Se comienza a verificar el rol del usuario para decidir
                qué interfaz principal debe abrir el sistema.*/
                if (rolReal.equalsIgnoreCase("Supervisor")) {

                    // Se llama la ventana principal correspondiente al Supervisor.
                    VistaPrincipalSupervisor vistaSupervisor = new VistaPrincipalSupervisor("", "");

                    //Se llama el controlador encargado de administrar toda la lógica del Supervisor*/
                    ControladorPrincipalSupervisor controladorSupervisor = new ControladorPrincipalSupervisor(vistaSupervisor, usuarioLogueado);

                    //Se hace visible la ventana principal del Supervisor.
                    vistaSupervisor.setVisible(true);

                    //Se cierra la ventana de inicio de sesión
                    vistaLogin.dispose();
                    
                    /*Si el usuario no es Supervisor, se verifica si pertenece al rol de Operario.*/
                } else if (rolReal.equalsIgnoreCase("Operario")) {

                    // Se llama la ventana principal correspondiente al Operario.
                    VistaPrincipalOperario vistaMenu = new VistaPrincipalOperario();

                    /*Se llama el controlador encargado de gestionar las acciones
                    que realizará el Operario en el sistema*/
                    ControladorPrincipalOperario controladorOperario = new ControladorPrincipalOperario(vistaMenu,usuarioLogueado);

                    // La ventana se abre por completo en toda la pantalla
                    vistaMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);

                    // Se muestra la ventana principal del Operario.
                    vistaMenu.setVisible(true);

                    //Se cierra la ventana de inicio de sesión
                    vistaLogin.dispose();
                    
//                } else if (rolReal.equalsIgnoreCase("Administrador_de_la_peluqueria")) {
//                    VistaPrincipalAdministrador vistaAdminPelu = new VistaPrincipalAdministrador();
////                    PanelAdmi panelAdmi = new PanelAdmi();
//
//                    ControladorPrincipalAdministrador controlAdminPelu = new ControladorPrincipalAdministrador(vistaAdminPelu, panelAdmi, usuarioLogueado);
//
//                    vistaAdminPelu.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                    vistaAdminPelu.setVisible(true);
//                    vistaLogin.dispose();
                  
                } else if (rolReal.equalsIgnoreCase("Administrador_del_software")) {
                    VistaPrincipalAdminSoftware vistaAdminSoft = new VistaPrincipalAdminSoftware();

                     ControladorPrincipalAdminSoftware controlAdminSoft = new ControladorPrincipalAdminSoftware(vistaAdminSoft, null, usuarioLogueado);

                    vistaAdminSoft.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    
                    vistaAdminSoft.setVisible(true);
                    
                    vistaLogin.dispose();
//                    
                } else if (rolReal.equalsIgnoreCase("Peluquero")) {
                    
                    VistaPrincipalPeluquero vistaPeluquero = new VistaPrincipalPeluquero();
                    
                    ControladorPrincipalPeluquero controlPeluquero = new ControladorPrincipalPeluquero(vistaPeluquero, usuarioLogueado);

                    vistaPeluquero.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    
                    vistaPeluquero.setVisible(true);
                    
                    vistaLogin.dispose();

                    /*Si el usuario no es Peluquero, se verifica si pertenece al rol de Cliente.*/
                } else if (rolReal.equalsIgnoreCase("Cliente")) {

                    // Se llama la ventana principal correspondiente al Cliente.
                    VistaPrincipalCliente vistaCliente = new VistaPrincipalCliente(nombreReal, rolReal);

                    //Se llama el controlador encargado de gestionar las acciones del Cliente
                    ControladorPrincipalCliente controladorCliente = new ControladorPrincipalCliente(vistaCliente);

                    // Se muestra la ventana principal del Cliente.
                    vistaCliente.setVisible(true);

                    //Se cierra la ventana de inicio de sesión
                    vistaLogin.dispose();
                } else {
                    /*Si el rol obtenido desde la base de datos no coincide
                    con ninguno de los roles que este controlador reconoce,
                    se informa al usuario que todavía no tiene
                    permisos para acceder. */
                    JOptionPane.showMessageDialog(vistaLogin, "El rol '" + rolReal + "' no tiene accesos asignados en este panel.", "Error de Permisos", JOptionPane.ERROR_MESSAGE);
                }

                /*Si usuarioLogueado es null significa que la autenticación falló,
                osea que no se encontró un usuario con la identificación y
                contraseña que proporciono.*/
            } else {
                /*Se informa al usuario que los datos ingresados son incorrectos
                para que pueda verificarlos e intentar nuevamente.*/
                JOptionPane.showMessageDialog(vistaLogin, "Número de identificación o contraseña incorrectos.\nInténtelo nuevamente.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {

            /*Se muestra un mensaje indicando que ocurrió un error inesperado.
            es mas que todo para facilitar la identificación del problema durante el desarrollo
            por si aparece un error que no tengamos en cuenta*/
            JOptionPane.showMessageDialog(vistaLogin,
                    "Ocurrió un error inesperado al procesar el ingreso: " + ex.getMessage(),
                    "Error Extraño", JOptionPane.ERROR_MESSAGE);
        }
    }
}
