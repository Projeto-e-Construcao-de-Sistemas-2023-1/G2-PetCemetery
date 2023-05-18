package com.petcemetery.petcemetery;

public class Admin extends Usuario{

    public Admin(String email, long telefone, String nome, long cpf, boolean admin) {
        super(email, telefone, nome, cpf, admin);
        
    }

    public Admin(String email, long telefone, String nome, long cpf, String cep, boolean admin, String rua, int numero,
            String complemento) {
        super(email, telefone, nome, cpf, cep, admin, rua, numero, complemento);
        
    }
    

}
