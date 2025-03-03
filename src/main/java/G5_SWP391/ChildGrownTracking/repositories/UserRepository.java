package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    List<User> findAllByStatusIsTrue();

    User findByUserNameAndPassword(String userName, String password);

    User findUserByEmailAndPassword(String email, String password);
    boolean existsById(Long parenId);
    boolean existsByIdAndStatusIsTrue(Long parenId);
}
