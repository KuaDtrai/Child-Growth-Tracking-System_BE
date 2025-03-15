package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
import G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private UserRepository userRepository;

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
                    child.getDoctor().getRole().equals(role.DOCTOR)) {

                ChildResponseDTO dto = new ChildResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getDob(),
                        child.getGender(),
                        child.getParent().getUserName(), // Lấy tên cha/mẹ
                        child.getDoctor().getUserName(), // Lấy tên bác sĩ
                        child.getCreateDate(),
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
                        child.getCreateDate(),
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
                    child.getCreateDate(),
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
//        if (parentId == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Invalid parentId: cannot be empty or null!", null));
//        }
//
//        List<ChildResponseDTO> children = childRepository.findByParentIdWithParentName(parentId);
//
//        if (!children.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "Children belonging to parent ID: " + parentId, children));
//        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No children found for parent ID: " + parentId, null));
        }



    public ResponseEntity<ResponseObject> createChild(ChildRequestDTO newChild) {
//        if (newChild.getName() == null || newChild.getName().trim().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Name cannot be empty!", null));
//        }
//
//        if (newChild.getDob() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Date of birth is required!", null));
//        }
//
//        if (newChild.getGender() == null || newChild.getGender().trim().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Gender cannot be empty!", null));
//        }
//
//        if (newChild.getParenId() == null ) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Parent ID cannot be empty!", null));
//        }
//
//        if(  !userRepository.existsById(newChild.getParenId()) ){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Parent ID is not exist!", null));
//        }
//
//
//
//        Child newChild1 = new Child(newChild.getName(), newChild.getDob(), newChild.getGender(), newChild.getParenId(), LocalDateTime.now(), LocalDateTime.now(), true);
//
//        Child savedChild = childRepository.save(newChild1);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObject("ok", "Insert successfully!", null));
    }


    public ResponseEntity<ResponseObject> updateChild(Long id, ChildRequestDTO childRequest) {
//        // Lấy đối tượng Child từ database, kiểm tra trạng thái
//        Child child = childRepository.findByIdAndStatusIsTrue(id)
//                .orElse(null);
//        if (child == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObject("fail", "Child ID " + id + " not found or inactive!", null));
//        }
//
//        // Kiểm tra parentId hợp lệ
//        User parent = userRepository.findByIdAndStatusIsTrue(childRequest.getParenId())
//                .orElse(null);
//        if (parent == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("fail", "Invalid Parent ID: Parent does not exist or is inactive!", null));
//        }
//
//        // Cập nhật thông tin nếu có dữ liệu mới
//        if (childRequest.getName() != null) child.setName(childRequest.getName());
//        if (childRequest.getDob() != null) child.setDob(childRequest.getDob());
//        if (childRequest.getGender() != null) child.setGender(childRequest.getGender());
//        if (parent != null) child.setParent(parent);
//
//        // Cập nhật thời gian chỉnh sửa
//        child.setUpdateDate(LocalDateTime.now());
//
//        // Lưu thay đổi vào database
//        childRepository.save(child);
//
//        // Truy vấn lại để lấy ChildResponseDTO (bao gồm parentName)
//        ChildResponseDTO updatedChildDTO = childRepository.findChildByIdWithParentName(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Updated successfully!", null));
    }


    public ResponseEntity<ResponseObject> deleteChild(Long id) {

        if (childRepository.existsByIdAndStatusIsTrue(id)) {
            childRepository.updateStatusById(id, false);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Deleted child with ID: " + id, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + id + " not found!", null));
        }
    }

    public ResponseEntity<ResponseObject> setDoctorForChild(Long childId, Long doctorId) {
        if (childId == null || doctorId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Child ID and Doctor ID are required.", null));
        }

        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(childId);
        if (childOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }

        Optional<User> doctorOptional = userRepository.findByIdAndStatusIsTrue(doctorId);
        if (doctorOptional.isEmpty() || !doctorOptional.get().getRole().equals(role.DOCTOR)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Doctor (User) with ID " + doctorId + " not found.", null));
        }

        // Gán user (doctor) cho child
        Child child = childOptional.get();
        User doctor = doctorOptional.get();
        child.setDoctor(doctor);

        // Lưu thay đổi vào database
        childRepository.save(child);

        return ResponseEntity.ok(new ResponseObject("ok", "Doctor assigned successfully to child.", null));
    }
}
