package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "Doctor")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Nationalized
    private String specialization;

    private String Certificate;



    public Doctor(User user, String specialization, String rowData) {
        this.user = user;
        this.specialization = specialization;
        this.Certificate = rowData;
    }
}
