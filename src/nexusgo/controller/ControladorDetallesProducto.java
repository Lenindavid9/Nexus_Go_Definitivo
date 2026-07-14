/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;
import nexusgo.model.Producto;
import nexusgo.model.ProductoDao;
import nexusgo.view.VistaDetallesProducto;

/**
 *
 * @author HOME
 */
public class ControladorDetallesProducto implements ActionListener {
    
    private final VistaDetallesProducto vista;
    private Producto productoActual;
    
    

    public ControladorDetallesProducto(VistaDetallesProducto vista, Producto producto) {
        // Guardo la vista de detalles que me pasan para poder meterle los datos del producto
        this.vista = vista;

        // Pinto el nombre del producto en mayúscula sostenida en mi etiqueta correspondiente
        this.vista.lblNombreProducto.setText(producto.getNombreProducto().toUpperCase());
        // Formateo el precio para que aparezca con el signo de pesos y separado por miles automáticamente
        this.vista.lblPrecioProducto.setText(String.format("$%,.0f", producto.getPrecioCompra()));
        // Agrego todo el texto detallado del producto en mi caja de descripción
        this.vista.txtDescripcion.setText(producto.getDescripcion());

        try {
            // Busco el archivo de la imagen en la ruta que viene guardada desde la base de datos
            File archivoImg = new File(producto.getUrlImagen());
            // Si el archivo físico de la imagen sí existe en mi computadora ejecuto el cambio
            if (archivoImg.exists()) {
                // Cargo la imagen original desde la ruta especificada
                ImageIcon iconoOriginal = new ImageIcon(producto.getUrlImagen());

                // Redimensiono la imagen de forma suave para que encaje exacto en el cuadro morado (320 de ancho por 400 de alto)
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(320, 400, Image.SCALE_SMOOTH);
                // Le pongo la imagen ya escalada a mi etiqueta de la interfaz
                this.vista.lblImagenProducto.setIcon(new ImageIcon(imagenEscalada));
                // Le borro el texto temporal que tenía la etiqueta para que no se sobreponga con la foto
                this.vista.lblImagenProducto.setText("");
            } else {
                // Si la ruta no es válida o no encuentra el archivo muestro un texto de advertencia
                this.vista.lblImagenProducto.setText("Imagen no encontrada");
            }
        } catch (Exception ex) {
            // Si ocurre algún fallo inesperado al procesar el archivo protejo el sistema mostrando el error en el texto
            this.vista.lblImagenProducto.setText("Error al cargar imagen");
        }

        // Pongo a escuchar mi botón de volver para saber cuándo quiere salir el usuario
        this.vista.btnVolver.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Si el usuario presionó el botón de volver ejecuto el cierre de la pantalla
        if (e.getSource() == vista.btnVolver) {
            // Cierro y destruyo la ventana actual para limpiar la memoria caché de mi programa
            this.vista.dispose();
        }
    }
}
