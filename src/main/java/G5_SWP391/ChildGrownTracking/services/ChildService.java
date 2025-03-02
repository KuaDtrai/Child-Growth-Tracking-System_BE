package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
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
import java.util.List;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Child> getAllChildren() {
        return childRepository.findByStatusIsTrue();
    }

    public ResponseEntity<ResponseObject> findChildByName(String name) {
        if (name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid name: name cannot be empty or null!", null));
        }

        List<Child> children = childRepository.findByNameContainingIgnoreCaseAndStatusIsTrue(name);
        if (!children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Found children with name containing: " + name, children));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No child found with name containing: " + name, null));
        }
    }

    public ResponseEntity<ResponseObject> getChildById(Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid ID: ID cannot be empty or null!", null));
        }
        Optional<Child> child = childRepository.findById(id);
        if (child.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Found Child with id: " + id, child));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Cannot find Child with id: " + id, null));
        }
    }

    public ResponseEntity<ResponseObject> findChildrenByParentId(Long parentId) {
        if (parentId == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Invalid parentId: cannot be empty or null!", null));
        }


        List<Child> children = childRepository.findByParenId(parentId);
        if (!children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Found children with parentId: " + parentId, children));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No child found with parentId: " + parentId, null));
        }
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

        if (newChild.getParenId() == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Parent ID cannot be empty!", null));
        }

        if(  !userRepository.existsById(newChild.getParenId()) ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Parent ID is not exist!", null));
        }



        Child newChild1 = new Child(newChild.getName(), newChild.getDob(), newChild.getGender(), newChild.getParenId(), LocalDateTime.now(), LocalDateTime.now(), true);

        Child savedChild = childRepository.save(newChild1);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseObject("ok", "Insert successfully!", savedChild));
    }


    public ResponseEntity<ResponseObject> updateChild(Long id, Child updatedChild) {
        Optional<Child> existingChild = childRepository.findById(id);

        if (existingChild.isPresent()) {
            Child child = existingChild.get();
            if (updatedChild.getName() != null) child.setName(updatedChild.getName());
            if (updatedChild.getDob() != null) child.setDob(updatedChild.getDob());
            if (updatedChild.getGender() != null) child.setGender(updatedChild.getGender());
            if (updatedChild.getParenId() != null) child.setParenId(updatedChild.getParenId());
            if (updatedChild.isStatus() != child.isStatus()) child.setStatus(updatedChild.isStatus());

            child.setUpdateDate(LocalDateTime.now());
            childRepository.save(child);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Updated successfully!", child));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child ID " + id + " not found!", null));
        }
    }

    public ResponseEntity<ResponseObject> deleteChild(Long id) {
        Optional<Child> child = childRepository.findById(id);
        if (child.isPresent()) {
            childRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Deleted child with ID: " + id, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + id + " not found!", null));
        }
    }
}
