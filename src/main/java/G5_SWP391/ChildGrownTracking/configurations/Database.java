package G5_SWP391.ChildGrownTracking.configurations;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.Metric;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
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
    CommandLineRunner initDatabase(UserRepository userRepository, ChildRepository childRepository, MetricRepository metricRepository) {
        return args -> {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            userRepository.deleteAll();
            childRepository.deleteAll();
            metricRepository.deleteAll();;
            // Thêm dữ liệu User
            User user1 = new User("Kien", "afefaf", "kien@example.com", "ADMIN", membership.BASIC, LocalDateTime.now(), LocalDateTime.now(), "true");
            User user2 = new User("hehehehehe", "afefaf", "hehe@example.com", "USER", membership.PREMIUM, LocalDateTime.now(), LocalDateTime.now(), "true");

            userRepository.save(user1);
            userRepository.save(user2);

            // Thêm dữ liệu Child
            Long user1Id = user1.getId();
            Long user2Id = user2.getId();
            Child child1 = new Child("Bé A", new Date(), "Male", user1Id, LocalDateTime.now(), LocalDateTime.now(), true);
            Child child2 = new Child("Bé B", new Date(), "Female", user2Id, LocalDateTime.now(), LocalDateTime.now(), true);

            childRepository.save(child1);
            childRepository.save(child2);



            Metric metric1 = createMetric(child1.getId(), 85.3, 125);
            Metric metric2 = createMetric(child1.getId(), 87.1, 130);
            Metric metric3 = createMetric(child2.getId(), 78.5, 120);
            metricRepository.save(metric1);
            metricRepository.save(metric2);
            metricRepository.save(metric3);

            System.out.println("✅ Dữ liệu User và Child đã được khởi tạo thành công!");
        };
    }
    private Metric createMetric(Long childId, double weight, double height) {
        BigDecimal weightBD = BigDecimal.valueOf(weight);
        BigDecimal heightBD = BigDecimal.valueOf(height); // Giữ nguyên cm khi lưu
        BigDecimal heightInMeters = heightBD.divide(BigDecimal.valueOf(100)); // Chuyển cm -> m để tính BMI
        BigDecimal bmi = weightBD.divide(heightInMeters.multiply(heightInMeters), 2, BigDecimal.ROUND_HALF_UP);

        return new Metric(childId, weightBD, heightBD, bmi, LocalDateTime.now(), LocalDateTime.now(), true);
    }




}
