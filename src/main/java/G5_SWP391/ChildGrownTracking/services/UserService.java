package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(UserDTO userDto) {
        User user = new User(userDto.getUserName(), userDto.getEmail(), userDto.getEmail(), userDto.getRoleId(), membership.valueOf(userDto.getMembership()), userDto.getCreatedDate(), userDto.getLastModifiedDate(), userDto.getStatus());
        return userRepository.save(user);
    }
}
