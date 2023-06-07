package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.DTO.JazigoDTO;
import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    private ClienteRepository clienteRepository;

    // Envia para o front quais jazigos estão disponíveis, para exibir o mapa de visualização de jazigos - FUNCIONANDO
    @GetMapping("/{cpf}/jazigos_disponiveis")
    public ResponseEntity<?> jazigoDisponivel() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAll()) {
            str = str + String.valueOf(i.getDisponivel()) + ";";
        }
    
        System.out.println(str);
        return ResponseEntity.ok("OK;" + str);  // Retorne a String de jazigos disponiveis 
    }

    // Envia para o front uma lista dos jazigos do proprietário, contendo o nome do pet e a data do enterro, ou "Vazio" caso não tenha pet enterrado.
    @GetMapping("/{cpf_proprietario}/meus_jazigos")
    public ResponseEntity<List<JazigoDTO>> jazigosInfo(@PathVariable("cpf_proprietario") String cpf_proprietario) {
        List<Jazigo> listaJazigos = jazigoRepository.findByProprietarioCpf(cpf_proprietario); 

        List<JazigoDTO> listaJazigosDTO = new ArrayList<>();

        for (Jazigo jazigo : listaJazigos) {
            JazigoDTO jazigoDTO;
            if(jazigo.getPetEnterrado() == null) {
                jazigoDTO = new JazigoDTO("Vazio", null);
            } else {
                jazigoDTO = new JazigoDTO(jazigo.getPetEnterrado().getNomePet(), jazigo.getPetEnterrado().getDataEnterro());
            }
            listaJazigosDTO.add(jazigoDTO);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listaJazigosDTO);
    }
    
    // Envia para o front o endereco do jazigo selecionado, o id dele e o preço de compra, para ser exibido na tela antes da compra do ornamento - FUNCIONANDO
    @GetMapping("/{cpf}/comprar_jazigo/{id}")
    public ResponseEntity<?> comprarJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        return ResponseEntity.ok("OK;" + jazigo.getEndereco() + ";" + String.valueOf(Jazigo.precoJazigo)); // Retorna o cpf do cliente, endereco do jazigo, id do jazigo e o seu preço
    }

    //todo Envia para o front o endereco do jazigo selecionado, o id dele e o preço de aluguel, para ser exibido na tela antes da compra do ornamento - TESTAR!
    @GetMapping("/{cpf}/alugar_jazigo/{id}")
    public ResponseEntity<?> alugarJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        return ResponseEntity.ok("OK;" + jazigo.getEndereco() + ";" + String.valueOf(Jazigo.aluguelJazigo)); // Retorna o cpf do cliente, endereco do jazigo, id do jazigo e o seu preço de aluguel
    }

    // Envia para o front os precos dos planos atuais do sistema, para ser exibido na tela de seleção de planos - FUNCIONANDO
    @GetMapping("/{cpf}/comprar_jazigo/{id}/listar_planos")
    public ResponseEntity<?> listarPlanos(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" + String.valueOf(PlanoEnum.BASIC) + ";" + String.valueOf(PlanoEnum.SILVER) + ";" + String.valueOf(PlanoEnum.GOLD));
    }

    // Envia para o front os precos dos planos atuais do sistema, para ser exibido na tela de seleção de planos para o jazigo alugado - FUNCIONANDO
     @GetMapping("/{cpf}/alugar_jazigo/{id}/listar_planos")
     public ResponseEntity<?> listarPlanosAlugar(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" + String.valueOf(PlanoEnum.BASIC) + ";" + String.valueOf(PlanoEnum.SILVER) + ";" + String.valueOf(PlanoEnum.GOLD));
     }

    //TODO METODO CRIADO NO REFATORAMENTO DE CARRINHO
    @PostMapping("/{cpf}/comprar_jazigo/{id}/listar_planos/plano")
    public ResponseEntity<?> finalizarCompra(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            // Pegando o plano selecionado 
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());
            Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
            Jazigo jazigo = jazigoRepository.findByIdJazigo(id);
            Cliente cliente = clienteRepository.findByCpf(cpf);

            //criando o servico
            Servico servico = new Servico(ServicoEnum.COMPRA, Jazigo.precoJazigo, cliente, jazigo, plano);

            //adiciona e seta no carrinho do cliente o servico
            servicoRepository.save(servico);
            carrinho.adicionarServico(servico);
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }

    //todo Recebe os valores do plano que o cliente selecionou, e então adiciona tanto o jazigo quanto o plano ao carrinho e salva no banco - TESTAR!
    @PostMapping("/{cpf}/alugar_jazigo/{id}/listar_planos/plano")
    public ResponseEntity<?> finalizarAluguel(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            // Pegando o plano selecionado 
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());
            Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
            Jazigo jazigo = jazigoRepository.findByIdJazigo(id);
            Cliente cliente = clienteRepository.findByCpf(cpf);

            //criando o servico
            Servico servico = new Servico(ServicoEnum.ALUGUEL, Jazigo.aluguelJazigo, cliente, jazigo, plano);

            //adiciona e seta no carrinho do cliente o servico
            servicoRepository.save(servico);
            carrinho.adicionarServico(servico);
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }

    }

    //Retorna array de servicos em json do cliente passado - FUNCIONANDO
    @GetMapping("/{cpf}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf) {
        
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);

        //retorna um json com array de servicos
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(carrinho.getServicos()); //TODO IMPLEMENTAR O DTO SERVICO

    }

    
    //Ao finalizar e comprar tudo do carrinho, se algum servico for alguel ou compra, seta o jazigo no banco.
    //limpa o carrinho e salva no banco
    @PostMapping("/{cpf}/informacoes_carrinho/finalizar")
    public ResponseEntity<?> realizarPagamento(@PathVariable("cpf") String cpf) {
        
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
        
        if (carrinho != null) {
            
            for (Servico servico : carrinho.getServicos()) {
                if(servico.getTipoServico().equals(ServicoEnum.COMPRA) || servico.getTipoServico().equals(ServicoEnum.ALUGUEL)){
                    Jazigo jazigo = servico.getJazigo();
                    Cliente cliente = clienteRepository.findByCpf(cpf);

                    jazigo.setDisponivel(false);
                    jazigo.setPlano(servico.getPlano());
                    jazigo.setProprietario(cliente);
                    jazigo.setStatus(StatusEnum.DISPONIVEL);
                    jazigoRepository.save(jazigo);
                }
            }

            carrinho.limparCarrinho();
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;carrinho_nao_encontrado");
        }
    }

    //todo falta implementar no front - FUNCIONANDO
    //edita só a mensagem do jazigo, nao sei a situação da foto ainda
    @PostMapping("/{cpf}/editar_jazigo/{id}")
    public ResponseEntity<?> editarMensagemJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("mensagem") String mensagem) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();

            jazigo.setMensagem(mensagem);
            jazigoRepository.save(jazigo);

            return ResponseEntity.ok("OK;Mensagem_editada");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }

}