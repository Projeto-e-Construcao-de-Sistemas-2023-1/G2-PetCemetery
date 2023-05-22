package com.petcemetery.petcemetery.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "Pagamento")
@Table(name = "Pagamento")
public class Pagamento {
    
    @Id
    @Column(name = "id_pagamento")
    private Integer idPagamento;

    @ManyToOne
    @JoinColumn(name = "cpf", referencedColumnName = "cpf")
    private Cliente cliente; 

    @Column(name = "valor")
    private float valor;

    @Column(name = "data_pagamento")
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    @Column(name = "data_vencimento")
    @Temporal(TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "isPago")
    private boolean pago;
    
    @Column(name = "id_servico")
    private int idServico;

    @Column(name = "metodo_pagamento")
    @Enumerated(EnumType.STRING)
    private MetodoEnum metodoPagamento;
    
    public enum MetodoEnum{
        CREDITO,
        DEBITO,
        PAYPAL
    }

    public Pagamento(Cliente cliente, float valor, Date dataPagamento, Date dataVencimento, boolean pago, int idServico,
            MetodoEnum metodoPagamento) {
        this.cliente = cliente;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.dataVencimento = dataVencimento;
        this.pago = pago;
        this.idServico = idServico;
        this.metodoPagamento = metodoPagamento;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
    public MetodoEnum getMetodoPagamento() {
        return metodoPagamento;
    }
    public void setMetodoPagamento(MetodoEnum metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    

}
