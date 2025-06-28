package com.despesasdeputados.controller;

import com.despesasdeputados.service.CsvProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload-ceap")
public class UploadController {

    @Autowired
    private CsvProcessorService csvProcessorService;

    @PostMapping
    public ResponseEntity<String> uploadCeapData() {
        try {
            csvProcessorService.processCsvData();
            return ResponseEntity.ok("Dados do CEAP processados e importados com sucesso!");
        } catch (Exception e) {
            e.printStackTrace(); // Ver o stack trace no console
            return ResponseEntity.internalServerError().body("Erro ao processar e importar dados do CEAP: " + e.getMessage());
        }
    }
}