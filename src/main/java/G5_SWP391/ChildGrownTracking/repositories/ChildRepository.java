package G5_SWP391.ChildGrownTracking.repositories;

import G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO;
import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByNameContainingIgnoreCaseAndStatusIsTrue(String name);
    List<Child> findByParentAndStatusIsTrue(User parent);
    List<Child> findByStatusIsTrue();
    Optional<Child> findByIdAndStatusIsTrue(Long id);


    boolean existsByIdAndStatusIsTrue(Long childId);











    @Modifying
    @Transactional
    @Query("UPDATE Child c SET c.status = :status WHERE c.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") boolean status);






}