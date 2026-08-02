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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import nexusgo.model.Factura;
import nexusgo.model.DetalleCarrito;
import nexusgo.model.Usuario;

/**
 *
 * @author USUARIO
 */
public class GeneradorFacturaPdf {

    // Paleta de colores para el PDF
    private static final BaseColor COLOR_PRINCIPAL = new BaseColor(18, 30, 49);     // Azul oscuro
    private static final BaseColor COLOR_ACCENTO = new BaseColor(212, 175, 55);    // Dorado Nexus
    private static final BaseColor COLOR_GRIS_FONDO = new BaseColor(245, 247, 250); // Fondo gris alternado
    private static final BaseColor COLOR_TEXTO_OSCURO = new BaseColor(40, 40, 40);
    private static final BaseColor COLOR_VERDE_DESCUENTO = new BaseColor(40, 167, 69); // Verde para descuento cliente fiel

    // Tipografías
    private static final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_ACCENTO);
    private static final Font FONT_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_PRINCIPAL);
    private static final Font FONT_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_PRINCIPAL);
    private static final Font FONT_REGULAR = FontFactory.getFont(FontFactory.HELVETICA, 10, COLOR_TEXTO_OSCURO);
    private static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_TEXTO_OSCURO);
    private static final Font FONT_DESCUENTO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_VERDE_DESCUENTO);
    private static final Font FONT_CABECERA_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private static final Font FONT_TOTAL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLOR_PRINCIPAL);

    /**
     * Genera la factura en PDF con desglose de Subtotal, Descuento Cliente Fiel
     * y Total.
     */
    public static String generarPdf(Factura factura, Usuario cliente) {
        String idFacturaStr = (factura != null && factura.getIdFactura() > 0)
                ? String.valueOf(factura.getIdFactura())
                : String.valueOf(System.currentTimeMillis());

        String rutaArchivo = System.getProperty("user.home")
                + File.separator + "Desktop"
                + File.separator + "Factura_NexusGO_" + idFacturaStr + ".pdf";

        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();

            // 1. ENCABEZADO PRINCIPAL
            Paragraph titulo = new Paragraph("NEXUSGO", FONT_TITULO);
            titulo.setAlignment(Element.ALIGN_LEFT);
            documento.add(titulo);

            Paragraph subtitulo = new Paragraph("COMPROBANTE DE COMPRA DIGITAL", FONT_SUBTITULO);
            subtitulo.setSpacingAfter(15);
            documento.add(subtitulo);

            // 2. INFORMACIÓN DEL CLIENTE Y DATOS DE LA VENTA
            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setWidths(new float[]{1f, 1f});

            PdfPCell celdaCliente = new PdfPCell();
            celdaCliente.setBorder(PdfPCell.NO_BORDER);
            celdaCliente.addElement(new Paragraph("DATOS DEL CLIENTE", FONT_SECCION));

            if (cliente != null) {
                String nombreCompleto = (cliente.getNombre() != null ? cliente.getNombre() : "") + " "
                        + (cliente.getApellido() != null ? cliente.getApellido() : "");
                celdaCliente.addElement(new Paragraph("Nombre: " + nombreCompleto.trim(), FONT_REGULAR));
                celdaCliente.addElement(new Paragraph("ID / Cedula: " + cliente.getIdentificacion(), FONT_REGULAR));
                celdaCliente.addElement(new Paragraph("Tipo: Cliente Registrado (Fiel)", FONT_BOLD));
                if (cliente.getCorreo() != null && !cliente.getCorreo().trim().isEmpty()) {
                    celdaCliente.addElement(new Paragraph("Correo: " + cliente.getCorreo(), FONT_REGULAR));
                }
            } else {
                celdaCliente.addElement(new Paragraph("Cliente: Consumidor Final / General", FONT_REGULAR));
            }

            PdfPCell celdaFactura = new PdfPCell();
            celdaFactura.setBorder(PdfPCell.NO_BORDER);
            celdaFactura.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaFactura.addElement(new Paragraph("DETALLES DE LA VENTA", FONT_SECCION));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd / HH:mm:ss");
            String fechaTexto = (factura != null && factura.getFechaVenta() != null)
                    ? sdf.format(factura.getFechaVenta())
                    : sdf.format(new Date());

            celdaFactura.addElement(new Paragraph("Factura N°: " + idFacturaStr, FONT_BOLD));
            celdaFactura.addElement(new Paragraph("Fecha: " + fechaTexto, FONT_REGULAR));
            celdaFactura.addElement(new Paragraph("Estado: PAGADO", FONT_BOLD));

            tablaInfo.addCell(celdaCliente);
            tablaInfo.addCell(celdaFactura);
            tablaInfo.setSpacingAfter(20);
            documento.add(tablaInfo);

            // 3. TABLA DE PRODUCTOS Y SERVICIOS
            PdfPTable tablaProductos = new PdfPTable(4);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setWidths(new float[]{3.5f, 1f, 1.5f, 1.5f});

            agregarCeldaEncabezado(tablaProductos, "Producto / Servicio");
            agregarCeldaEncabezado(tablaProductos, "Cant.");
            agregarCeldaEncabezado(tablaProductos, "Precio U.");
            agregarCeldaEncabezado(tablaProductos, "Subtotal");

            boolean alternarColor = false;
            if (factura != null && factura.getDetalles() != null && !factura.getDetalles().isEmpty()) {
                for (DetalleCarrito detalle : factura.getDetalles()) {
                    BaseColor colorFondo = alternarColor ? COLOR_GRIS_FONDO : BaseColor.WHITE;

                    agregarCeldaCuerpo(tablaProductos, detalle.getNombreProducto(), Element.ALIGN_LEFT, colorFondo);
                    agregarCeldaCuerpo(tablaProductos, String.valueOf(detalle.getCantidad()), Element.ALIGN_CENTER, colorFondo);
                    agregarCeldaCuerpo(tablaProductos, "$" + String.format("%.0f", detalle.getPrecioUnitario()), Element.ALIGN_RIGHT, colorFondo);
                    agregarCeldaCuerpo(tablaProductos, "$" + String.format("%.0f", detalle.getSubtotal()), Element.ALIGN_RIGHT, colorFondo);

                    alternarColor = !alternarColor;
                }
            }

            tablaProductos.setSpacingAfter(15);
            documento.add(tablaProductos);

            // 4. DESGLOSE DE TOTALES (SUBTOTAL, DESCUENTO CLIENTE FIEL Y TOTAL FINAL)
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(50);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);

            double subtotalVal = (factura != null) ? factura.getSubtotal() : 0.0;
            double descuentoVal = (factura != null) ? factura.getDescuentoAplicado() : 0.0;
            double totalVal = (factura != null) ? factura.getTotal() : 0.0;

            // Fila 1: Subtotal
            tablaTotales.addCell(crearCeldaSinBorde("Subtotal:", FONT_REGULAR, Element.ALIGN_LEFT));
            tablaTotales.addCell(crearCeldaSinBorde("$" + String.format("%.0f", subtotalVal), FONT_REGULAR, Element.ALIGN_RIGHT));

            // Fila 2: Descuento Cliente Fiel
            String etiquetaDescuento = (descuentoVal > 0) ? "Descuento Cliente Fiel:" : "Descuento Aplicado:";
            tablaTotales.addCell(crearCeldaSinBorde(etiquetaDescuento, FONT_DESCUENTO, Element.ALIGN_LEFT));
            tablaTotales.addCell(crearCeldaSinBorde("-$" + String.format("%.0f", descuentoVal), FONT_DESCUENTO, Element.ALIGN_RIGHT));

            // Fila 3: Total Pagado
            tablaTotales.addCell(crearCeldaSinBorde("TOTAL PAGADO:", FONT_TOTAL, Element.ALIGN_LEFT));
            tablaTotales.addCell(crearCeldaSinBorde("$" + String.format("%.0f", totalVal), FONT_TOTAL, Element.ALIGN_RIGHT));

            tablaTotales.setSpacingAfter(25);
            documento.add(tablaTotales);

            // 5. MENSAJE DE AGRADECIMIENTO
            Paragraph pie = new Paragraph("¡Gracias por tu compra en Salon de Belleza NEXUS! ✨\nConserva este comprobante para cualquier reclamo o garantía.", FONT_REGULAR);
            pie.setAlignment(Element.ALIGN_CENTER);
            documento.add(pie);

            documento.close();
            return rutaArchivo;

        } catch (Exception e) {
            System.err.println("Error al generar el PDF de la factura: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String generarPdf(Factura factura) {
        return generarPdf(factura, null);
    }

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

            BodyPart mensajeBodyPart = new MimeBodyPart();
            String textoCorreo = "¡Hola! 👋\n\n"
                    + "🎉 ¡Confirmamos que tu compra en NexusGO se realizó con éxito!\n\n"
                    + "📎 Adjunto encontrarás tu comprobante en formato PDF con el desglose de tu compra y tu descuento de cliente fiel aplicado.\n\n"
                    + "✨ ¡Muchas gracias por preferirnos!\n\n"
                    + "Atentamente,\n"
                    + "El equipo de NexusGO 🚀";

            mensajeBodyPart.setText(textoCorreo);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(rutaPdf));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mensajeBodyPart);
            multipart.addBodyPart(attachmentPart);

            mensaje.setContent(multipart);

            Transport.send(mensaje);
            return true;

        } catch (Exception e) {
            System.err.println("Error de red SMTP al enviar factura: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- MÉTODOS AUXILIARES ---
    private static void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_CABECERA_TABLA));
        celda.setBackgroundColor(COLOR_ACCENTO);
        celda.setPadding(8);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBorderColor(COLOR_PRINCIPAL);
        tabla.addCell(celda);
    }

    private static void agregarCeldaCuerpo(PdfPTable tabla, String texto, int alineacion, BaseColor fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_REGULAR));
        celda.setBackgroundColor(fondo);
        celda.setPadding(6);
        celda.setHorizontalAlignment(alineacion);
        celda.setBorderColor(new BaseColor(230, 230, 230));
        tabla.addCell(celda);
    }

    private static PdfPCell crearCeldaSinBorde(String texto, Font fuente, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(3);
        return celda;
    }
}
