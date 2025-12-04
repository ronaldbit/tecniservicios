package com.example.simplemvc.dto;

import java.util.Map;
import lombok.Data;

@Data
public class CheckoutPrepareResponse {
  private String actionUrl;
  private Map<String, String> params;
}
