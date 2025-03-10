package G5_SWP391.ChildGrownTracking.responses;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.membership;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("membership")
    private membership membership;

    @JsonProperty("createdDate")
    private LocalDateTime createdDate;

    @JsonProperty("updateDate")
    private LocalDateTime updateDate;

    @JsonProperty("status")
    private boolean status;
}
