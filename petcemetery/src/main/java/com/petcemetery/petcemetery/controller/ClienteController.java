package com.petcemetery.petcemetery.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.outros.EmailValidator;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/api/cliente/{cpf}")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;


    // Recebe as informações que o cliente deseja mudar, em JSON, e altera no banco de dados
    @PostMapping(path = "/editar-perfil", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editarPerfil(@RequestBody Map<String, Object> requestBody,
            @PathVariable("cpf") String cpf) {

        Cliente cliente = clienteRepository.findByCpf(cpf);

        // Vai passar por todos os valores do forms de editar perfil, para verificar
        // quais foram preenchidos e quais não foram
        for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
            String campo = entry.getKey();
            Object valor = entry.getValue();

            if (!StringUtils.isBlank((String) valor)) {
                // Atualizar o campo correspondente no cliente
                switch (campo) {
                    case "nome":
                        cliente.setNome((String) valor);
                        break;
                    case "email":
                        if (!EmailValidator.isValid((String) valor)) {
                            System.out.println("Formato do email inválido");
                            return ResponseEntity.ok("ERR;email_invalido");
                        }
                        cliente.setEmail((String) valor);
                        break;
                    case "telefone":
                        cliente.setTelefone((String) valor);
                        break;
                    case "rua":
                        cliente.setRua((String) valor);
                        break;
                    case "numero":
                        cliente.setNumero((String) valor);
                        break;
                    case "complemento":
                        cliente.setComplemento((String) valor);
                        break;
                    case "cep":
                        cliente.setCep((String) valor);
                        break;
                    case "senha":
                        String senha_repetida = (String) requestBody.get("senharepeat");
                        if (!valor.equals(senha_repetida)) {
                            System.out.println("Senhas nao coincidem");
                            return ResponseEntity.ok("ERR;senhas_nao_coincidem");
                        }
                        cliente.setSenha((String) valor);
                        break;
                }
            }
        }

        clienteRepository.save(cliente);

        System.out.println("Informaçoes alteradas com sucesso.");
        // Se tudo ocorrer certo (validações) o cliente é redirecionado para uma página
        // de confirmação.
        return ResponseEntity.ok("OK;informacoes_alteradas_com_sucesso");
    }

    // Desativa o perfil do Cliente quando solicitado
    @PostMapping("/desativar-perfil")
    public ResponseEntity<?> desativarPerfil(@PathVariable("cpf") String cpf) {

        System.out.println(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);

        if (cliente.getDesativado()) {
            return ResponseEntity.ok("ERR;conta_desativada");
        }

        cliente.setDesativado(true);

        clienteRepository.save(cliente);
        return ResponseEntity.ok("OK;");
    }

    // Retorna os dados do cliente, menos a senha e CPF, na tela AlterarPerfil.js, para serem exibidos no editar perfil. Isso evita o cliente ter que escrever tudo de novo.
    @GetMapping("/get-alterar-perfil") 
    public ResponseEntity<?> getAlterarPerfil(@PathVariable("cpf") String cpf) {

        System.out.println(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);

        if (cliente.getDesativado()) {
            return ResponseEntity.ok("ERR;conta_desativada");
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("OK;" + cliente.getEmail() + ";" + cliente.getNome() + ";" + cliente.getTelefone() + ";" + cliente.getRua() + ";" + cliente.getNumero() + ";" + cliente.getComplemento() + ";" + cliente.getCep());
    }


    // Exibe o nome e o email do perfil do Cliente na página EditarPerfil.js
    @GetMapping("/exibir-perfil")
    public ResponseEntity<?> exibirPerfil(@PathVariable("cpf") String cpf) {
        System.out.println(cpf);
        Cliente cliente = clienteRepository.findByCpf(cpf);

        if (cliente.getDesativado()) {
            return ResponseEntity.ok("ERR;conta_desativada");
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("OK;" + cliente.getNome() + ";" + cliente.getEmail());
    }
}