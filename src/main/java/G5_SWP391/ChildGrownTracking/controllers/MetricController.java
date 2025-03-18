package G5_SWP391.ChildGrownTracking.controllers;


import G5_SWP391.ChildGrownTracking.dtos.MetricRequestDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/metric")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
// http://localhost:8080/api/v1/metric
public class MetricController {


    private final MetricService service;


    // http://localhost:8080/api/v1/metric/findByChildId
    @GetMapping("/findByChildId/{id}")
    public ResponseEntity<ResponseObject> getMetricsByChildId(@PathVariable(required = false) Long id ) {
        return service.getAllMetricByChildId(id);
    }


    // http://localhost:8080/api/v1/metric/create
    @PostMapping("/create")

    public ResponseEntity<?> createMetric(@RequestBody(required = false) MetricRequestDTO request) {
        return service.createMetric(request);
    }

    // http://localhost:8080/api/v1/metric/delete
    @PutMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteMetric(@PathVariable(required = false) Long id ) {
        return service.deleteMetric(id);
    }

}
