package com.petcemetery.petcemetery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.DTO.ExibirServicoDTO;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

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

    // Igual ao do cliente, porém o admin não vai conseguir selecionar um jazigo (Isso tem que ser mudado no front?).
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

    // Retorna o valor de todos os servicos em formato JSON
    @GetMapping("/servicos")
    public ResponseEntity<?> exibirServicos() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ExibirServicoDTO()); // redireciona para a página de serviços
    }

    @PostMapping("/servicos/alterar")
    public ResponseEntity<?> alterarServicos(@RequestParam String servico, @RequestParam double valor) {
        // Atualiza o valor do plano com base no seu nome
        if(servico.equals("BASIC") || servico.equals("SILVER") || servico.equals("GOLD")) {
            System.out.println("isso é um plano");
            PlanoEnum planoEnum = PlanoEnum.valueOf(servico);
            if(planoEnum != null) {
                planoEnum.setPreco(valor);
                System.out.println(planoEnum.getPreco());
            }
        } else {
            System.out.println("isso é um servico");
            // Atualiza o valor do serviço com base no seu nome
            ServicoEnum servicoEnum = ServicoEnum.valueOf(servico);
            if (servicoEnum != null) {
                servicoEnum.setPreco(valor);
            }
        }
        return ResponseEntity.ok("OK;servico_alterado;"); // Exibe uma mensagem de servico alterado
    }
    
}
