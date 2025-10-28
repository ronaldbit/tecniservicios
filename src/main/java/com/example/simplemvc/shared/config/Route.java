package com.example.simplemvc.shared.config;

import lombok.Data;

@Data
public class Route {
  private String[] routes;
  private Method[] methods;

  public enum Method {
    GET,
    POST,
    PUT,
    DELETE;
  }
}
