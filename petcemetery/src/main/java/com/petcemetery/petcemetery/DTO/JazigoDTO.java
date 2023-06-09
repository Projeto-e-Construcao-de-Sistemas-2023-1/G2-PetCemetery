package com.petcemetery.petcemetery.DTO;

import java.util.Date;

public class JazigoDTO {
    String nomePet;
    private Date dataEnterro;
    private String endereco;
    

    public JazigoDTO(String nomePet, Date dataEnterro, String endereco) {
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

    public Date getDataEnterro() {
        return this.dataEnterro;
    }

    public void setDataEnterro(Date dataEnterro) {
        this.dataEnterro = dataEnterro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
