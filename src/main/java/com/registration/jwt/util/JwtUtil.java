package com.registration.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {

    private String SECRET_KEY = "secret";
    private Logger LOG = LoggerFactory.getLogger(JwtUtil.class);
    @Autowired
    @Qualifier("signingKeyStore")
    KeyStore keyStore;


    @Autowired
    private Environment env;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        PrivateKey key = null;
        try {
            key = (PrivateKey) keyStore.getKey(env.getProperty("regservice.jwt.signing.alias"), env.getProperty("regservice.jwt.privatekeyPassword").toCharArray());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error While Fetching Private key Entry from keystore : " + e.getMessage());
        } catch (UnrecoverableEntryException e) {
            LOG.error("Error While Fetching Private key Entry from keystore : " + e.getMessage());
        } catch (KeyStoreException e) {
            LOG.error("Error While Fetching Private key Entry from keystore : " + e.getMessage());
        }
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        KeyStore.ProtectionParameter entryPassword =
                new KeyStore.PasswordProtection(env.getProperty("regservice.jwt.privatekeyPassword").toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = null;
        try {
            privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(env.getProperty("regservice.jwt.signing.alias"), entryPassword);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error While Fetching Private key Entry from keystore : " + e.getMessage());
        } catch (UnrecoverableEntryException e) {
            LOG.error("Error While Fetching Private key Entry from keystore : " + e.getMessage());
        } catch (KeyStoreException e) {
            LOG.error("Error While Fetching Private key Entry from keystore : " + e.getMessage());
        }


        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 100))
                .signWith(privateKeyEntry.getPrivateKey(), SignatureAlgorithm.RS256).compact();
    }

    public Boolean validateToken(String token, String unm) {
        try {
            PublicKey publicKey = keyStore.getCertificate(env.getProperty("regservice.jwt.signing.alias")).getPublicKey();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token).getBody();
            return extractUsername(token).equals(claims.getSubject()) && !isTokenExpired(token);
        } catch (KeyStoreException e) {
            LOG.error("Error While Fetching Public key Entry from keystore : " + e.getMessage());
        }

        return false;
    }
}

