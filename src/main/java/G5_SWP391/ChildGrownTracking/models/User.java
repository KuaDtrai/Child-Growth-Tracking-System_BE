package G5_SWP391.ChildGrownTracking.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "userName")
    private String userName;

    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Child> children = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Child> children2 = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("user-feedbacks")
    private List<Feedback> userFeedbacks = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("doctor-feedbacks")
    private List<Feedback> doctorFeedbacks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private membership membership;

    @Enumerated(EnumType.STRING)
    private role role;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private boolean status;

    public User(String userName, String password, String email, role role, membership membership, LocalDateTime createdDate, LocalDateTime updateDate, boolean status) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.membership = membership;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
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