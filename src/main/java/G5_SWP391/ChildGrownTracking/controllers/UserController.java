package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.DoctorDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateUserDTO;
import G5_SWP391.ChildGrownTracking.dtos.UpdateUserProfileDTO;
import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.DoctorRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.DoctorResponse;
import G5_SWP391.ChildGrownTracking.responses.DoctorResponse2;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import G5_SWP391.ChildGrownTracking.services.DoctorService;
import G5_SWP391.ChildGrownTracking.services.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userSevice;
    private final UserRepository userRepository;
    private final DoctorService doctorSevice;
    private final DoctorRepository doctorRepository;

    // http://localhost:8080/api/v1/users/member
    @GetMapping("/member")
    ResponseEntity<ResponseObject> getUsers() {
        List<UserResponse> users = userSevice.getAllMember();
        if(!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Found UserList", users));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("fail", "Not Found UserList", null));
        }
    }

    // http://localhost:8080/api/v1/users/doctor
    @GetMapping("/doctor")
    ResponseEntity<ResponseObject> getAllDoctor(){
        List<DoctorResponse2> users = doctorSevice.getAllDoctor();
        if(!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Found UserList", users));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("fail", "Not Found UserList", null));
        }
    }

    @GetMapping("/doctor/{id}")
    ResponseEntity<ResponseObject> getDoctorSpec(@PathVariable Long id){
        DoctorResponse doctorResponse = doctorSevice.getDoctor(id);
        if (doctorResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Found Doctor", doctorResponse));
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("fail", "Not Found Doctor", null));
    }

    // http://localhost:8080/api/v1/users/userid/{id}
    @GetMapping("/userid/{id}")
    ResponseEntity<ResponseObject> getUserById(@PathVariable("id") Long id){

        UserResponse user = userSevice.getUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "Found User with id: " +id, user));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "fail", "Cannot find User with id: " +id, null));
        }
    }

    // http://localhost:8080/api/v1/users/username/{username}
    @GetMapping("/username/{username}")
    ResponseEntity<ResponseObject> getUserByUsername(
            @PathVariable("username") String username
    ){
        UserResponse user = userSevice.getUserByUserName(username);

        if(user != null)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "Found User with username: " +username, user));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "fail", "Cannot find User with username: " +username, null));
    }

    // http://localhost:8080/api/v1/users
    @PostMapping("")
    ResponseEntity<ResponseObject> addUser(
            @Valid
            @RequestBody UserDTO userDTO
    ){
        User user = userSevice.getUserByEmail(userDTO.getEmail());
        if (user == null) {
            UserResponse newUser = userSevice.saveUser(userDTO);
            if (newUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject( "ok", "User saved successfully", newUser));
            }else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject( "fail", "User saved fail", null));
        }else
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "false", "User is already exist", null));
    }

    // http://localhost:8080/api/v1/users/{id}
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateUser(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserDTO userDTO
            ){
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            UserResponse userResponse = userSevice.updateUser(user, userDTO);
            if (userResponse != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User updated successfully", userResponse));
            }else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject( "fail", "User updated fail", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "fail", "User not found", null));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateUserById(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserProfileDTO userProfileDTO
            ){
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            UserResponse userResponse = userSevice.updateUserTwo(user, userProfileDTO);
            if (userResponse != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User updated successfully", userResponse));
            }
            else
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "fail", "User updated faily", null));
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "fail", "User not found", null));
    }

    // http://localhost:8080/api/v1/users/membership/{id}
    @PutMapping("/membership/{id}")
    ResponseEntity<ResponseObject> updateUserMembership(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody membership membership
    ){
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            user.setMembership(membership);
            user = userRepository.save(user);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "User updated successfully", user));
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("fail", "User updated fail", null));
            }
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "fail", "User not found", null));

    }

    // http://localhost:8080/api/v1/users/doctor/{id}
    @PutMapping("/doctor/{id}")
    ResponseEntity<ResponseObject> updateDoctor(
            @Valid @PathVariable("id") Long doctorId,
            @Valid @RequestBody DoctorDTO doctorDTO
    ){
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if(doctor != null){
            DoctorResponse doctorResponse = doctorSevice.updateDoctor(doctor, doctorDTO);
            if (doctorResponse != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User updated successfully", doctorResponse));
            }else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "fail", "Doctor not found", null));
        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "fail", "Cannot find Doctor with id: " +doctorId, null));
    }

    // http://localhost:8080/api/v1/users/{id}
    @PutMapping("/delete/{id}")
    ResponseEntity<ResponseObject> deleteUserById(@PathVariable("id") Long id){
        UserResponse user = userSevice.deleteUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User deleted successfully", user));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "false", "Cannot delete User with id: "+id, null));
        }
    }
}
