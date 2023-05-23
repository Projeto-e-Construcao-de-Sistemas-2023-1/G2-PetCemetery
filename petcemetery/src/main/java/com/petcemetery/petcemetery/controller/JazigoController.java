package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class JazigoController {

    private final JazigoRepository jazigoRepository;

    public JazigoController(JazigoRepository jazigoRepository) {
        this.jazigoRepository = jazigoRepository;
    }

    @GetMapping("/jazigo")
    public String jazigo(Model model) {
        List<Jazigo> jazigos = jazigoRepository.findAll();  // Busque todos os jazigos do banco de dados
        model.addAttribute("jazigos", jazigos);  // Adicione a lista de jazigos ao modelo
        return "jazigo";  // Retorne a página jazigo.html
    }

    @GetMapping("/comprar/{id}")
public String comprarJazigo(@PathVariable("id") Long id, Model model) {
    Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
    if (optionalJazigo.isPresent()) {
        Jazigo jazigo = optionalJazigo.get();
        model.addAttribute("jazigo", jazigo);
        return "comprarJazigo";
    } else {
        return "redirect:/jazigo";
    }
}

}
