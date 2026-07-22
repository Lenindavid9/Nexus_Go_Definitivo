/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nexusgo.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import nexusgo.model.Factura;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.Usuario;

/**
 *
 * @author USUARIO
 */
public class GeneradorFacturaPdf {

    // --- PALETA DE COLORES ELEGANTES ---
    private static final BaseColor COLOR_PRINCIPAL = new BaseColor(18, 30, 49);     // Azul oscuro ejecutivo
    private static final BaseColor COLOR_ACCENTO = new BaseColor(212, 175, 55);    // Dorado 
    private static final BaseColor COLOR_GRIS_FONDO = new BaseColor(245, 247, 250); // Fondo tabla claro
    private static final BaseColor COLOR_TEXTO_OSCURO = new BaseColor(40, 40, 40);

    // --- FUENTES ESTILIZADAS ---
    private static final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_ACCENTO);
    private static final Font FONT_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_PRINCIPAL);
    private static final Font FONT_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_PRINCIPAL);
    private static final Font FONT_REGULAR = FontFactory.getFont(FontFactory.HELVETICA, 10, COLOR_TEXTO_OSCURO);
    private static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_TEXTO_OSCURO);
    private static final Font FONT_CABECERA_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private static final Font FONT_TOTAL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLOR_PRINCIPAL);

    /**
     * Genera un PDF visualmente atractivo para la factura de venta.
     */
    public static String generarPdf(Factura factura, Usuario cliente) {
        String rutaArchivo = System.getProperty("user.home")
                + File.separator + "Desktop"
                + File.separator + "Factura_" + System.currentTimeMillis() + ".pdf";

        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            // 1. ENCABEZADO Y LOGO TEXTUAL
            Paragraph titulo = new Paragraph("NEXUSGO", FONT_TITULO);
            titulo.setAlignment(Element.ALIGN_LEFT);
            documento.add(titulo);

            Paragraph subtitulo = new Paragraph("COMPROBANTE DE COMPRA DIGITAL", FONT_SUBTITULO);
            subtitulo.setSpacingAfter(15);
            documento.add(subtitulo);

            // 2. TABLA DE INFORMACIÓN GENERAL (Empresa vs Datos de Venta)
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setWidths(new float[]{1f, 1f});

            // Columna Izquierda: Datos del Cliente
            PdfPCell celdaCliente = new PdfPCell();
            celdaCliente.setBorder(PdfPCell.NO_BORDER);
            celdaCliente.addElement(new Paragraph("DATOS DEL CLIENTE", FONT_SECCION));

            if (cliente != null) {
                celdaCliente.addElement(new Paragraph("Nombre: " + cliente.getNombre() + " " + cliente.getApellido(), FONT_REGULAR));
                celdaCliente.addElement(new Paragraph("ID: " + cliente.getIdentificacion(), FONT_REGULAR));
                if (cliente.getCorreo() != null && !cliente.getCorreo().trim().isEmpty()) {
                    celdaCliente.addElement(new Paragraph("Correo: " + cliente.getCorreo(), FONT_REGULAR));
                }
            } else {
                celdaCliente.addElement(new Paragraph("Cliente: Consumidor Final / General", FONT_REGULAR));
            }

            // Columna Derecha: Detalle Factura
            PdfPCell celdaFactura = new PdfPCell();
            celdaFactura.setBorder(PdfPCell.NO_BORDER);
            celdaFactura.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaFactura.addElement(new Paragraph("DETALLES DE LA VENTA", FONT_SECCION));
            celdaFactura.addElement(new Paragraph("Fecha: " + factura.getFechaVenta(), FONT_REGULAR));
            celdaFactura.addElement(new Paragraph("Estado: PAGADO", FONT_BOLD));

            tablaInfo.addCell(celdaCliente);
            tablaInfo.addCell(celdaFactura);
            tablaInfo.setSpacingAfter(20);
            documento.add(tablaInfo);

            // 3. TABLA DE PRODUCTOS
            PdfPTable tablaProductos = new PdfPTable(3);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setWidths(new float[]{3f, 1f, 1.5f}); // Ancho de columnas

            // Encabezados de la Tabla
            agregarCeldaEncabezado(tablaProductos, "Producto / Descripción");
            agregarCeldaEncabezado(tablaProductos, "Cantidad");
            agregarCeldaEncabezado(tablaProductos, "Subtotal");

            // Filas de Productos
            boolean alternarColor = false;
            if (factura.getDetalles() != null) {
                for (DetalleCarrito detalle : factura.getDetalles()) {
                    BaseColor colorFondo = alternarColor ? COLOR_GRIS_FONDO : BaseColor.WHITE;

                    agregarCeldaCuerpo(tablaProductos, detalle.getNombreProducto(), Element.ALIGN_LEFT, colorFondo);
                    agregarCeldaCuerpo(tablaProductos, String.valueOf(detalle.getCantidad()), Element.ALIGN_CENTER, colorFondo);
                    agregarCeldaCuerpo(tablaProductos, "$" + String.format("%.2f", detalle.getSubtotal()), Element.ALIGN_RIGHT, colorFondo);

                    alternarColor = !alternarColor;
                }
            }

            tablaProductos.setSpacingAfter(20);
            documento.add(tablaProductos);

            // 4. SECCIÓN DE TOTAL
            Paragraph totalParagraph = new Paragraph("TOTAL PAGADO: $" + String.format("%.2f", factura.getTotal()), FONT_TOTAL);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            totalParagraph.setSpacingAfter(30);
            documento.add(totalParagraph);

            // 5. PIE DE PÁGINA / AGRADECIMIENTO
            Paragraph pie = new Paragraph("¡Gracias por tu compra en NexusGO! ✨\nConserva este comprobante para cualquier reclamo o garantía.", FONT_REGULAR);
            pie.setAlignment(Element.ALIGN_CENTER);
            documento.add(pie);

            documento.close();
            return rutaArchivo;

        } catch (Exception e) {
            System.err.println("Error al generar el PDF elegante de la factura: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Auxiliar para crear celdas de encabezado con estilo
    private static void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_CABECERA_TABLA));
        celda.setBackgroundColor(COLOR_ACCENTO);
        celda.setPadding(8);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBorderColor(COLOR_PRINCIPAL);
        tabla.addCell(celda);
    }

    // Auxiliar para crear celdas de datos con estilo
    private static void agregarCeldaCuerpo(PdfPTable tabla, String texto, int alineacion, BaseColor fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_REGULAR));
        celda.setBackgroundColor(fondo);
        celda.setPadding(6);
        celda.setHorizontalAlignment(alineacion);
        celda.setBorderColor(new BaseColor(230, 230, 230));
        tabla.addCell(celda);
    }

    public static String generarPdf(Factura factura) {
        return generarPdf(factura, null);
    }

    /**
     * Envía la factura PDF adjunta en un correo electrónico con presentación
     * atractiva.
     */
    public static boolean enviarCorreo(String destinatarioF, String rutaPdf) {
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
            mensaje.setFrom(new InternetAddress(miCorreoRemitente, "NexusGO Store 🛍️"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatarioF));
            mensaje.setSubject("🧾 Comprobante de Compra - ¡Muchas Gracias por tu Pedido!");

            // Cuerpo del mensaje decorado con emojis
            BodyPart mensajeBodyPart = new MimeBodyPart();
            String textoCorreo = "¡Hola! 👋\n\n"
                    + "🎉 ¡Confirmamos que tu compra en NexusGO se realizó con éxito!\n\n"
                    + "📎 Adjunto a este correo encontrarás tu comprobante oficial en formato PDF con la lista detallada de tus productos.\n\n"
                    + "✨ ¡Muchas gracias por preferirnos! Nos alegra mucho tenerte con nosotros.\n\n"
                    + "Atentamente,\n"
                    + "El equipo de NexusGO 🚀";

            mensajeBodyPart.setText(textoCorreo);

            // Archivo adjunto PDF
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(rutaPdf));

            // Contenedor Multipart
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mensajeBodyPart);
            multipart.addBodyPart(attachmentPart);

            mensaje.setContent(multipart);

            // Enviar correo
            Transport.send(mensaje);
            return true;

        } catch (Exception e) {
            System.err.println("Error de red SMTP al enviar factura: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
