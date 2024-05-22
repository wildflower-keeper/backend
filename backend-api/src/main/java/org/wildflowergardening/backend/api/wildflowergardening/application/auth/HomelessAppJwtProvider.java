package org.wildflowergardening.backend.api.wildflowergardening.application.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.core.kernel.config.YamlPropertySourceFactory;

@Component
@PropertySource(
    value = "classpath:application-${spring.profiles.active:dev}.yaml",
    factory = YamlPropertySourceFactory.class
)
public class HomelessAppJwtProvider {

  private final SecretKey key;

  public HomelessAppJwtProvider(
      @Value("${token.secret}") String tokenSecret
  ) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
  }

  public String createToken(HomelessUserContext homelessUserContext) {
    Map<String, String> claims = new HashMap<>();

    claims.put("homelessId", String.valueOf(homelessUserContext.getHomelessId()));
    claims.put("homelessName", homelessUserContext.getHomelessName());
    claims.put("shelterId", String.valueOf(homelessUserContext.getShelterId()));

    return Jwts.builder()
        .subject(String.valueOf(homelessUserContext.getUserId()))
        .claims(claims)
        .issuedAt(new Date())         // 현재시각
        .expiration(null)       // 유효기간 없음
        .signWith(key)
        .compact();
  }

  public HomelessUserContext getUserContextFrom(String token) {
    JwtParser jwtParser = Jwts.parser()
        .verifyWith(key)
        .build();
    Claims claims = jwtParser.parseSignedClaims(token)
        .getPayload();

    Long homelessId = Long.parseLong((String) claims.get("homelessId"));
    Long shelterId = Long.parseLong((String) claims.get("shelterId"));
    String homelessName = (String) claims.get("homelessName");

    return HomelessUserContext.builder()
        .homelessId(homelessId)
        .homelessName(homelessName)
        .shelterId(shelterId)
        .build();
  }
}
