package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
public class JazigoController {

    @Autowired
    private JazigoRepository jazigoRepository;

    @GetMapping("/jazigos_disponiveis")
    public boolean[] jazigoDisponivel(Model model) {
        boolean jazigos[] = new boolean[72];
        int indice = 0;

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAll()) {
            jazigos[indice] = i.isDisponivel();
            indice++;
        }

        return jazigos;  // Retorne vetor de jazigos disponiveis 
    }

    @GetMapping("/meus_jazigos/{cpf_proprietario}")
    public ResponseEntity<List<Jazigo>> jazigosInfo(@PathVariable("cpf_proprietario") String cpf_proprietario) {
        System.out.println("CU e " + cpf_proprietario);
        List<Jazigo> jazzzz = jazigoRepository.findByProprietarioCpf(cpf_proprietario);
        System.out.println(jazzzz);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jazzzz);
    }

    @GetMapping("/comprar_jazigo/{id}")
    public ResponseEntity<?> comprarJazigo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(String.valueOf(id) + ";" + String.valueOf(Jazigo.getPrecoFixo())); // Retorna o id do jazigo e o seu preço
    }
}
