package com.petcemetery.petcemetery.controller;

import java.io.ByteArrayOutputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Collectors;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.petcemetery.petcemetery.DTO.ClienteDTO;
import com.petcemetery.petcemetery.DTO.HistoricoJazigoDTO;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.DTO.HorarioFuncionamentoDTO;
import com.petcemetery.petcemetery.DTO.JazigoDTO;
import com.petcemetery.petcemetery.DTO.HistoricoServicosDTO;
import com.petcemetery.petcemetery.model.HorarioFuncionamento;
import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Pet;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.HistoricoServicos;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;
import com.petcemetery.petcemetery.outros.VerificadorData;
import com.petcemetery.petcemetery.model.Servico.PlanoEnum;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;
import com.petcemetery.petcemetery.repositorio.HistoricoServicosRepository;
import com.petcemetery.petcemetery.repositorio.HorarioFuncionamentoRepository;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;
import com.petcemetery.petcemetery.services.EmailService;

@EnableAsync
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private JazigoRepository jazigoRepository;

    @Autowired
    private HistoricoServicosRepository historicoServicosRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HorarioFuncionamentoRepository horarioFuncionamentoRepository;

    @Autowired
    private EmailService emailService;

    // Retorna, em formato JSON, informações sobre todos os pets que já passaram no jazigo passado pelo seu id.
    @GetMapping("/{id}/visualizar-historico")
    public ResponseEntity<?> visualizarHistorico(@PathVariable("id") Long id) {
        if(id < 1 || id > 72) {
            return ResponseEntity.ok("ERR;id_invalido;"); // Exibe uma mensagem de id inválido
        }

        Jazigo jazigo = jazigoRepository.findById(id).get();

        List<HistoricoJazigoDTO> historicoJazigoDTO = new ArrayList<>();

        for(Pet pet: jazigo.getHistoricoPets()) {
            System.out.println("NOME DO PET QUE PASSOU PELO JAZIGO "+ jazigo.getIdJazigo() + ": " + "  + pet.getNomePet()");
            System.out.println("DATA DE EXUMACAO DOS PET: " + pet.getDataExumacao());
            historicoJazigoDTO.add(new HistoricoJazigoDTO(id, pet.getNomePet(), pet.getDataNascimento(), pet.getEspecie(), pet.getProprietario().getNome(), pet.getDataEnterro(), pet.getDataExumacao()));
        }
        
        return ResponseEntity.ok(historicoJazigoDTO); 
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
        List<Servico> servicos = servicoRepository.findAll();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(servicos); // redireciona para a página de serviços
    }

    // Atualiza o valor do plano no banco com base no seu nome
    @PostMapping("/servicos/alterar")
    public ResponseEntity<?> alterarServicos(@RequestParam String servico, @RequestParam double valor) { 

        if (servico == "BASIC" || servico == "SILVER" || servico == "GOLD") {
            PlanoEnum planoEnum = PlanoEnum.valueOf(servico);
            Servico servicoEntity = servicoRepository.findByTipoServico(ServicoEnum.valueOf(planoEnum.toString()));

            servicoEntity.setValor(valor);
            servicoRepository.save(servicoEntity);
        } else {
            ServicoEnum servicoEnum = ServicoEnum.valueOf(servico);
            Servico servicoEntity = servicoRepository.findByTipoServico(servicoEnum);

            servicoEntity.setValor(valor);
            servicoRepository.save(servicoEntity);
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

        System.out.println("Horários alterados com sucesso!");
        notificaClientes();

        return ResponseEntity.ok("OK;horario_alterado;"); // Exibe uma mensagem de horario alterado
    }

    // Método que é chamado depois que o admin altera o horário de funcionamento do cemitério, e envia para todos os clientes os novos horários.
    @Async
    public void notificaClientes() {
        System.out.println("Notificando clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        String[] emails = new String[clientes.size()];
        for(Cliente cliente : clientes) {
            emails[clientes.indexOf(cliente)] = cliente.getEmail();
        }
        System.out.println("Pegando todos as informacoes do banco");
        String subject = "Horário de funcionamento do cemitério alterado";
        String body = "Olá! Os horários de funcionamento do cemítério essa semana foram alterados. Segue os novos horários:\n" +
                "Segunda: " + horarioFuncionamentoRepository.findByDiaSemana("segunda").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("segunda").getFechamento() + "\n" +
                "Terça: " + horarioFuncionamentoRepository.findByDiaSemana("terca").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("terca").getFechamento() + "\n" +
                "Quarta: " + horarioFuncionamentoRepository.findByDiaSemana("quarta").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("quarta").getFechamento() + "\n" +
                "Quinta: " + horarioFuncionamentoRepository.findByDiaSemana("quinta").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("quinta").getFechamento() + "\n" +
                "Sexta: " + horarioFuncionamentoRepository.findByDiaSemana("sexta").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("sexta").getFechamento() + "\n" +
                "Sábado: " + horarioFuncionamentoRepository.findByDiaSemana("sabado").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("sabado").getFechamento() + "\n" +
                "Domingo: " + horarioFuncionamentoRepository.findByDiaSemana("domingo").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("domingo").getFechamento() + "\n" +
                "Feriado: " + horarioFuncionamentoRepository.findByDiaSemana("feriado").getAbertura() + " - " + horarioFuncionamentoRepository.findByDiaSemana("feriado").getFechamento() + "\n" +
                "Atenciosamente, Pet Cemetery.";

        System.out.println("Enviando email");
        emailService.sendEmail(emails, subject, body);
        System.out.println("terminou de enviar");
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

    // Visualizar clientes inadimplentes
    @GetMapping("/relatorio")
    public ResponseEntity<List<ClienteDTO>> exibirRelatorio() {
        List<Cliente> clientesInadimplentes = clienteRepository.findByInadimplenteTrue();

        List<ClienteDTO> clientesDTO = clientesInadimplentes.stream().map(cliente -> new ClienteDTO(
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getNome(),
                cliente.getDesativado(),
                cliente.getInadimplente()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(clientesDTO);
    }

    // Relatório de enterros
    @GetMapping("/visualizar_enterros")
    public ResponseEntity<List<HistoricoServicosDTO>> visualizarEnterros() {
        List<HistoricoServicos> historicoServicos = historicoServicosRepository.findByTipoServico(ServicoEnum.valueOf("ENTERRO"));
        List<HistoricoServicosDTO> enterros = new ArrayList<>();

        for (HistoricoServicos servico : historicoServicos) {
            HistoricoServicosDTO servicoDTO = new HistoricoServicosDTO(
                servico.getValor(),
                servico.getTipoServico(),
                servico.getJazigo().getEndereco(),
                servico.getPlano(),
                servico.getJazigo().getIdJazigo(),
                servico.getPet().getId(),
                servico.getPet().getDataEnterro().toString(),
                servico.getCliente().getCpf()
            );

            enterros.add(servicoDTO);
        }

        return ResponseEntity.ok(enterros); 
    }

    // Gerar PDF de enterros
    @GetMapping("/gerar_pdf_enterros")
    public ResponseEntity<byte[]> gerarPDFEnterros() {
    // Pega o retorno do método acima
    List<HistoricoServicosDTO> enterros = visualizarEnterros().getBody(); 

    if(enterros == null) {
        return ResponseEntity.notFound().build();
    }

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        // Adicionando o título ao PDF
        Paragraph paragraph = new Paragraph("Relatório de Enterros", FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        // Adicionar espaços em branco
        Chunk chunk = new Chunk("\n");
        document.add(chunk);

        document.add(new Paragraph("        VALOR                    JAZIGO                    CPF                                     DATA ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
        // Adicionando o conteúdo de cada objeto ao PDF
        for (HistoricoServicosDTO enterro : enterros) {
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.add(new Paragraph("         " + String.valueOf(enterro.getValor()) + "                         " + enterro.getEnderecoJazigo() + "                    " + enterro.getCpfCliente() + "                       " + enterro.getDataServico()));
        }
        document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
        document.close();
        writer.close();

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "relatorio_enterros.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    // Relatório de exumações
    @GetMapping("/visualizar_exumacoes")
    public ResponseEntity<List<HistoricoServicosDTO>> visualizarExumacoes() {
        List<HistoricoServicos> historicoServicos = historicoServicosRepository.findByTipoServico(ServicoEnum.valueOf("EXUMACAO"));
        List<HistoricoServicosDTO> exumacoes = new ArrayList<>();

        for (HistoricoServicos servico : historicoServicos) {
            HistoricoServicosDTO servicoDTO = new HistoricoServicosDTO(
                servico.getValor(),
                servico.getTipoServico(),
                servico.getJazigo().getEndereco(),
                servico.getPlano(),
                servico.getJazigo().getIdJazigo(),
                servico.getPet().getId(),
                servico.getDataServico().toString(),
                servico.getCliente().getCpf()
            );

            exumacoes.add(servicoDTO);
        }

        return ResponseEntity.ok(exumacoes);
    }

    // Gerar PDF de exumações
    @GetMapping("/gerar_pdf_exumacoes")
    public ResponseEntity<byte[]> gerarPDFExumacoes() {
        // Pega o retorno do método acima
        List<HistoricoServicosDTO> exumacoes = visualizarExumacoes().getBody();

        if(exumacoes == null) {
            return ResponseEntity.notFound().build();
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Adicionando o título ao PDF
            Paragraph paragraph = new Paragraph("Relatório de Exumações", FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            // Adicionar espaços em branco
            Chunk chunk = new Chunk("\n");
            document.add(chunk);

            document.add(new Paragraph("     VALOR                    JAZIGO                    CPF                                     DATA ", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
            // Adicionando o conteúdo de cada objeto ao PDF
            for (HistoricoServicosDTO exumacao : exumacoes) {
                document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
                document.add(new Paragraph("      " + String.valueOf(exumacao.getValor()) + "                         " + exumacao.getEnderecoJazigo() + "                    " + exumacao.getCpfCliente() + "                       " + exumacao.getDataServico()));
            }
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));
            document.close();
            writer.close();

            byte[] pdfBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio_exumacoes.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    
    }

    // Relatório de jazigos
    @GetMapping("/get_jazigos")
    public ResponseEntity<?> getJazigos(){
        List<Jazigo> jazigos = jazigoRepository.findAllOrderByIdAsc();
        List<JazigoDTO> jazigosDTO = new ArrayList<>();

        for (Jazigo jazigo : jazigos) {
            JazigoDTO jazigoDto;

            if(jazigo.getPetEnterrado() != null) { // Caso tenha pet enterrado
                jazigoDto = new JazigoDTO(
                jazigo.getPetEnterrado().getNomePet(),
                jazigo.getPetEnterrado().getDataEnterro(),
                jazigo.getEndereco(),
                jazigo.getIdJazigo(),
                jazigo.getPetEnterrado().getDataNascimento(),
                jazigo.getPetEnterrado().getEspecie(),
                jazigo.getMensagem(),
                jazigo.getPlano().toString(),
                jazigo.getProprietario().getCpf()
                );
            } else if(jazigo.getProprietario() != null) { // Caso não tenha pet enterrado mas tenha proprietario
                jazigoDto = new JazigoDTO(
                null,
                null,
                jazigo.getEndereco(),
                jazigo.getIdJazigo(),
                null,
                null,
                jazigo.getMensagem(),
                jazigo.getPlano().toString(),
                jazigo.getProprietario().getCpf()
                );
            } else { // Caso não tenha pet enterrado nem proprietario
                jazigoDto = new JazigoDTO(
                null,
                null,
                jazigo.getEndereco(),
                jazigo.getIdJazigo(),
                null,
                null,
                jazigo.getMensagem(),
                null,
                null
                );
            }
            
            jazigosDTO.add(jazigoDto);
        }
        
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jazigosDTO);
    }

    // gerar PDF de jazigos
    @GetMapping("/gerar_pdf_jazigos")
    public ResponseEntity<byte[]> gerarPDFJazigos() {
    List<JazigoDTO> jazigos = (List<JazigoDTO>) getJazigos().getBody();

    if (jazigos == null) {
        return ResponseEntity.notFound().build();
    }

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        Paragraph title = new Paragraph("Relatório de Jazigos", FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Chunk space = new Chunk("\n");
        document.add(space);

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);

        table.addCell(new PdfPCell(new Phrase("PET", font)));
        table.addCell(new PdfPCell(new Phrase("DATA ENTERRO", font)));
        table.addCell(new PdfPCell(new Phrase("ENDEREÇO", font)));
        table.addCell(new PdfPCell(new Phrase("ID JAZIGO", font)));
        table.addCell(new PdfPCell(new Phrase("DATA NASC.", font)));
        table.addCell(new PdfPCell(new Phrase("ESPÉCIE", font)));
        table.addCell(new PdfPCell(new Phrase("PLANO", font)));
        table.addCell(new PdfPCell(new Phrase("CPF CLIENTE", font)));

        for (JazigoDTO jazigo : jazigos) {
            table.addCell(jazigo.getNomePet());
            if(jazigo.getDataEnterro() == null) {
                table.addCell("");
            } else {
                table.addCell(jazigo.getDataEnterro().toString());
            }
            table.addCell(jazigo.getEndereco());
            table.addCell(jazigo.getIdJazigo().toString());
            if(jazigo.getDataNascimento() == null) {
                table.addCell("");
            } else {
                table.addCell(jazigo.getDataNascimento().toString());
            }
            table.addCell(jazigo.getEspecie());
            table.addCell(jazigo.getPlano());
            table.addCell(new PdfPCell(new Phrase(jazigo.getCpfCliente(), font)));
        }

        document.add(table);

        document.close();
        writer.close();

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "relatorio_jazigos.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Recebe um request parameter "data" no formato "yyyy-MM-dd" e seta a data atual do sistema para a data recebida. Retorna "ERR;data_invalida"
    // caso o formato da data seja inválido, e "OK" se tudo ocorrer bem.
    @PostMapping("/time_travel")
    public ResponseEntity<?> timeTravel(@RequestParam("data") String data) {
        LocalDate dataAvancada;
        
        try {
            dataAvancada = LocalDate.parse(data);
        }
        catch (DateTimeException e){
            return ResponseEntity.ok("ERR;data_invalida");
        }

        VerificadorData.setCurrentDate(dataAvancada);

        return ResponseEntity.ok("OK");
    }
}