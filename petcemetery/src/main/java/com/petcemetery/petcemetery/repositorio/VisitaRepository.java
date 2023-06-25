package com.petcemetery.petcemetery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petcemetery.petcemetery.model.Visita;

public interface VisitaRepository extends JpaRepository<Visita, Long>{
    @Query("SELECT v FROM Visita v")
    List<Visita> findAll();
}
