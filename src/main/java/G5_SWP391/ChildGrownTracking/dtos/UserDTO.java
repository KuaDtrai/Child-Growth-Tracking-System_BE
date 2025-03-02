package G5_SWP391.ChildGrownTracking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roleId")
    private String roleId;

    @JsonProperty("membership")
    private String membership;

//    @JsonProperty("createdDate")
//    private Date createdDate;
//
//    @JsonProperty("lastModifiedDate")
//    private Date lastModifiedDate;

    @JsonProperty("status")
    private boolean status;
}
