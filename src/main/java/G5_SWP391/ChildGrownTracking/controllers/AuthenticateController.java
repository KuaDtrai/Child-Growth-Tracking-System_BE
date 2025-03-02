package G5_SWP391.ChildGrownTracking.controllers;

import G5_SWP391.ChildGrownTracking.dtos.AuthenticateDTO;
import G5_SWP391.ChildGrownTracking.dtos.IntrospcectDTO;
import G5_SWP391.ChildGrownTracking.dtos.UserDTO;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.responses.AuthenticateResponse;
import G5_SWP391.ChildGrownTracking.responses.ResponseObject;
import G5_SWP391.ChildGrownTracking.services.AuthenticateService;
import G5_SWP391.ChildGrownTracking.services.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
public class AuthenticateController {
    private final UserService userService;
    private final AuthenticateService authenticateService;

    //    http://localhost:8080/api/v1/authenticate
    @PostMapping("")
    ResponseEntity<ResponseObject> Register(
            @RequestHeader String username,
            @RequestHeader String email,
            @RequestHeader String password
    ) {
        User user = userService.getUserByUserName(username);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", "Username is already used", null));
        } else {
            UserDTO userDTO = new UserDTO(username, password, email, "", "", true);
            user = userService.saveUser(userDTO);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("ok", "Register successfully", user));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", "Username or password is invalid", null));
            }
        }
    }

    // http://localhost:8080/api/v1/authenticate
    @PostMapping("/token")
    ResponseEntity<ResponseObject> LoginByUserName(
            @RequestBody AuthenticateDTO request
    ) {
        AuthenticateResponse token = authenticateService.authenticate(request);
        if (token != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("ok", "login 1 successfully", token));
        }else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", "Invalid username or password", null));
    }

    @PostMapping("/introspect")
    ResponseEntity<ResponseObject> authenticate(@RequestBody IntrospcectDTO token) throws ParseException, JOSEException {
        var result = authenticateService.introspect(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("ok", "introspect", result));
    }

}
