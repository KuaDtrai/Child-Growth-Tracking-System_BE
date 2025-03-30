package G5_SWP391.ChildGrownTracking.services;

import G5_SWP391.ChildGrownTracking.dtos.DoctorDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateUserDTO;
import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.DoctorRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.DoctorResponse;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public UserService(UserRepository userRepository, DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getMembership(),
                    user.getCreatedDate(),
                    user.getUpdateDate(),
                    user.isStatus()
            );
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public List<UserResponse> getAllMember() {
        List<User> members = userRepository.findAllByStatusIsTrueAndRole(role.MEMBER);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : members) {
            UserResponse userResponse = new UserResponse(user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getMembership(),
                    user.getCreatedDate(),
                    user.getUpdateDate(),
                    user.isStatus());
            userResponses.add(userResponse);
        }
        return userResponses;
    }



    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        assert user != null;
        return new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getMembership(),
                user.getCreatedDate(),
                user.getUpdateDate(),
                user.isStatus()
        );
    }

    public UserResponse getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName).orElse(null);
        assert user != null;
        return new UserResponse(
                user.getId(), user.getUserName(), user.getEmail(), user.getRole(), user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus()
        );
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmailAndStatusIsTrue(email).orElse(null);
        return user;
    }

    public UserResponse saveUser(UserDTO userDto) {
        if (!isEmailValid(userDto.getEmail()))
            return null;
        User user = new User(userDto.getUserName(), userDto.getPassword(), userDto.getEmail(), role.valueOf(userDto.getRole()), membership.BASIC,java.time.LocalDateTime.now(), java.time.LocalDateTime.now(), true);

        if (user.getRole() == role.DOCTOR){
            user.setMembership(membership.PREMIUM);
            user = userRepository.save(user);
            doctorRepository.save(new Doctor(user, "", ""));
        } else if (user.getRole() == role.MEMBER) {
            user.setMembership(membership.BASIC);
            user = userRepository.save(user);
        }
        return new UserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getRole(), user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus());
    }

    public UserResponse updateUser(User user, UpdateUserDTO userDto) {
        if (!isEmailValid(userDto.getEmail()))
            return null;

        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setRole(role.valueOf(userDto.getRole()));
        user.setMembership(membership.valueOf(userDto.getMembership()));
        user.setUpdateDate(java.time.LocalDateTime.now());
        user = userRepository.save(user);

        if (user.getRole() == role.DOCTOR){
            doctorRepository.save(new Doctor(user, "", ""));
        }
        UserResponse userResponse = new UserResponse(
                user.getId(), user.getUserName(), user.getEmail(), user.getRole(), user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus()
        );
        return userResponse;
    }

    public UserResponse deleteUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            user.setStatus(false);
            userRepository.save(user);
            return new UserResponse(
                    user.getId(), user.getUserName(), user.getEmail(), user.getRole(), user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus()
            );
        }else {
            return null;
        }
    }

//    public User findUserByUserNameAndPassword(String userName, String password) {
//        return userRepository.findByUserNameAndPassword(userName, password);
//    }



    public User findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    public static boolean isEmailValid(String email) {

        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);

        // Check if email matches the pattern
        return email != null && p.matcher(email).matches();
    }
}
