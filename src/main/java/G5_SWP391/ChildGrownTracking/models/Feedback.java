package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public Feedback(User user, User doctor, Rating rating, String description, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.user = user;
        this.doctor = doctor;
        this.rating = rating;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}