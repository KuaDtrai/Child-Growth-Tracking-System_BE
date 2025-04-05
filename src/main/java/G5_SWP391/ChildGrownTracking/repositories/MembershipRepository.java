package G5_SWP391.ChildGrownTracking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import G5_SWP391.ChildGrownTracking.models.Membership;
import G5_SWP391.ChildGrownTracking.models.User;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUser(User user);
}
