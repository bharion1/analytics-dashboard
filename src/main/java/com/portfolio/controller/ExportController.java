package com.portfolio.controller;

import com.portfolio.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ExportController {
    
    private final ExportService exportService;
    
    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        byte[] data = exportService.exportToExcel();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "metricas.xlsx");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(data);
    }
    
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf() throws Exception {
        byte[] data = exportService.exportToPdf();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "metricas.pdf");
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(data);
    }
}

