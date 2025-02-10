package G5_SWP391.ChildGrownTracking.configurations;

import G5_SWP391.ChildGrownTracking.models.Child;
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
            // Thêm User
            userRepository.save(new G5_SWP391.ChildGrownTracking.models.User("Kien", "afefaf", "fewafaf", "gwegs000", membership.BASIC, new Date(), new Date(), "true"));
            userRepository.save(new G5_SWP391.ChildGrownTracking.models.User("Hehe", "afefaf", "fewafaf", "gwegs000", membership.PREMIUM, new Date(), new Date(), "true"));

            // Thêm 2 Child
            childRepository.save(new Child("Bé A", new Date(), "Male", "P001", LocalDateTime.now(), LocalDateTime.now(), true));
            childRepository.save(new Child("Bé B", new Date(), "Female", "P002", LocalDateTime.now(), LocalDateTime.now(), true));

        };
    }
}
