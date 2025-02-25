package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userSevice;

    // http://localhost:8080/api/v1/users
    @GetMapping("")
    List<User> getAllUsers(){
        return userSevice.getAllUsers();
    }

    @GetMapping("/userid/{id}")
    ResponseEntity<ResponseObject> getUserById(@PathVariable("id") Long id){

        User user = userSevice.getUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "Found User with id: " +id, user));
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "false", "Cannot find User with id: " +id, null));
        }
    }

    @GetMapping("/username/{username}")
    ResponseEntity<ResponseObject> getUserByUsername(
            @PathVariable("username") String username
    ){
        User user = userSevice.getUserByUserName(username);

        if(user != null)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "Found User with username: " +username, user));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject( "false", "Cannot find User with username: " +username, null));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> addUser(
            @Valid
            @RequestBody UserDTO userDTO
    ){


        User user = userSevice.getUserByUserName(userDTO.getUserName());
        System.out.println(user);
        if (user == null) {
            user = userSevice.saveUser(userDTO);
        }else user = null;

        if(user != null)
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject( "ok", "User saved successfully", user));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "false", "Cannot save User", null));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUserById(@PathVariable("id") Long id){
        User user = userSevice.deleteUserById(id);
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "ok", "User deleted successfully", user));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject( "false", "Cannot delete User with id: "+id, null));
        }
    }
}
