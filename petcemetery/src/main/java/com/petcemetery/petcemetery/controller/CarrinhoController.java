package com.petcemetery.petcemetery.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/{cpf}/informacoes_carrinho/finalizar")
    public ResponseEntity<?> realizarPagamento(@PathVariable("cpf") String cpf, @RequestParam("data") LocalDate data, @RequestParam("hora") LocalTime hora) {
        
        Carrinho carrinho = carrinhoRepository.findByCpfCliente(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);
        Pet pet;
        Servico servico;
        
        if (carrinho != null) {
            
            for (Carrinho item : carrinhoRepository.findAllByCpfCliente(cpf)) {

                Jazigo jazigo = item.getJazigo();
                if (jazigo != null) { pet = jazigo.getPetEnterrado(); } else { pet = null;}

                // TODO terminar os casos de compra, manutencao e personalização. Aluguel precisa ser ajustado pra criar cobranças mensais.
                switch(item.getServico()) {
                    case COMPRA:
                    case ALUGUEL:
                        if(item.getServico() == ServicoEnum.COMPRA){
                            servico = new Servico(ServicoEnum.COMPRA, Jazigo.precoJazigo, cliente, jazigo, item.getPlano(), null, data, hora);
                        } else {
                            servico = new Servico(ServicoEnum.ALUGUEL, Jazigo.aluguelJazigo, cliente, jazigo, item.getPlano(), null, data ,hora);
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
                        servico = new Servico(ServicoEnum.ENTERRO, ServicoEnum.ENTERRO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, data, hora);
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
                        servico = new Servico(ServicoEnum.EXUMACAO, ServicoEnum.EXUMACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), pet, data, hora);
                        pet.setDataExumacao(servico.getDataServico());
                        pet.setHoraExumacao(servico.getHoraServico());
                        petRepository.save(pet);
                        servicoRepository.save(servico);
                        break;

                    case PERSONALIZACAO:
                        servico = new Servico(ServicoEnum.PERSONALIZACAO, ServicoEnum.PERSONALIZACAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), null, data, null);
                        jazigo.setPlano(servico.getPlano());
                        jazigoRepository.save(jazigo);
                        servicoRepository.save(servico);
                        break;

                    case MANUTENCAO:
                        servico = new Servico(ServicoEnum.MANUTENCAO, ServicoEnum.MANUTENCAO.getPreco(), clienteRepository.findByCpf(cpf), jazigo, jazigo.getPlano(), null, data, null);
                        servicoRepository.save(servico);
                    break;

                }
            }

            carrinhoRepository.deleteAllByCpfCliente(cpf);

            //TODO daqui deve levar pros métodos de pagamento onde é criado e setado o pagamento no banco

            return ResponseEntity.ok("OK;");
        } else {
            return ResponseEntity.ok("ERR;carrinho_nao_encontrado");
        }
    }
}
