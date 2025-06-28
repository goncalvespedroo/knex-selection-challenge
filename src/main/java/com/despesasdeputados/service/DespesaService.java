package com.despesasdeputados.service;

import com.despesasdeputados.model.Despesa;
import com.despesasdeputados.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    public List<Despesa> findDespesasByDeputadoId(Long deputadoId) {
        return despesaRepository.findByDeputadoId(deputadoId);
    }

    // Retorna lista de despesas, filtrando essas despesas por período de data ou por fornecedor.
    public Page<Despesa> getAllDespesas(Pageable pageable, LocalDate startDate, LocalDate endDate, String fornecedor) {
        if (startDate != null && endDate != null) {
            return despesaRepository.findByDataEmissaoBetween(startDate, endDate, pageable);
        } else if (fornecedor != null && !fornecedor.isEmpty()) {
            return despesaRepository.findByFornecedorContainingIgnoreCase(fornecedor, pageable);
        } else {
            return despesaRepository.findAll(pageable);
        }
    }

    // Sobrecarga para permitir paginação sem filtros específicos
    public Page<Despesa> getAllDespesas(Pageable pageable) {
        return despesaRepository.findAll(pageable);
    }
}