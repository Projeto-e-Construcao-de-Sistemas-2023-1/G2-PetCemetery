package com.petcemetery.petcemetery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity(name = "Cliente")
@Table(name = "Cliente")
@NoArgsConstructor
public class Cliente extends Usuario{

    @Column(name = "pagamento_pendente")
    private boolean pagamentoPendente = false;

    @Column(name = "quant_jazigos")
    private int quantJazigos;


    public Cliente(String email, String telefone, String nome, String cpf, String senha) {
        super(email, telefone, nome, cpf, false, senha);
        this.quantJazigos = 0;
    }
    public Cliente(String email, String telefone, String nome, String cpf, String cep, String rua, int numero, String complemento, String senha) {
        super(email, telefone, nome, cpf, cep, false, rua, numero, complemento, senha);
        this.pagamentoPendente = false;
        this.quantJazigos = 0;
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
