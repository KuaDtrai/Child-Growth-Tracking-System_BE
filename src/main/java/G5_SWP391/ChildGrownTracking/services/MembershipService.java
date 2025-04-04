package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.models.Membership;
import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import G5_SWP391.ChildGrownTracking.models.Role;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.repositories.MembershipPlanRepository;
import G5_SWP391.ChildGrownTracking.repositories.MembershipRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.MembershipPlanResponse;
import G5_SWP391.ChildGrownTracking.responses.MembershipResponse;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MembershipService {
    @Autowired
    private MembershipRepository membershipRepository;
    private UserRepository userRepository;
    private MembershipPlanRepository membershipPlanRepository;

    public MembershipResponse updateMembership(Long userId, Long membershipPlanId) {
        User user = userRepository.findById(userId).orElse(null);
        Membership membership = membershipRepository.findByUser(user);
        MembershipPlan membershipPlan = membershipPlanRepository.findById(membershipPlanId).orElse(null);
        membership.setPlan(membershipPlanRepository.findById(membershipPlanId).orElse(null));
        membership.setStartDate(LocalDateTime.now());
        membership.setEndDate(LocalDateTime.now().plusDays(membershipPlan.getDuration()));
        if (user.getRole().equals(Role.DOCTOR)) {
            membership.setStatus(false);
        }
        membership = membershipRepository.save(membership);

        UserResponse userResponse = new UserResponse(
                user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole(),
                membershipRepository.findByUser(user).getPlan().getName(),
                user.getCreatedDate(), user.getUpdateDate(), user.isStatus());

        MembershipPlanResponse membershipPlanResponse = new MembershipPlanResponse(
                membershipPlan.getId(),
                membershipPlan.getName(),
                membershipPlan.getDescription(),
                membershipPlan.getFeatures(),
                membershipPlan.getCreatedDate(),
                membershipPlan.getUpdateDate(),
                membershipPlan.getAnnualPrice(),
                membershipPlan.getMaxChildren(),
                membershipPlan.isStatus(),
                membershipPlan.getDuration()
        );

        MembershipResponse membershipResponse = new MembershipResponse(
                membership.getId(),
                userResponse,
                membershipPlanResponse,
                membership.getStartDate(),
                membership.getEndDate(),
                membership.isStatus()
        );
        return membershipResponse;
    }
}
