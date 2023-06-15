package com.petcemetery.petcemetery.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;

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
import jakarta.persistence.Transient;

@Entity(name = "Servico")
@Table(name = "Servico")
public class Servico {

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

    @Transient
    private static double manutencao = 1300.0; 

    @Transient 
    private static double exumacao = 200.0;

    @Transient 
    private static double enterro = 400.0;

    public enum ServicoEnum {
        COMPRA,
        ALUGUEL,
        PERSONALIZACAO,
        MANUTENCAO,
        EXUMACAO,
        ENTERRO;
        public double getPreco() {
            switch (this) {
                case COMPRA:
                    return Jazigo.precoJazigo;
                case ALUGUEL:
                    return Jazigo.aluguelJazigo;
                case PERSONALIZACAO:
                    PlanoEnum[] valores = PlanoEnum.values();
                    
                    for (PlanoEnum tipo : valores) {
                        switch(tipo) {
                            case BASIC:
                                return PlanoEnum.BASIC.getPreco();
                            case SILVER:
                                return PlanoEnum.SILVER.getPreco();
                            case GOLD:
                                return PlanoEnum.GOLD.getPreco();
                        }
                    }
                 
                case MANUTENCAO:
                    return manutencao;
                case EXUMACAO:
                    return exumacao;
                case ENTERRO:
                    return enterro;
                default:
                    return 0.0;
            }
        }

        public void setPreco(Double valor) {
            switch (this) {
                case COMPRA:
                    Jazigo.precoJazigo = valor;
                case ALUGUEL:
                    Jazigo.aluguelJazigo = valor;
                case PERSONALIZACAO:
                    PlanoEnum[] valores = PlanoEnum.values();
                    
                    for (PlanoEnum tipo : valores) {
                        switch(tipo) {
                            case BASIC:
                                PlanoEnum.BASIC.setPreco(valor);
                            case SILVER:
                                PlanoEnum.SILVER.setPreco(valor);
                            case GOLD:
                                PlanoEnum.GOLD.setPreco(valor);
                        }
                    }
                 
                case MANUTENCAO:
                    manutencao = valor;
                case EXUMACAO:
                    exumacao = valor;
                case ENTERRO:
                    enterro = valor;
            }

        }
    }

    public Servico(){}
    
    public Servico(ServicoEnum tipoServico, double valor, Cliente cliente, Jazigo jazigo, PlanoEnum plano, Pet pet, LocalDate dataServico, LocalTime horaServico) {
        this.tipoServico = tipoServico;
        if(plano != null){
            this.valor = valor + plano.getPreco(); //* O valor do servico vai englobar o valor do jazigo + o valor do seu plano (caso seja compra ou aluguel, senão esses valores serão null) */
        } else {
            this.valor = valor;
        }
        this.cliente = cliente;
        this.jazigo = jazigo;
        this.plano = plano;
        this.pet = pet;
        this.dataServico = dataServico;
        this.horaServico = horaServico;
        
    }

    

    public PlanoEnum getPlano(){
        return plano;
    }

    public Long getIdServico() {
        return idServico;
    }

    public ServicoEnum getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(ServicoEnum servico) {
        this.tipoServico = servico;
    }

    public Jazigo getJazigo() {
        return jazigo;
    }

    public void setJazigo(Jazigo jazigo) {
        this.jazigo = jazigo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
    
    public void setPet(Pet pet){
        this.pet = pet;
    }

    public Pet getPet(){
        return pet;
    }

    public LocalDate getDataServico() {
        return dataServico;
    }

    public void setDataServico(LocalDate dataServico) {
        this.dataServico = dataServico;
    }

    public LocalTime getHoraServico() {
        return horaServico;
    }

    public void setHoraServico(LocalTime horaServico) {
        this.horaServico = horaServico;
    }

    public void setPrimeiroPagamento(LocalDate primeiroPagamento) {
        this.primeiroPagamento = primeiroPagamento;
    }

    public LocalDate getPrimeiroPagamento() {
        return primeiroPagamento;
    }
}
