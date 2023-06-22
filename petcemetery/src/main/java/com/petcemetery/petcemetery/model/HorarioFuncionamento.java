package com.petcemetery.petcemetery.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "horario_funcionamento")
@Entity(name = "horario_funcionamento")
@Data
public class HorarioFuncionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "abertura")
    private LocalTime abertura;

    @Column(name = "fechamento")
    private LocalTime fechamento;

    @Column(name = "fechado")
    private boolean fechado;
}
