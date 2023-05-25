package com.petcemetery.petcemetery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Carrinho {
    
    @Id
    @Column(name = "cpf_cliente")
    private String cpfCliente;

    @OneToOne
    @JoinColumn(name = "jazigo", referencedColumnName="id_jazigo")
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

    public void setJazigo(Jazigo jazigo) {
        this.jazigo = jazigo;
    }

    public void limparCarrinho() {
        this.jazigo = null;
        this.totalCarrinho = 0.0;
    }

    public void removerItem() {
        this.jazigo = null;
        this.totalCarrinho = 0.0;
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
