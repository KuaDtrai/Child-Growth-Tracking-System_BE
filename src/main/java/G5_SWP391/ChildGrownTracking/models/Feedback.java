package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Feedback")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference("user-feedbacks")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctorId")
    @JsonBackReference("doctor-feedbacks")
    private User doctor;

    @Enumerated(EnumType.STRING)
    private RatingPoint rating;

    private String description;



    public Feedback(User user, User doctor, RatingPoint rating, String description) {
        this.user = user;
        this.doctor = doctor;
        this.rating = rating;
        this.description = description;
    }
}
