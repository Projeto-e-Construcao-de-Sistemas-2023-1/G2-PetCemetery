package com.petcemetery.petcemetery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Servico findByTipoServico(ServicoEnum servicoEnum);
}