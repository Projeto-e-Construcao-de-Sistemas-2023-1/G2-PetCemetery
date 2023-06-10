package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class ReuniaoDTO {
    private String cpfCliente;
    private String data;
    private String assunto;
    private String horario;

    public ReuniaoDTO(String cpfCliente, LocalDate data, String assunto, LocalTime horario) {
        this.cpfCliente = cpfCliente;
        this.data = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.assunto = assunto;
        this.horario = horario.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
