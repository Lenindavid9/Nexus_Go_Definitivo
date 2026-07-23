/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import static javax.swing.Box.createRigidArea;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author HOME
 */
public class PanelCitasPeluquero extends JPanel {
   private JPanel panelContenedorSolicitudes;
    private JButton btnCerrarSesion;
    private JPanel tarjetaBlanca;

    public PanelCitasPeluquero() {
        // Layout nulo para posicionamiento absoluto con coordenadas fijas (setBounds)
        setLayout(null); 
        // Panel transparente para renderizar la textura de fondo de la ventana principal
        setOpaque(false); 

        // Inicialización y diseño personalizado del botón Cerrar Sesión
        btnCerrarSesion = new JButton("cerrar sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 230, 100), getWidth(), 0, new Color(255, 180, 0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnCerrarSesion.setBounds(680, 20, 140, 35);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setFocusPainted(false);
        add(btnCerrarSesion);

        // Contenedor principal redondeado de la interfaz
        tarjetaBlanca = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245, 240)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        tarjetaBlanca.setBounds(30, 70, 800, 470);
        tarjetaBlanca.setLayout(null);
        tarjetaBlanca.setOpaque(false);
        add(tarjetaBlanca);

        // CONFIGURACIÓN DEL CALENDARIO (ZONA IZQUIERDA)
        JPanel panelCalendario = new JPanel(null);
        panelCalendario.setBounds(20, 20, 400, 430);
        panelCalendario.setOpaque(false);
        tarjetaBlanca.add(panelCalendario);

        // Cabecera de los días de la semana
        JPanel panelDiasSemana = new JPanel(new GridLayout(1, 7));
        panelDiasSemana.setBounds(10, 80, 380, 30);
        panelDiasSemana.setOpaque(false);
        String[] dias = {"L", "m", "m", "j", "v", "s", "d"};
        for (String dia : dias) {
            JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
            lblDia.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblDia.setForeground(new Color(100, 100, 100));
            panelDiasSemana.add(lblDia);
        }
        panelCalendario.add(panelDiasSemana);

        // Matriz de días numéricos del mes
        JPanel panelNumeros = new JPanel(new GridLayout(6, 7, 5, 5));
        panelNumeros.setBounds(10, 120, 380, 260);
        panelNumeros.setOpaque(false);
        
        for (int i = 1; i <= 31; i++) {
            String numStr = (i < 10) ? "0" + i : "" + i;
            JLabel lblNum;
            
            // Destacar días específicos con citas agendadas (Mock inicial)
            if (i == 2 || i == 24) {
                lblNum = new JLabel(numStr, SwingConstants.CENTER) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(240, 173, 7)); 
                        int size = Math.min(getWidth(), getHeight()) - 10;
                        g2.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                lblNum.setForeground(Color.WHITE);
                lblNum.setFont(new Font("SansSerif", Font.BOLD, 12));
            } else {
                lblNum = new JLabel(numStr, SwingConstants.CENTER);
                lblNum.setForeground(new Color(50, 50, 50));
                lblNum.setFont(new Font("SansSerif", Font.PLAIN, 12));
            }
            panelNumeros.add(lblNum);
        }
        
        // Relleno estético del final de la cuadrícula
        for (int i = 1; i <= 11; i++) {
            JLabel lblNumGris = new JLabel("0" + i, SwingConstants.CENTER);
            lblNumGris.setForeground(new Color(180, 180, 180));
            lblNumGris.setFont(new Font("SansSerif", Font.PLAIN, 12));
            panelNumeros.add(lblNumGris);
        }
        panelCalendario.add(panelNumeros);

        // CONFIGURACIÓN DEL CONTENEDOR DE SOLICITUDES (ZONA DERECHA)
        panelContenedorSolicitudes = new JPanel();
        panelContenedorSolicitudes.setLayout(new BoxLayout(panelContenedorSolicitudes, BoxLayout.Y_AXIS));
        panelContenedorSolicitudes.setOpaque(false);

        JScrollPane scrollSolicitudes = new JScrollPane(panelContenedorSolicitudes);
        scrollSolicitudes.setBounds(440, 20, 340, 430);
        scrollSolicitudes.setBorder(null);
        scrollSolicitudes.setOpaque(false);
        scrollSolicitudes.getViewport().setOpaque(false);
        scrollSolicitudes.getVerticalScrollBar().setUnitIncrement(12);
        tarjetaBlanca.add(scrollSolicitudes);
    }

    /**
     * Añade de forma dinámica una tarjeta de solicitud al contenedor vertical.
     * Permite enlazar las acciones de los botones directamente desde el controlador.
     * 
     * @param idCita Identificador único para mapear en las acciones
     * @param servicio Nombre del servicio solicitado
     * @param fecha Fecha de ejecución de la cita
     * @param hora Hora de la cita
     * @param accionAceptar Listener del controlador para procesar la aceptación
     * @param accionRechazar Listener del controlador para procesar el rechazo
     */
    public void agregarSolicitudTarjeta(int idCita, String servicio, String fecha, String hora, 
                                        ActionListener accionAceptar, ActionListener accionRechazar) {
        
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 220, 220, 235)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        tarjeta.setLayout(null);
        tarjeta.setPreferredSize(new Dimension(320, 110));
        tarjeta.setMaximumSize(new Dimension(320, 110));
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblTitulo = new JLabel("Solicitud de cita", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTitulo.setBounds(10, 8, 300, 18);
        tarjeta.add(lblTitulo);

        JLabel lblDetalle = new JLabel("<html><center>servicio de: " + servicio + "<br>fecha: " + fecha + "<br>hora: " + hora + "</center></html>", SwingConstants.CENTER);
        lblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblDetalle.setForeground(new Color(80, 80, 80));
        lblDetalle.setBounds(10, 26, 300, 42);
        tarjeta.add(lblDetalle);

        // Botón Rechazar
        JButton btnRechazar = new JButton("Rechazar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnRechazar.setBounds(25, 72, 110, 28);
        btnRechazar.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnRechazar.setForeground(Color.BLACK);
        btnRechazar.setContentAreaFilled(false);
        btnRechazar.setBorderPainted(false);
        btnRechazar.setFocusPainted(false);
        btnRechazar.setActionCommand(String.valueOf(idCita)); // Pasa el ID de la cita al presionar
        btnRechazar.addActionListener(accionRechazar);
        tarjeta.add(btnRechazar);

        // Botón Aceptar
        JButton btnAceptar = new JButton("Aceptar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 160, 0)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnAceptar.setBounds(185, 72, 110, 28);
        btnAceptar.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setContentAreaFilled(false);
        btnAceptar.setBorderPainted(false);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setActionCommand(String.valueOf(idCita)); // Pasa el ID de la cita al presionar
        btnAceptar.addActionListener(accionAceptar);
        tarjeta.add(btnAceptar);

        // Guardado e indexación fluida en la cola visual
        panelContenedorSolicitudes.add(tarjeta);
        panelContenedorSolicitudes.add(createRigidArea(new Dimension(0, 10)));
        
        panelContenedorSolicitudes.revalidate();
        panelContenedorSolicitudes.repaint();
    }

    // --- Getters limpios para la comunicación con el Controlador ---
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
    public JPanel getPanelContenedorSolicitudes() { return panelContenedorSolicitudes; }
}
