package G5_SWP391.ChildGrownTracking.dtos;

import G5_SWP391.ChildGrownTracking.models.RatingPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTO {

    private String discription;
    private RatingPoint rating;


}
