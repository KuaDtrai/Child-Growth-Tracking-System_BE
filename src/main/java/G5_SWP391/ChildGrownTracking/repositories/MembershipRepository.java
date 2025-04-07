package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import G5_SWP391.ChildGrownTracking.models.Membership;
import G5_SWP391.ChildGrownTracking.models.User;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUser(User user);
    List<Membership> getMembershipsByPlanAndStatusIs(MembershipPlan membershipPlan, boolean status);
}
