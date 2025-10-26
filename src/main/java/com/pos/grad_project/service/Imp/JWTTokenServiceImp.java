package com.pos.grad_project.service.Imp;

import com.pos.grad_project.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JWTTokenServiceImp implements JWTTokenService {
    private final JwtEncoder encoder;
    private final Duration expiry;
    private final String issuer;
    public JWTTokenServiceImp(JwtEncoder encoder,
                              @Value("${app.jwt.exp-minutes}") long expMinutes,
                              @Value("${app.jwt.issuer}") String issuer) {
        this.encoder = encoder;
        this.expiry = Duration.ofMinutes(expMinutes);
        this.issuer = issuer;
    }
    @Override
    public String generateJWTToken(Authentication auth) {
        Instant now= Instant.now();
        Instant expires = now.plus(expiry);
        String scope=auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet cliams=JwtClaimsSet.builder()
                .claim("scope" , scope)
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expires)
                .subject(auth.getName())
                .build();

        JwsHeader header= JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        return encoder.encode(JwtEncoderParameters.from(header,cliams)).getTokenValue();
    }
}
