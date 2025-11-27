package com.example.simplemvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
  @Value("${spring.image.path}")
  private String uploadPath;
  @Value("${spring.doc.path}")
  private String docPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/productos/**")
        .addResourceLocations("file:" + uploadPath + "/");
    registry.addResourceHandler("/uploads/documentos/**")
        .addResourceLocations("file:" + docPath + "/");
  }
}