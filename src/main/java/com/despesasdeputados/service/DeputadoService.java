package com.despesasdeputados.service;

import com.despesasdeputados.model.Deputado;
import com.despesasdeputados.repository.DeputadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeputadoService {

    @Autowired
    private DeputadoRepository deputadoRepository;

    public List<Deputado> findDeputadosByUf(String uf) {
        return deputadoRepository.findByUf(uf);
    }

    public Optional<Deputado> getDeputadoById(Long id) {
        return deputadoRepository.findById(id);
    }

    public List<Deputado> getAllDeputados() {
        return deputadoRepository.findAll();
    }
}