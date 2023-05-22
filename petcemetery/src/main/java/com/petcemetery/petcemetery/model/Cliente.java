package com.petcemetery.petcemetery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity(name = "Cliente")
@Table(name = "Cliente")
@NoArgsConstructor
public class Cliente extends Usuario{

    @Column(name = "quant_jazigos")
    private int quantJazigos;

    public Cliente(String email, String telefone, String nome, String cpf, String senha) {
        super(email, telefone, nome, cpf, false, senha);
        this.quantJazigos = 0;
    }
    public Cliente(String email, String telefone, String nome, String cpf, String cep, String rua, int numero, String complemento, String senha) {
        super(email, telefone, nome, cpf, cep, false, rua, numero, complemento, senha);
        this.quantJazigos = 0;
    }

    public int getQuantJazigos() {
        return quantJazigos;
    }
    public void setQuantJazigos(int quantJazigos) {
        this.quantJazigos = quantJazigos;
    }

     

    
}
