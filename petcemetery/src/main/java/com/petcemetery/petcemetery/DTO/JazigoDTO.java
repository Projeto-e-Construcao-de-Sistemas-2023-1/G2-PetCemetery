package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;

public class JazigoDTO {
    String nomePet;
    private LocalDate dataEnterro;
    private String endereco;
    

    public JazigoDTO(String nomePet, LocalDate dataEnterro, String endereco) {
        this.nomePet = nomePet;
        this.dataEnterro = dataEnterro;
        this.endereco = endereco;
    }

    public JazigoDTO() {
    }

    public String getNomePet() {
        return this.nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public LocalDate getDataEnterro() {
        return this.dataEnterro;
    }

    public void setDataEnterro(LocalDate dataEnterro) {
        this.dataEnterro = dataEnterro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
