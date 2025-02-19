package G5_SWP391.ChildGrownTracking.configurations;

import G5_SWP391.ChildGrownTracking.models.Child;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

@Configuration
public class Database {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ChildRepository childRepository) {
        return args -> {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            userRepository.deleteAll();
            childRepository.deleteAll();

            // Thêm dữ liệu User
            User user1 = new User("Kien", "afefaf", "kien@example.com", "ADMIN", membership.BASIC, LocalDateTime.now(), LocalDateTime.now(), "true");
            User user2 = new User("hehehehehe", "afefaf", "hehe@example.com", "USER", membership.PREMIUM, LocalDateTime.now(), LocalDateTime.now(), "true");

            userRepository.save(user1);
            userRepository.save(user2);

            // Thêm dữ liệu Child
            Child child1 = new Child("Bé A", new Date(), "Male", "P001", LocalDateTime.now(), LocalDateTime.now(), true);
            Child child2 = new Child("Bé B", new Date(), "Female", "P002", LocalDateTime.now(), LocalDateTime.now(), true);

            childRepository.save(child1);
            childRepository.save(child2);

            System.out.println("✅ Dữ liệu User và Child đã được khởi tạo thành công!");
        };
    }
}
