package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
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


    private final   ChildService service;




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

    // Search by name
    // http://localhost:8080/api/v1/child/findByName
    @GetMapping("/findByName")
    public ResponseEntity<ResponseObject> findChildByName(@RequestParam String name) {
        return service.findChildByName(name);
    }

    // Get child by ID
    // http://localhost:8080/api/v1/child/findById
    @GetMapping("/findById")
    public ResponseEntity<ResponseObject> getChildById(@RequestParam(required = false) Long childId ) {
        return service.getChildById(childId);
    }

    // Search by parentId
    // http://localhost:8080/api/v1/child/findByParentId
    @GetMapping("/findByParentId")
    public ResponseEntity<ResponseObject> findChildrenByParentId(@RequestParam Long parentId) {
        return service.findChildrenByParentId(parentId);
    }

    // Create new child
    // http://localhost:8080/api/v1/child/createChild
    @PostMapping("/createChild")
    public ResponseEntity<ResponseObject> createChild( @RequestBody ChildRequestDTO newChild) {
        return service.createChild(newChild);
    }


    // Update child by ID
    // http://localhost:8080/api/v1/child/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateChild(@PathVariable Long id, @RequestBody ChildRequestDTO updatedChild) {
        return service.updateChild(id, updatedChild);
    }

    // Delete child by ID
    // http://localhost:8080/api/v1/child/delete/{id}
    @PutMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteChild(@PathVariable Long id) {
        return service.deleteChild(id);
    }

    // set doctor for child
    // http://localhost:8080/api/v1/child/setDoctor/
    @PutMapping("/setDoctor/")
    public ResponseEntity<ResponseObject> setDoctorForChild(@RequestParam Long childId, @RequestParam Long doctorId) {
        return service.setDoctorForChild(childId, doctorId);
    }
}
