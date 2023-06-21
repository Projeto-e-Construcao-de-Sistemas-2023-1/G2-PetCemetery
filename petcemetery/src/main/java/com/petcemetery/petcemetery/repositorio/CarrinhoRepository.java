package com.petcemetery.petcemetery.repositorio;

import com.petcemetery.petcemetery.model.Carrinho;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Carrinho findByCpfCliente(String cpfCliente);
    List<Carrinho> findAllByCpfCliente(String cpfCliente);    

    Carrinho deleteByJazigo(long id_jazigo);
    List<Carrinho> deleteAllByCpfCliente(String cpfCliente);
}