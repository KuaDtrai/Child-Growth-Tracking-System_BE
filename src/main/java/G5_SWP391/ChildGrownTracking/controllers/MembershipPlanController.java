package G5_SWP391.ChildGrownTracking.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import G5_SWP391.ChildGrownTracking.models.Membership;
import G5_SWP391.ChildGrownTracking.repositories.MembershipRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import G5_SWP391.ChildGrownTracking.dtos.MembershipPlanDTO;
import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import G5_SWP391.ChildGrownTracking.repositories.MembershipPlanRepository;
import G5_SWP391.ChildGrownTracking.responses.MembershipPlanResponse;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.MembershipPlanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/membershipplan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MembershipPlanController {

    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanService membershipPlanService;
    private final MembershipRepository membershipRepository;

    @GetMapping("/getAllActive")
    public ResponseEntity<ResponseObject> getMembershipPlan() {
        List<MembershipPlan> membershipPlans = membershipPlanRepository.findAllByStatusIsTrue();
        List<MembershipPlanResponse> membershipPlansResponse = new ArrayList<>();
        for (MembershipPlan membershipPlan : membershipPlans) {
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
                    membershipPlan.getDuration());
            membershipPlansResponse.add(membershipPlanResponse);
        }
        if (membershipPlansResponse.size() > 0)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Membership plans founded", membershipPlansResponse));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No membership plan founded", null));
    }

    @GetMapping("/count/{planId}")
    public ResponseEntity<ResponseObject> getMembershipCount(@PathVariable Long planId,@Valid @RequestParam boolean status) {
        Optional<MembershipPlan> membershipPlan = membershipPlanRepository.findById(planId);
        if (membershipPlan.isPresent()) {
            List<Membership> memberships = membershipRepository.getMembershipsByPlanAndStatusIs(membershipPlan.get(), status);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Membership count", memberships.size()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("fail", "No membership founded", null));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject> getMembershipPlans() {
        List<MembershipPlan> membershipPlans = membershipPlanRepository.findAll();
        List<MembershipPlanResponse> membershipPlansResponse = new ArrayList<>();
        for (MembershipPlan membershipPlan : membershipPlans) {
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
                    membershipPlan.getDuration());
            membershipPlansResponse.add(membershipPlanResponse);
        }
        if (membershipPlansResponse.size() > 0)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Membership plans founded", membershipPlansResponse));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No membership plan founded", null));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createMembershipPlan(@Valid @RequestBody MembershipPlanDTO membershipPlanDTO) {
        if (membershipPlanDTO.getName().trim().isEmpty() || membershipPlanDTO.getAnnualPrice() < 0 || membershipPlanDTO.getDuration() < 0
                || membershipPlanDTO.getMaxChildren() <= 0
        ) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Name require, Price/Duration must be positive & max children must greater than 0", null));

        MembershipPlanResponse membershipPlan = membershipPlanService.saveMembershipPlan(membershipPlanDTO);
        if (membershipPlan != null)
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Membership plan create successfully", membershipPlan));
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("fail", "Membership plan's name is already exist", null));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateMembershipPlan(
            @PathVariable Long id,
            @RequestBody MembershipPlanDTO membershipPlanDTO) {
        if (membershipPlanDTO.getName().trim().isEmpty() || membershipPlanDTO.getAnnualPrice() < 0 || membershipPlanDTO.getDuration() < 0
                || membershipPlanDTO.getMaxChildren() <= 0
        ) return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "Name require, Price/Duration must be positive & max children must greater than 0", null));

        MembershipPlan membershipPlan = membershipPlanRepository.findById(id).orElse(null);
        if (membershipPlan != null) {
            MembershipPlanResponse membershipPlanResponse = membershipPlanService.updateMembershipPlan(membershipPlan,
                    membershipPlanDTO);
            if (membershipPlan != null)
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Membership plan modified successfully", membershipPlanResponse));
            else
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("fail", "No membership plan modified", membershipPlanResponse));
        } else
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("fail", "Membership plan is already existed", null));
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<ResponseObject> disableMembershipPlan(@PathVariable Long id) {
        return membershipPlanRepository.findById(id)
                .map(mp -> {
                    mp.setStatus(false);
                    membershipPlanRepository.save(mp);
                    ResponseObject resp = new ResponseObject(
                            "ok",
                            "Membership plan disable successfully",
                            null);
                    return ResponseEntity.ok(resp);
                })
                .orElseGet(() -> {

                    ResponseObject resp = new ResponseObject(
                            "fail",
                            "No membership plan found",
                            null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
                });
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<ResponseObject> activeMembershipPlan(@PathVariable Long id) {
        return membershipPlanRepository.findById(id)
                .map(mp -> {
                    mp.setStatus(true);
                    membershipPlanRepository.save(mp);
                    ResponseObject resp = new ResponseObject(
                            "ok",
                            "Membership plan active successfully",
                            null);
                    return ResponseEntity.ok(resp);
                })
                .orElseGet(() -> {

                    ResponseObject resp = new ResponseObject(
                            "fail",
                            "No membership plan found",
                            null);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
                });
    }
}
