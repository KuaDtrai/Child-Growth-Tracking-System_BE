package G5_SWP391.ChildGrownTracking.responses;

import G5_SWP391.ChildGrownTracking.models.RatingPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
    private Long id;
    private String description;
    private RatingPoint rating;
    private String parentName;
    private String doctorName;
}
