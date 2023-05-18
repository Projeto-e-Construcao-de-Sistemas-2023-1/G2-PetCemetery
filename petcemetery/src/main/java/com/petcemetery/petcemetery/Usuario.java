package com.petcemetery.petcemetery;

public class Usuario {
    private String email;
    private long telefone;
    private String nome;
    private long cpf;
    private String cep;
    private boolean admin;
    private String rua;
    private int numero;
    private String complemento;
    
    public Usuario(String email, long telefone, String nome, long cpf, boolean admin) {
        this.email = email;
        this.telefone = telefone;
        this.nome = nome;
        this.cpf = cpf;
        this.admin = admin;
    }

    public Usuario(String email, long telefone, String nome, long cpf, String cep, boolean admin, String rua,
            int numero, String complemento) {
        this.email = email;
        this.telefone = telefone;
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.admin = admin;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelefone() {
        return telefone;
    }

    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getCpf() {
        return cpf;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    



    
}
