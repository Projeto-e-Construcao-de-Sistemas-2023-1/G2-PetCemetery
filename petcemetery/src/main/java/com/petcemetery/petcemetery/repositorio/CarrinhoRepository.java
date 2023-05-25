package com.petcemetery.petcemetery.repositorio;

import com.petcemetery.petcemetery.model.Carrinho;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Carrinho findByCpfCliente(String cpfCliente);
}