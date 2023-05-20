package com.petcemetery.petcemetery;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "Visita")
@Table(name = "Visita")
public class Visita {
    
    @ManyToOne
    @JoinColumn(name = "cpf_cliente", referencedColumnName = "cpf")
    private Cliente proprietario;

    @Column(name = "data_visita")
    @Temporal(TemporalType.DATE)
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
