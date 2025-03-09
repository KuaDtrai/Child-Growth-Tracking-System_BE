package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Child")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private Long  id ;


    @NotBlank(message = "Name cannot be empty")
   private String name ;

   private Date dob ;

   private String gender ;

    @ManyToOne
    @JoinColumn(name = "parentId")  // Sửa tên cột cho đúng chuẩn SQL
    private User parent;

    @ManyToOne
    @JoinColumn(name = "doctorId")  // Sửa tên cột cho đúng chuẩn SQL
    private User doctor;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Metric> metrics = new ArrayList<>();

   public User getParenId() {
       return parent;
   }

    public void setParenId(User parent) {
         this.parent = parent;
    }

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> Post = new ArrayList<>();



   private LocalDateTime createDate ;

   private LocalDateTime updateDate ;

   private boolean status ;

    public Child(String name, Date dob, String gender, User parent, LocalDateTime createDate, LocalDateTime updateDate, boolean status) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.parent = parent; // Gán đúng giá trị
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }


    public void addMetric(Metric metric) {
       metrics.add(metric);
       metric.setChild(this);
   }

   public void removeMetric(Metric metric) {
         metrics.remove(metric);
         metric.setChild(null);
   }

    public void addPost(Post post) {
         Post.add(post);
         post.setChild(this);
    }

    public void removePost(Post post) {
         Post.remove(post);
         post.setChild(null);
    }
}
