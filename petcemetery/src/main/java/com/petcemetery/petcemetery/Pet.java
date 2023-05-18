package com.petcemetery.petcemetery;

import java.sql.Date;

public class Pet {
    private long id;
    private String nomePet;
    private Date dataEnterro;
    private Date dataNascimento;
    private String especie;
    public Pet(long id, String nomePet, String especie) {
        this.id = id;
        this.nomePet = nomePet;
        this.especie = especie;
    }
    public Pet(long id, String nomePet, Date dataNascimento, String especie) {
        this.id = id;
        this.nomePet = nomePet;
        this.dataNascimento = dataNascimento;
        this.especie = especie;
    }
    public Pet(long id, String nomePet, Date dataEnterro, Date dataNascimento, String especie) {
        this.id = id;
        this.nomePet = nomePet;
        this.dataEnterro = dataEnterro;
        this.dataNascimento = dataNascimento;
        this.especie = especie;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNomePet() {
        return nomePet;
    }
    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }
    public Date getDataEnterro() {
        return dataEnterro;
    }
    public void setDataEnterro(Date dataEnterro) {
        this.dataEnterro = dataEnterro;
    }
    public Date getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getEspecie() {
        return especie;
    }
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    
}
