package G5_SWP391.ChildGrownTracking.configurations;

import G5_SWP391.ChildGrownTracking.models.*;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.DoctorRepository;
import G5_SWP391.ChildGrownTracking.repositories.MetricRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class Database {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ChildRepository childRepository, MetricRepository metricRepository, DoctorRepository doctorRepository) {
        return args -> {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            userRepository.deleteAll();
            childRepository.deleteAll();
            metricRepository.deleteAll();
            doctorRepository.deleteAll();

            // Thêm dữ liệu User
            User user1 = new User("kien",  "afefaf", "kien@example.com", role.MEMBER ,membership.BASIC, LocalDateTime.now(), LocalDateTime.now(), true);
            User user2 = new User("ducanh",  "afefaf", "hehe@example.com", role.MEMBER ,membership.PREMIUM, LocalDateTime.now(), LocalDateTime.now(), true);
            User user3 = new User("ducphan",  "afefaf", "hehe@example.com", role.DOCTOR ,membership.PREMIUM, LocalDateTime.now(), LocalDateTime.now(), true);

            user1 = userRepository.save(user1); // Lưu User trước
            user2 = userRepository.save(user2);
            user3 = userRepository.save(user3);

            Doctor doctor1 = new Doctor(user3, "grgsg", "sgsegrg");

            doctor1 = doctorRepository.save(doctor1);

            Child child1 = new Child("Bé A", new Date(), "Male", user1, LocalDateTime.now(), LocalDateTime.now(), true);
            Child child2 = new Child("Bé B", new Date(), "Female", user2, LocalDateTime.now(), LocalDateTime.now(), true);

            childRepository.save(child1); // Lưu Child sau khi User đã có ID
            childRepository.save(child2);


            // Thêm dữ liệu Metric (truyền `Child` vào thay vì `Long childId`)
            Metric metric1 = createMetric(child1, 85.3, 125);
            Metric metric2 = createMetric(child1, 87.1, 130);
            Metric metric3 = createMetric(child2, 78.5, 120);

            metricRepository.save(metric1);
            metricRepository.save(metric2);
            metricRepository.save(metric3);

            System.out.println("✅ Dữ liệu đã được khởi tạo thành công!");
        };
    }

    private Metric createMetric(Child child, double weight, double height) {
        BigDecimal weightBD = BigDecimal.valueOf(weight);
        BigDecimal heightBD = BigDecimal.valueOf(height); // Giữ nguyên cm khi lưu
        BigDecimal heightInMeters = heightBD.divide(BigDecimal.valueOf(100)); // Chuyển cm -> m để tính BMI
        BigDecimal bmi = weightBD.divide(heightInMeters.multiply(heightInMeters), 2, BigDecimal.ROUND_HALF_UP);

        return new Metric( child, weightBD, heightBD, bmi, LocalDateTime.now(), LocalDateTime.now(), true);
    }
}
