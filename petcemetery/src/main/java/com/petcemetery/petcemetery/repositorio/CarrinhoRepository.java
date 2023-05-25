package com.petcemetery.petcemetery.repositorio;

import com.petcemetery.petcemetery.model.Carrinho;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Carrinho findByCpf(String cpfCliente);
}