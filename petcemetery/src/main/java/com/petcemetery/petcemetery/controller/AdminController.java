package com.petcemetery.petcemetery.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.DTO.ExibirServicoDTO;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Servico;
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
    public ResponseEntity<?> alterarServicos(@RequestBody Map<String, Double> servicos) {
        for (Map.Entry<String, Double> entry : servicos.entrySet()) {
            String nomeServico = entry.getKey().toUpperCase();
            Double novoValor = entry.getValue();
    
            System.out.println("cheguei aqui");
            System.out.println(nomeServico + " " + novoValor);
            System.out.println(nomeServico == "GOLD");

            // Atualiza o valor do plano com base no seu nome
            if(nomeServico.equals("BASIC") || nomeServico.equals("SILVER") || nomeServico.equals("GOLD")) {

                System.out.println("isso é um plano");
                PlanoEnum planoEnum = PlanoEnum.valueOf(nomeServico);
                if(planoEnum != null) {
                    planoEnum.setPreco(novoValor);
                }

            } else {

                System.out.println("isso é um servico");

                // Atualiza o valor do serviço com base no seu nome
                ServicoEnum servicoEnum = ServicoEnum.valueOf(nomeServico);
                if (servicoEnum != null) {
                    servicoEnum.setPreco(novoValor);
                }
            }
        }

        return ResponseEntity.ok("OK;servico_alterado;"); // Exibe uma mensagem de servico alterado
    }
    
}
