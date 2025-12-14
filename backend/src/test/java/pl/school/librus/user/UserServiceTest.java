package pl.school.librus.user;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.school.librus.user.api.request.RegisterUserRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegister() {

        //given
        String rawPassword = "password";
        String encryptedPassword = "encrypted password";

        RegisterUserRequest request = new RegisterUserRequest(
            "kamil",
            rawPassword,
            "kamil@mail.com",
            "kamil",
            "nowak",
            "123"
        );

        UserEntity expectedRequestUser = UserEntity.builder()
            .username(request.username())
            .password(encryptedPassword)
            .email(request.email())
            .firstname(request.firstname())
            .surname(request.surname())
            .phone(request.phone())
            .build();

        UserEntity expectedSavedUser = UserEntity.builder()
            .id(UUID.randomUUID())
            .username(request.username())
            .password(encryptedPassword)
            .email(request.email())
            .firstname(request.firstname())
            .surname(request.surname())
            .phone(request.phone())
            .build();

        //when
        Mockito.when(userRepository.existsByUsername(any())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(any())).thenReturn(encryptedPassword);
        Mockito.when(userRepository.save(any())).thenReturn(expectedSavedUser);

        UserEntity newUser = userService.register(request);

        //then
        Mockito.verify(userRepository).existsByUsername(request.username());
        Mockito.verify(passwordEncoder).encode(rawPassword);

        ArgumentCaptor<UserEntity> toSaveUserCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).save(toSaveUserCaptor.capture());

        UserEntity toSaveUser = toSaveUserCaptor.getValue();
        assertNotNull(toSaveUser);
        assertEquals(expectedRequestUser, toSaveUser);

        assertNotNull(newUser);
        assertEquals(expectedSavedUser, newUser);

        Mockito.verify(userRepository).save(toSaveUser);
    }

    @Test
    void shouldNotRegisterWithDuplicateUsername() {

        //given
        RegisterUserRequest request = new RegisterUserRequest(
            "kamil",
            "password",
            "kamil@mail.com",
            "kamil",
            "nowak",
            "123"
        );

        //when
        Mockito.when(userRepository.existsByUsername(any())).thenReturn(true);

        //then
        assertThrows(EntityExistsException.class, () -> userService.register(request));

        Mockito.verify(userRepository).existsByUsername(request.username());
    }

    @Test
    void shouldLoadUserByUsernameWhenExists() {

        //given
        String checkUsername = "kamil";

        Optional<UserEntity> expectedUserOpt = Optional.of(new UserEntity());

        //when
        Mockito.when(userRepository.findByUsername(any())).thenReturn(expectedUserOpt);

        UserDetails gotUser = userService.loadUserByUsername(checkUsername);

        //then
        assertEquals(expectedUserOpt.get(), gotUser);

        Mockito.verify(userRepository).findByUsername(checkUsername);
    }

    @Test
    void shouldNotLoadUserByUsernameWhenDoesNotExist() {

        //given
        String checkUsername = "kamil";

        //when
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        //then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(checkUsername));

        Mockito.verify(userRepository).findByUsername(checkUsername);
    }
}