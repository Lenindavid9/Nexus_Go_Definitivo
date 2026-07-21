/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import nexusgo.model.Factura;
import nexusgo.model.DetalleCarrito;

/**
 *
 * @author USUARIO
 */
public class GeneradorFacturaPdf {
    
   public static String generarPdf(Factura factura) {
        String rutaArchivo = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Factura_" + System.currentTimeMillis() + ".pdf";
        
        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            // Encabezado de la factura
            documento.add(new Paragraph("=== NEXUSGO - TICKET DE VENTA ==="));
            documento.add(new Paragraph("Fecha: " + factura.getFechaVenta()));
            documento.add(new Paragraph("--------------------------------------------------------------------------------"));

            // Listar los productos del carrito/detalles
            if (factura.getDetalles() != null) {
                for (DetalleCarrito detalle : factura.getDetalles()) {
                    documento.add(new Paragraph("Producto: " + detalle.getNombreProducto() + 
                                              " | Cant: " + detalle.getCantidad() + 
                                              " | Subtotal: $" + detalle.getSubtotal()));
                }
            }

            documento.add(new Paragraph("--------------------------------------------------------------------------------"));
            documento.add(new Paragraph("TOTAL A PAGAR: $" + factura.getTotal()));

            documento.close();
            return rutaArchivo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para enviar el correo con el PDF adjunto usando tu configuración SMTP validada
    public static boolean enviarCorreo(String destinatarioF, String rutaPdf) {
        // Mismas credenciales y configuración que ya usas en el sistema
        final String miCorreoRemitente = "liliannysbaptistap@gmail.com";
        final String miClaveDeCorreo = "rksu umvz hnom irzf";

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        Session sesionMail = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(miCorreoRemitente, miClaveDeCorreo);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesionMail);
            mensaje.setFrom(new InternetAddress(miCorreoRemitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("NexusGO - Su Comprobante de Compra");

            // Cuerpo del mensaje
            BodyPart mensajeBodyPart = new MimeBodyPart();
            mensajeBodyPart.setText("Estimado cliente,\n\nAdjunto encontrará el archivo PDF con el detalle de su compra en NexusGO. ¡Muchas gracias por preferirnos!");

            // Archivo adjunto (PDF)
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(rutaPdf));

            // Unir texto y adjunto en un contenedDor Multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mensajeBodyPart);
            multipart.addBodyPart(attachmentPart);

            mensaje.setContent(multipart);

            // Enviar correo
            Transport.send(mensaje);
            return true;

        } catch (Exception e) {
            System.out.println("Error de red SMTP al enviar factura: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
