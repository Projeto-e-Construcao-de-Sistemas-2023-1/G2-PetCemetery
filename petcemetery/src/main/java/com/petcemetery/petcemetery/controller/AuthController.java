package com.petcemetery.petcemetery.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.model.Admin;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.outros.EmailValidator;
import com.petcemetery.petcemetery.repositorio.AdminRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String senha = loginRequest.get("senha");

        Cliente cliente = clienteRepository.findByEmailAndSenha(email, senha);
        Admin admin = adminRepository.findByEmailAndSenha(email, senha);

        if (cliente != null) {
            return ResponseEntity.ok("redirect:/clientes/" + cliente.getCpf() + "/home"); //redireciona para home do cliente
        } else if (admin != null) {
            return ResponseEntity.ok("redirect:/admin/home"); //redireciona para home do admin
        }
        else {
            return ResponseEntity.badRequest().body("Informações inválidas."); //retorna para pagina de login
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody Cliente cliente, @RequestParam("senha_repetida") String senha_repetida) {
        
        //Checa se algum dos campos não foi preenchido e exibe uma mensagem de erro
        if(cliente.getNome().isEmpty() || cliente.getEmail().isEmpty() || cliente.getSenha().isEmpty() || senha_repetida.isEmpty() || cliente.getCep().isEmpty() || 
           cliente.getRua().isEmpty() || cliente.getNumero() <= 0 || cliente.getCpf().isEmpty() || cliente.getTelefone().isEmpty()) {
            return ResponseEntity.badRequest().body("Preencha todos os campos obrigatórios.");

        //Checa se a senha é igual a senha repetida e exibe uma mensagem de erro
        } else if (!cliente.getSenha().equals(senha_repetida)) {
            return ResponseEntity.badRequest().body("As senhas devem ser iguais.");

        //Checa se o email é válido através de regex e exibe uma mensagem de erro
        } else if (!EmailValidator.isValid(cliente.getEmail())) {
            return ResponseEntity.badRequest().body("Digite um e-mail válido.");
        //Checa se já existe um cliente cadastrado com o email fornecido e exibe uma mensagem de erro
        } else if (clienteRepository.findByEmail(cliente.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Esse e-mail já está cadastrado.");

        //Adiciona o cliente no banco de dados
        } else {
            clienteRepository.save(cliente);
            System.out.println("Cliente adicionado no banco com sucesso.");
            return ResponseEntity.ok("redirect:/login");
        }
    }
}