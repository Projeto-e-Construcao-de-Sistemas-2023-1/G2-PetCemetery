package com.petcemetery.petcemetery;

import java.sql.Date;

public class Jazigo {
    
    private String endereco;
    private Cliente proprietario;
    private int idJazigo;
    private String status;
    private Date dataUltimaVisita;
    private boolean disponivel;
    private String mensagem;
    private boolean foto;
    private String notas;
    private PLANO plano; 
    private Pet petEnterrado;

    public enum PLANO{
        BASIC,
        SILVER,
        GOLD
    }

    public Jazigo(String endereco, Cliente proprietario, int idJazigo, String status, boolean disponivel,
            PLANO plano) {
        this.endereco = endereco;
        this.proprietario = proprietario;
        this.idJazigo = idJazigo;
        this.status = status;
        this.disponivel = disponivel;
        this.plano = plano;
    }
    public Jazigo(String endereco, Cliente proprietario, int idJazigo, String status, Date dataUltimaVisita,
            boolean disponivel, String mensagem, boolean foto, String notas, PLANO plano, Pet petEnterrado) {
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
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
    public PLANO getPlano() {
        return plano;
    }
    public void setPlano(PLANO plano) {
        this.plano = plano;
    }

    
    

}
