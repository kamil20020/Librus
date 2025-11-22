package pl.school.librus.user;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.school.librus.security.JwtService;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserRepository;
import pl.school.librus.user.api.request.RegisterUserRequest;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity register(RegisterUserRequest request) throws EntityExistsException{

        String username = request.username();

        if(userRepository.existsByUsername(username)){
            throw new EntityExistsException("Istnieje już użytkownik o podanym loginie");
        }

        String rawPassword = request.password();
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        UserEntity toCreateUser = UserEntity.builder()
            .username(username)
            .password(encryptedPassword)
            .email(request.email())
            .firstname(request.firstname())
            .surname(request.surname())
        .build();

        return userRepository.save(toCreateUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> foundUserOpt = userRepository.findByUsername(username);

        return foundUserOpt
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " was not found"));
    }
}
