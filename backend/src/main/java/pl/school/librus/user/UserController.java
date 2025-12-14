package pl.school.librus.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.LoggedUserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<LoggedUserResponse> register(@RequestBody @Valid RegisterUserRequest request){

        UserEntity createdUser = userService.register(request);
        LoggedUserResponse response = userMapper.map(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("Test");
    }
}
