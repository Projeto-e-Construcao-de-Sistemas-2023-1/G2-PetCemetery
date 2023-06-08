package com.petcemetery.petcemetery.DTO;


import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Pet;

import lombok.Data;

@Data
public class DetalharJazigoDTO{
    String nomePet;
    String dataNascimento;
    String especiePet;
    String nomeProrietario;
    String endereçoJazigo;
    String dataUltimaVisita;
    String dataEnterro;
    String ornamentacao;
    String mensagemLapide;
    String urlImagem;


    public DetalharJazigoDTO(Pet pet, Jazigo jazigo){
 
        this.nomePet = pet.getNomePet();

        if(pet.getDataNascimento() != null) {
            this.dataNascimento = pet.getDataNascimento().toString();
        } else {
            this.dataNascimento = null;
        }

        this.especiePet = pet.getEspecie();
        this.nomeProrietario = jazigo.getProprietario().getNome();
        this.endereçoJazigo = jazigo.getEndereco();
        
        if(jazigo.getDataUltimaVisita() != null) {
            this.dataUltimaVisita = jazigo.getDataUltimaVisita().toString();
        } else {
            this.dataUltimaVisita = null;
        }
        
        if(pet.getDataEnterro() != null) { 
            this.dataEnterro = pet.getDataEnterro().toString();
        } else {
            this.dataEnterro = null;
        }

        if(jazigo.getPlano() != null) {
            this.ornamentacao = jazigo.getPlano().toString();
        }else {
            this.ornamentacao = null;
        }
        
        this.mensagemLapide = jazigo.getMensagem();
        this.urlImagem = jazigo.getFoto();
    }

}