package G5_SWP391.ChildGrownTracking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByChild(Child child);
}
