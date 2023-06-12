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
import lombok.Data;

@Data
@Entity(name = "Pet")
@Table(name = "Pet")
public class Pet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proprietario_cpf", referencedColumnName = "cpf")
    private Cliente proprietario;
    
    @Column(name = "nome_pet")
    private String nomePet;
    
    @Column(name = "data_enterro")
    private LocalDate dataEnterro;

    @Column(name = "hora_enterro")
    private LocalTime horaEnterro;

    @Column(name = "data_exumacao")
    private LocalDate dataExumacao;

    @Column(name = "hora_exumacao")
    private LocalTime horaExumacao;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "especie")
    private String especie;
    
    public Pet() {
        // Construtor padrão sem argumentos
    }
    public Pet(String nomePet, String especie, Cliente proprietario) {
        this.nomePet = nomePet;
        this.especie = especie;
        this.proprietario = proprietario;
    }
    public Pet(String nomePet, LocalDate dataNascimento, String especie, Cliente proprietario) {
        this.nomePet = nomePet;
        this.dataNascimento = dataNascimento;
        this.especie = especie;
        this.proprietario = proprietario;
    }
    public Pet(LocalDate data, LocalTime hora, Cliente proprietario) {
        this.dataEnterro = data;
        this.horaEnterro = hora;
        this.proprietario = proprietario;
    }
    public Pet(String nomePet, LocalDate dataEnterro, LocalTime horaEnterro, LocalDate dataNascimento, String especie, Cliente proprietario) {
        this.nomePet = nomePet;
        this.dataEnterro = dataEnterro;
        this.dataNascimento = dataNascimento;
        this.especie = especie;
        this.horaEnterro = horaEnterro;
        this.proprietario = proprietario;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomePet() {
        return nomePet;
    }
    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }
    public LocalDate getDataEnterro() {
        return dataEnterro;
    }
    public void setDataEnterro(LocalDate dataEnterro) {
        this.dataEnterro = dataEnterro;
    }
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getEspecie() {
        return especie;
    }
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    public LocalTime getHoraEnterro() {
        return horaEnterro;
    }
    public void setHoraEnterro(LocalTime horaEnterro) {
        this.horaEnterro = horaEnterro;
    }
    public Cliente getProprietario() {
        return proprietario;
    }
    public void setProprietario(Cliente proprietario) {
        this.proprietario = proprietario;
    }

    
}
