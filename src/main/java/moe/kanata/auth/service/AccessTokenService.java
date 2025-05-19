package moe.kanata.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import moe.kanata.auth.api.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class AccessTokenService {

    private final SecretKey key;

    public AccessTokenService(
        @Value("${kanata.jwt.secret}")
        final String jwtSecret
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateAccessToken(final Authentication authentication) {
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        final ZonedDateTime expired = now.plusHours(1L);

        final String subject = authentication.getName();
        final String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(", "));

        return Jwts.builder()
            .subject(subject)
            .claim("roles", authorities)
            .expiration(Date.from(expired.toInstant()))
            .issuedAt(Date.from(now.toInstant()))
            .issuer("kanata-application-server")
            .compact();
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = parseClaims(token);
        final List<? extends GrantedAuthority> authorities = getAuthorities(claims);
        final User principal = new User(claims.getSubject(), "", authorities);
        return UsernamePasswordAuthenticationToken.authenticated(
            principal,
            null,
            authorities
        );
    }

    private List<? extends GrantedAuthority> getAuthorities(final Claims claims) {
        final String roles = claims.get("roles", String.class);
        return Stream.of(roles.split(", "))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (final ExpiredJwtException exception) {
            throw new TokenException("Expired JWT token");
        } catch (final Exception exception) {
            throw new TokenException("Invalid JWT token");
        }
    }
}