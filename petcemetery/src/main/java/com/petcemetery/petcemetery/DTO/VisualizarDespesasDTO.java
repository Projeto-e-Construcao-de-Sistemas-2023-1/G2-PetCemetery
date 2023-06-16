package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Servico;

import lombok.Data;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VisualizarDespesasDTO {
    String endJazigo;
    String tipoServico;
    double valor;

    public VisualizarDespesasDTO(Servico servico){
        this.endJazigo = servico.getJazigo().getEndereco();
        this.tipoServico = servico.getTipoServico().toString();
        this.valor = servico.getValor();
    }
}
