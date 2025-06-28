package com.despesasdeputados.controller;

import com.despesasdeputados.model.Deputado;
import com.despesasdeputados.model.Despesa;
import com.despesasdeputados.service.DeputadoService;
import com.despesasdeputados.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private DeputadoService deputadoService;

    @Autowired
    private DespesaService despesaService;

    @GetMapping("/deputados/{id}/total-despesas")
    public ResponseEntity<BigDecimal> getTotalDespesasByDeputado(@PathVariable Long id) {
        Optional<Deputado> deputadoOptional = deputadoService.getDeputadoById(id);
        if (deputadoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Despesa> despesas = despesaService.findDespesasByDeputadoId(id);
        BigDecimal total = despesas.stream()
                .map(Despesa::getValorLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total-despesas")
    public ResponseEntity<BigDecimal> getTotalDespesasGeral() {
        List<Deputado> deputados = deputadoService.getAllDeputados();
        BigDecimal totalGeral = BigDecimal.ZERO;

        for (Deputado deputado : deputados) {
            List<Despesa> despesas = despesaService.findDespesasByDeputadoId(deputado.getId());
            BigDecimal totalDeputado = despesas.stream()
                    .map(Despesa::getValorLiquido)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalGeral = totalGeral.add(totalDeputado);
        }
        return ResponseEntity.ok(totalGeral);
    }
}