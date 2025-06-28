package com.despesasdeputados.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataEmissao;
    private String fornecedor;
    private BigDecimal valorLiquido;
    private String urlDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deputado_id")
    private Deputado deputado;
}