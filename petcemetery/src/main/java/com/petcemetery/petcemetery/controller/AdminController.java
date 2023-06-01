package com.petcemetery.petcemetery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.repositorio.AdminRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;

@RestController
@RequestMapping("/api/admin/{cpf}")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JazigoRepository jazigoRepository;

    // Retorna o jazigo em formato JSON com o id passado, ou uma mensagem de erro caso o id seja inválido
    @GetMapping("/detalhar-jazigo/{id}")
    public ResponseEntity<?> detalharJazigo(@PathVariable("id") Long id) {
        if(id < 1 || id > 72) {
            return ResponseEntity.ok("ERR;id_invalido;"); // Exibe uma mensagem de id inválido
        }
        
        Jazigo jazigo = jazigoRepository.findById(id).get();

        return ResponseEntity.ok(jazigo); 
    }

    // Igual ao do cliente, porém o admin não vai conseguir selecionar um jazigo (Isso tem que ser mudado no front).
    @GetMapping("/mapa-jazigos")
    public ResponseEntity<?> mapaJazigos() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAll()) {
            str = str + String.valueOf(i.getDisponivel()) + ";";
        }
    
        System.out.println(str);
        return ResponseEntity.ok("OK;" + str);  // Retorne a String de jazigos disponiveis 
    }
    
}
