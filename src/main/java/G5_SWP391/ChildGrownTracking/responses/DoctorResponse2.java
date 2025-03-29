package G5_SWP391.ChildGrownTracking.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DoctorResponse2(
        @JsonProperty("doctorId") Long doctorId,
        @JsonProperty("user") UserResponse user,
        @JsonProperty("specialization") String specialization,
        @JsonProperty("certificate") String certificate,
        @JsonProperty("childCount") Long childCount
) {}
