package G5_SWP391.ChildGrownTracking.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Child_parentName_DoctorName_Response {
    private Long id;
    private String name;
    private Date dob;
    private String gender;
    private String parentName;
    private String doctorName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean status;
}
