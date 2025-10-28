package com.example.simplemvc.controller;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.repository.UsuarioRepository;
import com.example.simplemvc.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excel")

// ** 1.EVALUAR EL TRABAJO CON USUARIO O UsuarioDTO**

public class ExcelController {

  @Autowired private ExcelService excelService;

  @Autowired private UsuarioRepository usuarioRepository;

  /**
   * Endpoint para generar un reporte de TODOS los usuarios. Llama a JPA, obtiene la lista y la pasa
   * al servicio de Excel.
   */
  @GetMapping("/reporte/usuarios")
  @PreAuthorize("hasRole('ADMIN')")
  public void generarReporteUsuarios(HttpServletResponse response) throws IOException {
    // 2. Definimos el título del reporte
    String titulo = "Reporte de Usuarios";

    // 3. Obtenemos los datos desde la base de datos
    List<Usuario> listaUsuarios = usuarioRepository.findAll();

    // 4. Preparamos el nombre del archivo
    String nombreArchivo = titulo.replaceAll("\\s+", "_") + ".xlsx";

    // 5. Configuramos la respuesta HTTP
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    String headerValue = "attachment; filename=\"" + nombreArchivo + "\"";
    response.setHeader("Content-Disposition", headerValue);

    // 6. Llamamos al servicio para generar el Excel
    // Le pasamos el título y la LISTA DE ENTIDADES JPA
    try (InputStream is = excelService.generarExcel(titulo, listaUsuarios)) {

      // 7. Escribimos el excel en el response
      is.transferTo(response.getOutputStream());
      response.flushBuffer();
    }
  }

  /*
   * // REPORTE POR ROL (FALTA AGREGAR UN findByRol EN EL REPOSITORY)
   *
   * @GetMapping("/reporte/usuarios/{rol}") public void
   * generarReporteUsuariosPorRol(
   *
   * @PathVariable String rol, HttpServletResponse response) throws IOException {
   *
   * String titulo = "Reporte de Usuarios por Rol: " + rol;
   *
   * // Buscamos con el método personalizado del repo List<Usuario> listaFiltrada
   * = usuarioRepository.findByRol(rol);
   *
   * String nombreArchivo = "Reporte_Rol_" + rol + ".xlsx";
   * response.setContentType(
   * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
   * response.setHeader("Content-Disposition", "attachment; filename=\"" +
   * nombreArchivo + "\"");
   *
   * try (InputStream is = excelService.generarExcel(titulo, listaFiltrada)) {
   * is.transferTo(response.getOutputStream()); response.flushBuffer(); } }
   */
}
