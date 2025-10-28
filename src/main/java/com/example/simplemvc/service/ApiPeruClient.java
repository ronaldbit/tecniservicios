package com.example.simplemvc.service;

import com.example.simplemvc.dto.apiperu.DniData;
import com.example.simplemvc.dto.apiperu.DniResponse;
import com.example.simplemvc.dto.apiperu.RucData;
import com.example.simplemvc.dto.apiperu.RucResponse;
import com.example.simplemvc.dto.apiperu.StdOk;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiPeruClient {
  private final RestTemplate http;
  private final String baseUrl;
  private final String token;

  public ApiPeruClient(
      RestTemplate http,
      @Value("${apiperu.base-url}") String baseUrl,
      @Value("${apiperu.token}") String token) {
    this.http = http;
    this.baseUrl = baseUrl;
    this.token = token;
  }

  public StdOk<DniData> consultarDni(String dni) {
    if (dni == null || !dni.matches("\\d{8}")) return StdOk.fail("DNI inválido (8 dígitos).");

    try {
      HttpHeaders h = new HttpHeaders();
      h.setContentType(MediaType.APPLICATION_JSON);
      h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
      h.setBearerAuth(token);
      HttpEntity<Map<String, String>> req = new HttpEntity<>(Map.of("dni", dni), h);

      ResponseEntity<DniResponse> resp =
          http.exchange(baseUrl + "/dni", HttpMethod.POST, req, DniResponse.class);

      DniResponse body = resp.getBody();
      if (body == null) return StdOk.fail("Sin respuesta.");
      if (!body.success())
        return StdOk.fail(body.message() != null ? body.message() : "No encontrado.");
      return StdOk.ok(body.data());
    } catch (RestClientException ex) {
      return StdOk.fail("Error de red/servicio.");
    }
  }

  public StdOk<RucData> consultarRuc(String ruc) {
    if (ruc == null || !ruc.matches("\\d{11}")) return StdOk.fail("RUC inválido (11 dígitos).");

    try {
      HttpHeaders h = new HttpHeaders();
      h.setContentType(MediaType.APPLICATION_JSON);
      h.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
      h.setBearerAuth(token);
      HttpEntity<Map<String, String>> req = new HttpEntity<>(Map.of("ruc", ruc), h);

      ResponseEntity<RucResponse> resp =
          http.exchange(baseUrl + "/ruc", HttpMethod.POST, req, RucResponse.class);

      RucResponse body = resp.getBody();
      if (body == null) return StdOk.fail("Sin respuesta.");
      if (!body.success())
        return StdOk.fail(body.message() != null ? body.message() : "No encontrado.");
      return StdOk.ok(body.data());
    } catch (RestClientException ex) {
      return StdOk.fail("Error de red/servicio.");
    }
  }
}
