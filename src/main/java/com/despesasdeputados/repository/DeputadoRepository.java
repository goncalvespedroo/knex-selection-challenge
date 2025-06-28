package com.despesasdeputados.repository;

import com.despesasdeputados.model.Deputado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeputadoRepository extends JpaRepository<Deputado, Long> {
    List<Deputado> findByUf(String uf);
    Optional<Deputado> findByCpf(String cpf);
}