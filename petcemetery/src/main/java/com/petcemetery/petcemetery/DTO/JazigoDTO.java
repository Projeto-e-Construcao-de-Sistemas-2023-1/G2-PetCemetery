package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class JazigoDTO {
    String nomePet;
    private LocalDate dataEnterro;
    private String endereco;
    

    public JazigoDTO(String nomePet, LocalDate dataEnterro, String endereco) {
        this.nomePet = nomePet;
        this.dataEnterro = dataEnterro;
        this.endereco = endereco;
    }
}
