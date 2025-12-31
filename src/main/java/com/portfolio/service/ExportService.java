package com.portfolio.service;

import com.portfolio.model.Metric;
import com.portfolio.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    
    private final MetricRepository metricRepository;
    
    public byte[] exportToExcel() throws IOException {
        List<Metric> metrics = metricRepository.findAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Métricas");
        
        // Estilo para cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.GOLD.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Criar cabeçalho
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nome", "Categoria", "Valor", "Data/Hora", "Descrição"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Preencher dados
        int rowNum = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        for (Metric metric : metrics) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(metric.getId());
            row.createCell(1).setCellValue(metric.getName());
            row.createCell(2).setCellValue(metric.getCategory());
            row.createCell(3).setCellValue(metric.getValue());
            row.createCell(4).setCellValue(metric.getTimestamp().format(formatter));
            row.createCell(5).setCellValue(metric.getDescription() != null ? metric.getDescription() : "");
        }
        
        // Ajustar largura das colunas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
    
    public byte[] exportToPdf() throws DocumentException {
        List<Metric> metrics = metricRepository.findAll();
        
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        // Título
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Relatório de Métricas", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Tabela
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2, 2, 3, 3});
        
        // Cabeçalho
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        String[] headers = {"ID", "Nome", "Categoria", "Valor", "Data/Hora", "Descrição"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.BLACK);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // Dados
        com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Metric metric : metrics) {
            table.addCell(new PdfPCell(new Phrase(metric.getId().toString(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(metric.getName(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(metric.getCategory(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", metric.getValue()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(metric.getTimestamp().format(formatter), dataFont)));
            table.addCell(new PdfPCell(new Phrase(metric.getDescription() != null ? metric.getDescription() : "", dataFont)));
        }
        
        document.add(table);
        document.close();
        
        return outputStream.toByteArray();
    }
}

