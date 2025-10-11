package pl.school.librus.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.school.librus.user.LoggedResponse;
import pl.school.librus.user.UserCredentials;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoggedResponse login(UserCredentials userCredentials){

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userCredentials.username(),
            userCredentials.password()
        );

        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserEntity gotUser = (UserEntity) auth.getPrincipal();

        String accessToken = jwtService.getAccessToken(gotUser);
        String refreshToken = jwtService.generateRefreshToken(gotUser.getId().toString(), gotUser.getUsername());

        return new LoggedResponse(accessToken, refreshToken);
    }

}
