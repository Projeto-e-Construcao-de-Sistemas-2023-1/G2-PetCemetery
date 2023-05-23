package com.petcemetery.petcemetery.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.model.Admin;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.outros.EmailValidator;
import com.petcemetery.petcemetery.repositorio.AdminRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String senha = loginRequest.get("senha");

        Cliente cliente = clienteRepository.findByEmailAndSenha(email, senha);
        Admin admin = adminRepository.findByEmailAndSenha(email, senha);

        if (cliente != null) {
            System.out.println("cliente logado com sucesso");
            return ResponseEntity.ok("redirect:/clientes/" + cliente.getCpf() + "/home"); //redireciona para home do cliente
        } else if (admin != null) {
            return ResponseEntity.ok("redirect:/admin/home"); //redireciona para home do admin
        }
        else {
            System.out.println("informaçoes invalidas");
            return ResponseEntity.badRequest().body("Informações inválidas."); //Exibe uma mensagem de erro
        }
    }

    @PostMapping(path = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastro(@RequestBody Map<String, Object> requestBody) {
        
        String email = (String) requestBody.get("email");
        String senha = (String) requestBody.get("senha");
        String senha_repetida = (String) requestBody.get("senha_repetida");
        String cpf = (String) requestBody.get("cpf");
        String cep = (String) requestBody.get("cep");
        String rua = (String) requestBody.get("rua");
        Integer numero = (int) requestBody.get("numero");
        String complemento = (String) requestBody.get("complemento");
        String nome = (String) requestBody.get("nome");
        String telefone = (String) requestBody.get("telefone");

        System.out.println(senha_repetida);
        System.out.println(numero);
        System.out.println(requestBody);

        //Checa se algum dos campos não foi preenchido e exibe uma mensagem de erro
        if(StringUtils.isBlank(nome) || StringUtils.isBlank(email)  || StringUtils.isBlank(senha) || StringUtils.isBlank(senha_repetida) 
        || StringUtils.isBlank(cep) || StringUtils.isBlank(rua) || numero <= 0 || StringUtils.isBlank(cpf) || StringUtils.isBlank(telefone)) {
            System.out.println("Preencha todos os campos");
            return ResponseEntity.badRequest().body("Preencha todos os campos obrigatórios.");

        //Checa se a senha é igual a senha repetida e exibe uma mensagem de erro
        } else if (!senha.equals(senha_repetida)) {
            System.out.println(senha + senha_repetida);
            System.out.println("Senhas devem ser iguais");
            return ResponseEntity.badRequest().body("As senhas devem ser iguais.");

        //Checa se o email é válido através de regex e exibe uma mensagem de erro
        } else if (!EmailValidator.isValid(email)) {
            System.out.println("Email deve ser valido");
            return ResponseEntity.badRequest().body("Digite um e-mail válido.");
        //Checa se já existe um cliente cadastrado com o email fornecido e exibe uma mensagem de erro
        } else if (clienteRepository.findByEmail(email) != null) {
            System.out.println("Email ja cadastrado");
            return ResponseEntity.badRequest().body("Esse e-mail já está cadastrado.");

        //Adiciona o cliente no banco de dados
        } else {
            clienteRepository.save(new Cliente(email, telefone, nome, cpf, cep, rua, numero, complemento, senha));
            System.out.println("Cliente adicionado no banco com sucesso.");
            return ResponseEntity.ok("redirect:/login");
        }
    }
}