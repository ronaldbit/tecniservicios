package com.example.simplemvc.dto.apiperu;

public record RucResponse(
  boolean success,
  RucData data,
  String message
) {}
