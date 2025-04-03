package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "memberships")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private MembershipPlan plan;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean status;

    public Membership(User user, MembershipPlan plan, LocalDateTime startDate, LocalDateTime endDate, boolean status) {
        this.user = user;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
}