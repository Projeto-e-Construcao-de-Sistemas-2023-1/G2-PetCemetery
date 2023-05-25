package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.model.Jazigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JazigoViewController {

    @Autowired
    JazigoRepository jazigoRepository;

    @GetMapping("/jazigo/{id}")
    public Jazigo getJazigoById(@PathVariable("id") Long id, Model model) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);
        model.addAttribute("jazigo", jazigo);
        return jazigo; // Nome do template HTML para exibir as informações do jazigo
    }
}
