package com.example.simplemvc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.model.Favorito;
import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.Producto;
import com.example.simplemvc.repository.FavoritoRepository;
import com.example.simplemvc.repository.PersonaRepository;
import com.example.simplemvc.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final PersonaRepository personaRepository;
    private final ProductoRepository productoRepository;
    private final ProductoService productoService; // asumiendo que ya existe

    public Set<Long> obtenerIdsFavoritosDeCliente(Long personaId) {
        return favoritoRepository.findByPersonaId(personaId).stream().map(f -> f.getProducto().getIdProducto()).collect(Collectors.toSet());
    }

    public List<ProductoDto> listarFavoritosCliente(Long personaId) {
        return favoritoRepository.findByPersonaId(personaId).stream()
                .map(Favorito::getProducto)
                .map(p -> productoService.findById(p.getIdProducto()))  // adapta según tu mapper real
                .collect(Collectors.toList());
    }

    public boolean esFavorito(Long personaId, Long productoId) {
        return favoritoRepository.existsByPersonaIdAndProductoId(personaId, productoId);
    }

    @Transactional
    public boolean toggleFavorito(Long personaId, Long productoId) {
        boolean existe = favoritoRepository.existsByPersonaIdAndProductoId(personaId, productoId);
        if (existe) {
            favoritoRepository.deleteByPersonaIdAndProductoId(personaId, productoId);
            return false; // ahora ya NO es favorito
        } else {
            Persona persona = personaRepository.findById(personaId)
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            Favorito fav = Favorito.builder()
                    .persona(persona)
                    .producto(producto)
                    .createdAt(LocalDateTime.now())
                    .build();
            favoritoRepository.save(fav);
            return true; // ahora SÍ es favorito
        }
    }
}
