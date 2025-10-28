package com.example.simplemvc.shared.config;

import com.example.simplemvc.shared.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Autowired private SecurityProperties securityProperties;

  @Autowired private AuthInterceptor authInterceptor;

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    registry
        .addInterceptor(authInterceptor)
        .addPathPatterns(securityProperties.getProtectedRoutes())
        .addPathPatterns(securityProperties.getAuthEntryRoutes());
  }
}
