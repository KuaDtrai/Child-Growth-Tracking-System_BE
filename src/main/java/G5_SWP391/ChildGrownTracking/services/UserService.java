package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.Role;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByStatusIsTrue();
    }

    public User getUserById(Long id) {
        User user = userRepository.findByIdAndStatusIsTrue(id);
        return user;
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public User saveUser(UserDTO userDto) {
        User user = new User(userDto.getUserName(),
                userDto.getPassword(),
                userDto.getEmail(),
                Role.valueOf(userDto.getRoleId()),
                membership.valueOf(userDto.getMembership()),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        return userRepository.save(user);

    }

    public User deleteUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setStatus(false);
            return userRepository.save(user);
        }else {
            return null;
        }
    }

    public User updateUser(long id, UserDTO userDto) {
        User user = getUserById(id);
        if (user != null) {
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            user.setEmail(userDto.getEmail());
            user.setRoleId(Role.valueOf(userDto.getRoleId()));
            user.setMembershipId(membership.valueOf(userDto.getMembership()));
            user.setStatus(userDto.isStatus());
            return userRepository.save(user);
        }else return null;
    }

    public User findUserByUserNameAndPassword(String userName, String password) {
        return userRepository.findByUserNameAndPasswordAndStatusIsTrue(userName, password);
    }

    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }
}
