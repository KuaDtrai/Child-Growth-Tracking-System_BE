package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Child, Long> {
}
