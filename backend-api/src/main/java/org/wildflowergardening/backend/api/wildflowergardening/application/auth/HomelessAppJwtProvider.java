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

  private static final String HOMELESS_ID_CLAIM_NAME = "homelessId";
  private static final String HOMELESS_NAME_CLAIM_NAME = "homelessName";
  private static final String SHELTER_ID_CLAIM_NAME = "shelterId";

  private final SecretKey key;

  public HomelessAppJwtProvider(
      @Value("${token.secret}") String tokenSecret
  ) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
  }

  public String createToken(HomelessUserContext homelessUserContext) {
    Map<String, String> claims = new HashMap<>();

    claims.put(HOMELESS_ID_CLAIM_NAME, String.valueOf(homelessUserContext.getHomelessId()));
    claims.put(HOMELESS_NAME_CLAIM_NAME, homelessUserContext.getHomelessName());
    claims.put(SHELTER_ID_CLAIM_NAME, String.valueOf(homelessUserContext.getShelterId()));

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

    Long homelessId = Long.parseLong((String) claims.get(HOMELESS_ID_CLAIM_NAME));
    String homelessName = (String) claims.get(HOMELESS_NAME_CLAIM_NAME);
    Long shelterId = Long.parseLong((String) claims.get(SHELTER_ID_CLAIM_NAME));

    return HomelessUserContext.builder()
        .homelessId(homelessId)
        .homelessName(homelessName)
        .shelterId(shelterId)
        .build();
  }
}
