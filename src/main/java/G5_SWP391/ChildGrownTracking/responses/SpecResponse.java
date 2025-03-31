package G5_SWP391.ChildGrownTracking.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecResponse {
    @JsonProperty("doctorId")
    private Long doctorId;

    @JsonProperty("specialization")
    private String specialization;

    @JsonProperty("certificate")
    private String certificate;

}
