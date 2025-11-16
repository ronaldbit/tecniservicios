package com.example.simplemvc.controller;

import com.example.simplemvc.dto.apiperu.DniData;
import com.example.simplemvc.dto.apiperu.RucData;
import com.example.simplemvc.dto.apiperu.StdOk;
import com.example.simplemvc.service.ApiPeruClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consulta")
@AllArgsConstructor
public class ConsultaDocController {

  private final ApiPeruClient api;

  @GetMapping("/dni")
  public ResponseEntity<StdOk<DniData>> dni(@RequestParam("num") String num) {
    StdOk<DniData> response = api.consultarDni(num);
    return ResponseEntity.status(response.success() ? 200 : 400).body(response);
  }

  @GetMapping("/ruc")
  public ResponseEntity<StdOk<RucData>> ruc(@RequestParam("num") String num) {
    StdOk<RucData> response = api.consultarRuc(num);
    return ResponseEntity.status(response.success() ? 200 : 400).body(response);
  }
}
