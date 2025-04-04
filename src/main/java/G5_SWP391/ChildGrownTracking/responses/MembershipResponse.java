package G5_SWP391.ChildGrownTracking.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MembershipResponse {
    private long id;

    private UserResponse user;

    private MembershipPlanResponse plan;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean status;
}
