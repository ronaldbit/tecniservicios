package com.example.simplemvc.config;

import com.example.simplemvc.shared.ClienteSesion;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addClienteSesion(Model model, HttpSession session) {
        ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("CLIENTE_SESION");
        model.addAttribute("clienteSesion", clienteSesion); // puede ser null
    }
}
