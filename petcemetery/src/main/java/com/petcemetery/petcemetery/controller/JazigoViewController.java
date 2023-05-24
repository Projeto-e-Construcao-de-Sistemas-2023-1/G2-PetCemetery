package com.petcemetery.petcemetery.controller;


import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.service.JazigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JazigoViewController {

    private final JazigoService jazigoService;

    @Autowired
    public JazigoViewController(JazigoService jazigoService) {
        this.jazigoService = jazigoService;
    }

    @GetMapping("/jazigo/{id}")
    public Jazigo getJazigoById(@PathVariable("id") Long id, Model model) {
        Jazigo jazigo = jazigoService.getJazigoById(id);
        model.addAttribute("jazigo", jazigo);
        return jazigo; // Nome do template HTML para exibir as informações do jazigo
    }
}
