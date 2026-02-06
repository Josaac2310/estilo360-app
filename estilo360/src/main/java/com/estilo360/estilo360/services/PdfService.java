package com.estilo360.estilo360.services;

import com.estilo360.estilo360.dao.ServicioDAO;
import com.estilo360.estilo360.entidades.Servicio;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Servicio encargado de generar PDFs para el catálogo de servicios.
 * Utiliza iText para crear documentos PDF con formato corporativo.
 * 
 * @version 1.0
 */
@Service
public class PdfService {

    /** DAO para acceso a datos de servicios */
    private final ServicioDAO servicioDAO;

    /**
     * Constructor que inyecta el DAO de servicios.
     * 
     * @param servicioDAO Componente de acceso a datos de servicios
     */
    public PdfService(ServicioDAO servicioDAO) {
        this.servicioDAO = servicioDAO;
    }

    /**
     * Genera un PDF con el catálogo de servicios disponibles.
     * Incluye nombre, descripción, precio y duración de cada servicio.
     * 
     * @return Arreglo de bytes que representa el PDF generado
     * @throws RuntimeException si ocurre un error al generar el PDF
     */
    public byte[] generarPdfServicios() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Color corporativo (azul de Estilo360)
            DeviceRgb colorAzul = new DeviceRgb(37, 99, 235);
            DeviceRgb colorFondoHeader = new DeviceRgb(239, 246, 255);

            // Título
            Paragraph titulo = new Paragraph("Estilo360 - Catálogo de Servicios")
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(colorAzul)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(titulo);

            // Subtítulo
            Paragraph subtitulo = new Paragraph("Todos nuestros servicios disponibles")
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(subtitulo);

            // Obtener servicios
            List<Servicio> servicios = servicioDAO.findAll();

            if (servicios.isEmpty()) {
                Paragraph noServicios = new Paragraph("No hay servicios disponibles")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setItalic();
                document.add(noServicios);
            } else {
                // Crear tabla
                float[] columnWidths = {3, 4, 1.5f, 1.5f};
                Table table = new Table(UnitValue.createPercentArray(columnWidths));
                table.setWidth(UnitValue.createPercentValue(100));

                // Headers de la tabla
                String[] headers = {"Servicio", "Descripción", "Precio", "Duración"};
                for (String header : headers) {
                    Cell cell = new Cell()
                            .add(new Paragraph(header).setBold())
                            .setBackgroundColor(colorFondoHeader)
                            .setFontColor(colorAzul)
                            .setPadding(10)
                            .setTextAlignment(TextAlignment.CENTER);
                    table.addHeaderCell(cell);
                }

                // Filas de servicios
                for (Servicio servicio : servicios) {
                    // Nombre
                    table.addCell(new Cell()
                            .add(new Paragraph(servicio.getNombre()).setBold())
                            .setPadding(8));

                    // Descripción
                    String descripcion = servicio.getDescripcion() != null && !servicio.getDescripcion().isEmpty()
                            ? servicio.getDescripcion()
                            : "Sin descripción";
                    table.addCell(new Cell()
                            .add(new Paragraph(descripcion))
                            .setPadding(8));

                    // Precio
                    table.addCell(new Cell()
                            .add(new Paragraph(String.format("%.2f€", servicio.getPrecio())))
                            .setPadding(8)
                            .setTextAlignment(TextAlignment.CENTER));

                    // Duración
                    table.addCell(new Cell()
                            .add(new Paragraph(servicio.getDuracion_minutos() + " min"))
                            .setPadding(8)
                            .setTextAlignment(TextAlignment.CENTER));
                }

                document.add(table);
            }

            // Footer
            Paragraph footer = new Paragraph("© 2025 Estilo360 - Todos los derechos reservados")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30)
                    .setFontColor(ColorConstants.GRAY);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            throw new RuntimeException("Error al generar PDF de servicios");
        }

        return baos.toByteArray();
    }
}