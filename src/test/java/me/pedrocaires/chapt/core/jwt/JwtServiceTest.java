package me.pedrocaires.chapt.core.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.pedrocaires.chapt.core.exception.InvalidJwtException;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private static final Key key;

    static {
        key = new SecretKeySpec(("sniandaidoçiasnidasoçdasiodnsaidiasnddaksndijsandoiasndoias").getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Test
    void shouldIssueToken() {
        String token = JwtService.issueToken(1);

        assertNotNull(token);
    }

    @Test
    void shouldConsumeValidToken() {
        String token = JwtService.issueToken(1);

        JwtService.validateToken(token);
    }

    @Test
    void shouldThrowWhenInvalidToken() {
        assertThrows(InvalidJwtException.class, () -> JwtService.validateToken("invalid token"));
    }

    @Test
    void shouldThrowNullToken() {
        assertThrows(InvalidJwtException.class, () -> JwtService.validateToken(null));
    }

    @Test
    void shouldExpireTokenIn10Minutes() {
        String token = JwtService.issueToken(1);
        long tenMinutesInMs = 600000;

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Date exp = claims.getExpiration();
        Date iat = claims.getIssuedAt();

        assertEquals(tenMinutesInMs, exp.getTime() - iat.getTime());
    }

    @Test
    void shouldRefreshToken() {
        String token = JwtService.issueToken(1);

        String refreshedToken = JwtService.refreshToken(token);

        JwtService.validateToken(refreshedToken);
    }
}
