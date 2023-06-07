package com.petcemetery.petcemetery.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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


    public String getCpfCliente() {
        return this.cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getData() {
        return this.data;
    }   

    public void setData(String data) {
        this.data = data;
    }

    public String getAssunto() {
        return this.assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getHorario() {
        return this.horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
