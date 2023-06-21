 package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.DTO.DetalharJazigoDTO;
import com.petcemetery.petcemetery.DTO.JazigoDTO;
import com.petcemetery.petcemetery.DTO.ServicoDTO;
import com.petcemetery.petcemetery.DTO.VisualizarDespesasDTO;
import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Pet;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.PetRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Autowired
    private PetRepository petRepository;

    // Envia para o front quais jazigos estão disponíveis, para exibir o mapa de visualização de jazigos - FUNCIONANDO
    @GetMapping("/{cpf}/jazigos_disponiveis")
    public ResponseEntity<?> jazigoDisponivel() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAllOrderByIdAsc()) {
            str = str + String.valueOf(i.getDisponivel()) + (i == jazigoRepository.findAllOrderByIdAsc().get(jazigoRepository.findAllOrderByIdAsc().size() - 1) ? "" : ";");
        }        
    
        System.out.println(str); //nota do lucas: tirei o OK antes da resposta e o ; do ultimo elemento
        return ResponseEntity.ok(str);  // Retorne a String de jazigos disponiveis 
    }

    // Envia para o front uma lista dos jazigos do proprietário, contendo o nome do pet e a data do enterro, ou "Vazio" e null caso não tenha pet enterrado.
    @GetMapping("/{cpf_proprietario}/meus_jazigos")
    public ResponseEntity<List<JazigoDTO>> jazigosInfo(@PathVariable("cpf_proprietario") String cpf_proprietario) {
        List<Jazigo> listaJazigos = jazigoRepository.findByProprietarioCpf(cpf_proprietario); 

        List<JazigoDTO> listaJazigosDTO = new ArrayList<>();

        for (Jazigo jazigo : listaJazigos) {
            JazigoDTO jazigoDTO;
            if(jazigo.getPetEnterrado() == null) {
                jazigoDTO = new JazigoDTO("Vazio", null, jazigo.getEndereco(), jazigo.getIdJazigo(), null, "Sem Espécie", jazigo.getMensagem());
            } else {
                jazigoDTO = new JazigoDTO(jazigo.getPetEnterrado().getNomePet(), jazigo.getPetEnterrado().getDataEnterro(), jazigo.getEndereco(), jazigo.getIdJazigo(), jazigo.getPetEnterrado().getDataNascimento(), jazigo.getPetEnterrado().getEspecie(), jazigo.getMensagem());
            }
            listaJazigosDTO.add(jazigoDTO);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listaJazigosDTO);
    }


    // Envia para o front o endereco do jazigo selecionado, o id dele e o preço de compra, para ser exibido na tela antes da compra do ornamento - FUNCIONANDO
    @GetMapping("/{cpf}/adquirir_jazigo/{id}")
    public ResponseEntity<?> comprarJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("tipo") String tipo) {
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        if(tipo.equals("compra")){
            return ResponseEntity.ok("OK;" + jazigo.getEndereco() + ";" + String.valueOf(Jazigo.precoJazigo)); // Retorna o cpf do cliente, endereco do jazigo, id do jazigo e o seu preço
        }
        
        return ResponseEntity.ok("OK;" + jazigo.getEndereco() + ";" + String.valueOf(Jazigo.aluguelJazigo));

    }

    // Envia para o front os precos dos planos atuais do sistema, para ser exibido na tela de seleção de planos - FUNCIONANDO
    @GetMapping("/{cpf}/adquirir_jazigo/{id}/listar_planos")
    public ResponseEntity<?> listarPlanos(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" + String.valueOf(PlanoEnum.BASIC) + ";" + PlanoEnum.BASIC.getPreco() + ";" 
                                       + String.valueOf(PlanoEnum.SILVER) + ";" + PlanoEnum.SILVER.getPreco() + ";"
                                       + String.valueOf(PlanoEnum.GOLD) + ";" + PlanoEnum.GOLD.getPreco());
    }

    //adiciona no carrinho o jazigo selecionado pelo cliente
    @PostMapping("/{cpf}/adquirir_jazigo/{id}/listar_planos/plano") //tipo == COMPRA ou ALUGUEL
    public ResponseEntity<?> finalizarCompra(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado, @RequestParam("tipo") String tipo) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            
            // Pegando o plano selecionado 
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());
            Jazigo jazigo = optionalJazigo.get();
            
            for(Carrinho carrinhoItem : carrinhoRepository.findAllByCpfCliente(cpf)) { 
                if(carrinhoItem.getJazigo().getIdJazigo() == id) {
                    return ResponseEntity.ok("ERR;jazigo_ja_adicionado");
                }
            }

            //adiciona e seta no carrinho do cliente o servico
            Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.valueOf(tipo.toUpperCase()), plano, null, null, null);
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }
    
    // Retorna a mensagem e a foto atual para serem exibidas no front quando o usuário quiser alterar as informações do jazigo
    // Tem que ver a foto ainda
    @GetMapping("/{cpf}/informacoes_jazigo/{id}")
    public ResponseEntity<?> exibirMensagemFotoJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();
            if(jazigo.getProprietario().equals(clienteRepository.findByCpf(cpf))){
                return ResponseEntity.ok("OK;" + jazigo.getMensagem() + ";" + jazigo.getFoto());
            } else {
                return ResponseEntity.ok("ERR;cliente_nao_proprietario");
            }
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }

    //falta implementar no front - FUNCIONANDO
    //edita só a mensagem do jazigo, nao sei a situação da foto ainda
    @PostMapping("/{cpf}/informacoes_jazigo/{id}/editar_jazigo")
    public ResponseEntity<?> editarMensagemFotoJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestBody String mensagem) {
    if (mensagem.length() > 80) {
        return ResponseEntity.ok("ERR;mensagem_maior_que_80_caracteres");
    }

    Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);

    if (optionalJazigo.isPresent()) {
        Jazigo jazigo = optionalJazigo.get();

        if (jazigo.getProprietario().equals(clienteRepository.findByCpf(cpf))) {
            jazigo.setMensagem(mensagem);
            jazigoRepository.save(jazigo);
            return ResponseEntity.ok("OK;Mensagem_editada");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_pertence_usuario");
        }
    } else {
        return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
    }
}   

    // Recebe a data e hora do enterro e também os dados do pet a ser enterrado.
    // Cria um novo pet e um novo servico de enterro e adiciona no carrinho.
    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_enterro")
    public ResponseEntity<?> agendarEnterro(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data, @RequestParam("hora") String hora, @RequestParam("nomePet") String nomePet, @RequestParam("especie") String especie, @RequestParam("dataNascimento") String dataNascimento) {
        
        Jazigo jazigo = jazigoRepository.findById(id).get();
        
        Pet pet = new Pet(nomePet, LocalDate.parse(dataNascimento), especie, clienteRepository.findByCpf(cpf));
        petRepository.save(pet); //! o pet é setado no banco mesmo q o kra nao pague o enterro e n prossiga c nada, vao ter pets setados sem estar no cemiterio

        //TODO resolver oq vai acontecer com isso aqui
        // Servico enterroServico = new Servico(ServicoEnum.ENTERRO, ServicoEnum.ENTERRO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, LocalDate.parse(data), LocalTime.parse(hora));
        // servicoRepository.save(enterroServico);

        Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.ENTERRO, jazigo.getPlano(), LocalDate.parse(data), LocalTime.parse(hora), pet);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok("OK;enterro_no_carrinho");
    }


    // Recebe os parâmetros data (yyyy-mm-dd) e hora (hh-mm) da exumacao, no formato correto, e salva no banco
    // Não estamos utilizando o cpf pra nada :D - utiliza sim, p saber se o jazigo é do kra ou nao
    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_exumacao")
    public ResponseEntity<?> agendarExumacao(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data, @RequestParam("hora") String hora) {
        Jazigo jazigo = jazigoRepository.findById(id).get();
        Pet pet = jazigo.getPetEnterrado();

        //TODO resolver oq vai acontecer com isso aqui
        // Servico exumacao = new Servico(ServicoEnum.EXUMACAO, ServicoEnum.EXUMACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, LocalDate.parse(data), LocalTime.parse(hora));
        // servicoRepository.save(exumacao);

        Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.EXUMACAO, jazigo.getPlano(), LocalDate.parse(data), LocalTime.parse(hora), pet);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok("OK;exumacao_no_carrinho");
    }

    // Retorna o valor atual do preço de enterro para ser exibido na tela de pagamento
    @GetMapping("/{cpf}/meus_jazigos/{id}/agendar_enterro/preco")
    public ResponseEntity<?> precoEnterro() {
        return ResponseEntity.ok("OK;" + Servico.ServicoEnum.ENTERRO.getPreco());
    }

    // Retorna o valor atual do preço de exumacao para ser exibido na tela de pagamento
    @GetMapping("/{cpf}/meus_jazigos/{id}/agendar_exumacao/preco")
    public ResponseEntity<?> precoExumacao() {
        return ResponseEntity.ok("OK;" + Servico.ServicoEnum.EXUMACAO.getPreco());
    }

    //retorna os detalhes do jazigo especificado para ser exibido na tela de visualizar detalhes de jazifo
    @GetMapping("/{cpf}/meus_jazigos/{id}/detalhar_jazigo")
    public ResponseEntity<?> detalharJazigo(@PathVariable("id") Long id){
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        if(jazigo.getPetEnterrado() == null) {
            return ResponseEntity.ok("OK;vazio");
        }

        DetalharJazigoDTO detalharJazigoDTO = new DetalharJazigoDTO(jazigo.getPetEnterrado(), jazigo);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(detalharJazigoDTO);
    }
    
    @GetMapping("/{cpf}/meus_jazigos/{id}/agendar_manutencao/preco")
    public ResponseEntity<?> precoManutencao() {
        return ResponseEntity.ok("OK;" + Servico.ServicoEnum.MANUTENCAO.getPreco());
    }

    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_manutencao")
    public ResponseEntity<?> agendarManutencao(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data) {
        Jazigo jazigo = jazigoRepository.findById(id).get();

        //TODO resolver oq vai acontecer com isso aqui
        // Servico manutencaoServico = new Servico(ServicoEnum.MANUTENCAO, ServicoEnum.MANUTENCAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), null, LocalDate.parse(data), null);
        // servicoRepository.save(manutencaoServico);

        Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.MANUTENCAO, jazigo.getPlano(), LocalDate.parse(data), null, null);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok("OK;manutencao_agendada");
    }
    
    //metodo post que cria e salva um carrinho com o servico PERSONALIZACAO, que troca o plano do jazigo
    @PostMapping("/{cpf}/meus_jazigos/{id}/trocar_plano")
    public ResponseEntity<?> trocarPlano (@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("tipo") String tipo){

        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        
        if(!optionalJazigo.isPresent()){
            return ResponseEntity.ok("OK;Jazigo_nao_encontrado");
        } else {

            Jazigo jazigo = optionalJazigo.get();

            Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.PERSONALIZACAO, PlanoEnum.valueOf(tipo), null, null, null);
            carrinhoRepository.save(carrinho);

            return ResponseEntity.ok("OK;troca_de_plano_no_carrinho");
        }

    }
    
    
}
