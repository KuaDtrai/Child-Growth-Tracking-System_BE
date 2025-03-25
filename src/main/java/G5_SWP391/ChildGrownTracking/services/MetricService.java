package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.MetricRequestDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Metric;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.MetricRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository ;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<ResponseObject> getAllMetricByChildId(Long childId) {
        if (childId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Child ID is required.", null));
        }

        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(childId);
        if (childOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Child with ID " + childId + " not found.", null));
        }

        Child child = childOptional.get(); // Chắc chắn có dữ liệu


        List<Metric> activeMetrics = metricRepository.findByChildAndStatusIsTrue(child);

        if (activeMetrics.isEmpty() || !userRepository.existsByChildrenAndStatusIsTrue(child)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No metrics found for childId: " + childId, null));
        }

        return ResponseEntity.ok(new ResponseObject("ok", "Metrics found for childId: " + childId, activeMetrics));
    }



    public ResponseEntity<?> createMetric(MetricRequestDTO inputMetric) {
        // Kiểm tra dữ liệu thiếu
        if (inputMetric.getChildId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Child ID is required.");
        }
        if (inputMetric.getWeight() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Weight is required.");
        }
        if (inputMetric.getHeight() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Height is required.");
        }
        if (inputMetric.getRecordedDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recorded date is required.");
        }
        if (inputMetric.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Weight must be greater than zero.");
        }
        if (inputMetric.getHeight().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Height must be greater than zero.");
        }

        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(inputMetric.getChildId());

        if (childOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with ID " + inputMetric.getChildId() + " not found.");
        }

        Child child = childOptional.get();

        Optional<User> userOptional = userRepository.findByChildrenAndStatusIsTrue(child);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with child ID " + inputMetric.getChildId() + " not found.");
        }

        User user = userOptional.get();
        LocalDateTime childDobLocalDateTime = Instant.ofEpochMilli(child.getDob().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (inputMetric.getRecordedDate().isBefore(childDobLocalDateTime)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recorded date is before child date.");
        }



        // Tính BMI = weight / (height * height) (chiều cao tính theo mét)
        BigDecimal heightInMeters = inputMetric.getHeight().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); // Chuyển cm -> m
        BigDecimal BMI = inputMetric.getWeight().divide(heightInMeters.multiply(heightInMeters), 2, RoundingMode.HALF_UP);
//        Optional<Child> childOptional = childRepository.findByIdAndStatusIsTrue(inputMetric.getChildId());
//        Child child = childOptional.get(); // Chắc chắn có dữ liệu
        // Tạo mới Metric
        Metric newMetric = new Metric();
        newMetric.setChild(child);
        newMetric.setWeight(inputMetric.getWeight());
        newMetric.setHeight(inputMetric.getHeight());
        newMetric.setBMI(BMI);
        newMetric.setRecordedDate(inputMetric.getRecordedDate());
        newMetric.setCreateDate(LocalDateTime.now());
        newMetric.setStatus(true);

        metricRepository.save(newMetric);

        // Trả về phản hồi có thông báo thành công
        return ResponseEntity.status(HttpStatus.CREATED).body(
        new ResponseObject("ok", "Metric created successfully.", newMetric.toString()));
    }


    public ResponseEntity<ResponseObject> deleteMetric(Long metricId) {

        // Kiểm tra dữ liệu thiếu
        if (metricId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("fail", "Metric ID is required.", null));
        }


        Optional<Metric> metricOptional = metricRepository.findByIdAndStatusIsTrue(metricId);
        if (metricOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Metric with ID " + metricId + " not found.", null));
        }


        Metric metric = metricOptional.get();

        if (!childRepository.existsByIdAndStatusIsTrue(metric.getChild().getId())
        && !userRepository.existsByChildrenAndStatusIsTrue(metric.getChild())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "Metric with ID " + metricId + " not found.", null));
        }

        metric.setStatus(false);
        metricRepository.save(metric);

        return ResponseEntity.status(HttpStatus.OK).body(
        new ResponseObject("ok", "Metric with ID " + metricId + " deleted successfully.", null)
        );
    }
}
