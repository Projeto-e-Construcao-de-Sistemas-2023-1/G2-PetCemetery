package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class JazigoDTO {
    String nomePet;
    private LocalDate dataEnterro;
    private String endereco;
    private Long idJazigo;
    

    public JazigoDTO(String nomePet, LocalDate dataEnterro, String endereco, Long idJazigo) {
        this.nomePet = nomePet;
        this.dataEnterro = dataEnterro;
        this.endereco = endereco;
        this.idJazigo = idJazigo;
    }
}
