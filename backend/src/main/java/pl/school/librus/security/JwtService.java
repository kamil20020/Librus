package pl.school.librus.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.school.librus.exception.InvalidBearerTokenException;
import pl.school.librus.role.RoleEntity;
import pl.school.librus.user.UserEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.access-token.expiration}")
    private Integer accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private Integer refreshTokenExpiration;

    @Value("${jwt.secret}")
    private String secret;

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final String USERNAME_KEY = "username";

    public String getAccessToken(UserEntity user){

        UUID userId = user.getId();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles());

        return generateToken(userId.toString(), user.getUsername(), accessTokenExpiration, claims)
            .compact();
    }

    public String generateRefreshToken(String userId, String username){

        return generateToken(userId, username, refreshTokenExpiration, new HashMap<>())
            .compact();
    }

    private JwtBuilder generateToken(String userId, String username, Integer tokenExpiration, Map<String, Object> claims){

        UUID tokenId = UUID.randomUUID();

        Instant rawDate = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Date createdAt = Date.from(rawDate);

        Instant rawExpirationDate = rawDate.plusMillis(tokenExpiration);
        Date expirationDate = Date.from(rawExpirationDate);

        claims.put(USERNAME_KEY, username);

        return Jwts.builder()
            .setId(tokenId.toString())
            .setSubject(userId)
            .setIssuer(issuer)
            .setAudience(audience)
            .setIssuedAt(createdAt)
            .setExpiration(expirationDate)
            .claims(claims)
            .signWith(signatureAlgorithm, secret);
    }

    public String extractUsername(String token, String tokenType) throws InvalidBearerTokenException{

        Claims claims = verifyToken(token, tokenType)
            .parseClaimsJws(token)
            .getBody();

        return (String) claims.get(USERNAME_KEY);
    }

    private JwtParser verifyToken(String token, String tokenType){

        if(token == null || token.isBlank()){

            throw new InvalidBearerTokenException(tokenType + " was not given");
        }

        Instant rawDate = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        Date createdAt = Date.from(rawDate);

        return Jwts.parser()
            .setSigningKey(secret)
            .requireIssuer(issuer)
            .requireAudience(issuer)
            .build();
    }

}
