package com.petcemetery.petcemetery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmailAndSenha(String email, String senha);
    Cliente findByEmail(String email);
    Cliente findByCpf(String cpf);
}