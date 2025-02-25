package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    // http://localhost:8080/api/v1/login
    @GetMapping("/username")
    ResponseEntity<ResponseObject> LoginByUserName(
            @RequestHeader String username,
            @RequestHeader String password
    ) {
        User user = userService.findUserByUserNameAndPassword(username, password);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("ok", "login successfully", user));
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("unauthorized", "login failed", null));
        }
    }

    @GetMapping("/email")
    ResponseEntity<ResponseObject> LoginByEmail(
            @RequestHeader String email,
            @RequestHeader String password
    ){
        User user = userService.findUserByEmailAndPassword(email, password);
        if(user != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("ok", "login successfully", user));
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("unauthorized", "login failed", null));
        }
    }
}
