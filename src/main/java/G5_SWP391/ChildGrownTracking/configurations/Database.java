package G5_SWP391.ChildGrownTracking.configurations;

import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                userRepository.save(new G5_SWP391.ChildGrownTracking.models.User("Kien", "afefaf", "fewafaf", "gwegs000", membership.BASIC, new java.util.Date(), new java.util.Date(), "true"));
                userRepository.save(new G5_SWP391.ChildGrownTracking.models.User("Hehe", "afefaf", "fewafaf", "gwegs000", membership.PREMIUM, new java.util.Date(), new java.util.Date(), "true"));
            }
        };
    }
}
