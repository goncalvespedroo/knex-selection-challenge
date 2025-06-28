package com.despesasdeputados.controller;

import com.despesasdeputados.model.Despesa;
import com.despesasdeputados.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping("/deputados/{id}/despesas")
    public ResponseEntity<List<Despesa>> getDespesasByDeputadoId(@PathVariable Long id) {
        List<Despesa> despesas = despesaService.findDespesasByDeputadoId(id);
        if (despesas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/despesas")
    public ResponseEntity<Page<Despesa>> getAllDespesas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String fornecedor) {

        Pageable pageable = PageRequest.of(page, size);
        LocalDate start = null;
        LocalDate end = null;

        try {
            if (startDate != null) {
                start = LocalDate.parse(startDate);
            }
            if (endDate != null) {
                end = LocalDate.parse(endDate);
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request caso a data seja inválida
        }

        Page<Despesa> despesas = despesaService.getAllDespesas(pageable, start, end, fornecedor);
        return ResponseEntity.ok(despesas);
    }
}