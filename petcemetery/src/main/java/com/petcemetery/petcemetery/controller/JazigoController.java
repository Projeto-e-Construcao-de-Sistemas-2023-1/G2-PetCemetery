package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


// Resto do seu código
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class JazigoController {

    @Autowired
    private JazigoRepository jazigoRepository;

    private Carrinho carrinho; // Supondo que você tenha uma instância de Carrinho injetada no controlador

    @GetMapping("/jazigos_disponiveis")
    public ResponseEntity<?> jazigoDisponivel() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAll()) {
            str = String.valueOf(i.isDisponivel()) + ";";
        }
    
        return ResponseEntity.ok(str);  // Retorne a String de jazigos disponiveis 
    }

    @GetMapping("/meus_jazigos/{cpf_proprietario}")
    public ResponseEntity<List<Jazigo>> jazigosInfo(@PathVariable("cpf_proprietario") String cpf_proprietario) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jazigoRepository.findByProprietarioCpf(cpf_proprietario));
    }

    @GetMapping("/comprar_jazigo/{id}")
    public ResponseEntity<?> comprarJazigo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(String.valueOf(id) + ";" + String.valueOf(Jazigo.precoJazigo)); // Retorna o id do jazigo e o seu preço
    }

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
