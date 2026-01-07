package pl.school.librus.person.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.school.librus.RestPage;
import pl.school.librus.person.PersonAddress;
import pl.school.librus.person.PersonEntity;
import pl.school.librus.person.PersonRepository;
import pl.school.librus.person.PersonService;
import pl.school.librus.person.api.request.address.CreateAddressRequest;
import pl.school.librus.person.api.request.address.PatchAddressRequest;
import pl.school.librus.person.api.request.person.CreatePersonRequest;
import pl.school.librus.person.api.request.person.PatchPersonRequest;
import pl.school.librus.person.api.request.person.SearchPersonRequest;
import pl.school.librus.person.api.response.PersonResponse;
import pl.school.librus.role.RoleService;
import pl.school.librus.security.AuthService;
import pl.school.librus.security.api.request.LoginRequest;
import pl.school.librus.security.api.response.LoggedUserTokensResponse;
import pl.school.librus.user.UserEntity;
import pl.school.librus.user.UserMapper;
import pl.school.librus.user.UserRepository;
import pl.school.librus.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonControllerTestIntegration {

    @ServiceConnection
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13-alpine");

    @LocalServerPort
    private Integer port;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private UserRepository userRepository;

    private String testAccessToken = "";

    @BeforeEach
    public void setUp(){

        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost/persons";

        personRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UserEntity loadAccessTokenWithUser(){

       return loadAccessTokenWithUser("user");
    }

    private UserEntity loadAccessTokenWithUser(String username){

        String rawPassword = "password";
        String encryptedPassword = "$2a$10$ylqMiiWlaQCrfF/ETbNSj.xpA6/DIsVJi.kz2GS3M.ZZrhOav3NPC";

        UserEntity loggedUser = UserEntity.builder()
            .username(username)
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

        return loggedUser;
    }

    @CsvSource(value = {
        "Kamil, 1",
        "Adam, 1",
        "kamil, 1",
        "kAmil, 1",
        "kamil , 1",
        "a, 2",
        "kam1il, 0",
        "1kamil, 0",
        "kamil1, 0",
        "1kamil1, 0"
    })
    @ParameterizedTest
    public void shouldGetPageWithFirstname(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wroclaw")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone("+48 123 456 789")
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone("+48 123 456 789")
            .address(personAddress)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            "", searchValue, null, null, null
        );

        //when
        Page<PersonResponse> foundPage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundPage);
        assertEquals(pageable.getPageNumber(), foundPage.getNumber());
        assertEquals(pageable.getPageSize(), foundPage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundPage.getTotalElements());

        List<PersonResponse> gotPersons = foundPage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.firstname().toUpperCase())
            .filter(value -> value.contains(searchValue.toUpperCase()))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @CsvSource(value = {
        "Nowak, 1",
        "Kowalski, 1",
        "nowak, 1",
        "nOwak, 1",
        "nowak , 1",
        "o, 2",
        "now1ak, 0",
        "1nowak, 0",
        "nowak1, 0",
        "1nowak1, 0"
    })
    @ParameterizedTest
    public void shouldGetPageWithSurname(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wroclaw")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone("+48 123 456 789")
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("kamil.nowak@mail.com")
            .phone("+48 123 456 789")
            .address(personAddress)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            "", null, searchValue, null, null
        );

        //when
        Page<PersonResponse> foundPage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundPage);
        assertEquals(pageable.getPageNumber(), foundPage.getNumber());
        assertEquals(pageable.getPageSize(), foundPage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundPage.getTotalElements());

        List<PersonResponse> gotPersons = foundPage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.surname().toUpperCase())
            .filter(value -> value.contains(searchValue.toUpperCase()))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @CsvSource(value = {
        "Wrocław, 1",
        "Poznań, 1",
        "wrocław, 1",
        "wrOcław, 1",
        "wrocław , 1",
        "o, 2",
        "wroc1ław, 0",
        "1wrocław, 0",
        "wrocław1, 0",
        "1wrocław1, 0"
    })
    @ParameterizedTest
    public void shouldGetPageWithCity(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wrocław")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonAddress personAddress1 = PersonAddress.builder()
            .city("Poznań")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone("+48 123 456 789")
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("kamil.nowak@mail.com")
            .phone("+48 123 456 789")
            .address(personAddress1)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            "", null, null, null, searchValue
        );

        //when
        Page<PersonResponse> foundPage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundPage);
        assertEquals(pageable.getPageNumber(), foundPage.getNumber());
        assertEquals(pageable.getPageSize(), foundPage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundPage.getTotalElements());

        List<PersonResponse> gotPersons = foundPage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.address().getCity().toUpperCase())
            .filter(value -> value.contains(searchValue.toUpperCase()))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @CsvSource(value = {
        "123456789, 1",
        "+48123456789, 1",
        "+48 234 567 890, 1",
        "234-567-890, 1",
        "+48 234-567-890, 1",
        "+48 123 456 789 , 1",
        "234, 2",
        "+48 125 456 789, 0",
        "1+48 123 456 789, 0",
        "1+48 123 456 7891, 0",
        "1+48 123 456 7891, 0"
    })
    @ParameterizedTest
    public void shouldGetPageWithPhone(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wroclaw")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone(PersonEntity.clearPhone("+48 123 456 789"))
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("kamil.nowak@mail.com")
            .phone(PersonEntity.clearPhone("+48 234 567 890"))
            .address(personAddress)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            "", null, null, searchValue, null
        );

        //when
        Page<PersonResponse> foundResponsePage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundResponsePage);
        assertEquals(pageable.getPageNumber(), foundResponsePage.getNumber());
        assertEquals(pageable.getPageSize(), foundResponsePage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundResponsePage.getTotalElements());

        List<PersonResponse> gotPersons = foundResponsePage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.phone())
            .filter(value -> value.contains(PersonEntity.clearPhone(searchValue)))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @CsvSource(value = {
        "Kamil, 1",
        "Kamil1, 0",
        "Adam, 1",
        "Adam1, 0"
    })
    @ParameterizedTest
    public void shouldGetPageWithSearchTextByFirstname(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wroclaw")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone("+48123456789")
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("kamil.nowak@mail.com")
            .phone("+48234567890")
            .address(personAddress)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            searchValue, null, null, null, null
        );

        //when
        Page<PersonResponse> foundPage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundPage);
        assertEquals(pageable.getPageNumber(), foundPage.getNumber());
        assertEquals(pageable.getPageSize(), foundPage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundPage.getTotalElements());

        List<PersonResponse> gotPersons = foundPage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.firstname().toUpperCase())
            .filter(value -> value.contains(searchValue.toUpperCase()))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @CsvSource(value = {
        "Nowak, 1",
        "Nowak1, 0",
        "Kowalski, 1",
        "Kowalski1, 0",
    })
    @ParameterizedTest
    public void shouldGetPageWithSearchTextBySurname(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wroclaw")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone("+48123456789")
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("kamil.nowak@mail.com")
            .phone("+48234567890")
            .address(personAddress)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            searchValue, null, null, null, null
        );

        //when
        Page<PersonResponse> foundPage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundPage);
        assertEquals(pageable.getPageNumber(), foundPage.getNumber());
        assertEquals(pageable.getPageSize(), foundPage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundPage.getTotalElements());

        List<PersonResponse> gotPersons = foundPage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.surname().toUpperCase())
            .filter(value -> value.contains(searchValue.toUpperCase()))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @CsvSource(value = {
        "+48 123 456 789, 1",
        "+48 124 456 789, 0",
        "+48 234 567 890, 1",
        "1+48 234 567 890, 0",
    })
    @ParameterizedTest
    public void shouldGetPageWithSearchTextByPhone(String searchValue, Integer expectedNumberOfPersons){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        Pageable pageable = PageRequest.of(0, 5);

        PersonAddress personAddress = PersonAddress.builder()
            .city("Wroclaw")
            .street("Street 123")
            .postCode("12-345")
            .buildingNumber("12B")
            .buildingFloor("12")
            .doorCode("14")
            .build();

        PersonEntity person = PersonEntity.builder()
            .firstname("Kamil")
            .surname("Nowak")
            .email("kamil.nowak@mail.com")
            .phone(PersonEntity.clearPhone("+48123456789"))
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PersonEntity person1 = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("kamil.nowak@mail.com")
            .phone(PersonEntity.clearPhone("+48234567890"))
            .address(personAddress)
            .user(loadAccessTokenWithUser("user1"))
            .build();

        person1 = personRepository.save(person1);

        SearchPersonRequest request = new SearchPersonRequest(
            searchValue, null, null, null, null
        );

        //when
        Page<PersonResponse> foundPage = RestAssured
            .given()
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post("/search")
            .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<RestPage<PersonResponse>>(){});

        //then
        assertNotNull(foundPage);
        assertEquals(pageable.getPageNumber(), foundPage.getNumber());
        assertEquals(pageable.getPageSize(), foundPage.getSize());
        assertEquals(expectedNumberOfPersons, (int) foundPage.getTotalElements());

        List<PersonResponse> gotPersons = foundPage.getContent();

        assertNotNull(gotPersons);

        long gotNumberOfGoodValues = gotPersons.stream()
            .map(p -> p.phone())
            .filter(value -> value.contains(PersonEntity.clearPhone(searchValue)))
            .count();

        assertEquals(expectedNumberOfPersons, (int) gotNumberOfGoodValues);
    }

    @Test
    public void shouldCreate(){

        //given
        CreateAddressRequest addressRequest = new CreateAddressRequest(
            "Wrocław",
            "Street 123",
            "12-345",
            "12B",
            "12",
            "14"
        );
        CreatePersonRequest request = new CreatePersonRequest(
            "Kamil",
            "Nowak",
            "kamil.nowak@mail.com",
            "+48 123 456 789",
            addressRequest
        );

        loadAccessTokenWithUser();

        //when
        PersonResponse createdPerson = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .post()
            .then()
                .statusCode(201)
                .extract()
                .as(PersonResponse.class);

        //then
        assertNotNull(createdPerson);
        assertNotNull(createdPerson.id());
        assertEquals(request.firstname(), createdPerson.firstname());
        assertEquals(request.surname(), createdPerson.surname());
        assertEquals(request.email(), createdPerson.email());
        assertEquals(request.phone(), createdPerson.phone());

        PersonAddress createdAddress = createdPerson.address();

        assertNotNull(createdAddress);
        assertEquals(addressRequest.city(), createdAddress.getCity());
        assertEquals(addressRequest.street(), createdAddress.getStreet());
        assertEquals(addressRequest.postCode(), createdAddress.getPostCode());
        assertEquals(addressRequest.buildingNumber(), createdAddress.getBuildingNumber());
        assertEquals(addressRequest.buildingFloor(), createdAddress.getBuildingFloor());
        assertEquals(addressRequest.doorCode(), createdAddress.getDoorCode());

        Pageable pageable = PageRequest.of(0, 5);

        assertEquals(1, personRepository.count());

        Page<PersonEntity> foundPersonsPage = personRepository.findAll(pageable);

        assertEquals(1, foundPersonsPage.getTotalElements());

        PersonEntity foundPerson = foundPersonsPage.getContent().get(0);

        assertEquals(foundPerson.getId().toString(), createdPerson.id());
        assertEquals(foundPerson.getFirstname(), createdPerson.firstname());
        assertEquals(foundPerson.getSurname(), createdPerson.surname());
        assertEquals(foundPerson.getEmail(), createdPerson.email());
        assertEquals(foundPerson.getPhone(), createdPerson.phone());
        assertEquals(foundPerson.getAddress(), createdPerson.address());
    }

    @Test
    public void shouldPatch(){

        //given
        UserEntity loggedUser = loadAccessTokenWithUser();

        PersonAddress personAddress = new PersonAddress(
            "Wrocław",
            "Street 123",
            "12-345",
            "12A",
            "2B",
            "20"
        );

        PersonEntity person = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("adam.kowalski@mail.com")
            .phone(PersonEntity.clearPhone("+48234567890"))
            .address(personAddress)
            .user(loggedUser)
            .build();

        person = personRepository.save(person);

        PatchAddressRequest addressRequest = new PatchAddressRequest(
            "Poznań",
            "Street 321",
            "54-321",
            "123A",
            "12",
            "16"
        );

        PatchPersonRequest request = new PatchPersonRequest(
            "Kamil",
            "Nowak",
            "kamil.nowak@mail.com",
            PersonEntity.clearPhone("+48 123 456 789"),
            addressRequest
        );

        //when
        PersonResponse changedPerson = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ testAccessToken)
            .when()
                .patch()
            .then()
                .statusCode(200)
                .extract()
                .as(PersonResponse.class);

        //then
        assertNotNull(changedPerson);
        assertEquals(person.getId().toString(), changedPerson.id());
        assertEquals(request.firstname(), changedPerson.firstname());
        assertEquals(request.surname(), changedPerson.surname());
        assertEquals(request.email(), changedPerson.email());
        assertEquals(request.phone(), changedPerson.phone());

        PersonAddress changedNewAddress = changedPerson.address();

        assertNotNull(changedNewAddress);
        assertEquals(addressRequest.city(), changedNewAddress.getCity());
        assertEquals(addressRequest.street(), changedNewAddress.getStreet());
        assertEquals(addressRequest.postCode(), changedNewAddress.getPostCode());
        assertEquals(addressRequest.buildingNumber(), changedNewAddress.getBuildingNumber());
        assertEquals(addressRequest.buildingFloor(), changedNewAddress.getBuildingFloor());
        assertEquals(addressRequest.doorCode(), changedNewAddress.getDoorCode());

        assertEquals(1, personRepository.count());

        Pageable pageable = PageRequest.of(0, 5);

        PersonEntity savedPerson = personRepository.findAll(pageable).getContent().get(0);

        assertEquals(savedPerson.getId().toString(), changedPerson.id());
        assertEquals(savedPerson.getFirstname(), changedPerson.firstname());
        assertEquals(savedPerson.getSurname(), changedPerson.surname());
        assertEquals(savedPerson.getEmail(), changedPerson.email());
        assertEquals(savedPerson.getPhone(), changedPerson.phone());
        assertEquals(savedPerson.getAddress(), changedPerson.address());
    }

    @Test
    public void shouldDeleteById(){

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

        PersonAddress personAddress = new PersonAddress(
            "Wrocław",
            "Street 123",
            "12-345",
            "12A",
            "2B",
            "20"
        );

        PersonEntity person = PersonEntity.builder()
            .firstname("Adam")
            .surname("Kowalski")
            .email("adam.kowalski@mail.com")
            .phone(PersonEntity.clearPhone("+48234567890"))
            .address(personAddress)
            .user(user)
            .build();

        person = personRepository.save(person);

        loadAccessTokenWithUser();

        //when
        RestAssured
            .given()
                .pathParam("personId", person.getId())
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testAccessToken)
            .when()
                .delete("/{personId}")
            .then()
                .statusCode(204);

        //then
        assertEquals(0, personRepository.count());
    }
}
