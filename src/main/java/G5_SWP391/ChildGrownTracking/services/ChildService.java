package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
import G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateChildRequestDTO;
import G5_SWP391.ChildGrownTracking.models.*;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.MembershipPlanRepository;
import G5_SWP391.ChildGrownTracking.repositories.MembershipRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    public ResponseEntity<ResponseObject> getAllChildrenHaveDoctor() {
        List<Child> children = childRepository.findByStatusIsTrue();

        if (children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "not found any child", null));
        }

        List<ChildResponseDTO> childResponseList = new ArrayList<>();

        for (Child child : children) {
            if (child.getParent() != null &&
                    child.getParent().isStatus() &&
                    child.getDoctor() != null &&
                    child.getDoctor().getRole().equals(Role.DOCTOR)) {

                ChildResponseDTO dto = new ChildResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getDob(),
                        child.getGender(),
                        child.getParent().getUserName(), // Lấy tên cha/mẹ
                        child.getDoctor().getUserName(), // Lấy tên bác sĩ
                        child.getCreatedDate(),
                        child.getUpdateDate(),
                        child.isStatus()
                );
                childResponseList.add(dto);
            }
        }

        if (childResponseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "not found any child have doctor", null));
        }

        return ResponseEntity.ok(new ResponseObject("ok", "list of children have doctor", childResponseList));
    }


    public ResponseEntity<ResponseObject> getAllChildDontHaveDoctor() {
        List<Child> children = childRepository.findByStatusIsTrue();

        if (children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "not found any child", null));
        }

        List<ChildResponseDTO> childResponseList = new ArrayList<>();

        for (Child child : children) {
            if (child.getParent() != null &&
                    child.getParent().isStatus() &&
                    child.getDoctor() == null
                   ) {

                ChildResponseDTO dto = new ChildResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getDob(),
                        child.getGender(),
                        child.getParent().getUserName(), // Lấy tên cha/mẹ
                        null, // Lấy tên bác sĩ
                        child.getCreatedDate(),
                        child.getUpdateDate(),
                        child.isStatus()
                );
                childResponseList.add(dto);
            }
        }

        if (childResponseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "not found any child have doctor", null));
        }

        return ResponseEntity.ok(new ResponseObject("ok", "list of children have doctor", childResponseList));
    }


    public ResponseEntity<ResponseObject> getChildById(Long ChildId) {
        if (ChildId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid ID: childId cannot be empty or null!", null));
        }

        Optional<Child> optionalChild = childRepository.findByIdAndStatusIsTrue(ChildId);

        if (optionalChild.isPresent()) {
            Child child = optionalChild.get();
            if (child.getParent() == null || !child.getParent().isStatus()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("fail", "Parent of Child is inactive or not found", null));
            }
            String doctorUserName;
            if (child.getDoctor() != null) {
                doctorUserName = child.getDoctor().getUserName();
            } else {
                doctorUserName = null;
            }
            ChildResponseDTO childResponseDTO = new ChildResponseDTO(
                    child.getId(),
                    child.getName(),
                    child.getDob(),
                    child.getGender(),
                    child.getParent().getUserName(),
                    doctorUserName,
                    child.getCreatedDate(),
                    child.getUpdateDate(),
                    child.isStatus()
            );

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Found Child with id: " + ChildId, childResponseDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Cannot find Child with id: " + ChildId, null));
        }
    }


    public ResponseEntity<ResponseObject> findChildrenByParentId(Long parentId) {
//        Kiểm tra parent hợp lệ k
        if (parentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid parentId: cannot be empty or null!", null));
        }

        Optional<User> parentOptional = userRepository.findByIdAndStatusIsTrue(parentId);

        if (parentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Parent with ID " + parentId + " not found ", null));

        }

        User parent = parentOptional.get();
//      Kiểm tra role của parent có phải member k
        if(parent.getRole() != Role.MEMBER || !parent.isStatus()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Parent with ID " + parentId + " not found  ", null));
        }

        Membership membership = membershipRepository.findByUser(parent);
        MembershipPlan membershipPlan = membership.getPlan();
        int size = membershipPlan.getMaxChildren();
        List<Child> children = childRepository.findByParentAndStatusIsTrue(parent);

        if (children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Parent with ID " + parentId + " has no children", null));
        }

//       Lấy số child trong danh sách dựa trên số trẻ tối đa trong membershipPlan
        List<ChildResponseDTO> childResponseList = new ArrayList<>();
        int counter = 0;
        for (Child child : children) {
            if (counter < size) {
                String doctorUserName;
                if (child.getDoctor() != null) {
                    doctorUserName = child.getDoctor().getUserName();
                } else {
                    doctorUserName = null;
                }
                ChildResponseDTO dto = new ChildResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getDob(),
                        child.getGender(),
                        child.getParent().getUserName(),
                        doctorUserName,
                        child.getCreatedDate(),
                        child.getUpdateDate(),
                        child.isStatus()
                );
                childResponseList.add(dto);
                counter++;
            }
            else
                break;
        }

        return ResponseEntity.ok(new ResponseObject("ok", "List of children of parent with ID " + parentId, childResponseList));
}

    public ResponseEntity<ResponseObject> createChild(ChildRequestDTO newChild) {

        if (newChild.getName() == null || newChild.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Name cannot be empty!", null));
        }

        if (newChild.getDob() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Date of birth is required!", null));
        }

        if (newChild.getGender() == null || newChild.getGender().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Gender cannot be empty!", null));
        }

        if (newChild.getParentId() == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Parent ID cannot be empty!", null));
        }
        Optional<User> parentOptional = userRepository.findByIdAndStatusIsTrue(newChild.getParentId());
        if (parentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Parent ID is not exist!", null));
        }

        User parent = parentOptional.get();

        Membership membership = membershipRepository.findByUser(parent);
        MembershipPlan membershipPlan = membership.getPlan();

        if(parent.getRole() != Role.MEMBER){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Parent ID is not exist!", null));
        }

//        Kiểm tra số lượng trẻ với số lượng qui định trong gói membership
        if(parent.getRole() == Role.MEMBER && parent.getMembership() == Membership.builder().build()){
            List<Child> child = childRepository.findByParentAndStatusIsTrue(parent);
            if(child.size() >= membershipPlan.getMaxChildren()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("fail", "Reach max child for BASIC account !", null));
            }
        }


        Child child = new Child(
                newChild.getName(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                newChild.getDob(),
                newChild.getGender(),
                parent,
                null,
                true
        );

        childRepository.save(child);

        ChildResponseDTO childResponseDTO = new ChildResponseDTO(
                child.getId(),
                child.getName(),
                child.getDob(),
                child.getGender(),
                child.getParent().getUserName(),
                null,
                child.getCreatedDate(),
                child.getUpdateDate(),
                child.isStatus()
        );
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("ok", "Created Child with id: " + child.getId(), childResponseDTO));
    }


    public ResponseEntity<ResponseObject> updateChild(Long childId, UpdateChildRequestDTO childRequest) {
        if (childId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Child ID is required.", null));
        }
        if (childRequest.getName() == null || childRequest.getName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Name cannot be empty!", null));
        }
        if (childRequest.getDob() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Date of birth is required!", null));
        }
        if (childRequest.getGender() == null || childRequest.getGender().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Gender of birth is required!", null));
        }

        // Fetch the child record without validating or updating the parent ID.
        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(childId);
        if (childOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }
        Child child = childOptional.get();

        // Update only the allowed fields
        child.setName(childRequest.getName());
        child.setDob(childRequest.getDob());
        child.setGender(childRequest.getGender());
        // Do not update the parent; it remains unchanged.
        child.setUpdateDate(LocalDateTime.now());

        childRepository.save(child);

        ChildResponseDTO childResponseDTO = new ChildResponseDTO(
                child.getId(),
                child.getName(),
                child.getDob(),
                child.getGender(),
                child.getParent().getUserName(),
                child.getDoctor() != null ? child.getDoctor().getUserName() : null,
                child.getCreatedDate(),
                child.getUpdateDate(),
                child.isStatus()
        );
        return ResponseEntity.ok(new ResponseObject("ok", "Child with ID " + childId + " updated successfully.", childResponseDTO));
    }



    public ResponseEntity<ResponseObject> deleteChild(Long id) {

        if(id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Child ID is required.", null));
        }

        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(id);

        if (childOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + id + " not found.", null));
        }

        Child child = childOptional.get();

        if(!userRepository.existsByChildrenAndStatusIsTrue(child)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Child with ID " + id + " not found.", null));

        }

        child.setStatus(false);
        childRepository.save(child);

        return ResponseEntity.ok(new ResponseObject("ok", "Child with ID " + id + " deleted successfully.", null));

    }

    public ResponseEntity<ResponseObject> setDoctorForChild(Long childId, Long doctorId) {
        if (childId == null || doctorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Child ID and Doctor ID are required.", null));
        }

        Optional<User> doctorOptional = userRepository.findByIdAndStatusIsTrue(doctorId);
        if (doctorOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Doctor with ID " + doctorId + " not found.", null));
        }

        User doctor = doctorOptional.get();

        if(doctor.getRole() != Role.DOCTOR){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "DOCTOR with ID " + doctor.getId() + " not found.", null));
        }

        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(childId);
        if (childOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }

        Child child = childOptional.get();
        child.setDoctor(doctor);


        childRepository.save(child);

        return ResponseEntity.ok(new ResponseObject("ok", "Doctor assigned successfully to child.", null));
    }

    public ResponseEntity<ResponseObject> getChildByDoctorId(Long doctorId) {
        if( doctorId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Doctor ID is required.", null));
        }

        Optional<User> doctorOptional = userRepository.findByIdAndStatusIsTrue(doctorId);

        if(doctorOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Doctor with ID " + doctorId + " not found.", null));
        }

        User doctor = doctorOptional.get();

        if(doctor.getRole() != Role.DOCTOR){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Doctor with ID " + doctorId + " not found.", null));
        }

        List<Child> children = childRepository.findByDoctorAndStatusIsTrue(doctor);

        if(children.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Doctor with ID " + doctorId + " has no children.", null));
        }

        List<ChildResponseDTO> childResponseList = new ArrayList<>();

        for(Child child : children){
            ChildResponseDTO dto = new ChildResponseDTO(
                    child.getId(),
                    child.getName(),
                    child.getDob(),
                    child.getGender(),
                    child.getParent().getUserName(),
                    child.getDoctor().getUserName(),
                    child.getCreatedDate(),
                    child.getUpdateDate(),
                    child.isStatus()
            );
            childResponseList.add(dto);
        }

        return ResponseEntity.ok(new ResponseObject("ok", "List of children of doctor with ID " + doctorId, childResponseList));



    }

    public ResponseEntity<ResponseObject> countAll() {
        Long count = childRepository.countByStatusIsTrue();
        if (count == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "not found any child", null));
        }
        return ResponseEntity.ok(new ResponseObject("ok", "Total number of children: " + count, count));
    }
}
