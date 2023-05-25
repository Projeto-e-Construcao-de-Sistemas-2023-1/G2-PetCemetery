package com.petcemetery.petcemetery.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Component
public class Carrinho {
    
    @Id
    @Column(name = "cpf_cliente")
    private String cpfCliente;

    @Column(name = "jazigo")
    private Jazigo jazigo;

    @Column(name = "total_carrinho")
    private double totalCarrinho;

    public Carrinho() {}

    // Construtor de carrinho que deve ser chamado na criação de um cliente.
    public Carrinho(String cpfCliente) {
        this.cpfCliente = cpfCliente;
        this.jazigo = null;
        this.totalCarrinho = 0.0;
    }

    public void adicionarItem(Jazigo jazigo) {
        double valorTotal = Jazigo.precoJazigo + jazigo.getPlano().getPreco();
        this.jazigo = jazigo;
    }

    public void setJazigo(Jazigo jazigo) {
        this.jazigo = jazigo;
    }

    public void limparCarrinho() {
        this.jazigo = null;
        this.totalCarrinho = 0.0;
    }

    public void removerItem() {
        this.jazigo = null;
        double valorItem = Jazigo.precoJazigo + jazigo.getPlano().getPreco();
        this.totalCarrinho -= valorItem;
    }

    public double getTotalCarrinho() {
        return this.totalCarrinho;
    }

    public Jazigo getJazigo() {
        return this.jazigo;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setTotalCarrinho(double valor) {
        this.totalCarrinho = valor;
    }
}
