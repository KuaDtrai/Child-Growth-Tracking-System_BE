package G5_SWP391.ChildGrownTracking.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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

   private String name ;

   private Date dob ;

   private String gender ;

   private String parenId ;

   private LocalDateTime createDate ;

   private LocalDateTime updateDate ;

   private boolean status ;

    public Child( String name, Date dob, String gender, String parenId, LocalDateTime createDate, LocalDateTime updateDate, boolean status) {

        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.parenId = parenId;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }



    @Override
    public String toString() {
        return "Child{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", parenId='" + parenId + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", status=" + status +
                '}';
    }
}
