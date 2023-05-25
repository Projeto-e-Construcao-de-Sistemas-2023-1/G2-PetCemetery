package com.petcemetery.petcemetery.model;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;

@Entity(name = "Servico")
@Table(name = "Servico")
public class Servico {
    
    @Id
    @Column(name = "id_servico")
    private int idServico;

    @Column(name = "servico")
    @Enumerated(EnumType.STRING)
    private ServicoEnum servico;

    @Transient
    private static double manutencao = 1300.0; 

    @Transient 
    private static double exumacao = 200.0;

    @Transient 
    private static double enterro = 400.0;

    @Transient 
    public static double basic = 1.0;

    @Transient 
    public static double silver = 50.0;

    @Transient 
    public static double gold = 80.0;

    @Column(name = "valor")
    private float valor;

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
    }
    
    public Servico(int idServico, ServicoEnum servico, float valor) {
        this.idServico = idServico;
        this.servico = servico;
        this.valor = valor;
    }
    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }
    public ServicoEnum getServico() {
        return servico;
    }
    public void setServico(ServicoEnum servico) {
        this.servico = servico;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }

    
}
