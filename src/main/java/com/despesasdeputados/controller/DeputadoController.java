package com.despesasdeputados.controller;

import com.despesasdeputados.model.Deputado;
import com.despesasdeputados.service.DeputadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deputados")
public class DeputadoController {

    @Autowired
    private DeputadoService deputadoService;

    @GetMapping
    public ResponseEntity<List<Deputado>> getDeputadosByUf(@RequestParam(required = false) String uf) {
        if (uf != null && !uf.isEmpty()) {
            List<Deputado> deputados = deputadoService.findDeputadosByUf(uf);
            return ResponseEntity.ok(deputados);
        } else {
            List<Deputado> deputados = deputadoService.getAllDeputados();
            return ResponseEntity.ok(deputados);
        }
    }
}