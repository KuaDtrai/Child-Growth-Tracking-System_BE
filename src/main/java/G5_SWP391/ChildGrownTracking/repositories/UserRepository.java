package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmailAndStatusIsTrue(String email);

//    List<User> findAllByStatusIsTrueAndRoleIsMEMBER();

    User findByUserNameAndPassword(String userName, String password);

    User findUserByEmailAndPassword(String email, String password);
    boolean existsById(Long parenId);
    boolean existsByIdAndStatusIsTrue(Long parenId);
    boolean existsByChildrenAndStatusIsTrue(Child child);

//    List<User> findAllByStatusIsTrue();

    List<User> findAllByStatusIsTrueAndRole(role role);

    Optional<User> findByIdAndStatusIsTrue(Long doctorId);
}
