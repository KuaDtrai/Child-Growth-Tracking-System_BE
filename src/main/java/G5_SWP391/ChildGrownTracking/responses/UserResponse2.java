package G5_SWP391.ChildGrownTracking.responses;

import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.models.role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse2 {
    private Long id;
    private String userName;
    private String email;
    private role role;
    private membership membership;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private boolean verified;
    private String scope;// ✅ Thêm field scope vào đây
    private boolean status;
}
