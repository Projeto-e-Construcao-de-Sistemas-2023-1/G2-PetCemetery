package com.petcemetery.petcemetery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.petcemetery.petcemetery.model.Admin;
import com.petcemetery.petcemetery.model.Cliente;
import com.petcemetery.petcemetery.outros.EmailValidator;
import com.petcemetery.petcemetery.repositorio.AdminRepository;
import com.petcemetery.petcemetery.repositorio.ClienteRepository;


@Controller
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/login")
    public String exibirFormsLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("email") String email, @RequestParam("senha") String senha, Model model) {
        Cliente cliente = clienteRepository.findByEmailAndSenha(email, senha);
        Admin admin = adminRepository.findByEmailAndSenha(email, senha);
        if (cliente != null) {
            return "redirect:/{id_cliente}/home"; //redireciona para home do cliente
        } else if (admin != null) {
            return "redirect:/{id_admin}/home"; //redireciona para home do admin
        }
        else {
            model.addAttribute("mensagem", "Informações inválidas");
            return "redirect:/login"; //retorna para pagina de login
        }
    }

    
    @GetMapping("/cadastro")
    public String exibirFormsCadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastroPost(@RequestParam("email") String email, @RequestParam("telefone") String telefone, @RequestParam("nome") String nome, 
                               @RequestParam("cpf") String cpf, @RequestParam("cep") String cep, @RequestParam("rua") String rua, 
                               @RequestParam("numero") int numero, @RequestParam(value = "complemento", required=false) String complemento, @RequestParam("senha") String senha, 
                               @RequestParam("senha_repetida") String senha_repetida, Model model) {
        
        
        //Checa se algum dos campos não foi preenchido e exibe uma mensagem de erro
        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha_repetida.isEmpty() || cep.isEmpty() || rua.isEmpty() || numero <= 0 || cpf.isEmpty() || telefone.isEmpty()) {
            model.addAttribute("mensagem", "Preencha todos os campos obrigatórios.");
            return "redirect:/cadastro";

        //Checa se a senha é igual a senha repetida e exibe uma mensagem de erro
        } else if (!senha.equals(senha_repetida)) {
            model.addAttribute("mensagem", "As senhas devem ser iguais");
            return "redirect:/cadastro";

        //Checa se o email é válido através de regex e exibe uma mensagem de erro
        } else if (!EmailValidator.isValid(email)) {
            model.addAttribute("mensagem", "Digite um email válido.");
            return "redirect:/cadastro";

        //Checa se já existe um cliente cadastrado com o email fornecido e exibe uma mensagem de erro
        } else if (clienteRepository.findByEmail(email) != null) {
            model.addAttribute("mensagem", "Esse email já está cadastrado.");
            return "redirect:/cadastro";

        //Adiciona o cliente no banco de dados
        } else {
            clienteRepository.save(new Cliente(email, telefone, nome, cpf, cep, rua, numero, complemento, senha));
            System.out.println("Cliente adicionado no banco com sucesso.");
            return "redirect:/login";
        }
    }
}
