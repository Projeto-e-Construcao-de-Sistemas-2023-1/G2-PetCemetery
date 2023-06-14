package com.petcemetery.petcemetery.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity(name = "Cliente")
@Table(name = "Cliente")
@NoArgsConstructor
public class Cliente extends Usuario{

    @Column(name = "quant_jazigos")
    private int quantJazigos;

    @Column(name = "desativado")
    private Boolean desativado;

     @OneToMany(mappedBy = "cliente")
     private List<Pagamento> pagamentos; 

    public Cliente(String email, String telefone, String nome, String cpf, String senha) {
        super(email, telefone, nome, cpf, false, senha);
        this.quantJazigos = 0;
        this.desativado = false;
    }
    public Cliente(String email, String telefone, String nome, String cpf, String cep, String rua, String numero, String complemento, String senha) {
        super(email, telefone, nome, cpf, cep, false, rua, numero, complemento, senha);
        this.quantJazigos = 0;
        this.desativado = false;
    }

    public int getQuantJazigos() {
        return quantJazigos;
    }
    
    public void setQuantJazigos(int quantJazigos) {
        this.quantJazigos = quantJazigos;
    }

    public Boolean getDesativado() {
        return this.desativado;
    }

    public void setDesativado(Boolean desativado) {
        this.desativado = desativado;
    }
    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
    public void addPagamento(Pagamento pagamento) {
        this.pagamentos.add(pagamento);
    }
    public void removePagamento(Pagamento pagamento) {
        this.pagamentos.remove(pagamento);
    }
    
}
