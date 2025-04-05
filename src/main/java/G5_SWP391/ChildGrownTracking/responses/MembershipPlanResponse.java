package G5_SWP391.ChildGrownTracking.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MembershipPlanResponse {

    private Long id;

    private String name;

    private String description;

    private String features;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private double annualPrice;

    private int maxChildren;

    private boolean status;

    private int duration;
}
