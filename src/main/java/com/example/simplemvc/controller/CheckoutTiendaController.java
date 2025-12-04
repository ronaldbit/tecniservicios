package com.example.simplemvc.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.simplemvc.dto.CheckoutPrepareResponse;
import com.example.simplemvc.model.Venta;
import com.example.simplemvc.request.CheckoutRequest;
import com.example.simplemvc.service.VentaService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tienda/checkout")
@RequiredArgsConstructor
public class CheckoutTiendaController {

  @Value("${payshop.public-key}")
  private String publicKey;

  @Value("${payshop.secret-key}")
  private String secretKey;

  @Value("${payshop.mode:sandbox}")
  private String mode;

  private final VentaService ventaService;

  @PostMapping("/prepare")
  public ResponseEntity<CheckoutPrepareResponse> prepare(
      @RequestBody CheckoutRequest request,
      HttpSession session) throws Exception {

          System.out.println(">>> ENTRO A /api/tienda/checkout/prepare <<<");
  System.out.println(">>> EMAIL: " + request.getEmail());

    // 1. Crear la venta ONLINE a partir del checkout
    String orderId = "WEB-" + System.currentTimeMillis();
    Venta venta = ventaService.crearVentaOnlineDesdeCheckout(request, orderId);

    // Guardar cosas en sesión (por si quieres usarlas al volver del pago)
    session.setAttribute("checkoutVentaId", venta.getIdVenta());
    session.setAttribute("checkoutEmail", request.getEmail());

    // 2. Parámetros para PayShop (sin signature de momento)
    Map<String, String> params = new HashMap<>();
    params.put("mode", mode);
    params.put("public_key", publicKey);
    params.put("order_id", orderId);
    params.put("amount", venta.getTotal().toPlainString());
    params.put("currency", "PEN");
    params.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
    params.put("nonce", UUID.randomUUID().toString().replace("-", ""));
    params.put("_redirect", "1"); // este NO se firma

    // 3. Generar firma
    String signature = generarSignature(params, secretKey);
    params.put("signature", signature);

    // 4. Respuesta para el front
    CheckoutPrepareResponse response = new CheckoutPrepareResponse();
    response.setActionUrl("https://pay.doolpool.com/checkout/create");
    response.setParams(params);

    return ResponseEntity.ok(response);
  }

  private String generarSignature(Map<String, String> params, String secretKey) throws Exception {
    // Clonamos sin signature ni _redirect
    Map<String, String> fil = new HashMap<>();
    for (Map.Entry<String, String> e : params.entrySet()) {
      if ("signature".equals(e.getKey()) || "_redirect".equals(e.getKey())) continue;
      fil.put(e.getKey(), e.getValue());
    }

    List<String> keys = new ArrayList<>(fil.keySet());
    Collections.sort(keys);

    StringBuilder baseString = new StringBuilder();
    for (String k : keys) {
      if (baseString.length() > 0) {
        baseString.append("&");
      }
      baseString.append(k)
                .append("=")
                .append(URLEncoder.encode(fil.get(k), StandardCharsets.UTF_8));
    }

    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    byte[] rawHmac = mac.doFinal(baseString.toString().getBytes(StandardCharsets.UTF_8));

    StringBuilder sb = new StringBuilder();
    for (byte b : rawHmac) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}


