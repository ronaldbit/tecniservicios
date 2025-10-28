package com.example.simplemvc.dto.apiperu;

public record StdOk<T>(boolean success, String message, T data) {
  public static <T> StdOk<T> ok(T data) {
    return new StdOk<>(true, null, data);
  }

  public static <T> StdOk<T> fail(String msg) {
    return new StdOk<>(false, msg, null);
  }
}
