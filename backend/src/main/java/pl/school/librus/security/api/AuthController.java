package pl.school.librus.security.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.school.librus.security.AuthService;
import pl.school.librus.security.api.request.LoginRequest;
import pl.school.librus.security.api.response.LoggedUserTokensResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/token")
    public ResponseEntity<LoggedUserTokensResponse> login(@RequestBody @Valid LoginRequest request){

        LoggedUserTokensResponse loggedUserResponse = authService.login(request);

        return ResponseEntity.ok(loggedUserResponse);
    }
}
