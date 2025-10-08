package com.haraif.real_time_chat_application.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
  private static final String PRIVATE_KEY_PATH = "src/main/resources/certs/private.pem";
  private static final long EXPIRATION_MS = 86400000; // 1 day

  private PrivateKey privateKey;

  public JwtService() {
    try {
      this.privateKey = loadPrivateKey();
    } catch (Exception e) {
      throw new RuntimeException("failed to load private key", e);
    }
  }

  private PrivateKey loadPrivateKey() throws Exception {
    String key = Files.readString(Paths.get(PRIVATE_KEY_PATH))
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s+", "");

    byte[] keyBytes = Base64.getDecoder().decode(key);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    return KeyFactory.getInstance("RSA").generatePrivate(spec);
  }

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
        .signWith(privateKey, SignatureAlgorithm.RS256).compact();
  }
}
