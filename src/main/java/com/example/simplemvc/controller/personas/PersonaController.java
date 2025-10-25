package com.example.simplemvc.controller.personas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personas")
public class PersonaController {

  @GetMapping
  public String mainView() {
    return "personas";
  }
}
