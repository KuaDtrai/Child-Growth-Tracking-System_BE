package G5_SWP391.ChildGrownTracking.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChildDoctorResponse {
    private Long childId;

    private String childName;

    private Long doctorId;

    private String doctorName;
}
