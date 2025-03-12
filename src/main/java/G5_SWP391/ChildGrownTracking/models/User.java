package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column( name = "userName")
    private String userName;

    private String password;

    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Child> children = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Child> children2 = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private membership membership;

    @Enumerated(EnumType.STRING)
    private role role;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private boolean status;

    public User(String userName, String password, String email ,role role, membership membership, LocalDateTime createdDate, LocalDateTime updateDate, boolean status) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role ;
        this.membership = membership;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public void addChildren(Child child) {
        children.add(child);
        child.setParenId(this);
    }

    public void removeChildren(Child child) {
        children.remove(child);
        child.setParenId(null);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", children=" + children +
                ", membership=" + membership +
                ", createdDate=" + createdDate +
                ", updateDate=" + updateDate +
                ", status=" + status +
                '}';
    }


}
