package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Servico.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import lombok.Data;

@Data
public class HistoricoServicosDTO {
    
    String cpfCliente;
    long idJazigo; // Pode ser nulo!
    double valor;
    ServicoEnum tipoServico;
    String enderecoJazigo; // Pode ser nulo!
    long idPet; // Pode ser nulo!
    String dataServico; 
    PlanoEnum plano; // Teoricamente também deveria poder ser nulo, mas não no momento não pode. kkk

    // CONSTRUTOR - Full args
    public HistoricoServicosDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, long idJazigo, long idPet, String dataServico, String cpfCliente) {
        this.idJazigo = idJazigo;
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.idPet = idPet;
        this.dataServico = dataServico;
        this.cpfCliente = cpfCliente;
    }

    public HistoricoServicosDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, long idJazigo, long idPet, String cpfCliente) {
        this.idJazigo = idJazigo;
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.idPet = idPet;
        this.cpfCliente = cpfCliente;
    }

    // CONSTRUTOR - Pet nulo
    public HistoricoServicosDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano, long idJazigo, String cpfCliente) {
        this.idJazigo = idJazigo;
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
        this.cpfCliente = cpfCliente;
    }

    // CONSTRUTOR - Pet e Jazigo nulos
    public HistoricoServicosDTO(double valor, ServicoEnum tipoServico, PlanoEnum plano, String cpfCliente) {
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.plano = plano;
        this.cpfCliente = cpfCliente;
    }
}
