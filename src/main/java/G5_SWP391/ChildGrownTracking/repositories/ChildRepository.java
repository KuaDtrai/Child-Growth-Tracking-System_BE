package G5_SWP391.ChildGrownTracking.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;

public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByParentAndStatusIsTrue(User parent);
    List<Child> findByStatusIsTrue();
    Optional<Child> findByIdAndStatusIsTrue(Long id);
    boolean existsByIdAndStatusIsTrue(Long childId);
    List<Child> findByDoctorAndStatusIsTrue (User doctor);
    Long countByDoctorAndStatusIsTrue(User doctor);


    Long countByStatusIsTrue();
}