package com.petcemetery.petcemetery;

public class Servico {
    
    private int idServico;
    private String servico;
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
