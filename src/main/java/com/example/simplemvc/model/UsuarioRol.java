package com.example.simplemvc.model;

import org.springframework.security.core.GrantedAuthority;

public enum UsuarioRol implements GrantedAuthority {
    
  ADMIN,
  USER;

  @Override
  public String getAuthority() {
    return "ROLE_" + name();
  }
}