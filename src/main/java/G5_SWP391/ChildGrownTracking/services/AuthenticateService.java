package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.AuthenticateDTO;
import G5_SWP391.ChildGrownTracking.dtos.IntrospcectDTO;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.AuthenticateResponse;
import G5_SWP391.ChildGrownTracking.responses.IntrospectResponse;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import G5_SWP391.ChildGrownTracking.responses.UserResponse2;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {
    private final UserService userService;
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;

    public AuthenticateResponse authenticate(AuthenticateDTO request) {
        User user = userService.findUserByEmailAndPassword(request.getEmail(), request.getPassword());

        if (user == null) {
            return AuthenticateResponse.builder().authenticated(false).build();
        }

        // ✅ Xác định scope theo role
        String scope = (user.getRole().equals(role.DOCTOR)) ? "doctor member" : "member";


        UserResponse2 userResponse = new UserResponse2(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getRole(), user.getMembership(),
                user.getCreatedDate(), user.getUpdateDate(), false, scope, user.isStatus()
        );

        String token = generateToken(user);
        return AuthenticateResponse.builder()
                .token(token)
                .userResponse(userResponse)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // ✅ Xác định scope
        String scope = (user.getRole().equals(role.DOCTOR)) ? "doctor member" : "member";

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("ChildTracking.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", scope)  // ✅ Lưu scope vào token
                .build();

        JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }


    public IntrospectResponse introspect(IntrospcectDTO token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token.getToken());

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier) && expirationDate.after(new Date());

        // ✅ Lấy scope từ token (nếu có lưu trong claims)
        String scope = signedJWT.getJWTClaimsSet().getStringClaim("scope");

        return IntrospectResponse.builder()
                .verified(verified)
                .scope(scope)
                .build();
    }


}
