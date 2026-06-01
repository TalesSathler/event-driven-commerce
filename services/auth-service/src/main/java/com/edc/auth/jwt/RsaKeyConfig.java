package com.edc.auth.jwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RsaKeyConfig {

  private static final Logger log = LoggerFactory.getLogger(RsaKeyConfig.class);
  private static final String PRIVATE_KEY_FILE = "private.pem";
  private static final String PUBLIC_KEY_FILE = "public.pem";

  private final PrivateKey privateKey;
  private final PublicKey publicKey;

  public RsaKeyConfig(@Value("${app.jwt.keys-dir:./keys}") String keysDir) {
    var dir = Paths.get(keysDir);
    try {
      Files.createDirectories(dir);
      var privatePath = dir.resolve(PRIVATE_KEY_FILE);
      var publicPath = dir.resolve(PUBLIC_KEY_FILE);

      if (Files.exists(privatePath) && Files.exists(publicPath)) {
        log.info("Loading existing RSA key pair from {}", dir);
        privateKey = loadPrivateKey(privatePath);
        publicKey = loadPublicKey(publicPath);
      } else {
        log.info("Generating new RSA key pair in {}", dir);
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        var pair = generator.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        saveKey(privatePath, pair.getPrivate().getEncoded());
        saveKey(publicPath, pair.getPublic().getEncoded());
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize RSA keys", e);
    }
  }

  public PrivateKey getPrivateKey() { return privateKey; }
  public PublicKey getPublicKey() { return publicKey; }

  private PrivateKey loadPrivateKey(Path path) throws Exception {
    var bytes = Files.readAllBytes(path);
    var spec = new PKCS8EncodedKeySpec(bytes);
    return KeyFactory.getInstance("RSA").generatePrivate(spec);
  }

  private PublicKey loadPublicKey(Path path) throws Exception {
    var bytes = Files.readAllBytes(path);
    var spec = new X509EncodedKeySpec(bytes);
    return KeyFactory.getInstance("RSA").generatePublic(spec);
  }

  private void saveKey(Path path, byte[] encoded) throws IOException {
    Files.write(path, encoded);
  }
}
