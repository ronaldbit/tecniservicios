package com.example.simplemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.TipoDocumento;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
}
