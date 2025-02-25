package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public User saveUser(UserDTO userDto) {
        User user = new User(userDto.getUserName(), userDto.getEmail(), userDto.getEmail(), userDto.getRoleId(), membership.valueOf(userDto.getMembership()),java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), userDto.getStatus());
        return userRepository.save(user);

    }

    public User deleteUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            userRepository.deleteById(id);
            return user;
        }else {
            return null;
        }
    }

    public User findUserByUserNameAndPassword(String userName, String password) {
        return userRepository.findByUserNameAndPassword(userName, password);
    }

    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }
}
