package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;

public class PetDTO {
    String nome;
    LocalDate dataNascimento;
    String especie;

    public PetDTO(String nome, LocalDate dataNascimento, String especie) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.especie = especie;
    }

    public PetDTO() {
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nomePet) {
        this.nome = nomePet;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especiePet) {
        this.especie = especiePet;
    }
}
