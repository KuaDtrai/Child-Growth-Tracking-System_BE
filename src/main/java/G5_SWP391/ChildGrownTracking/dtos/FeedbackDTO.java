package G5_SWP391.ChildGrownTracking.dtos;

import G5_SWP391.ChildGrownTracking.models.RatingPoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTO {

    @JsonProperty("description")
    private String description;

    @JsonProperty("rating")
    private int rating;


}
