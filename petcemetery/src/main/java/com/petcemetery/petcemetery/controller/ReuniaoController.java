package com.petcemetery.petcemetery.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.DTO.ReuniaoDTO;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.model.Reuniao;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;
import com.petcemetery.petcemetery.repositorio.ReuniaoRepository;
import com.petcemetery.petcemetery.services.EmailService;


@RestController
@RequestMapping("/api/reuniao")
public class ReuniaoController {

    @Autowired
    private ReuniaoRepository reuniaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;
    
    // Retorna a lista de todas as reunioes do banco de dados, para serem visualizadas pelo admin, de forma crescente pela data
    @GetMapping("/admin/visualizar")
    public ResponseEntity<?> visualizarReunioes() {
        List<Reuniao> reunioes = reuniaoRepository.findAllOrderByDataAsc();
        List<ReuniaoDTO> reunioesDTO = new ArrayList<>();

        for (Reuniao reuniao : reunioes) {
            ReuniaoDTO reuniaoDTO = new ReuniaoDTO(
                reuniao.getCliente().getCpf(),
                reuniao.getData(),
                reuniao.getAssunto(),
                reuniao.getHorario()
            );

            reunioesDTO.add(reuniaoDTO);
        }

        return ResponseEntity.ok(reunioesDTO); 
    }

    // Salva uma reuniao no banco de dados, com base no cpf do cliente e nos parâmetros que ele escolheu na página (data, horário e assunto)
    // A data deve ser no formato yyyy-MM-dd, e o horário no formato hh:mm, e o assunto deve ser uma String. Deve ser enviado no formato JSON.
    @PostMapping("/cliente/{cpf}/agendar")
    public ResponseEntity<?> agendarReuniao(@PathVariable("cpf") String cpf, @RequestBody Reuniao reuniao) {

        // Verificando se a reunião está sendo agendada com uma antecedência de dois dias
        if(reuniao.getData().isBefore(LocalDate.now().minusDays(2))) {
            return ResponseEntity.ok("ERR;data_invalida");
        }

        Cliente cliente = clienteRepository.findByCpf(cpf);
        reuniao.setCliente(cliente);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        reuniaoRepository.save(reuniao);

        String[] to = {cliente.getEmail()};
        emailService.sendEmail(to, "Agendamento de reunião", "Sua reunião foi agendada com sucesso para o dia " + reuniao.getData().format(formatter) + ", no horário " + reuniao.getHorario() + "!");

        return ResponseEntity.ok("OK;"); 
    }
}
