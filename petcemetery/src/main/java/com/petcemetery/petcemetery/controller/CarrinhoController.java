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

import com.petcemetery.petcemetery.DTO.HistoricoServicosDTO;
import com.petcemetery.petcemetery.model.Carrinho;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.model.HistoricoServicos;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Pagamento;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import com.petcemetery.petcemetery.model.Pagamento.MetodoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.repositorio.CarrinhoRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.PagamentoRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;
import com.petcemetery.petcemetery.repositorio.HistoricoServicosRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class CarrinhoController {

    @Autowired
    private JazigoRepository jazigoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private HistoricoServicosRepository historicoServicosRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    //Ao finalizar e comprar tudo do carrinho, se algum servico for alguel ou compra, seta o jazigo no banco.
    //limpa o carrinho e salva no banco
    @Transactional
    @PostMapping("/{cpf}/informacoes_carrinho/finalizar")
    public ResponseEntity<?> realizarPagamento(@PathVariable("cpf") String cpf) {
        
        List<Carrinho> carrinho = carrinhoRepository.findAllByCpfCliente(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);
        HistoricoServicos historicoServicos;
        
        if (carrinho != null) {
            LocalDate data = LocalDate.now();
            LocalTime horario = LocalTime.now();
            
            for (Carrinho item : carrinhoRepository.findAllByCpfCliente(cpf)) {

                Jazigo jazigo = item.getJazigo();

                // TODO Aluguel precisa ser ajustado pra criar cobranças mensais.
                switch(item.getServico()) {
                    case COMPRA:
                    case ALUGUEL:
                        if(item.getServico() == ServicoEnum.COMPRA){
                            double valor = servicoRepository.findByTipoServico(item.getServico()).getValor();
                            double valorPlano = servicoRepository.findByTipoServico(ServicoEnum.valueOf(item.getPlano().toString())).getValor();
                            historicoServicos = new HistoricoServicos(ServicoEnum.COMPRA, valor + valorPlano, cliente, jazigo, item.getPlano(), null, data, horario, data, data);
                        } else {
                            double valor = servicoRepository.findByTipoServico(item.getServico()).getValor();
                            double valorPlano = servicoRepository.findByTipoServico(ServicoEnum.valueOf(item.getPlano().toString())).getValor();
                            historicoServicos = new HistoricoServicos(ServicoEnum.ALUGUEL, valor + valorPlano, cliente, jazigo, item.getPlano(), null, data, horario);
                            historicoServicos.setUltimoPagamento(data);
                            Pagamento pagamento = new Pagamento(cliente, valor, data, data.plusMonths(1), true, historicoServicos, MetodoEnum.CREDITO);
                            pagamentoRepository.save(pagamento);
                        }
                        jazigo.setDisponivel(false);
                        jazigo.setPlano(historicoServicos.getPlano());
                        jazigo.setProprietario(cliente);
                        jazigo.setStatus(StatusEnum.DISPONIVEL);
                        jazigoRepository.save(jazigo);
                        historicoServicos.setPrimeiroPagamento(data);
                        historicoServicosRepository.save(historicoServicos);
                        break;
                    
                    case ENTERRO:
                        historicoServicos = historicoServicosRepository.findByIdServico(item.getIdServico().getIdServico());
                        historicoServicos.setPrimeiroPagamento(data);
                        historicoServicos.setUltimoPagamento(data);
                        historicoServicosRepository.save(historicoServicos);
                        break;
                    
                    case EXUMACAO:
                        historicoServicos = historicoServicosRepository.findByIdServico(item.getIdServico().getIdServico());
                        historicoServicos.setPrimeiroPagamento(data);
                        historicoServicos.setUltimoPagamento(data);
                        historicoServicosRepository.save(historicoServicos);
                        break;

                    case PERSONALIZACAO: //TODO nao querem trocar o nome desse servico pra "TROCAPLANO"? p n confundir c personalizacao de mensagem/foto?
                        historicoServicos = historicoServicosRepository.findByIdServico(item.getIdServico().getIdServico());
                        historicoServicos.setPrimeiroPagamento(data);
                        historicoServicos.setUltimoPagamento(data);
                        jazigo.setPlano(historicoServicos.getPlano());
                        jazigoRepository.save(jazigo);
                        historicoServicosRepository.save(historicoServicos);
                        break;

                    case MANUTENCAO:
                        historicoServicos = historicoServicosRepository.findByIdServico(item.getIdServico().getIdServico());
                        historicoServicos.setPrimeiroPagamento(data);
                        historicoServicos.setUltimoPagamento(data);
                        historicoServicosRepository.save(historicoServicos);
                        Pagamento pagamento = new Pagamento(cliente, servicoRepository.findByTipoServico(item.getServico()).getValor(), LocalDate.now(), LocalDate.now().plusYears(1), true, historicoServicos, MetodoEnum.CREDITO);
                        pagamentoRepository.save(pagamento);
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

    //Retorna array de servicos em json do cliente passado
    @GetMapping("/{cpf}/informacoes_carrinho")
    public ResponseEntity<?> informacoesCarrinho(@PathVariable("cpf") String cpf) {
        List<HistoricoServicosDTO> listaHistoricoServicosDTO = new ArrayList<>();

        for (Carrinho item : carrinhoRepository.findAllByCpfCliente(cpf)) {
            HistoricoServicosDTO historicoServicosDTO;
            double valor = servicoRepository.findByTipoServico(item.getServico()).getValor();
            double valorPlano;

            //adiciona à variavel valor, o valor do plano caso o servico seja compra ou aluguel
            switch (item.getServico()) {
                case COMPRA:
                    valorPlano = servicoRepository.findByTipoServico(ServicoEnum.valueOf(item.getPlano().toString())).getValor();
                    valor += valorPlano;
                    break;
                case ALUGUEL:
                    valorPlano = servicoRepository.findByTipoServico(ServicoEnum.valueOf(item.getPlano().toString())).getValor();
                    valor += valorPlano;
                    break;
                default:
                    break;
            }
            
            if(item.getPet() != null){
                historicoServicosDTO = new HistoricoServicosDTO(valor, item.getServico(), item.getJazigo().getEndereco(), item.getPlano(), item.getJazigo().getIdJazigo(), item.getPet().getId(), item.getDataAgendamento().toString(), item.getCpfCliente());
            } else if(item.getJazigo() != null){
                historicoServicosDTO = new HistoricoServicosDTO(valor, item.getServico(), item.getJazigo().getEndereco(), item.getPlano(), item.getJazigo().getIdJazigo(), item.getCpfCliente());
            } else {
                historicoServicosDTO = new HistoricoServicosDTO(valor, item.getServico(), item.getPlano(), item.getCpfCliente());
            }

            listaHistoricoServicosDTO.add(historicoServicosDTO);
        }

        //retorna um json com array de DTO servicos
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(listaHistoricoServicosDTO);

    }

    @Transactional
    @PostMapping("/{cpf}/informacoes_carrinho/remover_servico")
    public ResponseEntity<?> removerServico(@PathVariable("cpf") String cpf, @RequestParam("idJazigo") long idJazigo) {
        Jazigo jazigo = jazigoRepository.findById(idJazigo).get();
        carrinhoRepository.deleteByJazigo(jazigo);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("OK;");
    }
}
