package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;

    private String password;

    private String email;

    private String roleId;

    private membership membership;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private boolean status;

    public User(String userName, String password, String email, String roleId, membership membership, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean status) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
        this.membership = membership;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", roleId='" + roleId + '\'' +
                ", membershipId='" + membership + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", status='" + status + '\'' +
                '}';
    }
}
