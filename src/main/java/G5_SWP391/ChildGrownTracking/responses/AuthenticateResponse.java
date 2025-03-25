package G5_SWP391.ChildGrownTracking.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticateResponse {
    @JsonProperty("token")
    String token;

    @JsonProperty("user")
    UserResponse2 userResponse;

    @JsonProperty("authenticated")
    boolean authenticated;
}
