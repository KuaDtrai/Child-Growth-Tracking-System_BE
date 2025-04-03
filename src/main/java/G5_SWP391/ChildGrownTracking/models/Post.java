package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    private String title;

    private String description;

    private LocalDateTime createdDate;

    private boolean status;

    public Post(User user, Child child, String title, String description, LocalDateTime createdDate, boolean status) {
        this.user = user;
        this.child = child;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.status = status;
    }
}