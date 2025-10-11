package pl.school.librus.user;

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

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity create(UserEntity user){

        String rawPassword = user.getPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> foundUserOpt = userRepository.findByUsername(username);

        return foundUserOpt
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " was not found"));
    }

}
