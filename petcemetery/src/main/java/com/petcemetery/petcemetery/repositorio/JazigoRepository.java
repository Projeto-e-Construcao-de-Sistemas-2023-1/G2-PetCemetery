package com.petcemetery.petcemetery.repositorio;

import com.petcemetery.petcemetery.model.Jazigo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JazigoRepository extends JpaRepository<Jazigo, Long> {
    Jazigo findByIdJazigo(Long id_jazigo);
    List<Jazigo> findByProprietarioCpf(String cpf_proprietario);
}