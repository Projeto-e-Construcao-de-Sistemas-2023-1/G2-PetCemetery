package com.petcemetery.petcemetery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petcemetery.petcemetery.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Servico findByText(String text);
}