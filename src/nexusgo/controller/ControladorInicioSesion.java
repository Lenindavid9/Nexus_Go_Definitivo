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
import registro.VistaRegistroDeUsuario;

/**
 *
 * @author USUARIO
 */
public class ControladorInicioSesion implements ActionListener{
  private final VistaInicioSesion vistaLogin;
    private final UsuarioDao usuarioDao;
    private boolean ocultarClave = true; // Control de estado para el botón del ojo

    public ControladorInicioSesion(VistaInicioSesion vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.usuarioDao = new UsuarioDao(); 
        
        inicializarListeners();
    }
  
    

    /**
     * Enlaza los componentes interactivos de la vista con sus respectivos escuchadores.
     */
    private void inicializarListeners() {
        // Escuchar el botón dorado "Entrar"
        this.vistaLogin.btnEntrar.addActionListener(this);

        // 👁️ ACCIÓN PARA EL BOTÓN DEL OJO (Ver/Ocultar contraseña)
        // Asegúrate de que en tu VistaInicioSesion declaraste un JButton llamado 'btnVerContrasena'
        if (this.vistaLogin.btnVerContrasena != null) {
            this.vistaLogin.btnVerContrasena.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (ocultarClave) {
                        // Muestra la contraseña (0 quita el caracter ocultador)
                        vistaLogin.tContrasena.setEchoChar((char) 0);
                        vistaLogin.btnVerContrasena.setText("ver");
                        ocultarClave = false;
                    } else {
                        // Vuelve a poner los puntos/asteriscos por defecto
                        vistaLogin.tContrasena.setEchoChar('*'); 
                        vistaLogin.btnVerContrasena.setText("ver");
                        ocultarClave = true;
                    }
                }
            });
        }

        // Escuchar el clic sobre el enlace interactivo "¡Regístrate aquí!"
        this.vistaLogin.lblRegistrate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    vistaLogin.dispose();
                    
                    VistaRegistroDeUsuario vistaRegistro = new VistaRegistroDeUsuario();
                    ControladorRegistroDeUsuarios controladorRegistro = new ControladorRegistroDeUsuarios(vistaRegistro);
                    
                    vistaRegistro.setLocationRelativeTo(null);
                    vistaRegistro.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vistaLogin, 
                        "Error al abrir el módulo de registro: " + ex.getMessage(), 
                        "Error de Interfaz", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Escuchar el enlace de "¿Olvidé mi contraseña?"
        this.vistaLogin.lblOlvideContrasena.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(vistaLogin, 
                    "Módulo de recuperación de contraseña en desarrollo.", 
                    "Nexus Go! Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.btnEntrar) {
            ejecutarLogin();
        }
    }

    /**
     * Extrae los datos ingresados por el usuario, valida contra MySQL y da paso al sistema general.
     */
    private void ejecutarLogin() {
        try {
            String identificacion = vistaLogin.tNroIdentidad.getText().trim();
            String contrasena = new String(vistaLogin.tContrasena.getPassword()).trim();

            // Validación de campos vacíos o placeholders por defecto
            if (identificacion.isEmpty() || contrasena.isEmpty() || 
                identificacion.equals("Ingrese su número de documento") || contrasena.equals("Ingresar su contraseña")) {
                JOptionPane.showMessageDialog(vistaLogin, "Por favor, complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Consulta relacional controlando posibles excepciones de base de datos
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
