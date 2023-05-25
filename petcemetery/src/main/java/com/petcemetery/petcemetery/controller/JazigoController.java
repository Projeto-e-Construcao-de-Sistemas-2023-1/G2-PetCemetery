package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JazigoController {

    @Autowired
    private JazigoRepository jazigoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    // Envia para o front quais jazigos estão disponíveis, para exibir o mapa de visualização de jazigos - FUNCIONANDO
    @GetMapping("/jazigos_disponiveis")
    public ResponseEntity<?> jazigoDisponivel() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAll()) {
            str = str + String.valueOf(i.getDisponivel()) + ";";
        }
    
        System.out.println(str);
        return ResponseEntity.ok(str);  // Retorne a String de jazigos disponiveis 
    }

    // Envia para o front todos os jazigos do proprietário, para ser exibido na home do cliente - FUNCIONANDO
    @GetMapping("/{cpf_proprietario}/meus_jazigos")
    public ResponseEntity<?> jazigosInfo(@PathVariable("cpf_proprietario") String cpf_proprietario) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jazigoRepository.findByProprietarioCpf(cpf_proprietario));
    }
    
    // Envia para o front o endereco do jazigo selecionado, o id dele e o preço de compra, para ser exibido na tela antes da compra do ornamento - FUNCIONANDO
    @GetMapping("/{cpf}/comprar_jazigo/{id}")
    public ResponseEntity<?> comprarJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        return ResponseEntity.ok("OK;" + cpf + ";" + id + ";" + jazigo.getEndereco() + ";" + String.valueOf(Jazigo.precoJazigo)); // Retorna o cpf do cliente, endereco do jazigo, id do jazigo e o seu preço
    }

    // Envia para o front os precos dos planos atuais do sistema, para ser exibido na tela de seleção de planos - FUNCIONANDO
    @GetMapping("/{cpf}/comprar_jazigo/{id}/listar_planos")
    public ResponseEntity<?> listarPlanos(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" +  cpf + ";" + String.valueOf(id) + ";" + String.valueOf(Servico.basic) + ";" + String.valueOf(Servico.silver) + ";" + String.valueOf(Servico.gold));
    }

    // Recebe os valores do plano que o cliente selecionou, e então adiciona tanto o jazigo quanto o plano ao carrinho e salva no banco - FUNCIONANDO
    @PostMapping("/{cpf}/comprar_jazigo/{id}/listar_planos/plano")
    public ResponseEntity<?> finalizarCompra(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            // Pegando o plano selecionado 
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());

            Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
            Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

            // Setando e salvando o carrinho no banco de dados.
            jazigo.setPlano(plano);    
            carrinho.setJazigo(jazigo);
            carrinho.setTotalCarrinho(carrinho.getTotalCarrinho() + plano.getPreco() + Jazigo.precoJazigo);
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;" + cpf + ";" + String.valueOf(id));
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }

    }

    // Retorna todas as informações do carrinho para o front, na hora de finalizar a compra - FUNCIONANDO
    @GetMapping("/{cpf}/comprar_jazigo/{id}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        // Retorna todas as informações do carrinho para o front
        return ResponseEntity.ok("OK;" + cpf + ";" + String.valueOf(id) + ";" + jazigo.getEndereco() + ";Compra permanente;" + 
                                String.valueOf(Jazigo.precoJazigo) + ";" + String.valueOf(jazigo.getPlano()) + ";Personalização;" + 
                                String.valueOf(jazigo.getPlano().getPreco()) + ";" + "Total:" + ";" + String.valueOf(jazigo.getPlano().getPreco() + Jazigo.precoJazigo));
    }

    // Quando o cliente finaliza a compra, o carrinho do cliente é limpado - FUNCIONANDO
    @PostMapping("/{cpf}/comprar_jazigo/{id}/informacoes_carrinho/finalizar")
    public ResponseEntity<?> realizarPagamento(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();

            // Atualiza o jazigo
            jazigo.setDisponivel(false);
            
            // Salva as alterações no banco de dados
            jazigoRepository.save(jazigo);

            // Limpa o carrinho
            carrinho.limparCarrinho();
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }
}