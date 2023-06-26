package com.petcemetery.petcemetery.repositorio;

import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Jazigo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Carrinho findByCpfCliente(String cpfCliente);
    List<Carrinho> findAllByCpfCliente(String cpfCliente);
    Carrinho findByJazigo(Jazigo jazigo);

    void deleteByJazigo(Jazigo jazigo); 
    List<Carrinho> deleteAllByCpfCliente(String cpfCliente);
    Carrinho deleteByIdServico(long idServico);
}