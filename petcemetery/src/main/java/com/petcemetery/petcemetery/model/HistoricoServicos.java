package com.petcemetery.petcemetery.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcemetery.petcemetery.model.Servico.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Entity(name = "HistoricoServicos")
@Table(name = "HistoricoServicos")
public class HistoricoServicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Adicionado para permitir auto incremento do id
    @Column(name = "id_servico")
    private Long idServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico")
    private ServicoEnum tipoServico;

    @ManyToOne
    @JoinColumn(name = "jazigo_id_jazigo")
    private Jazigo jazigo;

    @Column(name = "valor")
    private double valor;

    @Column(name = "data_servico")
    private LocalDate dataServico;

    @Column(name = "hora_servico")
    private LocalTime horaServico;

    @OneToOne
    @JoinColumn(name = "id_pet")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "cliente_cpf")
    private Cliente cliente;

    @Column(name = "primeiro_pagamento")
    private LocalDate primeiroPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "plano")
    private PlanoEnum plano;

    @Column(name = "ultimo_pagamento")
    private LocalDate ultimoPagamento;

    public HistoricoServicos(){}

    // All args constructor
    public HistoricoServicos(ServicoEnum tipoServico, double valor, Cliente cliente, Jazigo jazigo, PlanoEnum plano, Pet pet, LocalDate dataServico, LocalTime horaServico, LocalDate primeiroPagamento, LocalDate ultimoPagamento) {
        this.tipoServico = tipoServico;
        this.valor = valor;
        this.cliente = cliente;
        this.jazigo = jazigo;
        this.plano = plano;
        this.pet = pet;
        this.dataServico = dataServico;
        this.horaServico = horaServico;
        this.primeiroPagamento = primeiroPagamento;
        this.ultimoPagamento = ultimoPagamento;
    }
    
    public HistoricoServicos(ServicoEnum tipoServico, double valor, Cliente cliente, Jazigo jazigo, PlanoEnum plano, Pet pet, LocalDate dataServico, LocalTime horaServico) {
        this.tipoServico = tipoServico;
        this.valor = valor;
        this.cliente = cliente;
        this.jazigo = jazigo;
        this.plano = plano;
        this.pet = pet;
        this.dataServico = dataServico;
        this.horaServico = horaServico;
    }
}
