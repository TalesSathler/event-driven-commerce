package com.edc.auth.jwt;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Public Key", description = "Endpoints for retrieving JWT signing keys")
public class PublicKeyController {

  private static final String KEY_ID = "edc-rs256-key";

  private final RsaKeyConfig rsaKeyConfig;

  public PublicKeyController(RsaKeyConfig rsaKeyConfig) {
    this.rsaKeyConfig = rsaKeyConfig;
  }

  @Operation(summary = "Get public key", description = "Returns the RSA public key in PEM format for verifying JWT signatures")
  @GetMapping(value = "/public-key", produces = MediaType.TEXT_PLAIN_VALUE)
  public String getPublicKey() {
    var key = rsaKeyConfig.getPublicKey();
    var encoded = Base64.getEncoder().encodeToString(key.getEncoded());
    return "-----BEGIN PUBLIC KEY-----\n" + encoded + "\n-----END PUBLIC KEY-----";
  }

  @Operation(summary = "Get JWKS", description = "Returns the JSON Web Key Set for JWT signature verification")
  @GetMapping(value = "/jwks", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Object> getJwks() throws Exception {
    var pubKey = (RSAPublicKey) rsaKeyConfig.getPublicKey();
    var spec = KeyFactory.getInstance("RSA").getKeySpec(pubKey, RSAPublicKeySpec.class);

    var jwk = new LinkedHashMap<String, Object>();
    jwk.put("kty", "RSA");
    jwk.put("alg", "RS256");
    jwk.put("use", "sig");
    jwk.put("kid", KEY_ID);
    jwk.put("n", Base64.getUrlEncoder().withoutPadding().encodeToString(spec.getModulus().toByteArray()));
    jwk.put("e", Base64.getUrlEncoder().withoutPadding().encodeToString(spec.getPublicExponent().toByteArray()));

    return Map.of("keys", List.of(jwk));
  }
}
