package G5_SWP391.ChildGrownTracking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDTO {
    @JsonProperty("certificate")
    private String certificate;

    @JsonProperty("specialization")
    private String specialization;
}
