package com.example.simplemvc.service;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.simplemvc.model.PedidoProveedor;
import com.example.simplemvc.model.PedidoProveedorDetalle;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ServicioCorreo {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String mailFrom;

  @Autowired
  public ServicioCorreo(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void enviarVerificacionCorreo(String email, String verificacionToken) {
    System.out.println(mailFrom);
    System.out.println("Enviando correo de verificación a: " + email);
    String subject = "Verificación de correo electrónico";
    String url = "/auth/verify";
    String mensaje = "Por favor, haga clic en el siguiente enlace para verificar su correo electrónico:";
    System.out.println("Enviando correo de verificación a: " + email);
    enviarEmail(email, verificacionToken, subject, url, mensaje);
  }

  public void enviarOlvidoCorreo(String email, String verificacionToken) {
    String subject = "Restablecimiento de contraseña";
    String url = "/auth/reset-password";
    String mensaje = "Por favor, haga clic en el siguiente enlace para restablecer su contraseña:";
    enviarEmail(email, verificacionToken, subject, url, mensaje);
  }

  private void enviarEmail(String email, String token, String subject, String url, String mensaje) {
    System.out.println("Enviando correo a: " + email);
    String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(url)
        .queryParam("token", token)
        .toUriString();
    System.out.println("URL de acción: " + actionUrl);
    String content = construirContenidoHTML(subject, mensaje, actionUrl, email);

    try {
      System.out.println("Preparando el mensaje de correo");
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setText(content, true);
      helper.setFrom(mailFrom);
      mailSender.send(mimeMessage);
      System.out.println("Correo enviado a: " + email);
    } catch (MessagingException e) {
      System.out.println("Error al preparar el mensaje de correo");
      System.err.println("Error al enviar correo: " + e.getMessage());
      throw new RuntimeException("Error al enviar correo", e);
    }
  }

  private String construirContenidoHTML(String subject, String mensaje, String actionUrl, String email) {
    String htmlTemplate = """
        <!doctype html>
        <html lang="es">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <title>%s</title>
          <style>
            body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial; margin:0; padding:0; background:#f4f6f8; }
            .wrapper { width:100%%; padding:40px 0; }
            .container { max-width:600px; margin:0 auto; background:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08); }
            .header { background:#0d6efd; color:#fff; padding:20px; text-align:center; }
            .content { padding:24px; color:#333333; line-height:1.5; }
            .btn { display:inline-block; padding:12px 20px; border-radius:6px; background:#0d6efd; color:#ffffff; text-decoration:none; font-weight:600; }
            .small { font-size:13px; color:#6b7280; }
            .fallback { word-break:break-all; color:#0d6efd; text-decoration:underline; }
            .footer { padding:16px 24px; background:#fafafa; color:#6b7280; font-size:13px; text-align:center; }
            @media (max-width:420px){ .content { padding:16px; } .header { padding:16px; } }
          </style>
        </head>
        <body>
          <div class="wrapper">
            <div class="container">
              <div class="header"><h1 style="margin:0; font-size:20px;">%s</h1></div>
              <div class="content">
                <p>Hola,</p>
                <p>%s</p>
                <p style="text-align:center; margin:28px 0;">
                  <a class="btn" href="%s" target="_blank" rel="noopener noreferrer">Abrir enlace</a>
                </p>
                <p class="small">Si el botón no funciona, copia y pega este enlace en tu navegador:</p>
                <p class="fallback"><a href="%s" target="_blank" rel="noopener noreferrer">%s</a></p>
                <hr style="border:none; border-top:1px solid #eee; margin:20px 0;">
                <p class="small">Si no solicitaste esta acción, puedes ignorar este correo. Por seguridad el enlace puede expirar.</p>
                <p style="margin-top:18px;">Saludos,<br><strong>Equipo de TecnIServicios</strong></p>
              </div>
              <div class="footer">Correo enviado a: %s — Si tienes problemas, responde a este correo.</div>
            </div>
          </div>
        </body>
        </html>
        """;
    return String.format(htmlTemplate, subject, subject, mensaje, actionUrl, actionUrl, actionUrl, email);
  }

  public void enviarNotificacionNuevoPedidoProveedor(PedidoProveedor pedido) {
    String emailDestino = pedido.getProveedor().getEmail();
    if (emailDestino == null || emailDestino.isBlank()) {
      System.err.println("Error: El proveedor '" + pedido.getProveedor().getRazonSocial()
          + "' no tiene un email registrado. No se envió la notificación.");
      return;
    }
    String subject = String.format("Solicitud de Cotización de TecnIServicios - Pedido N° %d", pedido.getId());
    String content = construirHtmlSolicitudCotizacion(pedido, subject);
    try {
      System.out.println("Preparando correo de solicitud para: " + emailDestino);
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(emailDestino);
      helper.setSubject(subject);
      helper.setText(content, true);
      helper.setFrom(mailFrom);
      mailSender.send(mimeMessage);
      System.out.println("Correo de solicitud enviado con éxito a: " + emailDestino);
    } catch (MessagingException e) {
      System.err.println("Error al enviar correo de solicitud de cotización: " + e.getMessage());
    }
  }

  private String construirHtmlSolicitudCotizacion(PedidoProveedor pedido, String subject) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaEmisionFmt = pedido.getFechaEmision().format(dtf);
    String fechaEsperadaFmt = (pedido.getFechaEntregaEsperada() != null)
        ? pedido.getFechaEntregaEsperada().format(dtf)
        : "Lo antes posible";
    StringBuilder filasProductos = new StringBuilder();
    for (PedidoProveedorDetalle detalle : pedido.getDetalles()) {
      filasProductos.append(String.format(
          """
                  <tr>
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                  </tr>
              """,
          detalle.getProducto().getCodigo(),
          detalle.getProducto().getNombre(),
          detalle.getCantidad().toString()));
    }
    String htmlTemplate = """
        <!doctype html>
        <html lang="es">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <title>%s</title>
          <style>
            body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial; margin:0; padding:0; background:#f4f6f8; }
            .wrapper { width:100%%; padding:40px 0; }
            .container { max-width:600px; margin:0 auto; background:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08); }
            .header { background:#0d6efd; color:#fff; padding:20px; text-align:center; }
            .content { padding:24px; color:#333333; line-height:1.5; }
            .footer { padding:16px 24px; background:#fafafa; color:#6b7280; font-size:13px; text-align:center; }
            .info-box { background: #f9f9f9; border-radius: 6px; padding: 16px; margin: 20px 0; }
            .info-box p { margin: 4px 0; }
            .product-table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
            .product-table th, .product-table td { border: 1px solid #ddd; padding: 8px; text-align: left; font-size: 14px; }
            .product-table th { background-color: #f2f2f2; }
            @media (max-width:420px){ .content { padding:16px; } .header { padding:16px; } }
          </style>
        </head>
        <body>
          <div class="wrapper">
            <div class="container">
              <div class="header"><h1 style="margin:0; font-size:20px;">%s</h1></div>
              <div class="content">
                <p>Estimados, <strong>%s</strong>,</p>
                <p>Por medio del presente, <strong>TecnIServicios</strong> solicita formalmente la cotización de los siguientes productos:</p>

                <div class="info-box">
                    <p><strong>N° de Solicitud:</strong> %s</p>
                    <p><strong>Fecha de Emisión:</strong> %s</p>
                    <p><strong>Fecha de Entrega Requerida:</strong> %s</p>
                </div>

                <h3 style="margin-top: 24px; border-bottom: 2px solid #eee; padding-bottom: 5px;">Detalle de Productos Solicitados</h3>
                <table class="product-table">
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Producto</th>
                            <th>Cantidad</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>

                <p style="margin-top: 24px;">Agradeceremos enviarnos su mejor oferta (precios unitarios y totales), incluyendo el tiempo de entrega estimado, a la brevedad posible.</p>
                <p>Quedamos a la espera de su pronta respuesta.</p>
                <p style="margin-top:18px;">Saludos cordiales,<br><strong>Equipo de Compras<br>TecnIServicios</strong></p>
              </div>
              <div class="footer">Este es un correo automático de solicitud. Correo de respuesta: %s</div>
            </div>
          </div>
        </body>
        </html>
        """;
    return String.format(
        htmlTemplate,
        subject,
        subject,
        pedido.getProveedor().getRazonSocial(),
        pedido.getId().toString(),
        fechaEmisionFmt,
        fechaEsperadaFmt,
        filasProductos.toString(),
        mailFrom);
  }

  public void enviarConfirmacionPedido(PedidoProveedor pedido) {
    String emailDestino = pedido.getProveedor().getEmail();
    if (emailDestino == null || emailDestino.isBlank()) {
      System.err.println("Error: El proveedor '" + pedido.getProveedor().getRazonSocial()
          + "' no tiene un email registrado. No se envió la confirmación.");
      return;
    }
    String subject = String.format("Confirmación de Orden de Compra N° %d - TecnIServicios", pedido.getId());
    String content = construirHtmlConfirmacionPedido(pedido, subject);
    try {
      System.out.println("Preparando correo de CONFIRMACIÓN para: " + emailDestino);
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

      helper.setTo(emailDestino);
      helper.setSubject(subject);
      helper.setText(content, true);
      helper.setFrom(mailFrom);

      mailSender.send(mimeMessage);
      System.out.println("Correo de CONFIRMACIÓN enviado con éxito a: " + emailDestino);
    } catch (MessagingException e) {
      System.err.println("Error al enviar correo de confirmación de pedido: " + e.getMessage());
    }
  }
  private String construirHtmlConfirmacionPedido(PedidoProveedor pedido, String subject) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaEmisionFmt = pedido.getFechaEmision().format(dtf);
    String fechaEsperadaFmt = (pedido.getFechaEntregaEsperada() != null)
        ? pedido.getFechaEntregaEsperada().format(dtf)
        : "No especificada";
    StringBuilder filasProductos = new StringBuilder();
    for (PedidoProveedorDetalle detalle : pedido.getDetalles()) {
      filasProductos.append(String.format(
          """
                  <tr>
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                  </tr>
              """,
          detalle.getProducto().getCodigo(),
          detalle.getProducto().getNombre(),
          detalle.getCantidad().toString()));
    }
    String htmlTemplate = """
        <!doctype html>
        <html lang="es">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
          <title>%s</title>
          <style>
            body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial; margin:0; padding:0; background:#f4f6f8; }
            .wrapper { width:100%%; padding:40px 0; }
            .container { max-width:600px; margin:0 auto; background:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08); }
            .header { background:#28a745; color:#fff; padding:20px; text-align:center; } /* Verde para confirmación */
            .content { padding:24px; color:#333333; line-height:1.5; }
            .footer { padding:16px 24px; background:#fafafa; color:#6b7280; font-size:13px; text-align:center; }
            .info-box { background: #f9f9f9; border-radius: 6px; padding: 16px; margin: 20px 0; }
            .info-box p { margin: 4px 0; }
            .product-table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
            .product-table th, .product-table td { border: 1px solid #ddd; padding: 8px; text-align: left; font-size: 14px; }
            .product-table th { background-color: #f2f2f2; }
            @media (max-width:420px){ .content { padding:16px; } .header { padding:16px; } }
          </style>
        </head>
        <body>
          <div class="wrapper">
            <div class="container">
              <div class="header"><h1 style="margin:0; font-size:20px;">%s</h1></div>
              <div class="content">
                <p>Estimados, <strong>%s</strong>,</p>
                <p>Confirmamos nuestra <strong>Orden de Compra</strong> basada en la cotización recibida. Por favor, proceda con el despacho de los siguientes productos según los términos acordados:</p>

                <div class="info-box">
                    <p><strong>N° de Orden de Compra:</strong> %s</p>
                    <p><strong>Fecha de Emisión:</strong> %s</p>
                    <p><strong>Fecha de Entrega Acordada:</strong> <strong style="color: #28a745;">%s</strong></p>
                    <p style="font-size: 1.2em; margin-top: 10px;"><strong>Costo Total Acordado: S/ %,.2f</strong></p>
                </div>

                <h3 style="margin-top: 24px; border-bottom: 2px solid #eee; padding-bottom: 5px;">Detalle de Productos</h3>
                <table class="product-table">
                    <thead>
                        <tr>
                            <th>Código</th>
                            <th>Producto</th>
                            <th>Cantidad</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>

                <p style="margin-top: 24px;">Agradeceremos confirmar la recepción de esta orden y la fecha de despacho.</p>
                <p>Saludos cordiales,<br><strong>Equipo de Compras<br>TecnIServicios</strong></p>
              </div>
              <div class="footer">Este es un correo automático de confirmación. Correo de respuesta: %s</div>
            </div>
          </div>
        </body>
        </html>
        """;
    return String.format(
        htmlTemplate,
        subject, 
        subject, 
        pedido.getProveedor().getRazonSocial(), 
        pedido.getId().toString(),
        fechaEmisionFmt,
        fechaEsperadaFmt, 
        pedido.getCostoCotizacion(), 
        filasProductos.toString(),
        mailFrom 
    );
  }
}
