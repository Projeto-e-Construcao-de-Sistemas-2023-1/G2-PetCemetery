package com.petcemetery.petcemetery.model;

import java.util.ArrayList;
import java.util.List;

import com.petcemetery.petcemetery.model.Servico.PlanoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "Jazigo")
@Table(name = "Jazigo")
public class Jazigo {
    
    @Column(name = "endereco")
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "cpf_proprietario", referencedColumnName = "cpf")
    private Cliente proprietario;

    @OneToMany
    @Column(name = "historico_pets")
    private List<Pet> historicoPets;

    @Id
    @Column(name = "id_jazigo")
    private long idJazigo;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "disponivel")
    private Boolean disponivel;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "foto")
    private String foto;

    @Column(name = "notas")
    private String notas;

    @Column(name = "plano")
    @Enumerated(EnumType.STRING)
    private PlanoEnum plano; 

    @OneToOne
    @JoinColumn(name = "pet_enterrado_id", referencedColumnName = "id_pet")
    private Pet petEnterrado;

    public enum StatusEnum {
        DISPONIVEL,
        OCUPADO
    }
    
    public Jazigo() {
    }

    public Jazigo(String endereco, Cliente proprietario, int idJazigo, StatusEnum status, Boolean disponivel,
            PlanoEnum plano) {
        this.endereco = endereco;
        this.proprietario = proprietario;
        this.idJazigo = idJazigo;
        this.status = status;
        this.disponivel = disponivel;
        this.plano = plano;
        this.historicoPets = new ArrayList<>();
    }

    public Jazigo(String endereco, Cliente proprietario, int idJazigo, StatusEnum status, boolean disponivel, 
                String mensagem, String foto, String notas, PlanoEnum plano, Pet petEnterrado) {
        this.endereco = endereco;
        this.proprietario = proprietario;
        this.idJazigo = idJazigo;
        this.status = status;
        this.disponivel = disponivel;
        this.mensagem = mensagem;
        this.foto = foto;
        this.notas = notas;
        this.plano = plano;
        this.petEnterrado = petEnterrado;
        this.historicoPets = new ArrayList<>();
    }

    public List<Pet> getHistoricoPets() {
        return historicoPets;
    }
    public void setHistoricoPets(List<Pet> historicoPets) {
        this.historicoPets = historicoPets;
    }
    public void addPetHistorico(Pet pet) {
        this.historicoPets.add(pet);
    }
    public void removePetHistorico(Pet pet) {
        this.historicoPets.remove(pet);
    }
}