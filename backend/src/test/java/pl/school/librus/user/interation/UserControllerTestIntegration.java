package pl.school.librus.user.interation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.school.librus.RestPage;
import pl.school.librus.security.AuthService;
import pl.school.librus.security.api.request.LoginRequest;
import pl.school.librus.security.api.response.LoggedUserTokensResponse;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserRepository;
import pl.school.librus.user.UserService;
import pl.school.librus.user.api.response.UserDetailsResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UserControllerTestIntegration {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13-alpine");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    private String testAccessToken;

    @BeforeEach
    public void setUp(){

        RestAssured.baseURI = "http://localhost/users";
        RestAssured.port = port;

        userRepository.deleteAll();
    }

    private void loadAccessTokenWithUser(){

        String rawPassword = "password";
        String encryptedPassword = "$2a$10$ylqMiiWlaQCrfF/ETbNSj.xpA6/DIsVJi.kz2GS3M.ZZrhOav3NPC";

        UserEntity loggedUser = UserEntity.builder()
            .username("user")
            .password(encryptedPassword)
            .firstname("user")
            .surname("user")
            .email("user@mail.com")
            .phone("123456")
            .build();

        userRepository.save(loggedUser);

        LoginRequest request = new LoginRequest(
            loggedUser.getUsername(),
            rawPassword
        );

        LoggedUserTokensResponse response = authService.login(request);
        testAccessToken = response.accessToken();
    }

    @WithMockUser
    @Test
    public void shouldFindUsers(){

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("password")
            .firstname("kamil")
            .surname("nowak")
            .email("kamil@mail.com")
            .phone("123")
            .build();

        user = userRepository.save(user);

        UserEntity user1 = UserEntity.builder()
            .username("adam")
            .password("password123")
            .firstname("adam")
            .surname("kowalski")
            .email("adam@mail.com")
            .phone("1234")
            .build();

        user1 = userRepository.save(user1);

        List<UserEntity> expectedUsers = List.of(user, user1);

        int expectedPageSize = 10;
        int expectedPage = 0;

        loadAccessTokenWithUser();

        //when
        Page<UserDetailsResponse> gotResponse = RestAssured
            .given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
                .param("size", expectedPageSize)
                .param("page", expectedPage)
            .when()
                .get()
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<UserDetailsResponse>>(){});

        //then
        assertNotNull(gotResponse);
        assertEquals(expectedUsers.size(), gotResponse.getTotalElements() - 1);
        assertEquals(expectedPageSize, gotResponse.getSize());
        assertEquals(expectedPage, gotResponse.getNumber());

        List<UserDetailsResponse> gotUsers = gotResponse.getContent();

        for(int i = 0; i < expectedUsers.size(); i++){

            UserEntity expectedUser = expectedUsers.get(i);
            UserDetailsResponse expectedUserResponse = userMapper.map(expectedUser);

            UserDetailsResponse gotUserResponse = gotUsers.get(i);

            assertEquals(expectedUserResponse, gotUserResponse);
        }
    }

    @Test
    public void shouldRegister(){

        //given
        UserEntity user = UserEntity.builder()
            .username("kamil")
            .password("password")
            .firstname("kamil")
            .surname("nowak")
            .email("mail@mail.com")
            .phone("123")
            .build();

        //when
        UserDetailsResponse gotUserResponse = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .post("/register")
            .then()
                .statusCode(201)
                .extract()
                .as(UserDetailsResponse.class);

        //then
        assertNotNull(gotUserResponse);
        assertNotNull(gotUserResponse.id());
        assertEquals(user.getUsername(), gotUserResponse.username());
        assertEquals(user.getFirstname(), gotUserResponse.firstname());
        assertEquals(user.getSurname(), gotUserResponse.surname());
        assertEquals(user.getEmail(), gotUserResponse.email());
        assertEquals(user.getPhone(), gotUserResponse.phone());

        assertEquals(1, userRepository.count());

        UUID gotUserId = UUID.fromString(gotUserResponse.id());
        Optional<UserEntity> foundUserOpt = userRepository.findById(gotUserId);

        assertTrue(foundUserOpt.isPresent());

        UserEntity foundUser = foundUserOpt.get();
        assertNotNull(foundUser);

        user.setPassword(foundUser.getPassword());
        user.setId(foundUser.getId());

        assertEquals(user, foundUser);
    }
}
