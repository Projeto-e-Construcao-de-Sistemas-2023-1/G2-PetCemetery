package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;

import jakarta.websocket.Decoder.Text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private Servico servico;

    // Envia para o front quais jazigos estão disponíveis, para exibir o mapa de visualização de jazigos
    @GetMapping("/jazigos_disponiveis")
    public ResponseEntity<?> jazigoDisponivel() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAll()) {
            str = String.valueOf(i.isDisponivel()) + ";";
        }
    
        System.out.println(str);
        return ResponseEntity.ok(str);  // Retorne a String de jazigos disponiveis 
    }

    // Envia para o front todos os jazigos do proprietário, para ser exibido na home do cliente
    @GetMapping("/{cpf_proprietario}/meus_jazigos")
    public ResponseEntity<List<Jazigo>> jazigosInfo(@PathVariable("cpf_proprietario") String cpf_proprietario) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jazigoRepository.findByProprietarioCpf(cpf_proprietario));
    }
    
    // Envia para o front o endereco do jazigo selecionado, o id dele e o preço de compra, para ser exibido na tela antes da compra do ornamento
    @GetMapping("/{cpf}/comprar_jazigo/{id}")
    public ResponseEntity<?> comprarJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" + cpf + ";" + String.valueOf(id) + ";" + jazigoRepository.findByIdJazigo(id).getEndereco() + ";" + String.valueOf(Jazigo.precoJazigo)); // Retorna o cpf do cliente, endereco do jazigo, id do jazigo e o seu preço
    }

    // Envia para o front os precos dos planos atuais do sistema, para ser exibido na tela de seleção de planos
    @GetMapping("/{cpf}/comprar_jazigo/{id}/listar_planos")
    public ResponseEntity<?> listarPlanos(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" +  cpf + ";" + String.valueOf(id) + ";" + String.valueOf(Servico.basic) + ";" + String.valueOf(Servico.silver) + ";" + String.valueOf(Servico.gold));
    }

    @PostMapping("/{cpf}/comprar_jazigo/{id}/listar_planos/plano")
    public ResponseEntity<?> finalizarCompra(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado) {
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();

            // Pegando o plano selecionado
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());

            // Pegando o carrinho correspondente ao cliente atual
            Carrinho carrinho = carrinhoRepository.findByCpf(cpf);

            // Atualizando o plano do jazigo para o plano selecionado pelo cliente
            jazigo.setPlano(plano);

            // Atualizando os valores do carrinho
            carrinho.setJazigo(jazigo);
            carrinho.setTotalCarrinho(Jazigo.precoJazigo + plano.getPreco());

            // Salvando no banco de dados
            jazigoRepository.save(jazigo);
            carrinhoRepository.save(carrinho);
        }

    }

    @GetMapping("/{cpf}/comprar_jazigo/{id}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);
        Carrinho carrinho = carrinhoRepository.findByCpf(cpf);

        return ResponseEntity.ok("OK;" + cpf + ";" + String.valueOf(id) + ";" + jazigo.getEndereco() + ";Compra permanente;" + 
                                String.valueOf(Jazigo.precoJazigo) + ";" + String.valueOf(jazigo.getPlano()) + ";Personalização;" + 
                                String.valueOf(jazigo.getPlano().getPreco()) + ";" + "Total:" + ";" + String.valueOf(carrinho.getTotalCarrinho()));
    }
    
    
    // Recebe do front o plano selecionado pelo cliente, e adiciona no carrinho o jazigo com o plano selecionado, salvando no banco de dados.
    @PostMapping("/{cpf}/comprar_jazigo/{id}/listar_planos/plano")
    public ResponseEntity<?> finalizarCompr(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();

            // Pegando o plano selecionado
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());

            // Pegando o carrinho correspondente ao cliente atual
            Carrinho carrinho = carrinhoRepository.findByCpf(cpf);

            // Atualizando o plano do jazigo para o plano selecionado pelo cliente
            jazigo.setPlano(plano);

            // Atualizando os valores do carrinho
            carrinho.setJazigo(jazigo);
            carrinho.setTotalCarrinho(Jazigo.precoJazigo + plano.getPreco());

            // Salvando no banco de dados
            jazigoRepository.save(jazigo);
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;" + cpf + ";" + String.valueOf(id) + ";" + jazigo.getEndereco() + "; Compra permanente;" + String.valueOf(Jazigo.precoJazigo) + ";" + String.valueOf(planoSelecionado) + "; Personalização;" + String.valueOf(plano.getPreco()) );
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
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