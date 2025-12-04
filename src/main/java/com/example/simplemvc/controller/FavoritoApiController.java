package com.example.simplemvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.service.FavoritoService;
import com.example.simplemvc.shared.ClienteSesion;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoApiController {

    private final FavoritoService favoritoService;

    @GetMapping("/mis")
    public ResponseEntity<?> misFavoritos(HttpSession session) {
        ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");
        if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
            return ResponseEntity.status(401).body("Debes iniciar sesión.");
        }

        List<ProductoDto> favoritos =
                favoritoService.listarFavoritosCliente(clienteSesion.getIdCliente());

        return ResponseEntity.ok(favoritos);
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleFavorito(@RequestParam Long productoId,
                                            HttpSession session) {
        ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");
        if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
            return ResponseEntity.status(401).body("Debes iniciar sesión para usar favoritos.");
        }

        boolean esFavorito = favoritoService.toggleFavorito(
                clienteSesion.getIdCliente(), productoId
        );

        Map<String, Object> resp = new HashMap<>();
        resp.put("favorito", esFavorito);
        return ResponseEntity.ok(resp);
    }
}
