package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membership_plans")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private String features;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private double annualPrice;

    private int maxChildren;

    private boolean status;

    private int duration; // Trong tháng hoặc năm

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Membership> memberships = new ArrayList<>();

    public MembershipPlan(String name, String description, String features, LocalDateTime createdDate, LocalDateTime updateDate, double annualPrice, int maxChildren, boolean status, int duration) {
        this.name = name;
        this.description = description;
        this.features = features;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.annualPrice = annualPrice;
        this.maxChildren = maxChildren;
        this.status = status;
        this.duration = duration;
    }
}