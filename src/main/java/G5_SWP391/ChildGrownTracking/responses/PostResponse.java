package G5_SWP391.ChildGrownTracking.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;

    private boolean status;
}
