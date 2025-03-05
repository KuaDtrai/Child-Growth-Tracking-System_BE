package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "roleId")
    private Role roleId;

    @Column(name = "membership")
    private membership membership;

    @Column(name = "createDate")
    private LocalDateTime createdDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "status")
    private boolean status;

    public User(String userName, String password, String email, Role roleId, membership membershipId, LocalDateTime createdDate, LocalDateTime updateDate, boolean status) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.roleId = roleId;
        this.membershipId = membershipId;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.status = status;
    }

//    public User(String userName, String password, String email, Long roleId, String membership, LocalDateTime now, LocalDateTime now1, boolean b) {
//    }
}
