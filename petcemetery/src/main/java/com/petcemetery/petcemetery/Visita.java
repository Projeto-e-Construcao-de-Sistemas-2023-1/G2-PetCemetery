package com.petcemetery.petcemetery;

import java.sql.Date;

public class Visita {
    
    private Cliente proprietario;
    private Date dataVisita;
    
    public Visita(Cliente proprietario, Date dataVisita) {
        this.proprietario = proprietario;
        this.dataVisita = dataVisita;
    }

    public Cliente getProprietario() {
        return proprietario;
    }

    public void setProprietario(Cliente proprietario) {
        this.proprietario = proprietario;
    }

    public Date getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(Date dataVisita) {
        this.dataVisita = dataVisita;
    }

    

    
}
