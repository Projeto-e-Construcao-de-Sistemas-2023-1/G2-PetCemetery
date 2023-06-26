package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.HistoricoServicos;

import lombok.Data;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VisualizarDespesasDTO {
    String endJazigo;
    String tipoServico;
    double valor;

    public VisualizarDespesasDTO(HistoricoServicos historicoServicos){
        this.endJazigo = historicoServicos.getJazigo().getEndereco();
        this.tipoServico = historicoServicos.getTipoServico().toString();
        this.valor = historicoServicos.getValor();
    }
}
