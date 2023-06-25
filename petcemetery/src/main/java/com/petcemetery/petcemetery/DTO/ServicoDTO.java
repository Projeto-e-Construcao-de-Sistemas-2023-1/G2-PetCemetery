package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import lombok.Data;

@Data
public class ServicoDTO {
    
    String cpfCliente;
    long idJazigo; // Pode ser nulo!
    double valor;
    ServicoEnum tipoServico;
    String enderecoJazigo; // Pode ser nulo!
    long idPet; // Pode ser nulo!
    PlanoEnum plano; // Teoricamente também deveria poder ser nulo, mas não no momento não pode. kkk

    // CONSTRUTOR - Full args
    public ServicoDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, long idJazigo, long idPet, String cpfCliente) {
        this.idJazigo = idJazigo;
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.idPet = idPet;
        this.cpfCliente = cpfCliente;
    }

    // CONSTRUTOR - Pet nulo
    public ServicoDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, long idJazigo, String cpfCliente) {
        this.idJazigo = idJazigo;
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.cpfCliente = cpfCliente;
    }

    // CONSTRUTOR - Pet e Jazigo nulos
    public ServicoDTO(double valor, ServicoEnum tipoServico, PlanoEnum plano, String cpfCliente) {
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.plano = plano;
        this.cpfCliente = cpfCliente;
    }
}
