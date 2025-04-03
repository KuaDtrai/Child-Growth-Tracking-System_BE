package G5_SWP391.ChildGrownTracking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import G5_SWP391.ChildGrownTracking.models.Membership;
import org.springframework.stereotype.Service;

import G5_SWP391.ChildGrownTracking.dtos.UpdateUserDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateUserProfileDTO;
import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.Role;
import G5_SWP391.ChildGrownTracking.repositories.DoctorRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import lombok.extern.slf4j.Slf4j;

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
                    user.getPassword(),
                    user.getRole(),
                    user.getMembership(),
                    user.getCreatedDate(),
                    user.getUpdateDate(),
                    user.isStatus());
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public List<UserResponse> getAllMember() {
        List<User> members = userRepository.findAllByStatusIsTrueAndRole(Role.MEMBER);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : members) {
            UserResponse userResponse = new UserResponse(user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getPassword(),
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
                user.getPassword(),
                user.getRole(),
                user.getMembership(),
                user.getCreatedDate(),
                user.getUpdateDate(),
                user.isStatus());
    }

    public UserResponse getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName).orElse(null);
        assert user != null;
        return new UserResponse(
                user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole(),
                user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus());
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmailAndStatusIsTrue(email).orElse(null);
        return user;
    }

    public UserResponse saveUser(UserDTO userDto) {
        if (!isEmailValid(userDto.getEmail()))
            return null;
        Membership membership = new Membership();

        User user = new User(userDto.getUserName(), userDto.getPassword(), userDto.getEmail(),
                Role.valueOf(userDto.getRole()), membership, java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now(), true);

        if (user.getRole() == Role.DOCTOR) {
            user.setMembership(membership);
            user = userRepository.save(user);
            doctorRepository.save(new Doctor(user, "", ""));
        } else if (user.getRole() == Role.MEMBER) {
            user.setMembership(membership);
            user = userRepository.save(user);
        }
        return new UserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole(),
                user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus());
    }

    public UserResponse updateUser(User user, UpdateUserDTO userDto) {
        if (!isEmailValid(userDto.getEmail()))
            return null;
        if (userRepository.findByEmail(userDto.getEmail()).isPresent() & !user.getEmail().equals(userDto.getEmail()))
            return null;
        Membership membership = new Membership();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setMembership(membership);
        user.setUpdateDate(java.time.LocalDateTime.now());
        user = userRepository.save(user);

        if (user.getRole() == Role.DOCTOR && doctorRepository.findByUser(user) == null) {
            doctorRepository.save(new Doctor(user, "", ""));
        }
        UserResponse userResponse = new UserResponse(
                user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole(),
                user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus());
        return userResponse;
    }

    public UserResponse updateUserTwo(User user, UpdateUserProfileDTO updateUserProfileDTO) {
        if (!isEmailValid(updateUserProfileDTO.getEmail()))
            return null;
        if (userRepository.findByEmail(updateUserProfileDTO.getEmail()).isPresent()
                & !user.getEmail().equals(updateUserProfileDTO.getEmail()))
            return null;

        user.setUserName(updateUserProfileDTO.getUserName());
        user.setEmail(updateUserProfileDTO.getEmail());
        user.setPassword(updateUserProfileDTO.getPassword());
        user = userRepository.save(user);

        return new UserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole(),
                user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus());
    }

    public UserResponse deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(false);
            userRepository.save(user);
            return new UserResponse(
                    user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getRole(),
                    user.getMembership(), user.getCreatedDate(), user.getUpdateDate(), user.isStatus());
        } else {
            return null;
        }
    }

    // public User findUserByUserNameAndPassword(String userName, String password) {
    // return userRepository.findByUserNameAndPassword(userName, password);
    // }

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