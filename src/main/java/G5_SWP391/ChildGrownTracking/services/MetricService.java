package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.MetricRequestDTO;
import G5_SWP391.ChildGrownTracking.dtos.MetricResponseDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Metric;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.MetricRepository;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository ;

    @Autowired
    private ChildRepository childRepository;

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


        Child child = childOptional.get(); // để chắc chắc có dữ liệu trong child đồng thời ép kiểu về child để xài

        // ✅ Kiểm tra Parent (User) của Child có status = true hay không
        User parent = child.getParent();
        if (parent == null || !parent.isStatus()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObject("fail", "Parent of childId " + childId + " is inactive.", null));
        }

        List<Metric> activeMetrics = metricRepository.findByChildAndStatusIsTrue(child);

        if (activeMetrics.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("fail", "No metrics found for childId: " + childId, null));
        }



        // Dùng vòng for để chuyển đổi dữ liệu
        List<MetricResponseDTO> metricDTOs = new ArrayList<>();
        for (Metric metric : activeMetrics) {
            MetricResponseDTO dto = new MetricResponseDTO(
                    metric.getId(),
                    metric.getWeight(),
                    metric.getHeight(),
                    metric.getBMI(),
                    metric.getRecordedDate(),
                    metric.getCreateDate(),
                    metric.isStatus()
            );
            metricDTOs.add(dto);
        }

        return ResponseEntity.ok(new ResponseObject("ok", "Metrics found for childId: " + childId, metricDTOs));
    }



    public ResponseEntity<?> getHeightAndRecordedDate(Long childId) {
        if(childId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Child ID is required.");
        }
        if(!childRepository.existsByIdAndStatusIsTrue(childId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with ID " + childId + " not found.");
        }

        List<Object[]> result = metricRepository.findHeightAndRecordedDateByChildId(childId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No height data found for childId: " + childId);
        }
        List<Map<String, Object>> data = result.stream()
                .map(row -> Map.of("height", row[0], "recordedDate", row[1]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    public ResponseEntity<?> getWeightAndRecordedDate(Long childId) {

        if(childId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Child ID is required.");
        }
        if(!childRepository.existsByIdAndStatusIsTrue(childId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with ID " + childId + " not found.");
        }

        List<Object[]> result = metricRepository.findWeightAndRecordedDateByChildId(childId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No weight data found for childId: " + childId);
        }
        List<Map<String, Object>> data = result.stream()
                .map(row -> Map.of("weight", row[0], "recordedDate", row[1]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    public ResponseEntity<?> getBMIAndRecordedDate(Long childId) {
        if(childId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Child ID is required.");
        }
        if(!childRepository.existsByIdAndStatusIsTrue(childId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with ID " + childId + " not found.");
        }

        List<Object[]> result = metricRepository.findBMIAndRecordedDateByChildId(childId);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No BMI data found for childId: " + childId);
        }
        List<Map<String, Object>> data = result.stream()
                .map(row -> Map.of("BMI", row[0], "recordedDate", row[1]))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }


    public ResponseEntity<?> createMetric(MetricRequestDTO request) {
//        // Kiểm tra dữ liệu thiếu
//        if (request.getChildId() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Child ID is required.");
//        }
//        if (request.getWeight() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Weight is required.");
//        }
//        if (request.getHeight() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Height is required.");
//        }
//        if (request.getRecordedDate() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recorded date is required.");
//        }
//        if (request.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Weight must be greater than zero.");
//        }
//        if (request.getHeight().compareTo(BigDecimal.ZERO) <= 0) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Height must be greater than zero.");
//        }
//
//
//        if(!childRepository.existsByIdAndStatusIsTrue(request.getChildId())){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with ID " + request.getChildId() + " not found.");
//        }
//        // Kiểm tra childId có tồn tại không
//
//        if (childRepository.existsByIdAndStatusIsTrue(request.getChildId())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child with ID " + request.getChildId() + " not found.");
//        }
//
//        // Tính BMI = weight / (height * height) (chiều cao tính theo mét)
//        BigDecimal heightInMeters = request.getHeight().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); // Chuyển cm -> m
//        BigDecimal BMI = request.getWeight().divide(heightInMeters.multiply(heightInMeters), 2, RoundingMode.HALF_UP);
//
//        // Tạo mới Metric
//        Metric metric = new Metric();
//        metric.setChildId(request.getChildId());
//        metric.setWeight(request.getWeight());
//        metric.setHeight(request.getHeight());
//        metric.setBMI(BMI);
//        metric.setRecordedDate(request.getRecordedDate());
//        metric.setCreateDate(LocalDateTime.now());
//        metric.setStatus(true);
//
//        metricRepository.save(metric);

        // Trả về phản hồi có thông báo thành công
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Metric created successfully!",




                "recordedDate", request.getRecordedDate()
        ));
    }




}
