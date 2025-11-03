package com.example.simplemvc.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.simplemvc.model.Persona;
import com.example.simplemvc.repository.PersonaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificacionService {

    private final PersonaRepository personaRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void verificarEmail(String token) {
        log.info("Verificando email con token: {}", token);
     
        
        if (!jwtTokenUtil.validateToken(token)) {
            log.error("Token inválido o expirado: {}", token);
            throw new IllegalArgumentException("Token inválido o expirado.");
        }

        String email = jwtTokenUtil.extractEmail(token);
        
        if (email == null) {
            log.error("Token inválido: {}", token);
            throw new IllegalArgumentException("Token inválido o expirado.");
        }
        Optional<Persona> optionalPersona = personaRepository.findByEmail(email);
        if (optionalPersona.isEmpty()) {
            log.error("Persona no encontrada para el email: {}", email);
            throw new IllegalArgumentException("Persona no encontrada.");
        }

        Persona persona = optionalPersona.get();
        if (persona.getEmailVerificado()) {
            log.info("El email ya ha sido verificado para la persona con ID: {}", persona.getId());
            return;
        }
        
        persona.setEmailVerificado(true);
        persona.setTokenVerificacionEmail(null);
               
        Persona temp = personaRepository.save(persona);
        System.out.println("Persona verificada: " + temp.getId());        
        log.info("Email verificado exitosamente para la persona con ID: {}", persona.getId());
    }

}
