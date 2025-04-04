package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "children")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private Date dob;

    private String gender;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private User parent;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    private boolean status;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Metric> metrics = new ArrayList<>();

    public Child(String name, LocalDateTime createdDate, LocalDateTime updateDate, Date dob, String gender, User parent, User doctor, boolean status) {
        this.name = name;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.dob = dob;
        this.gender = gender;
        this.parent = parent;
        this.doctor = doctor;
        this.status = status;
    }
}