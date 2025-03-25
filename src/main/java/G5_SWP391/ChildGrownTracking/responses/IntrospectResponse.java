package G5_SWP391.ChildGrownTracking.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectResponse {
    private boolean verified;
    private String scope;
}
