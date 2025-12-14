package pl.school.librus.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.user.api.request.RegisterUserRequest;
import pl.school.librus.user.api.response.UserDetailsResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<UserDetailsResponse>> getPage(Pageable pageable){

        Page<UserEntity> foundUsersPage = userService.getPage(pageable);
        Page<UserDetailsResponse> foundUsersResponsePage = foundUsersPage
            .map(user -> userMapper.map(user));

        return ResponseEntity.ok(foundUsersResponsePage);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> register(@RequestBody @Valid RegisterUserRequest request){

        UserEntity createdUser = userService.register(request);
        UserDetailsResponse response = userMapper.map(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        return ResponseEntity.ok("Test");
    }
}
