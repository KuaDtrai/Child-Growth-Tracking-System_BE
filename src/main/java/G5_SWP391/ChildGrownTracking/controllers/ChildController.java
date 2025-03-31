package G5_SWP391.ChildGrownTracking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import G5_SWP391.ChildGrownTracking.dtos.ChildRequestDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateChildRequestDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.ChildService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/child")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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
            @PathVariable("id") Long id) {
        return service.getChildById(id);
    }

    // Search by parentId
    // http://localhost:8080/api/v1/child/findByParentId
    @GetMapping("/findByParentId/{id}")
    public ResponseEntity<ResponseObject> findChildrenByParentId(
            @PathVariable("id") Long id) {
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
            @PathVariable("id") Long id,
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
    @PutMapping("/setDoctor/{id}")
    public ResponseEntity<ResponseObject> setDoctorForChild(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Long doctorId) {
        return service.setDoctorForChild(id, doctorId);
    }

    // get child by doctor
    // http://localhost:8080/api/v1/child/getChildByDoctorId
    @GetMapping("/getChildByDoctorId/{id}")
    public ResponseEntity<ResponseObject> getChildByDoctorId(
            @PathVariable("id") Long id) {
        return service.getChildByDoctorId(id);
    }

}
