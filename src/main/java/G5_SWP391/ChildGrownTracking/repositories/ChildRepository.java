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

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByNameContainingIgnoreCaseAndStatusIsTrue(String name);
    List<Child> findByParentAndStatusIsTrue(User parent);
    List<Child> findByStatusIsTrue();
    List<Child> findByIdAndStatusIsTrue(Long id);
    boolean existsByIdAndStatusIsTrue(Long childId);

    @Modifying
    @Transactional
    @Query("UPDATE Child c SET c.status = :status WHERE c.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") boolean status);

    @Query("SELECT new G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO(" +
            "c.id, c.name, c.dob, c.gender, c.parent.userName, c.createDate, c.updateDate, c.status) " +
            "FROM Child c WHERE c.id = :id AND c.status = true")
    ChildResponseDTO findChildByIdWithParentName(@Param("id") Long id);

    @Query("SELECT new G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO(" +
            "c.id, c.name, c.dob, c.gender, c.parent.userName, c.createDate, c.updateDate, c.status) " +
            "FROM Child c WHERE c.parent.id = :parentId AND c.status = true")
    List<ChildResponseDTO> findByParentIdWithParentName(@Param("parentId") Long parentId);

    @Query("SELECT new G5_SWP391.ChildGrownTracking.dtos.ChildResponseDTO(" +
            "c.id, c.name, c.dob, c.gender, c.parent.userName, c.createDate, c.updateDate, c.status) " +
            "FROM Child c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.status = true")
    List<ChildResponseDTO> findByNameWithParentName(@Param("name") String name);
}