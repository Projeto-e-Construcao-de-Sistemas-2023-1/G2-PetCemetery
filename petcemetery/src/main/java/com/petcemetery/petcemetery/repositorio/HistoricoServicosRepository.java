package com.petcemetery.petcemetery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.HistoricoServicos;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;


public interface HistoricoServicosRepository extends JpaRepository<HistoricoServicos, Long> {
    HistoricoServicos findByIdServico(Long idServico);
    List<HistoricoServicos> findAllByClienteCpf(String cpf);
    List<HistoricoServicos> findByTipoServico(ServicoEnum tipoServico);
}