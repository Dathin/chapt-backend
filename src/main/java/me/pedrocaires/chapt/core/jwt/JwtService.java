package me.pedrocaires.chapt.core.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import me.pedrocaires.chapt.core.enumerator.Prefix;
import me.pedrocaires.chapt.core.exception.InvalidJwtException;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.USER_ID;

public class JwtService {

    private static final Key key;

    static {
        key = new SecretKeySpec(("sniandaidoçiasnidasoçdasiodnsaidiasnddaksndijsandoiasndoias").getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    private JwtService() {

    }

    public static String issueToken(int userId) {
        long currentTimeInMs = System.currentTimeMillis();
        long tenMinutesInMs = 600000;
        return Jwts.builder().setIssuedAt(new Date(currentTimeInMs)).setExpiration(new Date(currentTimeInMs + tenMinutesInMs)).claim(USER_ID, userId).signWith(key).compact();
    }

    public static int validateToken(String token) {
        try {
            if (token != null) {
                return (int) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token.replace(Prefix.BEARER.getValue(), "")).getBody().get(USER_ID);
            } else {
                throw new InvalidJwtException();
            }
        } catch (JwtException ex) {
            throw new InvalidJwtException();
        }
    }

    public static String refreshToken(String token) {
        int userId = validateToken(token);
        return issueToken(userId);
    }
}
