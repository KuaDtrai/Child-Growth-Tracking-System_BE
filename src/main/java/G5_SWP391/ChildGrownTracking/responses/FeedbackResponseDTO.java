package G5_SWP391.ChildGrownTracking.responses;

import G5_SWP391.ChildGrownTracking.models.RatingPoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("rating")
    private RatingPoint rating;

    @JsonProperty("parentname")
    private String parentName;

    @JsonProperty("doctorname")
    private String doctorName;
}
