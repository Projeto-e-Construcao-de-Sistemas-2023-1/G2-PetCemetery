package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;

import com.petcemetery.petcemetery.model.HistoricoServicos;

import lombok.Data;

@Data
public class VisualizarDespesasDTO {
    private String tipoServico;
    private double valor;
    private LocalDate ultimoPagamento;
    private LocalDate dataVencimento;

    // Retorna para o front uma lista de despesas, com o tipo de servico, valor. Caso seja um aluguel ou manutenção, também retornará data de vencimento e data do ultimo pagamento.
    public VisualizarDespesasDTO(HistoricoServicos historicoServicos){
        this.tipoServico = historicoServicos.getTipoServico().toString();
        this.valor = historicoServicos.getValor();
        // Caso o servico for um aluguel, a data de vencimento sera o mes seguinte ao ultimo pagamento
        if(historicoServicos.getTipoServico().toString().equals("ALUGUEL")) {
            this.dataVencimento = historicoServicos.getUltimoPagamento().plusMonths(1);
            this.ultimoPagamento = historicoServicos.getUltimoPagamento();
        }
        // Caso o servico for uma manutencao, a data de vencimento sera o ano seguinte ao ultimo pagamento
        else if (historicoServicos.getTipoServico().toString().equals("MANUTENCAO")){
            this.dataVencimento = historicoServicos.getUltimoPagamento().plusYears(1);
            this.ultimoPagamento = historicoServicos.getUltimoPagamento();
        }
        // Caso não seja nenhum dos dois acima, a data de vencimento é nula, e não precisa ser exibida. Os outros serviços serão apenas visualizáveis, como um extrato.
        else {
            this.dataVencimento = null;
            this.ultimoPagamento = historicoServicos.getUltimoPagamento();
        }
    }
}
