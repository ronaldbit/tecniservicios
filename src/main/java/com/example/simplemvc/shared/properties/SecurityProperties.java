package com.example.simplemvc.shared.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.simplemvc.shared.config.Route;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
  private boolean allowCredentials;
  private String[] allowedOrigins;
  private String[] allowedHeaders;
  private String[] exposedHeaders;
  private String[] allowedMethods;
  private String[] publicRoutes;
  private Route[] adminRoutes;
  private Route[] noAdminOperationToSelfRoutes;
}
