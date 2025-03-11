package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
import G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<ResponseObject> getAllChildren() {
        List<Child> children = childRepository.findByStatusIsTrue();
        List<ChildResponseDTO> childResponseList = new ArrayList<>();

        for (Child child : children) {
            if (child.getParent() != null && child.getParent().isStatus()) { // Kiểm tra parent có tồn tại và active
                ChildResponseDTO dto = new ChildResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getDob(),
                        child.getGender(),
                        child.getParent().getUserName(), // Lấy tên cha/mẹ
                        child.getCreateDate(),
                        child.getUpdateDate(),
                        child.isStatus()
                );
                childResponseList.add(dto);
            }
        }

        if (childResponseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No active children with active parents", null));
        }

        return ResponseEntity.ok(new ResponseObject("ok", "List of active children", childResponseList));
    }



    public ResponseEntity<ResponseObject> findChildByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid name: name cannot be empty or null!", null));
        }

        // Tìm tất cả Child có status = true và tên chứa từ khóa (không phân biệt hoa thường)
        List<Child> children = childRepository.findByNameContainingIgnoreCaseAndStatusIsTrue(name);

        // Kiểm tra status của parent sau khi lấy dữ liệu
        List<ChildResponseDTO> childResponseList = new ArrayList<>();
        for (Child child : children) {
            if (child.getParent() != null && child.getParent().isStatus() && child.isStatus()) {
                // Nếu parent có status = true thì thêm vào danh sách
                ChildResponseDTO dto = new ChildResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getDob(),
                        child.getGender(),
                        child.getParent().getUserName(),
                        child.getCreateDate(),
                        child.getUpdateDate(),
                        child.isStatus()
                );
                childResponseList.add(dto);
            }
        }

        if (childResponseList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No children found with name containing: " + name, null));
        }

        return ResponseEntity.ok(new ResponseObject("ok", "Children found with name containing: " + name, childResponseList));
    }


    public ResponseEntity<ResponseObject> getChildById(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid ID: ID cannot be empty or null!", null));
        }

        Optional<Child> optionalChild = childRepository.findByIdAndStatusIsTrue(id);

        if (optionalChild.isPresent()) {
            Child child = optionalChild.get();
            if (child.getParent() == null || !child.getParent().isStatus()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("fail", "Parent of Child is inactive or not found", null));
            }

            ChildResponseDTO childResponseDTO = new ChildResponseDTO(
                    child.getId(),
                    child.getName(),
                    child.getDob(),
                    child.getGender(),
                    child.getParent().getUserName(),  // Lấy userName từ parent
                    child.getCreateDate(),
                    child.getUpdateDate(),
                    child.isStatus()
            );

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Found Child with id: " + id, childResponseDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Cannot find Child with id: " + id, null));
        }
    }



    public ResponseEntity<ResponseObject> findChildrenByParentId(Long parentId) {
        if (parentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid parentId: cannot be empty or null!", null));
        }

        List<ChildResponseDTO> children = childRepository.findByParentIdWithParentName(parentId);

        if (!children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Children belonging to parent ID: " + parentId, children));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No children found for parent ID: " + parentId, null));
        }
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
            childRepository.updateStatusById(id,false);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Deleted child with ID: " + id, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + id + " not found!", null));
        }
    }
}
