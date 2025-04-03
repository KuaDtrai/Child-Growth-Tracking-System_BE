package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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

    private String metric; // Các chỉ số sức khỏe hoặc phát triển

    private boolean status; // Thêm trường status

    public Child(String name, LocalDateTime createdDate, LocalDateTime updateDate, Date dob, String gender, User parent, User doctor, String metric, boolean status) {
        this.name = name;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.dob = dob;
        this.gender = gender;
        this.parent = parent;
        this.doctor = doctor;
        this.metric = metric;
        this.status = status;
    }

    public Child(@NotNull(message = "name is required.") String name, LocalDateTime now, LocalDateTime now1, @NotNull(message = "dob is required.") Date dob, @NotNull(message = "gender is required.") String gender, User parent, Object o, boolean b) {
    }
}