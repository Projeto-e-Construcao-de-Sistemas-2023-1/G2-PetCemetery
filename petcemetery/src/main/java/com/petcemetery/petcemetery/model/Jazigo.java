package com.petcemetery.petcemetery.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
    private int idJazigo;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "data_ultima_visita")
    @Temporal(TemporalType.DATE)
    private Date dataUltimaVisita;

    @Column(name = "disponivel")
    private boolean disponivel;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "foto")
    private boolean foto;

    @Column(name = "notas")
    private String notas;

    @Column(name = "plano")
    @Enumerated(EnumType.STRING)
    private PlanoEnum plano; 

    @Column(name = "preco")
    private static final float PRECO_FIXO = 30000.0f;
    

    
    @OneToOne
    @JoinColumn(name = "pet_enterrado_id", referencedColumnName = "id_pet")
    private Pet petEnterrado;

    

    public enum PlanoEnum {
        BASIC,
        SILVER,
        GOLD
    }

    public enum StatusEnum {
        DISPONÍVEL,
        OCUPADO
    }

    // Construtor sem argumentos
    public Jazigo() {
    }

    public Jazigo(String endereco, Cliente proprietario, int idJazigo, StatusEnum status, boolean disponivel,
            PlanoEnum plano) {
        this.endereco = endereco;
        this.proprietario = proprietario;
        this.idJazigo = idJazigo;
        this.status = status;
        this.disponivel = disponivel;
        this.plano = plano;
    }

    public Jazigo(String endereco, Cliente proprietario, int idJazigo, StatusEnum status, Date dataUltimaVisita,
            boolean disponivel, String mensagem, boolean foto, String notas, PlanoEnum plano, Pet petEnterrado) {
        this.endereco = endereco;
        this.proprietario = proprietario;
        this.idJazigo = idJazigo;
        this.status = status;
        this.dataUltimaVisita = dataUltimaVisita;
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
    public int getIdJazigo() {
        return idJazigo;
    }
    public void setIdJazigo(int idJazigo) {
        this.idJazigo = idJazigo;
    }
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }
    public Date getDataUltimaVisita() {
        return dataUltimaVisita;
    }
    public void setDataUltimaVisita(Date dataUltimaVisita) {
        this.dataUltimaVisita = dataUltimaVisita;
    }
    public boolean isDisponivel() {
        return disponivel;
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public boolean isFoto() {
        return foto;
    }
    public void setFoto(boolean foto) {
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
    
    public static float getPrecoFixo() {
        return PRECO_FIXO;
    }

}