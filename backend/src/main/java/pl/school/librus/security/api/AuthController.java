package pl.school.librus.security.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.school.librus.security.AuthService;
import pl.school.librus.security.api.request.LoginRequest;
import pl.school.librus.security.api.response.LoggedUserTokensResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<LoggedUserTokensResponse> login(@RequestBody @Valid LoginRequest request){

        LoggedUserTokensResponse loggedUserResponse = authService.login(request);

        return ResponseEntity.ok(loggedUserResponse);
    }
}
