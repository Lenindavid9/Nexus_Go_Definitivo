
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;
import nexusgo.model.UsuarioDao;
import nexusgo.view.VistaInicioSesion;
import nexusgo.view.VistaPrincipalCliente;
import nexusgo.view.VistaPrincipalOperario;
import nexusgo.view.VistaValidarIdentificacion;
import registro.VistaRegistroDeUsuario;

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

  
    /**
     * Enlaza los componentes interactivos de la vista con sus respectivos escuchadores.
     */


    /*Cada componente recibe un "escuchador"
    ( osea el Listener), el cual estará pendiente de las acciones
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
                        permitiendo ver el texto real.*/
                        // Muestra la contraseña (0 quita el caracter ocultador)
                        vistaLogin.tContrasena.setEchoChar((char) 0);
                        // se mantiene el texto del botón
                        vistaLogin.btnVerContrasena.setText("ver");
                        /*Se actualiza el estado indicando que
                         * ahora la contraseña está visible.*/
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

        /* esta es la escuchar el clic sobre el enlace interactivo "¡Regístrate aquí!"
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
                
                //Se abre la ventana donde podran ingresar su numero de identificacion
                // parea mirar si existe en la bases de batos
                VistaValidarIdentificacion validarIdentificacion = new VistaValidarIdentificacion();
                
                // tambien se imvoca su controlador correspondiente
                ControladorValidarIdentificacion controladorValidarIdentificacion = new ControladorValidarIdentificacion(validarIdentificacion);
                
                //Se centra la ventana en la pantalla.
                validarIdentificacion.setLocationRelativeTo(null);
                
                //muestra la ventana
                validarIdentificacion.setVisible(true);
                
                //esto queda mientras tanto
                JOptionPane.showMessageDialog(vistaLogin,
                        "Módulo de recuperación de contraseña en desarrollo, ACTUALMENTE TIENE ERRORES.",
                        "Nexus Go! Info", JOptionPane.ERROR_MESSAGE);
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
            //Se obtiene el número de documento escrito por el usuario
            String identificacion = vistaLogin.tNroIdentidad.getText();
            /*Se obtiene la contraseña.
            
            getPassword() devuelve un arreglo de caracteres,
            por lo que primero se convierte en String.
            
            y se eliminan posibles espacios innecesarios*/
            String contrasena = new String(vistaLogin.tContrasena.getPassword()).trim();

            /*Antes de consultar la base de datos se verifica
            que ambos campos contengan información válida
            
            También se evita que el usuario intente iniciar
            sesión dejando los textos de ayuda*/
            if (identificacion.isEmpty() || contrasena.isEmpty()
                || identificacion.equals("Ingrese su número de documento")
                || contrasena.equals("Ingresar su contraseña")){
                // Si alguno de los campos no es válido y se le informa al usuario
                JOptionPane.showMessageDialog(vistaLogin, 
                "Por favor, complete todos los campos.", "Campos Vacíos",
                JOptionPane.WARNING_MESSAGE);
                return;
            }

            /*se declara una variable*/
            Usuario usuarioLogueado = null;
            try {
                usuarioLogueado = usuarioDao.autenticarUsuario(identificacion, contrasena);
            } catch (Exception sqlEx) {
                JOptionPane.showMessageDialog(vistaLogin,
                        "Error crítico de conexión con la base de datos MySQL (Laragon).\nVerifique que el servicio esté activo.\nDetalle: " + sqlEx.getMessage(),
                        "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioLogueado != null) {
                String nombreReal = usuarioLogueado.getNombre();
                String rolReal = usuarioLogueado.getRol(); // "Operario", "Supervisor", o "Cliente"

                JOptionPane.showMessageDialog(vistaLogin, "¡Bienvenido " + nombreReal + " al Sistema NEXUS!");

                // --- ENRUTAMIENTO POR ROL ---
                // 1. Roles Administrativos / Internos
                if (rolReal.equalsIgnoreCase("Operario") || rolReal.equalsIgnoreCase("Supervisor")) {

                    VistaPrincipalOperario vistaMenu = new VistaPrincipalOperario();
                    new ControladorInventarioOperario(vistaMenu, usuarioLogueado);

                    vistaMenu.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
                    vistaMenu.setVisible(true);
                    vistaLogin.dispose();

                    // 2. 🚀 Nuevo Enrutamiento del Módulo de Clientes
                } else if (rolReal.equalsIgnoreCase("Cliente")) {

                    // Instanciamos la ventana del cliente pasándole sus datos correspondientes
                    VistaPrincipalCliente vistaCliente = new VistaPrincipalCliente(nombreReal, rolReal);

                    // Inicializamos su respectivo controlador para mapear el catálogo
                    new ControladorPrincipalCliente(vistaCliente);

                    // Desplegamos la ventana del cliente a pantalla completa
                    vistaCliente.setVisible(true);

                    // Destruimos el login para liberar memoria RAM
                    vistaLogin.dispose();

                } else if (rolReal.equalsIgnoreCase("Administrador")) {
                    JOptionPane.showMessageDialog(vistaLogin, "Panel de Administrador en desarrollo.", "Módulo Pendiente", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vistaLogin, "El rol '" + rolReal + "' no tiene accesos asignados en este panel.", "Error de Permisos", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(vistaLogin, "Número de identificación o contraseña incorrectos.\nInténtelo nuevamente.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaLogin,
                    "Ocurrió un error inesperado al procesar el ingreso: " + ex.getMessage(),
                    "Error Inesperado", JOptionPane.ERROR_MESSAGE);
        }
}
}