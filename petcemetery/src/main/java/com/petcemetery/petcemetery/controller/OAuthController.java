/* 

package com.petcemetery.petcemetery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcemetery.petcemetery.repositorio.ClienteRepository;

@RestController
@RequestMapping("/api")
public class OAuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/loginOAuth")
    public String loginWithGoogle() {
        // Redirecionar para a página de login do Google
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/callback")
    public String handleGoogleCallback(@AuthenticationPrincipal OAuth2User oauth2User) {
        // Obter os detalhes do usuário autenticado via OAuth do Google
        String email = oauth2User.getAttribute("email");
        String nome = oauth2User.getAttribute("name");
        String cpf = oauth2User.getAttribute("cpf");
        // implementar passando valores extraidos do OAuth
        if (clienteRepository.findByEmail(email) == null){
            return "redirect:/cadastro";
        }
        else
            return "redirect:/" + cpf;
    }
}

*/