package com.petcemetery.petcemetery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "Servicos")
@Table(name = "Servicos")
public class Servico {
    
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico")
    private ServicoEnum tipoServico;

    @Column(name = "valor")
    private double valor;

    public enum ServicoEnum {
        COMPRA,
        ALUGUEL,
        PERSONALIZACAO,
        MANUTENCAO,
        EXUMACAO,
        ENTERRO,
        BASIC,
        SILVER,
        GOLD;
    }

    public enum PlanoEnum {
        BASIC,
        SILVER,
        GOLD;
    }
}
