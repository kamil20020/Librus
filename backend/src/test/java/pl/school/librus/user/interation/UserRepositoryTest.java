package pl.school.librus.user.interation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class UserRepositoryTest {

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:13-alpine");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clear(){
        userRepository.deleteAll();
    }

    @CsvSource({
        "kamil",
        " kamil",
        "kamil ",
        " kamil ",
        "    kamil   ",
    })
    @ParameterizedTest
    public void shouldExistByUsernameWhenExists(String checkUsername) {

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("password")
            .email("mail@mail.com")
            .phone("123")
            .build();

        userRepository.save(user);

        //when
        boolean doesExist = userRepository.existsByUsername(checkUsername);

        //then
        assertTrue(doesExist);
    }

    @CsvSource({
        "kamil1",
        "3kamil",
        "kam2il",
        "221221"
    })
    @ParameterizedTest
    public void shouldExistByUsernameWhenDoesNotExist(String checkUsername) {

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("password")
            .email("mail@mail.com")
            .phone("123")
            .build();

        userRepository.save(user);

        //when
        boolean doesExist = userRepository.existsByUsername(checkUsername);

        //then
        assertFalse(doesExist);
    }

    @CsvSource({
        "kamil",
        " kamil",
        "kamil ",
        " kamil ",
        "    kamil   ",
    })
    @ParameterizedTest
    public void shouldFindByUsernameWhenExists(String checkUsername) {

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("password")
            .email("mail@mail.com")
            .phone("123")
            .build();

        user = userRepository.save(user);

        //when
        Optional<UserEntity> foundUserOpt = userRepository.findByUsername(checkUsername);

        //then
        assertTrue(foundUserOpt.isPresent());
        assertEquals(user.getUsername(), foundUserOpt.get().getUsername());
    }

    @CsvSource({
        "kamil1",
        "3kamil",
        "kam2il",
        "221221"
    })
    @ParameterizedTest
    public void shouldNotFindByUsernameWhenDoesNotExist(String checkUsername) {

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("password")
            .email("mail@mail.com")
            .phone("123")
            .build();

        user = userRepository.save(user);

        //when
        Optional<UserEntity> foundUserOpt = userRepository.findByUsername(checkUsername);

        //then
        assertTrue(foundUserOpt.isEmpty());
    }
}