package com.petcemetery.petcemetery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petcemetery.petcemetery.model.Servico;


public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Servico findByIdServico(int idServico);
    List<Servico> findBycliente_cpf(String cpf);

}