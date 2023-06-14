 package com.petcemetery.petcemetery.controller;

import com.petcemetery.petcemetery.DTO.DetalharJazigoDTO;
import com.petcemetery.petcemetery.DTO.JazigoDTO;
import com.petcemetery.petcemetery.DTO.PetDTO;
import com.petcemetery.petcemetery.DTO.ServicoDTO;
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
                jazigoDTO = new JazigoDTO("Vazio", null, jazigo.getEndereco());
            } else {
                jazigoDTO = new JazigoDTO(jazigo.getPetEnterrado().getNomePet(), jazigo.getPetEnterrado().getDataEnterro(), jazigo.getEndereco());
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
        return ResponseEntity.ok("OK;" + String.valueOf(PlanoEnum.BASIC) + ";" + PlanoEnum.BASIC.getPreco() + ";" 
                                       + String.valueOf(PlanoEnum.SILVER) + ";" + PlanoEnum.SILVER.getPreco() + ";"
                                       + String.valueOf(PlanoEnum.GOLD) + ";" + PlanoEnum.GOLD.getPreco());
    }

    // Envia para o front os precos dos planos atuais do sistema, para ser exibido na tela de seleção de planos para o jazigo alugado - FUNCIONANDO
     @GetMapping("/{cpf}/alugar_jazigo/{id}/listar_planos")
     public ResponseEntity<?> listarPlanosAlugar(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        return ResponseEntity.ok("OK;" + String.valueOf(PlanoEnum.BASIC) + ";" + PlanoEnum.BASIC.getPreco() + ";" 
                                       + String.valueOf(PlanoEnum.SILVER) + ";" + PlanoEnum.SILVER.getPreco() + ";"
                                       + String.valueOf(PlanoEnum.GOLD) + ";" + PlanoEnum.GOLD.getPreco());
     }

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
            Servico servico = new Servico(ServicoEnum.COMPRA, Jazigo.precoJazigo, cliente, jazigo, plano, null, LocalDate.now(), LocalTime.now());

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
            Servico servico = new Servico(ServicoEnum.ALUGUEL, Jazigo.aluguelJazigo, cliente, jazigo, plano, null, LocalDate.now(), LocalTime.now());

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
    @GetMapping("/{cpf}/editar_jazigo/{id}")
    public ResponseEntity<?> exibirMensagemFotoJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id) {
        
        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();

            return ResponseEntity.ok("OK;" + jazigo.getMensagem() + ";" + jazigo.getFoto());
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }

    //todo falta implementar no front - FUNCIONANDO
    //edita só a mensagem do jazigo, nao sei a situação da foto ainda
    @PostMapping("/{cpf}/editar_jazigo/{id}")
    public ResponseEntity<?> editarMensagemFotoJazigo(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("mensagem") String mensagem, @RequestParam("urlFoto") String urlFoto) {
        
        if (mensagem.length() > 80) {
            return ResponseEntity.ok("ERR;mensagem_maior_que_80_caracteres");
        }

        Optional<Jazigo> optionalJazigo = jazigoRepository.findById(id);
        
        if (optionalJazigo.isPresent()) {
            Jazigo jazigo = optionalJazigo.get();

            if(jazigo.getProprietario().equals(clienteRepository.findByCpf(cpf))){
                
                jazigo.setFoto(urlFoto);
                jazigo.setMensagem(mensagem);
                jazigoRepository.save(jazigo);
            } else {
                return ResponseEntity.ok("ERR;jazigo_nao_pertence_usuario");
            }

            return ResponseEntity.ok("OK;Mensagem_editada");
        } else {
            return ResponseEntity.ok("ERR;jazigo_nao_encontrado");
        }
    }

    //Retorna array de servicos em json do cliente passado - FUNCIONANDO
    @GetMapping("/{cpf}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf) {
        
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);

        if(carrinho == null) {
            return ResponseEntity.ok("ERR;carrinho_nulo");
        }

        List<Servico> listaServicos = carrinho.getServicos();

        List<ServicoDTO> listaServicosDTO = new ArrayList<>();

        for (Servico servico : listaServicos) {
            
            ServicoDTO servicoDTO = new ServicoDTO(servico.getValor(), servico.getTipoServico(), servico.getJazigo().getEndereco(), servico.getPlano(), servico.getIdServico());

            listaServicosDTO.add(servicoDTO);
        }

        //retorna um json com array de DTO servicos
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listaServicosDTO);

    }
    
    //Ao finalizar e comprar tudo do carrinho, se algum servico for alguel ou compra, seta o jazigo no banco.
    //limpa o carrinho e salva no banco
    @PostMapping("/{cpf}/informacoes_carrinho/finalizar")
    public ResponseEntity<?> realizarPagamento(@PathVariable("cpf") String cpf) {
        
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);
        
        if (carrinho != null) {
            
            for (Servico servico : carrinho.getServicos()) {
                Jazigo jazigo = servico.getJazigo();
                Pet pet = servico.getPet();

                switch(servico.getTipoServico()){
                    case COMPRA:
                    case ALUGUEL:
                        jazigo.setDisponivel(false);
                        jazigo.setPlano(servico.getPlano());
                        jazigo.setProprietario(cliente);
                        jazigo.setStatus(StatusEnum.DISPONIVEL);
                        jazigoRepository.save(jazigo);
                        cliente.setQuantJazigos(cliente.getQuantJazigos() + 1);
                        clienteRepository.save(cliente);
                        break;
                    
                    case ENTERRO:
                        jazigo.setPetEnterrado(pet);
                        jazigo.addPetHistorico(pet);
                        jazigo.setStatus(StatusEnum.OCUPADO);
                        jazigoRepository.save(jazigo);
                        pet.setDataEnterro(servico.getDataServico());
                        pet.setHoraEnterro(servico.getHoraServico());
                        petRepository.save(pet);
                        break;
                    
                    case EXUMACAO:
                        pet.setDataExumacao(servico.getDataServico());
                        pet.setHoraExumacao(servico.getHoraServico());
                        petRepository.save(pet);
                        
                        break;

                }
            }

            carrinho.limparCarrinho();
            carrinhoRepository.save(carrinho);

            //TODO daqui deve levar pros métodos de pagamento onde é criado e setado o pagamento no banco

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;carrinho_nao_encontrado");
        }
    }

    @PostMapping("/{cpf}/informacoes_carrinho/remover_servico")
    public ResponseEntity<?> removerServico(@PathVariable("cpf") String cpf, @RequestParam("idServico") Long idServico) {
        
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
        Optional<Servico> optionalServico = servicoRepository.findById(idServico);

        if(optionalServico.isPresent()){
            Servico servico = optionalServico.get();

            carrinho.removerServico(servico);
            carrinhoRepository.save(carrinho);
            servicoRepository.delete(servico);
        
            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;servico_not_found");
        }
    }

    // Recebe a data e hora do enterro e também os dados do pet a ser enterrado.
    // Cria um novo pet e um novo servico de enterro e adiciona no carrinho.
    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_enterro")
    public ResponseEntity<?> agendarEnterro(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data, @RequestParam("hora") String hora, @RequestParam("nomePet") String nomePet, @RequestParam("especie") String especie, @RequestParam("dataNascimento") String dataNascimento) {
        
        Jazigo jazigo = jazigoRepository.findById(id).get();

        if (!jazigo.getProprietario().equals(clienteRepository.findByCpf(cpf)) ) {
            return ResponseEntity.ok("ERR;jazigo_nao_pertence_ao_cliente");
        }
        
        Pet pet = new Pet(nomePet, LocalDate.parse(dataNascimento), especie, clienteRepository.findByCpf(cpf));
        petRepository.save(pet);

        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);

        Servico enterroServico = new Servico(ServicoEnum.ENTERRO, ServicoEnum.ENTERRO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, null, pet, LocalDate.parse(data), LocalTime.parse(hora));
        servicoRepository.save(enterroServico);

        carrinho.adicionarServico(enterroServico);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok("OK;");
    }

    // Recebe os parâmetros data (yyyy-mm-dd) e hora (hh-mm) da exumacao, no formato correto, e salva no banco
    // Não estamos utilizando o cpf pra nada :D - utiliza sim, p saber se o jazigo é do kra ou nao
    @PostMapping("/{cpf}/meus_jazigos/{id}/agendar_exumacao")
    public ResponseEntity<?> agendarExumacao(@PathVariable("cpf") String cpf, @PathVariable("id") Long id, @RequestParam("data") String data, @RequestParam("hora") String hora) {
        Jazigo jazigo = jazigoRepository.findById(id).get();
        Pet pet = jazigo.getPetEnterrado();
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);

        if (!jazigo.getProprietario().equals(clienteRepository.findByCpf(cpf)) ) {
            return ResponseEntity.ok("ERR;jazigo_nao_pertence_ao_cliente");
        }
        if(pet == null) {
            return ResponseEntity.ok("ERR;jazigo_nao_tem_pet");
        }

        Servico exumacao = new Servico(ServicoEnum.EXUMACAO, ServicoEnum.EXUMACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, null, pet, LocalDate.parse(data), LocalTime.parse(hora));
        servicoRepository.save(exumacao);

        carrinho.adicionarServico(exumacao);
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

    @GetMapping("/{cpf}/meus_jazigos/{id}/detalhar_jazigo")
    public ResponseEntity<?> detalharJazigo(@PathVariable("id") Long id){
        Jazigo jazigo = jazigoRepository.findByIdJazigo(id);

        if(jazigo.getPetEnterrado() == null) {
            return ResponseEntity.ok("OK;vazio");
        }

        DetalharJazigoDTO detalharJazigoDTO = new DetalharJazigoDTO(jazigo.getPetEnterrado(), jazigo);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(detalharJazigoDTO);
    }
    
}