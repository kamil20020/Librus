package pl.school.librus.role.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.school.librus.RestPage;
import pl.school.librus.role.RoleEntity;
import pl.school.librus.role.RoleRepository;
import pl.school.librus.role.RoleService;
import pl.school.librus.role.api.request.CreateRoleRequest;
import pl.school.librus.role.api.request.PatchRoleRequest;
import pl.school.librus.security.AuthService;
import pl.school.librus.security.api.request.LoginRequest;
import pl.school.librus.security.api.response.LoggedUserTokensResponse;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserRepository;
import pl.school.librus.user.api.response.UserDetailsResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class RoleControllerTestIntegration {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13-alpine");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private Integer port;

    private String testAccessToken;

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/roles";

        userRepository.deleteAll();
        roleRepository.deleteAll();
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

    @Test
    void shouldGetAll() {

        //given
        RoleEntity role1 = new RoleEntity("ADMIN");
        role1 = roleRepository.save(role1);

        RoleEntity role2 = new RoleEntity("STUDENT");
        role2 = roleRepository.save(role2);

        List<RoleEntity> expectedRoles = List.of(role1, role2);

        loadAccessTokenWithUser();

        //when
        List<RoleEntity> gotRoles = RestAssured
            .given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .get()
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<RoleEntity>>(){});

        //then
        assertNotNull(gotRoles);
        assertEquals(expectedRoles.size(), gotRoles.size());
        assertTrue(gotRoles.containsAll(expectedRoles));
    }

    @Test
    void shouldGetRoleUsers() {

        //given
        RoleEntity studentRole = new RoleEntity("STUDENT");
        studentRole = roleRepository.save(studentRole);

        RoleEntity adminRole = new RoleEntity("ADMIN");
        adminRole = roleRepository.save(adminRole);

        UserEntity student = UserEntity.builder()
            .username("adam.nowak")
            .password("password")
            .firstname("adam")
            .surname("nowak")
            .email("adam@mail.com")
            .phone("123")
            .build();
        student = userRepository.save(student);
        roleService.assignRoleToUser(studentRole.getId(), student.getId());

        UserEntity student1 = UserEntity.builder()
            .username("jan.kowalski")
            .password("password1")
            .firstname("jan")
            .surname("kowalski")
            .email("jan@mail.com")
            .phone("123456")
            .build();
        student1 = userRepository.save(student1);
        roleService.assignRoleToUser(studentRole.getId(), student1.getId());

        UserEntity admin = UserEntity.builder()
            .username("piotr.kowalski")
            .password("password2")
            .firstname("piotr")
            .surname("kowalski")
            .email("piotr@mail.com")
            .phone("123456789")
            .build();
        admin = userRepository.save(admin);
        roleService.assignRoleToUser(adminRole.getId(), admin.getId());

        List<UserEntity> expectedUsers = List.of(student, student1);
        List<UserDetailsResponse> expectedUsersResponses = expectedUsers.stream()
            .map(user -> userMapper.map(user))
            .collect(Collectors.toList());

        loadAccessTokenWithUser();

        //when
        Page<UserDetailsResponse> gotUsersDetailsPage = RestAssured
            .given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
                .pathParam("roleId", studentRole.getId().toString())
            .when()
                .get("/{roleId}/users")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<UserDetailsResponse>>(){});

        //then
        assertNotNull(gotUsersDetailsPage);
        assertEquals(expectedUsers.size(), gotUsersDetailsPage.getTotalElements());

        List<UserDetailsResponse> gotUsers = gotUsersDetailsPage.getContent();

        assertTrue(gotUsers.containsAll(expectedUsersResponses));
    }

    @Test
    void shouldCreate() {

        //given
        RoleEntity expectedRole = new RoleEntity("ADMIN");

        loadAccessTokenWithUser();

        CreateRoleRequest request = new CreateRoleRequest(expectedRole.getName());

        //when
        RoleEntity createdRole = RestAssured
            .given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
                .contentType(ContentType.JSON)
                .content(request)
            .when()
                .post()
            .then()
                .statusCode(201)
                .extract()
                .as(RoleEntity.class);

        //then
        assertNotNull(createdRole);
        assertEquals(request.name(), createdRole.getName());
        assertEquals(1, roleRepository.count());

        RoleEntity gotRole = roleRepository.findAll().get(0);

        assertEquals(gotRole, createdRole);
    }

    @Test
    void shouldPatchById() {

        //given
        RoleEntity role = new RoleEntity("ADMIN");
        role = roleRepository.save(role);

        PatchRoleRequest request = new PatchRoleRequest("STUDENT");

        loadAccessTokenWithUser();

        //when
        RoleEntity changedRole = RestAssured
            .given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
                .pathParam("roleId", role.getId().toString())
                .contentType(ContentType.JSON)
                .content(request)
            .when()
                .patch("/{roleId}")
            .then()
                .statusCode(200)
                .extract()
                .as(RoleEntity.class);

        //then
        assertNotNull(changedRole);
        assertEquals(role.getId(), changedRole.getId());
        assertEquals(request.name(), changedRole.getName());

        RoleEntity gotRole = roleRepository.findAll().get(0);

        assertEquals(gotRole, changedRole);
    }

    @Test
    void shouldDeleteById() {

        //given
        RoleEntity role = new RoleEntity("ADMIN");
        role = roleRepository.save(role);

        loadAccessTokenWithUser();

        //when
        RestAssured
            .given()
                .pathParam("roleId", role.getId().toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .delete("/{roleId}")
            .then()
                .statusCode(204);

        //then
        assertEquals(0, roleRepository.count());
    }
}