package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateChildRequestDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.ChildService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/child")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
// http://localhost:8080/api/v1/child

public class ChildController {


    private final ChildService service;




    // Get all children
    // http://localhost:8080/api/v1/child/getAllChildHaveDoctor
    @GetMapping("/getAllChildHaveDoctor")
    public ResponseEntity<ResponseObject> getAllChildHaveDoctor() {
        return service.getAllChildrenHaveDoctor();
    }

    // Get all children
    // http://localhost:8080/api/v1/child/getAllChildDontHaveDoctor
    @GetMapping("/getAllChildDontHaveDoctor")
    public ResponseEntity<ResponseObject> getAllChildDontHaveDoctor() {
        return service.getAllChildDontHaveDoctor();
    }


    // Get child by ID
    // http://localhost:8080/api/v1/child/findById
    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseObject> getChildById(
            @PathVariable(required = false) Long id) {
        return service.getChildById(id);
    }

    // Search by parentId
    // http://localhost:8080/api/v1/child/findByParentId
    @GetMapping("/findByParentId/{id}")
    public ResponseEntity<ResponseObject> findChildrenByParentId(
            @PathVariable(required = false) Long id ) {
        return service.findChildrenByParentId(id);
    }

    // Create new child
    // http://localhost:8080/api/v1/child/createChild
    @PostMapping("/createChild")
    public ResponseEntity<ResponseObject> createChild(
            @RequestBody(required = false) ChildRequestDTO newChild) {
        return service.createChild(newChild);
    }


    // Update child by ID
    // http://localhost:8080/api/v1/child/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateChild(
            @PathVariable(required = false) Long id,
            @RequestBody(required = false) UpdateChildRequestDTO updatedChild) {
        return service.updateChild(id, updatedChild);
    }

    // Delete child by ID
    // http://localhost:8080/api/v1/child/delete/{id}
    @PutMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteChild(@PathVariable(required = false) Long id) {
        return service.deleteChild(id);
    }

    // set doctor for child
    // http://localhost:8080/api/v1/child/setDoctor/
    @PutMapping("/setDoctor/")
    public ResponseEntity<ResponseObject> setDoctorForChild(
            @RequestParam(required = false) Long childId,
            @RequestParam(required = false) Long doctorId) {
        return service.setDoctorForChild(childId, doctorId);
    }
}
