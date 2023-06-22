package com.petcemetery.petcemetery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.HorarioFuncionamento;

public interface HorarioFuncionamentoRepository extends JpaRepository<HorarioFuncionamento, Long>{
    HorarioFuncionamento findByDiaSemana(String diaSemana);
}
