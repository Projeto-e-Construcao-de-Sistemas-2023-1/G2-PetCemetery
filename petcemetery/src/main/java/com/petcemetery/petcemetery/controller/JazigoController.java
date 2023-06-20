 package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.DTO.DetalharJazigoDTO;
import com.petcemetery.petcemetery.DTO.JazigoDTO;
import com.petcemetery.petcemetery.DTO.ServicoDTO;
import com.petcemetery.petcemetery.DTO.VisualizarDespesasDTO;
import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Pet;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
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

    @PostMapping("/{cpf}/adquirir_jazigo/{id}/listar_planos/plano") //tipo ==
    public ResponseEntity<?> finalizarCompra(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("planoSelecionado") String planoSelecionado, @RequestParam("tipo") String tipo) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        if (optionalJazigo.isPresent()) {
            
            // Pegando o plano selecionado 
            PlanoEnum plano = PlanoEnum.valueOf(planoSelecionado.toUpperCase());
            Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
            Jazigo jazigo = jazigoRepository.findByIdJazigo(id);
            Cliente cliente = clienteRepository.findByCpf(cpf);
            Servico servico;

            if(tipo.equals("compra")){
                servico = new Servico(ServicoEnum.COMPRA, Jazigo.precoJazigo, cliente, jazigo, plano, null, null, null);
            } else {
                servico = new Servico(ServicoEnum.ALUGUEL, Jazigo.aluguelJazigo, cliente, jazigo, plano, null, null ,null);
            }
            
            for(Carrinho carrinhos : carrinhoRepository.findAllByCpfCliente(cpf)) { 
                if(carrinhos.getJazigo().getIdJazigo() == id) {
                    return ResponseEntity.ok("ERR;jazigo_ja_adicionado");
                }
            }

            //adiciona e seta no carrinho do cliente o servico
            servicoRepository.save(servico);
            carrinho.adicionarServico(servico);
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

    //todo falta implementar no front - FUNCIONANDO
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



    //Retorna array de servicos em json do cliente passado - FUNCIONANDO
    @GetMapping("/{cpf}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf) {
        List<ServicoDTO> listaServicosDTO = new ArrayList<>();

        for (Carrinho item : carrinhoRepository.findAllByCpfCliente(cpf)) {
            
            ServicoDTO servicoDTO = new ServicoDTO(servico.getValor(), item.getServico(), item.getJazigo().getEndereco(), item.getPlano(), null);

            listaServicosDTO.add(servicoDTO);
        }

        //retorna um json com array de DTO servicos
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listaServicosDTO);

    }
    
    

    

    // Recebe a data e hora do enterro e também os dados do pet a ser enterrado.
    // Cria um novo pet e um novo servico de enterro e adiciona no carrinho.
    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_enterro")
    public ResponseEntity<?> agendarEnterro(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data, @RequestParam("hora") String hora, @RequestParam("nomePet") String nomePet, @RequestParam("especie") String especie, @RequestParam("dataNascimento") String dataNascimento) {
        Jazigo jazigo = jazigoRepository.findById(id).get();
        
        Pet pet = new Pet(nomePet, LocalDate.parse(dataNascimento), especie, clienteRepository.findByCpf(cpf));
        petRepository.save(pet);

        Servico enterroServico = new Servico(ServicoEnum.ENTERRO, ServicoEnum.ENTERRO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, LocalDate.parse(data), LocalTime.parse(hora));
        servicoRepository.save(enterroServico);

        Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.ENTERRO, jazigo.getPlano());
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok("OK;");
    }

   
    // Recebe os parâmetros data (yyyy-mm-dd) e hora (hh-mm) da exumacao, no formato correto, e salva no banco
    // Não estamos utilizando o cpf pra nada :D - utiliza sim, p saber se o jazigo é do kra ou nao
    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_exumacao")
    public ResponseEntity<?> agendarExumacao(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data, @RequestParam("hora") String hora) {
        Jazigo jazigo = jazigoRepository.findById(id).get();
        Pet pet = jazigo.getPetEnterrado();

        Servico exumacao = new Servico(ServicoEnum.EXUMACAO, ServicoEnum.EXUMACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, LocalDate.parse(data), LocalTime.parse(hora));
        servicoRepository.save(exumacao);

        Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.EXUMACAO, jazigo.getPlano());
        carrinhoRepository.save(carrinho);
        return ResponseEntity.ok("OK;");
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

        Servico manutencaoServico = new Servico(ServicoEnum.MANUTENCAO, ServicoEnum.MANUTENCAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), null, LocalDate.parse(data), null);
        servicoRepository.save(manutencaoServico);

        Carrinho carrinho = new Carrinho(cpf, jazigo, ServicoEnum.MANUTENCAO, jazigo.getPlano());
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok("OK;manutencao_agendada");
    }
    
    //retorna as informaçoes necessárias para visualizar as despesas de cada cpf na tela visualizar despesas
    //talvez seja melhor mover para o cliente contorller e modificar a url prefixada??
    @GetMapping("/{cpf}/visualizar_despesas")
    public ResponseEntity<?>visualizarDespesas(@PathVariable("cpf") String cpf){
        List<Servico> servicos = servicoRepository.findBycliente_cpf(cpf);
        List <VisualizarDespesasDTO> despesasDTO = new ArrayList<>();
    
        for (Servico s : servicos){
            VisualizarDespesasDTO despesaDTO = new VisualizarDespesasDTO(s);
            despesasDTO.add(despesaDTO);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(despesasDTO);
    }
    
}
