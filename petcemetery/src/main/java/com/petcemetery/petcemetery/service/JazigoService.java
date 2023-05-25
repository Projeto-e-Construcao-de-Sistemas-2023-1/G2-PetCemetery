package com.petcemetery.petcemetery.service;

import com.petcemetery.petcemetery.model.Jazigo;
import com.petcemetery.petcemetery.repositorio.JazigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JazigoService {
    private final JazigoRepository jazigoRepository;

    @Autowired
    public JazigoService(JazigoRepository jazigoRepository) {
        this.jazigoRepository = jazigoRepository;
    }

    public List<Jazigo> getAllJazigos() {
        return jazigoRepository.findAll();
    }

    public Jazigo getJazigoById(Long id) {
        return jazigoRepository.findById(id).orElse(null);
    }
}
