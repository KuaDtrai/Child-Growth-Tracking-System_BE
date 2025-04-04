package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    List<MembershipPlan> getMembershipPlanByName(String name);

    List<MembershipPlan> findAllByStatusIsTrue();

    MembershipPlan findByName(String name);
}
