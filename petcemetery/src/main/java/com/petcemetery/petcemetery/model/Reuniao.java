package com.petcemetery.petcemetery.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(name = "Reuniao")
@Table(name = "Reuniao")   
public class Reuniao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reuniao")
    private Integer idReuniao;

    @ManyToOne
    @JoinColumn(name = "cpf_cliente", referencedColumnName = "cpf")
    private Cliente cliente;

    @Temporal(TemporalType.DATE)
    @Column(name = "data")
    private LocalDate data;

    @Column(name = "assunto")
    private String assunto;

    @Column(name = "horario")
    private LocalTime horario;

    public Reuniao(Cliente cliente, LocalDate data, String assunto) {
        this.cliente = cliente;
        this.data = data;
        this.assunto = assunto;
    }

    public int getIdReuniao() {
        return idReuniao;
    }

    public void setIdReuniao(int idReuniao) {
        this.idReuniao = idReuniao;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getAssunto() {
        return this.assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public LocalTime getHorario() {
        return this.horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public void setHora(LocalTime hora) {
    }
}
