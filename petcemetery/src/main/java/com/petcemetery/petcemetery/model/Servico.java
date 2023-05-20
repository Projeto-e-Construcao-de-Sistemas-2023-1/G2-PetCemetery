package com.petcemetery.petcemetery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Servico")
@Table(name = "Servico")
public class Servico {
    
    @Id
    @Column(name = "id_servico")
    private int idServico;

    @Column(name = "servico")
    private String servico;

    @Column(name = "valor")
    private float valor;
    
    public Servico(int idServico, String servico, float valor) {
        this.idServico = idServico;
        this.servico = servico;
        this.valor = valor;
    }
    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }
    public String getServico() {
        return servico;
    }
    public void setServico(String servico) {
        this.servico = servico;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }

    
}
