package com.example.simplemvc.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        Object clienteSesion = session.getAttribute("CLIENTE_SESION");
        model.addAttribute("clienteSesion", clienteSesion);
    }
}
