package com.example.simplemvc.service;

import org.springframework.stereotype.Service;

import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.repository.PersonaRepository;
import com.example.simplemvc.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificacionService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtTokenEmail jwtTokenUtil;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public void verificarEmailYGuardarPassword(String token, String password) {
        log.info("Verificando email con token: {}", token);
        if (!jwtTokenUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token inválido o expirado.");
        }
        String email = jwtTokenUtil.extractEmail(token);
        if (email == null) {
            throw new IllegalArgumentException("Token inválido o expirado.");
        }
        Persona persona = personaRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada."));

        if (persona.getEmailVerificado()) {
            throw new IllegalArgumentException("El correo ya fue verificado anteriormente.");
        }
        persona.setEmailVerificado(true);
        persona.setTokenVerificacionEmail(null);
        personaRepository.save(persona);

        var usuarioOpt = usuarioRepository.findByPersona_Id(persona.getId());
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró usuario asociado a esta persona.");
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(passwordEncoder.encode(password));
        usuarioRepository.save(usuario);

        log.info("Email verificado y contraseña actualizada para el usuario ID {}", usuario.getId());
    }
}
