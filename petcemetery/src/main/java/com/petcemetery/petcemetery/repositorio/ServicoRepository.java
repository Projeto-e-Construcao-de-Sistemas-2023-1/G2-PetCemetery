package com.petcemetery.petcemetery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

}