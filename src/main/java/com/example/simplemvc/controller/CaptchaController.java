package com.example.simplemvc.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;

@RestController
public class CaptchaController {

  private static final String SESSION_KEY = "CAPTCHA_CODE";
  private static final char[] CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray(); // sin 0/O/1/I
  private static final SecureRandom RND = new SecureRandom();

  @GetMapping("/captcha")
  public void captcha(HttpSession session, HttpServletResponse resp) throws IOException {
    String code = randomCode(6);
    session.setAttribute(SESSION_KEY, code);

    int w = 160, h = 48;
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = img.createGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, w, h);

    for (int i = 0; i < 18; i++) {
      g.setColor(new Color(220 + RND.nextInt(35), 220 + RND.nextInt(35), 220 + RND.nextInt(35)));
      int x1 = RND.nextInt(w), y1 = RND.nextInt(h), x2 = RND.nextInt(w), y2 = RND.nextInt(h);
      g.drawLine(x1, y1, x2, y2);
    }

    g.setFont(new Font("SansSerif", Font.BOLD, 28));
    int x = 15;
    for (char c : code.toCharArray()) {
      AffineTransform old = g.getTransform();
      double angle = (RND.nextDouble() - 0.5) * 0.4; // ligera rotación
      g.setTransform(AffineTransform.getRotateInstance(angle, x, 30));
      g.setColor(new Color(30 + RND.nextInt(120), 30 + RND.nextInt(120), 30 + RND.nextInt(120)));
      g.drawString(String.valueOf(c), x, 34);
      g.setTransform(old);
      x += 22;
    }

    g.dispose();

    resp.setContentType("image/png");
    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    resp.setHeader("Pragma", "no-cache");
    ImageIO.write(img, "png", resp.getOutputStream());
  }

  private static String randomCode(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) sb.append(CHARS[RND.nextInt(CHARS.length)]);
    return sb.toString();
  }
}
 