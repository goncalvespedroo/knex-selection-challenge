package com.despesasdeputados.repository;

import com.despesasdeputados.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findByDeputadoId(Long deputadoId);
    Page<Despesa> findByFornecedorContainingIgnoreCase(String fornecedor, Pageable pageable); // Adicionado Pageable
    Page<Despesa> findByDataEmissaoBetween(LocalDate startDate, LocalDate endDate, Pageable pageable); // Adicionado Pageable
}