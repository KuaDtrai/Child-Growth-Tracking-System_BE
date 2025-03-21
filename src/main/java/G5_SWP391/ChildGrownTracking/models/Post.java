package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "childId")
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
