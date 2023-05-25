package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// Resto do seu código
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
@Autowired
private Carrinho carrinho; // Supondo que você tenha uma instância de Carrinho injetada no controlador

@GetMapping("/comprar/{id}/plano")
public String selecionarPlano(@PathVariable("id") Long id, Model model) {
    Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
    if (optionalJazigo.isPresent()) {
        Jazigo jazigo = optionalJazigo.get();
        model.addAttribute("jazigo", jazigo);
        model.addAttribute("planos", Jazigo.PlanoEnum.values());
        model.addAttribute("totalCarrinho", carrinho.getTotalCarrinho()); // Adiciona o valor total do carrinho ao modelo
        return "confirmarPagamento";
    } else {
        return "redirect:/jazigo";
    }
}
@PostMapping("/comprar/{id}/plano/planof")
public String finalizarCompra(@PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado, Model model) {
    Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
    if (optionalJazigo.isPresent()) {
        Jazigo jazigo = optionalJazigo.get();
        
        // Pegando o plano selecionado
        PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());
        
        // Adicionando o jazigo ao carrinho com o plano selecionado
        carrinho.adicionarItem(jazigo, plano);
        jazigo.setPlano(plano);
        jazigo = jazigoRepository.save(jazigo);
        model.addAttribute("jazigo", jazigo);
        model.addAttribute("plano", plano);
        
        return "redirect:/comprar/{id}/plano/finalizar";
    } else {
        return "redirect:/comprar/{id}/plano";
    }
}

// Este método apenas exibe a página de finalização do pagamento com as informações do jazigo e do carrinho.
@GetMapping("/comprar/{id}/plano/finalizar")
public String exibirFinalizarPagamento(@PathVariable("id") Long id, Model model) {
    Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
    if (optionalJazigo.isPresent()) {
        Jazigo jazigo = optionalJazigo.get();
        model.addAttribute("jazigo", jazigo);
        model.addAttribute("carrinho", carrinho);
        return "finalizarPagamento";
    } else {
        return "redirect:/jazigo";
    }
}

@PostMapping("/comprar/{id}/plano/finalizar/limpar")
public String realizarPagamento(@PathVariable("id") Long id, Model model) {
    Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
    if (optionalJazigo.isPresent()) {
        Jazigo jazigo = optionalJazigo.get();
        
        // Atualiza o jazigo
        jazigo.setDisponivel(false);
        jazigo.setPlano(jazigo.getPlano()); // assumindo que o carrinho tem o plano que foi selecionado
        
        jazigo.setStatus(StatusEnum.OCUPADO);
        // Salva as alterações no banco de dados
        jazigoRepository.save(jazigo);
        
        // Limpa o carrinho
        carrinho.limparCarrinho();
        
        return "redirect:/jazigo";
    } else {
        return "redirect:/jazigo";
    }
}
}
