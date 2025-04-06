package G5_SWP391.ChildGrownTracking.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Metric;


public interface MetricRepository extends JpaRepository<Metric, Long> {

    List<Metric> findByChildIdAndStatusIsTrue (long childId);


    // Lấy height và recordedDate theo childId
//    @Query("SELECT m.height, m.recordedDate FROM Metric m WHERE m.childId = :childId")
    List<Object[]> findHeightAndRecordedDateByChildId(@Param("childId") Long childId);

    // Lấy weight và recordedDate theo childId
//    @Query("SELECT m.weight, m.recordedDate FROM Metric m WHERE m.childId = :childId")
    List<Object[]> findWeightAndRecordedDateByChildId(@Param("childId") Long childId);

    // Lấy BMI và recordedDate theo childId
//    @Query("SELECT m.BMI, m.recordedDate FROM Metric m WHERE m.childId = :childId")
    List<Object[]> findBMIAndRecordedDateByChildId(@Param("childId") Long childId);

    List<Metric> findByChildAndStatusIsTrue(Child child);

    Optional<Metric> findByIdAndStatusIsTrue(Long metricId);
}