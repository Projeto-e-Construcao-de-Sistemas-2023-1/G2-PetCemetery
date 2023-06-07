package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

public class ServicoDTO {
    
    double valor;
    ServicoEnum tipoServico;
    String enderecoJazigo;
    PlanoEnum plano;
    
    public ServicoDTO() {}

    public ServicoDTO(double valor, ServicoEnum tipoServico, String enderecoJazigo, PlanoEnum plano) {
        this.valor = valor;
        this.tipoServico = tipoServico;
        this.enderecoJazigo = enderecoJazigo;
        this.plano = plano;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public ServicoEnum getTipoServico() {
        return tipoServico;
    }
    public void setTipoServico(ServicoEnum tipoServico) {
        this.tipoServico = tipoServico;
    }
    public String getEnderecoJazigo() {
        return enderecoJazigo;
    }
    public void setEnderecoJazigo(String enderecoJazigo) {
        this.enderecoJazigo = enderecoJazigo;
    }
    public PlanoEnum getPlano() {
        return plano;
    }
    public void setPlano(PlanoEnum plano) {
        this.plano = plano;
    }

    

}
