package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByNameContainingIgnoreCase(String name);
    List<Child> findByParenId(String parenId);




}
