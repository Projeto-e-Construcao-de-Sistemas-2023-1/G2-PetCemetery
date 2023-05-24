package com.petcemetery.petcemetery.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Carrinho {
    
    private List<Jazigo> itens;
    private double totalCarrinho;

    public Carrinho() {
        this.itens = new ArrayList<>();
        this.totalCarrinho = 0.0;
    }

    public void adicionarItem(Jazigo jazigo, Jazigo.PlanoEnum plano) {
        double valorItem = Jazigo.precoJazigo + plano.getPreco();
        this.itens.add(jazigo);
        this.totalCarrinho += valorItem;
    }

    public void removerItem(Jazigo jazigo) {
        this.itens.remove(jazigo);
        double valorItem = Jazigo.precoJazigo + jazigo.getPlano().getPreco();
        this.totalCarrinho -= valorItem;
    }

    public double getTotalCarrinho() {
        return this.totalCarrinho;
    }

    public List<Jazigo> getItens() {
        return this.itens;
    }
}
