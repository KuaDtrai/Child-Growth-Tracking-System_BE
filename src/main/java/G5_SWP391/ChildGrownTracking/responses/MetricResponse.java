package G5_SWP391.ChildGrownTracking.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MetricResponse {
    private Long id;

    private BigDecimal weight;

    private BigDecimal height;

    private BigDecimal BMI;

    private LocalDateTime recoredDate;

    private LocalDateTime createdDate;

    private boolean status;
}
