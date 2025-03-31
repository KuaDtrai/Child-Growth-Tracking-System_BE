package G5_SWP391.ChildGrownTracking.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G5_SWP391.ChildGrownTracking.dtos.MetricRequestDTO;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.MetricService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/v1/metric")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
// http://localhost:8080/api/v1/metric
public class MetricController {


    private final MetricService service;


    // http://localhost:8080/api/v1/metric/findByChildId
    @GetMapping("/findByChildId/{id}")
    public ResponseEntity<ResponseObject> getMetricsByChildId(@PathVariable("id") Long id) {
        return service.getAllMetricByChildId(id);
    }


    // http://localhost:8080/api/v1/metric/create
    @PostMapping("/create")

    public ResponseEntity<?> createMetric(@RequestBody(required = false) MetricRequestDTO request) {
        return service.createMetric(request);
    }

    // http://localhost:8080/api/v1/metric/delete
    @PutMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteMetric(@PathVariable("id") Long id) {
        return service.deleteMetric(id);
    }

}
