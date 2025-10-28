package com.example.simplemvc.controller;

import com.example.simplemvc.dto.apiperu.*;
import com.example.simplemvc.service.ApiPeruClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consulta")
public class ConsultaDocController {

  private final ApiPeruClient api;

  public ConsultaDocController(ApiPeruClient api) {
    this.api = api;
  }

  @GetMapping("/dni")
  public ResponseEntity<StdOk<DniData>> dni(@RequestParam("num") String num) {
    var r = api.consultarDni(num);
    return ResponseEntity.status(r.success() ? 200 : 400).body(r);
  }

  @GetMapping("/ruc")
  public ResponseEntity<StdOk<RucData>> ruc(@RequestParam("num") String num) {
    var r = api.consultarRuc(num);
    return ResponseEntity.status(r.success() ? 200 : 400).body(r);
  }
}
