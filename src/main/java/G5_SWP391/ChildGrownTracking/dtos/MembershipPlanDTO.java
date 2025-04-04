package G5_SWP391.ChildGrownTracking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MembershipPlanDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("features")
    private String features;

    @JsonProperty("anualPrice")
    private double anualPrice;

    @JsonProperty("maxChildren")
    private int maxChildren;

    @JsonProperty("duration")
    private int duration;
}
