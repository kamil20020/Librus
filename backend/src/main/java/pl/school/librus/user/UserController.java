package pl.school.librus.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.user.api.request.RegisterUserRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody @Valid RegisterUserRequest request){

        UserEntity createdUser = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("Test");
    }
}
