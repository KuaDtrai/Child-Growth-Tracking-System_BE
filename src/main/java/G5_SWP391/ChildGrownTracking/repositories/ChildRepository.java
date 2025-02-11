package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByNameContainingIgnoreCase(String name);





}
