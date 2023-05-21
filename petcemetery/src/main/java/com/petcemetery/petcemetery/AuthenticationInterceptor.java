package com.petcemetery.petcemetery;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.petcemetery.petcemetery.model.Cliente;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("email");
        if (cliente == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
