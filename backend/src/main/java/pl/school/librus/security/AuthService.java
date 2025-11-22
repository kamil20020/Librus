package pl.school.librus.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import pl.school.librus.security.api.request.LoginRequest;
import pl.school.librus.security.api.response.LoggedUserTokensResponse;
import pl.school.librus.user.UserEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoggedUserTokensResponse login(LoginRequest request) throws AuthenticationException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        );

        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserEntity gotUser = (UserEntity) auth.getPrincipal();

        String accessToken = jwtService.getAccessToken(gotUser);
        String refreshToken = jwtService.generateRefreshToken(gotUser.getId().toString(), gotUser.getUsername());

        return new LoggedUserTokensResponse(accessToken, refreshToken);
    }
}
