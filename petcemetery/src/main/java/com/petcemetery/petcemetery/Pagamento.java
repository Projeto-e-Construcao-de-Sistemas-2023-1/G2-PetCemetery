package com.petcemetery.petcemetery;

import java.sql.Date;

public class Pagamento {
    
    private long cpf; //** IMPORTANTE! -> nao acham q aqui devia ser o tipo cliente? e daí temos acesso ao cpf e outros?
    private float valor;
    private Date dataPagamento;
    private Date dataVencimento;
    private boolean pago;
    private int idServico;
    
    private METODOS metodoPagamento;
    public enum METODOS{
        CREDITO,
        DEBITO,
        PAYPAL
    }
    public Pagamento(long cpf, float valor, Date dataPagamento, Date dataVencimento, boolean pago, int idServico,
            METODOS metodoPagamento) {
        this.cpf = cpf;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.dataVencimento = dataVencimento;
        this.pago = pago;
        this.idServico = idServico;
        this.metodoPagamento = metodoPagamento;
    }
    public long getCpf() {
        return cpf;
    }
    public void setCpf(long cpf) {
        this.cpf = cpf;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public Date getDataPagamento() {
        return dataPagamento;
    }
    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    public Date getDataVencimento() {
        return dataVencimento;
    }
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    public boolean isPago() {
        return pago;
    }
    public void setPago(boolean pago) {
        this.pago = pago;
    }
    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }
    public METODOS getMetodoPagamento() {
        return metodoPagamento;
    }
    public void setMetodoPagamento(METODOS metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    

}
