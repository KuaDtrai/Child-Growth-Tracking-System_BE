package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.Feedback;

import G5_SWP391.ChildGrownTracking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

//     List<Feedback> findByDoctorAndUser(User doctor, User user);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.user.id = :userId AND f.doctor.id = :doctorId")
    long countByUserAndDoctor(@Param("userId") Long userId, @Param("doctorId") Long doctorId);

    List<Feedback> findByUserId(Long userId);

    List<Feedback> findByDoctorId(Long doctorId);}


