package com.petcemetery.petcemetery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "Cliente")
@Table(name = "Cliente")
public class Cliente extends Usuario{

    @Column(name = "pagamento_pendente")
    private boolean pagamentoPendente = false;

    @Column(name = "quant_jazigos")
    private int quantJazigos;
    
    public Cliente(String email, long telefone, String nome, long cpf, boolean admin, int quantJazigos) {
        super(email, telefone, nome, cpf, false);
        this.quantJazigos = quantJazigos;
    }
    public Cliente(String email, long telefone, String nome, long cpf, String cep, boolean admin, String rua, int numero, String complemento, boolean pagamentoPendente, int quantJazigos) {
        super(email, telefone, nome, cpf, cep, admin, rua, numero, complemento);
        this.pagamentoPendente = pagamentoPendente;
        this.quantJazigos = quantJazigos;
    }
    public boolean isPagamentoPendente() {
        return pagamentoPendente;
    }
    public void setPagamentoPendente(boolean pagamentoPendente) {
        this.pagamentoPendente = pagamentoPendente;
    }
    public int getQuantJazigos() {
        return quantJazigos;
    }
    public void setQuantJazigos(int quantJazigos) {
        this.quantJazigos = quantJazigos;
    }

     

    
}
