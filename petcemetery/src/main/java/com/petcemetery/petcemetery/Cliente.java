package com.petcemetery.petcemetery;

public class Cliente extends Usuario{

    private boolean pagamentoPendente = false;
    private int quantJazigos;
    
    public Cliente(String email, long telefone, String nome, long cpf, boolean admin, int quantJazigos) {
        super(email, telefone, nome, cpf, admin);
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
