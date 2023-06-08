package com.petcemetery.petcemetery.DTO;

import com.petcemetery.petcemetery.model.Jazigo.PlanoEnum;
import com.petcemetery.petcemetery.model.Servico.ServicoEnum;

import lombok.Data;

@Data
public class ExibirServicoDTO {

    double precoCompra;
    double precoAluguel;
    double precoBasic;
    double precoSilver;
    double precoGold;
    double precoEnterro;
    double precoManutencao;
    double precoExumacao;

    public ExibirServicoDTO() {
        this.precoCompra = ServicoEnum.COMPRA.getPreco();
        this.precoAluguel = ServicoEnum.ALUGUEL.getPreco();
        this.precoBasic = PlanoEnum.BASIC.getPreco();
        this.precoSilver = PlanoEnum.SILVER.getPreco();
        this.precoGold = PlanoEnum.GOLD.getPreco();
        this.precoEnterro = ServicoEnum.ENTERRO.getPreco();
        this.precoManutencao = ServicoEnum.MANUTENCAO.getPreco();
        this.precoExumacao = ServicoEnum.EXUMACAO.getPreco();
    }
}
