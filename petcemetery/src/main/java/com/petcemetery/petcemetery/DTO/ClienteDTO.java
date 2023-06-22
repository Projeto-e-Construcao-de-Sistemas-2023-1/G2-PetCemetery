package com.petcemetery.petcemetery.DTO;

import lombok.Data;

@Data
public class ClienteDTO {

    private String email;
    private String telefone;
    private String nome;
    private int quantJazigos;
    private Boolean desativado;
    private Boolean inadimplente;

    public ClienteDTO(String email, String telefone, String nome, int quantJazigos, Boolean desativado, Boolean inadimplente) {
        this.email = email;
        this.telefone = telefone;
        this.nome = nome;
        this.quantJazigos = quantJazigos;
        this.desativado = desativado;
        this.inadimplente = inadimplente;
    }
}
