package com.petcemetery.petcemetery.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.DTO.ServicoDTO;
import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import com.petcemetery.petcemetery.model.Pet;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.PetRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class CarrinhoController {

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


    //Ao finalizar e comprar tudo do carrinho, se algum servico for alguel ou compra, seta o jazigo no banco.
    //limpa o carrinho e salva no banco
    @Transactional
    @PostMapping("/{cpf}/informacoes_carrinho/finalizar")
    public ResponseEntity<?> realizarPagamento(@PathVariable("cpf") String cpf) {
        
        List<Carrinho> carrinho = carrinhoRepository.findAllByCpfCliente(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);
        Servico servico;
        
        if (carrinho != null) {
            
            for (Carrinho item : carrinhoRepository.findAllByCpfCliente(cpf)) {

                Jazigo jazigo = item.getJazigo();
                Pet pet = item.getPet();

                // TODO terminar os casos de compra, manutencao e personalização. Aluguel precisa ser ajustado pra criar cobranças mensais.
                switch(item.getServico()) {
                    case COMPRA:
                    case ALUGUEL:
                        if(item.getServico() == ServicoEnum.COMPRA){
                            servico = new Servico(ServicoEnum.COMPRA, Jazigo.precoJazigo, cliente, jazigo, item.getPlano(), null, LocalDate.now(), LocalTime.now());
                        } else {
                            servico = new Servico(ServicoEnum.ALUGUEL, Jazigo.aluguelJazigo, cliente, jazigo, item.getPlano(), null, LocalDate.now(), LocalTime.now());
                        }
                        jazigo.setDisponivel(false);
                        jazigo.setPlano(servico.getPlano());
                        jazigo.setProprietario(cliente);
                        jazigo.setStatus(StatusEnum.DISPONIVEL);
                        cliente.setQuantJazigos(cliente.getQuantJazigos() + 1);
                        jazigoRepository.save(jazigo);
                        clienteRepository.save(cliente);
                        servicoRepository.save(servico);
                        break;
                    
                    case ENTERRO:
                        servico = new Servico(ServicoEnum.ENTERRO, ServicoEnum.ENTERRO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, item.getPlano(), pet, item.getDataAgendamento(), item.getHoraAgendamento());
                        jazigo.setPetEnterrado(pet);
                        jazigo.addPetHistorico(pet);
                        jazigo.setStatus(StatusEnum.OCUPADO);
                        pet.setDataEnterro(servico.getDataServico());
                        pet.setHoraEnterro(servico.getHoraServico());
                        jazigoRepository.save(jazigo);
                        petRepository.save(pet);
                        servicoRepository.save(servico);
                        break;
                    
                    case EXUMACAO:
                        servico = new Servico(ServicoEnum.EXUMACAO, ServicoEnum.EXUMACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, item.getDataAgendamento(), item.getHoraAgendamento());
                        pet.setDataExumacao(servico.getDataServico());
                        pet.setHoraExumacao(servico.getHoraServico());
                        petRepository.save(pet);
                        servicoRepository.save(servico);
                        break;

                    case PERSONALIZACAO: //TODO nao querem trocar o nome desse servico pra "TROCAPLANO"? p n confundir c personalizacao de mensagem/foto?
                        servico = new Servico(ServicoEnum.PERSONALIZACAO, ServicoEnum.PERSONALIZACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), null, LocalDate.now(), LocalTime.now());
                        jazigo.setPlano(servico.getPlano());
                        jazigoRepository.save(jazigo);
                        servicoRepository.save(servico);
                        break;

                    case MANUTENCAO:
                        servico = new Servico(ServicoEnum.MANUTENCAO, ServicoEnum.MANUTENCAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), null, item.getDataAgendamento(), null);
                        servicoRepository.save(servico);
                    break;

                    default:
                        return ResponseEntity.ok("ERR;servico_nao_encontrado");

                }
            }

            carrinhoRepository.deleteAllByCpfCliente(cpf);

            //TODO daqui deve levar pros métodos de pagamento onde é criado e setado o pagamento no banco

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;carrinho_nao_encontrado");
        }
    }

    //! esse metodo basicamente é o mesmo embaixo dele 
    //retorna as informaçoes necessárias para visualizar as despesas de cada cpf na tela visualizar despesas
    //talvez seja melhor mover para o cliente contorller e modificar a url prefixada??
    // @GetMapping("/{cpf}/visualizar_despesas")
    // public ResponseEntity<?>visualizarDespesas(@PathVariable("cpf") String cpf){
    //     List<Servico> servicos = servicoRepository.findBycliente_cpf(cpf);
    //     List <VisualizarDespesasDTO> despesasDTO = new ArrayList<>();
    
    //     for (Servico s : servicos){
    //         VisualizarDespesasDTO despesaDTO = new VisualizarDespesasDTO(s);
    //         despesasDTO.add(despesaDTO);
    //     }
    //     return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(despesasDTO);
    // }

    //Retorna array de servicos em json do cliente passado
    @GetMapping("/{cpf}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf) {
        List<ServicoDTO> listaServicosDTO = new ArrayList<>();

        for (Carrinho item : carrinhoRepository.findAllByCpfCliente(cpf)) {
            double valor = item.getServico().getPreco();

            //adiciona à variavel valor, o valor do plano caso o servico seja compra ou aluguel
            switch (item.getServico()) {
                case COMPRA:
                    valor += item.getPlano().getPreco();
                    break;
                case ALUGUEL:
                    valor += item.getPlano().getPreco();
                    break;
                default:
                    break;
            }
            
            ServicoDTO servicoDTO = new ServicoDTO(valor, item.getServico(), item.getJazigo().getEndereco(), item.getPlano());

            listaServicosDTO.add(servicoDTO);
        }

        //retorna um json com array de DTO servicos
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listaServicosDTO);

    }
}
