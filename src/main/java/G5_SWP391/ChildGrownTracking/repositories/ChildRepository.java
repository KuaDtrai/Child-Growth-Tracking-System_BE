    package G5_SWP391.ChildGrownTracking.repositories;

    import G5_SWP391.ChildGrownTracking.models.Child;
    import jakarta.transaction.Transactional;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.List;


    public interface ChildRepository extends JpaRepository<Child, Long> {
        List<Child> findByNameContainingIgnoreCaseAndStatusIsTrue(String name);
        List<Child> findByParenIdAndStatusIsTrue(Long parenId);
        List<Child> findByStatusIsTrue();
        List<Child> findByIdAndStatusIsTrue(Long id);
        boolean existsByIdAndStatusIsTrue(Long ChildId);


        @Modifying // dùng khi thay đổi dữ liệu trong database thì xài  @Modifying để đánh dấu nó
        @Transactional // thay đổi data, ảnh hưởng đến database thì phải có @Transactional
        @Query("UPDATE Child c SET c.status = :status WHERE c.id = :id")
        void updateStatusById(@Param("id") Long id, @Param("status") boolean status);



        @Query("SELECT c.id, c.name, c.dob, c.gender, u.userName, c.createDate, c.updateDate, c.status " +
                "FROM Child c JOIN User u ON c.parenId = u.id " +
                "WHERE c.id = :id AND c.status = true")
        List<Object[]> findChildWithParentName(@Param("id") Long id);
        // trả về child nhưng là parent name thay vì parent id




    }
