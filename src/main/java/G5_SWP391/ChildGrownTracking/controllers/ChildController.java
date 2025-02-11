package G5_SWP391.ChildGrownTracking.controllers;


import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( path = "/api/v1/child" )
// http://localhost:8080/api/v1/child
public class ChildController {

    //DI dependency injection
    @Autowired
    private ChildRepository repository;
    @Autowired
    private ChildService service;

    // get all children
    // http://localhost:8080/api/v1/child/getAllChild
    @GetMapping("/getAllChild")
    List<Child> getAllChildren() {
        return repository.findAll();
    }
    // http://localhost:8080/api/v1/child/findByName
    @GetMapping("/findByName")
    public ResponseEntity<ResponseObject> findChildByName(@RequestParam String name) {
        List<Child> children = repository.findByNameContainingIgnoreCase(name);

        if (!children.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "ok", "Found children with name containing: " + name, children
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(
                    "fail", "No child found with name containing: " + name, null
            ));
        }
    }

    // get child by id
    // http://localhost:8080/api/v1/child/{id}
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getUserById(@PathVariable("id") Long id){

        Optional<Child> child = repository.findById(id);
        if(child.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "Found Child with id: " +id, child));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "false", "Cannot find Child with id: " +id, null));
        }
    }

    // Create new child
    // http://localhost:8080/api/v1/child/createChild
    @PostMapping("/createChild")
    public ResponseEntity<ResponseObject> createChild(@RequestBody Child newChild) {
        // Kiểm tra dữ liệu đầu vào
        if (newChild.getName() == null || newChild.getDob() == null || newChild.getGender() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("fail", "Missing required fields!", null)
            );
        }

        // Lưu vào database
        Child savedChild = repository.save(newChild);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("ok", "Insert successfully!", repository.save(newChild))
        );
    }

    // Update child by id
    // http://localhost:8080/api/v1/child/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateChild(@PathVariable Long id, @RequestBody Child updatedChild) {
        Optional<Child> existingChild = repository.findById(id);

        if (existingChild.isPresent()) {
            Child child = existingChild.get();

            // Cập nhật các giá trị nếu có trong request
            if (updatedChild.getName() != null) child.setName(updatedChild.getName());
            if (updatedChild.getDob() != null) child.setDob(updatedChild.getDob());
            if (updatedChild.getGender() != null) child.setGender(updatedChild.getGender());
            if (updatedChild.getParenId() != null) child.setParenId(updatedChild.getParenId());
            if (updatedChild.isStatus() != child.isStatus()) child.setStatus(updatedChild.isStatus());

            // Cập nhật thời gian chỉnh sửa
            child.setUpdateDate(LocalDateTime.now());

            // Lưu thay đổi vào database
            repository.save(child);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Updated successfully!", child)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("fail", "Child ID " + id + " not found!", null)
            );
        }
    }
    // http://localhost:8080/api/v1/child/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteChild(@PathVariable Long id) {
        Optional<Child> child = repository.findById(id);

        if (child.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "ok", "Deleted child with ID: " + id, null
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(
                    "fail", "Child with ID " + id + " not found!", null
            ));
        }
    }
}
