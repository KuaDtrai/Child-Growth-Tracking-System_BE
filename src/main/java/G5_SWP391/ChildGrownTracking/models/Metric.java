package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Metric")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metric {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private long id ;


    @ManyToOne
    @JoinColumn(name = "childId") // Để dễ dàng truy vấn
    private Child child;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(precision = 5, scale = 2)
    private BigDecimal height;

    @Column(precision = 5, scale = 2)
    private BigDecimal BMI;

    private LocalDateTime recordedDate ;

    private LocalDateTime createDate ;

    private boolean status;

    public Metric(Child child, BigDecimal weight, BigDecimal height, BigDecimal BMI, LocalDateTime recordedDate, LocalDateTime createDate, boolean status) {
        this.child = child;
        this.weight = weight;
        this.height = height;
        this.BMI = BMI;
        this.recordedDate = recordedDate;
        this.createDate = createDate;
        this.status = status;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "id=" + id +
                ", weight=" + weight +
                ", height=" + height +
                ", BMI=" + BMI +
                ", recordedDate=" + recordedDate +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }



}
