package G5_SWP391.ChildGrownTracking.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MetricResponseDTO {

    private Long id;
    private BigDecimal weight;
    private BigDecimal height;
    private BigDecimal BMI;
    private LocalDateTime recordedDate ;
    private LocalDateTime createDate ;
    private boolean status;
}
