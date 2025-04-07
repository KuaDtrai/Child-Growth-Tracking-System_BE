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
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    public MembershipService(MembershipRepository membershipRepository, UserRepository userRepository, MembershipPlanRepository membershipPlanRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    public MembershipResponse getMembershipByUserId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        Membership membership = membershipRepository.findByUser(user);

        if (user != null && membership != null) {
            MembershipResponse membershipResponse = new MembershipResponse(
                    membership.getId(),
                    membership.getUser().getUserName(),
                    membership.getPlan().getName(),
                    membership.getStartDate(),
                    membership.getEndDate(),
                    membership.isStatus()
            );
            return membershipResponse;
        }
        return null;
    }

    public MembershipResponse updateMembership(Long userId, Long membershipPlanId) {
        User user = userRepository.findById(userId).orElse(null);
        Membership membership = membershipRepository.findByUser(user);
        if (membership == null) return null;
        MembershipPlan oldPlan = membership.getPlan();

//        Nếu từ basic thì bth
//        Nếu không từ basic
        if (!oldPlan.getName().equals("BASIC") && membership.getEndDate().isAfter(LocalDateTime.now()))
            return null;

        MembershipPlan membershipPlan = membershipPlanRepository.findById(membershipPlanId).orElse(null);
        if (membershipPlan == null) return null;

        membership.setPlan(membershipPlan);
        membership.setStartDate(LocalDateTime.now());
        membership.setEndDate(LocalDateTime.now().plusDays(membershipPlan.getDuration()));
        if (user.getRole().equals(Role.DOCTOR)) {
            membership.setStatus(false);
        }
        membership = membershipRepository.save(membership);

        MembershipResponse membershipResponse = new MembershipResponse(
                membership.getId(),
                membership.getUser().getUserName(),
                membership.getPlan().getName(),
                membership.getStartDate(),
                membership.getEndDate(),
                membership.isStatus()
        );
        return membershipResponse;
    }
}
