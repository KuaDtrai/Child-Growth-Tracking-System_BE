package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.MembershipPlanDTO;
import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import G5_SWP391.ChildGrownTracking.repositories.MembershipPlanRepository;
import G5_SWP391.ChildGrownTracking.responses.MembershipPlanResponse;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/membershipplan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MembershipPlanController {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanService membershipPlanService;
    @GetMapping("")
    public ResponseEntity<ResponseObject> getMembershipPlan() {
        List<MembershipPlan> membershipPlans = membershipPlanRepository.findAllByStatusIsTrue();
        if (membershipPlans.size()>0)
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Membership plans founded", membershipPlans));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("fail", "No membership plan founded", membershipPlans));
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createMembershipPlan(@RequestBody MembershipPlanDTO membershipPlanDTO) {
        MembershipPlanResponse membershipPlan = membershipPlanService.saveMembershipPlan(membershipPlanDTO);
        if (membershipPlan != null)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Membership plan create successfully", membershipPlan));
        else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "Membership plan's name is already exist", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateMembershipPlan(
            @PathVariable Long id,
            @RequestBody MembershipPlanDTO membershipPlanDTO) {
        MembershipPlan membershipPlan = membershipPlanRepository.findById(id).orElse(null);
        if (membershipPlan != null) {
            MembershipPlanResponse membershipPlanResponse = membershipPlanService.updateMembershipPlan(membershipPlan, membershipPlanDTO);
            if (membershipPlan != null)
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Membership plan modified successfully", membershipPlanResponse));
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("fail", "No membership plan modified", membershipPlanResponse));
        }
        else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "Membership plan is already existed", null));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteMembershipPlan(@PathVariable Long id) {
        MembershipPlan membershipPlan = membershipPlanRepository.findById(id).orElse(null);
        if (membershipPlan != null) {
            membershipPlan.setStatus(false);
            membershipPlan = membershipPlanRepository.save(membershipPlan);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Membership plan removed successfully", membershipPlan));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "No membership plan founded", null));
    }

}
