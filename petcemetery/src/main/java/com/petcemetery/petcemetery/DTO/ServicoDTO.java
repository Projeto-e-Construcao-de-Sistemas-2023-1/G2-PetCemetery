package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import lombok.Data;

@Data
public class ServicoDTO {
    
    String cpfCliente;
    long idJazigo;
    double valor;
    ServicoEnum tipoServico;
    String enderecoJazigo;
    long idPet;
    PlanoEnum plano;

    public ServicoDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, long idJazigo, long idPet, String cpfCliente) {
        this.idJazigo = idJazigo;
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.idPet = idPet;
        this.cpfCliente = cpfCliente;
    }
}
