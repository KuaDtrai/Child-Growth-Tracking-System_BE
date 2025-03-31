package G5_SWP391.ChildGrownTracking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.User;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByUserId(Long id);
    Doctor findByUser(User user);
}