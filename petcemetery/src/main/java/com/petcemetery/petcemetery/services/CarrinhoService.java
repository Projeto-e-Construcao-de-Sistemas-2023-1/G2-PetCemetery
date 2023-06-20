package com.petcemetery.petcemetery.services;

import org.springframework.stereotype.Service;

import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;

import lombok.Data;

@Data
@Service
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;

    public void limparCarrinho(String cpfCliente) {
        carrinhoRepository.deleteAllByCpfCliente(cpfCliente);
    }

    public void removerServico(long id_jazigo) {
        carrinhoRepository.deleteByJazigo(id_jazigo);
    }
}