package pl.school.librus.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.security.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<LoggedResponse> login(@RequestBody UserCredentials userCredentials){

        LoggedResponse response = authService.login(userCredentials);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserEntity> create(@RequestBody UserEntity user){

        UserEntity createdUser = userService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("Test");
    }

}
