package com.example.simplemvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
}
