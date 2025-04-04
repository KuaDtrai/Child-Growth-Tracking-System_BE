package G5_SWP391.ChildGrownTracking.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
