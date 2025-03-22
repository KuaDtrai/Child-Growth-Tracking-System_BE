package G5_SWP391.ChildGrownTracking.dtos;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ChildRequestDTO {

    @NotNull(message = "name is required.")
    private String name;

    @NotNull(message = "dob is required.")
    private Date dob;

    @NotNull(message = "gender is required.")
    private String gender;

    @NotNull(message = "parenId ID is required.")
    private Long parentId;

}

