package com.petcemetery.petcemetery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.petcemetery.petcemetery.model.Admin;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.repositorio.AdminRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;

@Controller
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/login")
    public String loginGet() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("email") String email, @RequestParam("senha") String senha) {
        Cliente cliente = clienteRepository.findByEmailAndSenha(email, senha);
        Admin admin = adminRepository.findByEmailAndSenha(email, senha);
        if (cliente != null) {
            return "redirect:/cliente_home"; //redireciona para home do cliente
        } else if (admin != null) {
            return "redirect:/admin_home"; //redireciona para home do admin
        }
        else {
            return "redirect:/login"; //retorna para pagina de login
        }
    }

    
    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }
}
