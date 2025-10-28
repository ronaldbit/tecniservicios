package com.example.simplemvc.shared.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.example.simplemvc.shared.annotation.Properties;
import com.example.simplemvc.shared.config.Route;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Properties
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
  private boolean allowCredentials;
  private List<String> allowedOrigins;
  private List<String> allowedHeaders;
  private List<String> exposedHeaders;
  private List<String> allowedMethods;
  private List<String> publicRoutes;
  private List<String> authEntryRoutes;
  private List<String> adminRoutes;
  private List<String> protectedRoutes;
  private List<Route> noAdminOperationToSelfRoutes;
}
