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
    private final ChildRepository childRepository;

    public Database(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                userRepository.save(new G5_SWP391.ChildGrownTracking.models.User("Kien", "afefaf", "fewafaf", "gwegs000", membership.BASIC, java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), "true"));
                userRepository.save(new G5_SWP391.ChildGrownTracking.models.User("Hehe", "afefaf", "fewafaf", "gwegs000", membership.PREMIUM, java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), "true"));

                // Thêm 2 Child
                childRepository.save(new Child("Bé A", new Date(), "Male", "P001", LocalDateTime.now(), LocalDateTime.now(), true));
                childRepository.save(new Child("Bé B", new Date(), "Female", "P002", LocalDateTime.now(), LocalDateTime.now(), true));
            }
        };
    }
}
