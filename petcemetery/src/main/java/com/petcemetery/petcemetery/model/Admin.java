package com.petcemetery.petcemetery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "Administrador")
@Table(name = "Administrador")
public class Admin extends Usuario{

    public Admin(String email, long telefone, String nome, long cpf, boolean admin) {
        super(email, telefone, nome, cpf, true);
        
    }

    public Admin(String email, long telefone, String nome, long cpf, String cep, boolean admin, String rua, int numero,
            String complemento) {
        super(email, telefone, nome, cpf, cep, admin, rua, numero, complemento);
        
    }
    

}
