package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.DoctorDTO;
import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.membership;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.DoctorRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
import G5_SWP391.ChildGrownTracking.responses.DoctorResponse;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import G5_SWP391.ChildGrownTracking.services.DoctorService;
import G5_SWP391.ChildGrownTracking.services.UserService;
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false", "Not Found UserList", users));
        }
    }

    // http://localhost:8080/api/v1/users/doctor
    @GetMapping("/doctor")
    ResponseEntity<ResponseObject> getAllDoctor(){
        List<DoctorResponse> users = doctorSevice.getAllDoctor();
        if(!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Found UserList", users));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("false", "Not Found UserList", users));
        }
    }

    // http://localhost:8080/api/v1/users/userid/{id}
    @GetMapping("/userid/{id}")
    ResponseEntity<ResponseObject> getUserById(@PathVariable("id") Long id){

        UserResponse user = userSevice.getUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "Found User with id: " +id, user));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "false", "Cannot find User with id: " +id, null));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "false", "Cannot find User with username: " +username, null));
    }

    // http://localhost:8080/api/v1/users
    @PostMapping("")
    ResponseEntity<ResponseObject> addUser(
            @Valid
            @RequestBody UserDTO userDTO
    ){
        User user = userSevice.getUserByEmail(userDTO.getUserName());
        if (user == null) {
            UserResponse newUser = userSevice.saveUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject( "ok", "User saved successfully", newUser));

        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "false", "Cannot save User", null));
    }

    // http://localhost:8080/api/v1/users/{id}
    @PutMapping("")
    ResponseEntity<ResponseObject> updateUser(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody UserDTO userDTO
    ){
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            UserResponse userResponse = userSevice.updateUser(user, userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User updated successfully", userResponse));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "false", "Cannot update User", null));
    }

    // http://localhost:8080/api/v1/users/membership/{id}
    @PutMapping("/membership/{id}")
    ResponseEntity<ResponseObject> updateUserMembership(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody membership membership
    ){
        User user = userRepository.findById(id).get();
        user.setMembership(membership);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User updated successfully", null));
    }

    // http://localhost:8080/api/v1/users/doctor/{id}
//    @PostMapping("/doctor/{id}")
//    ResponseEntity<ResponseObject> addDoctor(
//            @Valid @PathVariable("id") Long id,
//            @Valid @RequestBody DoctorDTO doctorDTO
//    ){
//        User user = userRepository.findById(id).get();
//        if(user != null){
//            DoctorResponse doctorResponse = doctorSevice.addDoctor(user, doctorDTO);
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User added successfully", doctorResponse));
//        }else
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "ok", "User added successfully", null));
//    }

    // http://localhost:8080/api/v1/users/doctor/{id}
    @PutMapping("/doctor/{id}")
    ResponseEntity<ResponseObject> updateDoctor(
            @Valid @PathVariable("id") Long doctorId,
            @Valid @RequestBody DoctorDTO doctorDTO
    ){
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if(doctor != null){
            DoctorResponse doctorResponse = doctorSevice.updateDoctor(doctor, doctorDTO);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User updated successfully", doctorResponse));
        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "false", "Cannot find Doctor with id: " +doctorId, null));
    }

    // http://localhost:8080/api/v1/users/{id}
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUserById(@PathVariable("id") Long id){
        UserResponse user = userSevice.deleteUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User deleted successfully", user));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "false", "Cannot delete User with id: "+id, null));
        }
    }
}
