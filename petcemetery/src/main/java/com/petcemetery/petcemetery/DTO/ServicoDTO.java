package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import lombok.Data;

@Data
public class ServicoDTO {
    
    double valor;
    ServicoEnum tipoServico;
    String enderecoJazigo;
    PlanoEnum plano;
    Long idServico;

    public ServicoDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, Long idServico) {
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.idServico = idServico;
    }
}
