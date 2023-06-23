package com.petcemetery.petcemetery.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "carrinho")
@Table(name = "carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Adicionado para permitir auto incremento do id
    @Column(name = "id_carrinho")
    private long id;

    @Column(name = "cpf_cliente")
    private String cpfCliente;

    @ManyToOne
    @JoinColumn(name = "id_jazigo")
    private Jazigo jazigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "servico")
    private ServicoEnum servico;

    @Enumerated(EnumType.STRING)
    @Column(name = "plano")
    private PlanoEnum plano;

    @Column(name = "data_agendamento")
    private LocalDate dataAgendamento;

    @Column(name = "hora_agendamento")
    private LocalTime horaAgendamento;

    //atributo Pet de tipo Pet
    @OneToOne
    //@Column(name = "id_pet")
    private Pet pet;

    //Referencia o serviço que precisa ser pago
    @OneToOne
    //@Column(name = "id_servico")
    private Servico idServico;

    public Carrinho() {
    }

    public Carrinho(String cpfCliente, Jazigo jazigo, ServicoEnum servico, PlanoEnum plano, LocalDate dataAgendamento, LocalTime horaAgendamento, Pet pet, Servico idServico) {
        this.cpfCliente = cpfCliente;
        this.jazigo = jazigo;
        this.servico = servico;
        this.plano = plano;
        this.dataAgendamento = dataAgendamento;
        this.horaAgendamento = horaAgendamento;
        this.pet = pet;
        this.idServico = idServico;
    }

}
