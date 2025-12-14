package pl.school.librus.user;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final UserMapper userMapper;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<UserEntity> getPage(Pageable pageable){

        if(pageable == null){

            pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE);
        }
        else if(pageable.getPageSize() > MAX_PAGE_SIZE){

            pageable = PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE);
        }

        return userRepository.findAll(pageable);
    }

    public UserEntity register(RegisterUserRequest request) throws EntityExistsException{

        String username = request.username();

        if(userRepository.existsByUsername(username)){
            throw new EntityExistsException("Istnieje już użytkownik o podanym loginie");
        }

        String rawPassword = request.password();
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        UserEntity toCreateUser = userMapper.map(request);
        toCreateUser.setPassword(encryptedPassword);

        return userRepository.save(toCreateUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " was not found"));
    }
}
