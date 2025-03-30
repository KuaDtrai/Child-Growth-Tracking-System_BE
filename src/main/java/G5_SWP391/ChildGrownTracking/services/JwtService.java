package G5_SWP391.ChildGrownTracking.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.signerkey}")
    private String SIGNER_KEY;

    public String generateToken(String username, String role) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("ChildTracking.com")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                    .claim("role", role)
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public boolean validateToken(SignedJWT signedJWT) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return signedJWT.verify(verifier) && expirationTime.after(new Date());
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }
}
