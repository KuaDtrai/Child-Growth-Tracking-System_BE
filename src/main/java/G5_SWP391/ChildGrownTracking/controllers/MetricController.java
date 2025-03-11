package G5_SWP391.ChildGrownTracking.controllers;


import G5_SWP391.ChildGrownTracking.dtos.MetricRequestDTO;
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


    // http://localhost:8080/api/v1/metric/
    @GetMapping("/findByChildId")
    public ResponseEntity<?> getMetricsByChildId(@RequestParam Long childId) {
        return service.getAllMetricByChildId(childId);
    }

    // http://localhost:8080/api/v1/metric/height
    @GetMapping("/height")
    public ResponseEntity<?> getHeightByChildId(@RequestParam Long childId) {
        return service.getHeightAndRecordedDate(childId);
    }

    // http://localhost:8080/api/v1/metric/weight
    @GetMapping("/weight")
    public ResponseEntity<?> getWeightByChildId(@RequestParam Long childId) {
        return service.getWeightAndRecordedDate(childId);
    }

    // http://localhost:8080/api/v1/metric/bmi
    @GetMapping("/bmi")
    public ResponseEntity<?> getBMIByChildId(@RequestParam Long childId) {
        return service.getBMIAndRecordedDate(childId);
    }
    // http://localhost:8080/api/v1/metric/create
    @PostMapping("/create")

    public ResponseEntity<?> createMetric(
            @Valid
            @RequestBody MetricRequestDTO request) {
        return service.createMetric(request);
    }
}
