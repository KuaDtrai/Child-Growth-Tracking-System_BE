package G5_SWP391.ChildGrownTracking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ChildResponseDTO {
    private Long id;
    private String name;
    private Date dob;
    private String gender;
    private String parentName; // userName thay vì parenId
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean status;
}
