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
    @GetMapping("/findByChildId")
    public ResponseEntity<ResponseObject> getMetricsByChildId(@RequestParam(required = false) Long childId ) {
        return service.getAllMetricByChildId(childId);
    }


    // http://localhost:8080/api/v1/metric/create
    @PostMapping("/create")

    public ResponseEntity<?> createMetric(@RequestBody(required = false) MetricRequestDTO request) {
        return service.createMetric(request);
    }

    // http://localhost:8080/api/v1/metric/delete
    @PutMapping("/delete")
    public ResponseEntity<ResponseObject> deleteMetric(@RequestParam(required = false) Long metricId) {
        return service.deleteMetric(metricId);
    }

}
