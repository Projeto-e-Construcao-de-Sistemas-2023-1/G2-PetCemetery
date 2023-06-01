package com.petcemetery.petcemetery.DTO;

import java.util.Date;


public class JazigoDTO {
    String nomePet;
    private Date dataEnterro;
    

    public JazigoDTO(String nomePet, Date dataEnterro) {
        this.nomePet = nomePet;
        this.dataEnterro = dataEnterro;
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


}
