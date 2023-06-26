package com.petcemetery.petcemetery.DTO;

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

    public ExibirServicoDTO() {}
}
