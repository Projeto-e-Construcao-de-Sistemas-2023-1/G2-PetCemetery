package com.petcemetery.petcemetery.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "Jazigo")
@Table(name = "Jazigo")
public class Jazigo {
    
    @Column(name = "endereco")
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "cpf_proprietario", referencedColumnName = "cpf")
    private Cliente proprietario;

    @Id
    @Column(name = "id_jazigo")
    private long idJazigo;

    @Transient
    public static double precoJazigo = 30000;

    @Transient
    public static double aluguelJazigo = 555.99;
    
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

    @ManyToOne
    private List<Pet> historicoPets = new ArrayList<>();

    @Transient
    public static double precoBasico = 1.0;

    @Transient
    public static double precoSilver = 50.0;

    @Transient
    public static double precoGold = 80.0;

    public enum PlanoEnum {
        BASIC(precoBasico),
        SILVER(precoSilver),
        GOLD(precoGold);
    
        private double preco;
    
        PlanoEnum(double preco) {
            this.preco = preco;
        }
    
        public double getPreco() {
            return preco;
        }
    
        public void setPreco(double preco) {
            this.preco = preco;
        }
    }

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
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public Cliente getProprietario() {
        return proprietario;
    }
    public void setProprietario(Cliente proprietario) {
        this.proprietario = proprietario;
    }
    public long getIdJazigo() {
        return idJazigo;
    }
    public void setIdJazigo(long idJazigo) {
        this.idJazigo = idJazigo;
    }
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }
    public Boolean getDisponivel() {
        return disponivel;
    }
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getNotas() {
        return notas;
    }
    public void setNotas(String notas) {
        this.notas = notas;
    }
    public Pet getPetEnterrado() {
        return petEnterrado;
    }
    public void setPetEnterrado(Pet petEnterrado) {
        this.petEnterrado = petEnterrado;
    }
    public PlanoEnum getPlano() {
        return plano;
    }
    public void setPlano(PlanoEnum plano) {
        this.plano = plano;
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