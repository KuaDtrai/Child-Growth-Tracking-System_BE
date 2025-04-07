package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.MetricRequestDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Metric;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.MetricRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.MetricResponse;
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
import java.util.ArrayList;
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
        
        List<MetricResponse> metricResponses = new ArrayList<>();
        for (Metric metric : activeMetrics) {
            MetricResponse metricResponse = new MetricResponse(
                    metric.getId(),
                    metric.getWeight(),
                    metric.getHeight(),
                    metric.getBMI(),
                    metric.getRecordedDate(),
                    metric.getCreateDate(),
                    metric.isStatus()
            );
            metricResponses.add(metricResponse);
        }
        
        return ResponseEntity.ok(new ResponseObject("ok", "Metrics found for childId: " + childId, activeMetrics));
    }



    public ResponseEntity<?> createMetric(MetricRequestDTO inputMetric) {
        // Kiểm tra dữ liệu thiếu
        if (inputMetric.getChildId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Child ID is required.", null));
        }
        if (inputMetric.getWeight() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Weight is required.", null));");
        }
        if (inputMetric.getHeight() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Height is required.", null));
        }
        if (inputMetric.getRecordedDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Recorded date is required.", null));
        }
        if (inputMetric.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Weight must be greater than zero.", null));
        }
        if (inputMetric.getHeight().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Height must be greater than zero.", null));
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

        //Kiểm tra không cho metric cùng ngày
        inputMetric.getRecordedDate().toLocalDate();
        List<Metric> metrics = metricRepository.findByChildAndStatusIsTrue(child);
        if (!metrics.isEmpty()){
            for (Metric metric : metrics) {
                if (metric.getRecordedDate().toLocalDate().isEqual(inputMetric.getRecordedDate().toLocalDate()))
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can not create metric with existed recorded date.");
            }
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
        
        // Chặn mức BMI phi thực tế
        if (BMI.compareTo(BigDecimal.TEN) <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Unrealistic BMI detected, must be greater than ten.", null));
        if (BMI.compareTo(BigDecimal.valueOf(60)) >= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("fail", "Unrealistic BMI detected, must be less than 60.", null));

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

        MetricResponse metricResponse = new MetricResponse(newMetric.getId(),
                newMetric.getWeight(),
                newMetric.getHeight(),
                newMetric.getBMI(),
                newMetric.getRecordedDate(),
                newMetric.getCreateDate(),
                newMetric.isStatus()
        );

        // Trả về phản hồi có thông báo thành công
        return ResponseEntity.status(HttpStatus.CREATED).body(
        new ResponseObject("ok", "Metric created successfully.", metricResponse));
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
