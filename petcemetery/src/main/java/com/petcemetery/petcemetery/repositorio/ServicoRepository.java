package com.petcemetery.petcemetery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;


public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Servico findByIdServico(Long idServico);
    List<Servico> findBycliente_cpf(String cpf);
    List<Servico> findByTipoServico(ServicoEnum tipoServico);
}