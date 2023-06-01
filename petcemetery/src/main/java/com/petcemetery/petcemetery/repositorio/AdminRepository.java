package com.petcemetery.petcemetery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcemetery.petcemetery.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmailAndSenha(String email, String senha);
    Admin findByEmail(String email);

}