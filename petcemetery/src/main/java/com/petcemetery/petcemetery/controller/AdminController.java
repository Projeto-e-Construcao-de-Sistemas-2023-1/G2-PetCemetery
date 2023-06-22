package com.petcemetery.petcemetery.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.DTO.ExibirServicoDTO;
import com.petcemetery.petcemetery.DTO.HorarioFuncionamentoDTO;
import com.petcemetery.petcemetery.model.HorarioFuncionamento;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.repositorio.HorarioFuncionamentoRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private JazigoRepository jazigoRepository;

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    // Retorna o jazigo em formato JSON com o id passado, ou uma mensagem de erro caso o id seja inválido
    @GetMapping("/detalhar-jazigo/{id}")
    public ResponseEntity<?> detalharJazigo(@PathVariable("id") Long id) {
        if(id < 1 || id > 72) {
            return ResponseEntity.ok("ERR;id_invalido;"); // Exibe uma mensagem de id inválido
        }
        
        Jazigo jazigo = jazigoRepository.findById(id).get();

        return ResponseEntity.ok(jazigo); 
    }

    // Igual ao do cliente, porém o admin não vai conseguir selecionar um jazigo (Isso tem que ser mudado no front?).
    @GetMapping("/mapa-jazigos")
    public ResponseEntity<?> mapaJazigos() {
        String str = "";

        // Busque todos os jazigos do banco de dados e adicione sua disponibilidade à lista.
        for (Jazigo i : jazigoRepository.findAllOrderByIdAsc()) {
            str = str + String.valueOf(i.getDisponivel()) + (i == jazigoRepository.findAllOrderByIdAsc().get(jazigoRepository.findAllOrderByIdAsc().size() - 1) ? "" : ";");
        }        
    
        System.out.println(str);
        return ResponseEntity.ok(str);  // Retorne a String de jazigos disponiveis 
    }

    // Retorna o valor de todos os servicos em formato JSON
    @GetMapping("/servicos")
    public ResponseEntity<?> exibirServicos() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ExibirServicoDTO()); // redireciona para a página de serviços
    }

    @PostMapping("/servicos/alterar")
    public ResponseEntity<?> alterarServicos(@RequestParam String servico, @RequestParam double valor) {
        // Atualiza o valor do plano com base no seu nome
        if(servico.equals("BASIC") || servico.equals("SILVER") || servico.equals("GOLD")) {
            System.out.println("isso é um plano");
            PlanoEnum planoEnum = PlanoEnum.valueOf(servico);
            if(planoEnum != null) {
                planoEnum.setPreco(valor);
                System.out.println(planoEnum.getPreco());
            }
        } else {
            System.out.println("isso é um servico");
            // Atualiza o valor do serviço com base no seu nome
            ServicoEnum servicoEnum = ServicoEnum.valueOf(servico);
            if (servicoEnum != null) {
                servicoEnum.setPreco(valor);
            }
        }
        return ResponseEntity.ok("OK;servico_alterado;"); // Exibe uma mensagem de servico alterado
    }

    // Altera o horário de funcionamento do cemitério de acordo com o horário passado no RequestBody. O front deve passar um body no formato:
    // {
    //  "segunda": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "false" }, "terca": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "false" }, 
    // "quarta": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "false" }, "quinta": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "false" }, 
    // "sexta": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "true" }, "sabado": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "false" }, 
    // "domingo": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "true" }, "feriado": {"abertura": "HH:MM", "fechamento": "HH:MM", "fechado": "true" }
    // }
    @PostMapping("/alterar_horario_funcionamento")
    public ResponseEntity<?> alterarHorarioFuncionamento(@RequestBody Map<String, Map<String, Object>> body) {
        
        Map<String, Object> horarioSegunda = body.get("segunda");
        LocalTime aberturaSegunda = LocalTime.parse((String) horarioSegunda.get("abertura"));
        LocalTime fechamentoSegunda = LocalTime.parse((String) horarioSegunda.get("fechamento"));
        boolean fechadoSegunda = (boolean) horarioSegunda.get("fechado");

        HorarioFuncionamento horarioSeg = horarioFuncionamentoRepository.findByDiaSemana("segunda");
        horarioSeg.setAbertura(aberturaSegunda);
        horarioSeg.setFechamento(fechamentoSegunda);
        horarioSeg.setFechado(fechadoSegunda);
        horarioFuncionamentoRepository.save(horarioSeg);

        Map<String, Object> horarioTerca = body.get("terca");
        LocalTime aberturaTerca = LocalTime.parse((String) horarioTerca.get("abertura"));
        LocalTime fechamentoTerca = LocalTime.parse((String) horarioTerca.get("fechamento"));
        boolean fechadoTerca = (boolean) horarioTerca.get("fechado");

        HorarioFuncionamento horarioTerc = horarioFuncionamentoRepository.findByDiaSemana("terca");
        horarioTerc.setAbertura(aberturaTerca);
        horarioTerc.setFechamento(fechamentoTerca);
        horarioTerc.setFechado(fechadoTerca);
        horarioFuncionamentoRepository.save(horarioTerc);

        Map<String, Object> horarioQuarta = body.get("quarta");
        LocalTime aberturaQuarta = LocalTime.parse((String) horarioQuarta.get("abertura"));
        LocalTime fechamentoQuarta = LocalTime.parse((String) horarioQuarta.get("fechamento"));
        boolean fechadoQuarta = (boolean) horarioQuarta.get("fechado");

        HorarioFuncionamento horarioQuar = horarioFuncionamentoRepository.findByDiaSemana("quarta");
        horarioQuar.setDiaSemana("quarta");
        horarioQuar.setAbertura(aberturaQuarta);
        horarioQuar.setFechamento(fechamentoQuarta);
        horarioQuar.setFechado(fechadoQuarta);
        horarioFuncionamentoRepository.save(horarioQuar);

        Map<String, Object> horarioQuinta = body.get("quinta");
        LocalTime aberturaQuinta = LocalTime.parse((String) horarioQuinta.get("abertura"));
        LocalTime fechamentoQuinta = LocalTime.parse((String) horarioQuinta.get("fechamento"));
        boolean fechadoQuinta = (boolean) horarioQuinta.get("fechado");

        HorarioFuncionamento horarioQuin = horarioFuncionamentoRepository.findByDiaSemana("quinta");
        horarioQuin.setAbertura(aberturaQuinta);
        horarioQuin.setFechamento(fechamentoQuinta);
        horarioQuin.setFechado(fechadoQuinta);
        horarioFuncionamentoRepository.save(horarioQuin);

        Map<String, Object> horarioSexta = body.get("sexta");
        LocalTime aberturaSexta = LocalTime.parse((String) horarioSexta.get("abertura"));
        LocalTime fechamentoSexta = LocalTime.parse((String) horarioSexta.get("fechamento"));
        boolean fechadoSexta = (boolean) horarioSexta.get("fechado");

        HorarioFuncionamento horarioSex = horarioFuncionamentoRepository.findByDiaSemana("sexta");
        horarioSex.setDiaSemana("sexta");
        horarioSex.setAbertura(aberturaSexta);
        horarioSex.setFechamento(fechamentoSexta);
        horarioSex.setFechado(fechadoSexta);
        horarioFuncionamentoRepository.save(horarioSex);
        
        Map<String, Object> horarioSabado = body.get("sabado");
        LocalTime aberturaSabado = LocalTime.parse((String) horarioSabado.get("abertura"));
        LocalTime fechamentoSabado = LocalTime.parse((String) horarioSabado.get("fechamento"));
        boolean fechadoSabado = (boolean) horarioSabado.get("fechado");

        HorarioFuncionamento horarioSab = horarioFuncionamentoRepository.findByDiaSemana("sabado");
        horarioSab.setDiaSemana("sabado");
        horarioSab.setAbertura(aberturaSabado);
        horarioSab.setFechamento(fechamentoSabado);
        horarioSab.setFechado(fechadoSabado);
        horarioFuncionamentoRepository.save(horarioSab);

        Map<String, Object> horarioDomingo = body.get("domingo");
        LocalTime aberturaDomingo = LocalTime.parse((String) horarioDomingo.get("abertura"));
        LocalTime fechamentoDomingo = LocalTime.parse((String) horarioDomingo.get("fechamento"));
        boolean fechadoDomingo = (boolean) horarioDomingo.get("fechado");

        HorarioFuncionamento horarioDom = horarioFuncionamentoRepository.findByDiaSemana("domingo");
        horarioDom.setDiaSemana("domingo");
        horarioDom.setAbertura(aberturaDomingo);
        horarioDom.setFechamento(fechamentoDomingo);
        horarioDom.setFechado(fechadoDomingo);
        horarioFuncionamentoRepository.save(horarioDom);

        Map<String, Object> horarioFeriado = body.get("feriado");
        LocalTime aberturaFeriado = LocalTime.parse((String) horarioFeriado.get("abertura"));
        LocalTime fechamentoFeriado = LocalTime.parse((String) horarioFeriado.get("fechamento"));
        boolean fechadoFeriado = (boolean) horarioFeriado.get("fechado");

        HorarioFuncionamento horarioFer = horarioFuncionamentoRepository.findByDiaSemana("feriado");
        horarioFer.setDiaSemana("feriado");
        horarioFer.setAbertura(aberturaFeriado);
        horarioFer.setFechamento(fechamentoFeriado);
        horarioFer.setFechado(fechadoFeriado);
        horarioFuncionamentoRepository.save(horarioFer);

        return ResponseEntity.ok("OK;horario_alterado;"); // Exibe uma mensagem de horario alterado
    }

    // Retorna todos os horários de funcionamento para serem exibidos quando o admin entrar na tela de Alterar Horário de Funcionamento
    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioFuncionamentoDTO>> getHorarios() {
        List<HorarioFuncionamento> horarios = horarioFuncionamentoRepository.findAll();
        List<HorarioFuncionamentoDTO> horariosDTO = new ArrayList<>();

        for(HorarioFuncionamento horario : horarios) {
            HorarioFuncionamentoDTO horarioDTO = new HorarioFuncionamentoDTO(horario.getDiaSemana(), horario.getAbertura(), horario.getFechamento(), horario.isFechado());
            horariosDTO.add(horarioDTO);
        }
        return ResponseEntity.ok(horariosDTO);
    }
    
}
