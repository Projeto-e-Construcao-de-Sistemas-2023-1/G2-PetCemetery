package com.petcemetery.petcemetery.outros;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.model.Lembrete;
import com.petcemetery.petcemetery.model.Reuniao;
import com.petcemetery.petcemetery.model.Servico;
import com.petcemetery.petcemetery.model.Jazigo.StatusEnum;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import com.petcemetery.petcemetery.repositorio.LembreteRepository;
import com.petcemetery.petcemetery.repositorio.ReuniaoRepository;
import com.petcemetery.petcemetery.repositorio.ServicoRepository;
import com.petcemetery.petcemetery.services.EmailService;
import java.util.List;

public class VerificadorData {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private JazigoRepository jazigoRepository;

    @Autowired
    private ReuniaoRepository reuniaoRepository;

    @Autowired
    private LembreteRepository lembreteRepository;
    
    // Checa a cada 2 minutos se há algum serviço de enterro para ser realizado, e envia um email pro cliente caso o enterro do pet dele seja hoje
    @Scheduled(cron = "0 */2 * * * ?") // Executa a cada dois minutos
    public void checaEnterros() {
        LocalDate dataAtual = LocalDate.now();
        List<Servico> servicos = servicoRepository.findAll();

        for (Servico servico : servicos) {
            if(servico.getTipoServico() != Servico.ServicoEnum.ENTERRO) {
                continue;
            }
            LocalDate dataEnterro = servico.getDataServico();

            if (dataEnterro.equals(dataAtual)) {
                Jazigo jazigo = servico.getJazigo();
                jazigo.setPetEnterrado(servico.getPet());
                jazigoRepository.save(jazigo);

                // Enviar o e-mail de aviso
                String[] email = {servico.getCliente().getEmail()};
                String assunto = "Lembrete de Enterro";
                String mensagem = "Lembrete: Hoje é o dia do enterro do seu pet no cemitério.";
                emailService.sendEmail(email, assunto, mensagem);
            }
        }
    }

    // Checa a cada 2 minutos se há algum serviço de exumação para ser realizado, e envia um email pro cliente caso a cremação do pet dele seja hoje
    @Scheduled(cron = "0 */2 * * * ?") // Executa a cada dois minutos
    public void checaExumacoes() {
        LocalDate dataAtual = LocalDate.now();
        List<Servico> servicos = servicoRepository.findAll();

        for (Servico servico : servicos) {
            if(servico.getTipoServico() != Servico.ServicoEnum.EXUMACAO) {
                continue;
            }
            LocalDate dataExumacao = servico.getDataServico();

            if (dataExumacao.equals(dataAtual)) {
                Jazigo jazigo = servico.getJazigo();
                jazigo.setPetEnterrado(null);
                jazigo.setStatus(StatusEnum.DISPONIVEL);
                jazigoRepository.save(jazigo);

                // Enviar o e-mail de aviso
                String[] email = {servico.getCliente().getEmail()};
                String assunto = "Lembrete de Exumação";
                String mensagem = "Lembrete: Hoje é o dia da exumação do seu pet no cemitério.";
                emailService.sendEmail(email, assunto, mensagem);
            }
        }
    }


    // Checa a cada 2 minutos se há alguma reunião para ser realizada, e envia um email pro cliente caso a reunião dele seja hoje
    @Scheduled(cron = "0 */2 * * * ?") // Executa a cada dois minutos
    public void checaReunioes() {
        LocalDate dataAtual = LocalDate.now();
        List<Reuniao> reunioes = reuniaoRepository.findAll();

        for (Reuniao reuniao : reunioes) {

            if (reuniao.getData().equals(dataAtual)) {
                // Enviar o e-mail de aviso
                String[] email = {reuniao.getCliente().getEmail()};
                String assunto = "Lembrete de Reunião";
                String mensagem = "Lembrete: Hoje é o dia da sua reunião, que irá ocorrer no horário " + reuniao.getHorario() + ", no cemitério.";
                emailService.sendEmail(email, assunto, mensagem);
            }
        }
    }

    // Checa a cada 2 minutos se há algum pagamento pendente de aluguel para os clientes, e envia um email pro cliente caso o pagamento dele esteja atrasado
    //TODO: Aplicar a classe pagamento aqui quando ela for feita. Por enquanto, está sendo utilizada a classe servico.
    @Scheduled(cron = "0 */2 * * * ?") // Executa a cada dois minutos
    public void checaPagamentoAluguel() {
        LocalDate dataAtual = LocalDate.now();
        List<Servico> servicos = servicoRepository.findAll();

        // Verifica se o serviço é aluguel e se o pagamento está atrasado
        for (Servico servico : servicos) {
            if(servico.getTipoServico() != Servico.ServicoEnum.ALUGUEL) {
                continue;
            }
            LocalDate dataPrimeiroPagamento = servico.getPrimeiroPagamento();

            if (dataPrimeiroPagamento.plusMonths(1).isBefore(dataAtual)) {
                // Enviar o e-mail de aviso
                String[] email = {servico.getCliente().getEmail()};
                String assunto = "Lembrete de Pagamento";
                String mensagem = "Lembrete: O pagamento do serviço de aluguel do seu jazigo está atrasado. Por favor, entre em contato conosco para regularizar a situação.";
                emailService.sendEmail(email, assunto, mensagem);
                servico.getCliente().setInadimplente(true);
            }
        }
    }

    // Checa a cada 2 minutos há algum pagamento pendente de manutenção para os clientes, e envia um email pro cliente caso o pagamento dele esteja atrasado
    //TODO: Aplicar a classe pagamento aqui quando ela for feita. Por enquanto, está sendo utilizada a classe servico.
    @Scheduled(cron = "0 */2 * * * ?") // Executa a cada dois minutos
    public void checaPagamentoManutencao() {
        LocalDate dataAtual = LocalDate.now();
        List<Servico> servicos = servicoRepository.findAll();

        // Verifica se o serviço é manutenção e se o pagamento está atrasado
        for (Servico servico : servicos) {
            if(servico.getTipoServico() != Servico.ServicoEnum.MANUTENCAO) {
                continue;
            }
            LocalDate dataPrimeiroPagamento = servico.getPrimeiroPagamento();

            if (dataPrimeiroPagamento.plusYears(1).isBefore(dataAtual)) {
                // Enviar o e-mail de aviso
                String[] email = {servico.getCliente().getEmail()};
                String assunto = "Lembrete de Pagamento";
                String mensagem = "Lembrete: O pagamento do serviço de manutenção do seu jazigo está atrasado. Por favor, entre em contato conosco para regularizar a situação.";
                emailService.sendEmail(email, assunto, mensagem);
                servico.getCliente().setInadimplente(true);
            }
        }
    }

    // Checa à meia-noite se há algum lembrete de visita para ser enviado, e envia um email pro cliente caso a visita dele seja hoje
    @Scheduled(cron = "0 0 0 * * ?") // Executa à meia-noite
    public void checaLembretes() {
        LocalDate dataAtual = LocalDate.now();
        // Checa todos os lembretes que ainda não foram enviados
        List<Lembrete> lembretes = lembreteRepository.findAllByEnviado(false);

        for (Lembrete lembrete : lembretes) {
            if (lembrete.getData().equals(dataAtual)) {
                // Enviar o e-mail de aviso
                String[] email = {lembrete.getCliente().getEmail()};
                String assunto = "Lembrete de Visita";
                String mensagem = "Lembrete: Hoje é o dia da sua visita no cemitério.";
                emailService.sendEmail(email, assunto, mensagem);
            }
        }
    }
}
