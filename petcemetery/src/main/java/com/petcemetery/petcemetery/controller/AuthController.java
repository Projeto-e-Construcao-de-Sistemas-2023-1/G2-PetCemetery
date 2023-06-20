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

        System.out.println(loginRequest);

        if (cliente != null) {
            System.out.println("cliente logado com sucesso");
            return ResponseEntity.ok("OK;cliente;" + cliente.getCpf()); // redireciona para home do cliente
        } else if (admin != null) {
            return ResponseEntity.ok("OK;admin;" + admin.getCpf()); // redireciona para home do admin
        } else {
            cliente = clienteRepository.findByEmail(email);
            admin = adminRepository.findByEmail(email);

            if (cliente == null && admin == null) {
                System.out.println("Email inválido");
                return ResponseEntity.ok("ERR;email_invalido;"); // Exibe uma mensagem de email errado
            } else {
                System.out.println("Senha inválida");
                return ResponseEntity.ok("ERR;senha_invalida;"); // Exibe uma mensagem de senha errada
            }
        }
    }

    @PostMapping(path = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cadastro(@RequestBody Map<String, Object> requestBody) {

        String email = (String) requestBody.get("email");
        String senha = (String) requestBody.get("senha");
        String senha_repetida = (String) requestBody.get("senharepeat");
        String cpf = (String) requestBody.get("cpf");
        String cep = (String) requestBody.get("cep");
        String rua = (String) requestBody.get("rua");
        String numero = (String) requestBody.get("numero");
        String complemento = "";
        if (!StringUtils.isBlank((String) requestBody.get("complemento"))) {
            complemento = (String) requestBody.get("complemento");
        } //complemento eh opcional
        String nome = (String) requestBody.get("nome");
        String telefone = (String) requestBody.get("telefone");

        System.out.println(requestBody);

        // Checa se algum dos campos não foi preenchido e exibe uma mensagem de erro
        if (StringUtils.isBlank(nome) || StringUtils.isBlank(email) || StringUtils.isBlank(senha)
                || StringUtils.isBlank(senha_repetida)
                || StringUtils.isBlank(cep) || StringUtils.isBlank(rua) || StringUtils.isBlank(numero) || StringUtils.isBlank(cpf)
                || StringUtils.isBlank(telefone)) {
            System.out.println("Preencha todos os campos");
            return ResponseEntity.ok("ERR;campo_vazio");
            // Formato: STATUS;dados

            // Checa se a senha é igual a senha repetida e exibe uma mensagem de erro
        } else if (!senha.equals(senha_repetida)) {
            System.out.println(senha + senha_repetida);
            System.out.println("Senhas devem ser iguais");
            return ResponseEntity.ok("ERR;senhas_diferentes");

            // Checa se o email é válido através de regex e exibe uma mensagem de erro
        } else if (!EmailValidator.isValid(email)) {
            System.out.println("Email deve ser valido");
            return ResponseEntity.ok("ERR;email_invalido");
            // Checa se já existe um cliente cadastrado com o email fornecido e exibe uma
            // mensagem de erro
        } else if (clienteRepository.findByEmail(email) != null) {
            System.out.println("Email ja cadastrado");
            return ResponseEntity.ok("ERR;email_ja_cadastrado");

            // Adiciona o cliente no banco de dados
        } else {
            clienteRepository.save(new Cliente(email, telefone, nome, cpf, cep, rua, numero, complemento, senha));
            System.out.println("Cliente adicionado no banco com sucesso.");
            return ResponseEntity.ok("OK;" + cpf);
        }
    }
}