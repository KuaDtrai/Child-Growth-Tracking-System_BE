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

    private String username;

    private String planname;

    private int maxChildren;

    private int duration;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean status;
}
