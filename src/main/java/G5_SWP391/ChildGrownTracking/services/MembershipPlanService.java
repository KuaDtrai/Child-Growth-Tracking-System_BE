package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.MembershipPlanDTO;
import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import G5_SWP391.ChildGrownTracking.repositories.MembershipPlanRepository;
import G5_SWP391.ChildGrownTracking.responses.MembershipPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipPlanService {
    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    public MembershipPlanService(MembershipPlanRepository membershipPlanRepository) {}

    private MembershipPlanResponse getMembershipPlanResponse(MembershipPlan membershipPlan) {
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
        return membershipPlanResponse;
    }

    public MembershipPlanResponse saveMembershipPlan(MembershipPlanDTO membershipPlanDTO) {


        if (membershipPlanRepository.findByName(membershipPlanDTO.getName()) != null)
            return null;

        MembershipPlan membershipPlan = new MembershipPlan(
                membershipPlanDTO.getName(),
                membershipPlanDTO.getDescription(),
                membershipPlanDTO.getFeatures(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                membershipPlanDTO.getAnnualPrice(),
                membershipPlanDTO.getMaxChildren(),
                true,
                membershipPlanDTO.getDuration()
        );
        membershipPlan = membershipPlanRepository.save(membershipPlan);
        return getMembershipPlanResponse(membershipPlan);
    }

    public MembershipPlanResponse updateMembershipPlan(MembershipPlan membershipPlan, MembershipPlanDTO membershipPlanDTO) {
        if (!membershipPlan.getName().equals(membershipPlanDTO.getName()))
            membershipPlan.setName(membershipPlanDTO.getName());


        membershipPlan.setDescription(membershipPlanDTO.getDescription());
        membershipPlan.setFeatures(membershipPlanDTO.getFeatures());
        membershipPlan.setUpdateDate(LocalDateTime.now());
        membershipPlan.setAnnualPrice(membershipPlanDTO.getAnnualPrice());
        membershipPlan.setMaxChildren(membershipPlanDTO.getMaxChildren());
        membershipPlan.setDuration(membershipPlanDTO.getDuration());

        membershipPlan = membershipPlanRepository.save(membershipPlan);

        return getMembershipPlanResponse(membershipPlan);
    }
}
