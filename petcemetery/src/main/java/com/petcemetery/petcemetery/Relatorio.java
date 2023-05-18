package com.petcemetery.petcemetery;

import java.sql.Date;

public class Relatorio {
    
    private int idRelatorio;
    private String tipo;
    private Date dataRelatorio;
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
