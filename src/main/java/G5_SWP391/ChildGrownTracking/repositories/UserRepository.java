package G5_SWP391.ChildGrownTracking.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmailAndStatusIsTrue(String email);

//    List<User> findAllByStatusIsTrueAndRoleIsMEMBER();

    User findByUserNameAndPassword(String userName, String password);

    User findUserByEmailAndPassword(String email, String password);

    boolean existsByChildrenAndStatusIsTrue(Child child);

//    List<User> findAllByStatusIsTrue();
    ;

    List<User> findAllByStatusIsTrueAndRole(Role role);

    Optional<User> findByIdAndStatusIsTrue(Long doctorId);

    Optional<User> findByChildrenAndStatusIsTrue(Child child);

    Optional<User> findByEmail(String email);


    @Query("SELECT COUNT(u) FROM User u " +
            "WHERE u.status = :userStatus " +
            "AND u.membership.status = :membershipStatus " +
            "AND u.membership.plan.name = :planName")
    Long countByMembershipPlanAndStatus(
            @Param("planName") String planName,
            @Param("userStatus") boolean userStatus,
            @Param("membershipStatus") boolean membershipStatus
    );



    Long countByRoleAndStatusIsTrue(Role role);

}