package com.petcemetery.petcemetery.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Relatorio")
@Table(name = "Relatorio")
public class Relatorio {
    
    @Id
    @Column(name = "id_relatorio")
    private int idRelatorio;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data_relatorio")
    private Date dataRelatorio;

    @Column(name = "email")
    private String email;
    
    
    public Relatorio(int idRelatorio, String tipo, Date dataRelatorio, String email) {
        this.idRelatorio = idRelatorio;
        this.tipo = tipo;
        this.dataRelatorio = dataRelatorio;
        this.email = email;
    }

    public int getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(int idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(Date dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
