package G5_SWP391.ChildGrownTracking.services;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import G5_SWP391.ChildGrownTracking.models.Membership;
import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import G5_SWP391.ChildGrownTracking.repositories.MembershipPlanRepository;
import G5_SWP391.ChildGrownTracking.repositories.MembershipRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import G5_SWP391.ChildGrownTracking.dtos.AuthenticateDTO;
import G5_SWP391.ChildGrownTracking.dtos.IntrospcectDTO;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.AuthenticateResponse;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    @NonFinal
    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;

    public AuthenticateResponse authenticate(AuthenticateDTO request) {
        User user = userService.findUserByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user == null) {
            return AuthenticateResponse.builder().authenticated(false).build();
        }
    
        Membership membership = membershipRepository.findByUser(user);
    
        String planName = null;
    if (membership != null && membership.isStatus()) {
        MembershipPlan plan = membership.getPlan();
        if (plan != null) {
            planName = plan.getName();
            LocalDateTime now = LocalDateTime.now();
            if (membership.getEndDate() != null && membership.getEndDate().isBefore(now)) {
                MembershipPlan basicPlan = membershipPlanRepository.findByName("BASIC");
                membership.setPlan(basicPlan);
                membershipRepository.save(membership);
                planName = basicPlan.getName();
            }
        }
    }
    
        UserResponse userResponse = new UserResponse(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            null,
            user.getRole(),
            planName,
            user.getCreatedDate(),
            user.getUpdateDate(),
            user.isStatus()
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

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("ChildTracking.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("Custom claim", "Custom")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public boolean introspect(IntrospcectDTO token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token.getToken());

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return verified && expirationDate.after(new Date());
    }

}
